document.addEventListener('DOMContentLoaded', function() {
	const profilePic = document.getElementById('profile-picture');
	const fileInput = document.getElementById('profile-upload');

	// 프로필 이미지를 클릭하면 파일 입력 창이 열리도록 설정
	profilePic.addEventListener('click', function() {
		fileInput.click();
	});

	fileInput.addEventListener('change', function(event) {
		const file = event.target.files[0];
		if (file) {
			const reader = new FileReader();
			reader.onload = function(e) {
				profilePic.src = e.target.result;
				// 여기서 서버로 이미지를 업로드하는 함수를 호출할 수 있습니다.
				uploadImageToServer(file);
			};
			reader.readAsDataURL(file);
		}
	});
});

function uploadImageToServer(file) {
	// FormData 객체 생성 및 파일 추가
	const formData = new FormData();
	formData.append('profileImage', file);

	// fetch API를 사용하여 서버로 POST 요청 보내기
	fetch('/api/user/profile-image', {
		method: 'POST',
		body: formData
	})
		.then(response => response.json())
		.then(data => {
			console.log('프로필 이미지가 성공적으로 업로드되었습니다:', data);
		})
		.catch(error => {
			console.error('프로필 이미지 업로드 중 오류 발생:', error);
		});
}