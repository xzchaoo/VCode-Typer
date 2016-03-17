package org.xzc.vcode.bilibiliregister;

import java.util.concurrent.ExecutorService;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.http.impl.client.BasicCookieStore;
import org.xzc.hc.HC;
import org.xzc.hc.Req;
import org.xzc.hc.util.HCs;
import org.xzc.vcode.manager.PositionManager;
import org.xzc.vcode.position.IPosition;
import org.xzc.vcode.worker.AbstractWorker;

public class BilibiliRegisterWorker extends AbstractWorker<byte[], String> {
	private final HC hc;
	private final BasicCookieStore bcs = new BasicCookieStore();

	private byte[] vcodeData;

	private String email;

	public BilibiliRegisterWorker(String tag, ExecutorService es, PositionManager<byte[], String> pm) {
		super( tag, es, pm );
		hc = HCs.makeHC( false, bcs );
	}

	public byte[] getData() {
		return vcodeData;
	}

	public void processWithPosition(IPosition<byte[], String> position) throws Exception {
		String yzm = position.getResult( this, String.format( "请输入验证码 [%s]", position.getTag() ) );
		pm.returnPosition( position );
		processWithVCodeAsync( yzm );
	}

	@Override
	protected void init() throws Exception {
		bcs.clear();
		email = RandomStringUtils.random( 10, true, false ).toLowerCase() + "@qq.com";
		vcodeData = hc.getAsByteArray( "http://passport.bilibili.com/captcha" );
	}

	@Override
	protected void processWithVCode(String yzm) {
		String content = hc.asString( Req.post( "http://passport.bilibili.com/register/mail" ).datas(
				"uname", email,
				"yzm", yzm,
				"agree", "1" ) );
		if (content.contains( "验证码错误" )) {
			System.out.println( "验证码错误" );
		} else if (content.contains( "邮件已发送" )) {
			System.out.println( "邮件已发送" );
		} else {
			System.out.println( content );
		}
	}

}
