
let backupIndex;
var line = $(".db-line");

// 게시글색 왔다리 갔다리
function colorSet(){
    const view = $(".board-view");

    for(let i = 0; i < view.length; i++){
        if(i % 2 == 0)  view.eq(i).css("background-color", "rgb(236, 236, 236)");
        else            view.eq(i).css("background-color", "rgb(220, 220, 220)");
    }
}
colorSet();

// line : <div class="db-line"></div>
// ele : 형제 요소(버튼들?) jquery로만 가능
function underLineDraw(line, ele){
    let underLine = line;
    let offset = ele.offset();
    let offsetWidth = ele.width();
    let offsetHeight = ele.height();

    underLine.css("left", offset.left + "px");
    underLine.css("width", offsetWidth + 10 + "px");
    underLine.css("top", offset.top + offsetHeight + 5 + "px");
}

$(document).on("click", ".div-btn", function(){

    const index = $(".div-btn").index($(this));
    backupIndex = index;

    underLineDraw(line, $(this));
});
$(".div-btn").eq(0).click();


// console.log($(".side_menu li"));

var backupWidth;
window.onresize = function(){
    var innerWidth = window.innerWidth;
    var innerHeight = window.innerHeight;

    if(innerWidth != backupWidth){
        underLineDraw(line, $(".div-btn").eq(backupIndex));
    }
    // if(innerWidth > 950){

    // }else if(innerWidth > 768 && innerWidth <= 950){

    // }else{

    // }
}

let backupMenuIndex = -1;
$(document).on("click", ".opImg > img", function(){

    const divAll = $(".opImg > div");
    const index = $(".opImg > img").index($(this));
    const div = $(".opImg").eq(index).children();

    divAll.css("visibility", "hidden");

    if(backupMenuIndex == index){
        
        divAll.css("visibility", "hidden");
        backupMenuIndex = -1;

    }else{

        div.css("visibility", "visible");
        backupMenuIndex = index;
    }
});

$(window).scroll(function(e){
    // console.log($(this).scrollTop());

    underLineDraw(line, $(".div-btn").eq(backupIndex));
});