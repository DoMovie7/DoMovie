let client;  // WebSocket 클라이언트 객체
let key;     // 각 세션을 구분하기 위한 고유 키
let flag = false;  // 챗봇 UI가 열려있는지 추적하는 플래그
let botContainer = document.getElementById("bot-container");
let userId = 0;
let userName = "guest";
let websocketStatus = 0; //0:기본 채팅, 1:문의 상담, 2:AI모드
let categories = [];

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
                                            <button class="bot-category-btn" data-action="FAQs">자주 묻는 질문</button>
                                            <button class="bot-category-btn" data-action="inquery">1:1 문의</button>
                                            <button class="bot-category-btn final-category-btn" data-action="movieRecommend">영화 추천</button>
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
}

//버튼 클릭 처리
document.addEventListener('click', async function(e) {
    if (e.target.classList.contains('bot-category-btn')) {
        const action = e.target.getAttribute('data-action');
        switch(action) {
            case 'FAQs':
                userChat("자주 묻는 질문");
                fetchCategories()
                break;
            case 'inquery':
                userChat("1:1 문의");
                await handleInquery();
                break;
            case 'movieRecommend':
                userChat("영화 추천");
                websocketStatus = 2;
                botChat("<p>어떤 영화를 추천해드릴까요?<br><br> 장르나 출연 배우 등 선호하는 영화와 관련된 정보를 뭐든 입력해주세요!</p>");
                break;
            case 'login-query':
                websocketStatus = 1;
				chatConnect();
                botChat("<p>상담사 연결중입니다.<br> 문의 내용을 입력해주세요.</p>");
                break;
            case 'past-query-list':
				websocketStatus = 1;
                botChat(`<p>이전 채팅문의 내역입니다.</p>`);
                break;
            case 'go-back':
                showWelcomeMessage(true);
                break;
        }
    }
});


/*
async function createRoom(){
	try {
	    const response = await fetch(url, {
	      method: 'POST',
	      headers: {
	        'Content-Type': 'application/json',
			[header] : token
	      },
	      body: JSON.stringify({ userId: userId })
	    });

	    if (!response.ok) {
	      throw new Error(`HTTP error! status: ${response.status}`);
	    }

	    const data = await response.json();
		console.log(data);
	    return data;
	  } catch (error) {
	    console.error('Error posting userId:', error);
	    throw error;
	  }
}
*/

async function handleInquery() {
    const userId = await checkAuthStatus();
    
    if(userId != 0){
        botChat(`<p>${userName} 고객님, 안녕하세요!<br> 상담사를 연결해드릴까요?</p>
                <div class="button-containers">
                    <button class="bot-category-btn" data-action="login-query">상담사 새로 연결</button>
                    <button class="bot-category-btn" data-action="past-query-list">이전 문의 보기</button>
                    <button class="bot-category-btn final-category-btn" data-action="go-back">돌아가기</button>
                </div>`);
    } else {
        botChat(`<p>현재 로그인되지 않았습니다. <br><br>1:1 문의를 원하시면 로그인해주세요.</p>
                <div class="button-containers">
                    <button class="bot-category-btn final-category-btn" onclick="location.href='/signin'">로그인 하러 가기</button>
                </div>`);
    }
}

// 카테고리 데이터를 서버로부터 가져오는 함수
function fetchCategories() {
    fetch('/api/categories')
        .then(response => response.json())
        .then(data => {
            categories = data;
            // 최상위 카테고리를 화면에 렌더링하는 함수 호출
            renderTopLevelCategories();
        })
        .catch(error => console.error('Error fetching categories:', error));
}

// 최상위 카테고리만 화면에 렌더링하는 함수
function renderTopLevelCategories() {
	let buttonsHtml = categories.map((category, index) => 
	  `<button class="${index === categories.length - 1 ? 'bot-category-btn final-category-btn' : 'bot-category-btn'}" onclick="showSubcategories(${category.id})">${category.name}</button>`
	).join('');

    botChat(`
        <p>자주 묻는 질문 리스트입니다. <br><br>원하시는 카테고리를 선택해주세요!</p>
        <div class="button-containers">
            ${buttonsHtml}
        </div>
    `);
}

// 선택된 카테고리의 서브카테고리를 표시하는 함수
function showSubcategories(categoryId) {
    const category = categories.find(cat => cat.id === categoryId);
    if (category && category.children && category.children.length > 0) {
		
		let subCategoryButtons = category.children.map((subCategory, index) => 
		  `<button class="${index === category.children.length - 1 ? 'bot-category-btn final-category-btn' : 'bot-category-btn'}" onclick="showContent(${subCategory.id})">${subCategory.name}</button>`
		).join('');

        botChat(`
            <p>${category.name}의 세부 카테고리입니다</p>
            <div class="button-containers">
                ${subCategoryButtons}
            </div>
        `);
    } else {
        botChat(`<p>${category.name}에 대한 세부 카테고리가 없습니다.</p>`);
    }
}

// 선택된 서브카테고리의 내용을 표시하는 함수
function showContent(subCategoryId) {
    let content = '';
    for (let category of categories) {
        for (let subCategory of category.children) {
            if (subCategory.id === subCategoryId) {
                content = subCategory.content;
                break;
            }
        }
        if (content) break;
    }

    if (content) {
        botChat(`<p>${content}</p>`);
        if (content.includes('아이디 찾기')) {
			chatLink('아이디 찾기', '/findId');
		} else if (content.includes('비밀번호 찾기')) {
			chatLink('비밀번호 찾기', '/findPassword');
		} else if (content.includes('비밀번호 변경')) {
			chatLink('비밀번호 변경', '/mypage');	
        } else if (content.includes('돌아가기')) {
            botChat(`<div class="button-containers">
						<button class="bot-category-btn first-category-btn final-category-btn" data-action="go-back">돌아가기</button>
					 </div>`);
        }
    } else {
        botChat(`<p>선택하신 항목에 대한 정보를 찾을 수 없습니다.</p>`);
    }
}

//링크 연결이 포함된 챗봇 응답
function chatLink(includeText, link){
	
	botChat(`<div class="button-containers">
				<a class="bot-category-btn first-category-btn" href=${link}>${includeText} 링크</a>
				<button class="bot-category-btn final-category-btn" data-action="go-back">돌아가기</button>
			 </div>`);
}


// WebSocket 연결을 설정하고 메시지 구독을 처리하는 함수
function connect() {
    client = Stomp.over(new SockJS('/chatbot')); // Stomp 라이브러리와 SockJS를 사용하여 WebSocket 연결 생성
    
    client.connect({}, (frame) => {
        key = generateUniqueKey();  //고유 키 생성
        console.log(key);
        client.subscribe(`/topic/bot/${key}`, (answer) => { // 특정 토픽을 구독하여 서버로부터 메시지를 받음
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

//채팅 연결
function chatConnect(){
	
	disconnect();
	client = Stomp.over(new SockJS('/chatbot')); // Stomp 라이브러리와 SockJS를 사용하여 WebSocket 연결 생성
	    
	    client.connect({}, (frame) => {
	        key = generateUniqueKey();  //고유 키 생성
	        console.log(key);
	        client.subscribe(`/topic/answer/${key}`, (answer) => { // 특정 토픽을 구독하여 서버로부터 메시지를 받음
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
	
    
	if(websocketStatus == 1){
		client.send(`/message/chat/query`, {}, JSON.stringify(data));
	} else if(websocketStatus == 2){
		client.send(`/message/openai`, {}, JSON.stringify(data));
	} else{
		client.send(`/message/question`, {}, JSON.stringify(data));
	}
    
    clearQuestion();
}