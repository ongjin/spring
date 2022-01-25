/* 모바일 댓글창 열기 */
function opneComment() {

}




$(" .posting > p").each(function () {
    var content = $('.posting > p');
    var content_txt = content.text();
    var content_html = content.html();

    var content_txt_short = content_txt.substring(0, 100) + "...";

    var btn_more = $('<a href="javascript:void(0)" class="more">더보기</a>');

    $(this).append(btn_more);

    if (content_txt.length >= 100) {
        content.html(content_txt_short)
    } else {
        btn_more.hide()
    }


    btn_more.click(toggle_content());
    function toggle_content() {
        if ($(this).hasClass('short')) {
            // 접기 상태
            $(this).html('더보기');
            content.html(content_txt_short)
            $(this).removeClass('short');
        } else {
            // 더보기 상태
            $(this).html('접기');
            content.html(content_html);
            $(this).addClass('short');

        }
    }
});

function adjustHeight() {
    var textEle = $('textarea');
    textEle[0].style.height = 'auto';
    var textEleHeight = textEle.prop('scrollHeight');
    textEle.css('height', textEleHeight);
};

$("textarea").on("input", function(){

    adjustHeight(); // 함수를 실행하면 자동으로 textarea의 높이 조절
});


$(".dark-brown").on("click", function(){
    const text = $(".insertReply");

    if(text.css("display") == "flex"){

        text.css("display", "none");
        
    }else{

        text.css("display", "flex");
    }
});