$(function () {
    $("#search").click(function (event) {
        var keywords = $.trim($("#keywords").val());
        if(keywords==""){
            event.preventDefault();
            alert("keyword is empty!");
            return false;
        }
        $.ajax({
            url: "/iSearch/ajax/search",
            type: "GET",
            data: {
                "keywords": keywords,
                "pageNo": 1
            },
            success: function (data) {
                $("#docs").html(data);
            }
        });
    });
});