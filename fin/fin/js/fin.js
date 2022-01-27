$("#map").css("height", window.innerHeight);

const menuWarp = $("#menu-warp");
const menuView = $("#menu-view");
const menuAll = $("#menu-warp, #menu-view");

let backupWidth = window.innerWidth;
window.onresize = function(){
    var innerWidth = window.innerWidth;
    var innerHeight = window.innerHeight;

    // $("#map") height == 웹 height
    $("#map").css("height", innerHeight);

    if(innerWidth != backupWidth)   location.reload();

    backupWidth = innerWidth;

}






// 마커를 클릭하면 장소명을 표출할 인포윈도우 입니다
// var infowindow = new kakao.maps.InfoWindow({ zIndex: 1 });

var mapContainer = document.getElementById('map'), // 지도를 표시할 div 
    mapOption = {
        center: new kakao.maps.LatLng(37.566826, 126.9786567), // 지도의 중심좌표
        level: 3 // 지도의 확대 레벨
    };

// 지도를 생성합니다
var map = new kakao.maps.Map(mapContainer, mapOption);

// 일반 지도와 스카이뷰로 지도 타입을 전환할 수 있는 지도타입 컨트롤을 생성합니다
var mapTypeControl = new kakao.maps.MapTypeControl();
// 지도에 컨트롤을 추가해야 지도위에 표시됩니다
// kakao.maps.ControlPosition은 컨트롤이 표시될 위치를 정의하는데 TOPRIGHT는 오른쪽 위를 의미합니다
map.addControl(mapTypeControl, kakao.maps.ControlPosition.TOPRIGHT);

// 지도 확대 축소를 제어할 수 있는  줌 컨트롤을 생성합니다
var zoomControl = new kakao.maps.ZoomControl();
map.addControl(zoomControl, kakao.maps.ControlPosition.RIGHT);







// var imageSrc, imageSize, imageOption, markerImage, markerPosition, marker1;
// 현재 위치담을 변수 선언
var currentPos;
function locationLoadSuccess(pos) {
    // 현재 위치 받아오기
    currentPos = new kakao.maps.LatLng(pos.coords.latitude, pos.coords.longitude);

    // 지도 이동(기존 위치와 가깝다면 부드럽게 이동)
    map.panTo(currentPos);

    // 마커 생성
    var marker = new kakao.maps.Marker({
        position: currentPos
    });


    // imageSrc = 'images/AA.png', // 마커이미지의 주소입니다    
    // imageSize = new kakao.maps.Size(80, 83), // 마커이미지의 크기입니다
    // imageOption = {offset: new kakao.maps.Point(27, 69)}; // 마커이미지의 옵션입니다. 마커의 좌표와 일치시킬 이미지 안에서의 좌표를 설정합니다.
      
    // // 마커의 이미지정보를 가지고 있는 마커이미지를 생성합니다
    // markerImage = new kakao.maps.MarkerImage(imageSrc, imageSize, imageOption),
    //     markerPosition = new kakao.maps.LatLng(pos.coords.latitude, pos.coords.longitude); // 마커가 표시될 위치입니다

    // 마커를 생성합니다
    // marker1 = new kakao.maps.Marker({
    //     position: markerPosition, 
    //     image: markerImage // 마커이미지 설정 
    // });

    // 마커가 지도 위에 표시되도록 설정합니다
    // marker1.setMap(map);  
    marker.setMap(map);  
    map.setCenter(currentPos); 

    // 기존에 마커가 있다면 제거
    // marker.setMap(null);
    // 마커가 지도 위에 표시되도록 설정합니다
    // marker.setMap(map);
    
};

function locationLoadError(pos) {
    alert('위치 정보를 가져오는데 실패했습니다.');
};
function getCurrentPosBtn() {
    navigator.geolocation.getCurrentPosition(locationLoadSuccess, locationLoadError);
};
getCurrentPosBtn();



// 미리 병원 정보들 배열에 저장
// 주소-좌표 변환 객체를 생성합니다
var geocoder = new kakao.maps.services.Geocoder();
// 내 위치와 병원 위치의 거리
var distance = [];
// 병원위치 담을 곳
var hospi = [];
// 결과값으로 받은 위치를 마커로 표시합니다
var markerArr = [];
// 인포윈도우로 장소에 대한 설명을 표시합니다
var infowindow;
var linePath = [];
var polyline = [];
for(let i = 0; i < $(".map-info").length; i++){
    // 주소로 좌표를 검색합니다
    geocoder.addressSearch(hospLocationSplit[i]/* 병원 주소 */, function (result, status) {
        // status == OK or ZERO_RESULT

        // 정상적으로 검색이 완료됐으면 
        if (status == kakao.maps.services.Status.OK) {

            hospi[i] = new kakao.maps.LatLng(result[0].y, result[0].x);
            // var linePath = [ new daum.maps.LatLng(33.452344169439975, 126.56878163224233), new daum.maps.LatLng(33.452739313807456, 126.5709308145358)];
            // 내 위치 ~ 병원[i]의 직선거리 계산을 위한 배열로 저장
            linePath[i] = [currentPos, hospi[i]];


            // 결과값으로 받은 위치를 마커로 표시합니다
            markerArr[i] = new kakao.maps.Marker({
                map: map,
                position: hospi[i]
            });
            // // 지도의 중심을 결과값으로 받은 위치로 이동시킵니다
            // map.setCenter(currentPos);
            
        } else if (status == "ZERO_RESULT") {
            alert("검색결과가 없습니다.");
        }
        polyline[i] = new daum.maps.Polyline({
            path: linePath[i] // 선을 구성하는 좌표배열 입니다
        });
        distance[i] = Math.round(polyline[i].getLength());
    });
}

var getDetailAddr;
// info 여러개 닫으려고
var info = [];
// 바깥에서 쓰기 위해서
let backupIndex = 0;
// 상세뷰 만들기
$(document).on("click", ".map-info", function () {

    // 선택한 요소 인덱스
    const index = $(".map-info").index(this);
    backupIndex = index;

    // 선택한 요소 제목 $(".option-view")에 만들기
    $("#menu-view").css("visibility", "visible");
    $(".option-view").html("<h2>" + "병원 명" + "</h2>" + "세부 과? 같은거");
    $(".location-span").text("병원 위치");
    $(".location-tel").text("폰-번-호");
    $(".location-internet").text("http://인터넷주소.com");
    $(".talk-b-span1").text("ex)아동상담");
    $(".talk-b-span2").text("ex)발달검사, 진로/적성검사");

    // 병원명
    hospiName = $(".map-info").eq(index).children().last().children().first().text();

    // 인포윈도우로 장소에 대한 설명을 표시합니다
    infowindow = new kakao.maps.InfoWindow({
        content: '<div style="width:150px;text-align:center;padding:6px 0;">' + hospiName + '</div>'
    });

    for(key in info)    info[key].close();
    info[index] = infowindow;
    
    infowindow.open(map, markerArr[index]);

    // 지도 이동(기존 위치와 가깝다면 부드럽게 이동)
    map.panTo(hospi[index]);

    getDetailAddr = $(".map-info").eq(backupIndex).children().eq(1).children().eq(2).text().split(" | ")[1];

    // $(".admin-query").attr("href", "문의사항으로 이동");
});



if(window.innerWidth > 1200){
    // 웹이면
    menuWarp.css("top", "0").css("height", innerHeight - 20);

    // menuWarp, menuView 클릭 시 
    $(document).on("click", menuAll, function(){

        // menuView 스타일에 visibility == visible true였으면
        if(menuView.css("visibility") == "visible"){
            // #quit가 클릭되었는지 체크하고
            $(document).on("click", "#quit", function() {
                // true면 menuWarp 올림
                menuWarp.css("top", "0");
            });
        }

    })
}else if(window.innerWidth <= 1200){
    menuWarp.css("top", (innerHeight - 60) + "px").css("height", innerHeight);
    $(".upArrow > img").attr("src", "images/upload.png");

    // menuWarp, menuView 클릭 시 
    $(document).on("click", menuAll, function(){

        // 병원 클릭 시
        $(".map-info").on("click", function(){
            if(menuView.css("visibility") == "hidden"){
                // menuWarp 내림
                menuWarp.css("top", (innerHeight - 60) + "px").css("height", innerHeight);
            }
        });
        
        // menuView 스타일에 visibility == visible true였으면
        if(menuView.css("visibility") == "visible"){
            // #quit가 클릭되었는지 체크하고
            $(document).on("click", "#quit", function() {
                // true면 menuWarp 올림
                menuWarp.css("top", "0");
            });
        }

    });


    $(document).on("click", ".upArrow", function(){
        
        if($(".upArrow > img").attr("src") == "images/upload.png"){
            $(".upArrow > img").attr("src", "images/arrow-down-sign-to-navigate.png")
            menuWarp.css("top", "0");
            
        }else{
            $(".upArrow > img").attr("src", "images/upload.png");
            menuWarp.css("top", (innerHeight - 60) + "px");
            
        }
    });
}












var myDetailAddr;
function searchDetailAddrFromCoords(coords, callback) {
    // 좌표로 법정동 상세 주소 정보를 요청합니다
    geocoder.coord2Address(coords.getLng(), coords.getLat(), callback);
}
function getHospAddress() {
    searchDetailAddrFromCoords(currentPos, function(result, status) {
        if (status === kakao.maps.services.Status.OK) {
            myDetailAddr = !!result[0].road_address ? result[0].road_address.address_name : '';
            console.log(myDetailAddr);
        }   
    });
}
$(function(){
    getHospAddress();
});



function loadSearch(){
    return 'https://map.kakao.com/?sName=' + myDetailAddr +
    '&eName=' + getDetailAddr;
    // return 'https://map.kakao.com/link/to/' +
    // hospLocationSplit[backupIndex] + ',' + la + ',' + ma + '';
}
loadSearch();

// qasdtfdgfa
$(document).on("click", "#quit", function () {
    $("#menu-view").css("visibility", "hidden");
});

$(document).on("click", ".option", function(){
    // 지도 이동(기존 위치와 가깝다면 부드럽게 이동)
    map.panTo(currentPos);
});


// function isBrowserCheck(){ 
// 	const agt = navigator.userAgent.toLowerCase(); 
//     if(agt.split("edg").length > 1) return 'Edge';
// 	if (agt.indexOf("chrome") != -1) return 'Chrome';
// 	if (agt.indexOf("opera") != -1) return 'Opera';
// 	if (agt.indexOf("staroffice") != -1) return 'Star Office';
// 	if (agt.indexOf("webtv") != -1) return 'WebTV';
// 	if (agt.indexOf("beonex") != -1) return 'Beonex';
// 	if (agt.indexOf("chimera") != -1) return 'Chimera';
// 	if (agt.indexOf("netpositive") != -1) return 'NetPositive';
// 	if (agt.indexOf("phoenix") != -1) return 'Phoenix';
// 	if (agt.indexOf("firefox") != -1) return 'Firefox';
// 	if (agt.indexOf("safari") != -1) return 'Safari';
// 	if (agt.indexOf("skipstone") != -1) return 'SkipStone';
// 	if (agt.indexOf("netscape") != -1) return 'Netscape';
// 	if (agt.indexOf("mozilla/5.0") != -1) return 'Mozilla';
// 	if (agt.indexOf("msie") != -1) { 
//     	let rv = -1; 
// 		if (navigator.appName == 'Microsoft Internet Explorer') { 
// 			let ua = navigator.userAgent; var re = new RegExp("MSIE ([0-9]{1,}[\.0-9]{0,})"); 
// 		if (re.exec(ua) != null) 
// 			rv = parseFloat(RegExp.$1); 
// 		} 
// 		return 'Internet Explorer '+rv; 
// 	} 
// }
// const browser = isBrowserCheck();
// console.log(browser);

// function WinClose(){
//     top.window.open('about:blank','_self').close(); 
//     top.window.opener=self;
//     top.self.close();
// }

// if(browser == "Edge"){
//     window.location.replace("http://127.0.0.1:5500/fin/map.html");
//     WinClose();
// }