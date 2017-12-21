package com.hgs.gpsserver.common;

public enum MsgCommonResult {
	Success(0),
	Failure(1),
	MsgExistError(2),
	NotSupport(3),
	AlarmHandleConfirm(4);
	
	private int value;
	
	private MsgCommonResult(int value) {
		this.value = value;
	}
	
	public int getValue() {
		return value;
	}
	
	public static MsgCommonResult parseValue(int value) {
		for (MsgCommonResult item : values()) {
			if (item.value == value) {
				return item;
			}
		}
		return Success;
	}
}
