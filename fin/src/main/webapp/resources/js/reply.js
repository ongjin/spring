
// 댓글 목록 조회
function selectReplyList() {

    // 비동기 통신(ajax)를 이용해서 JSON 형태로 댓글 목록 조회 및 출력
    $.ajax({
        url: contextPath + "/reply/select",
        data: { "boardNo": boardNo },
        type: "GET",
        dataType: "JSON", // JS 객체 형태의 문자열, 응답 데이터 형식이 JSON 형식임을 알려주어 
        // 자동으로 JS 객체로 변환을 시킬 수 있게함
        success: function (result) {

            // 기존 댓글 영역 내용 삭제
            // $("#replyListArea").html("");
            $("#replyListArea").empty();

            // rList 요소를 순차적으로 반복 접근
            $.each(result, function (i, reply) {
                //const li = $("<li>").addClass("reply-row");
                const li = $('<li class="reply-row">');

                // 자식 댓글인 경우 li요소에 클래스 추가
                if (reply.parentReplyNo != 0) li.addClass("child-reply");

                const div = $('<div>');

                const rWriter = $('<p class="rWriter">').text(reply.memberName);
                const rDate = $('<p class="rDate">').text('작성일 : ' + reply.replyCreateDate);

                // div에 자식으로 rWriter, rDate 추가
                div.append(rWriter, rDate);

                // 댓글 내용
                const rContent = $('<p class="rContent">').html(reply.replyContent);

                li.append(div, rContent);


                const replyBtnArea = $('<div class="replyBtnArea">');

                // 로그인이 되어있는 경우
                if (loginMemberNo != "") {
                    const childReply = $("<button>").addClass("btn btn-primary btn-sm ml-1").text("대댓글");

                    // 버튼 클릭 시 대댓글 작성 영역을 출력하는 함수 호출
                    // reply.replyNo : 대댓글의 부모 댓글 번호(parentReplyNo)
                    // this : 이벤트가 발생한 요소 자체

                    childReply.attr("onclick", "showInsertReply(" + reply.replyNo + ", this)");

                    replyBtnArea.append(childReply);
                }


                // 댓글 작성자 == 로그인 멤버 -> 버튼 영역 생성
                if (reply.memberNo == loginMemberNo) {


                    const showUpdate = $('<button>').addClass('btn btn-primary btn-sm ml-1').text('수정');
                    showUpdate.attr("onclick", "showUpdateReply(" + reply.replyNo + ", this)");

                    const deleteReply = $('<button>').addClass('btn btn-primary btn-sm ml-1').text('삭제');
                    deleteReply.attr("onclick", "deleteReply(" + reply.replyNo + ")");

                    replyBtnArea.append(showUpdate, deleteReply);
                }

                li.append(replyBtnArea);

                $("#replyListArea").append(li);
            });
        },
        error: function (req, sta, er) {
            console.log("댓글 목록 조회 실패");
            console.log(req.responseText);
        }
    });
}


// -----------------------------------------------------------------------------------------
// 댓글 등록
function addReply() {

    // 게시글 번호(boardNo), 로그인한 회원 번호(loginMemberNo), 댓글 내용

    if (loginMemberNo == "") { // 로그인이 되어 있지 않은 경우
        alert("로그인 후 이용해 주세요.");

    } else { // 로그인한 경우



        // 댓글 미작성한 경우
        if ($("#replyContent").val().trim().length == 0) {
            alert("댓글을 작성한 후 버튼을 클릭해주세요.");
            $("#replyContent").focus();

        } else { // 댓글을 작성한 경우


            $.ajax({
                url: contextPath + "/reply/insert",
                data: {
                    "memberNo": loginMemberNo,
                    "boardNo": boardNo,
                    "replyContent": $("#replyContent").val()
                },
                type: "POST",
                success: function (result) {

                    if (result > 0) {
                        alert("댓글 삽입 성공");
                        $("#replyContent").val(""); // 작성한 댓글 내용 삭제

                        selectReplyList(); // 댓글 조회 함수를 호출하여 댓글 화면 다시 만들기
                    } else {
                        alert("댓글 삽입 실패");

                    }

                },

                error: function (req, status, error) {
                    console.log("댓글 삽입 실패");
                    console.log(req.responseText);
                }
            });

        }
    }
}

//---------------------------------------------------------------------------
// 댓글 수정폼 전환
function showUpdateReply(replyNo, el) {

    // 이미 열려있는 댓글 수정 화면이 존재하면 닫아주기
    if ($(".replyUpdateContent").length > 0) {

        // 1개 이상 존재 == 이미 다른 댓글 수정화면이 열려 있음
        if (confirm("확인 클릭 시 수정한 댓글 내용이 취소됩니다.")) {
            $(".replyUpdateContent").eq(0).parent().html(beforeReplyRow);
        } else {
            return;
        }

    }


    // 댓글 수정폼 출력 전 원본 모습 저장
    beforeReplyRow = $(el).parent().parent().html();

    // 기존에 작성되어 있던 댓글 내용 저장
    let beforeContent = $(el).parent().prev().html();

    // 이전 댓글 내용의 크로스사이트 스크립트 처리 해제, 개행문자 변경
    // -> 자바스크립트에는 replaceAll() 메소드가 없으므로 정규 표현식을 이용하여 변경
    beforeContent = beforeContent.replace(/&amp;/g, "&");
    beforeContent = beforeContent.replace(/&lt;/g, "<");
    beforeContent = beforeContent.replace(/&gt;/g, ">");
    beforeContent = beforeContent.replace(/&quot;/g, "\"");

    beforeContent = beforeContent.replace(/<br>/g, "\n");
    // textarea -> 서버 : 개행문자 \r\n
    // textarea -> JS : 개행문자 \n

    // 기존 댓글 영역의 내용, 버튼을 삭제하고 새로운 textarea, 버튼 추가
    $(el).parent().prev().remove(); // 이전 내용 삭제

    const textarea = $("<textarea class='replyUpdateContent' rows='3' >").val(beforeContent);
    $(el).parent().before(textarea);

    // 수정 버튼
    const updateReply = $("<button>").addClass("btn btn-primary btn-sm ml-1 mb-4").text("댓글 수정").attr("onclick", "updateReply(" + replyNo + ", this)");

    // 취소 버튼
    const cancelBtn = $("<button>").addClass("btn btn-primary btn-sm ml-1 mb-4").text("취소").attr("onclick", "updateCancel(this)");

    // 기존 버튼 영역 내부를 초기화 후 수정, 삭제 버튼 추가
    const replyBtnArea = $(el).parent();

    $(replyBtnArea).empty();
    $(replyBtnArea).append(updateReply, cancelBtn);

}


//---------------------------------------------------
// 댓글 수정폼 취소
function updateCancel(el) {
    $(el).parent().parent().html(beforeReplyRow);
}

//-----------------------------------------------
// 댓글 수정
function updateReply(replyNo, el) {
    const replyContent = $(el).parent().prev().val();

    $.ajax({
        url: contextPath + "/reply/update",
        data: { "replyNo": replyNo, "replyContent": replyContent },
        type: "POST",
        success: function (result) {
            if (result > 0) {
                swal({ "title": "댓글이 수정되었습니다.", "icon": "success" });
                selectReplyList();
            } else swal({ "title": "댓글 수정 실패", "icon": "error" });

        },
        error: function (req, status, error) {
            console.log("댓글 수정 실패");
            console.log(req.responseText);
        }
    });
}

// 댓글 삭제
function deleteReply(replyNo) {
    swal({
        title: "삭제할꺼?",
        icon: "warning",
        buttons: true,
        dangerMode: true,
    })
        .then((willDelete) => {
            if (willDelete) {
                swal("삭제 함", {
                    icon: "success",
                });
                $.ajax({
                    url: contextPath + "/reply/delete",
                    data: { "replyNo": replyNo },
                    type: "POST",
                    success: function (result) {
                        if (result > 0) {
                            swal({ "title": "댓글 삭제 성공.", "icon": "success" });
                            selectReplyList();
                        } else swal({ "title": "댓글 삭제 실패", "icon": "error" });
        
                    },
                    error: function (req, status, error) {
                        console.log("댓글 삭제 실패");
                        console.log(req.responseText);
                    }
                });
            } else {
                swal("삭제 안해");
            }
        });


}

//------------------------------------------------------
// 이전 대댓글 작성 textarea 요소 기억하기 위한 변수
let beforeChildReplyContent;
// 대댓글 작성폼 추가
function showInsertReply(parentReplyNo, el){

    // 이전 대댓글 작성 textarea가 존재하는 경우
    // + 이전 대댓글 textarea에 내용이 있다면 
    if($(".replyInsertContent").length > 0 && $(beforeChildReplyContent).val().trim().length > 0){
        if(confirm("확인 클릭 시 작성한 내용이 사라집니다.")){

            // 다른 textarea 다음에 존재하는 버튼영역 삭제
            $(beforeChildReplyContent).next().remove();
            $(beforeChildReplyContent).remove();
        }else{
            return;
        }
    }else{
        $(beforeChildReplyContent).next().remove();
        $(beforeChildReplyContent).remove();
    }

    // parentReplyNo : 대댓글 버튼이 클릭된 부모 댓글의 번호
    // el : 대댓글 버튼

    // 대댓글 작성을 위한 textarea 생성
    const textarea = $("<textarea>").addClass("replyInsertContent").attr("rows", "3");

    // 버튼 영역 + 대댓글 등록, 취소 버튼 
    const replyBtnArea = $("<div>").addClass("replyBtnArea");

    const insertChildReply = $("<button>").addClass("btn btn-primary btn-sm ml-1 mb-4").text("대댓글 등록");
    
    insertChildReply.attr("onclick", "insertChildReply("+parentReplyNo+",this)")

    const insertCancel = $("<button>").addClass("btn btn-primary btn-sm ml-1 mb-4").text("취소");
    insertCancel.attr("onclick", "insertCancel(this)");

    // 버튼 영역에 등록, 취소 버튼을 추가
    $(replyBtnArea).append(insertChildReply, insertCancel);

    // textarea 화면 삽입
    $(el).parent().after(textarea);

    // 화면에 자리잡은 textarea 다음에 replyBtnArea 추가
    $(textarea).after(replyBtnArea);

    // 새로 생성된 textarea
    beforeChildReplyContent = textarea;
}

// 대댓글 작성 폼 취소
function insertCancel(el){
    $(el).parent().prev().remove();
    $(el).parent().remove();
}

// 대댓글 등록
function insertChildReply(parentReplyNo, el){
    const replyContent = $(el).parent().prev(); // 대댓글 textarea 

    if($(replyContent).val().trim().length == 0){ // 미작성 시
        alert("대댓글 작성 후 클릭해주세요.");
        $(replyContent).focus();
    }else{
        $.ajax({
            url:contextPath + "/reply/insert",
            data:{"memberNo": loginMemberNo, 
                "boardNo": boardNo, 
                "parentReplyNo": parentReplyNo,
                "replyContent": $(replyContent).val()},
            type: "POST",
            success: function(result){
                if (result > 0) {
                    swal({ "title": "대댓글 삽입 성공.", "icon": "success" });
                    selectReplyList();
                } else swal({ "title": "대댓글 삽입 실패", "icon": "error" });
            },
            error: function (req, status, error) {
                console.log("대댓글 삽입 실패");
                console.log(req.responseText);
            }
        });
    }

    

}