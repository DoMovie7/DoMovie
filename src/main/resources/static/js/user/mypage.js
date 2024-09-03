document.addEventListener('DOMContentLoaded', function() {
	// DOM 요소 선택
	const profilePic = document.getElementById('profile-picture');
	const fileInput = document.getElementById('profile-upload');
	const profileBtn = document.getElementById('profile-btn');
	const tierBtn = document.getElementById('tier-btn');
	const myPostsBtn = document.getElementById('my-posts-btn');
	const profileContent = document.getElementById('profile-content');
	const tierContent = document.getElementById('tier-content');
	const myPostsContent = document.getElementById('my-posts-content');
	const form = document.getElementById('profile-form');
	const editBtn = document.getElementById('edit-btn');
	const cancelBtn = document.getElementById('cancel-btn');
	const saveBtn = document.getElementById('save-btn');

	// 비밀번호 변경 관련 DOM 요소 선택
	const changePasswordBtn = document.getElementById('change-password-btn');
	const passwordPopup = document.getElementById('password-popup');
	const closePopupBtn = document.getElementById('close-popup');
	const passwordForm = document.getElementById('password-form');
	const popupOverlay = document.querySelector('.popup-overlay');

	// csrf 토큰
	const token = document.querySelector('meta[name="_csrf"]').getAttribute('content');
	const header = document.querySelector('meta[name="_csrf_header"]').getAttribute('content');

	// 등급 시스템 클래스
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

	// 원래 프로필 이미지 src 저장
	const originalProfilePicSrc = profilePic.src;

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

	// 수정 모드 토글 함수
	function toggleEditMode(isEditing) {
		const nicknameDisplay = document.getElementById('nickname');
		const nicknameInput = document.getElementById('nickname-input');

		nicknameDisplay.style.display = isEditing ? 'none' : 'inline';
		nicknameInput.style.display = isEditing ? 'inline' : 'none';

		editBtn.style.display = isEditing ? 'none' : 'inline';
		saveBtn.style.display = isEditing ? 'inline' : 'none';
		cancelBtn.style.display = isEditing ? 'inline' : 'none';

		profilePic.style.cursor = isEditing ? 'pointer' : 'default';
		profilePic.title = isEditing ? '클릭하여 이미지 변경' : '';

		if (isEditing) {
			nicknameInput.value = nicknameDisplay.textContent;
			profilePic.addEventListener('click', triggerFileInput);
		} else {
			profilePic.removeEventListener('click', triggerFileInput);
		}
	}

	// 파일 입력 트리거 함수
	function triggerFileInput() {
		fileInput.click();
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

		sendPasswordChangeRequest(currentPassword, newPassword);
		alert('비밀번호가 성공적으로 변경되었습니다.');
		closePasswordPopup();
	}

	// 나의 등급 페이지 로드 함수
	function loadTierContent() {
		fetch('/api/user-post-count')
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

	// 프로필 업데이트 함수
	function updateProfile() {
		const newNickname = document.getElementById('nickname-input').value;
		const currentNickname = document.getElementById('nickname').textContent;
		const file = fileInput.files[0];

		const profileData = {};

		// 닉네임이 변경되었는지 확인
		if (newNickname !== currentNickname) {
			profileData.nickName = newNickname;
		}

		let updatePromise;

		if (file) {
			// 이미지 파일이 선택된 경우
			updatePromise = uploadImageToServer(file)
				.then(imageData => {
					profileData.profileImageUrl = imageData.url;
					profileData.profileImageBucketKey = imageData.bucketKey;
					profileData.profileImageOrgName = imageData.originalName;
					return sendProfileUpdateToServer(profileData);
				});
		} else if (Object.keys(profileData).length > 0) {
			// 이미지는 없지만 닉네임이 변경된 경우
			updatePromise = sendProfileUpdateToServer(profileData);
		} else {
			// 변경된 내용이 없는 경우
			alert("변경된 내용이 없습니다.");
			toggleEditMode(false);
			return;
		}

		updatePromise
			.then(() => {
				if (profileData.nickName) {
					document.getElementById('nickname').textContent = profileData.nickName;
				}
				if (profileData.profileImageUrl) {
					profilePic.src = profileData.profileImageUrl;
				}
				alert("회원 정보가 수정되었습니다.");
				toggleEditMode(false);
			})
			.catch(error => {
				console.error('프로필 업데이트 중 오류 발생:', error);
				alert("회원 정보 수정에 실패했습니다: " + error.message);
			});
	}

	// 파일 선택 시 이미지 미리보기
	fileInput.addEventListener('change', function(e) {
		const file = e.target.files[0];
		if (file) {
			const reader = new FileReader();
			reader.onload = function(e) {
				profilePic.src = e.target.result;
			}
			reader.readAsDataURL(file);
		}
	});

	// CSRF 토큰을 가져오는 함수
	function getCsrfToken() {
		const csrfToken = document.querySelector('meta[name="_csrf"]');
		const csrfHeader = document.querySelector('meta[name="_csrf_header"]');
		if (csrfToken && csrfHeader) {
			const header = csrfHeader.getAttribute('content');
			// 헤더 이름에서 특수 문자 제거
			const safeHeader = header.replace(/[^\w-]/g, '');
			return {
				header: safeHeader,
				token: csrfToken.getAttribute('content')
			};
		}
		return null;
	}

	// 서버로 이미지 업로드 함수
	function uploadImageToServer(file) {
		const formData = new FormData();
		formData.append('file', file);

		const csrfData = getCsrfToken();
		const headers = {};
		if (csrfData) {
			headers[csrfData.header] = csrfData.token;
		}

		return fetch('/uploadImage', {
			method: 'POST',
			headers: {
				[header]: token //이 부분만 추가하면 됨
			},
			body: formData,
			credentials: 'include'  // 쿠키를 포함시키기 위해 추가
		})
			.then(response => {
				if (!response.ok) {
					throw new Error('이미지 업로드 실패');
				}
				return response.json();
			});
	}

	// 서버로 프로필 업데이트 요청 보내는 함수
	function sendProfileUpdateToServer(profileData) {
		const headers = {
			'Content-Type': 'application/json'
		};

		const csrfData = getCsrfToken();
		if (csrfData && csrfData.header && csrfData.token) {
			headers[csrfData.header] = csrfData.token;
		}

		return fetch('/updateProfile', {
			method: 'PUT',
			headers: {
				[header]: token //이 부분만 추가하면 됨
			},
			body: JSON.stringify(profileData),
			credentials: 'include'
		})
			.then(response => {
				if (!response.ok) {
					return response.text().then(text => {
						throw new Error('프로필 업데이트 실패: ' + text);
					});
				}
				return response.json();
			})
			.catch(error => {
				console.error('프로필 업데이트 중 오류 발생:', error);
				throw error;  // 에러를 다시 throw하여 상위 catch 블록에서 처리할 수 있게 함
			});
	}

	// 서버로 비밀번호 변경 요청 함수 (구현 필요)
	function sendPasswordChangeRequest(currentPassword, newPassword) {
		console.log('비밀번호 변경 요청 함수 구현 필요:', currentPassword, newPassword);
		// TODO: 실제 서버로 비밀번호 변경 요청을 보내는 로직 구현
	}

	// 이벤트 리스너 설정
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

	editBtn.addEventListener('click', () => toggleEditMode(true));

	cancelBtn.addEventListener('click', () => {
		toggleEditMode(false);
		profilePic.src = originalProfilePicSrc;
		document.getElementById('nickname-input').value = document.getElementById('nickname').textContent;
	});

	// 수정완료 버튼 이벤트 리스너
	saveBtn.addEventListener('click', (e) => {
		e.preventDefault();
		updateProfile();
	});

	changePasswordBtn.addEventListener('click', showPasswordPopup);
	closePopupBtn.addEventListener('click', closePasswordPopup);
	passwordForm.addEventListener('submit', changePassword);
	popupOverlay.addEventListener('click', closePasswordPopup);

	// 초기 페이지 로드
	loadContent('profile');
	setActiveButton(profileBtn);
});