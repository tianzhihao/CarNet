package com.server.car.domain;

import java.io.Serializable;

public class Car implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int cid;
	private int uid;
	private String brand;
	private String version;
	private String carnb;
	private String enginenb;
	private String level;
	private int distance;
	private int oil;
	private int engine;
	private int speed;
	private int light;
	public int getCid() {
		return cid;
	}
	public void setCid(int cid) {
		this.cid = cid;
	}
	public int getUid() {
		return uid;
	}
	public void setUid(int uid) {
		this.uid = uid;
	}
	public String getBrand() {
		return brand;
	}
	public void setBrand(String brand) {
		this.brand = brand;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getCarnb() {
		return carnb;
	}
	public void setCarnb(String carnb) {
		this.carnb = carnb;
	}
	public String getEnginenb() {
		return enginenb;
	}
	public void setEnginenb(String enginenb) {
		this.enginenb = enginenb;
	}
	public String getLevel() {
		return level;
	}
	public void setLevel(String level) {
		this.level = level;
	}
	public int getDistance() {
		return distance;
	}
	public void setDistance(int distance) {
		this.distance = distance;
	}
	public int getOil() {
		return oil;
	}
	public void setOil(int oil) {
		this.oil = oil;
	}
	public int getEngine() {
		return engine;
	}
	public void setEngine(int engine) {
		this.engine = engine;
	}
	public int getSpeed() {
		return speed;
	}
	public void setSpeed(int speed) {
		this.speed = speed;
	}
	public int getLight() {
		return light;
	}
	public void setLight(int light) {
		this.light = light;
	}
	@Override
	public String toString() {
		return "Car [cid=" + cid + ", uid=" + uid + ", brand=" + brand
				+ ", version=" + version + ", carnb=" + carnb + ", enginenb="
				+ enginenb + ", level=" + level + ", distance=" + distance
				+ ", oil=" + oil + ", engine=" + engine + ", speed=" + speed
				+ ", light=" + light + "]";
	}
	
}
