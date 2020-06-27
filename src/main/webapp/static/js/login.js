$(function () {

    /**
     * 标语
     */
   biaoyu();

    $('#registerBtn').click(function () {
        location.href = cwh.getProjectUrl()+'page/register.jsp';
    });
    $('#phoneBtn').click(function () {
        cwh.cwhConfirm(function () {
            location.href = cwh.getProjectUrl()+'page/loginPhone.jsp';
        },'使用手机号码登录',"别用了,我还得花钱!",'让你知道社会的险恶,冲!')
    });

    //账号验证
    jQuery.validator.addMethod('account',function(value,element){
        var bol = false;
        //通过账号查询数据库中是否存在相同的账号名
        cwh.asyncGet('/power/user/byAccount', {account:value}, function(data) {
            if (data)bol=true;
        })
        return bol;
    },'用户不存在');
    //验证密码
    jQuery.validator.addMethod('password',function(value,element){
        var tel = /^[a-zA-Z]\w{5,16}$/;
        return this.optional (element) || (tel.test(value));
    },'密码格式错误');
    //表单验证
    $('#login').validate({
        rules:{
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
            account:{
                required:'请输入账号',
            },
            password:{
                required:'请输入密码',
            }
        },
        submitHandler:function(form){
            //saveOrUpdate(保存用户信息)
            var serializeForm = cwh.serializeForm('login');
            cwh.asyncPostOrPut('/power/user/login',"POST",serializeForm,function (data) {
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
            })
        }
    },);
})

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
