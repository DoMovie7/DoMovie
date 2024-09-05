document.addEventListener('DOMContentLoaded', function() {
    const form = document.getElementById('findIdForm');
    const nameInput = document.getElementById('name');
    const birthDateInput = document.getElementById('birthDate');
    const resultDiv = document.getElementById('result');

    // 생년월일 입력 포맷팅
    birthDateInput.addEventListener('input', function(e) {
        let input = e.target.value.replace(/\D/g, '').substring(0, 8); // 숫자만 추출, 최대 8자
        let formatted = '';

        if (input.length > 4) {
            formatted += input.substring(0, 4) + '-';
            if (input.length > 6) {
                formatted += input.substring(4, 6) + '-' + input.substring(6);
            } else {
                formatted += input.substring(4);
            }
        } else {
            formatted = input;
        }

        e.target.value = formatted;
    });

    form.addEventListener('submit', function(e) {
        e.preventDefault();
        findId();
    });

    function findId() {
        const name = nameInput.value.trim();
        const birthDate = birthDateInput.value.replace(/-/g, ''); // 하이픈 제거

        if (!name || birthDate.length !== 8) {
            showResult('이름과 올바른 형식의 생년월일을 입력해주세요.');
            return;
        }

        fetch('/api/find-id', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({ userName: name, birthDate: birthDate })
        })
        .then(response => {
            if (!response.ok) {
                throw new Error('서버 응답 오류');
            }
            return response.json();
        })
        .then(data => {
            if (data.emails && data.emails.length > 0) {
                const emailList = data.emails.map(email => `<li>${maskEmail(email)}</li>`).join('');
                showResult(`<p>귀하의 이메일 주소:</p><ul>${emailList}</ul>`);
            } else {
                showResult('일치하는 사용자 정보를 찾을 수 없습니다.');
            }
        })
        .catch(error => {
            console.error('Error:', error);
            showResult('오류가 발생했습니다. 다시 시도해 주세요.');
        });
    }

    function showResult(message) {
        form.style.display = 'none';
        resultDiv.innerHTML = message;
        resultDiv.style.display = 'block';
    }

    function maskEmail(email) {
        const [localPart, domain] = email.split('@');
        const maskedLocalPart = localPart.substring(0, 2) + '*'.repeat(localPart.length - 2);
        return `${maskedLocalPart}@${domain}`;
    }
});