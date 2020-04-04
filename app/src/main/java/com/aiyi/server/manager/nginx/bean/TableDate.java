package com.aiyi.server.manager.nginx.bean;

import java.util.ArrayList;

public class TableDate {

  private long size;
  
  private long start;
  
  private long length;
  
  private Object list = new ArrayList<>();

  public long getSize() {
    return size;
  }

  public void setSize(long size) {
    this.size = size;
  }

  public long getStart() {
    return start;
  }

  public void setStart(long start) {
    this.start = start;
  }

  public long getLength() {
    return length;
  }

  public void setLength(long length) {
    this.length = length;
  }

  public Object getList() {
    return list;
  }

  public void setList(Object list) {
    this.list = list;
  }
  
}
