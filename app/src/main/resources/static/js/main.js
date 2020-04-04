/**
 * 后台主框架
 */
$(function(){
	
	window.hTab = Htab.init({
		div:$(".layui-body")[0],
		width:"100%",
		height:"calc(100% - 500px)"
	});
	hTab.addDefault("sys/status/", "sys/status/", "服务器状态");
	
	$(".layui-side .layui-side-scroll .layui-nav .layui-nav-item a").click(function(){
		var link = $(this).attr("link");
		if(link){
			hTab.open(link, link, $(this).text());
		}
	});
});