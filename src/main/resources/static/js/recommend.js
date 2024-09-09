// 문서의 모든 콘텐츠가 로드된 후 실행되는 함수입니다.
document.addEventListener('DOMContentLoaded', function() {
    // 클래스가 'tab'인 모든 요소를 선택하여 NodeList로 저장합니다.
    const tabs = document.querySelectorAll('.tab');
    
    // 각 탭 요소에 대해 클릭 이벤트 리스너를 추가합니다.
    tabs.forEach(tab => {
        tab.addEventListener('click', function() {
            // 현재 활성화된 탭에서 'active' 클래스를 제거하여 비활성화 상태로 만듭니다.
            document.querySelector('.tab.active').classList.remove('active');
            
            // 클릭한 탭에 'active' 클래스를 추가하여 활성화 상태로 만듭니다.
            this.classList.add('active');
            
            // 클릭한 탭의 'data-content' 속성 값(콘텐츠 ID)을 가져옵니다.
            const contentId = this.getAttribute('data-content');
            
            // 비동기 요청을 통해 탭 콘텐츠를 로드하는 함수를 호출합니다.
            loadTabContent(contentId);
        });
    });
    
    function loadTabContent(genreIdx) {
        // 탭 콘텐츠를 로드할 URL을 설정합니다. 실제 URL로 변경해야 합니다.
        const url = `/genres/${genreIdx}/recommendations`;
        
        // Fetch API를 사용하여 비동기 요청을 보냅니다.
        fetch(url)
            .then(response => response.text())  // 서버 응답을 텍스트로 변환합니다.
            .then(html => {
                // 'tabId' 요소의 내용을 로드한 HTML로 업데이트합니다.
                // 'tabId'는 코드에서 정의되지 않았으므로 실제로는 적절한 요소 ID로 변경해야 합니다.
                document.getElementById(tabId).innerHTML = html;
            })
            .catch(error => {
                // 오류가 발생한 경우, 콘솔에 오류 메시지를 출력합니다.
                console.error('탭 콘텐츠 로드 실패:', error);
            });
    }
});
