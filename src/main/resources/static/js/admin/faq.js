const token = document.querySelector('meta[name="_csrf"]').getAttribute('content');
const header = document.querySelector('meta[name="_csrf_header"]').getAttribute('content');

function createFAQTree(data, parentElement) {
	// 데이터 배열의 각 항목에 대해 반복
	data.forEach(item => {
		// 새로운 div 요소 생성
		const itemElement = document.createElement('div');
		itemElement.className = 'faq-item';

		itemElement.innerHTML = `
                <div class="faq-question">
            		<div class="main-text-id">
                        <h3>${item.name}</h3>
                        <input value="${item.id}" type="hidden">
                        <button class="delete-btn">-</button>
                    </div>
                    <button class="faq-toggle">▼</button>
                </div>
                <div class="faq-answer">
                    <div class="faq-answer-content">
                    	${item.content ? `<p>${item.content}</p>` : ''}
                        <div class="faq-actions">
                            <button class="add-btn">+</button>
                            <!-- <button class="edit-btn">수정</button> -->
                            <!-- <button class="delete-btn">삭제</button> -->
                        </div>
                    </div>
                    <div class="children"></div>
                </div>
            `;
		parentElement.appendChild(itemElement); // 생성된 요소를 부모 요소에 추가

		// 삭제 버튼에 대한 이벤트 리스너 추가
		const deleteBtn = itemElement.querySelector('.delete-btn');
		deleteBtn.addEventListener('click', (e) => {
			e.stopPropagation(); // 이벤트 버블링 방지
			const id = item.id;
			deleteFAQItem(id);
		});

		// 질문, 답변, 토글 버튼 요소 선택
		const questionElement = itemElement.querySelector('.faq-question');
		const answerElement = itemElement.querySelector('.faq-answer');
		const toggleButton = itemElement.querySelector('.faq-toggle');

		questionElement.addEventListener('click', () => {
			const isOpen = answerElement.classList.contains('active');
			if (isOpen) {
				answerElement.classList.remove('active');
				toggleButton.textContent = '▼';
			} else {
				answerElement.classList.add('active');
				toggleButton.textContent = '▲';
			}
		});

		// 자식 항목을 위한 컨테이너 선택
		const childrenContainer = itemElement.querySelector('.children');

		// 자식 항목이 있으면 재귀적으로 createFAQTree 함수 호출
		if (item.children && item.children.length > 0) {
			createFAQTree(item.children, childrenContainer);
		}
	});
}

function addNewFAQItem(parentElement) {
	const newItem = document.createElement('div');
	newItem.className = 'faq-item';
	newItem.innerHTML = `
        	<div class="faq-question">
                <form class="main-text-id" id="faqForm">
                    <input type="hidden" name="${header}" value="${token}"/>
                    <input class="faq-input" type="text" name="name" placeholder="카테고리 입력">
                    <button class="complete-btn" id="complete-btn" type="submit">완료</button>
                </form>
                <button class="faq-toggle">▼</button>
            </div>
            <div class="faq-answer">
                <div class="faq-answer-content">
                    <textarea class="faq-input" placeholder="답변 입력"></textarea>
                    <div class="faq-actions">
                        <button class="save-btn">저장</button>
                        <button class="cancel-btn">취소</button>
                    </div>
                </div>
            </div>
        `;
	parentElement.insertBefore(newItem, parentElement.firstChild);

	const form = newItem.querySelector('#faqForm');
	form.addEventListener('submit', function(e) {
		//e.preventDefault(); // 기본 제출 동작 방지
		const formData = new FormData(this);
		fetch('/admin/faqs/first', {
			method: 'POST',
			body: formData,
			headers: {
				[header]: token
			}
		})
			.then(response => {
				if (!response.ok) {
					throw new Error('Network response was not ok');
				}
				return response.json();
			})
			.then(data => {
				console.log('Success:', data);
				location.reload();
			})
			.catch((error) => {
				console.error('Error:', error);
				// 에러 처리 로직
				// 예: 에러 메시지 표시
			});
	});

	const saveBtn = newItem.querySelector('.save-btn');
	const cancelBtn = newItem.querySelector('.cancel-btn');

	saveBtn.addEventListener('click', () => {
		const questionInput = newItem.querySelector('.faq-question input');
		const answerTextarea = newItem.querySelector('.faq-answer textarea');
		// 여기에 저장 로직 추가 (예: API 호출)
		console.log('저장:', questionInput.value, answerTextarea.value);
	});

	cancelBtn.addEventListener('click', () => {
		parentElement.removeChild(newItem);
	});
}

// FAQ 항목 삭제 함수
function deleteFAQItem(id) {
	if (confirm('정말로 이 항목을 삭제하시겠습니까?')) {
		fetch(`/admin/faqs/${id}`, {
			method: 'DELETE',
			headers: {
				[header]: token,
				'Content-Type': 'application/json'
			}
		})
			.then(response => {
				return response.json();
			})
			.then(data => {
				console.log('Delete successful:', data);
				location.reload(); // 페이지 새로고침
			})
			.catch((error) => {
				console.error('Error:', error);
				location.reload();
			});
	}
}

// FAQ 트리 생성
const faqListElement = document.getElementById('faq-list');
createFAQTree(faqData, faqListElement);

// 최상위 추가 버튼과 완료, 취소 버튼
const addBtn = document.getElementById('first-add-btn');
const cancelBtn = document.getElementById('cancel-btn');

// 추가 버튼 클릭 이벤트
addBtn.addEventListener('click', () => {
	addNewFAQItem(faqListElement);
	addBtn.style.display = 'none';
	cancelBtn.style.display = 'inline-block';
});

/*    
// 완료 버튼 클릭 이벤트
const completeBtn = document.getElementById('complete-btn');
completeBtn.addEventListener('click', () => {
	completeBtn.style.display = 'none';
	cancelBtn.style.display = 'none';
	addBtn.style.display ='inline-block';
	// 여기에 완료 시 수행할 작업 추가 (예: 서버에 데이터 전송)
	console.log('FAQ 추가 완료');
});
*/


//취소 버튼 클릭 이벤트
cancelBtn.addEventListener('click', () => {

	if (faqListElement.firstChild) {
		faqListElement.removeChild(faqListElement.firstChild);
	}

	completeBtn.style.display = 'none';
	cancelBtn.style.display = 'none';
	addBtn.style.display = 'inline-block';
});