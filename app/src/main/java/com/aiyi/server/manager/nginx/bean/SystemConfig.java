package com.aiyi.server.manager.nginx.bean;

/**
 * 系统信息
 * @author guo
 *
 */
public class SystemConfig {

	/**
	 * 计算机名
	 */
	private String name;
	
	/**
	 * 系统名称
	 */
	private String osName;
	
	/**
	 * 系统版本
	 */
	private String osVersion;
	
	/**
	 * 系统架构
	 */
	private String osArch;
	
	/**
	 * 登录用户
	 */
	private String userName;
	
	/**
	 * 用户主目录
	 */
	private String userHome;
	
	/**
	 * 计算机所在域
	 */
	private String domain;
	
	/**
	 * IP地址
	 */
	private String ip;
	
	/**
	 * Mac地址
	 */
	private String mac;
	
	/**
	 * Java版本
	 */
	private String javaVersion;
	
	/**
	 * java提供商
	 */
	private String javaVendor;
	
	/**
	 * Java安装路径
	 */
	private String javaHome;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getOsName() {
		return osName;
	}

	public void setOsName(String osName) {
		this.osName = osName;
	}

	public String getOsVersion() {
		return osVersion;
	}

	public void setOsVersion(String osVersion) {
		this.osVersion = osVersion;
	}

	public String getOsArch() {
		return osArch;
	}

	public void setOsArch(String osArch) {
		this.osArch = osArch;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserHome() {
		return userHome;
	}

	public void setUserHome(String userHome) {
		this.userHome = userHome;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getMac() {
		return mac;
	}

	public void setMac(String mac) {
		this.mac = mac;
	}

	public String getJavaVersion() {
		return javaVersion;
	}

	public void setJavaVersion(String javaVersion) {
		this.javaVersion = javaVersion;
	}

	public String getJavaVendor() {
		return javaVendor;
	}

	public void setJavaVendor(String javaVendor) {
		this.javaVendor = javaVendor;
	}

	public String getJavaHome() {
		return javaHome;
	}

	public void setJavaHome(String javaHome) {
		this.javaHome = javaHome;
	}
	
}
