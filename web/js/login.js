/**
 * Created by wisdom on 2017/6/19.
 */
var ServerUrl="../LoginServlet"

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
            if(data.logInfo == "-1")
            {
                $("#alterinfo").text("账号不存在！").show();
            }else if(data.logInfo == "-2")
            {
                $("#alterinfo").text("账号或密码错误").show();
            }else
            {
                $.cookie('accountid',data.logInfo);
                window.location.href="chat.html";
            }
            
        },
        error: function(e){
            alert(e.name + ": " + e.message + "\n链接失败");
        }
    });
}