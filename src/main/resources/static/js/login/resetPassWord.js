document.addEventListener('DOMContentLoaded', function() {
    const form = document.querySelector('form');
    const csrfToken = document.querySelector('meta[name="_csrf"]').getAttribute('content');
    const csrfHeader = document.querySelector('meta[name="_csrf_header"]').getAttribute('content');

    form.addEventListener('submit', function(e) {
        e.preventDefault();

        const token = document.querySelector('input[name="token"]').value;
        const password = document.querySelector('input[name="password"]').value;
        const confirmPassword = document.querySelector('input[name="confirmPassword"]').value;

        if (password !== confirmPassword) {
            alert('비밀번호가 일치하지 않습니다.');
            return;
        }

        fetch(`/reset-password?token=${token}`, {
            method: 'Post',
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
            return response.text();
        })
        .then(message => {
            alert(message);
            window.location.href = '/signin';
        })
        .catch(error => {
            console.error('Error:', error);
            alert('비밀번호 재설정 중 오류가 발생했습니다: ' + error.message);
        });
    });
});