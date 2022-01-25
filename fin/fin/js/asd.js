// const container = document.querySelector(".image_wrap");
const container = $(".image_wrap");
// const box = container.querySelector(".item");
const box = $(".item");

function getViewportOffse1t($e) {
    var $window = $(window),
        scrollLeft = $window.scrollLeft(),
        scrollTop = $window.scrollTop(),
        locationWidth = $e.width(),
        locationHeight = $e.height(),
        offset = $e.offset(),
        rect1 = { x1: scrollLeft, y1: scrollTop, x2: scrollLeft + $window.width(), y2: scrollTop + $window.height() },
        rect2 = { x1: offset.left, y1: offset.top, x2: offset.left + $e.width(), y2: offset.top + $e.height() };
    return {
        left: offset.left - scrollLeft,
        top: offset.top - scrollTop,
        insideViewport: rect1.x1 < rect2.x2 && rect1.x2 > rect2.x1 && rect1.y1 < rect2.y2 && rect1.y2 > rect2.y1,
        locationWidth, locationHeight
    };
}


$(document).on("click", ".item", function(){
    // console.log(getViewportOffse1t($(this)).left, getViewportOffse1t($(this)).top);
    // console.log(getViewportOffse1t($(this)).locationWidth, getViewportOffse1t($(this)).locationHeight );
});


const { width: containerWidth,
    height: containerHeight }
    = container.getBoundingClientRect();


// const { width: boxWidth,
//     height: boxHeight }
//     = box.getViewportOffset();


let isDragging = null;
let originLeft = null;
let originTop = null;
let originX = null;
let originY = null;

for (let i = 0; i < box.length; i++) {
    box[i].addEventListener("mousedown", (e) => {
        isDragging = true;
        originX = e.clientX;
        originY = e.clientY;
        originLeft = box.offsetLeft;
        originTop = box.offsetTop;

    });
}

document.addEventListener("mousemove", (e) => {
    if (isDragging) {
        const diffX = e.clientX - originX;
        const diffY = e.clientY - originY;
        
        // console.log(getViewportOffse1t($(".image_wrap")).locationWidth, getViewportOffse1t($(".image_wrap")).locationHeight);
        const endOfXPoint = containerWidth - getViewportOffse1t($(".image_wrap")).locationWidth;
        const endOfYPoint = containerHeight - getViewportOffse1t($(".image_wrap")).locationHeight;

        console.log(endOfXPoint, endOfYPoint);


        //box.style.left = originLeft + diffX + "px";
        //box.style.top = originTop + diffY + "px";

        // box.style.left = Math.min(Math.max(0, originLeft + diffX), endOfXPoint) + "px";
        // box.style.top = Math.min(Math.max(0, originTop + diffY), endOfYPoint) + "px";

        box.eq(1).css("left", Math.min(Math.max(0, originLeft + diffX), endOfXPoint) + "px");
        box.eq(1).css("top", Math.min(Math.max(0, originTop + diffY), endOfYPoint) + "px");

        // console.log( box.eq(1).css("left", Math.min(Math.max(0, originLeft + diffX), endOfXPoint) + "px") );

        // box.style.left = Math.min(Math.max(0, originLeft + diffX), endOfXPoint) + "px";
        // box.style.top = Math.min(Math.max(0, originTop + diffY), endOfYPoint) + "px";


    }

});


document.addEventListener("mouseup", (e) => {
    isDragging = false;

});
