<%--
  Created by IntelliJ IDEA.
  User: CWH
  Date: 2020/6/16
  Time: 21:04
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<html>
<head>
    <title>Title</title>
    <link href="<%=basePath %>static/css/bootstrap.min.css" rel="stylesheet"/>
    <link href="<%=basePath %>static/bootstrap-table/bootstrap-table.min.css" rel="stylesheet"/>
    <link href="<%=basePath %>static/css/mr.css" rel="stylesheet"/>
    <link href="<%=basePath %>static/css/plugins/sweetalert/sweetalert.css" rel="stylesheet">
    <link rel="stylesheet" href="<%=basePath %>static/ztree/css/zTreeStyle/zTreeStyle.css" type="text/css">
    <link rel="stylesheet" href="<%=basePath%>static/css/user.css">
</head>
<body>
    <div id="body-div">
        <div id="btn-div">
            <input type="button" class="btn btn-success" value="添加" />
            <input type="button" class="btn btn-warning" value="修改"/>
            <input type="button" class="btn btn-danger" value="删除"/>
            <input type="button" class="btn btn-primary" value="角色" />
        </div>
        <!-- table -->
        <table id="user-table"></table>
        <!-- 模态框（Modal） -->
        <div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                        <h4 class="modal-title" id="myModalLabels">模态框（Modal）标题</h4>
                    </div>
                    <form class="form-horizontal" id="user-form" role="form">
                        <div class="modal-body">
                            <div class="form-group">
                                <label class="col-sm-2 control-label">名字</label>
                                <div class="col-sm-10">
                                    <input type="hidden" id="userId" name="id"/>
                                    <input type="text" class="form-control" name="name" placeholder="请输入名字"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-2 control-label">性别</label>
                                <div class="col-sm-10">
                                    <input type="radio" name="sex" value="1"/>男
                                    <input type="radio" name="sex" value="2"/>女
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-2 control-label">生日</label>
                                <div class="col-sm-10">
                                    <input type="text" class="form-control laydate-icon" name="birthday" id="birthday" placeholder="请输入生日"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-2 control-label">邮箱</label>
                                <div class="col-sm-10">
                                    <input type="text" class="form-control" name="email" placeholder="请输入邮箱"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-2 control-label">手机号码</label>
                                <div class="col-sm-10">
                                    <input type="text" class="form-control" name="phone" placeholder="请输入手机号码"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-2 control-label">账号</label>
                                <div class="col-sm-10">
                                    <input type="text" class="form-control" name="account" placeholder="请输入账号"/>
                                </div>
                            </div>
                            <div class="form-group ">
                                <label class="col-sm-2 control-label ">密码</label>
                                <!-- <div class="col-sm-10">
                                    <input type="password" class="form-control "  name="password" placeholder="请输入密码"/>
                                    <label class="glyphicon glyphicon-eye-open" style="float: right;" aria-hidden="true"></label>
                                </div> -->
                                <div class=" col-sm-10 input-group">
                                    <input type="password" class="form-control" placeholder="请输入密码"  name="password" aria-describedby="basic-addon2">
                                    <span class="input-group-addon glyphicon glyphicon-eye-open" id="basic-addon2"></span>
                                </div>
                            </div>

                            <div class="form-group">
                                <label class="col-sm-2 control-label">城市</label>
                                <div class="col-sm-10">
                                    省:<select id="sheng">
                                            <option value="0">---请选择---</option>
                                        </select>
                                    市:<select id="shi">
                                            <option value="0">---请选择---</option>
                                        </select>
                                    县:<select name="city" id="xian">
                                            <option value="0">---请选择---</option>
                                        </select>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-2 control-label">部门</label>
                                <div class="col-sm-10">
                                    部门:<select name="dept" id="dept">
                                    <option value="0">---请选择---</option>
                                </select>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-2 control-label">img</label>
                                <div class="col-sm-10">
                                    <div>
                                        <img id="upLodaImg" width="100px" >
                                    </div>
                                    <input type="hidden" name="url"  >
                                    <input type="button" value="上传图片" id="up" class="btn btn-default">
                                </div>
                            </div>
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                            <button type="submit" class="btn btn-primary">提交更改</button>
                        </div>
                    </form>
                </div><!-- /.modal-content -->
                <form id="up-form" style="display: none">
                    <input type="file" name="imgUrl" >
                </form>
            </div><!-- /.modal -->
        </div>

        <!-- 模态框（Modal） -->
        <div class="modal fade" id="ztree" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
                        <h4 class="modal-title" id="myModalLabel">模态框（Modal）标题</h4>
                    </div>
                    <div class="modal-body">

                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                        <button type="button" class="btn btn-primary">提交更改</button>
                    </div>
                </div><!-- /.modal-content -->
            </div><!-- /.modal-dialog -->
        </div>
        <!-- /.modal -->
    </div>

<script type="text/javascript" src="<%=basePath %>static/js/jquery-1.11.3.min.js"></script>
<script type="text/javascript" src="<%=basePath %>static/ztree/js/jquery.ztree.core.js"></script>
<script type="text/javascript" src="<%=basePath %>static/ztree/js/jquery.ztree.excheck.js"></script>
<script type="text/javascript" src="<%=basePath %>static/js/bootstrap.min.js"></script>
<script type="text/javascript" src="<%=basePath %>static/js/plugins/sweetalert/sweetalert.min.js"></script>
<script type="text/javascript" src="<%=basePath %>static/bootstrap-table/bootstrap-table.min.js"></script>
<script type="text/javascript" src="<%=basePath %>static/jquery-calendar/laydate.js"></script>
<script type="text/javascript" src="<%=basePath %>static/jquery-calendar/laydate_public.js"></script>
<script type="text/javascript" src="<%=basePath %>static/js/plugins/validate/jquery.validate.min.js"></script>
<script type="text/javascript" src="<%=basePath%>static/js/public.js"></script>
<script type="text/javascript" src="<%=basePath%>static/js/user.js"></script>
</body>
</html>
