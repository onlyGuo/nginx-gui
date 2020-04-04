package com.aiyi.server.manager.nginx.beam;

import java.util.ArrayList;
import java.util.List;

/**
 * 统一错误页面配置
 * @Project : nginx-gui
 * @Program Name : com.aiyi.server.manager.nginx.beam.NginxErrorPage.java
 * @Description : 
 * @Author : 郭胜凯
 * @Creation Date : 2018年2月27日 下午4:56:53
 * @ModificationHistory Who When What ---------- ------------- -----------------------------------
 *                      郭胜凯 2018年2月27日 create
 */
public class NginxErrorPage {
	
	/**
	 * 页面状态码
	 */
	private List<String> status = new ArrayList<>();
	
	/**
	 * 跳转页面
	 */
	private String path;

	public List<String> getStatus() {
		return status;
	}

	public void setStatus(List<String> status) {
		this.status = status;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "NginxErrorPage [status = " + status.toString() + ", path = " + path + "]";
	}

	public String getStatusStr(){
		String str = "";
		for (String s:
			 status) {
			str += s + ",";
		}
		return  str.substring(0, str.length() - 1);
	}
	
}
