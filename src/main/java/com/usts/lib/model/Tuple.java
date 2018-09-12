package com.usts.lib.model;

public class Tuple {

	public Tuple(Object first, Object second) {
		this.first = first;
		this.second = second;
	}

	private Object first;

	public Object getFirst() {
		return first;
	}

	public void setFirst(Object first) {
		this.first = first;
	}

	public Object getSecond() {
		return second;
	}

	public void setSecond(Object second) {
		this.second = second;
	}

	private Object second;

	@Override
	public String toString() {
		return "" + first;
	}
	
}
