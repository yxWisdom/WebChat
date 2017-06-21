/**
 * Created by wisdom on 2017/6/19.
 */

var chat_contents_height = 20;
$(document).ready(function () {
    // $("#ChatContent").empty();
    // $("#ChatInput").val("");

    // var int=self.setInterval("readMsg()",1000);
    //var Test=[{"a":"aaa","b":"231"},{"a":"haha","b":"231"}];
    //alert(Test.length);
    chatTo("123");
    if($("#sendmsg").size()>0)
        alert(123);
    $("#sendmsg").click(function () {
        // var text=$("#message").val();
        // if(text) {
        //     var textbox = new ChatContent.Text(text, "ai");
        //     showMsg(textbox);
        // }
        sendMsg();
    })
})

function refresh()
{
    var  c;
    var csender =   $("#xxxx");
    $.ajax({
        type: "POST",
        url: ServerUrl,
        async: false,
        dataType: "json",
        data: {
            "id": id,
            "reviver":$.cookie("accountid")
        },
        beforeSend : function() {
        },
        complete: function() {
        },
        success: function(Data) {
            c=Data;
        },
        error: function(e){
            alert(e.name + ": " + e.message + "\n链接失败");
        }
    });
   for(var i=0;i<c.length;i++)
   {
       if(c[i].sender == csender)
       {
           var textbox = new ChatContent.Text(data.text,"ai");
           showMsg(textbox);
       }
   }

}

function chatTo(id) {
    id=id.substr("message".length);
    var  data = {"accountid":"1001","nickname":"hello","gender":"男","birthday":"1996-12-05"}//readFriendInfo(id);
    while(!data)
    {
        data = readFriendInfo(id);
    }
    var str;
    str = "账号："+data.accountid+"<br>"+"昵称："+data.nickname+"<br>"+"性别："+data.gender+"<br>"+"出生日期："+data.birthday;
    var label = $("#div_userinfo").find("label");
    label.text(data.nickname);
    label.attr("data-original-title",data.nickname);
    label.attr("title",data.nickname);
    label.attr("data-content",str);
    label.attr("id",data.accountid);
}
function readFriendInfo(id) {
    var retData=null;
    $.ajax({
        type: "POST",
        url: ServerUrl,
        async: false,
        dataType: "json",
        data: {
            "id": id
        },
        beforeSend : function() {
            if(!id)
                return false;
        },
        complete: function() {
        },
        success: function(Data) {
            retData = Data;
        },
        error: function(e){
            alert(e.name + ": " + e.message + "\n链接失败");
        }
    });
    return retData;
}



function readMsg() {
    $.ajax({
        type: "POST",
        async: false,
        url: ServerUrl,
        dataType: "json",
        data: {
            "sender": $("#accountid").val(),
            "receiver":$("#password").val(),
            "text":  $("#message").val()
        },
        beforeSend : function() {
            if(!data.text)
            {
                return false;
            }
        },
        complete: function() {
        },
        success: function(Data) {
            if(Data.info == "1")
            {
                var textbox = new ChatContent.Text(data.text,"user");
                showMsg(textbox);
                $("#message").text("");
            }else
            {
                alert("消息发送失败！");
            }
        },
        error: function(e){
            alert(e.name + ": " + e.message + "\n链接失败");
        }
    });
}


function sendMsg()
{
    $.ajax({
        type: "POST",
        url: ServerUrl,
        async: false,
        dataType: "json",
        data: {
            "sender": $("#accountid").val(),
            "receiver":$("#password").val(),
            "text":  $("#message").val()
        },
        beforeSend : function() {
            if(!data.text)
            {
                return false;
            }
        },
        complete: function() {
        },
        success: function(Data) {
            if(Data.info == "1")
            {
                var textbox = new ChatContent.Text(data.text,"user");
                showMsg(textbox);
                $("#message").text("");
            }else
            {
               alert("消息发送失败！");
            }
        },
        error: function(e){
            alert(e.name + ": " + e.message + "\n链接失败");
        }
    });
}

function showMsg(textBox) {
    
    $("#ChatContentBox").append(textBox.getDom());
    var this_box = $("#ChatContentBox" + String(ChatContent.counter));
    this_box.animate({top: "-20px", opacity: "1"}, 200, $.easeInOutCubic);
    chat_contents_height += this_box.height();
    $("#ChatBox").scrollTop(chat_contents_height);
}

var ChatContent= {
    counter : -1,
    BoxPrefixLeft : '<span class="triangle"></span><span class="contentbox"><p class="txtcontent">',
    BoxPrefixRight : '<span class="triangle"></span><span class="contentbox"><p class="txtcontent">',
    BoxSuffix : '</p></span></div>',
    BoxDivHeadLeft : '<div class="ChatContent clearfix" ',
    BoxDivHeadRight : '<div class="ChatContent clearfix right" ',
    getDivHeadLeft : function() {
        return this.BoxDivHeadLeft + 'id="ChatContentBox' + String(this.counter) + '">';
    },

    getDivHeadRight : function() {
        return this.BoxDivHeadRight + 'id="ChatContentBox' + String(this.counter) + '">';
    },
    Text : function(_text, _speaker) {
        this.text = _text;
        this.speaker = _speaker;
        this.dom = String("");

        this.getDom = function() {
            if(this.dom === "") {
                this.init();
            }
            return this.dom;
        };

        this.init = function() {
            ++ChatContent.counter;
            if(this.speaker === "user") {
                this.dom = ChatContent.getDivHeadRight() + '<img src="../img/photo1.jpg">' + ChatContent.BoxPrefixRight + this.text + ChatContent.BoxSuffix;
            }else {
                this.dom = ChatContent.getDivHeadLeft() + '<img src="../img/photo1.jpg">' + ChatContent.BoxPrefixLeft + this.text + ChatContent.BoxSuffix;
            }
        };

        return this;
    }
}
