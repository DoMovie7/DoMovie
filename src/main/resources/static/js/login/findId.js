document.addEventListener('DOMContentLoaded', function() {
    const form = document.getElementById('findIdForm');
    const nameInput = document.getElementById('name');
    const phoneInput = document.getElementById('phone');
    const requestVerificationBtn = document.getElementById('requestVerification');
    const verificationGroup = document.getElementById('verificationGroup');
    const verificationCodeInput = document.getElementById('verificationCode');
    const verifyCodeBtn = document.getElementById('verifyCode');
    const resultDiv = document.getElementById('result');

    requestVerificationBtn.addEventListener('click', () => {
        if (nameInput.value && phoneInput.value && phoneInput.validity.valid) {
            // 실제로는 서버에 인증번호를 요청하는 API를 호출해야 합니다.
            alert('인증번호가 발송되었습니다. (이는 실제 발송되지 않은 예시입니다)');
            verificationGroup.classList.remove('hidden');
            verifyCodeBtn.classList.remove('hidden');
            requestVerificationBtn.disabled = true;
        } else {
            alert('이름과 올바른 형식의 연락처를 입력해주세요.');
        }
    });

    form.addEventListener('submit', (e) => {
        e.preventDefault();
        if (verificationCodeInput.value) {
            // 실제로는 서버에 인증번호를 확인하고 아이디를 조회하는 API를 호출해야 합니다.
            resultDiv.textContent = '회원님의 아이디는 user123입니다. (이는 예시입니다)';
            resultDiv.classList.remove('hidden');
        } else {
            alert('인증번호를 입력해주세요.');
        }
    });
});