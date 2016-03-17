package org.xzc.vcode.manager;

import java.util.LinkedList;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.xzc.vcode.position.IPosition;
import org.xzc.vcode.worker.IWorker;

public class PositionManager<T, U> implements IPositionManager<T, U> {

	private static final class BindedEntry<T, U> {
		public final IPosition<T, U> position;
		public final IWorker<T, U> worker;

		public BindedEntry(IPosition<T, U> position, IWorker<T, U> worker) {
			this.position = position;
			this.worker = worker;
		}
	}

	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger( PositionManager.class );

	private Object unbindedLock = new Object();

	private LinkedList<IWorker<T, U>> unbinededworkerList = new LinkedList<IWorker<T, U>>();
	private LinkedList<IPosition<T, U>> unbindedPositionList = new LinkedList<IPosition<T, U>>();
	private LinkedBlockingQueue<BindedEntry<T, U>> bindedPositionList = new LinkedBlockingQueue<BindedEntry<T, U>>();

	private ContinueDecider cd;
	private long timeout = 1000;
	
	@SuppressWarnings("unused")
	private PositionManagerStatistics statistics = new PositionManagerStatistics();

	public PositionManager(ContinueDecider cd) {
		this.cd = cd;
	}

	/**
	 * 尝试为该worker分配一个 position , 如果暂时没有可用的 position , 那么就先加入到一个等待队列里
	 */
	public void bindWorker(IWorker<T, U> worker) {
		synchronized (unbindedLock) {
			IPosition<T, U> p = unbindedPositionList.poll();
			if (p != null) {
				bindAndDisplay( p, worker );
			} else {
				unbinededworkerList.offer( worker );
			}
		}
	}

	public void loop() throws Exception {
		while (cd.canContinue()) {
			processNext();
		}
	}

	private ExceptionHandler eh = ExceptionHandler.NO_OP;

	public boolean processNext() {
		//拿到一个已经绑定的p
		BindedEntry<T, U> be = null;
		try {
			be = bindedPositionList.poll( timeout, TimeUnit.MILLISECONDS );
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		if (be != null) {
			try {
				be.worker.processWithPosition( be.position );
				return true;
			} catch (Exception e) {
				eh.handle( Where.PROCESS_WITH_POSITION, e );
			}
		}
		return false;
	}

	/**
	 * 归还该 position
	 * 1. 检查是否有待绑定的 worker , 如果有就分给它, 因为这肯定说明此事 可用 position 为空
	 * 2. 如果没有就放到带绑定队列里
	 * 
	 * @param position
	 */
	public void returnPosition(IPosition<T, U> position) {
		synchronized (unbindedLock) {
			IWorker<T, U> worker = unbinededworkerList.poll();
			if (worker != null) {
				bindAndDisplay( position, worker );
			} else {
				unbindedPositionList.offer( position );
			}
		}
	}

	/**
	 * 将 position 和 worker 绑定起来
	 * @param position
	 * @param worker
	 */
	private void bindAndDisplay(IPosition<T, U> position, IWorker<T, U> worker) {
		try {
			//让 position 先显示数据
			position.display( worker );
			bindedPositionList.add( new BindedEntry<T, U>( position, worker ) );
		} catch (Exception e) {
			eh.handle( Where.BIND_AND_DISPLAY, e );
			//绑定的时候发生了异常
			e.printStackTrace();
			//重新初始化worker
			worker.initAsync();
			//归还position
			returnPosition( position );
		}
	}

}
