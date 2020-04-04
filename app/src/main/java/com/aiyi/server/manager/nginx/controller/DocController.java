package com.aiyi.server.manager.nginx.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 操作说明页面
 * @author guo
 *
 */
@Controller
@RequestMapping("admin/doc")
public class DocController {

	/**
	 * 页面主页
	 * @return
	 */
	@RequestMapping("/")
	public String index() {
		return "admin/doc/index";
	}
}
