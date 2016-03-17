package org.xzc.vcode.worker;

import org.xzc.vcode.position.IPosition;

public interface IWorker<T, U> {
	enum State {
		RUNNING, STOP
	}

	/**
	 * 该worker的tag
	 * @return
	 */
	String getTag();

	/**
	 * 异步初始化该worker
	 */
	void initAsync();

	/**
	 * 获得该worker的状态
	 * @return
	 */
	State getState();

	/**
	 * 现在要从position里获取验证码
	 * @param position
	 * @throws Exception
	 */
	void processWithPosition(IPosition<T, U> position) throws Exception;

	void processWithVCodeAsync(U yzm);

	T getData();
}
