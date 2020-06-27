$(function () {



    /**
     * shiro注销登录
     */
    $('#quit,#exit').click(function () {
        cwh.cwhConfirm(function () {
            cwh.get("/power/user/loginout",{},function (data) {
                location.href = cwh.getProjectUrl()+'/page/login.jsp';
            })
        },"退出","确定退出登录吗?",'退出')
    });
    /**
     * 修改头像
     */
    $('#updataHead').click(function(){
        $('#headUrl').click();
    });
    $('#headUrl').change(function(){
        let formData = new FormData($('#head-form')[0]);
        cwh.upLoad('/power/baseController/upload', 'POST', formData, function(data) {
            $('#headImg').attr('src',cwh.getProjectUrl()+'upload/'+data);
            //获取当前登录用户id
            let userId = $('#headId').val();
            //上传图片名称到数据库
            cwh.postOrPut('/power/user/editHead', 'PUT', {id:userId,url:data}, function(data1) {
                //修改session中的url值
                cwh.get('/power/user/updataSessionImg',{url:data}, function(data2) {
                    if (data2.result == 0){
                        cwh.alert("修改成功")
                    }
                })
            })
        })
    });
})
