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
    //chatTo("123");
    if(document.getElementById("sendmsg123"))
        alert("123");
   //alert($("#sendmsg"));
   // alert($("#div_userinfo").find("label").attr("id"));
   
    array=[];
    array.push("1");
    array.push("2");
    json={"name":array};
    //if("1,2"===array.toString())
    alert(json.name[0]);

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
    var  messageList;
    var csender =  $("#div_userinfo").find("label").attr("id")
    $.ajax({
        type: "POST",
        url: ServerUrl,
        async: false,
        dataType: "json",
        data: {
            "id": id,
            "reviver":$.cookie("accountid")
        },
        success: function(Data) {
            messageList=Data;
        },
        error: function(e){
            alert(e.name + ": " + e.message + "\n链接失败");
        }
    });

    haveread=[];

   for(var message in messageList)
   {
       if(message.sender == csender)
       {
           var textbox = new ChatContent.Text(data.text,"sender");
           showMsg(textbox);
           haveread.push(message.messageid);
       }else
       {
           var friend = document.getElementById("friend"+message.accountid);
           if(friend)
           {
               var count=parseInt(friend.find("span").text())+1;
               friend.find("span").text(count);
           }else
           {
               messageDiv.append("<li id='message" + message.friendId + "' onclick='chatTo(this.id)' class='list-group-item' style='background: transparent; border: none; border-radius: 0; border-top: 1px solid #555555;'>" +
                   "<div class='row'>" +
                   "<div class='col-xs-2'>" +
                   "<img src='" + message["friendPhoto"] + "' class='img-circle' style='height: 40px; width: 40px;'></div>" +
                   "<div class='col-xs-10'>" +
                   "<div class='row'>" +
                   "<div class='col-xs-10'>" + message.friendNickname +
                   "<div class='col-xs-2 pull-right'>" + message.lastTime +
                   "</div>" +
                   "<div class='row' style='margin-left: 0'>" + lastMessage +
                   "<span class='badge pull-right' style='background: #900000'>" + message.messageNumber +
                   "</span></div></div></div></li>");
           }

       }
   }
}

function readMessage(recevier)
{
    
}

function readMsgHistory()
{

}


function chatToFriend(Sid)
{
    id = Sid.substr("friend".length);
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


function chatTo(Sid) {
    id=Sid.substr("message".length);
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
    $("#"+Sid).remove();

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
