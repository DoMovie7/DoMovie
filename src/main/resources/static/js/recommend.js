/**
 * 
 */
document.addEventListener('DOMContentLoaded', function() {
    const tabs = document.querySelectorAll('.tab');
    tabs.forEach(tab => {
        tab.addEventListener('click', function() {
            // 현재 활성화된 탭 비활성화
            document.querySelector('.tab.active').classList.remove('active');
                        
            // 클릭한 탭 활성화
            this.classList.add('active');
            const contentId = this.getAttribute('data-content');
            // 비동기 요청을 통해 탭 콘텐츠 로드
            loadTabContent(contentId);
        });
    });
    
    function loadTabContent(genreIdx) {
        // 예시 URL; 실제 URL로 변경해야 함
        const url = `/genres/${genreIdx}/recommendations`;
        fetch(url)
            .then(response => response.text())
            .then(html => {
                document.getElementById(tabId).innerHTML = html;
            })
            .catch(error => {
                console.error('탭 콘텐츠 로드 실패:', error);
            });
    }
});