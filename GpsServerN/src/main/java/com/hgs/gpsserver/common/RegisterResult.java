package com.hgs.gpsserver.common;

public enum RegisterResult {
	Failure(-1),
	Success(0),
	VehicleAlreadyRegister(1),
	VehicleNotExist(2),
	DeviceAlreadyRegister(3),
	DeviceNotExist(4);
	
	private int value;
	private RegisterResult(int value) {
		this.value = value;
	}
	
	public int getValue() {
		return value;
	}
	
	public static RegisterResult parseValue(int value) {
		for (RegisterResult item : values()) {
			if (item.value == value) {
				return item;
			}
		}
		return Success;
	}
}
