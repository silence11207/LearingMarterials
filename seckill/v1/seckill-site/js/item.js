$(document).ready(function () {
    // 获取ID
    var id = $.getUrlParam("id");
    // 加载数据
    get_item(id);
    // 立即购买单击事件
    $("#buy").click(function (e) { 
        create_order(id);
    });
});

function get_item(id) {
    $.ajax({
        type: "GET",
        url: SERVER_PATH + "/item/detail/" + id,
        xhrFields: {withCredentials: true},
        success: function (result) {
            if (result.status) {
                alertBox(result.data.message);
                return false;
            }
            set_item(result.data);
        }
    });
}

function set_item(item) {
    if (!item) {
        return false;
    }

    $(".goods-left img").attr("src", item.imageUrl);
    $(".goods-right .goods-title div").text(item.title);
    $(".goods-right .seckill-price b").text(item.promotion.promotionPrice);
    $(".goods-right .goods-price del").text(item.price);
    $(".goods-right .goods-sales span").text(item.sales);
    $(".goods-right .goods-detail div").html(item.description);

    $("#stock").val(item.itemStock.stock);
    $("#promotionId").val(item.promotion.id);
}

function create_order(id) {
    var stock = $("#stock").val();
    var promotionId = $("#promotionId").val();
    var amount = parseInt($("#amount").val());

    if (!amount || amount <= 0 || amount > stock) {
        alertBox("请输入正确的数量！");
        return false;
    }

    $.ajax({
        type: "POST",
        url: SERVER_PATH + "/order/create",
        data: {
            "itemId": id,
            "amount": amount,
            "promotionId": promotionId
        },
        xhrFields: {withCredentials: true},
        success: function (result) {
            if (result.status) {
                alertBox(result.data.message, function(){
                    if (result.data.code == "101") window.location.href = "./login.html";
                });
                return false;
            }
            alertBox("下单成功！", function(){
                window.location.href = "./seckill.html"
            });
        }
    });    
}