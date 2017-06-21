/**
 * Created by wisdom on 2017/6/18.
 */

var ServerUrl ="../RegisterServlet"
$(document).ready(function() {
    /*
     Fullscreen background
     */
    $.backstretch("../img/backgrounds/12.jpg");
    $("#sighupinfo").hide();

    $("#sighup").click(function () {


        sighup();


    })




});

function sighup(){
    $.ajax({
        type: "POST",
        url: ServerUrl,
        async: false,
        dataType: "json",
        data: {
            "nickname": $("#nickname").val(),
            "password":$("#form-password").val(),
            "gender":$("#gender").val(),
            "birthday":$("#form-date").val()
        },
        beforeSend : function() {
            if(!$("#nickname").val() || !$("#form-password").val() || !$("#form-checkpwd").val() || !$("#form-date").val())
            {
                $("#sighupinfo").text("选项不能为空！").show();
                return false;
            }
            if($("#form-password").val()!=$("#form-checkpwd").val())
            {
                $("#sighupinfo").text("两次密码不一致！").show();
                return false;
            }
        },
        complete: function() {
        },
        success: function(data) {
            $("#sighupinfo").hide();
            $('#info').empty();
            if(data.accountid)
            {
                $('#info').append("注册成功，账号为："+ data.accountid);
                $('#myModal').modal('show');
            }
        },
        error: function(e){
            alert(e.name + ": " + e.message + "\n链接失败");
        }
    });
}