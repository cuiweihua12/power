var curWwwPath=window.document.location.href;
var pathName=window.document.location.pathname;
var pos=curWwwPath.indexOf(pathName);
var root_path=curWwwPath.substring(0,pos);//root_path  项目跟路径

var mingrui = {
		ajax:function(json){
			$.ajax(json);
		},
		//ajax post/put请求
		postOrPut:function(url,type,data,success){
			$.ajax({
				url:root_path + url,
				type:type,
				contentType:'application/json;charset=UTF-8',
				data:decodeURIComponent(JSON.stringify(data)),
				success:success,
				error:function(err){
					if (err&& err.status && err.status == 403) {
						// 重定向到登陆页面
						//window.location.href = getRootPath() + "/login.jsp";
						mingrui.powerConfirm(function(){
							window.location.href= getRootPath() + "/login.jsp";
						});
					}
				}
			});
		},
		upLoad:function(url,type,data,success){
			$.ajax({
				url:root_path + url,
				type:type,
				cache:false,
				contentType:false,
				processData:false,
				data:data,
				success:success,
				error:function(err){
					if (err&& err.status && err.status == 403) {
						// 重定向到登陆页面
						mingrui.powerConfirm(function(){
							window.location.href= getRootPath() + "/login.jsp";
						});
					}
				}
			});
		},
		asyncPostOrPut:function(url,type,data,success){
			$.ajax({
				url:root_path + url,
				type:type,
				async:false,
				contentType:'application/json;charset=UTF-8',
				data:decodeURIComponent(JSON.stringify(data)),
				success:success,
				error:function(err){
					if (err&& err.status && err.status == 403) {
						// 重定向到登陆页面
						//window.location.href= getRootPath() + "/login.jsp";
						mingrui.powerConfirm(function(){
							window.location.href= getRootPath() + "/login.jsp";
						});
					}
				}
			});
		},
		//ajax get请求
		get:function(url,data,success){
			$.ajax({
				url:root_path + url,
				type:'GET',
				data:data,
				success:success,
				error:function(err){
					if (err&& err.status && err.status == 403) {
						// 重定向到登陆页面
						//window.location.href= getRootPath() + "/login.jsp";
						mingrui.powerConfirm(function(){
							window.location.href= getRootPath() + "/login.jsp";
						});
					}
				}
			});
		},
		asyncGet:function(url,data,success){
			$.ajax({
				url:root_path + url,
				type:'GET',
				async:false,
				data:data,
				success:success,
				error:function(err){
					if (err&& err.status && err.status == 403) {
						// 重定向到登陆页面
						//window.location.href= getRootPath() + "/login.jsp";
						mingrui.powerConfirm(function(){
							window.location.href= getRootPath() + "/login.jsp";
						});
					}
				}
			});
		},
		//ajax delete请求
		delete:function(url,success){
			$.ajax({
				url:root_path + url,
				type:'DELETE',
				success:success,
				error:function(err){
					if (err&& err.status && err.status == 403) {
						// 重定向到登陆页面
						//window.location.href= getRootPath() + "/login.jsp";
						mingrui.powerConfirm(function(){
							window.location.href= getRootPath() + "/login.jsp";
						});
					}
				}
			});
		},
		//刷新表格
		refreshTable:function(domId){
			
			$('#' + domId).bootstrapTable('refresh',{pageNumber:1});
		},
		//获取选中的数据（只获取一条数据）
		getOnlySelectData:function(formId){
			var selectData = $('#' + formId).bootstrapTable('getSelections');
			if(selectData.length <= 0){
				swal({
	        		title:"失败",
	        		text:"请选择数据"
	        		})
				return ;
			}
			if(selectData.length > 1){
				swal({
	        		title:"失败",
	        		text:"只能操作一条数据"
	        		})
				return ;
			}
			return selectData[0];
		},
		//获取选中的数据（获取多条数据）
		getSelectData:function(formId){
			var selectData = $('#' + formId).bootstrapTable('getSelections');
			if(selectData.length <= 0){
				swal({
	        		title:"失败",
	        		text:"请选择数据"
	        		})
				return ;
			}
			
			return selectData;
		},
		//初始化表格
		iniTable:function(table_obj,data_url,data_field,columns,queryParams){
			$('#' + table_obj).bootstrapTable('destroy').bootstrapTable({
		                url: root_path + data_url,//数据源
		                dataField: data_field,//服务端返回数据键值 就是说记录放的键值是rows，分页时使用总记录数的键值为total
		                striped: true,//行间色
		                clickToSelect: true,
		                cache: false, //不启用缓存
		            //    height: $(window).height()-375,//高度调整
		                pagination: true,//是否分页
		                pageSize: 10,//单页记录数
		                pageList: [5,10,15,20],//分页步进值
		                sidePagination: "server",//服务端分页
		                contentType: "application/x-www-form-urlencoded",//请求数据内容格式 默认是 application/json 自己根据格式自行服务端处理
		                dataType: "json",//期待返回数据类型
		                method: "GET",//请求方式
		                searchAlign: "right",//查询框对齐方式
		                queryParamsType: "limit",//查询参数组织方式
		                queryParams: queryParams,
		                searchOnEnterKey: false,//回车搜索
		                uniqueId: "id",
		                showToggle:false, 
		                //cardView: false, 
		                minimumCountColumns: 2,
		                showExport: true,
		               // exportDataType: "basic",
		                search:false,
		                showRefresh: true,//刷新按钮
		                //showColumns: true,//列选择按钮
		                buttonsAlign: "right",//按钮对齐方式
		               // toolbar: "#toolbar",//指定工具栏
		                toolbarAlign: "right",//工具栏对齐方式
		                columns: columns,
		               // onClickRow: onClickRow,//单击row事件
		                locale: "zh-CN",//中文支持,
		                detailView: false, //是否显示详情折叠
		                detailFormatter: function(index, row, element) {
		                    var html = '';
		                    $.each(row, function(key, val){
		                        html += "<p>" + key + ":" + val +  "</p>"
		                    });
		                    return html;
		                }
		            });
		},
		//清空form表单
		clearForm:function(formId){
			$(':input', $('#' + formId)).each(function() {
			    var type = this.type;
			    var tag = this.tagName.toLowerCase(); 
			    if (type == 'text' || type == 'password' || tag == 'textarea' || type == 'hidden' || type == 'date')
			      this.value = "";
			    else if (type == 'checkbox' || type == 'radio')
			      this.checked = false;
			    else if (tag == 'select')
			      this.selectedIndex = 0;
			  });
			$('#' + formId).find('label[class="error"]').remove();
		},
		//序列化表单内容
		serializeForm:function(formId){
			var fid = "#" + formId;
			var str = $(fid).serialize();
			var ob = strToObj(str);
			return ob;
		},
		//回显form表单数据
		loadForm:function(dom,obj){
			var key,value,tagName,type,arr;
			
		    for(x in obj){
		        key = x;
		        value = obj[x];
		        $("#" + dom + " [name='"+key+"'],#" + dom + " [name='"+key+"[]']").each(function(){
		        	
		            tagName = $(this)[0].tagName;
		            type = $(this).attr('type');
		            
		            if(tagName=='INPUT'){
		                if(type=='radio'){
		                   // $(this).attr('checked',$(this).val()==value);
		                    $(this).prop('checked',$(this).val()==value);
		                }else if(type=='checkbox'){
		                	
		                    arr = value.split(',');
		                    for(var i =0;i<arr.length;i++){
		                    	
		                        if($(this).val()==arr[i]){
		                        	console.log('aaaaa')
		                        	$(this).prop('checked',true);
		                           // $(this).attr('checked',true);
		                            break;
		                        }
		                    }
		                }else if(type == 'date' || type == 'newDate'){
		                	
		                    if(value != undefined && value != null){
		                    	
		                    	$(this).val(getSmpFormatDateByLong(value,false));
		                    }
		                }else{
		                	
		                    $(this).val(value);
		                }
		            }else if(tagName=='SELECT' || tagName=='TEXTAREA'){
		            	
		                $(this).val(value);
		            }
		        });
		    }
		},
		//将long类型的数据转换成yyyy-MM-dd
		getSmpFormatDateByLong:function(l, isFull){
			return getSmpFormatDate(new Date(l), isFull); 
		},
		//打开模态框
		openModal:function(dom,title,buttonHTML){
			$('#'+ dom + ' .modal-title').html(title);
			$('#'+ dom + ' .btn-primary').text(buttonHTML);
			$('#'+ dom + '').modal('show');
		},
		//关闭模态框
		closeModal:function(dom){
			$('#'+ dom + '').modal('hide');
		},
		//提示框
		alert:function(info){
			swal("提示！",info,"success");
		},
		error:function(info){
			swal("提示！",info,"error");
		},
		//确认框
		confirm:function(success){
			swal({
    			title:"您确定要删除选中数据吗？",
    			text:"删除后将无法恢复，请谨慎操作！",
    			type:"warning",
    			showCancelButton:true,
    			confirmButtonColor:"#DD6B55",
    			confirmButtonText:"删除",
    			closeOnConfirm:false},
    			success
    			);
		},
		relConfirm:function(success,title){
			swal({
    			title:title,
    			text:"删除后将无法恢复，请谨慎操作！",
    			type:"warning",
    			showCancelButton:true,
    			confirmButtonColor:"#DD6B55",
    			confirmButtonText:"删除",
    			closeOnConfirm:false},
    			success
    			);
		},
		powerConfirm:function(success){
			swal({
				title:'你没有此权限是否退出,以管理员身份登录',
				text:"点击退出则跳转到登录页!",
				type:"warning",
				showCancelButton:true,
				confirmButtonColor:"#DD6B55",
				confirmButtonText:"退出",
				closeOnConfirm:false},
				success
			);
		},
		cwhConfirm:function(success,title,text,btn){
			swal({
				title:title,
				text:text,
				type:"warning",
				showCancelButton:true,
				confirmButtonColor:"#DD6B55",
				confirmButtonText:btn,
				closeOnConfirm:false},
				success
			);
		},
		uploadImg:function(){
			
			$list = $("#fileList");
			// 初始化Web Uploader

			var webUploaderParams = {
					fileVal:"imgUrl",
					auto: true,
					server:mingrui.getProjectUrl() + '/baseController/upload',
					pick : '#filePicker',
					fileNumLimit : 0,
					threads : 1,
					accept : {
							title : 'Images',
							extensions : 'gif,jpg,jpeg,bmp,png',
							mimeTypes : 'image/*'
						}
				}
			var uploader = WebUploader.create(webUploaderParams);
			uploader.on('fileQueued', function(file) {
				$list.empty();
				var $li = $('<div id="' + file.id + '" class="file-item thumbnail">'
							+ '<img>'
							+ '<div class="info">'
							+ file.name + '</div>' + '</div>'),
				$img = $li.find('img');
				$list.append($li);
				var thumbnailWidth = 100;
				var thumbnailHeight = 100;
				uploader.makeThumb(file, function(error, src) {
					if (error) {
						$img.replaceWith('<span>不能预览</span>');
						return;
					}
					$img.attr('src', src);
				}, thumbnailWidth, thumbnailHeight);
			});
				
			uploader.on('uploadSuccess', function(file, data) {//上传文件成功以后的回调函数
				console.log(data)
				$("#imgUrl").val(data._raw);
			})
			uploader.on('uploadError', function(file) {//上传失败的回调函数
				var $li = $('#' + file.id), $error = $li.find('div.error');
				if (!$error.length) {
					$error = $('<div class="error"></div>').appendTo($li);
				}
				$error.text('上传失败');
			}); 
		},
		getProjectUrl:function(){
			//获取当前网址，如： http://localhost:8083/uimcardprj/share/meun.jsp
			var curWwwPath = window.document.location.href;
			//获取主机地址之后的目录，如： uimcardprj/share/meun.jsp
			var pathName = window.document.location.pathname;
			var pos = curWwwPath.indexOf(pathName);
			//获取主机地址，如： http://localhost:8083
			var localhostPaht = curWwwPath.substring(0, pos);
			//获取带"/"的项目名，如：/uimcardprj
			var projectName = pathName.substring(0, pathName.substr(1).indexOf('/') + 1);
			return localhostPaht+projectName + "/";
		},
		
}


function strToObj(str) {                                                           
	str = str.replace(/&/g, "','");
	str = str.replace(/=/g, "':'");
	str = str.replace(/\+/g, "");
	str = "({'" + str + "'})";
	obj = eval(str);
	return obj;
}


Date.prototype.format = function (format) {
    var o = {  
        "M+": this.getMonth() + 1,  
        "d+": this.getDate(),  
        "h+": this.getHours(),  
        "m+": this.getMinutes(),  
        "s+": this.getSeconds(),  
        "q+": Math.floor((this.getMonth() + 3) / 3),  
        "S": this.getMilliseconds()  
    }  
    if (/(y+)/.test(format)) {  
        format = format.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));  
    }  
    for (var k in o) {  
        if (new RegExp("(" + k + ")").test(format)) {  
            format = format.replace(RegExp.$1, RegExp.$1.length == 1 ? o[k] : ("00" + o[k]).substr(("" + o[k]).length));  
        }  
    }  
    return format;  
}  
/**   
*转换日期对象为日期字符串   
* @param date 日期对象   
* @param isFull 是否为完整的日期数据,   
*               为true时, 格式如"2000-03-05 01:05:04"   
*               为false时, 格式如 "2000-03-05"   
* @return 符合要求的日期字符串   
*/    
function getSmpFormatDate(date, isFull) {  
    var pattern = "";  
    if (isFull == true || isFull == undefined) {  
        pattern = "yyyy-MM-dd hh:mm:ss";  
    } else {  
        pattern = "yyyy-MM-dd";  
    }  
    return getFormatDate(date, pattern);  
}  
/**   
*转换当前日期对象为日期字符串   
* @param date 日期对象   
* @param isFull 是否为完整的日期数据,   
*               为true时, 格式如"2000-03-05 01:05:04"   
*               为false时, 格式如 "2000-03-05"   
* @return 符合要求的日期字符串   
*/    
  
function getSmpFormatNowDate(isFull) {  
    return getSmpFormatDate(new Date(), isFull);  
}  
    /**   
*转换long值为日期字符串   
* @param l long值   
* @param isFull 是否为完整的日期数据,   
*               为true时, 格式如"2000-03-05 01:05:04"   
*               为false时, 格式如 "2000-03-05"   
* @return 符合要求的日期字符串   
*/    
  
function getSmpFormatDateByLong(l, isFull) {  
    return getSmpFormatDate(new Date(l), isFull);  
}  
    /**   
*转换long值为日期字符串   
* @param l long值   
* @param pattern 格式字符串,例如：yyyy-MM-dd hh:mm:ss   
* @return 符合要求的日期字符串   
*/    
  
    function getFormatDateByLong(l, pattern) {  
        return getFormatDate(new Date(l), pattern);  
    }  
    /**   
*转换日期对象为日期字符串   
* @param l long值   
* @param pattern 格式字符串,例如：yyyy-MM-dd hh:mm:ss   
* @return 符合要求的日期字符串   
*/    
function getFormatDate(date, pattern) {  
    if (date == undefined) {  
        date = new Date();  
    }  
    if (pattern == undefined) {  
        pattern = "yyyy-MM-dd hh:mm:ss";  
    }  
    return date.format(pattern);  
}

function getRootPath(){
	//获取当前网址，如： http://localhost:8083/uimcardprj/share/meun.jsp
	var curWwwPath = window.document.location.href;
	//获取主机地址之后的目录，如： uimcardprj/share/meun.jsp
	var pathName = window.document.location.pathname;
	var pos = curWwwPath.indexOf(pathName);
	//获取主机地址，如： http://localhost:8083
	var localhostPaht = curWwwPath.substring(0, pos);
	//获取带"/"的项目名，如：/uimcardprj
	var projectName = pathName.substring(0, pathName.substr(1).indexOf('/') + 1);
	return (localhostPaht+projectName)
}
    

//所有页面输入框的页面数据缓存的清除
$("body input").each(function(){
	$(this).attr("autocomplete","off");
})

