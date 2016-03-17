package org.xzc.vcode;

import java.util.Random;
import java.util.concurrent.ExecutorService;

import org.xzc.vcode.manager.PositionManager;
import org.xzc.vcode.position.IPosition;
import org.xzc.vcode.worker.AbstractWorker;

public class SimpleStringWorker extends AbstractWorker<String, String> {

	public SimpleStringWorker(String tag, ExecutorService es, PositionManager<String, String> pm) {
		super( tag, es, pm );
	}

	private String question;
	private String myAnswer;

	private static final Random r = new Random();

	protected void init() throws Exception {
		System.out.println( "初始化" + getTag() );
		int a = r.nextInt( 10 );
		int b = r.nextInt( 10 );
		question = String.format( "%d+%d=?", a, b );
		myAnswer = Integer.toString( a + b );
	}

	@Override
	protected void processWithVCode(String yzm) {
		if (yzm.equals( myAnswer )) {
			System.out.println( "答案是" + myAnswer + ", 你答对了." );
		} else {
			System.out.println( "答案是" + myAnswer + ", 你的答案是" + yzm + ", 你答错了." );
		}
		question = null;
		myAnswer = null;
		initAsync();
	}

	public void processWithPosition(IPosition<String, String> position) throws Exception {
		String yzm = position.getResult( this, String.format( "请输入验证码 [%s]", question ) );
		pm.returnPosition( position );
		processWithVCodeAsync( yzm );
	}

	public String getData() {
		return question;
	}

}
