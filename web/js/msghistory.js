/**
 * Created by 22150 on 2017/6/21.
 */

var currentUserId = "1001";
var currentFriend = "1002";

$(document).ready(function () {
    //showMsgHistory();
    //alert(currentFriend);
  
})



function showMsgHistory(msgList) {
    //var msgList=[{"messageid":"1001","text":"你好啊"},{"messageid":"1002","text":"你好啊,haha"},{"messageid":"1003","text":"你好啊,hhhhhhh"}];
    $("#msgHistory").empty();
    if(!msgList || msgList.length<=0)
        return ;
    var str = '<form action="" method="post"></form> <label>'
    for(var i=0; i<msgList.length; i++)
    {
        var msg = msgList[i].SENDER+":"+ msgList[i].TEXT +"--"+ msgList[i].TIME;
        str+=' <input type="checkbox" name = "checkItem" value= "' + msgList[i].MESSAGEID + '"/>' + msg + '<br/>';
    }
    str+='</label></form><br/>';
    str+=' <label>全选：<input id= "isSelectALL" type="checkbox" value="select" onclick="selectAllOrNot()"/></label>'
    $("#msgHistory").append(str);
}


function selectAllOrNot()
{
    if($("#isSelectALL").prop("checked"))
        $("[name=checkItem]:checkbox").attr("checked",true);
    else
        $("[name=checkItem]:checkbox").attr("checked",false);
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
    $('#ChatHistoryModal').modal('show');
}


function  loadInfo() {
    var info = readFriendInfo(currentUserId);
    if(info)
    {
        $("#nickname").val(info.nickname);
        $("#gender").val(info.gender).text(info.gender);
        $("#birthday").val(info.birthday);
    }
    $('#ChangeInfoModal').modal('show');
    
}
function changeInfo()
{
    $.ajax({
        type: "POST",
        url: ServerUrl,
        async: false,
        dataType: "json",
        data: {
            "accountid":currentUserId,
            "nickname":  $("#nickname").val(),
            "gender"  :  $("#gender").val(),
            "birthday":  $("#gender").val()
        },
        success:function (data) {
            if(data)
                alert("修改成功");
        },
        errer:function () {
            alert("修改失败");
        }
    })
    loadInfo();
}