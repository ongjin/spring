(function () {
	// 오늘 날짜 출력 
	var today = new Date();
	var month = (today.getMonth() + 1);
	var date = today.getDate();

	var str = today.getFullYear() + "-"
		+ (month < 10 ? "0" + month : month) + "-"
		+ (date < 10 ? "0" + date : date);
	$("#today").html(str);
})();


// 유효성 검사 
function boardValidate() {
	if ($("#boardTitle").val().trim().length == 0) {
		alert("제목을 입력해 주세요.");
		$("#title").focus();
		return false;
	}

	if ($("#boardContent").val().trim().length == 0) {
		alert("내용을 입력해 주세요.");
		$("#content").focus();
		return false;
	}

	// 유효성 검사 후 문제가 없다면 input[name=deleteImages] 요소의 value 값으로 deleteImages 배열을 추가
	$("input[name=deleteImages]").val(deleteImages);
}

// 이미지 영역을 클릭할 때 파일 첨부 창이 뜨도록 설정하는 함수
$(function () {
	$(".boardImg").on("click", function () {
		var index = $(".boardImg").index(this);
		// 현재 클릭된 요소가 .boardImg 중 몇 번째 인덱스인지 반환



		$("[type=file]").eq(index).click();
		// 타입이 file인 요소 중 몇번째 인덱스 요소를 선택하여 클릭해라
	});

});


// 파일을 선택한 경우 선택된 input 요소를 백업해서 저장하는 용도의 객체
const fileClone = {};

// x 버튼이 눌러져 삭제된 이미지 레벨을 저장할 배열
// -> 배열을 input 태그 value로 추가하면 '요소1, 요소2, ...' 형태의 문자열로 변환 됨
const deleteImages = [];

// 각각의 영역에 파일을 첨부 했을 경우 미리 보기가 가능하도록 하는 함수
function loadImg(input, num) {
	// 매개변수 input == 클릭된 input 요소

	// 파일이 선택된 경우 true
	if (input.files && input.files[0]) {

		fileClone[num] = $(input).clone(); // 백업 객체에 복제본 추가

		// deleteImages 배열에 num 값과 같은 번호가 존재하는지 확인
		if (deleteImages.indexOf(num) != -1) { // 존재 하는 경우

			// 배열.splice(시작 인덱스, 제거할 수 ) : 배열 내 시작 인덱스 부터 지정된 개수 만큼 요소 삭제 
			deleteImages.splice(deleteImages.indexOf(num), 1);
		}

		var reader = new FileReader();
		reader.readAsDataURL(input.files[0]);
		reader.onload = function (e) {
			$(".boardImg").eq(num).children("img").attr("src", e.target.result);
		}

	} else {
		console.log("취소 클릭함");
		// 취소가 실행된 input 태그 앞에 백업해둔 복제본을 추가
		$(input).before(fileClone[num].clone());
		$(input).remove(); // 원본 삭제

		// innerHTML, html()
		// -> 작성된 문자열을 HTML Parser를 이용해서 해석 후 화면에 반영
		//    --> 문자열 내부에 요소가 있는지 처음에 모른다
		//       --> 읽을 때 마다 같은 문장이라도 새로운 요소로 인식한다.

		// createElement("태그명"), $("<태그명>")
		// == DOM 요소
		// -> 화면 상에는 동일한 DOM 요소가 존재할 수 없다.
		//
		// 공통점 : 화면에 요소를 추가할 수 있다.

	}
}



// 수정버튼 클릭 시 동작
function updateForm() {
	document.requestForm.action = "../updateForm";
	document.requestForm.method = "POST";
	document.requestForm.submit();
}
function deleteBoard() {

	if(confirm("정말 삭제 하시겠습니까?")){
		document.requestForm.action = "../delete";
		document.requestForm.method = "POST";
		document.requestForm.submit();

	}
}


// 이미지에 있는 X 버턴을 눌렀을 때의 동작
$(".deleteImg").on("click", function (e) {
	e.stopPropagation(); // 이벤트 버블링(감싸고 있는 요소에 이벤트가 전파되는 것)

	$(this).prev().removeAttr("src");

	// 클린된 x 버튼의 인덱스와 같은 인덱스에 위치한 input type="file" 요소의 value를 초기화
	const index = $(this).index(".deleteImg");

	$("input[name=images]").eq(index).val("");

	// deleteImages 배열에 같은 번호가 없으면
	if (deleteImages.indexOf(index) == -1) {
		// deleteImages 배열에 삭제된 이미지의 레벨을 추가
		deleteImages.push(index);
	}


});