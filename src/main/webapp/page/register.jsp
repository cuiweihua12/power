<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
    <link href="<%=basePath %>static/css/bootstrap.min.css" rel="stylesheet"/>
    <link href="<%=basePath %>static/css/plugins/sweetalert/sweetalert.css" rel="stylesheet">
</head>
<body style=" background: url(<%=basePath %>static/img/bg.jpg) no-repeat center center fixed; background-size: 100%;">

 <form id="register">
    <div class="modal-dialog" style="margin-top: 10%;">
        <div class="modal-content">
            <div class="modal-header">

                <h4 class="modal-title text-center" id="myModalLabel">注册</h4>
                <h5 class="modal-title text-center" id="biaoyu" style="color: #a7b1c2"></h5>
            </div>
            <div class="modal-body" id = "model-body">
                <div class="form-group">

                    <input type="text" class="form-control" placeholder="用户名" name="account" autocomplete="off">
                </div>
                <div class="form-group">

                    <input type="password" class="form-control" placeholder="密码" name="password" autocomplete="off">
                </div>
                <!-- <div class="form-group">

                    <input type="password" class="form-control" placeholder="确认密码" name="rePassword" autocomplete="off">
                </div> -->
                <div class="form-group">

                    <input type="text" class="form-control" placeholder="邮箱" name="email" autocomplete="off">
                </div>
                <div class="form-group">

                    <input type="phone" class="form-control" placeholder="手机号" name="phone" autocomplete="off">
                </div>
            </div>
            <div class="modal-footer">
                <div class="form-group">
                    <button type="submit"  class="btn btn-primary form-control">注册</button>
                </div>
                <div class="form-group">
                    <button type="button" id="loginBtn" class="btn btn-default form-control">GO登录</button>
                </div>
            </div>
        </div><!-- /.modal-content -->
    </div><!-- /.modal -->
</form>
 <script type="text/javascript" src="<%=basePath %>static/js/jquery-1.11.3.min.js"></script>
 <script type="text/javascript" src="<%=basePath %>static/js/bootstrap.min.js"></script>
 <script type="text/javascript" src="<%=basePath %>static/js/plugins/sweetalert/sweetalert.min.js"></script>
 <script type="text/javascript" src="<%=basePath %>static/js/plugins/validate/jquery.validate.min.js"></script>
 <script type="text/javascript" src="<%=basePath%>static/js/public.js"></script>
 <script type="text/javascript" src="<%=basePath%>static/js/register.js"></script>
</body>
</html>
