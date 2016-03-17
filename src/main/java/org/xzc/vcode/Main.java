package org.xzc.vcode;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.xzc.vcode.manager.ContinueDecider;
import org.xzc.vcode.manager.IVCodeReader;
import org.xzc.vcode.manager.PositionManager;
import org.xzc.vcode.manager.VCodeReader;
import org.xzc.vcode.position.StringBasedPosition;

public class Main {
	public static void main(String[] args) throws Exception {
		int batch = 4;

		final IVCodeReader cr = new VCodeReader();
		PositionManager<String, String> pm = new PositionManager<String, String>( ContinueDecider.ALWAYS);
		for (int i = 0; i < batch; ++i)
			pm.returnPosition( new StringBasedPosition( "p" + i, cr ) );

		ExecutorService es = Executors.newFixedThreadPool( batch );
		for (int i = 0; i < batch * 2; ++i) {
			SimpleStringWorker worker = new SimpleStringWorker( "w" + i, es, pm );
			worker.initAsync();
		}
		pm.loop();
		es.shutdown();
		es.awaitTermination( 1, TimeUnit.HOURS );
	}
}
