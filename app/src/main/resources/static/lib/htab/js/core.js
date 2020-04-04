var Htab = {
	conf:{
	},
	pages:[],
	iframes:[],
	activeTab:null,
	init:function(params){
		if(!params){
			throw new Error("note find params!");
		}
		this.conf = params;
		
		var style = "";
		if(this.conf.width){
			style += "width:" + this.conf.width + ";";
		}
		if(this.conf.height){
			style += "height:" + this.conf.height + ";";
		}
		
		var subDiv = document.createElement("div");
		subDiv.setAttribute("class", "hui-tab close iframe");
		subDiv.style = style;
		subDiv.innerHTML = '<ul class="hui-tab-list"></ul><div class="hui-tab-content"></div>';
		this.conf.subDiv = subDiv;
		
		this.conf.div.appendChild(subDiv);
		return this;
	},
	open:function(id, src, title){
		var have = false;
		for(var i in this.pages){
			if(this.pages[i].id == id){
				have = true;
				this.activeTab = this.pages[i];
				this.pages[i].div.setAttribute("class", "active");
				//显示这个iframe
				for(var ifr in this.iframes){
					if(this.iframes[ifr].id == "tab-id=" + id){
						this.iframes[ifr].style="display:block;";
					}else{
						this.iframes[ifr].style="display:none;";
					}
				}
			}else{
				this.pages[i].div.setAttribute("class", "");
			}
		}
		if(have){
			return;
		}
		
		//没有这个窗口则创建
		var list = this.conf.div.getElementsByClassName("hui-tab-list")[0];
		
		var item = document.createElement("li");
		item.setAttribute("tab-id", id);
		item.setAttribute("href", src);
		item.innerHTML = '<span>' + title + '</span>' + 
						'<span class="close">×</span>';
		item.setAttribute("class", "active");
		
		var T = this;
		var cl = false;
		item.onclick = function(){
			if(cl){
				return;
			}
			T.open(this.getAttribute("tab-id"));
		};
		item.getElementsByClassName("close")[0].onclick = function(){
			T.close(this.parentNode.getAttribute("tab-id"));
			cl = true;
		};
		
		//隐藏之前的iFrame
		for(var i in this.pages){
			this.pages[i].div.setAttribute("class", "");
		}
		
		for(var ifr in this.iframes){
			this.iframes[ifr].style="display:none;";
		}
		
		//载入新的内容
		list.appendChild(item);
		
		//这个Div添加到容器中
		var pa = {
			id:id,
			div:item
		};
		this.pages.push(pa);
		this.activeTab = pa;
		
		//创建新的iframe
		var ifr = document.createElement("iframe");
		ifr.setAttribute("id", "tab-id=" + id);
		ifr.setAttribute("src", src);
		var conts = this.conf.subDiv.getElementsByClassName("hui-tab-content");
		conts[0].appendChild(ifr);
		
		//添加到容器
		this.iframes.push(ifr);
	},
	addDefault:function(id, src, title){
		//没有这个窗口则创建
		var list = this.conf.div.getElementsByClassName("hui-tab-list")[0];
		
		var item = document.createElement("li");
		item.setAttribute("tab-id", id);
		item.setAttribute("href", src);
		item.innerHTML = '<span>' + title + '</span>' ;
		item.setAttribute("class", "active");
		
		var T = this;
		var cl = false;
		item.onclick = function(){
			if(cl){
				return;
			}
			T.open(this.getAttribute("tab-id"));
		};
		
		//隐藏之前的iFrame
		for(var i in this.pages){
			this.pages[i].div.setAttribute("class", "");
		}
		
		for(var ifr in this.iframes){
			this.iframes[ifr].style="display:none;";
		}
		
		//载入新的内容
		list.appendChild(item);
		
		//这个Div添加到容器中
		var pa = {
			id:id,
			div:item
		};
		this.pages.push(pa);
		this.activeTab = pa;
		
		//创建新的iframe
		var ifr = document.createElement("iframe");
		ifr.setAttribute("id", "tab-id=" + id);
		ifr.setAttribute("src", src);
		var conts = this.conf.subDiv.getElementsByClassName("hui-tab-content");
		conts[0].appendChild(ifr);
		
		//添加到容器
		this.iframes.push(ifr);
	},
	close:function(id){
		var nextTab = this.activeTab;
		for(var i in this.pages){
			if(this.pages[i].id == id){
				//删除这个Tab
				this.pages[i].div.parentNode.removeChild(this.pages[i].div);
				//删除这个Tab对应的Iframe
				for(var ifr in this.iframes){
					if(this.iframes[ifr].id == ("tab-id=" + id)){
						this.iframes[ifr].parentNode.removeChild(this.iframes[ifr]);
						this.iframes.splice(ifr, 1);
					}
				}
				//显示别的Tab
				if(nextTab && nextTab != this.pages[i]){
					this.open(nextTab.id);
				}else{
					if(this.pages.length > i + 1){
						this.open(this.pages[parseInt(i) + 1].id);
					}
				}
				
				this.pages.splice(i, 1);
			}
			nextTab = this.pages[i];
		}
	}
};
