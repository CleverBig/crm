<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
String basePath = request.getScheme() + "://" + request.getServerName() + ":" + 	request.getServerPort() + request.getContextPath() + "/";
%>
<!DOCTYPE html>
<html>
<head>
	<base href="<%=basePath%>">
	<meta charset="UTF-8">
	<link href="jquery/bootstrap_3.3.0/css/bootstrap.min.css" type="text/css" rel="stylesheet" />
	<script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
	<script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>
	<script type="text/javascript">
		$(function(){
			// 页面加载完毕后，loginAct获取焦点
			$("#loginAct").focus();

			// 设置回车键登录
			$(window).keydown(function (event) {
				if(13==event.keyCode){
					login();
				}
			})

			$("#submitBtn").click(function () {
				login();
			})

			// 登录函数
			function login() {
				// 获取用户名(去除所有空格)和密码(去除左右空格)
				var loginAct = $("#loginAct").val().replace(/\s/g,"");
				var loginPwd = $.trim($("#loginPwd").val());
				if(loginAct == "" || loginPwd == ""){
					// 强制结束当前程序
					return false;
				}
				// 发送ajax异步请求
				$.ajax({
					url: "settings/user/login.do",
					data: {
						loginAct,
						loginPwd
					},
					type: "post",
					dataType: "json",
					success: function (data) {
						// 获取登录验证结果
						// {"success":"true","msg":"错误信息"}

						// 登录验证成功
						if(data.success){
							// 跳转到欢迎页
							window.location.href = "workbench/index.jsp";
						// 登录验证失败
						}else{
							// 展示提示信息
							$("#msg").html(data.msg);
						}
					}
				})
			}
		})
	</script>
</head>
<body>
	<div style="position: absolute; top: 0px; left: 0px; width: 60%;">
		<img src="image/IMG_7114.JPG" style="width: 100%; height: 90%; position: relative; top: 50px;">
	</div>
	<div id="top" style="height: 50px; background-color: #3C3C3C; width: 100%;">
		<div style="position: absolute; top: 5px; left: 0px; font-size: 30px; font-weight: 400; color: white; font-family: 'times new roman'">CRM &nbsp;<span style="font-size: 12px;">&copy;2017&nbsp;动力节点</span></div>
	</div>
	
	<div style="position: absolute; top: 120px; right: 100px;width:450px;height:400px;border:1px solid #D5D5D5">
		<div style="position: absolute; top: 0px; right: 60px;">
			<div class="page-header">
				<h1>登录</h1>
			</div>
			<form action="workbench/index.jsp" class="form-horizontal" role="form">
				<div class="form-group form-group-lg">
					<div style="width: 350px;">
						<input class="form-control" type="text" placeholder="用户名" id="loginAct">
					</div>
					<div style="width: 350px; position: relative;top: 20px;">
						<input class="form-control" type="password" placeholder="密码" id="loginPwd">
					</div>
					<div class="checkbox"  style="position: relative;top: 30px; left: 10px;">
						
							<span id="msg" style="color: red;"></span>
						
					</div>
					<button type="button" class="btn btn-primary btn-lg btn-block"  style="width: 350px; position: relative;top: 45px;" id="submitBtn">登录</button>
				</div>
			</form>
		</div>
	</div>
</body>
</html>