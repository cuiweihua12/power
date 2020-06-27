
	//时间插件的修改和封装start
!function(){

	laydate.skin('molv');//切换皮肤，请查看skins下面皮肤库

	laydate({elem: '#demo'});//绑定元素
}();

//日期范围限制
function startdate(time){
	 var start = {

		    elem: '#'+time,

		    format: 'YYYY-MM-DD',

		    min: '1900-01-01', //设定最小日期为当前日期的写法 min: laydate.now(),

		    max: '2099-06-16', //最大日期

		    istime: true,

		    istoday: false,

/*		    choose: function(datas){

		         end.min = datas; //开始日选好后，重置结束日的最小日期

		         end.start = datas //将结束日的初始值设定为开始日

		    }*/

		};
	laydate(start);
}
function enddate(time1){
	/*alert(2)*/
	 var end = {

	    elem: '#'+time1,

	    format: 'YYYY-MM-DD',

	    min: '1900-01-01', //设定最小日期为当前日期的写法 min: laydate.now(),

	    max: '2099-06-16',

	    istime: true,

	    istoday: false,

/*	    choose: function(datas){

	        start.max = datas; //结束日选好后，充值开始日的最大日期

	    }*/

	};
	laydate(end);
}

$(document).ready(function(){
	
	$('.laydate-icon').click(function(){
		
		 var data = {
			    elem: '#' + $(this).attr('id'),

			    format: 'YYYY-MM-DD',

			    min: '1900-01-01', //设定最小日期为当前日期的写法 min: laydate.now(),

			    max: '2099-06-16',

			    istime: true,

			    istoday: false,


			};
			laydate(data);
		
	});
	
});

function showDate(time1){
	/*alert(2)*/
	 var end = {

	    elem: '#'+time1,

	    format: 'YYYY-MM-DD',

	    min: '1900-01-01', //设定最小日期为当前日期的写法 min: laydate.now(),

	    max: '2099-06-16',

	    istime: true,

	    istoday: false,

/*	    choose: function(datas){

	        start.max = datas; //结束日选好后，充值开始日的最大日期

	    }*/

	};
	laydate(end);
}

//自定义日期格式

laydate({

    elem: '#test1',

    format: 'YYYY年MM月DD日',

    festival: true, //显示节日
/*
    choose: function(datas){ //选择日期完毕的回调

        alert('得到：'+datas);

    }*/

});



//日期范围限定在昨天到明天

laydate({

    elem: '#hello3',

    min: laydate.now(-1), //-1代表昨天，-2代表前天，以此类推

    max: laydate.now(+1) //+1代表明天，+2代表后天，以此类推

});

//时间插件的修改和封装end
