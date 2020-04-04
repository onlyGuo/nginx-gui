package com.aiyi.server.manager.nginx.beam;

import java.util.ArrayList;
import java.util.List;

/**
 * Nginx 监听配置信息承载类
 * @Project : nginx-gui
 * @Program Name : com.aiyi.server.manager.nginx.beam.NginxServer.java
 * @Description : 
 * @Author : 郭胜凯
 * @Creation Date : 2018年2月27日 下午5:01:33
 * @ModificationHistory Who When What ---------- ------------- -----------------------------------
 *                      郭胜凯 2018年2月27日 create
 */
public class NginxServer {

	/**
	 * 监听端口
	 */
	private int port;
	
	/**
	 * 监听域名
	 */
	private String name;
	
	/**
	 * 监听日志
	 */
	private String accessLog;
	
	/**
	 * 统一错误页面
	 */
	private List<NginxErrorPage> errorPage = new ArrayList<>();
	
	private String errorPageStr;

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAccessLog() {
		return accessLog;
	}

	public void setAccessLog(String accessLog) {
		this.accessLog = accessLog;
	}

	public List<NginxErrorPage> getErrorPage() {
		return errorPage;
	}

	public void setErrorPage(List<NginxErrorPage> errorPage) {
		this.errorPage = errorPage;
	}

	@Override
	public String toString() {
		return "NginxServer [port=" + port + ", name=" + name + ", accessLog=" + accessLog + ", errorPage=" + errorPage
				+ "]";
	}

	public String getErrorPageStr() {
		if (null == errorPageStr) {
			errorPageStr = "";
			for (NginxErrorPage nginxErrorPage : errorPage) {
				errorPageStr += nginxErrorPage.getPath() + ";";
			}
		}
		return errorPageStr;
	}

	public void setErrorPageStr(String errorPageStr) {
		this.errorPageStr = errorPageStr;
	}


	public boolean getErrorPageHash(){
		return errorPage.size() > 0;
	}
	
	
}
