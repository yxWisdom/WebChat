/**
 * Created by wisdom on 2017/6/19.
 */



var chat_contents_height = 20;
var userPhoto=-1;
var friendPhoto
// var currentFriend;

$(document).ready(function () {
    // $("#ChatContent").empty();
    // $("#ChatInput").val("");

    // var int=self.setInterval("readMsg()",1000);
    //var Test=[{"a":"aaa","b":"231"},{"a":"haha","b":"231"}];
    //alert(Test.length);
    //chatTo("123");
    //$.cookie("accountid","hah");
    //if(document.getElementById("sendmsg123"))
        //alert("123");
    //alert(currentUserId);
   //alert($("#sendmsg"));
   // alert($("#div_userinfo").find("label").attr("id"));
   
    // array=[];
    // array.push("1");
    // array.push("2");
    // json={"name":array};
    // //if("1,2"===array.toString())
    // alert(json.name[0]);

    $("#sendmsg").click(function () {
        sendMsg();
    })
})

function refresh()
{
    var  messageList;
    var sender =  $("#div_userinfo").find("label").attr("id")
    var receiver=currentUserId;
    messageList=readNotReadMessage(sender,receiver);
    for(var message in messageList)
    {
        if(message.text)
        {
            var textbox = new ChatContent.Text(text, "friend");
            showMsg(textbox);
        }
    }
    loadRecentMessageList();
}

function readNotReadFriend()
{
    var friendList=null;
    $.ajax({
        type: "POST",
        url:"",
        async: false,
        dataType: "json",
        data: {
            "accountid":currentUserId
        },
        success: function(Data) {
            friendListList=Data;
        },
        error: function(e){
            alert(e.name + ": " + e.message + "\n链接失败");
        }
    });
    return friendList;
}



function readNotReadMessage(sender,receiver)
{
    var messageList=null;
    $.ajax({
        type: "POST",
        url: ServerUrl,
        async: false,
        dataType: "json",
        data: {
            "sender":sender,
            "receiver":receiver
        },
        success: function(Data) {
            messageList=Data;
        },
        error: function(e){
            alert(e.name + ": " + e.message + "\n链接失败");
        }
    });
    return messageList;
}

function readMsgHistory()
{
    var msgHistory=null;
    $.ajax({
        type: "POST",
        url: ServerUrl,
        async: false,
        dataType: "json",
        data: {
            "receiver":receiver
        },
        success: function(Data) {
            msgHistory=Data;
        },
        error: function(e){
            alert(e.name + ": " + e.message + "\n链接失败");
        }
    });
    return messageList;
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
    currentPhoto = data.photo;
    currentFriend = data.accountid;
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
    currentPhoto = data.photo;
    var msgList = readNotReadMessage(id,currentUserId);
    currentFriend = data.accountid;
    for(var msg in msgList) {
        var textbox = new ChatContent.Text(text, "friend");
        showMsg(textbox);
    }
}
function readFriendInfo(id) {
    var retData=null;
    $.ajax({
        type: "POST",
        url: "../ReadUserFriendsServlet",
        async: false,
        dataType: "json",
        data: {
            "accountid": id
        },
        beforeSend : function() {
            if(!id)
                return false;
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



// function readMsg() {
//     $.ajax({
//         type: "POST",
//         async: false,
//         url: ServerUrl,
//         dataType: "json",
//         data: {
//             "sender": $("#accountid").val(),
//             "receiver":$("#password").val(),
//             "text":  $("#message").val()
//         },
//         beforeSend : function() {
//             if(!data.text)
//             {
//                 return false;
//             }
//         },
//         complete: function() {
//         },
//         success: function(Data) {
//             if(Data.info == "1")
//             {
//                 var textbox = new ChatContent.Text(data.text,"user");
//                 showMsg(textbox);
//                 $("#message").text("");
//             }else
//             {
//                 alert("消息发送失败！");
//             }
//         },
//         error: function(e){
//             alert(e.name + ": " + e.message + "\n链接失败");
//         }
//     });
// }


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
                this.dom = ChatContent.getDivHeadRight() + '<img src="../img/randomphoto'+userPhoto+'.JPG">' + ChatContent.BoxPrefixRight + this.text + ChatContent.BoxSuffix;
            }else {
                this.dom = ChatContent.getDivHeadLeft() + '<img src="../img/randomphoto'+friendPhoto+'.JPG">' + ChatContent.BoxPrefixLeft + this.text + ChatContent.BoxSuffix;
            }
        };

        return this;
    }
}
