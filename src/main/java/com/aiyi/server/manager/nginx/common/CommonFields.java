package com.aiyi.server.manager.nginx.common;

/**
 * 公共字段
 * @Project : nginx
 * @Program Name : com.aiyi.server.manager.nginx.common.CommonFields
 * @Description : 
 * @Author : 郭胜凯
 * @Creation Date : 2018年2月1日 下午4:56:28
 * @ModificationHistory Who When What ---------- ------------- -----------------------------------
 *                      郭胜凯 2018年2月1日 create
 */
public class CommonFields {
  
  
  /**
   * nginx名称
   */
  public static final String NGINX = "nginx";
  
  /**
   * Session存储的一些Key
   * @Project : nginx
   * @Program Name : com.aiyi.server.manager.nginx.common.SESSION_KEY
   * @Description : 
   * @Author : 郭胜凯
   * @Creation Date : 2018年2月2日 上午11:22:58
   * @ModificationHistory Who When What ---------- ------------- -----------------------------------
   *                      郭胜凯 2018年2月2日 create
   */
  public static final class SESSION_KEY{
    
    /**
     * 用户登陆Key
     */
    public static final String LOGIN_USER = "login_user";
  }
  
  /**
   * 错误码
   * @Project : nginx
   * @Program Name : com.aiyi.server.manager.nginx.common.ERROR_CODE
   * @Description : 
   * @Author : 郭胜凯
   * @Creation Date : 2018年2月2日 下午1:44:28
   * @ModificationHistory Who When What ---------- ------------- -----------------------------------
   *                      郭胜凯 2018年2月2日 create
   */
  public static final class ERROR_CODE{
    /**
     * Nginx操作异常错误码
     */
    public static final String NGINX = "NGINX_WORK_EXCEPTION";
    
    /**
     * 服务器错误码
     */
    public static final String SERVER = "SERVER_ERROR";
    
    /**
     * 参数校验错误码
     */
    public static final String VALIDATION = "VALI_ERROR";
  }
  
  /**
   * 路径配置
   * @Project : nginx
   * @Program Name : com.aiyi.server.manager.nginx.common.PATH
   * @Description : 
   * @Author : 郭胜凯
   * @Creation Date : 2018年1月29日 下午6:19:08
   * @ModificationHistory Who When What ---------- ------------- -----------------------------------
   *                      郭胜凯 2018年1月29日 create
   */
  public static final class PATH{
    /**
     * 公共路径占位付，该符号表示当前工程所在目录
     */
    public static final String FMT_CONTEXT = "${p}";
    
    /**
     * Nginx所在目录配置Key
     */
    public static final String PATH_NGINX = "nginx.path";
    
    /**
     * Nginx配置文件所在目录配置Key
     */
    public static final String PATH_NGINX_CONF = "nginx.config";
    
    /**
     * 公共路径占位付，该付宝表示Nginx所配路径
     */
    public static final String FMT_NGINX = "${n}";
  }

  /**
   * Nginx配置文件参数
   * @Project : nginx
   * @Program Name : com.aiyi.server.manager.nginx.common.PROP
   * @Description : 
   * @Author : 郭胜凯
   * @Creation Date : 2018年1月29日 下午6:03:48
   * @ModificationHistory Who When What ---------- ------------- -----------------------------------
   *                      郭胜凯 2018年1月29日 create
   */
  public static final class PROP{
    
    /**
     * 账号Key
     */
    public static final String ACCOUNT = "account";
    
    /**
     * 默认配置
     */
    public static final String DEFAULT = 
        "\r\n" + 
        "worker_processes  1;\r\n" + 
        "\r\n" + 
        "events {\r\n" + 
        "    worker_connections  1024;\r\n" + 
        "}\r\n" + 
        "\r\n" + 
        "\r\n" + 
        "http {\r\n" + 
        "    include       mime.types;\r\n" + 
        "    default_type  application/octet-stream;\r\n" + 
        "\r\n" + 
        "    sendfile        on;\r\n" + 
        "    keepalive_timeout  65;\r\n" + 
        "\r\n" + 
        "    #gzip  on;\r\n" + 
        "\r\n" + 
        "    server {\r\n" + 
        "        listen       80;\r\n" + 
        "        server_name  localhost;\r\n" + 
        "\r\n" + 
        "        #charset koi8-r;\r\n" + 
        "\r\n" + 
        "        #access_log  logs/host.access.log  main;\r\n" + 
        "\r\n" + 
        "        location / {\r\n" + 
        "            root   E:\\guoshengkai\\development\\git-respostory\\dcms_web\\2.0\\dist;\r\n" + 
        "            index  index.html index.htm;\r\n" + 
        "        }\r\n" + 
        "\r\n" + 
        "        #error_page  404              /404.html;\r\n" + 
        "\r\n" + 
        "        # redirect server error pages to the static page /50x.html\r\n" + 
        "        #\r\n" + 
        "        error_page   500 502 503 504  /50x.html;\r\n" + 
        "        location = /50x.html {\r\n" + 
        "            root   html;\r\n" + 
        "        }\r\n" + 
        "\r\n" + 
        "        location /v1/cif/ {\r\n" + 
        "            proxy_pass http://127.0.0.1:8081;\r\n" + 
        "            proxy_set_header  X-Real-IP        $remote_addr;\r\n" + 
        "            proxy_set_header  X-Forwarded-For  $proxy_add_x_forwarded_for;\r\n" + 
        "        }\r\n" + 
        "\r\n" + 
        "        location /v1/coll/ {\r\n" + 
        "            proxy_pass http://127.0.0.1:8082;\r\n" + 
        "            proxy_set_header  X-Real-IP        $remote_addr;\r\n" + 
        "            proxy_set_header  X-Forwarded-For  $proxy_add_x_forwarded_for;\r\n" + 
        "        }\r\n" + 
        "\r\n" + 
        "        location /v1/sec/ {\r\n" + 
        "            proxy_pass http://127.0.0.1:8085;\r\n" + 
        "            proxy_set_header  X-Real-IP        $remote_addr;\r\n" + 
        "            proxy_set_header  X-Forwarded-For  $proxy_add_x_forwarded_for;\r\n" + 
        "        }\r\n" + 
        "\r\n" + 
        "        location /v1/maint/ {\r\n" + 
        "            proxy_pass http://127.0.0.1:8083;\r\n" + 
        "            proxy_set_header  X-Real-IP        $remote_addr;\r\n" + 
        "            proxy_set_header  X-Forwarded-For  $proxy_add_x_forwarded_for;\r\n" + 
        "        }\r\n" + 
        "\r\n" + 
        "        location /v1/wf/ {\r\n" + 
        "            proxy_pass http://127.0.0.1:8084;\r\n" + 
        "            proxy_set_header  X-Real-IP        $remote_addr;\r\n" + 
        "            proxy_set_header  X-Forwarded-For  $proxy_add_x_forwarded_for;\r\n" + 
        "        }\r\n" + 
        "        \r\n" + 
        "        location /v1/facade/ {\r\n" + 
        "            proxy_pass http://127.0.0.1:8086;\r\n" + 
        "            proxy_set_header  X-Real-IP        $remote_addr;\r\n" + 
        "            proxy_set_header  X-Forwarded-For  $proxy_add_x_forwarded_for;\r\n" + 
        "        }\r\n" + 
        "\r\n" + 
        "        location /console/static/ {\r\n" + 
        "            alias D:\\\\guoshengkai\\\\development\\\\git-respostory\\\\console\\\\src\\\\main\\\\webapp\\\\static;\r\n" + 
        "            #alias  D:\\\\project\\\\console\\\\src\\\\main\\\\webapp\\\\static\\\\;\r\n" + 
        "            index  index.html index.htm;\r\n" + 
        "        }\r\n" + 
        "        \r\n" + 
        "        location /console/ {\r\n" + 
        "            proxy_pass http://127.0.0.1:8080/dcms-console/;\r\n" + 
        "        }\r\n" + 
        "    }\r\n" + 
        "\r\n" + 
        "\r\n" + 
        "    \r\n" + 
        "\r\n" + 
        "}\r\n" + 
        "";
    /**
     * 头部公共信息
     */
    public static final String HEADER = 
        "user nginx;\r\n" + 
        "worker_processes auto;\r\n" + 
        "pid /var/run/nginx.pid;";
    
    /**
     * 头部标识
     */
    public static final String AREA_HEADER = "HEADER";
    
    /**
     * 错误日志标识
     */
    public static final String AREA_LOGGER_ERROR = "LOGGER-ERROR";
    
    /**
     * EVENTS标识
     */
    public static final String AREA_EVENTS = "EVENTS";
    
    /**
     * 描述文件最大长度
     */
    public static final String EVENTS = 
        "events {\r\n" + 
        "  worker_connections 1025;\r\n" + 
        "}";
    
    /**
     * Http配置块
     * @Project : nginx
     * @Program Name : com.aiyi.server.manager.nginx.common.HTTP
     * @Description : 
     * @Author : 郭胜凯
     * @Creation Date : 2018年1月29日 下午6:11:12
     * @ModificationHistory Who When What ---------- ------------- -----------------------------------
     *                      郭胜凯 2018年1月29日 create
     */
    public static final class HTTP{
      
    }
  }
  
}
