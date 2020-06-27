<%--
  Created by IntelliJ IDEA.
  User: CWH
  Date: 2020/6/19
  Time: 17:27
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
            <input type="button" class="btn btn-danger" value="删除" />
        </div>
        <div class="divztree1" id="ztree">
            <ul id="treeDemo" class="ztree"></ul>
        </div>
        <div  class="divztree" >
            <form class="form-horizontal" id="power-details-form" role="form" style="margin: 100px;">
                <div class="form-group">
                    <label class="col-sm-2 control-label">父级权限名称</label>
                    <div class="col-sm-10">
                        <input type="hidden" name="pid"/>
                        <input type="text" readonly="readonly" class="form-control" name="pname" />
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-2 control-label">新增权限名称</label>
                    <div class="col-sm-10">
                        <input type="text" readonly="readonly" class="form-control" name="name" />
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-2 control-label">请求路径</label>
                    <div class="col-sm-10">
                        <input type="text"  readonly="readonly" class="form-control" name="url" />
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-2 control-label">shiro权限</label>
                    <div class="col-sm-10">
                        <input type="text"  readonly="readonly" class="form-control" name="auth" />
                    </div>
                </div>
            </form>
        </div>
        <!-- 新增修改 start -->
        <div class="modal fade" id="power-addOrUpdate-modal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <form class="form-horizontal" id="power-form" role="form" >
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal"
                                    aria-hidden="true">&times;</button>
                            <h4 class="modal-title" id="myModalLabel"></h4>
                        </div>
                        <div class="modal-body">
                            <div class="form-group">
                                <label class="col-sm-2 control-label">父级权限名称</label>
                                <div class="col-sm-10">
                                    <input type="text" class="form-control" readonly="readonly" name="pname" />
                                    <input type="hidden" class="form-control" name="pid" />
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-2 control-label">权限名称</label>
                                <div class="col-sm-10">
                                    <input type="hidden" name="id">
                                    <input type="text" class="form-control" name="name" />
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-2 control-label">请求路径</label>
                                <div class="col-sm-10">
                                    <input type="text" class="form-control" name="url" />
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-2 control-label">shiro权限</label>
                                <div class="col-sm-10">
                                    <input type="text"   class="form-control" name="auth" />
                                </div>
                            </div>
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-default" data-dismiss="modal">关闭
                            </button>
                            <button type="submit" class="btn btn-primary"></button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
        <!-- 新增修改 end -->
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
<script type="text/javascript" src="<%=basePath%>static/js/power.js"></script>
</body>
</html>
