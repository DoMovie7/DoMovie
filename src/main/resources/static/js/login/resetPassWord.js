document.addEventListener('DOMContentLoaded', function() {
    const form = document.querySelector('form');
    const csrfToken = document.querySelector('meta[name="_csrf"]').getAttribute('content');
    const csrfHeader = document.querySelector('meta[name="_csrf_header"]').getAttribute('content');
    const messageDiv = document.createElement('div');
    messageDiv.id = 'message';
    form.parentNode.insertBefore(messageDiv, form);

    function showErrorMessage(message) {
        messageDiv.textContent = message;
        messageDiv.className = 'error-message';
    }

    form.addEventListener('submit', function(e) {
        e.preventDefault();

        const token = document.querySelector('input[name="token"]').value;
        const password = document.querySelector('input[name="password"]').value;
        const confirmPassword = document.querySelector('input[name="confirmPassword"]').value;

        if (password !== confirmPassword) {
            showErrorMessage('비밀번호가 일치하지 않습니다.');
            return;
        }

        fetch(`/reset-password?token=${token}`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                [csrfHeader]: csrfToken
            },
            body: JSON.stringify({ newPassword: password })
        })
        .then(response => {
            if (!response.ok) {
                return response.text().then(text => { throw new Error(text) });
            }
            // 성공 시 바로 리다이렉트
            window.location.href = '/signin';
        })
        .catch(error => {
            console.error('Error:', error);
            showErrorMessage('비밀번호 재설정 중 오류가 발생했습니다: ' + error.message);
        });
    });
});
