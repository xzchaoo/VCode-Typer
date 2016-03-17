package org.xzc.vcode.position;

import org.xzc.vcode.manager.IVCodeReader;
import org.xzc.vcode.worker.IWorker;

public abstract class AbstractPosition<T> implements IPosition<T, String> {
	protected final String tag;
	protected final IVCodeReader cr;

	public AbstractPosition(String tag, IVCodeReader cr) {
		this.tag = tag;
		this.cr = cr;
	}

	public String getTag() {
		return tag;
	}

	public String getResult(IWorker<T, String> worker, String prompt) throws Exception {
		return cr.nextVCode( prompt );
	}

}
