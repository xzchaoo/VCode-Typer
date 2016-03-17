package org.xzc.vcode.manager;

import java.util.Scanner;

public class VCodeReader implements IVCodeReader {
	private final Scanner scanner;

	public VCodeReader() {
		scanner = new Scanner( System.in );
	}

	public String nextVCode(String prompt) {
		System.out.println( prompt );
		return scanner.nextLine();
	}

}
