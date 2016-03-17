package org.xzc.vcode.manager;

public class PositionManagerStatistics implements Cloneable {

	public int roundCount;

	public PositionManagerStatistics clone() {
		try {
			return (PositionManagerStatistics) super.clone();
		} catch (CloneNotSupportedException e) {
			throw new RuntimeException( e );
		}
	}

}
