/**
 * Created by wisdom on 2017/6/19.
 */

var chat_contents_height = 20;
$(document).ready(function () {
    // $("#ChatContent").empty();
    // $("#ChatInput").val("");
    $("#sendmsg").click(function () {
        var text=$("#message").val();
        var textbox = new ChatContent.Text(text,"user");
        showMsg(textbox);
    })
})

function showMsg(textBox) {
    
    $("#ChatContentBox").append(textBox.getDom());
    var this_box = $("#ChatContentBox" + String(ChatContent.counter));
    this_box.animate({top: "-20px", opacity: "1"}, 200, $.easeInOutCubic);
    chat_contents_height += this_box.height();
    $("#ChatBox").scrollTop(chat_contents_height);
}

var ChatContent= {
    counter : -1,
    BoxPrefixLeft : '<span class="triangle"></span><span class="contentbox"><p class="content">',
    BoxPrefixRight : '<span class="triangle"></span><span class="contentbox"><p class="content">',
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
