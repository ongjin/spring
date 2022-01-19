// 마커를 클릭하면 장소명을 표출할 인포윈도우 입니다
var infowindow = new kakao.maps.InfoWindow({ zIndex: 1 });

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


// 마커가 표시될 위치입니다 
var markerPosition = new kakao.maps.LatLng(33.450701, 126.570667);

// 마커를 생성합니다
var marker = new kakao.maps.Marker({
    position: markerPosition
});

// 마커가 지도 위에 표시되도록 설정합니다
marker.setMap(map);
// 현재 위치
var currentPos;
// 아래 코드는 지도 위의 마커를 제거하는 코드입니다
marker.setMap(null);
function locationLoadSuccess(pos) {
    // 현재 위치 받아오기
    currentPos = new kakao.maps.LatLng(pos.coords.latitude, pos.coords.longitude);

    // 지도 이동(기존 위치와 가깝다면 부드럽게 이동)
    // map.panTo(currentPos);

    // 마커 생성
    var marker = new kakao.maps.Marker({
        position: currentPos
    });

    // 기존에 마커가 있다면 제거
    marker.setMap(null);
    marker.setMap(map);
};
function locationLoadError(pos) {
    alert('위치 정보를 가져오는데 실패했습니다.');
};
function getCurrentPosBtn() {
    navigator.geolocation.getCurrentPosition(locationLoadSuccess, locationLoadError);
};

$(function () {
    getCurrentPosBtn();
});


// 미리 병원 정보들 배열에 저장
// 주소-좌표 변환 객체를 생성합니다
var geocoder = new kakao.maps.services.Geocoder();
// 내 위치와 병원 위치의 거리
var distance = [];
// 병원위치 담을 곳
var hospi = [];
// 결과값으로 받은 위치를 마커로 표시합니다
var marker = [];
// 인포윈도우로 장소에 대한 설명을 표시합니다
var infowindow;
var linePath = [];
var polyline = [];
for(let i = 0; i < $(".map-info").length; i++){
    // 주소로 좌표를 검색합니다
    geocoder.addressSearch(hospLocationSplit[i]/* 병원 주소 */, function (result, status) {
        // status == OK or ZERO_RESULT

        // 정상적으로 검색이 완료됐으면 
        if (status === kakao.maps.services.Status.OK) {

            hospi[i] = new kakao.maps.LatLng(result[0].y, result[0].x);
            // var linePath = [ new daum.maps.LatLng(33.452344169439975, 126.56878163224233), new daum.maps.LatLng(33.452739313807456, 126.5709308145358)];
            // 내 위치 ~ 병원[i]의 직선거리 계산을 위한 배열로 저장
            linePath[i] = [currentPos, hospi[i]];

            // 결과값으로 받은 위치를 마커로 표시합니다
            marker[i] = new kakao.maps.Marker({
                map: map,
                position: hospi[i]
            });
            // 지도의 중심을 결과값으로 받은 위치로 이동시킵니다
            map.setCenter(currentPos);
            
        } else if (status == "ZERO_RESULT") {
            alert("검색결과가 없습니다.");
        }
        polyline[i] = new daum.maps.Polyline({
            path: linePath[i], // 선을 구성하는 좌표배열 입니다
        });
        distance[i] = Math.round(polyline[i].getLength());
        console.log(distance[i]);
    });
}


var info = [];
// 상세뷰 만들기
$(document).on("click", ".map-info", function () {

    // 선택한 요소 인덱스
    const index = $(".map-info").index(this);

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
    
    infowindow.open(map, marker[index]);
    info[index] = infowindow;

    // 지도 이동(기존 위치와 가깝다면 부드럽게 이동)
    map.panTo(hospi[index]);

    // $(".admin-query").attr("href", "문의사항으로 이동");
});

// ?
$(document).on("click", "#quit", function () {
    $("#menu-view").css("visibility", "hidden");
});

$(document).on("click", ".option", function(){
        // 지도 이동(기존 위치와 가깝다면 부드럽게 이동)
    map.panTo(currentPos);
});