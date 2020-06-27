let code = "";
$(function () {

    /**
     *标语
     */
    biaoyu();

    /**
     * 图片验证码
     */
    picCodeSend();

    /***
     * 点击图片验证码重新获取
     */
    $('#rePicCode').click(function () {
        picCodeSend();
    });

    $('#send').click(function () {
        let val = $('input[name="email"]').val();
        if(!val){
            cwh.error("邮箱不能为空");
            return;
        }
        var tel = /^[a-zA-Z0-9]+([-_.][a-zA-Z0-9]+)*@[a-zA-Z0-9]+([-_.][a-zA-Z0-9]+)*\.[a-z]{2,}$/;
        if (!tel.test(val)){
            cwh.error("邮箱错误")
            return;
        }
        let picCode = $('#picYantext').val()
        if (!picCode){
            cwh.error("请填写验证码");
            return;
        }
        let bol = picCode.toLowerCase().replace(/\r/g,'') ==code.toLowerCase().replace(/\r/g,'');
        if (!bol){
            cwh.error("验证码输入错误");
            picCodeSend();
            return;
        }
        $('#send').attr('disabled','disabled');
        //计时器
        setTimeout(function(){
            $("#send").removeAttr("disabled").val('点击重新发送验证码').ucss("cursor","pointer");
        }, 60000);
        //发送验证码
        cwh.get('/power/mail/send', {email:val},function (data) {
            if (data == 'success'){
                $("#code").html("短信验证码已成功发送！");
            }else{
                $("#code").html("发送失败请重试！");
            }
        })
    });

    $('#registerBtn').click(function () {
        location.href = cwh.getProjectUrl()+'page/register.jsp';
    });

    //邮箱验证
    jQuery.validator.addMethod('email',function(value,element){
        var bol = false;
        //通过邮箱查询是否存在
        cwh.asyncGet('/power/user/byEmail', {Email:value}, function(data) {
            if (data)bol=true
        })
        return bol;
    },'用户不存在或者请输入正确邮箱地址');
    //验证密码
    jQuery.validator.addMethod('authcode',function(value,element){
        var tel = /^\d{6}$/;
        return this.optional (element) || (tel.test(value));
    },'密码格式错误');
    //表单验证
    $('#login').validate({
        rules:{
            email:{
                required:true,
                email:true,
            },
            authcode:{
                required:true,
                authcode:true,
            },
        },
        messages:{
            email:{
                required:'请输入账号',
            },
            authcode:{
                required:'请输入验证码',
            }
        },
        submitHandler:function(form){
            //saveOrUpdate(保存用户信息)
            var serializeForm = cwh.serializeForm('login');
            $.ajax({
                url:'http://localhost:8080/power/user/loginEmail',
                type:"POST",
                async:false,
                contentType:'application/json;charset=UTF-8',
                data:decodeURIComponent(JSON.stringify(serializeForm)),
                success:function (data) {
                    if (data.result == 0){
                        if (window != top){
                            location.href = cwh.getProjectUrl()+'page/user.jsp';
                            //浏览器窗口刷新
                            window.top.location.reload();
                        }else {
                            location.href = cwh.getProjectUrl()+'page/index.jsp';
                        }
                    }else{
                        cwh.error(data.msg);
                    }
                }
            })
        }
    },);
})

function picCodeSend() {
    /**
     * 图片验证码
     */
    $.ajax({
        url:'https://www.mxnzp.com/api//verifycode/code',
        type:'GET',
        data: {
            app_id:"8tjokomspglos9nc",
            app_secret:"ejhSR1lZM1BZMVFwUHoza2xnT0FpQT09"
        },
        success:function (data) {
            if (data.code == 1){
                console.log(data)
                code = data.data.verifyCode;
                $('#picYan').attr('src',data.data.verifyCodeImgUrl)
            }
        }
    })
}

/**
 * 标语
 */
function biaoyu() {
    $.ajax({
        url:'https://www.mxnzp.com/api/daily_word/recommend',
        type:'GET',
        data: {
            app_id:"8tjokomspglos9nc",
            app_secret:"ejhSR1lZM1BZMVFwUHoza2xnT0FpQT09"
        },
        success:function (data) {
            $('#biaoyu').html(data.data[0].content);
        }
    })
}
