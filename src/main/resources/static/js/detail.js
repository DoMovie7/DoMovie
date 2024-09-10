document.addEventListener('DOMContentLoaded', function () {
    const editorContent = document.getElementById('editor-content').value;

    const editor = new toastui.Editor({
        el: document.querySelector('#editor-container'),
        initialValue: editorContent || '내용이 없습니다.',
        viewer: true,
        height: '400px'
    });

    // 숨겨진 부분을 표시하기 위한 추가 로직
    const hiddenElements = document.querySelectorAll('.hidden');
    hiddenElements.forEach(element => {
        element.style.display = 'block'; // 숨겨진 부분을 명시적으로 보이게 설정
    });

    // 수정 버튼 클릭 시 업데이트 페이지로 이동
    const editButton = document.getElementById('editButton');
    if (editButton) {
        editButton.addEventListener('click', function () {
            const recommendId = document.querySelector('input[name="recommendId"]').value;
            window.location.href = `/recommends/${recommendId}/update`; // 업데이트 페이지로 이동
        });
    }
});
