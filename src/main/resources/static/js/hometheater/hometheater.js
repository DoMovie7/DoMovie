document.addEventListener('DOMContentLoaded', function() {
    // 카테고리 탭 기능
    const tabs = document.querySelectorAll('#category-menu .tab');
    const posts = document.querySelectorAll('.board-table tbody tr');

    tabs.forEach(tab => {
        tab.addEventListener('click', () => {
            // 활성 탭 변경
            tabs.forEach(t => t.classList.remove('active'));
            tab.classList.add('active');

            // 카테고리에 따른 게시물 필터링
            const category = tab.textContent;
            posts.forEach(post => {
                if (category === 'ALL' || post.dataset.category === category) {
                    post.style.display = '';
                } else {
                    post.style.display = 'none';
                }
            });
        });
    });
});