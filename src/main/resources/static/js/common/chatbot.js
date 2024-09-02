let client;  // WebSocket 클라이언트 객체
let key;     // 각 세션을 구분하기 위한 고유 키
let flag = false;  // 챗봇 UI가 열려있는지 추적하는 플래그
let botContainer = document.getElementById("bot-container");
let userId = 0;
let userName = "guest";
let isConnectedToAgent = false;

// 챗봇 초기 메세지
async function showWelcomeMessage(forceShow = false) {
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
											안녕하세요. 영화를 '하다', DoMovie입니다.<br><br>
	
											무엇을 도와드릴까요?<br>
                                        </p>
                                        <div class="button-containers">
		                                    <button class="bot-category-btn" id="FAQs">자주 묻는 질문</button>
		                                    <button class="bot-category-btn" id="inquery">1:1 문의</button>
		                                    <button class="bot-category-btn final-category-btn" id="movieRecommend">영화 추천</button>
		                                </div>
                                    </div>
									<div class="time">${time}</div>
                                </div>
                            </div>`;
    
    // 환영 메시지를 이미 표시했는지 확인
    var hasShownWelcomeMessage = localStorage.getItem('hasShownWelcomeMessage');
    
    if (!hasShownWelcomeMessage || forceShow) {
        showMessage(welcomeMessage);
        localStorage.setItem('hasShownWelcomeMessage', 'true');
    }

    localStorage.setItem('lastOpenedDate', today);
    showDateIfNew();
    setupButtonListeners();
}

function setupButtonListeners() {
    // '자주 묻는 질문' 버튼 클릭 시
    document.getElementById('FAQs').addEventListener('click', function() {
        userChat("자주 묻는 질문");
        fAQsInfo();
    });
    
    // '1:1 문의' 버튼 클릭 시
    document.getElementById('inquery').addEventListener('click', async function() {
        userChat("1:1 문의");
        const userId = await checkAuthStatus();
        
        if(userId != 0){
            botChat(`<p>${userName} 고객님, 안녕하세요!<br> 상담사를 연결해드릴까요?</p>
                    <div class="button-containers">
                        <button class="bot-category-btn" id="login-query">상담사 새로 연결</button>
                        <button class="bot-category-btn" id="past-query-list">이전 문의 보기</button>
                        <button class="bot-category-btn final-category-btn" id="go-back">돌아가기</button>
                    </div>`);
        } else {
            botChat(`<p>현재 로그인되지 않았습니다. <br><br>비회원으로 문의 시 채팅 화면을 끄면 문의 내용이 모두 삭제됩니다. 괜찮으십니까?</p>
                    <div class="button-containers">
                        <button class="bot-category-btn" id="not-login-query">비회원 문의</button>
                        <button class="bot-category-btn final-category-btn" onclick="location.href='/signin'">로그인 하러 가기</button>
                    </div>`);
        }
        
        // '돌아가기' 버튼에 대한 이벤트 리스너
        document.getElementById('go-back').addEventListener('click', function() {
            showWelcomeMessage(true);  // 강제로 환영 메시지 표시
        });
        
        document.getElementById('login-query').addEventListener('click', function() {
            isConnectedToAgent = true;
            botChat("<p>상담사 연결중입니다.<br> 문의 내용을 입력해주세요.</p>");
        });
        
        document.getElementById('past-query-list').addEventListener('click', function() {
            isConnectedToAgent = true;
            botChat("<p>이전 채팅문의 내역입니다.</p>");
        });
    });
    
    // '영화 추천' 버튼 클릭 시
    document.getElementById('movieRecommend').addEventListener('click', function() {
        userChat("영화 추천");
        botChat("<p>어떤 영화를 추천해드릴까요?<br><br> 장르나 출연 배우 등 선호하는 영화와 관련된 정보를 뭐든 입력해주세요!</p>");
    });
}



// WebSocket 연결을 설정하고 메시지 구독을 처리하는 함수
function connect() {
    client = Stomp.over(new SockJS('/chatbot')); // Stomp 라이브러리와 SockJS를 사용하여 WebSocket 연결 생성
    
    client.connect({}, (frame) => {
        key = generateUniqueKey();  //고유 키 생성
        console.log(key);
        client.subscribe(`/topic/bot/${key}`, (answer) => { // 특정 토픽을 구독하여 서버로부터 메시지를 받음
          var response = answer.body; //서버로부터 받은 메세지 객체
        //client.subscribe(`/exchange/chatbot-exchange/chatbot.key`, (message) => {
			//var response = message.body;
            //var response = JSON.parse(message.body);
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
            
            /*
            // 특정 키워드에 따른 추가 버튼 또는 이미지 표시
            if (responseMessage.includes("배송조회")) {//includes ""가 포함되어있을경우
                // 배송조회 버튼 HTML
                var buttonHTML = `...`;  // (버튼 HTML 코드 생략)
                showMessage(buttonHTML);
            }
            */
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
    
    if (isConnectedToAgent) {
        // 상담사와 연결된 경우 다른 경로로 메시지 전송
        client.send(`/message/agent`, {}, JSON.stringify(data));
    } else {
        // 일반 챗봇 대화
        client.send(`/message/question`, {}, JSON.stringify(data));
    }
    
    clearQuestion();
}