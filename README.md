<p align="center">
  The nginx GUI makes maintenance easy
</p>

<p align="center">
  <a href="https://github.com/996icu/996.ICU/blob/master/LICENSE">
    <img alt="996icu" src="https://img.shields.io/badge/license-NPL%20(The%20996%20Prohibited%20License)-blue.svg">
  </a>

  <a href="https://www.apache.org/licenses/LICENSE-2.0">
    <img alt="code style" src="https://img.shields.io/github/license/onlyGuo/nginx-gui.svg?style=popout">
  </a>
</p>

## Download
### Builder release-1.0
- [Nginx-GUI-For-Mac-1.0.zip](http://aiyiupload.oss-cn-beijing.aliyuncs.com/blog/file/nginxgui/Nginx-GUI-For-Mac-1.0.zip) 
- [Nginx-GUI-For-Linux-1.0.zip](http://aiyiupload.oss-cn-beijing.aliyuncs.com/blog/file/nginxgui/Nginx-GUI-For-Linux-1.0.zip) (这个包有个BUG, 解压并配置好conf.properties后, 将lib/bin/java_vms重命名为java_vms_nginx_gui即可. 另外, 这是64位版本, 32位系统请下载jdk1.8 X86, 解压jdk包, 将jre/lib和jre/bin这两个目录替换Nginx-GUI-For-Linux-1.0/lib路径下的两个目录, 再执行这个命令: `cp lib/bin/java lib/bin/java_vms_nginx_gui`即可)
- [Nginx-GUI-For-Windows-1.0.zip](http://aiyiupload.oss-cn-beijing.aliyuncs.com/blog/file/nginxgui/Nginx-GUI-For-Windows-1.0.zip) 

## Quick start
1. Download the release package.
2. Unzip pachage to your {dir}.
3. Edit the {dir}/conf/conf.properties, set your nginx path.
4. Run {dir}/startup.sh or {dir}/startup.bat

## How to use source code?

1. If your system is Mac os or idea, please copy "conf.properties" to parent directory。

2. Please eidt "conf.properties", fill in your nginx path to "conf.properties".

3. Now, please experience!, default account and pwssword is "admin".

## AC QQ Group
群号:933481759

## UI
![登录](https://raw.githubusercontent.com/onlyGuo/nginx-gui/master/doc/login.png)

![主页](https://raw.githubusercontent.com/onlyGuo/nginx-gui/master/doc/home.png)

![监听](https://raw.githubusercontent.com/onlyGuo/nginx-gui/master/doc/lisner.png)

![负载](https://raw.githubusercontent.com/onlyGuo/nginx-gui/master/doc/upstream.png)

![规则](https://raw.githubusercontent.com/onlyGuo/nginx-gui/master/doc/location.png)

![配置](https://raw.githubusercontent.com/onlyGuo/nginx-gui/master/doc/conf.png)

## LICENCE

[![LICENSE](https://img.shields.io/badge/license-Anti%20996-blue.svg)](https://github.com/996icu/996.ICU/blob/master/LICENSE)

[1]: https://github.com/oychao/riact/tree/master/demos
