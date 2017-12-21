package com.hgs.gpsserver.common;

public enum OutputMessageType {
	Invalid(0),
	Hands(0xB1),
	GpsData(0x80),
	ChangeGpsTimeInteval(0x34),
	TaxiState(0x91),
	Register(0x8100),
	Platform(0x8001),
	TextMessage(0x8300),
	DZGrabOrderResp(0x8002),
	PublishOrder(0x8F01);
	

	private int value;

	private OutputMessageType(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}

	public static OutputMessageType parseValue(int value) {
		for (OutputMessageType item : values()) {
			if (item.value == value) {
				return item;
			}
		}
		return Invalid;
	}
}
