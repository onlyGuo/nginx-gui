package com.aiyi.server.manager.nginx.bean;

/**
 * 用户实体
 * @Project : nginx-gui
 * @Program Name : com.aiyi.server.manager.nginx.bean.User.java
 * @Description : 
 * @Author : 郭胜凯
 * @Creation Date : 2018年2月26日 上午9:41:50
 * @ModificationHistory Who When What ---------- ------------- -----------------------------------
 *                      郭胜凯 2018年2月26日 create
 */
public class User {

	private String username;
	
	private String password;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	
}
