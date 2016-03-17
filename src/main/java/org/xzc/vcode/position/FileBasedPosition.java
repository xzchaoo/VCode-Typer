package org.xzc.vcode.position;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.xzc.vcode.manager.IVCodeReader;
import org.xzc.vcode.worker.IWorker;

public class FileBasedPosition extends AbstractPosition<byte[]> {
	private File file;

	public FileBasedPosition(String tag, File file, IVCodeReader cr) {
		super( tag, cr );
		this.file = file;
	}

	public void display(IWorker<byte[], String> worker) throws Exception {
		FileUtils.writeByteArrayToFile( file, worker.getData() );
	}

}
