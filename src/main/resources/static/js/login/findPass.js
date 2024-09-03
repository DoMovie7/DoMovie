document.addEventListener('DOMContentLoaded', function() {
    const findPasswordForm = document.getElementById('findPasswordForm');
    const messageElement = document.getElementById('message');
	
	const csrfToken = document.querySelector('meta[name="_csrf"]').getAttribute('content');
	const csrfHeader = document.querySelector('meta[name="_csrf_header"]').getAttribute('content');

    findPasswordForm.addEventListener('submit', function(e) {
        e.preventDefault();
        
        const userName = document.getElementById('userName').value;
        const email = document.getElementById('email').value;

        fetch('/api/find-password', {
            method: 'POST',
            headers: {
				[csrfHeader]: csrfToken,
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({ userName, email }),
        })
        .then(response => response.json())
        .then(data => {
            if (data.message) {
                showMessage(data.message, 'success');
            } else {
                throw new Error(data.error || '알 수 없는 오류가 발생했습니다.');
            }
        })
        .catch(error => {
            showMessage(error.message, 'error');
        });
    });

    function showMessage(message, type) {
        messageElement.textContent = message;
        messageElement.className = type;
    }
});