/**
 * Created by DingYiwei on 2017/6/19.
 */
var friendList;
var currentUserId;

$(function () {
    $("[data-toggle='popover']").popover();
    $.getJSON("FriendServlet", {uid: currentUserId}, function (friends) {
        for (var friend in friends) {
            var friendInfo;
            friendInfo["nickname"] = friend["nickname"];
            friendInfo["gender"] = friend["gender"];
            friendInfo["birthday"] = friend["birthday"];
            friendInfo["photo"] = friend["photo"];
            friendList[friend["id"]] = friendInfo;
        }
    });
    loadRecentMessages(currentUserId);
});

function loadRecentMessages(userId) {
    $.getJSON("MessageServlet", {uid: userId}, function (messageList) {
        var messageDiv = $("#messageList");
        messageDiv.clear();
        for (var message in messageList) {
            var friendId = message["sender"] == currentUserId ? message["receiver"] : message["sender"];
            messageDiv.append("<li class='list-group-item'>" +
                "<div class='row'>" +
                "<div class='col-xs-3'>" +
                "<img src='" + friendList[friendId]["photo"] + "' class='img-circle' style='height: 30px; width: 30px;'></div>" +
                "<div class='col-xs-6'>"+friendList[friendId]["nickname"]+"<br>"+)
        }
    })
}

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
    })
}
