$(function () {
    ztreeinfo();
    add();
    update();
    del();
});

function del() {

    $('#btn-div .btn-danger').click(function () {
        //获取选择节点
        var treeObj = $.fn.zTree.getZTreeObj("treeDemo");
        var nodes = treeObj.getSelectedNodes();
        if (nodes[0].id == 1){
            cwh.error("跟节点不能删除")
            return;
        }
        //获取子节点
        let chirlden = treeObj.transformToArray(nodes);
        if (nodes.length < 1){
            cwh.error("请选择节点");
            return;
        }
        if (nodes[0].id == 1){
            cwh.error("此节点不能删除");
            return;
        }
        //查看是否是父节点
        let parent = nodes[0].isParent;
        if (!parent){
            //无子节点
            cwh.delete("/power/power?id="+nodes[0].id,function (data) {
                if (data.result == 0){
                    cwh.alert("删除成功");
                    ztreeinfo();
                }else{
                    cwh.error(data.msg);
                }
            });

        }else{
            cwh.cwhConfirm(function () {
                //有子节点
                let ids = [];
                for (let chirldenKey in chirlden) {
                    if (chirlden[chirldenKey].id != 1){
                        ids.push(chirlden[chirldenKey].id);
                    }else{
                        cwh.error("根节点   "+chirlden[chirldenKey].name+"   不能被删除!");
                        return;
                    }
                }
                cwh.delete("/power/power/batch?ids="+ids.join(),function (data) {
                    if (data.result == 0){
                        cwh.alert("删除成功");
                        ztreeinfo();
                    }else{
                        cwh.error(data.msg);
                    }
                });
            },'删除权限','此节点为父节点,确认删除吗?','删除');


        }
    });

}

function update() {
    $('#btn-div .btn-warning').click(function () {
        var treeObj = $.fn.zTree.getZTreeObj("treeDemo");
        var nodes = treeObj.getSelectedNodes();
        if (nodes.length < 0) {
            cwh.error("请选择节点")
            return;
        }
        if (nodes.length > 1) {
            cwh.error("只能选择一个节点");
            return;
        }

        cwh.openModal("power-addOrUpdate-modal", '修改权限', '修改');
        cwh.clearForm('power-form');
        $('input[name="pname"]').val(nodes[0].name);
        $('input[name="pid"]').val(nodes[0].id);
        cwh.loadForm('power-form', nodes[0])
    });
}

function add() {
    $('#btn-div .btn-success ').click(function () {
        var treeObj = $.fn.zTree.getZTreeObj("treeDemo");
        var nodes = treeObj.getSelectedNodes();
        if (nodes.length<0){
            cwh.error("请选择节点")
            return;
        }
        if (nodes.length>1){
            cwh.error("只能选择一个节点");
            return;
        }
        cwh.openModal("power-addOrUpdate-modal",'增加权限','增加');
        cwh.clearForm('power-form');
        $('input[name="pname"]').val(nodes[0].name);
        $('input[name="pid"]').val(nodes[0].id);
    });

    //saveOrUpdate
    jQuery.validator.addMethod('name',function (value,element) {
        let bol = true;
        var form = cwh.serializeForm('power-form');
        //通过pid和权限名查看是否存在
        $.ajax({
            url:'http://localhost:8080/power/power/byPidName',
            type:"GET",
            data : {name:value,pid:form.pid},
            dataType:'JSON',
            async:false,
            success:function (data) {
                console.log(data)
                if (data)bol=false;
            }
        });
        //修改时调用
        if (form.id){
            $.ajax({
                url:'http://localhost:8080/power/power/byIdName',
                type:"GET",
                data : {name:value,id:form.id},
                dataType:'JSON',
                async:false,
                success:function (data) {
                    if (data)bol=true;
                }
            });
        }
        return bol;
    },'权限已存在')
    $('#power-form').validate({
        rules:{
            name:{
                required:true,
                name:true
            },
            url:{
                required:true,
            },
            auth:{
                required:true
            }
        },
        messages:{
            name:{
                required:'请输入权限名'
            },
            url:{
                required:'请输入权限路径',
            },
            auth:{
                required:'请输入Shiro权限路径'
            }
        },
        submitHandler:function (form) {
            var form = cwh.serializeForm('power-form');
            let type = "POST";
            if (form.id){
                type = "PUT";
            }
            cwh.postOrPut('/power/power',type,form,function (data) {
                if (data.result == 0){
                    cwh.alert("保存成功")
                    cwh.closeModal('power-addOrUpdate-modal');
                    ztreeinfo();
                }else {
                    cwh.error("保存失败")
                }
            })
        }
    });


}

function ztreeinfo() {
    var setting = {
        check: {
            enable: false
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
        callback: {
            beforeClick: function (treeId, treeNode, clickFlag) {
                cwh.clearForm('power-details-form');
                if (treeNode.getParentNode()) {
                    treeNode.pname = treeNode.getParentNode().name;//获取父节点name
                }
                cwh.loadForm('power-details-form',treeNode);
            }
        }

    };
    cwh.get("/power/power",{},function (data) {
            var zNodes =data.list;
            $.fn.zTree.init($("#treeDemo"), setting, zNodes);
            var treeObj = $.fn.zTree.getZTreeObj("treeDemo");
            treeObj.expandAll(true);
    })
}