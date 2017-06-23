/**
 * Created by DingYiwei on 2017/6/19.
 */
var currentUserId;
var userPhoto;
var currentTab = 0;

$(function () {
    $("[data-toggle='popover']").popover();
    var messagesDiv = $("#messages");
    messagesDiv.height(messagesDiv.parent().height() - 144);
    currentUserId = $.cookie("accountid");
    loadUserInfo();
    loadRecentMessageList();
    loadGroups();
    self.setInterval("refresh()", 2000);
});

function loadUserInfo() {
    $.getJSON("../AccountInfoServlet", {accountid: currentUserId}, function (userInfo) {
        var photo = userInfo[0]["PHOTO"];
        userPhote = photo;
        var nickname = userInfo[0]["NICKNAME"];
        $("#photo").find("img").attr("src", photo);
        $("#nicknameOnTop").find("p").text(nickname);
    })
}

function loadRecentMessageList() {
    currentTab = 0;
    $.getJSON("../FindNotReadFriendsServlet", {accoutid: currentUserId}, function (messageList) {
        var messageDiv = $("#messageList");
        messageDiv.empty();
        for (var i = 0; i < messageList.length; ++i) {
            var message = messageList[i];
            var lastMessage = message["TEXT"];
            if (lastMessage.length > 24) {
                lastMessage = lastMessage.substr(0, 22) + "...";
            }
            var id = message["SENDER"];
            var nickname = message["NICKNAME"];
            var time = message["TIME"].substr("xxxx-xx-xx ".length, "xx:xx:xx".length);
            messageDiv.append("<li id='message" + id + "' onclick='chatTo(this.id)' class='list-group-item' " +
                "style='background: transparent; border: none; border-radius: 0; border-top: 1px solid #555555;'>" +
                "<div class='row'>" +
                "<div class='col-xs-2'>" +
                "<img src='" + message["PHOTO"] + "' class='img-circle' style='height: 40px; width: 40px;'></div>" +
                "<div class='col-xs-10'>" +
                "<div class='row'>" +
                "<div class='col-xs-10'>" + nickname +
                "<div class='col-xs-2 pull-right'>" + time +
                "</div>" +
                "<div class='row' style='margin-left: 0'>" + lastMessage +
                "<span class='badge pull-right' style='background: #900000'>" + message["UnreadNum"] +
                "</span></div></div></div></li>");
        }
        if (messageList.length == 0) {
            messageDiv.append("<li class='list-group-item' style='background: transparent; border: none; border-radius: 0; border-top: 1px solid #555555;'>暂无未读消息</li>");
        }
    });
}

function loadFriendList() {
    currentTab = 1;
    $.getJSON("../ReadUserFriendsServlet", {accountid: currentUserId}, function (friendList) {
        var messageDiv = $("#messageList");
        messageDiv.empty();
        var strToAppend = "";
        var groupCount = 0;
        for (var group in friendList) {
            strToAppend += "<div id='group" + groupCount + "' class='panel-group' style='margin: 0;'>" +
                "<div class='panel panel-default' style='background: transparent; border: none; border-radius: 0; border-top: 1px solid #555555;'>" +
                "<div class='panel-heading' style='background: transparent; color: white; border: none'>" +
                "<label data-toggle='collapse' data-parent='#group" + groupCount + "' href='#collapse" + groupCount + "'>" +
                group + "(" + friendList[group].length + ")</label>" +
                "</div>" +
                "<div id='collapse" + groupCount + "' class='panel-collapse collapse'>" +
                "<div class='panel-body' style='padding: 0; border: none;'>" +
                "<ul class='list-group' style='margin: 0'>";
            for (var i = 0; i < friendList[group].length; ++i) {
                var friend = friendList[group][i];
                strToAppend += "<li id='friend" + friend["ACCOUNTID"] + "' " +
                    "onclick='chatToFriend(this.id)' class='list-group-item' " +
                    "style='background: transparent; border: none; border-radius: 0; border-top: 1px solid #555555;'>" +
                    "<div class='row'>" +
                    "<div class='col-xs-3'>" +
                    "<img src='" + friend["PHOTO"] + "' class='img-circle' style='height: 30px; width: 30px;'>" +
                    "</div>" +
                    "<div class='col-xs-9'>" + friend["NICKNAME"] +
                    "</div>" +
                    "</li>";
            }
            strToAppend += "</ul>" +
                "</div></div></div></div>";
            groupCount++;
        }
        messageDiv.append(strToAppend);
    });
}

function loadApplyList() {
    currentTab = 2;
    $.getJSON("../ReadUserNewFriendServlet", {accountid: currentUserId}, function (applyList) {
        var messageDiv = $("#messageList");
        messageDiv.empty();
        for (var i = 0; i < applyList.length; ++i) {
            var apply = applyList[i];
            messageDiv.append("<li class='list-group-item' " +
                "style='background: transparent; border: none; border-radius: 0; border-top: 1px solid #555555'>" +
                "<div class='row'>" +
                "<div class='col-xs-2'>" +
                "<img src='" + apply["PHOTO"] + "' class='img-circle' style='height: 40px; width: 40px;'></div>" +
                "<div class='col-xs-8'>" + apply["NICKNAME"] + "(" + apply["FRIENDID"] + ")<br>请求添加你为好友</div>" +
                "<span id='user" + apply["FRIENDID"] + "' class='glyphicon glyphicon-ok-circle' " +
                "style='font-size: xx-large; color: dodgerblue' onclick='agree(this.id)'></span>" +
                "<span class='glyphicon glyphicon-remove-circle' style='font-size: xx-large; color: #900000'></span>" +
                "</li>");
        }
        if (applyList.length == 0) {
            messageDiv.append("<li class='list-group-item' style='background: transparent; border: none; border-radius: 0; border-top: 1px solid #555555;'>暂无新的申请</li>");
        }
    });
}

function agree(applyUserId) {
    $.getJSON("../AgreeNewFriendServlet", {
        accountid: currentUserId,
        friendid: applyUserId.substr("user".length)
    }, function (agreeInfo) {
        var noticeBox = $("#notice");
        var noticeBody = noticeBox.find(".modal-body");
        if (agreeInfo["ifNew"] == 1) {
            noticeBody.text("好友添加成功");
        } else {
            noticeBody.text("好友添加失败");
        }
        noticeBox.modal();
        loadApplyList();
    });
}

function searchUsers() {
    var searchOption = $("#searchOption").attr("value");
    var searchContent = $("#searchContent").val();
    var sendData = {};
    sendData.accountid = currentUserId;
    sendData[searchOption] = searchContent;
    $.getJSON("../SearchFriendServlet", sendData, function (userList) {
        var list = $("#searchList");
        list.empty();
        for (var i = 0; i < userList.length; ++i) {
            var user = userList[i];
            var isFriend = user["isFriend"];
            var spanId = isFriend == 0 ? "friend" : "message";
            var classAndOnclick = isFriend == 0 ?
                "class='glyphicon glyphicon-send' onclick='addFriend(this.id)'" :
                "class='glyphicon glyphicon-comment' onclick='chatTo(this.id)'";
            list.append("<li class='list-group-item'>" +
                "<div class='row'>" +
                "<div class='col-xs-2'>" +
                "<img src='" + user["PHOTO"] + "' class='img-circle' style='height: 30px; width: 30px'></div>" +
                "<div class='col-xs-9'>" + user["NICKNAME"] + "(" + user["ACCOUNTID"] + ")" + "</div>" +
                "<span id='" + spanId + user["ACCOUNTID"] + "' style='color: dodgerblue; font-size: x-large;' " +
                classAndOnclick + "</span>" + "</div></li>");
        }
        if (userList.length == 0) {
            list.append("<li class='list-group-item'>未找到匹配用户</li>");
        }
    });
}

function addFriend(friendId) {
    var userId = friendId.substr("friend".length);
    $.getJSON("../AddFriendServlet", {
        accountid: currentUserId,
        friendid: userId
    }, function () {
        $("#searchUser").modal("hide");
        var noticeBox = $("#notice");
        noticeBox.find(".modal-body").text("请求已发送");
        noticeBox.modal();
    });
}

function changeSearchOption() {
    var searchOption = $("#searchOption");
    var searchContent = $("#searchContent");
    if (searchOption.attr("value") == "findaccountid") {
        searchOption.attr("value", "nickname");
        searchOption.text("昵称▼");
        searchContent.attr("placeholder", "请输入昵称");
    } else {
        searchOption.attr("value", "findaccountid");
        searchOption.text("账号▼");
        searchContent.attr("placeholder", "请输入账号");
    }
    if (searchContent.val() != "") {
        searchUsers();
    }
}

function loadGroups() {
    $.getJSON("../ShowAllGroupsServlet", {accountid: currentUserId}, function (groupList) {
        var groupBox = $("#groupList");
        for (var i = 0; i < groupList.length; ++i) {
            var group = groupList[i];
            groupBox.append("<li id='allgroup" + group["GROUPID"] + "' class='list-group-item' onclick='changeGroupOfFriend(this.id)'>" +
                group["NAME"] + "</li>");
        }
    })
}

function changeGroupOfFriend(groupId) {
    groupId = groupId.substr("allgroup".length);
    var friendId = $("#div_userinfo").find("label").attr("id");
    $.getJSON("../MoveFriendToGroupServlet", {
        accountid: currentUserId,
        friendid: friendId,
        groupid: groupId
    }, function () {
        $("#groupSetting").modal("hide");
        var noticeBox = $("#notice");
        noticeBox.find(".modal-body").text("修改成功");
        noticeBox.modal();
        loadFriendList();
    })
}

function deleteFriend() {
    var friendId = $("#div_userinfo").find("label").attr("id");
    $.getJSON("../ConfirmDeleteFriendServlet", {accountid: currentUserId, friendid: friendId}, function () {
        var noticeBox = $("#notice");
        noticeBox.find(".modal-body").text("删除成功");
        noticeBox.modal();
        loadFriendList();
        document.getElementById("chatMask").style.display = "";
    });
}

function showNotice(noticeMessage) {
    var noticeBox = $("#notice");
    noticeBox.find(".modal-body").text(noticeMessage);
    noticeBox.modal();
}
