$(function () {
    //添加用户
    addUser();

    //删除用户
    delUser();
    //修改用户
    updateUser();
    //初始化table
    tableInfo();
    //角色
    quanxian();
    //部门
    depeSelect();
    //省
    citySelect(0,"sheng");
    //市
    $('#sheng').change(function () {
        $('#xian').html('<option value="0">---请选择---</option>');
        citySelect(this.value,'shi');
    });
    //县
    $('#shi').change(function () {
        citySelect(this.value,'xian');
    });

    //上传
    upload();
});

function delUser() {
    /**
     * 此方法删除以后后台无异常,前台异常,因此直接禁用
     */
    $('#btn-div .btn-danger').attr('disabled',true);
    $('#btn-div .btn-danger').click(function () {
       /* cwh.confirm(function () {
            let selectData = cwh.getSelectData('user-table');
            if (selectData.length < 1){
                cwh.error("请选择数据");
                return ;
            }
            let user = [];
            for (let selectDataKey in selectData) {
                user.push(selectData[selectDataKey].id);
            }
            cwh.delete('/power/user?ids='+user.join(),function (data) {
                if (data.result == 1){
                    cwh.error("删除失败");
                }else{
                    cwh.refreshTable('user-table');
                }
            })
        })*/
    });
}

function upload() {
    $('#up').click(function () {
        $('#up-form input[name="imgUrl"]').click();
    });
    $('#up-form input[name="imgUrl"]').change(function () {
        let formData = new FormData($('#up-form')[0]);
        cwh.upLoad('/power/baseController/upload',"POST",formData,function (data) {
            $('input[name="url"]').val(data);
            $('#upLodaImg').attr('src',cwh.getProjectUrl()+"upload/"+data);
        })
    });
}

/**
 * 修改用户
 */
function updateUser() {
    $('#btn-div .btn-warning').click(function () {
        //获取表格信息
        let data = cwh.getOnlySelectData(`user-table`);
        //通过id查询用户信息
        cwh.asyncGet('/power/user/byId',{id:data.id},function (data) {
            //清空表单
            cwh.clearForm('user-form');
            //设置图片回显
            $('#upLodaImg').attr('src',cwh.getProjectUrl()+'upload/'+data.url);
            //回显表单
            cwh.loadForm('user-form',data);
            //设置密码禁止输入
            $('input[name="password"]').attr("readonly","readonly");
            //回显城市
            cityInfo(data.city,'xian');
            //打开模态框
            cwh.openModal('myModal','修改','修改');
        })
    });

}

/**
 * 城市回显
 */
function cityInfo(id,sel) {
    //获取县信息
    cwh.asyncGet('/power/city',{id:id},function (xian) {
        //获取市信息
        cwh.asyncGet('/power/city',{id:xian.parentId},function (shi) {
            //获取省信息
            cwh.asyncGet('/power/city',{id:shi.parentId},function (sheng) {
                //设置省市县值
                $('#sheng').val(sheng.id);
                $('#sheng').trigger('change');
                $('#shi').val(shi.id);
                $('#shi').trigger('change');
                $('#xian').val(id);
            })
        });
    })
}

/**
 * 初始化城市
 */
function citySelect(pid,city) {
    cwh.asyncGet('/power/city/byPid',{parentId:pid},function (data) {
        let citySelect = '<option value="0">---请选择---</option>';
        for (let x in data.list) {
            citySelect += '<option value="'+data.list[x].id+'">'+data.list[x].name+'</option>';
        }
        $('#'+city).html(citySelect);
    });
}

/**
 * 初始化部门列表
 */
function depeSelect() {
    cwh.get("/power/dept",{},function (data) {
        let deptHtml = '';
        for (let x in data.list) {
            deptHtml+= '<option value="'+data.list[x].id+'">'+data.list[x].name+'</option>';
        }
        $('#dept').html(deptHtml);
    })
}

/**
 * 添加用户
 */
function addUser() {
    //添加
    $('.btn-success').click(function () {
        cwh.openModal('myModal','添加用户','添加');
        //设置密码禁止输入 取消
        $('input[name="password"]').removeAttr("readonly");
        $('#upLodaImg').attr('src','');
        cwh.clearForm('user-form');
    });
    //账号验证
    jQuery.validator.addMethod('account',function(value,element){
        var bol = true;
        //通过账号查询数据库中是否存在相同的账号名，如不存在可以注册
        cwh.asyncGet('/power/user/byAccount', {account:value}, function(data) {
            console.log(data)
            if (data)bol=false;
        })
        //获取表单值
        var from = cwh.serializeForm("user-form");
        //如果id不为空,通过userid和account查询数据库中是否有数据，如有数据则说明account和userid匹配（即账号是原账号返回true，不提示错误）
        if(from.id){
            cwh.asyncGet('/power/user/byIdAccount', {account:value,id:from.id}, function(data1) {
                if (data1) bol=true;
            })
        }
        return bol;
    },'账号已存在');
    //验证密码
    jQuery.validator.addMethod('password',function(value,element){
        let val = $('input[name="password"]').val();
        if (val){
            return true;
        }
        var tel = /^[a-zA-Z]\w{5,16}$/;
        return this.optional (element) || (tel.test(value));
    },'密码格式错误');
    //验证手机号码
    jQuery.validator.addMethod("phone", function(value, element) {
        var tel = /^1([38]\d|5[0-35-9]|7[3678])\d{8}$/;
        return this.optional(element) || (tel.test(value));
        //校验失败提示信息
    }, "请输入正确的手机号");
    //验证城市
    jQuery.validator.addMethod("city", function(value, element) {
        if(value == 0){
            return false;
        }
        return true;
        //校验失败提示信息
    }, "请选择城市");
    //邮箱验证'
    jQuery.validator.addMethod("emails", function(value, element) {
        var tel = /^[a-zA-Z0-9]+([-_.][a-zA-Z0-9]+)*@[a-zA-Z0-9]+([-_.][a-zA-Z0-9]+)*\.[a-z]{2,}$/;
        return this.optional(element) || (tel.test(value));
        //校验失败提示信息
    }, "请输入正确邮箱");
    jQuery.validator.addMethod('email',function(value,element){
        var bol = true;
        // 通过邮箱查询是否存在
        cwh.asyncGet('/power/user/byEmail', {Email:value}, function(data) {
            if (data)bol=false
        })
        let id = $('#userId').val();
        //在修改时进入
        if (id){
            //通过用户id和邮箱查询是否存在
            cwh.asyncGet('/power/user/byIdEmail', {id:id,Email:value}, function(data) {
                if (data)bol=true
            })
        }
        return bol;
    },'邮箱已被注册');
    $('#user-form').validate({
        rules:{
            name:{
                required:true,
            },
            sex:{
                required:true,
            },
            birthday:{
                required:true,
                //date:true
            },
            email:{
                required:true,
                email:true,
                emails:true
            },
            city:{
                required:true,
                city:true,
            },
            phone:{
                required:true,
                phone:true,
            },
            account:{
                required:true,
                account:true,
            },
            password:{
                required:true,
                password:true,
            },
        },
        messages:{
            name:{
                required:'请输入名字',
            },
            sex:{
                required:'请选择性别',
            },
            birthday:{
                required:'请选择生日',
                //date:'请输入正确的日期'
            },
            email:{
                required:'请输入邮箱',
            },
            city:{
                required:'请选择城市'
            },
            phone:{
                required:'请输入手机号',
                phone:'请输入正确的手机号',
            },
            account:{
                required:'请输入账号',
            },
            password:{
                required:'请输入密码',
            },
        },
        submitHandler:function(form){
            //saveOrUpdate(保存用户信息)
            var serializeForm = cwh.serializeForm('user-form');
            let type = 'PUT';
            let user = $('#user-form input[name="id"]').val();
            if (user){
                type = 'POST';
            }
            cwh.asyncPostOrPut('/power/user',type,serializeForm,function (data) {
                if (data.result == 0){
                    cwh.alert("添加成功");
                    cwh.closeModal('myModal');
                    cwh.refreshTable('user-table');
                    //修改session中的url值
                    cwh.get('/power/user/updataSessionImg',{url:serializeForm.url}, function(data2) {
                        if (data2.result == 0){
                            cwh.alert("修改成功")
                        }
                    })
                }else{
                    cwh.error("添加失败");
                }
            })
        }
    });
}

//用户权限
function quanxian() {
    //权限按钮
    $('#btn-div .btn-primary').click(function () {
        //获取table选择
        let onlySelectData = cwh.getOnlySelectData('user-table');
        //获取所有角色
        cwh.get("/power/role",{},function (data) {
            let roleArr = [];
            //获取用户拥有角色
            cwh.asyncGet("/power/role/byUser",{user:onlySelectData.id},function (userdata) {
                for (let x in userdata.list) {
                    roleArr.push(userdata.list[x].role);
                }
            })
            let roleHtml = '<input type="hidden" name="user" value="'+onlySelectData.id+'" />';
            for (let role in data.list) {
                if (roleArr.indexOf(data.list[role].id) == -1){
                    roleHtml += ' <label><input  type="checkbox" name="'+data.list[role].name+'" value="'+data.list[role].id+'">'+data.list[role].name+'</label>';
                }else{
                    roleHtml += ' <label><input  type="checkbox" checked="checked" name="'+data.list[role].name+'" value="'+data.list[role].id+'">'+data.list[role].name+'</label>';
                }
            }
            $('#ztree .modal-body').html(roleHtml);
            cwh.openModal("ztree","用户绑定角色","确定");
        })
    });
    //确认赋权限
    $('#ztree .modal-footer .btn-primary').click(function () {
        //获取用户id
        let user = $('#ztree .modal-body input[name="user"]').val();
        let role = [];
        //获取角色id
        $('#ztree .modal-body :checkbox').each(function () {
            if (this.checked){
                role.push(this.value);
            }
        });
        console.log(user)
        console.log(role.join())
        //保存用户角色关系
        cwh.postOrPut('/power/user/userAndRole',"POST",{user:user,roles:role.join()},function (data) {
            console.log(data)
            if (data.result==0){
                cwh.alert("保存成功");
                cwh.closeModal('ztree');
            }else{
                cwh.error(data.msg);
            }
        })
    });
}


/**
 * 初始化表格
 */
function tableInfo() {
    cwh.iniTable("user-table","/power/user","list",[
        {
            checkbox:true
        },{
            field:'id',
            title:'编号',
            sortable:true
        },{
            field:'name',
            title:'姓名',
            sortable:true
        },{
            field:'sex',
            title:'性别',
            sortable:true,
            formatter:function (sex) {
                if (sex){
                    if (sex == 1){
                        return '男';
                    }else if( sex ==2){
                        return '女';
                    }else{
                        return sex;
                    }
                }
            }
        },{
            field:'birthday',
            title:'生日',
            sortable:true
        },{
            field:'email',
            title:'邮箱',
            sortable:true
        },{
            field:'city',
            title:'城市',
            sortable:true,
            formatter:function (city) {
                if (city){
                    cwh.asyncGet('/power/city',{id:city},function (data) {
                        city = data.name;
                    })
                    return city;
                }
            }
        },{
            field:'phone',
            title:'电话',
            sortable:true
        },{
            field:'account',
            title:'用户名',
            sortable:true
        },{
            field:'createTime',
            title:'创建时间',
            sortable:true
        },{
            field:'dept',
            title:'所属部门',
            sortable:true,
            formatter:function (dept) {
                if (dept){
                    cwh.asyncGet('/power/dept/byId',{id:dept},function (data) {
                        dept = data.name;
                    })
                }
                return dept;
            }
        },{
            field:'url',
            title:'头像',
            formatter:function (url) {
                if (url){
                    return '<a href="'+cwh.getProjectUrl()+'baseController/download?imgUrl='+url+'" ><img src="'+cwh.getProjectUrl()+'upload/'+url+'" width="100px" /></a>';
                }
            }
        }
    ],function (params) {
        return params;
    })
}
