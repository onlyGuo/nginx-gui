> 这是一个免费且功能齐全的GPT：http://link.fuckicoding.com
> [https://onlyguo.github.io/chatgpt-mirror/](https://onlyguo.github.io/chatgpt-mirror/)


> Nginx GUI 项目恢复开发，已新建2.0的文件夹： https://github.com/onlyGuo/nginx-gui-2
> 
> Nginx GUI 2.0 将是一个从头构建的全新本本，将会使用自有的轻量级解库完成对conf文件的操作：https://github.com/onlyGuo/nginx-conf-analysis


<p align="center">
  The nginx GUI makes maintenance easy
</p>

<p align="center">
  <a href="https://github.com/996icu/996.ICU/blob/master/LICENSE">
    <img alt="996icu" src="https://img.shields.io/badge/license-NPL%20(The%20996%20Prohibited%20License)-blue.svg">
  </a>

  <a href="https://github.com/onlyGuo/nginx-gui/blob/master/LICENSE">
    <img alt="code style" src="https://img.shields.io/github/license/onlyGuo/nginx-gui.svg?style=popout">
  </a>
</p>

## Download
### Builder release-1.6
If you want to [download](https://github.com/onlyGuo/nginx-gui/releases/tag/1.6) this package, please go to this link: [https://github.com/onlyGuo/nginx-gui/releases/tag/1.6](https://github.com/onlyGuo/nginx-gui/releases/tag/1.6)
## Docker
感谢[CrazyLeoJay](https://github.com/CrazyLeoJay)提供的Docker版本， 分支地址：
[gradle-master](https://github.com/onlyGuo/nginx-gui/tree/gradle-master)
[CrazyLeoJay/nginx-gui](https://github.com/CrazyLeoJay/nginx-gui)

## Docker Hub

ducker hub https://hub.docker.com/r/crazyleojay/nginx_ui

 拉取镜像：

```dockerfile
docker pull crazyleojay/nginx_ui
```

run

```dockerfile
docker run --detach \
--publish 80:80 --publish 8889:8889 \
--name nginx_ui \
--restart always \
crazyleojay/nginx_ui:latest
```



持久化：

配置文件路径：`/usr/local/nginx/conf/nginx.conf`

开发者可以自行配置。

```dockerfile
docker run --detach \
--publish 80:80 --publish 8889:8889 \
--name nginx_ui \
--restart always \
--volume /home/nginx.conf:/usr/local/nginx/conf/nginx.conf \
crazyleojay/nginx_ui:latest
```



### China download node
这里提供了国内下载节点， 如果您无法通过以上连接下载release包，可以尝试从下方连接下载(但您需要支付流量费用)：
- [Nginx-GUI-For-Linux_X64_v1.6.zip](http://disk.321aiyi.com/share/b88e02f8aca04cdd8ce3a1fb02499e79)
- [Nginx-GUI-For-Linux_X86_v1.6.zip](http://disk.321aiyi.com/share/6b945535bfc0437bb2b91ff2fa2f97b1)
- [Nginx-GUI-For-Mac_v1.6.zip](http://disk.321aiyi.com/share/95075b8f92bb49c297085cba9c1c89a9)
- [Nginx-GUI-For-Windows_x64_v1.6.zip](http://disk.321aiyi.com/share/235943a302e140a4b69b005f4874446e)
- [国内节点采用的云盘项目开源地址](https://github.com/onlyGuo/disk)


## New idea
If you like algorithms, you can implement them [here](https://github.com/onlyGuo/nginx-conf-analysis).  
In the future, it will be a nginx configuration file management tool library supporting complete modules and files.

## Quick start
1. Download the release package.
2. Unzip pachage to your {dir}.
3. Edit the {dir}/conf/conf.properties, set your nginx path.
4. Run {dir}/startup.sh or {dir}/startup.bat

## How to use source code?

1. If your system is Mac os or idea, please copy "conf.properties" to parent directory。

2. Please eidt "conf.properties", fill in your nginx path to "conf.properties".

3. Now, please experience!, the default account and pwssword is "admin".

## AC QQ Group
群号:933481759(已满)
群号:274862188

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
