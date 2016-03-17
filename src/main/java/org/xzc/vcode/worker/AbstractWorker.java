package org.xzc.vcode.worker;

import java.util.concurrent.ExecutorService;

import org.xzc.vcode.manager.PositionManager;

public abstract class AbstractWorker<T, U> implements IWorker<T, U> {
	protected final String tag;
	protected final ExecutorService es;
	protected final PositionManager<T, U> pm;

	public ExecutorService getExecutorService() {
		return this.es;
	}

	public AbstractWorker(String tag, ExecutorService es, PositionManager<T,U> pm) {
		this.tag = tag;
		this.es = es;
		this.pm = pm;
	}

	public String getTag() {
		return tag;
	}

	protected void onInitFail(Exception e) {
		e.printStackTrace();
		initAsync();
	}

	public void initAsync() {
		es.execute( new Runnable() {
			public void run() {
				try {
					init();
					pm.bindWorker( AbstractWorker.this );
				} catch (Exception e) {
					onInitFail( e );
				}
			}
		} );
	}

	protected abstract void init() throws Exception;

	public State getState() {
		return State.RUNNING;
	}

	protected abstract void processWithVCode(final U yzm);

	public void processWithVCodeAsync(final U yzm) {
		getExecutorService().execute( new Runnable() {
			public void run() {
				processWithVCode( yzm );
				initAsync();
			}
		} );
	}

}
