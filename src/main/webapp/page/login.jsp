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
<iframe allowtransparency="true" frameborder="0" width="290" height="96" scrolling="no" src="//tianqi.2345.com/plugin/widget/index.htm?s=2&z=1&t=0&v=0&d=2&bd=0&k=000000&f=000000&ltf=009944&htf=cc0000&q=1&e=1&a=1&c=54511&w=290&h=96&align=right"></iframe>
 <form id="login">
    <div class="modal-dialog" style="margin-top: 10%;">
        <div class="modal-content">
            <div class="modal-header">

                <h4 class="modal-title text-center" id="myModalLabel">登录</h4>
                <h5 class="modal-title text-center" id="biaoyu" style="color: #a7b1c2"></h5>
            </div>
            <div class="modal-body" id = "model-body">
                <div class="form-group">

                    <input type="text" class="form-control" name="account" placeholder="用户名" autocomplete="off">
                </div>
                <div class="form-group">

                    <input type="password" class="form-control" name="password" placeholder="密码" autocomplete="off">
                </div>
            </div>
            <div class="modal-footer">
                <div class="form-group">
                    <button type="submit" class="btn btn-primary form-control">登录</button>
                </div>
                <div class="form-group">
                    <button type="button" id="phoneBtn" class="btn btn-info form-control">手机号码登录</button>
                </div>
                <div class="form-group">
                    <button type="button" id="registerBtn" class="btn btn-default form-control">注册</button>
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
 <script type="text/javascript" src="<%=basePath%>static/js/login.js"></script>
</body>
</html>
