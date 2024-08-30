let client;  // WebSocket 클라이언트 객체
let key;     // 각 세션을 구분하기 위한 고유 키
let flag = false;  // 챗봇 UI가 열려있는지 추적하는 플래그
let botContainer = document.getElementById("bot-container");

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

// 환영 메시지를 표시하고 저장하는 함수
function showWelcomeMessage() {
    const now = new Date();
    const today = formatDate(now);
	var time = formatTime(now);

    const welcomeMessage = `<div class="msg bot flex">
                                <div class="icon">
                                    <img src="/img/chatbot-img.png">
                                </div>
                                <div class="message">
                                	<div class="bot-name">두비</div>
                                    <div class="part chatbot">
                                        <p>
											안녕하세요. 영화를 '하다', DoMovie입니다.<br>
											반갑습니다. 000님.<br><br>
	
											무엇을 도와드릴까요?<br>
                                        </p>
                                    </div>
									<div class="time">${time}</div>
                                </div>
                            </div>`;
    
    // 환영 메시지를 이미 표시했는지 확인
    var hasShownWelcomeMessage = localStorage.getItem('hasShownWelcomeMessage');
    
    if (!hasShownWelcomeMessage) {
        showMessage(welcomeMessage);
        localStorage.setItem('hasShownWelcomeMessage', 'true');
    }

    localStorage.setItem('lastOpenedDate', today);
    showDateIfNew();
}

// 채팅 메시지를 UI에 표시하는 함수
function showMessage(tag) {
    var chatContent = document.getElementById("chat-content");
    chatContent.innerHTML += tag;
    chatContent.scrollTop = chatContent.scrollHeight;  // 스크롤을 최하단으로 이동
}

// WebSocket 연결을 설정하고 메시지 구독을 처리하는 함수
function connect() {
    client = Stomp.over(new SockJS('/chatbot')); // Stomp 라이브러리와 SockJS를 사용하여 WebSocket 연결 생성
    client.connect({}, (frame) => {
        console.log("Connected to WebSocket server with frame:", frame);
        
        key = new Date().getTime();  // 현재 시간을 기반으로 고유 키 생성
        client.subscribe(`/topic/bot/${key}`, (answer) => { // 특정 토픽을 구독하여 서버로부터 메시지를 받음
            var msgObj = answer.body; //서버로부터 받은 메세지 객체
            console.log("Received message from server:", msgObj);
            
            var now = new Date();
            var time = formatTime(now);
            
            // 봇의 응답 메시지 HTML 생성
            var tag = `<div class="msg bot flex">
                        <div class="icon">
                            <img src="/img/bot/bot-img.png">
                        </div>
                        <div class="message">
                        <div class="bot-name">두비</div>
                            <div class="part chatbot">
                                <p>${msgObj}</p>
                            </div>
                        </div>
                    </div>`;
            showMessage(tag);
            
            // 특정 키워드에 따른 추가 버튼 또는 이미지 표시
            if (msgObj.includes("배송조회")) {//includes ""가 포함되어있을경우
                // 배송조회 버튼 HTML
                var buttonHTML = `...`;  // (버튼 HTML 코드 생략)
                showMessage(buttonHTML);
            }
        });
    });
}

// 닫기버튼 : WebSocket 연결 종료
function disconnect() {
    if (client) {
        client.disconnect(() => {
            console.log("Disconnected...");
        });
    }
}

// 챗봇이 열려있는지 아닌지를 로컬 스토리지에 저장하는 함수
function saveBotState() {
    var isVisible = document.getElementById("bot-container").classList.contains('open');
    localStorage.setItem('botState', isVisible ? 'open' : 'closed');
}

// 저장된 챗봇 UI 상태를 불러오고 적용하는 함수
function loadBotState() {
    var botState = localStorage.getItem('botState'); // 로컬 스토리지에서 챗봇 상태 불러오기
    

    if (botState === 'open') { // 챗봇이 열려있는 상태라면
        botContainer.classList.add('open');
        flag = true;
        connect();
    } else {
        botContainer.classList.remove('open');
        flag = false;
        disconnect();
    }

    // 환영 메시지 표시 여부 확인 및 처리
    var hasShownWelcomeMessage = localStorage.getItem('hasShownWelcomeMessage');
    var wasChatReset = localStorage.getItem('chatReset');
    
    if (!hasShownWelcomeMessage || wasChatReset) {
        if (botState === 'open') {
            showWelcomeMessage();
            localStorage.removeItem('chatReset');
        }
    }
}

// 페이지를 떠날 때 챗봇 상태를 저장하는 이벤트 리스너
window.addEventListener('beforeunload', function() {
    saveBotState();
    localStorage.removeItem('chatContent');  // 대화 내용 초기화
});


// 챗봇 열기 버튼 클릭 이벤트 핸들러
function btnBotClicked() {
	
    if (flag) return; // 이미 열려있으면 무시

    botContainer.classList.add('open');
    botContainer.classList.add('animate__animated', 'animate__bounceIn');
    connect();
    flag = true;

    // 로컬 스토리지에서 웰컴 메시지 표시 여부와 채팅 리셋 여부를 확인
    var hasShownWelcomeMessage = localStorage.getItem('hasShownWelcomeMessage');
    var wasChatReset = localStorage.getItem('chatReset');

    // 웰컴 메시지를 아직 보여주지 않았거나 채팅이 리셋된 경우
    if (!hasShownWelcomeMessage || wasChatReset) {
        // 웰컴 메시지 표시 함수 호출 (별도로 정의된 함수)
        showWelcomeMessage();
        // 채팅 리셋 상태를 로컬 스토리지에서 제거
        localStorage.removeItem('chatReset');
    }
	
	botContainer.addEventListener('animationend', () => {
      botContainer.classList.remove('animate__animated', 'animate__bounceIn');
    });

    // 현재 챗봇 상태를 저장하는 함수 호출
    saveBotState();
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
                        <p>${question}</p>
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
        userId: 1 // 사용자 ID (예시로 '1'을 사용)
    };
    client.send(`/message/bot/question`, {}, JSON.stringify(data));
    clearQuestion();
}

// 입력 필드 초기화 함수
function clearQuestion() {
    document.getElementById("question").value = "";
}

// 챗봇 닫기 버튼 클릭 이벤트 핸들러
function btnCloseClicked() {
    //const botContainer = document.getElementById("bot-container");
    botContainer.classList.add('animate__animated', 'animate__bounceOut');
    
    // 애니메이션 종료 후 실행할 함수
    function onAnimationEnd() {
        botContainer.classList.remove('open', 'animate__animated', 'animate__bounceOut');
        saveBotState();
        disconnect();
        flag = false;
        document.getElementById("chat-content").innerHTML = ""; // 채팅 내용 초기화
        localStorage.removeItem('chatContent');
        localStorage.setItem('chatReset', 'true');
        localStorage.removeItem('hasShownWelcomeMessage');
        
        // 이벤트 리스너 제거
        botContainer.removeEventListener('animationend', onAnimationEnd);
    }
    
    // 애니메이션 종료 이벤트 리스너 추가
    botContainer.addEventListener('animationend', onAnimationEnd);
}

// 페이지 로드 시 초기화 및 이벤트 리스너 설정
document.addEventListener('DOMContentLoaded', (event) => {
    //btnCloseClicked();  // 초기 상태에서 챗봇 UI 닫기
	botContainer = document.getElementById("bot-container");
    botContainer.classList.remove('open', 'animate__animated', 'animate__bounceOut');
        saveBotState();
        disconnect();
        flag = false;
        document.getElementById("chat-content").innerHTML = ""; // 채팅 내용 초기화
        localStorage.removeItem('chatContent');
        localStorage.setItem('chatReset', 'true');
        localStorage.removeItem('hasShownWelcomeMessage');
        
    loadBotState();  // 저장된 챗봇 상태 로드

    // 각 버튼에 이벤트 리스너 추가
    document.getElementById("chat-icon").addEventListener('click', btnBotClicked);
    document.getElementById("close-button").addEventListener('click', btnCloseClicked);
    document.getElementById("send-button").addEventListener('click', btnMsgSendClicked);

    // 입력 필드에서 Enter 키 입력 시 메시지 전송
    document.getElementById("question").addEventListener('keydown', function(event) {
        if (event.key === 'Enter') {
            event.preventDefault();
            btnMsgSendClicked();
        }
    });
});