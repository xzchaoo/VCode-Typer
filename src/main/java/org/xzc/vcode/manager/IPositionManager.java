package org.xzc.vcode.manager;

import org.xzc.vcode.position.IPosition;
import org.xzc.vcode.worker.IWorker;

public interface IPositionManager<T, U> {
	/**
	 * worker 请求绑定一个 position
	 */
	void bindWorker(IWorker<T, U> worker);

	/**
	 * 归还一个position
	 * @param position
	 */
	void returnPosition(IPosition<T, U> position);

	/**
	 * 让pm处理下一个 位置/工作者 对
	 * @return
	 * @throws Exception 
	 */
	boolean processNext() throws Exception;
}
