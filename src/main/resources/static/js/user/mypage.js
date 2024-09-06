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
	const cancelNicknameBtn = document.getElementById('cancel-nickname-btn');
	const changePasswordBtn = document.getElementById('change-password-btn');
	const passwordPopup = document.getElementById('password-popup');
	const closePopupBtn = document.getElementById('close-popup');
	const passwordForm = document.getElementById('password-form');
	const popupOverlay = document.querySelector('.popup-overlay');


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

		//게시글 갖고오기
		fetch("/mypage/recommends")
			.then(response => response.json())
			.then(data => {
				console.log("list:", data);
				let str = "";
				data.forEach(function(dto) {
					str += `
				<li class="post-item">
					<span><a href="/recommends/${dto.id}" >${dto.title}</a></span>
					<p>${dto.createdAt.substring(0, 10)}</p>
				</li>
				`
				});
				const postsListContainer = document.querySelector('#posts-list-container');
				postsListContainer.innerHTML = str;
			})
			.catch(error => {
				alert('게시글 로드 오류!');
			});
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
	
	// 비밀번호 가시성 토글 기능
    const toggleButtons = document.querySelectorAll('.toggle-password'); // <--- 추가 코드
    toggleButtons.forEach(button => { // <--- 추가 코드
        button.addEventListener('click', function() { // <--- 추가 코드
            const targetId = this.getAttribute('data-target');
            const passwordInput = document.getElementById(targetId);
            const icon = this.querySelector('i');

            if (passwordInput.type === 'password') {
                passwordInput.type = 'text';
                icon.classList.remove('fa-eye');
                icon.classList.add('fa-eye-slash');
            } else {
                passwordInput.type = 'password';
                icon.classList.remove('fa-eye-slash');
                icon.classList.add('fa-eye');
            }
        });
    });

	// 등급 계산 처리
	function calculateTier(recommendCount) {
        if (recommendCount >= 0 && recommendCount <= 1) {
            return {
                name: 'CORN',
                description: '옥수수',
                image: '/img/tier/Asset 1.png'
            };
        } else if (recommendCount >= 2 && recommendCount <= 3) {
            return {
                name: 'POPCORN',
                description: '터진 옥수수',
                image: '/img/tier/Asset 2.png'
            };
        } else {
            return {
                name: 'FULLPOPCORN',
                description: '팝콘',
                image: '/img/tier/Asset 3.png'
            };
        }
    }

	// 나의 등급 페이지 불러오기
	function loadTierContent() {
        fetch('/api/user-recommend-count', {
            headers: {
                [header]: token
            },
            credentials: 'include'
        })
        .then(response => response.json())
        .then(data => {
            const currentTier = calculateTier(data.recommendCount);

            const tierImage = document.getElementById('tier-image');
            const tierDescription = document.querySelector('#tier-description b i');
            const recommendCount = document.getElementById('recommend-count');

            if (tierImage && tierDescription && recommendCount) {
                tierImage.src = currentTier.image;
                tierImage.alt = `${currentTier.name} 등급 이미지`;
                
                // 등급 설명 업데이트
                tierDescription.textContent = currentTier.description;
                
                // recommendCount 업데이트
                recommendCount.textContent = data.recommendCount.toString();
            }

            // 등급 정보를 로드한 후 tier 컨텐츠를 표시
            loadContent('tier');
            setActiveButton(tierBtn);
        })
        .catch(error => {
            console.error('Error fetching user info:', error);
            alert('등급 정보를 불러오는 데 실패했습니다.');
        });
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

	changePasswordBtn.addEventListener('click', showPasswordPopup);

	closePopupBtn.addEventListener('click', closePasswordPopup);

	passwordForm.addEventListener('submit', changePassword);

	// 프로필 이미지 업로드
	fileInput.addEventListener("change", fileuploadS3Temp);
	
	function fileuploadS3Temp(){
		//s3 temp 폴더 파일업로드해야함
		//FormData 객체를 사용하여 파일 데이터를 서버에 전송할수있다
		//const fileInput = document.getElementById('poster-file');
		const formData = new FormData();
		//console.log(posterFile.files[0]);
        formData.append('profile', fileInput.files[0]); // 파일 추가
		//console.log("---");
		//console.log(formData);
		//*
		//파일을 서버에 비동기 전송
		fetch("/mypage/profile/temp-upload",{
			method: "POST",
			headers: {
				[header]: token
			},
			body: formData
		})
		//fetch로부터 받은 응답(Response 객체)을 처리
		.then(response=>response.json())
		.then(data=>{
			console.log(data.url);
			//console.log(data.key);
			//console.log(data.orgName);
			
			//파일의 부모인 label태그의 백그라운드에 이미지 처리
			const fileLabel=fileInput.parentElement;
			fileLabel.style.backgroundImage=`url(${data.url})`;
							
			
		})
		.catch(error=>{
			alert("파일업로드 실패! 서버연결을 확인하시고 다시 시도 해주세요!");
		})
	}
	
});


