/**
 * Created by DingYiwei on 2017/6/19.
 */
var currentUserId;

$(function () {
    $("[data-toggle='popover']").popover();
    currentUserId = $.cookie("accountid");
    loadRecentMessageList(currentUserId);
});

// MessageList
// friendId, friendNickname, friendPhoto, lastTime, lastMessage
function loadRecentMessageList(userId) {
    $.getJSON("MessageListServlet", {uid: userId}, function (messageList) {
        var messageDiv = $("#messageList");
        messageDiv.clear();
        for (var message in messageList) {
            var lastMessage = message["lastMessage"];
            if (lastMessage.length > 22) {
                lastMessage = lastMessage.substr(0, 20) + "...";
            }
            var nickname = message["friendNickname"];
            if (nickname.length > 18) {
                nickname.length = 18;
                nickname += message["lastTime"];
            }
            messageDiv.append("<li class='list-group-item'>" +
                "<div class='row'>" +
                "<div class='col-xs-3'>" +
                "<img src='" + message["friendPhoto"] + "' class='img-circle' style='height: 30px; width: 30px;'></div>" +
                "<div class='col-xs-6'>" + message["friendNickname"] + "<br>" + lastMessage + "</div></div></li>");
        }
    })
}

// FriendList
// id, nickname, photo
function loadFriendList(userId) {
    $.getJSON("FriendListServlet", {uid: userId}, function (friendList) {
        var messageDiv = $("#messageList");
        messageDiv.clear();
        for (var friend in friendList) {
            messageDiv.append("<li class='list-group-item'>" +
                "<div class='row'>" +
                "<div class='col-xs-3'>" +
                "<img src='"+friend["photo"]+"' class='img-circle' style='height: 30px; width: 30px'></div>" +
                "<div class='col-xs-6'>"+friend["nickname"]+"</div></div></li>");
        }
    });
}

// FriendApply
// id, nickname, photo
function loadApplyList(userId) {
    $.getJSON("ApplyListServlet",{uid:userId},function (applyList) {
        var messageDiv=$("#messageList");
        messageDiv.clear();
        for(var apply in applyList) {
            messageDiv.append("<li class='list-group-item'>" +
                "<div class='row' " +
                "<div class='col-xs-3'>" +
                "<img src='"+apply["photo"]+"' class='img-circle' style='height: 30px; width: 30px;'></div>" +
                "<div class='col-xs-6'>"+apply["nickname"]+"("+apply["id"]+")请求添加你为好友</div></li>");
        }
    });
}

// Users
// photo, nickname, id
function searchUsers(searchContentId) {
    var userId = document.getElementById(searchContentId).value;
    $.getJSON("SearchUserServlet", {uid: userId}, function (userList) {
        var list = $("#searchList");
        list.clear();
        for (var user in userList) {
            list.append("<li class='list-group-item'>" +
                "<div class='row'>" +
                "<div class='col-xs-3'>" +
                "<img src='" + user["photo"] + "' class='img-circle' style='height: 30px; width: 30px'></div>" +
                "<div class='col-xs-6'>" + user["nickname"] + "(" + user["id"] + ")" + "</div>" +
                "<span id='friend" + user["id"] + "' class='glyphicon glyphicon-send' onclick='addFriend(this.id)'></span>" +
                "</div></li>");
        }
    })
}

function addFriend(friendId) {
    var userId = friendId.substr("friend".length);
    $.getJSON("AddFriendServlet", {uid: userId}, function () {
        var noticeBox = $("#notice.modal-body");
        noticeBox.clear();
        noticeBox.append("请求已发送");
        $("#notice").modal();
    });
}
