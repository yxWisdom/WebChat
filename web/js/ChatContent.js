/**
 * Created by wisdom on 2017/6/19.
 */



var chat_contents_height = 20;
var friendPhoto;
var currentFriend = null;

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

})

function refresh() {
    if (!currentFriend)
        return;
    var msgList;
    // var sender = $("#div_userinfo").find("label").attr("id")
    // var receiver = currentUserId;
    msgList = readNotReadMessage();
    for (var i in msgList) {
        if (msgList[i].TEXT) {
            var textbox = new ChatContent.Text(msgList[i].TEXT, "friend");
            showMsg(textbox);
        }
    }
    if (currentTab == 0) {
        loadRecentMessageList();
    }
}

// function readNotReadFriend() {
//     var friendList = null;
//     $.ajax({
//         type: "POST",
//         url: "",
//         async: false,
//         dataType: "json",
//         data: {
//             "accountid": currentUserId
//         },
//         success: function (Data) {
//             friendListList = Data;
//         },
//         error: function (e) {
//             alert(e.name + ": " + e.message + "\n链接失败");
//         }
//     });
//     return friendList;
// }


function readNotReadMessage() {
    var messageList = null;
    //alert(currentFriend+" "+currentUserId);
    $.ajax({
        type: "POST",
        url: "../ReadOneNotReadMegServlet",
        async: false,
        dataType: "json",
        data: {
            "sender": currentFriend,
            "receiver": currentUserId
        },
        success: function (Data) {
            messageList = Data;
        },
        error: function (e) {
            //alert(e.name + ": " + e.message + "\n链接失败");
        }
    });
    return messageList;
}

// function readMsgHistory() {
//     var msgHistory = null;
//     $.ajax({
//         type: "POST",
//         url: ServerUrl,
//         async: false,
//         dataType: "json",
//         data: {
//             "receiver": receiver
//         },
//         success: function (Data) {
//             msgHistory = Data;
//         },
//         error: function (e) {
//             alert(e.name + ": " + e.message + "\n链接失败");
//         }
//     });
//     return messageList;
// }


function chatToFriend(Sid) {
    document.getElementById("chatMask").style.display = "none";
    $("#ChatContentBox").empty();
    id = Sid.substr("friend".length);
    var data = null; //{"accountid":"1001","nickname":"hello","gender":"男","birthday":"1996-12-05"}//readFriendInfo(id);
    data = readFriendInfo(id);
    var str;
    var birthday = data.BIRTHDAY;
    birthday = birthday.substr(0, 10);
    str = "账号：" + data.ACCOUNTID + "<br>" + "昵称：" + data.NICKNAME + "<br>" + "性别：" + data.GENDER + "<br>" + "生日：" + birthday;
    var label = $("#div_userinfo").find("label");
    label.text(data.NICKNAME);
    label.attr("data-original-title", data.NICKNAME);
    label.attr("title", data.NICKNAME);
    label.attr("data-content", str);
    label.attr("id", data.ACCOUNTID);
    friendPhoto = data.PHOTO;
    currentFriend = data.ACCOUNTID;

}


function chatTo(Sid) {
    document.getElementById("chatMask").style.display = "none";
    id = Sid.substr("message".length);
    $("#ChatContentBox").empty();
    //var  data = {"accountid":"1001","nickname":"hello","gender":"男","birthday":"1996-12-05"}//readFriendInfo(id);
    var data = null;
    while (!data) {
        data = readFriendInfo(id);
    }
    var str;
    str = "账号：" + data.ACCOUNTID + "<br>" + "昵称：" + data.NICKNAME + "<br>" + "性别：" + data.GENDER + "<br>" + "出生日期：" + data.BIRTHDAY;
    var label = $("#div_userinfo").find("label");
    label.text(data.NICKNAME);
    label.attr("data-original-title", data.NICKNAME);
    label.attr("title", data.NICKNAME);
    label.attr("data-content", str);
    label.attr("id", data.ACCOUNTID);
    $("#" + Sid).remove();
    friendPhoto = data.PHOTO;
    currentFriend = data.ACCOUNTID;
    var msgList = readNotReadMessage();
    for (var i in msgList) {
        var textbox = new ChatContent.Text(msgList[i].TEXT, "friend");
        showMsg(textbox);
    }
}
function readFriendInfo(id) {
    var retData = null;
    $.ajax({
        type: "POST",
        url: "../AccountInfoServlet",
        async: false,
        dataType: "json",
        data: {
            "accountid": id
        },
        beforeSend: function () {
            if (!id)
                return false;
        },
        success: function (Data) {
            retData = Data[0];
        },
        error: function (e) {
            alert(e.name + ": " + e.message + "\n链接失败");
        }
    });
    return retData;
}


function sendMsg() {
   // alert(currentFriend+" "+currentUserId);
    var text = $("#message").val();
    $.ajax({
        type: "POST",
        url: "../SendMessageServlet",
        async: false,
        dataType: "json",
        data: {
            "sender": currentUserId,
            "receiver": currentFriend,
            "text": text
        },
        beforeSend: function () {
            if (!text) {
                return false;
            }
        },
        success: function (Data) {
            if (Data[0].SendMessage == "1") {
                var textbox = new ChatContent.Text(text, "user");
                showMsg(textbox);
                $("#message").val("");
            } else {
                alert("消息发送失败！");
            }
        },
        error: function (e) {
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

var ChatContent = {
    counter: -1,
    BoxPrefixLeft: '<span class="triangle"></span><span class="contentbox"><p class="txtcontent">',
    BoxPrefixRight: '<span class="triangle"></span><span class="contentbox"><p class="txtcontent">',
    BoxSuffix: '</p></span></div>',
    BoxDivHeadLeft: '<div class="ChatContent clearfix" ',
    BoxDivHeadRight: '<div class="ChatContent clearfix right" ',
    getDivHeadLeft: function () {
        return this.BoxDivHeadLeft + 'id="ChatContentBox' + String(this.counter) + '">';
    },

    getDivHeadRight: function () {
        return this.BoxDivHeadRight + 'id="ChatContentBox' + String(this.counter) + '">';
    },
    Text: function (_text, _speaker) {
        this.text = _text;
        this.speaker = _speaker;
        this.dom = String("");

        this.getDom = function () {
            if (this.dom === "") {
                this.init();
            }
            return this.dom;
        };

        this.init = function () {
            ++ChatContent.counter;
            if (this.speaker === "user") {
                this.dom = ChatContent.getDivHeadRight() + '<img src="' + userPhoto + '">' + ChatContent.BoxPrefixRight + this.text + ChatContent.BoxSuffix;
            } else {
                this.dom = ChatContent.getDivHeadLeft() + '<img src="' + friendPhoto + '">' + ChatContent.BoxPrefixLeft + this.text + ChatContent.BoxSuffix;
            }
        };

        return this;
    }
}
