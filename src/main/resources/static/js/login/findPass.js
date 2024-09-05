document.addEventListener('DOMContentLoaded', function() {
    const findPasswordForm = document.getElementById('findPasswordForm');
    const messageElement = document.getElementById('message');

    const csrfToken = document.querySelector('meta[name="_csrf"]').getAttribute('content');
    const csrfHeader = document.querySelector('meta[name="_csrf_header"]').getAttribute('content');

    findPasswordForm.addEventListener('submit', function(e) {
        e.preventDefault();

        const email = document.getElementById('email').value;

        // 입력값 검증
        if (!email) {
            showMessage('이메일을 입력해주세요.', 'error');
            return;
        }

        fetch('/api/find-password', {
            method: 'POST',
            headers: {
                [csrfHeader]: csrfToken,
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({ email }),
        })
        .then(response => {
            if (!response.ok) {
                return response.json().then(err => { throw err; });
            }
            return response.json();
        })
        .then(data => {
            showMessage(data.message, 'success');
            findPasswordForm.reset(); // 폼 초기화
        })
        .catch(error => {
            showMessage(error.message || '알 수 없는 오류가 발생했습니다.', 'error');
        });
    });

    function showMessage(message, type) {
        messageElement.textContent = message;
        messageElement.className = `message ${type}`;
        messageElement.style.display = 'block';

        // 3초 후 메시지 숨기기
        setTimeout(() => {
            messageElement.style.display = 'none';
        }, 3000);
    }
});