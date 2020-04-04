/**
 * Ajax核心方法
 */
var H = {
		/**
		 * 默Http认错误处理方法
		 */
		errorFun:function(res){
			console.log(res);
		},
		/**
		 * 格式化参数
		 */
		formatParams:function(prams, collback, error){
			var coll;
			var par;
			var err;
			if(prams){
				if(typeof prams == 'function'){
					coll = prams;
					if(collback && typeof collback == 'function'){
						err = collback;
					}
				}else{
					par = prams;
					if(collback){
						coll = collback;
						if(error && typeof error == 'function'){
							err = error;
						}
					}else{
						coll = function(){};
					}
				}
			}
			
			if(!err){
				err = H.errorFun;
			}
			return {
				coll:coll,
				par:par,
				err:err
			};
		},
		/**
		 * 发送自定义Http请求
		 */
		sendHttp:function(url, param, collback, error, type){
			var xmlhttp = new XMLHttpRequest();
			
			var data = null;
			
			xmlhttp.onreadystatechange = function(){
				if (xmlhttp.readyState==4){
					if (xmlhttp.status==200){
						var result = xmlhttp.responseText;
						try{
							result = JSON.parse(xmlhttp.responseText)
						}catch(e){
							console.log("Not Parse Json, result:" + result);
						}
						collback(result);
					}else{
						var result = xmlhttp.responseText;
						try{
							result = JSON.parse(xmlhttp.responseText)
						}catch(e){
							console.log("Not Parse Json, result:" + result);
						}
						error(result);
					}
				}
			};
			
			xmlhttp.open(type,url,true);
			
			if(type == "PUT" || type == "DELETE" || type == "POST"){
				xmlhttp.setRequestHeader("Content-Type","application/json;charset=utf-8");
				if(param){
					data = JSON.stringify(param);
				}
			}else{
				if(param){
					data = new FormData();
					for(var i in param){
						data.append(i, param[i]);
					}
				}
			}
			
			try{
				xmlhttp.send(data);
			}catch(e){
				//50x,40x
			}
		},
		sendHttpTxt:function(url, param, collback, error, type){
			var xmlhttp = new XMLHttpRequest();
			
			var data = null;
			
			xmlhttp.onreadystatechange = function(){
				if (xmlhttp.readyState==4){
					if (xmlhttp.status==200){
						collback(xmlhttp.responseText);
					}else{
						error(xmlhttp.responseText);
					}
				}
			};
			
			xmlhttp.open(type,url,true);
			
			if(type == "PUT" || type == "DELETE" || type == "POST"){
				xmlhttp.setRequestHeader("Content-Type","application/json;charset=utf-8");
				if(param){
					data = JSON.stringify(param);
				}
			}else{
				if(param){
					data = new FormData();
					for(var i in param){
						data.append(i, param[i]);
					}
				}
			}
			
			try{
				xmlhttp.send(data);
			}catch(e){
				//50x,40x
			}
		},
		/**
		 * 发送get请求
		 */
		get:function(url, prams, collback, error){
			var param = H.formatParams(prams, collback, error);
			if(param.par){
				var p = "";
				for(var i in param.par){
					p += i + "=" + param.par[i] + "&";
				}
				p = encodeURI(p);
				url += "?" + p;
			}
			H.sendHttp(url, null, param.coll, param.err, 'GET');
		},
		getTxt:function(url, prams, collback, error){
			var param = H.formatParams(prams, collback, error);
			if(param.par){
				var p = "";
				for(var i in param.par){
					p += i + "=" + param.par[i] + "&";
				}
				p = encodeURI(p);
				url += "?" + p;
			}
			H.sendHttpTxt(url, null, param.coll, param.err, 'GET');
		},
		/**
		 * 发送post请求
		 */
		post:function(url, prams, collback, error){
			var param = H.formatParams(prams, collback, error);
			H.sendHttp(url, prams, param.coll, param.err, 'POST');
		},
		/**
		 * 发送put请求
		 */
		put:function(url, prams, collback, error){
			var param = H.formatParams(prams, collback, error);
			H.sendHttp(url, prams, param.coll, param.err, 'PUT');
		},
		/**
		 * 发送delete请求
		 */
		del:function(url, prams, collback, error){
			var param = H.formatParams(prams, collback, error);
			H.sendHttp(url, prams, param.coll, param.err, 'DELETE');
		}
		

};