const color = "rgb(179, 162, 140)";

$(document).on("click", ".div-btn", function(){
    const index = $(this).index();
    $(this).css("background-color", color);
});

function colorSet(){
    const view = $(".enquiry-view");

    for(let i = 0; i < view.length; i++){
        if(i % 2 == 0)  view.eq(i).css("background-color", "rgb(236, 236, 236)");
        else            view.eq(i).css("background-color", "rgb(220, 220, 220)");
    }
}
colorSet();