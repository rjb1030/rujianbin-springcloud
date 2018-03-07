;(function($){

    $("tbody tr").eq(0).clone().appendTo($('tbody'));
    $("tbody tr").eq(-1).show();


    $(".productList .product").eq(0).clone().appendTo($('.productList'));
    $(".productList .product").eq(-1).show();

    $("#addRule").click(function(){
        $("tbody tr").eq(0).clone().appendTo($('tbody'));
        $("tbody tr").eq(-1).show();
    })
    $("#addProduct").click(function(){
        $(".productList .product").eq(0).clone().appendTo($('.productList'));
        $(".productList .product").eq(-1).show();
    });

    $("#jisuan").click(function(){

        var param = $("form").serialize();
        $.ajax({
            url:"/rujianbin-app-web/home/ruleEngineSubmit",
            type:"post",
            data:param,
            success:function(data){
                $("textarea").val(data)
            },
            error:function(err){
                console.log(err);
            }

        })
        return false;
    });
})(jQuery);