package org.xzc.vcode.manager;

public interface ContinueDecider {
	boolean canContinue();

	ContinueDecider ALWAYS = new ContinueDecider() {
		public boolean canContinue() {
			return true;
		}
	};
}
