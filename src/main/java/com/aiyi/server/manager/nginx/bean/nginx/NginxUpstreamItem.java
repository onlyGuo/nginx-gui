package com.aiyi.server.manager.nginx.bean.nginx;

/**
 * Nginx反向代理容器参数
 * @author guoshengkai
 *
 */
public class NginxUpstreamItem {

	/**
	 * 代理地址
	 */
	private String address;
	
	/**
	 * 权重
	 */
	private int weight;

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public int getWeight() {
		return weight;
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}
	
	@Override
	public String toString() {
		return "server " + address + " weight=" + weight + ";";
	}
}
