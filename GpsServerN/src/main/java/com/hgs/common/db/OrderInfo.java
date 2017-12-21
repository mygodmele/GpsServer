package com.hgs.common.db;

public class OrderInfo{
	private Integer id;
	private String orderNum;
	private Integer passengerId;
	private Integer taxiId;
	private String orderCreateTime;
	private String orderState;
	private String orderEndTime;
	private Float drivingMileage;
	private Float totalFee;
	private String startPosition;
	private String destination;

	public OrderInfo(){
	}

	public OrderInfo(Integer id){
		this.id = id;
	}

	public void setId(Integer value) {
		this.id = value;
	}
	
	public Integer getId() {
		return this.id;
	}
	public void setOrderNum(String value) {
		this.orderNum = value;
	}
	
	public String getOrderNum() {
		return this.orderNum;
	}
	public void setPassengerId(Integer value) {
		this.passengerId = value;
	}
	
	public Integer getPassengerId() {
		return this.passengerId;
	}
	public void setTaxiId(Integer value) {
		this.taxiId = value;
	}
	
	public Integer getTaxiId() {
		return this.taxiId;
	}
	public void setOrderCreateTime(String value) {
		this.orderCreateTime = value;
	}
	
	public String getOrderCreateTime() {
		return this.orderCreateTime;
	}
	public void setOrderState(String value) {
		this.orderState = value;
	}
	
	public String getOrderState() {
		return this.orderState;
	}
	public void setOrderEndTime(String value) {
		this.orderEndTime = value;
	}
	
	public String getOrderEndTime() {
		return this.orderEndTime;
	}
	public void setDrivingMileage(Float value) {
		this.drivingMileage = value;
	}
	
	public Float getDrivingMileage() {
		return this.drivingMileage;
	}
	public void setTotalFee(Float value) {
		this.totalFee = value;
	}
	
	public Float getTotalFee() {
		return this.totalFee;
	}

	public String getStartPosition() {
		return startPosition;
	}

	public void setStartPosition(String startPosition) {
		this.startPosition = startPosition;
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

}

