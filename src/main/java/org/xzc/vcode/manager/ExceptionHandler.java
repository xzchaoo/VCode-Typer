package org.xzc.vcode.manager;

public interface ExceptionHandler {
	void handle(Where processWithPosition, Exception e);

	ExceptionHandler NO_OP = new ExceptionHandler() {
		public void handle(Where processWithPosition, Exception e) {
		}
	};
}
