$(function () {
    tableInfo();
    addUser();
    updateRole();

    //删除角色
    $('#btn-div .btn-danger').click(function () {
        let selectData = cwh.getSelectData('role-table');
        if (selectData.length < 1 ){
            cwh.error("请选择数据");
            return;
        }
        let role = [];
        for (let selectDataKey in selectData) {
            if (selectData[selectDataKey].id == 1){
                cwh.error("超级管理员不能删除！")
                return;
            }
            role.push(selectData[selectDataKey].id);
        }
        cwh.delete('/power/role?ids='+role.join(),function (data) {
            if (data.result == 0){
                cwh.alert('删除成功');
                cwh.refreshTable('role-table');
            }else {
                cwh.error(data.msg)
            }
        })

    });

    $('#btn-div .btn-primary').click(function () {

        var data = cwh.getOnlySelectData('role-table');
        $('#role-power-modal input[name="role"]').val(data.id);
        cwh.openModal('role-power-modal','权限',"权限");
        ztreeinfo(data.id);
    });
    $('#role-power-modal .modal-footer .btn-primary').click(function () {
        //获取角色id
        var role = $('#role-power-modal input[name="role"]').val();

        //获取选择的权限
        var treeObj = $.fn.zTree.getZTreeObj("treeDemo");
        var nodes = treeObj.getCheckedNodes(true);
        let powerArr = [];
        for (let x in nodes) {
            powerArr.push(nodes[x].id);
        }
       cwh.postOrPut('/power/power/saveRolePower',"POST",{role:role,powers:powerArr.join()},function (data) {
           if (data.result == 0){
               cwh.alert("保存成功");
               cwh.closeModal("role-power-modal");
           }else{
               cwh.error(data.msg);
           }
       })
    });
});


function ztreeinfo(role) {
    var setting = {
        check: {
            enable: true,
            chkStyle: "checkbox",
            chkboxType: { "Y": "p", "N": "s" }
        },
        data: {
            simpleData: {
                enable: true,
                pIdKey:'pid'
            },
            key: {
                url: "xUrl"
            }
        },
    };
    //查询所有权限
    cwh.get("/power/power",{},function (data) {
        //查询角色拥有权限
        cwh.get("/power/power/byRole",{role:role},function (data1) {
            //加载数据
            var zNodes =data.list;
            $.fn.zTree.init($("#treeDemo"), setting, zNodes);
            //获取ztree
            var treeObj = $.fn.zTree.getZTreeObj("treeDemo");
            //展开所有
            treeObj.expandAll(true);
            //遍历角色拥有权限
            for(var i in data1.list){
                //设置为选中
                var node = treeObj.getNodeByParam("id", data1.list[i].power, null);
                treeObj.checkNode(node, true, false);

            }
        })

    })
}

/**
 * 修改角色
 */
function updateRole() {
    $('#btn-div .btn-warning').click(function () {
        let selectData = cwh.getOnlySelectData(`role-table`);
        cwh.get('/power/role/byId',{id:selectData.id},function (data) {
            cwh.loadForm('role-form',data);
            cwh.openModal('role-addOrUpdate-modal','修改');
        })
    });
}

/**
 * 添加角色
 */
function addUser() {
    //添加
    $('.btn-success').click(function () {
        cwh.openModal('role-addOrUpdate-modal','添加角色','添加');
    });
    //确认添加
    $('#role-addOrUpdate-modal .modal-footer .btn-primary').click(function () {
        var serializeForm = cwh.serializeForm('role-form');
        let type = 'PUT';
        let user = $('#user-form input[name="id"]').val();
        if (user){
            type = 'POST';
        }
        cwh.postOrPut('/power/role',type,serializeForm,function (data) {
            if (data.result == 0){
                cwh.alert("添加成功");
                cwh.closeModal('role-addOrUpdate-modal');
                cwh.refreshTable('role-table');
            }else{
                cwh.error("添加失败");
            }
        })
    });
}

/**
 * 初始化表格
 */
function tableInfo() {
    cwh.iniTable("role-table","/power/role","list",[
        {
            title: "全选",
            width: 20,//宽度
            align: "center",//水平
            valign: "middle",//垂直
            checkbox: true
        },
        {
            field: "name",
            title: "name",
            sortable: true
        },
        {
            field: "description",
            title: "description",
            sortable: true
        },
        {
            field: "createTime",
            title: "createTime",
            sortable: true
        }
    ],function (params) {
        return params;
    })
}