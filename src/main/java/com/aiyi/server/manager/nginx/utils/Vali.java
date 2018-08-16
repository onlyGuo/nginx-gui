package com.aiyi.server.manager.nginx.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class Vali {

	public static boolean isEpt(Object obj) {
		if (null == obj) {
			return true;
		}
		if (obj instanceof String) {
			return "".equals(((String) obj).trim());
		}
		Class<? extends Object> classes = obj.getClass();
		Field[] fields = classes.getDeclaredFields();
		for (Field field : fields) {
			try {
				String get = "get";
				if(field.getClass().getName().equals(Boolean.class.getSimpleName()) || 
						field.getType().getSimpleName().equals(Boolean.TYPE.getSimpleName())) {
					get = "is";
				}
				String name = field.getName().substring(0, 1).toUpperCase() + field.getName().substring(1, field.getName().length());
				
				Method method = classes.getMethod(get + name);
				Object invoke = method.invoke(obj);
				if (invoke != null) {
					return false;
				}
			} catch (Exception e) {
				e.printStackTrace();
			} 
		}
		return true;
	}

	/**
	 * @param obj	等待校验的对象
	 * @Description : 表单是否为空
	 * @Creation Date : 2018/4/18 下午1:32
	 * @Author : 郭胜凯
	 */
	public static boolean isFormEpt(Object obj){
		boolean res = isEpt(obj);
		if (!res){
			res = "null".equals(obj) || "undefined".equals(obj);
		}
		return res;
	}
}
