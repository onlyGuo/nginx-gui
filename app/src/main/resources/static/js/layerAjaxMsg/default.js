/**
 * Ajax请求错误处理的默认方式（通过Layer抛出问题）
 */
//初始化Ajax
H.errorFun = function(res){
	layui.use('form', function() {
		var layer = layui.layer;
		var msg;
		if(res.code == 'NGINX_WORK_EXCEPTION'){
			msg = "操作失败";
		}else if(res.code == 'SERVER_ERROR'){
			msg = "内部错误";
		}else if(res.code == 'VALI_ERROR'){
			msg = "参数错误";
		}else{
			//msg = "未知错误";
			if(res.message){
				layer.msg(res.message);
			}else{
				layer.msg("未知错误");
			}
			return;
		}
		msg += "," + res.message;
		layer.msg(msg);
	});
};