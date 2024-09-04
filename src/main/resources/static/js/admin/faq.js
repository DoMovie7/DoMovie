let client;  // WebSocket 클라이언트 객체
let key;
//let botContainer = document.getElementById("bot-container");

//채팅 연결
function chatConnect(){
	
	client = Stomp.over(new SockJS('/chatbot')); // Stomp 라이브러리와 SockJS를 사용하여 WebSocket 연결 생성
	    
	    client.connect({}, (frame) => {
	        key = generateUniqueKey();  //고유 키 생성
	        console.log(key);
	        client.subscribe(`/topic/query/${key}`, (answer) => { // 특정 토픽을 구독하여 서버로부터 메시지를 받음
	            var response = answer.body; //서버로부터 받은 메세지 객체
	            var now = new Date();
	            var time = formatTime(now);
	            
	            // 봇의 응답 메시지 HTML 생성
	            var tag = `<div class="msg bot flex">
	                        <div class="icon">
	                            <img src="/img/chatbot-img.png">
	                        </div>
	                        <div class="message">
	                        <div class="bot-name">두비</div>
	                            <div class="part chatbot">
	                                <p>${response}</p>
	                            </div>
	                            <div class="time">${time}</div>
	                        </div>
	                    </div>`;
	            showMessage(tag);
	            
	        });
	    });
	
}

// 메시지 전송 버튼 클릭 이벤트 핸들러
function btnMsgSendClicked() {
	
    if (!client) { // WebSocket 클라이언트가 초기화되지 않은 경우
        console.error("WebSocket client is not initialized.");
        return;
    }

    var question = document.getElementById("question").value.trim(); //입력 필드에서 질문을 가져와 공백 제거
    if (question.length < 2) {
        alert("질문은 최소 2글자 이상으로 부탁드립니다.");
        return;
    }

    var now = new Date();
    var time = formatTime(now);
    
    // 사용자 메시지 HTML 생성 및 표시
    var tag = `<div class="msg user flex">
                <div class="message">
                    <div class="part guest">
                        <p class="user-chat">${question}</p>
                    </div>
                    <div class="time">${time}</div>
                </div>
            </div>`;

    showDateIfNew();
    showMessage(tag);

    // 서버로 전송할 데이터 객체
    var data = {
        key: key,
        content: question, // 사용자 질문 내용
        userId: userId // 사용자 ID
    };
    
		client.send(`/message/chat/query`, {}, JSON.stringify(data));
    
    clearQuestion();
}


////////////////////////////////////////////////////////////////////////////
// 페이지 로드 시 초기화 및 이벤트 리스너 설정
document.addEventListener('DOMContentLoaded', (event) => {
	
	chatConnect();
    
    // 입력 필드에서 Enter 키 입력 시 메시지 전송
    document.getElementById("question").addEventListener('keydown', function(event) {
        if (event.key === 'Enter') {
            event.preventDefault();
            btnMsgSendClicked();
        }
    });
    
});

/////////기본 함수
// WebSocket 지원 여부를 확인하는 함수
function isWebSocketSupported() {
    return 'WebSocket' in window;
}

// WebSocket 지원 여부를 콘솔에 출력
if (isWebSocketSupported()) {
    console.log("이 브라우저는 WebSocket을 지원합니다.");
} else {
    console.log("이 브라우저는 WebSocket을 지원하지 않습니다.");
}

// 입력 필드 초기화 함수
function clearQuestion() {
    document.getElementById("question").value = "";
}

// 채팅 메시지를 UI에 표시하는 함수
function showMessage(tag) {
    var chatContent = document.getElementById("chat-content");
    chatContent.innerHTML += tag;
    chatContent.scrollTop = chatContent.scrollHeight;  // 스크롤을 최하단으로 이동
}


//사용자가 보낸 채팅으로 뜨게 만드는 함수
function userChat(text){
	var now = new Date();
    var time = formatTime(now);
    
    // 사용자 메시지 HTML 생성 및 표시
    var tag = `<div class="msg user flex">
                <div class="message">
                    <div class="part guest">
                        <p class="user-chat">${text}</p>
                    </div>
                    <div class="time">${time}</div>
                </div>
            </div>`;
            
	showMessage(tag);
}

//챗봇 응답
function botChat(text){
	var now = new Date();
    var time = formatTime(now);
    
    // 챗봇 응답 메시지 HTML 생성 및 표시
    var tag = `<div class="msg bot flex">
                        <div class="icon">
                            <img src="/img/chatbot-img.png">
                        </div>
                        <div class="message">
                        <div class="bot-name">두비</div>
                            <div class="part chatbot">
                                ${text}
                            </div>
                            <div class="time">${time}</div>
                        </div>
                    </div>`;
            
	showMessage(tag);
}


/////////날짜, 시간 포맷팅
// 현재 시간을 "오전/오후 HH:MM" 형식으로 포맷팅하는 함수
function formatTime(now) {
    var ampm = (now.getHours() > 11) ? "오후" : "오전";
    var hour = now.getHours() % 12;
    if (hour == 0) hour = 12;
    var minute = now.getMinutes();
    var formattedMinute = String(minute).padStart(2, '0');
    return `${ampm} ${hour}:${formattedMinute}`;
}

// 현재 날짜를 "YYYY년 MM월 DD일 요일" 형식으로 포맷팅하는 함수
function formatDate(now) {
    const year = now.getFullYear();
    const month = now.getMonth() + 1;
    const date = now.getDate();
    const dayOfWeek = now.getDay();
    const days = ["일요일", "월요일", "화요일", "수요일", "목요일", "금요일", "토요일"];
    return `${year}년 ${month}월 ${date}일 ${days[dayOfWeek]}`;
}

// 새로운 날짜인 경우 날짜를 표시하는 함수
function showDateIfNew() {
    var now = new Date();
    var today = formatDate(now);
    
    // 로컬 스토리지에서 마지막으로 표시된 날짜를 가져옴
    var savedDate = localStorage.getItem('lastDisplayedDate');
    
    if (savedDate !== today) {
        var dateTag = `<div class="flex center date">${today}</div>`;
        var chatContent = document.getElementById("chat-content");
        chatContent.innerHTML = dateTag + chatContent.innerHTML;
        localStorage.setItem('lastDisplayedDate', today);  // 현재 날짜를 로컬 스토리지에 저장
    }
}