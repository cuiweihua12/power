/**
 * Created by Administrator on 2017/5/8.
 */

var ModalPanel= {
    systemMessage : '系统提示',
    defaultFn : function() {},
    /**
     * 确认框
     * @param message 提示语
     * @param okCallbace 回调函数，用户点击确定按钮后执行。如果不配置，默认是关闭弹框。
     * @param noCallback 回调函数，用户点击取消按钮后执行。如果不配置，默认是关闭弹框。
     */
    confirm : function(title, message, okCallback, noCallback) {
        if (undefined== noCallback) {
            noCallback = ModalPanel.defaultFn;
        }
        ModalPanel.simpleDialog(title, message, okCallback,noCallback);
    },

    /**
     * 提示框
     * @param message 提示语
     * @param okCallbace 回调函数，用户点击确定按钮后执行。如果不配置，默认是关闭弹框。
     */
    alert : function(title, message, okCallback,noCallback) {
        if (undefined== noCallback) {
            noCallback = ModalPanel.defaultFn;
        }
        ModalPanel.simpleDialog(title, message, okCallback);
    },

    /**
     * 弹框，如：simpleDialog(
     * @param message 提示语
     * @param okCallback 函数，用户点击了确定按钮要执行
     * @param noCallback 函数，用户点击了取消按钮要执行
     */
    simpleDialog : function(title, message, okCallback, noCallback) {
        name=name||ModalPanel.systemMessage;
        message=message||""; 
        var glamodal=$('#glamodal');
        if (undefined== glamodal[0]) {
            var content= '<div class="modal fade" id="glamodal" tabindex="-1" role="dialog"'+
                ' aria-labelledby="myModalLabel" aria-hidden="true">';
            content += '<div class="modal-dialog">';
            content += '<div class=" modal-content gla-content">';
            content += '<div class="modal-header">';
            content += '<span id="glamodaltitle" class="modal-title">'+ title +'</span>';
            content += '<img class="offbtn"  src="img/modal-off-default.png">';
            content += '</div>';
            content += '<div id="glamodalbody" class="gla-modal-body">'+ message + '</div>';
            content += '<div class="modal-footer">';
            content += '<button type="button" id="glaSureBtn" class="btn btn-warn ui-button">确定</button>';
            content += '<button type="button" id="glaCloseBtn" class="btn btn-cancle ui-button"'+
                ' style="display: none;">取消</button>';
            content += '</div></div></div></div>';
            $('body').after(content);
            glamodal = $('#glamodal');
        } else {
            $('#glaCloseBtn').hide();// 初始化取消按钮
            $('#glamodalbody').html(message);
            $('#glamodaltitle').html(title);
        }
        glamodal.modal({
            backdrop : 'static',
            keyboard : false
        });
        $(".offbtn").click(function(){
            glamodal.modal('hide');
        });
        $(".offbtn").mouseover(function(){
        	$(this).attr("src","img/delate-hover.png");
        });
        $(".offbtn").mouseout(function(){
        	$(this).attr("src","img/modal-off-default.png");
        });
        $('#glaSureBtn').unbind('click').bind('click',function() {
            //$('#glaSureBtn').style.backgroundColor="red";
            glamodal.modal('hide');
            glamodal.on('hidden.bs.modal',function() {
                if (undefined!= okCallback) {
                    okCallback();
                    // 清空赋值是必须的，防止alert里面套alert出现是循环
                    okCallback = undefined;
                }
            })
        });
        // 取消按钮给定的回调函数不为空，才显示该按钮
        if (undefined!= noCallback) {
            var btn= $('#glaCloseBtn');
            btn.show();
            btn.unbind('click').bind('click',function() {
                glamodal.modal('hide');
                noCallback();
                noCallback = undefined;
            });
        }
    }
};
function over(obj){
	$(obj).attr("src","img/delate-hover.png");
}
function out(obj){
	$(obj).attr("src","img/modal-off-default.png");
}
