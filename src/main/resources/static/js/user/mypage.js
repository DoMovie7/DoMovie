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
		const passwordDisplay = document.getElementById('password');
		const passwordInput = document.getElementById('password-input');

		[nicknameDisplay, nicknameInput, passwordDisplay, passwordInput].forEach(el => {
			el.style.display = isEditing ? (el.tagName === 'INPUT' ? 'inline' : 'none')
				: (el.tagName === 'INPUT' ? 'none' : 'inline');
		});

		editBtn.style.display = isEditing ? 'none' : 'inline';
		saveBtn.style.display = isEditing ? 'inline' : 'none';
		cancelBtn.style.display = isEditing ? 'inline' : 'none';

		if (isEditing) {
			nicknameInput.value = nicknameDisplay.textContent;
			passwordInput.value = '';
		}
	}

	// 이벤트 리스너 설정
	profileBtn.addEventListener('click', () => {
		loadContent('profile');
		setActiveButton(profileBtn);
	});

	tierBtn.addEventListener('click', () => {
		loadContent('tier');
		setActiveButton(tierBtn);
	});

	myPostsBtn.addEventListener('click', () => {
		loadContent('my-posts');
		setActiveButton(myPostsBtn);
	});

	editBtn.addEventListener('click', () => toggleEditMode(true));

	cancelBtn.addEventListener('click', () => {
		toggleEditMode(false);
		form.reset();
	});

	form.addEventListener('submit', (e) => {
		e.preventDefault();
		updateProfile();
	});

	// 기타 함수들 (updateProfile, uploadImageToServer 등) 유지
});