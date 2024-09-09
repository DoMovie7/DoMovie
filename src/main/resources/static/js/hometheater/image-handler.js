document.addEventListener('DOMContentLoaded', function() {
    // 이미지 관련 코드
    const postThumbnail = document.querySelector('.post-thumbnail');
    const mainImage = postThumbnail.querySelector('div > img');
    const thumbnailList = postThumbnail.querySelector('ul');
    let originalSrc = mainImage.src;

    // 메인 이미지에 id 추가
    mainImage.id = 'mainImage';

    // 썸네일 리스트에 이벤트 리스너 추가
    thumbnailList.addEventListener('mouseover', function(e) {
        if (e.target.tagName === 'IMG') {
            originalSrc = mainImage.src;
            mainImage.src = e.target.src;
        }
    });

    thumbnailList.addEventListener('mouseout', function(e) {
        if (e.target.tagName === 'IMG') {
            mainImage.src = originalSrc;
        }
    });

    // 스타일 동적 추가
    const style = document.createElement('style');
    style.textContent = `
        .post-thumbnail {
            margin-bottom: 20px;
        }
        .post-thumbnail > div {
            text-align: center;
            margin-bottom: 20px;
        }
        #mainImage {
            width: 100%;
            max-width: 600px;
            height: auto;
            display: inline-block;
            transition: all 0.3s ease;
        }
        .post-thumbnail ul {
            list-style-type: none;
            padding: 0;
            display: flex;
            flex-wrap: wrap;
            justify-content: center;
            gap: 10px;
        }
        .post-thumbnail ul li {
            width: calc(20% - 10px);
            max-width: 100px;
            cursor: pointer;
        }
        .post-thumbnail ul li img {
            width: 100%;
            height: auto;
            object-fit: cover;
            border-radius: 5px;
            transition: all 0.3s ease;
        }
        .post-thumbnail ul li img:hover {
            transform: scale(1.1);
            box-shadow: 0 0 10px rgba(0,0,0,0.2);
        }
        @media (max-width: 768px) {
            .post-thumbnail ul li {
                width: calc(25% - 10px);
            }
        }
        @media (max-width: 480px) {
            .post-thumbnail ul li {
                width: calc(33.333% - 10px);
            }
        }
    `;
    document.head.appendChild(style);

    // 댓글 관련 코드
    const postId = document.querySelector('meta[name="post-id"]').getAttribute('content');
    const csrfToken = document.querySelector('meta[name="_csrf"]').getAttribute('content');
    const csrfHeader = document.querySelector('meta[name="_csrf_header"]').getAttribute('content');

    function loadComments() {
        fetch(`/hometheater/api/${postId}/comments`)
            .then(response => response.json())
            .then(comments => {
                const commentList = document.getElementById('commentList');
                commentList.innerHTML = ''; // 기존 댓글 제거
                comments.forEach(comment => {
                    const commentElement = document.createElement('div');
                    commentElement.className = 'comment';
                    commentElement.innerHTML = `
                        <div class="comment-author">${comment.author}</div>
                        <div class="comment-content">${comment.content}</div>
                        <div class="comment-date">${formatDate(comment.createdAt)}</div>
                    `;
                    commentList.appendChild(commentElement);
                });
            })
            .catch(error => console.error('댓글 로드 실패:', error));
    }

    function formatDate(dateString) {
        const date = new Date(dateString);
        return date.toLocaleString('ko-KR', { year: 'numeric', month: '2-digit', day: '2-digit', hour: '2-digit', minute: '2-digit' });
    }

    // 페이지 로드 시 댓글 로드
    loadComments();

    // 댓글 작성 폼 제출 이벤트
    const commentForm = document.getElementById('commentForm');
    commentForm.addEventListener('submit', function(e) {
        e.preventDefault();
        const content = document.getElementById('commentContent').value;

        fetch(this.action, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
                [csrfHeader]: csrfToken
            },
            body: `content=${encodeURIComponent(content)}`
        })
            .then(response => response.json())
            .then(data => {
                document.getElementById('commentContent').value = ''; // 입력 필드 초기화
                loadComments(); // 댓글 목록 새로고침
            })
            .catch(error => {
                console.error('댓글 작성 실패:', error);
                alert('댓글 작성에 실패했습니다. 다시 시도해 주세요.');
            });
    });

    // 게시글 수정 및 삭제 관련 코드
    const editButton = document.getElementById('editButton');
    const deleteButton = document.getElementById('deleteButton');
    const saveButton = document.getElementById('saveButton');
    const cancelButton = document.getElementById('cancelButton');
    const postTitle = document.getElementById('postTitle');
    const postContent = document.getElementById('postContent');
    const editForm = document.getElementById('editForm');
    const editTitle = document.getElementById('editTitle');
    const editContent = document.getElementById('editContent');

    editButton.addEventListener('click', function() {
        postTitle.style.display = 'none';
        postContent.style.display = 'none';
        editForm.style.display = 'block';
        this.style.display = 'none';
    });

    cancelButton.addEventListener('click', function() {
        editForm.style.display = 'none';
        postTitle.style.display = 'block';
        postContent.style.display = 'block';
        editButton.style.display = 'inline-block';
    });

    saveButton.addEventListener('click', function() {
        const title = editTitle.value;
        const content = editContent.value;

        fetch(`/hometheater/api/${postId}`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
                [csrfHeader]: csrfToken
            },
            body: JSON.stringify({ title: title, content: content })
        })
            .then(response => response.json())
            .then(data => {
                postTitle.textContent = data.title;
                postContent.innerHTML = data.content;
                editForm.style.display = 'none';
                postTitle.style.display = 'block';
                postContent.style.display = 'block';
                editButton.style.display = 'inline-block';
                alert('게시글이 수정되었습니다.');
            })
            .catch(error => {
                console.error('게시글 수정 실패:', error);
                alert('게시글 수정에 실패했습니다. 다시 시도해 주세요.');
            });
    });

    deleteButton.addEventListener('click', function() {
        if (confirm('정말로 이 게시글을 삭제하시겠습니까?')) {
            fetch(`/hometheater/api/${postId}`, {
                method: 'DELETE',
                headers: {
                    [csrfHeader]: csrfToken
                }
            })
                .then(response => {
                    if (response.ok) {
                        alert('게시글이 삭제되었습니다.');
                        window.location.href = '/hometheater/list'; // 리스트 페이지로 이동
                    } else {
                        throw new Error('게시글 삭제 실패');
                    }
                })
                .catch(error => {
                    console.error('게시글 삭제 실패:', error);
                    alert('게시글 삭제에 실패했습니다. 다시 시도해 주세요.');
                });
        }
    });
});