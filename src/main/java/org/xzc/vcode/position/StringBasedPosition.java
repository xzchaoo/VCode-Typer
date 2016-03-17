package org.xzc.vcode.position;

import org.xzc.vcode.manager.IVCodeReader;
import org.xzc.vcode.worker.IWorker;

public class StringBasedPosition extends AbstractPosition<String> {
	public StringBasedPosition(String tag, IVCodeReader cr) {
		super( tag, cr );
	}

	public void display(IWorker<String, String> worker) throws Exception {
		System.out.println( String.format( "位置[%s] %s", getTag(), worker.getData() ) );
	}

}
