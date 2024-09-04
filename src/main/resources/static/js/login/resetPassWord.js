document.addEventListener('DOMContentLoaded', function() {
    const form = document.querySelector('form');
    const passwordInput = document.querySelector('input[name="password"]');
    const confirmPasswordInput = document.querySelector('input[name="confirmPassword"]');
    const submitButton = document.querySelector('button[type="submit"]');

    function validatePassword() {
        const password = passwordInput.value;
        const confirmPassword = confirmPasswordInput.value;

        // 비밀번호 길이 검사
        if (password.length < 8) {
            setError(passwordInput, '비밀번호는 최소 8자 이상이어야 합니다.');
            return false;
        }

        // 비밀번호 복잡성 검사 (예: 숫자와 특수문자 포함)
        if (!/^(?=.*\d)(?=.*[!@#$%^&*])/.test(password)) {
            setError(passwordInput, '비밀번호는 숫자와 특수문자를 포함해야 합니다.');
            return false;
        }

        // 비밀번호 일치 검사
        if (password !== confirmPassword) {
            setError(confirmPasswordInput, '비밀번호가 일치하지 않습니다.');
            return false;
        }

        clearError(passwordInput);
        clearError(confirmPasswordInput);
        return true;
    }

    function setError(input, message) {
        const errorElement = input.parentElement.querySelector('.error-message') || document.createElement('div');
        errorElement.className = 'error-message';
        errorElement.textContent = message;
        if (!input.parentElement.contains(errorElement)) {
            input.parentElement.appendChild(errorElement);
        }
    }

    function clearError(input) {
        const errorElement = input.parentElement.querySelector('.error-message');
        if (errorElement) {
            errorElement.remove();
        }
    }

    passwordInput.addEventListener('input', validatePassword);
    confirmPasswordInput.addEventListener('input', validatePassword);

    form.addEventListener('submit', function(e) {
        if (!validatePassword()) {
            e.preventDefault();
        }
    });
});