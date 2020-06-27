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
 <form id="login">
    <div class="modal-dialog" style="margin-top: 10%;">
        <div class="modal-content">
            <div class="modal-header">

                <h4 class="modal-title text-center" id="myModalLabel">登录</h4>
                <h5 class="modal-title text-center" id="biaoyu" style="color: #a7b1c2"></h5>
            </div>
            <div class="modal-body" id = "model-body">
                <div class="form-group">
                    <input type="text" class="form-control" name="email" placeholder="邮箱" autocomplete="off">
                </div>
                <div class="form-group">
                    <input type="password" class="form-control" name="authcode" placeholder="验证码" autocomplete="off">
                    <a id="rePicCode"><img src="" id="picYan" style="width: 225px;height: 80px" /></a>
                    <input type="text" id="picYantext" />
                    <input type="button" class="btn btn-link" id="send" value="发送验证码" /><label id="code"></label>
                </div>
            </div>
            <div class="modal-footer">
                <div class="form-group">
                    <button type="submit" class="btn btn-primary form-control">登录</button>
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
 <script type="text/javascript" src="<%=basePath%>static/js/loginPhone.js"></script>
</body>
</html>
