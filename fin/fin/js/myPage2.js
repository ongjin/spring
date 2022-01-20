const color = "rgb(179, 162, 140)";
$(document).on("mouseover", ".div-btn", function(){
    const index = $(this).index();

    $(".div-btn").css("background-color", "rgb(155, 150, 143)");
    $(this).css("background-color", color);
    $(this).css("cursor", "pointer");

    
});
$(document).on("click", ".div-btn", function(){
    const index = $(this).index();
    $(this).css("background-color", color);
});

function colorSet(){
    const view = $(".board-view");

    for(let i = 0; i < view.length; i++){
        if(i % 2 == 0)  view.eq(i).css("background-color", "rgb(236, 236, 236)");
        else            view.eq(i).css("background-color", "rgb(220, 220, 220)");
    }
}
colorSet();

