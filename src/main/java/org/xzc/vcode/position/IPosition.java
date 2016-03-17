package org.xzc.vcode.position;

import org.xzc.vcode.worker.IWorker;

/**
 * 表示了一个位置
 * @author xzchaoo
 *
 * @param <T>
 */
public interface IPosition<T, U> {
	String getTag();

	/**
	 * 呈现验证码数据
	 * @param t
	 * @throws Exception 
	 */
	void display(IWorker<T, U> worker) throws Exception;

	U getResult(IWorker<T, U> worker, String prompt) throws Exception;

}
