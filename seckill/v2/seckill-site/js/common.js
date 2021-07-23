/**
 * 服务端PATH
 */
var SERVER_PATH = "http://127.0.0.1:8080";

/**
 * 获取URL中的参数
 */
(function($) {
    $.getUrlParam = function (name) {
        var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
        var r = window.location.search.substr(1).match(reg);
        if (r != null) return unescape(r[2]); return null;
    }
})(jQuery);

/**
 * 自定义弹出框
 * 
 * @param {*} message  提示消息
 * @param {*} callback 回调函数，在弹出框关闭时触发。
 */
function alertBox(message, callback) {
	if (!message) return false;

	var template_alert_box = 
		'<div class="modal" id="alert-box" tabindex="-1" role="dialog">'+
			'<div class="modal-dialog" role="document" style="margin:100px auto;">'+
				'<div class="modal-content">'+
					'<div class="modal-header">'+
						'<h5 class="modal-title">提示</h5>'+
						'<button type="button" class="close" data-dismiss="modal" aria-label="Close">'+
							'<span aria-hidden="true">&times;</span>'+
						'</button>'+
					'</div>'+
					'<div class="modal-body">'+
						'<p id="alert-box-message"></p>'+
					'</div>'+
					'<div class="modal-footer">'+
						'<button type="button" class="btn btn-secondary" data-dismiss="modal">确定</button>'+
					'</div>'+
				'</div>'+
			'</div>'+
		'</div>';

	if ($("#alert-box")) $("#alert-box").remove();

	var $box = $(template_alert_box);
	$box.find("#alert-box-message").text(message);
	$box.modal({backdrop:"static", show:true});
	if (callback) $box.on('hidden.bs.modal', callback);
	$("body").append($box);

	return true;
}