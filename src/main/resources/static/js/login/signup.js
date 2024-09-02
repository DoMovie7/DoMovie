document.addEventListener('DOMContentLoaded', function() {
    const form = document.getElementById('signupForm');
    const birthDateInput = document.getElementById('birthDate');
    const emailCheckButton = document.getElementById('emailCheckButton');
    let isEmailVerified = false;

    // 에러 메시지 표시 함수
    function showError(inputName, message) {
        const errorDiv = document.getElementById(inputName + 'Error');
        errorDiv.textContent = message;
        errorDiv.className = 'error-message';
    }

    // 성공 메시지 표시 함수
    function showSuccess(inputName, message) {
        const errorDiv = document.getElementById(inputName + 'Error');
        errorDiv.textContent = message;
        errorDiv.className = 'success-message';
    }

    // 에러 메시지 초기화 함수
    function clearError(inputName) {
        const errorDiv = document.getElementById(inputName + 'Error');
        errorDiv.textContent = '';
        errorDiv.className = '';
    }

    // 이름 유효성 검사
    function validateUserName() {
        const userName = document.querySelector('input[name="userName"]').value.trim();
        if (!userName) {
            showError('userName', '이름을 입력해주세요.');
            return false;
        } else if (/[!@#$%^&*(),.?":{}|<>]/.test(userName)) {
            showError('userName', '이름에 특수문자를 사용할 수 없습니다.');
            return false;
        } else {
            clearError('userName');
            return true;
        }
    }

    // 닉네임 유효성 검사
    function validateNickName() {
        const nickName = document.querySelector('input[name="nickName"]').value.trim();
        if (!nickName) {
            showError('nickName', '닉네임을 입력해주세요.');
            return false;
        } else if (nickName.length > 20) {
            showError('nickName', '닉네임은 20자 이내여야 합니다.');
            return false;
        } else {
            clearError('nickName');
            return true;
        }
    }

    // 이메일 유효성 검사
    function validateEmail() {
        const email = document.querySelector('input[name="email"]').value.trim();
        const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        if (!email) {
            showError('email', '이메일을 입력해주세요.');
            return false;
        } else if (!emailRegex.test(email)) {
            showError('email', '올바른 이메일 주소를 입력해주세요.');
            return false;
        } else {
            clearError('email');
            return true;
        }
    }

    // 전화번호 유효성 검사
    function validatePhoneNumber() {
        const phoneNumber = document.querySelector('input[name="phoneNumber"]').value.trim();
        const phoneRegex = /^01[016789]-?\d{3,4}-?\d{4}$/;
        if (!phoneNumber) {
            showError('phoneNumber', '전화번호를 입력해주세요.');
            return false;
        } else if (!phoneRegex.test(phoneNumber)) {
            showError('phoneNumber', '올바른 전화번호를 입력해주세요.');
            return false;
        } else {
            clearError('phoneNumber');
            return true;
        }
    }

    // 비밀번호 유효성 검사
    function validatePassword() {
        const password = document.querySelector('input[name="password"]').value;
        const passwordRegex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,}$/;
        if (!password) {
            showError('password', '비밀번호를 입력해주세요.');
            return false;
        } else if (!passwordRegex.test(password)) {
            showError('password', '비밀번호는 8자 이상이며, 대문자, 소문자, 숫자, 특수문자를 포함해야 합니다.');
            return false;
        } else {
            clearError('password');
            return true;
        }
    }

    // 비밀번호 확인 유효성 검사
    function validateConfirmPassword() {
        const password = document.querySelector('input[name="password"]').value;
        const confirmPassword = document.querySelector('input[name="confirmPassword"]').value;
        if (!confirmPassword) {
            showError('confirmPassword', '비밀번호 확인을 입력해주세요.');
            return false;
        } else if (password !== confirmPassword) {
            showError('confirmPassword', '비밀번호가 일치하지 않습니다.');
            return false;
        } else {
            showSuccess('confirmPassword', '비밀번호가 일치합니다.');
            return true;
        }
    }

    // 생년월일 입력 필드 포맷팅
    birthDateInput.addEventListener('input', function(e) {
        let value = e.target.value.replace(/\D/g, ''); // 숫자만 남기기
        if (value.length > 4) {
            value = value.slice(0, 4) + '-' + value.slice(4);
        }
        if (value.length > 7) {
            value = value.slice(0, 7) + '-' + value.slice(7);
        }
        e.target.value = value.slice(0, 10); // YYYY-MM-DD 형식의 최대 길이
    });

    // 생년월일 유효성 검사
    function validateBirthDate() {
        const birthDate = birthDateInput.value.trim();
        const birthRegex = /^\d{4}-\d{2}-\d{2}$/;
        if (!birthDate) {
            showError('birthDate', '생년월일을 입력해주세요.');
            return false;
        } else if (!birthRegex.test(birthDate)) {
            showError('birthDate', '올바른 생년월일 형식(YYYY-MM-DD)으로 입력해주세요.');
            return false;
        } else {
            const [year, month, day] = birthDate.split('-').map(Number);
            const inputDate = new Date(year, month - 1, day);
            const currentDate = new Date();
            
            if (inputDate > currentDate) {
                showError('birthDate', '생년월일은 현재 날짜보다 이후일 수 없습니다.');
                return false;
            } else if (inputDate.getFullYear() !== year || inputDate.getMonth() !== month - 1 || inputDate.getDate() !== day) {
                showError('birthDate', '유효하지 않은 날짜입니다.');
                return false;
            } else {
                clearError('birthDate');
                return true;
            }
        }
    }

   if (emailCheckButton) {
        emailCheckButton.addEventListener('click', function(event) {
            event.preventDefault(); // 폼 제출 방지
            const email = document.querySelector('input[name="email"]').value.trim();
            if (validateEmail()) {
                // CSRF 토큰 가져오기
                const csrfToken = document.querySelector('input[name="_csrf"]').value;
                
                fetch('/api/check-email', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json',
                        'X-CSRF-TOKEN': csrfToken
                    },
                    body: JSON.stringify({ email: email })
                })
                .then(response => {
                    if (!response.ok) {
                        throw new Error('Server response was not ok');
                    }
                    return response.json();
                })
                .then(data => {
                    if (data.isDuplicate) {
                        showError('email', '이미 사용 중인 이메일입니다. 다른 이메일을 사용해 주세요.');
                        isEmailVerified = false;
                        emailCheckButton.textContent = '다시 확인';
                    } else {
                        showSuccess('email', '사용 가능한 이메일입니다.');
                        isEmailVerified = true;
                        emailCheckButton.textContent = '확인 완료';
                        emailCheckButton.disabled = true;
                    }
                })
                .catch(error => {
                    console.error('Error:', error);
                    showError('email', '이메일 중복 확인 중 오류가 발생했습니다. 다시 시도해 주세요.');
                    isEmailVerified = false;
                });
            }
        });
    } else {
        console.error('Email check button not found');
    }

    // 각 입력 필드에 blur 이벤트 리스너 추가
    document.querySelector('input[name="userName"]').addEventListener('blur', validateUserName);
    document.querySelector('input[name="nickName"]').addEventListener('blur', validateNickName);
    document.querySelector('input[name="email"]').addEventListener('blur', validateEmail);
    document.querySelector('input[name="phoneNumber"]').addEventListener('blur', validatePhoneNumber);
    document.querySelector('input[name="password"]').addEventListener('blur', validatePassword);
    document.querySelector('input[name="confirmPassword"]').addEventListener('blur', validateConfirmPassword);
    birthDateInput.addEventListener('blur', validateBirthDate);

    // 이메일 입력 필드에 변화가 있을 때마다 버튼 상태 초기화
    document.querySelector('input[name="email"]').addEventListener('input', function() {
        emailCheckButton.textContent = '중복 확인';
        emailCheckButton.disabled = false;
        isEmailVerified = false;
        clearError('email');
    });

    // 실시간 비밀번호 확인 검사
    const passwordInput = document.querySelector('input[name="password"]');
    const confirmPasswordInput = document.querySelector('input[name="confirmPassword"]');

    function checkPasswordMatch() {
        if (passwordInput.value && confirmPasswordInput.value) {
            validateConfirmPassword();
        } else {
            clearError('confirmPassword');
        }
    }

    passwordInput.addEventListener('input', checkPasswordMatch);
    confirmPasswordInput.addEventListener('input', checkPasswordMatch);

    // 폼 제출 이벤트
    form.addEventListener('submit', function(event) {
        event.preventDefault();
        
        const isUserNameValid = validateUserName();
        const isNickNameValid = validateNickName();
        const isEmailValid = validateEmail() && isEmailVerified;
        const isPhoneNumberValid = validatePhoneNumber();
        const isPasswordValid = validatePassword();
        const isConfirmPasswordValid = validateConfirmPassword();
        const isBirthDateValid = validateBirthDate();

        if (isUserNameValid && isNickNameValid && isEmailValid && isPhoneNumberValid && 
            isPasswordValid && isConfirmPasswordValid && isBirthDateValid) {
            
            // 생년월일 형식 변환 (YYYYMMDD)
            const formattedBirthDate = birthDateInput.value.replace(/-/g, '');
            
            // 숨겨진 입력 필드 생성 또는 업데이트
            let hiddenBirthDate = document.getElementById('hiddenBirthDate');
            if (!hiddenBirthDate) {
                hiddenBirthDate = document.createElement('input');
                hiddenBirthDate.type = 'hidden';
                hiddenBirthDate.id = 'hiddenBirthDate';
                hiddenBirthDate.name = 'birthDate';
                form.appendChild(hiddenBirthDate);
            }
            hiddenBirthDate.value = formattedBirthDate;

            // 원래의 birthDate 입력 필드의 name 속성 제거
            birthDateInput.removeAttribute('name');

            // 폼 제출
            this.submit();
        } else {
            // 유효성 검사 실패 시 사용자에게 알림
            alert('모든 필드를 올바르게 입력하고 이메일 중복 확인을 완료해주세요.');
        }
    });
});