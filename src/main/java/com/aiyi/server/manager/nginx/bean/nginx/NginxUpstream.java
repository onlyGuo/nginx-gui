package com.aiyi.server.manager.nginx.bean.nginx;

import java.util.List;

/**
 * Nginx反向代理容器
 * @author guoshengkai
 *
 */
public class NginxUpstream {

	private String name;
	
	private String value;
	
	private String desp;
	
	private List<NginxUpstreamItem> items;
	
	private int itemsLength;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDesp() {
		return desp == null ? "-" : desp;
	}

	public void setDesp(String desp) {
		this.desp = desp;
	}

	public List<NginxUpstreamItem> getItems() {
		return items;
	}

	public void setItems(List<NginxUpstreamItem> items) {
		this.items = items;
	}

	@Override
	public String toString() {
		return "NginxUpstream [name=" + name + ", value=" + value + ", desp=" + desp + ", items=" + items + "]";
	}

  public int getItemsLength() {
    if (0 == itemsLength) {
      if (items != null) {
        itemsLength = items.size();
      }
    }
    return itemsLength;
  }

  public void setItemsLength(int itemsLength) {
    this.itemsLength = itemsLength;
  }

public String getValue() {
	return value;
}

public void setValue(String value) {
	this.value = value;
}
	
	
}
