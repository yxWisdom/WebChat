/**
 * Created by 22150 on 2017/6/21.
 */

var currentUserId = "1001";
var currentFriend = "1002";

$(document).ready(function () {
    //showMsgHistory();
    alert(currentFriend);
})



function showMsgHistory(msgList) {
    //var msgList=[{"messageid":"1001","text":"你好啊"},{"messageid":"1002","text":"你好啊,haha"},{"messageid":"1003","text":"你好啊,hhhhhhh"}];
    if(!msgList || msgList.length<=0)
        return ;
    $("#msgHistory").empty();
    var str = '<form action="" method="post"></form> 聊天历史纪录<br/> <label>'
    for(var i=0; i<msgList.length; i++)
    {
        var msg = msgList[i].SENDER+":"+ msgList[i].TEXT +"--"+ msgList[i].TIME;
        str+=' <input type="checkbox" name = "checkItem" value= "' + msgList[i].MESSAGEID + '"/>' + msg + '<br/>';
    }
    str+='</label></form>';
    $("#msgHistory").append(str);
}


function selectAll() {
    $("[name=checkItem]:checkbox").attr("checked",true);
}
function reSelect() {
    $("[name=checkItem]:checkbox").attr("checked",false);
}

function deleteMsg() {
    var msgList=[];
    $("[name=checkItem]:checkbox").each(function () {
        if($(this).is(":checked"))
        {
            msgList.push($(this).attr("value"));
        }
    });
    if(msgList.length>0)
    {
        $.ajax({
            type: "POST",
            url: "../DeleteHistoryServlet",
            async: false,
            dataType: "json",
            data: {
                "accountid": currentUserId,
                "messageid":msgList
            },

            success: function(Data) {
                msgHistory=Data;
            },
            error: function(e){
                alert(e.name + ": " + e.message + "\n链接失败");
            }
        });
    }
    askMsgHistroy();
}

function askMsgHistroy()
{
    var msgHistory;
    $.ajax({
        type: "POST",
        url: "../ChatHistoryServlet",
        async: false,
        dataType: "json",
        data: {
            "accountid": currentUserId,
            "friendid":currentFriend
        },
        success: function(Data) {
            msgHistory=Data;
        },
        error: function(e){
            alert(e.name + ": " + e.message + "\n链接失败");
        }
    });
    showMsgHistory(msgHistory);
}