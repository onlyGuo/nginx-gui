(function($) {
    $.fn.createTable = function (param) {
        var $t = this;
        if(!param){
            param = {
                pageSize:10,
                start:0,
                request:"demo.json",
                colom:[
                ],
                requestParam:{},
                filter:function(obj){
                    return obj;
                }
            }
        }else{
            if(!param.pageSize){
                param.pageSize = 10;
            }
            if(!param.request){
                param.request = "demo.json";
            }
            if(!param.colom){
                throw new Error("colom is null");
            }
            if(!param.filter){
                param.filter = function(obj){
                    return obj;
                }
            }
            if(!param.start) {
                param.start = 0;
            }
        }

        var tableStr = "<div class=\"aiyi-tools-table-list\">\n" +
            "            <table>\n" +
            "                <tr class=\"head\">\n";

        for(var index in param.colom){
            tableStr += "<th>" + param.colom[index][1] + "</th>\n";
        }
        tableStr += "</tr>\n";


        getUrlCountent(param.request, param.start, param.pageSize, param.requestParam);

        function getUrlCountent(url, start, length, params){
            //构建请求参数
            var req = {
                length:length,
                start:start
            };
            for(var index in params){
                req[index] = params[index];
            }

            var res = {};

            //请求表数据
            $.post(url, req, function(result) {
                res.allCount = (!result.size ? 0 : result.size);
                res.size = result.list.length;
                res.list = result.list;
                res.start = result.start;
                res.length = result.length;

                for (var i in res.list) {
                    tableStr += "<tr>\n";
                    var obj = param.filter(res.list[i]);
                    if (obj) {
                        for (var c in param.colom) {
                            tableStr += "<td>" + obj[param.colom[c][0]] + "</td>\n";
                        }
                    }
                    tableStr += "</tr>\n";
                }
                tableStr += "</table>\n";
                //构建分页
                if(!param.noPage){
                    tableStr += "<div class=\"page\">\n" +
                        "                <div class=\"page-number\">当前第<span>1</span>页 每页\n" +
                        "                    <select>\n";
                    if (param.pageSize == 10) {
                        tableStr += "<option selected>10</option>\n";
                    } else {
                        tableStr += "<option>10</option>\n";
                    }
                    if (param.pageSize == 20) {
                        tableStr += "<option selected>20</option>\n";
                    } else {
                        tableStr += "<option>20</option>\n";
                    }
                    if (param.pageSize == 40) {
                        tableStr += "<option selected>40</option>\n";
                    } else {
                        tableStr += "<option>40</option>\n";
                    }
                    if (param.pageSize == 70) {
                        tableStr += "<option selected>70</option>\n";
                    } else {
                        tableStr += "<option>70</option>\n";
                    }
                    if (param.pageSize == 100) {
                        tableStr += "<option selected>100</option>\n";
                    } else {
                        tableStr += "<option>100</option>\n"
                    }
                    var maxPage =  res.allCount % param.pageSize > 0 ? parseInt(res.allCount / param.pageSize) + 1 : res.allCount / param.pageSize;
                    tableStr += "</select>\n条, 共<span>" + maxPage + "</span>页</div>\n";

                    //构建分页按钮
                    var pagenumStr = "<div class=\"page-menu\">\n" +
                        "                    <span><</span>";
                    var start = 0;
                    var length = 0;
                    if(res.allCount >= 10 * param.pageSize){
                        length = 10;
                    }else{
                        length = parseInt(res.allCount / param.pageSize);
                        if(res.allCount % param.pageSize > 0){
                            length ++;
                        }
                    }
                    if(res.start / param.pageSize > 5){
                        start = res.start / param.pageSize - 5;
                    }
                    for(var i = start; i < start + length; i++){
                        if(i == parseInt(res.start / param.pageSize)){
                            pagenumStr += "<span class='this-page'>" + (i + 1) + "</span>"
                        }else{
                            pagenumStr += "<span>" + (i + 1) + "</span>"
                        }
                    }
                    pagenumStr += "<span class=\"end\">></span>\n"

                    tableStr += pagenumStr;
                }
                $t.html(tableStr);
                $(".aiyi-tools-table-list .page .page-number select").change(function(){
                    var length =  parseInt($(this).find("option:selected").text());
                    $t.createTable({
                        request:param.request,
                        requestParam:param.requestParam,
                        colom:param.colom,
                        pageSize:length,
                        filter:param.filter
                    });
                });

                $(".aiyi-tools-table-list .page .page-menu span").click(function(){
                    var page = $(this).text();
                    var start = 0;
                    var length = param.pageSize;
                    if(page == "<"){
                        start = 0;
                    }else if(page == ">"){
                        start = res.allCount % param.pageSize > 0 ? parseInt(res.allCount / param.pageSize) * param.pageSize : (parseInt(res.allCount / param.pageSize) - 1) * param.pageSize;
                    }else{
                        start =  (parseInt(page) - 1) * param.pageSize;
                    }
                    $t.createTable({
                        request:param.request,
                        requestParam:param.requestParam,
                        colom:param.colom,
                        start:start,
                        pageSize:length,
                        filter:param.filter
                    });

                });

                if (param.success){
                    param.success($t);
                }
            });
            
        }
        $($t).attr("table_params", JSON.stringify(param));
        return $(this);
    },
    $.fn.reloadTable = function () {
    		var param = JSON.parse($(this).attr("table_params"));
    		$(this).createTable({
                request:param.request,
                requestParam:param.requestParam,
                colom:param.colom,
                start:start,
                pageSize:length,
                filter:param.filter
        });
    }
}(jQuery));