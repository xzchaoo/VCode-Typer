package org.xzc.vcode.bilibiliregister;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.xzc.vcode.manager.ContinueDecider;
import org.xzc.vcode.manager.IVCodeReader;
import org.xzc.vcode.manager.PositionManager;
import org.xzc.vcode.manager.VCodeReader;
import org.xzc.vcode.position.FileBasedPosition;

public class Main {
	public static void main(String[] args) throws Exception {
		int batch = 4;

		final IVCodeReader cr = new VCodeReader();

		//位置管理器
		PositionManager<byte[], String> pm = new PositionManager<byte[], String>( ContinueDecider.ALWAYS );
		
		//添加若干个位置
		for (int i = 0; i < batch; ++i)
			pm.returnPosition( new FileBasedPosition( "p" + i, new File( "vcode/vcode_" + i + ".png" ),
					cr ) );

		//线程池
		ExecutorService es = Executors.newFixedThreadPool( batch );

		//启动多个工作者
		for (int i = 0; i < batch * 2; ++i) {
			BilibiliRegisterWorker worker = new BilibiliRegisterWorker( "w" + i, es, pm );
			worker.initAsync();
		}

		//开始循环
		pm.loop();

		es.shutdown();
		es.awaitTermination( 1, TimeUnit.HOURS );
	}
}
