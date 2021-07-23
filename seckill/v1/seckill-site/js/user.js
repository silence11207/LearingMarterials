$(document).ready(function () {
    // 设置用户登录状态
    set_login_status();
    // 注销按钮单击事件
    $("#btn-logout").click(function (e) { 
        logout();        
    });
});

function set_login_status() {
    var $A = $(".user-name");
    if (!$A) return false;

    $.ajax({
        type: "GET",
        url: SERVER_PATH + "/user/status",
        xhrFields: {withCredentials: true},
        success: function (result) {
            if (result.status == "0" && result.data) {
                $A.text(result.data.nickname);
                $("#user-info").show();
            } else {
                $("#user-info").hide();
            }
        }
    });    
}

function logout() {
    $.ajax({
        type: "GET",
        url: SERVER_PATH + "/user/logout",
        xhrFields: {withCredentials: true},
        success: function (result) {
            if (result.status) {
                alertBox(result.data.message);
                return false;
            }
            alertBox("已经注销！", function(){
                window.location.href = "./login.html"
            });
        }
    });    
}