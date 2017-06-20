/**
 * Created by wisdom on 2017/6/19.
 */
var ServerUrl=""

$(document).ready(function () {
    $.backstretch("../img/backgrounds/12.jpg");
    $("#alterinfo").hide();
    $("#login").click(function () {
        login();


    })
})


function login(){
    $.ajax({
        type: "POST",
        url: ServerUrl,
        async: false,
        dataType: "json",
        data: {
            "accountid": $("#accountid").val(),
            "password":$("#password").val()
        },
        beforeSend : function() {
          if(!$("#accountid").val() && !$("#password").val())
          {
              $("#alterinfo").text("账号密码不能为空！").show();
              return false;
          }

        },
        complete: function() {

        },
        success: function(data) {
            if(data == "success")
            {
                $.cookie('accountid',$("#accountid").val());
                window.location.href="test.html";
            }else
            {
                $("#alterinfo").text(data).show();
            }
        },
        error: function(e){
            alert(e.name + ": " + e.message + "\n链接失败");
        }
    });
}