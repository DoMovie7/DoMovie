document.addEventListener('DOMContentLoaded', function() {
    // CSRF 토큰 설정
    const token = document.querySelector('meta[name="_csrf"]').getAttribute('content');
    const header = document.querySelector('meta[name="_csrf_header"]').getAttribute('content');

    // DOM 요소 선택
    const profilePic = document.getElementById('profile-picture');
    const fileInput = document.getElementById('profile-upload');
    const profileBtn = document.getElementById('profile-btn');
    const tierBtn = document.getElementById('tier-btn');
    const myPostsBtn = document.getElementById('my-posts-btn');
    const profileContent = document.getElementById('profile-content');
    const tierContent = document.getElementById('tier-content');
    const myPostsContent = document.getElementById('my-posts-content');
    const nicknameForm = document.getElementById('nickname-form');
    const nicknameDisplay = document.getElementById('nickname');
    const nicknameInput = document.getElementById('nickname-input');
    const editNicknameBtn = document.getElementById('edit-nickname-btn');
    const saveNicknameBtn = document.getElementById('save-nickname-btn');
    const cancelNicknameBtn = document.getElementById('cancel-nickname-btn');
    const changePasswordBtn = document.getElementById('change-password-btn');
    const passwordPopup = document.getElementById('password-popup');
    const closePopupBtn = document.getElementById('close-popup');
    const passwordForm = document.getElementById('password-form');
    const popupOverlay = document.querySelector('.popup-overlay');

    // 생년월일 및 핸드폰 번호 관련 요소 선택
    const birthdateDisplay = document.getElementById('birthdate-display');
    const birthdateForm = document.getElementById('birthdate-form');
    const birthdateInput = document.getElementById('birthdate-input');
    const editBirthdateBtn = document.getElementById('edit-birthdate-btn');
    const saveBirthdateBtn = document.getElementById('save-birthdate-btn');
    const cancelBirthdateBtn = document.getElementById('cancel-birthdate-btn');

    const phoneNumberDisplay = document.getElementById('phoneNumber-display');
    const phoneNumberForm = document.getElementById('phoneNumber-form');
    const phoneNumberInput = document.getElementById('phoneNumber-input');
    const editPhoneNumberBtn = document.getElementById('edit-phoneNumber-btn');
    const savePhoneNumberBtn = document.getElementById('save-phoneNumber-btn');
    const cancelPhoneNumberBtn = document.getElementById('cancel-phoneNumber-btn');

    // 원래 프로필 이미지 src 저장
    const originalProfilePicSrc = profilePic.src;

    // 등급 시스템 클래스 정의
    class TierSystem {
        constructor(postCount) {
            this.postCount = postCount;
            this.tiers = {
                'corn': { image: '/img/tier/Asset 1.png', description: '나의 등급은 옥수수입니다.' },
                'popcorn': { image: '/img/tier/Asset 2.png', description: '나의 등급은 터진 옥수수입니다.' },
                'fullPopcorn': { image: '/img/tier/Asset 3.png', description: '나의 등급은 팝콘입니다.' }
            };
        }

        getCurrentTier() {
            if (this.postCount >= 6) return this.tiers.fullPopcorn;
            if (this.postCount >= 3) return this.tiers.popcorn;
            return this.tiers.corn;
        }
    }

    // 컨텐츠 로드 함수
    function loadContent(type) {
        profileContent.style.display = type === 'profile' ? 'block' : 'none';
        tierContent.style.display = type === 'tier' ? 'block' : 'none';
        myPostsContent.style.display = type === 'my-posts' ? 'block' : 'none';
    }

    // 활성 버튼 설정 함수
    function setActiveButton(button) {
        [profileBtn, tierBtn, myPostsBtn].forEach(btn => btn.classList.remove('active'));
        button.classList.add('active');
    }

    // 닉네임 수정 모드 토글 함수
    function toggleNicknameEditMode(isEditing) {
        nicknameDisplay.style.display = isEditing ? 'none' : 'inline';
        nicknameForm.style.display = isEditing ? 'inline-flex' : 'none';
        editNicknameBtn.style.display = isEditing ? 'none' : 'inline';

        if (isEditing) {
            nicknameInput.value = nicknameDisplay.textContent;
            nicknameInput.focus();
        }
    }

    // 생년월일 수정 모드 토글 함수
    function toggleBirthdateEditMode(isEditing) {
        birthdateDisplay.style.display = isEditing ? 'none' : 'inline';
        birthdateForm.style.display = isEditing ? 'inline-flex' : 'none';
        editBirthdateBtn.style.display = isEditing ? 'none' : 'inline';

        if (isEditing) {
            birthdateInput.value = birthdateDisplay.textContent;
            birthdateInput.focus();
        }
    }

    // 핸드폰 번호 수정 모드 토글 함수
    function togglePhoneNumberEditMode(isEditing) {
        phoneNumberDisplay.style.display = isEditing ? 'none' : 'inline';
        phoneNumberForm.style.display = isEditing ? 'inline-flex' : 'none';
        editPhoneNumberBtn.style.display = isEditing ? 'none' : 'inline';

        if (isEditing) {
            phoneNumberInput.value = phoneNumberDisplay.textContent;
            phoneNumberInput.focus();
        }
    }

    // 이미지 변경 함수
    function changeProfileImage(file) {
        const formData = new FormData();
        formData.append('file', file);
        fetch('/uploadImage', {
            method: 'POST',
            headers: {
                [header]: token
            },
            body: formData,
            credentials: 'include'
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error('이미지 업로드 실패');
                }
                return response.json();
            })
            .then(data => {
                profilePic.src = data.imageUrl;
                alert('프로필 이미지가 성공적으로 변경되었습니다.');
            })
            .catch(error => {
                console.error('이미지 업로드 중 오류 발생:', error);
                alert('이미지 업로드에 실패했습니다: ' + error.message);
            });
    }

    // 비밀번호 변경 팝업 표시 함수
    function showPasswordPopup() {
        popupOverlay.classList.add('active');
        passwordPopup.classList.add('active');
    }

    // 비밀번호 변경 팝업 닫기 함수
    function closePasswordPopup() {
        popupOverlay.classList.remove('active');
        passwordPopup.classList.remove('active');
        passwordForm.reset();
    }

    // 비밀번호 변경 처리 함수
    function changePassword(e) {
        e.preventDefault();
        const currentPassword = document.getElementById('current-password').value;
        const newPassword = document.getElementById('new-password').value;
        const confirmPassword = document.getElementById('confirm-password').value;

        if (newPassword !== confirmPassword) {
            alert('새 비밀번호와 확인 비밀번호가 일치하지 않습니다.');
            return;
        }

        const updateData = {
            currentPassword: currentPassword,
            newPassword: newPassword
        };

        fetch('/updateProfile', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                [header]: token
            },
            body: JSON.stringify(updateData),
            credentials: 'include'
        })
            .then(response => {
                if (!response.ok) {
                    if (response.status === 400) {
                        return response.json().then(data => {
                            throw new Error(data.error || '비밀번호 변경 실패');
                        });
                    }
                    throw new Error('서버 오류');
                }
                return response.json();
            })
            .then(data => {
                alert('비밀번호가 성공적으로 변경되었습니다.');
                closePasswordPopup();
            })
            .catch(error => {
                alert('비밀번호 변경 실패: ' + error.message);
            });
    }

    // 나의 등급 페이지 로드 함수
    function loadTierContent() {
        fetch('/api/user-post-count', {
            headers: {
                [header]: token
            },
            credentials: 'include'
        })
            .then(response => response.json())
            .then(data => {
                const tierSystem = new TierSystem(data.postCount);
                const currentTier = tierSystem.getCurrentTier();

                const tierImage = document.getElementById('tier-image');
                const tierDescription = document.getElementById('tier-description');
                const postCount = document.getElementById('post-count');

                if (tierImage && tierDescription && postCount) {
                    tierImage.src = currentTier.image;
                    tierDescription.textContent = currentTier.description;
                    postCount.textContent = data.postCount.toString();
                }
            })
            .catch(error => console.error('Error fetching user info:', error));
    }

    // 닉네임 업데이트 함수
    function updateNickname(e) {
        e.preventDefault();
        const newNickname = nicknameInput.value;

        fetch('/updateProfile', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                [header]: token
            },
            body: JSON.stringify({ nickName: newNickname }),
            credentials: 'include'
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error('닉네임 업데이트 실패');
                }
                return response.json(); // 서버가 JSON을 반환한다고 가정
            })
            .then(data => {
                nicknameDisplay.textContent = newNickname;
                toggleNicknameEditMode(false);
                alert('닉네임이 성공적으로 변경되었습니다.');
            })
            .catch(error => {
                alert('닉네임 업데이트에 실패했습니다: ' + error.message);
            });
    }

    // 생년월일 업데이트 함수
    function updateBirthdate(e) {
        e.preventDefault();
        const newBirthdate = birthdateInput.value;

        fetch('/updateProfile', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                [header]: token
            },
            body: JSON.stringify({ birthDate: newBirthdate }),
            credentials: 'include'
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error('생년월일 업데이트 실패');
                }
                return response.json(); // 서버가 JSON을 반환한다고 가정
            })
            .then(data => {
                birthdateDisplay.textContent = newBirthdate;
                toggleBirthdateEditMode(false);
                alert('생년월일이 성공적으로 변경되었습니다.');
            })
            .catch(error => {
                alert('생년월일 업데이트에 실패했습니다: ' + error.message);
            });
    }

    // 핸드폰 번호 업데이트 함수
    function updatePhoneNumber(e) {
        e.preventDefault();
        const newPhoneNumber = phoneNumberInput.value;

        fetch('/updateProfile', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                [header]: token
            },
            body: JSON.stringify({ phoneNumber: newPhoneNumber }),
            credentials: 'include'
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error('핸드폰 번호 업데이트 실패');
                }
                return response.json(); // 서버가 JSON을 반환한다고 가정
            })
            .then(data => {
                phoneNumberDisplay.textContent = newPhoneNumber;
                togglePhoneNumberEditMode(false);
                alert('핸드폰 번호가 성공적으로 변경되었습니다.');
            })
            .catch(error => {
                alert('핸드폰 번호 업데이트에 실패했습니다: ' + error.message);
            });
    }

    // 이벤트 리스너 추가
    profileBtn.addEventListener('click', () => {
        loadContent('profile');
        setActiveButton(profileBtn);
    });

    tierBtn.addEventListener('click', () => {
        loadContent('tier');
        setActiveButton(tierBtn);
        loadTierContent();
    });

    myPostsBtn.addEventListener('click', () => {
        loadContent('my-posts');
        setActiveButton(myPostsBtn);
    });

    editNicknameBtn.addEventListener('click', () => {
        toggleNicknameEditMode(true);
    });

    nicknameForm.addEventListener('submit', updateNickname);

    cancelNicknameBtn.addEventListener('click', () => {
        toggleNicknameEditMode(false);
    });

    editBirthdateBtn.addEventListener('click', () => {
        toggleBirthdateEditMode(true);
    });

    birthdateForm.addEventListener('submit', updateBirthdate);

    cancelBirthdateBtn.addEventListener('click', () => {
        toggleBirthdateEditMode(false);
    });

    editPhoneNumberBtn.addEventListener('click', () => {
        togglePhoneNumberEditMode(true);
    });

    phoneNumberForm.addEventListener('submit', updatePhoneNumber);

    cancelPhoneNumberBtn.addEventListener('click', () => {
        togglePhoneNumberEditMode(false);
    });

    changePasswordBtn.addEventListener('click', showPasswordPopup);

    closePopupBtn.addEventListener('click', closePasswordPopup);

    passwordForm.addEventListener('submit', changePassword);

    // 프로필 이미지 업로드
    fileInput.addEventListener('change', (event) => {
        const file = event.target.files[0];
        if (file) {
            const reader = new FileReader();
            reader.onload = function(e) {
                profilePic.src = e.target.result; // 미리보기
                changeProfileImage(file); // 서버로 업로드
            };
            reader.readAsDataURL(file);
        }
    });
});
