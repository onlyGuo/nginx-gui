FROM  ubuntu:18.04
MAINTAINER leojay crazyleojay@163.com
#添加源
ADD docker/sources.list /etc/apt/sources.list
#安装jdk8
RUN apt-get update \
    && apt-get install -y openjdk-8-jre

# 配置依赖环境
RUN apt-get install -y ssh \
# 安装GCC 环境 该命令将安装一堆新包，包括gcc，g ++和make。
    && apt-get install -y build-essential \
    && apt-get install -y openssl \
    && apt-get install -y libssl-dev \
    && apt-get install -y libpcre3 \
    && apt-get install -y libpcre3-dev \
    && apt-get install -y zlib1g-dev

# 添加nginx源码，准备编译
ADD docker/nginx-1.16.1.tar.gz /nginx

WORKDIR /nginx/nginx-1.16.1/

RUN ./configure \
--with-stream \
--with-http_ssl_module \
--with-http_realip_module \
--with-http_stub_status_module

RUN make && make install && /usr/local/nginx/sbin/nginx

WORKDIR /
#导入 springboot 程序
#ADD build/libs/app-1.0.jar server.jar
ADD docker/app-1.6.jar server.jar
#导入配置
ADD docker/conf.properties conf.properties

#设置端口
EXPOSE 80
EXPOSE 8889

# 分别启动服务
CMD /usr/local/nginx/sbin/nginx && java -jar /server.jar
