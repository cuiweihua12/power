<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@ page import="com.mr.cwh.system.condition.UserCondition" %>
<%@ page import="org.springframework.util.StringUtils" %><%--
  Created by IntelliJ IDEA.
  User: CWH
  Date: 2020/6/19
  Time: 11:25
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<%
    UserCondition user = (UserCondition) request.getSession().getAttribute("user");
    String name = null;
    String url = null;
    String roleName = null;
    Integer id = null;
    if (user != null){
        if (user.getId() != null){
            id =user.getId();
        }
        if (user.getName() != null){
            name = user.getName();
        }
        if (user.getUrl() != null){
            url = user.getUrl();
        }
        if (!StringUtils.isEmpty(user.getRoleName())){
            roleName = user.getRoleName();
        }
    }
%>
<html>
<head>
    <title>Title</title>
    <link href="<%=basePath %>static/css/bootstrap.min14ed.css?v=3.3.6" rel="stylesheet">
    <link href="<%=basePath %>static/css/font-awesome.min93e3.css?v=4.4.0" rel="stylesheet">
    <link href="<%=basePath %>static/css/animate.min.css" rel="stylesheet">
    <link href="<%=basePath %>static/css/plugins/sweetalert/sweetalert.css" rel="stylesheet">
    <link href="<%=basePath %>static/css/style.min862f.css?v=4.1.0" rel="stylesheet">
</head>
<body class="fixed-sidebar full-height-layout gray-bg" style="overflow:hidden">
    <div id="wrapper">
        <!--左侧导航开始-->
        <nav class="navbar-default navbar-static-side" role="navigation">
            <div class="nav-close"><i class="fa fa-times-circle"></i>
            </div>
            <div class="sidebar-collapse">
                <ul class="nav" id="side-menu">
                    <li class="nav-header">
                        <div class="dropdown profile-element">
                            <span><img alt="image" width="100px" id="headImg" class="img-circle" src="<%=basePath%>upload/<%=url%>" /></span>
                            <a data-toggle="dropdown" class="dropdown-toggle" href="#">
                                        <span class="clear">
                                       <span class="block m-t-xs"><strong class="font-bold">
                                        <%=name%>
                                       </strong></span>
                                        <span class="text-muted text-xs block"><%=roleName%><b class="caret"></b></span>
                                        </span>
                            </a>
                            <ul class="dropdown-menu animated fadeInRight m-t-xs">
                                <li><a class="J_menuItem" id="updataHead">修改头像</a>
                                </li>
                                <li><a class="J_menuItem" id="personal-data">个人资料</a>
                                </li>
                                <li><a class="J_menuItem">联系我们</a>
                                </li>
                                <li><a class="J_menuItem">信箱</a>
                                </li>
                                <li class="divider"></li>
                                <li><a id="quit">安全退出</a></li>
                            </ul>
                        </div>
                        <div class="logo-element">MR
                        </div>
                    </li>
                    <li>
                        <a href="#">
                            <i class="fa fa-home"></i>
                            <span class="nav-label">系统管理</span>
                            <span class="fa arrow"></span>
                        </a>
                        <ul class="nav nav-second-level">
                            <li>
                                <a class="J_menuItem" href="<%=basePath %>page/user.jsp" data-index="0">用户管理</a>
                            </li>
                            <li>
                                <a class="J_menuItem" href="<%=basePath %>page/role.jsp">角色管理</a>
                            </li>
                            <li>
                                <a class="J_menuItem" href="<%=basePath %>page/power.jsp">权限管理</a>
                            </li>
                           <%-- <li>
                                <a class="J_menuItem" href="<%=basePath %>main/sys/dept.jsp">部门管理</a>
                            </li>
                            <li>
                                <a class="J_menuItem" href="index_v5.html">学生管理</a>
                            </li>--%>
                        </ul>

                    </li>


                </ul>
            </div>
        </nav>
        <!--左侧导航结束-->
        <!--右侧部分开始-->
        <div id="page-wrapper" class="gray-bg dashbard-1">
            <div class="row border-bottom">
                <nav class="navbar navbar-static-top" role="navigation" style="margin-bottom: 0">
                    <div class="navbar-header"><a class="navbar-minimalize minimalize-styl-2 btn btn-primary " href="#"><i class="fa fa-bars"></i> </a>

                    </div>
                </nav>
            </div>
            <div class="row content-tabs">
                <button class="roll-nav roll-left J_tabLeft"><i class="fa fa-backward"></i>
                </button>
                <nav class="page-tabs J_menuTabs">
                    <div class="page-tabs-content">
                        <a href="javascript:;" class="active J_menuTab" data-id="<%=basePath %>page/user">首页</a>
                    </div>
                </nav>
                <button class="roll-nav roll-right J_tabRight"><i class="fa fa-forward"></i>
                </button>
                <div class="btn-group roll-nav roll-right">
                    <button class="dropdown J_tabClose" data-toggle="dropdown">关闭操作<span class="caret"></span>

                    </button>
                    <ul role="menu" class="dropdown-menu dropdown-menu-right">
                        <li class="J_tabShowActive"><a>定位当前选项卡</a>
                        </li>
                        <li class="divider"></li>
                        <li class="J_tabCloseAll"><a>关闭全部选项卡</a>
                        </li>
                        <li class="J_tabCloseOther"><a>关闭其他选项卡</a>
                        </li>
                    </ul>
                </div>
                <a class="roll-nav roll-right J_tabExit" id="exit"><i class="fa fa fa-sign-out"></i> 退出</a>
            </div>
            <div class="row J_mainContent" id="content-main">
                <iframe class="J_iframe" name="iframe0" width="100%" height="100%" src="<%=basePath %>page/user.jsp" frameborder="0" data-id="index_v1.html" seamless></iframe>
            </div>
            <div class="footer">
                <div class="pull-right">&copy; 2019-8-16 <a href="#" target="_blank">崔蔚华</a>
                </div>
            </div>
        </div>
        <!--右侧部分结束-->
        <form id="head-form" style="display: none;">
            <input type="text" name="id" id="headId" value="<%=id%>">
            <input type="file"  id="headUrl" name="imgUrl" >
        </form>

    </div>
</body>
<script src="<%=basePath %>static/js/jquery.min.js?v=2.1.4"></script>
<script src="<%=basePath %>static/js/bootstrap.min.js?v=3.3.6"></script>
<script src="<%=basePath %>static/js/plugins/metisMenu/jquery.metisMenu.js"></script>
<script src="<%=basePath %>static/js/plugins/slimscroll/jquery.slimscroll.min.js"></script>
<script src="<%=basePath %>static/js/plugins/layer/layer.min.js"></script>
<script src="<%=basePath %>static/js/hplus.min.js?v=4.1.0"></script>
<script type="text/javascript" src="<%=basePath %>static/js/contabs.min.js"></script>
<script src="<%=basePath %>static/js/plugins/pace/pace.min.js"></script>
<script type="text/javascript" src="<%=basePath %>static/js/mr-public.js"></script>
<script type="text/javascript" src="<%=basePath %>static/js/plugins/validate/jquery.validate.min.js"></script>
<script type="text/javascript" src="<%=basePath %>static/js/mr-js/index.js"></script>
<script type="text/javascript" src="<%=basePath %>static/js/plugins/sweetalert/sweetalert.min.js"></script>
<script type="text/javascript" src="<%=basePath%>static/js/public.js"></script>
<script type="text/javascript" src="<%=basePath%>static/js/index.js"></script>
</html>
