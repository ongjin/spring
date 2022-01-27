const container = document.querySelector(".image_wrap");
const box = container.querySelectorAll(".item");

const{ width : containerWidth, 
       height : containerHeight}
= container.getBoundingClientRect();

var oriWidth = [];
var oriHeight = [];
for(let i = 0; i < box.length; i++){
    oriWidth[i] = box[i].getBoundingClientRect().width;
    oriHeight[i] = box[i].getBoundingClientRect().height;
    // original.oriWidth
}

let isDragging = null;
let originLeft = null;
let originTop = null;
let originX = null;
let originY = null;

var backupIndex;
for(let i = 0; i < box.length; i++){
    box[i].addEventListener("mousedown", function(e) {

        backupIndex = i;

        isDragging = true;
        originX = e.clientX;
        originY = e.clientY; 
        originLeft = box[i].offsetLeft;
        originTop = box[i].offsetTop;
    });
}

document.addEventListener("mousemove", function(e) {
    if(isDragging){
        const diffX = e.clientX - originX;
        const diffY = e.clientY - originY;

        const endOfXPoint = containerWidth - oriWidth[backupIndex];
        const endOfYPoint = containerHeight - oriHeight[backupIndex];

        box[backupIndex].style.left = Math.min(Math.max(0, originLeft + diffX), endOfXPoint) + "px";
        box[backupIndex].style.top = Math.min(Math.max(0, originTop + diffY), endOfYPoint) + "px";
    }
    
});


document.addEventListener("mouseup", function(e) {
    isDragging = false;
});
