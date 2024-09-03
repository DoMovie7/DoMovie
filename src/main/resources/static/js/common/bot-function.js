// 페이지 로드 시 초기화 및 이벤트 리스너 설정
document.addEventListener('DOMContentLoaded', (event) => {
	
    // 초기 상태에서 챗봇 UI 닫기
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

// 고유한 키를 생성하는 함수
function generateUniqueKey() {
    // 현재 시간과 랜덤 값을 조합하여 36진법으로 변환
    return new Date().getTime().toString(36) + Math.random().toString(36).substring(2);
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



/////////챗봇 열기 및 닫기
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

// 챗봇 닫기 버튼 클릭 이벤트 핸들러
function btnCloseClicked() {
    //const botContainer = document.getElementById("bot-container");
    botContainer.classList.add('animate__animated', 'animate__bounceOut');
    websocketStatus = 0;
    
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

// 닫기버튼 : WebSocket 연결 종료
function disconnect() {
    if (client) {
        client.disconnect(() => {
            console.log("Disconnected...");
        });
    }
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

// 챗봇이 열려있는지 아닌지를 로컬 스토리지에 저장하는 함수
function saveBotState() {
    var isVisible = document.getElementById("bot-container").classList.contains('open');
    localStorage.setItem('botState', isVisible ? 'open' : 'closed');
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



/////////로그인된 사용자인지 체크
async function checkAuthStatus() {
  try {
    const response = await fetch('/api/user/status');
    const data = await response.json();
    
    if (data.status === 'authenticated') {
      console.log('User is logged in:', data.userName);
      userId = data.userId;
      userName = data.userName;
      
      return data.userId;
      
    } else {
      console.log('User is not logged in');
      return 0;
    }
    
  } catch (error) {
    console.error('Error:', error);
    return 0;
  }
}