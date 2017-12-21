package com.hgs.gpsserver.common;

public enum InputMessageType {
	Invalid(0), 
	Hands(0xB1), 
	GpsData(0x80),
	TaxiState(0x91),
	HeartBeat(0x0002),
	Register(0x0100),
	JT808GpsData(0x0200),
	Authentication(0x0102),
	UnRegister(0x0003),
	DeviceCommonResponse(0x0001),
	DZOrderResponse(0x0F01),
	DZGrabOrderReport(0x0F02);

	private int value;

	private InputMessageType(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}

	
	public static InputMessageType parseValue(int value, String ownerId) {
		for (InputMessageType item : values()) {
			if (item.value == value) {
				return item;
			}
		}
		return Invalid;
	}

}
