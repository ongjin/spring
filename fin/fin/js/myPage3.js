const color = "rgb(179, 162, 140)";

$(document).on("click", ".div-btn", function(){
    const index = $(this).index();
    $(this).css("background-color", color);
});

function colorSet(){
    const view = $(".message-view");

    for(let i = 0; i < view.length; i++){
        if(i % 2 == 0)  view.eq(i).css("background-color", "rgb(236, 236, 236)");
        else            view.eq(i).css("background-color", "rgb(220, 220, 220)");
    }
}
colorSet();

$(document).on("click", ".message-view", function(){

    const per = $(this).children().eq(0).text();
    const msg = $(this).children().eq(1).text();
    const date = $(this).children().eq(2).text();

    $("#modal").css("display", "flex");
    $(".postscript-cut").text("TO. " + per);

});
