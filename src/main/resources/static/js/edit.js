document.addEventListener('DOMContentLoaded', function() {
    // Editor 인스턴스 생성
    const editor = new toastui.Editor({
        el: document.querySelector('#editor'),
        initialEditType: 'markdown',
        height: '300px',
        initialValue: document.querySelector('#content').value || '' // 서버에서 넘겨준 기존 내용
        //,previewStyle: 'vertical'                // 마크다운 미리보기 스타일을 수직 형식으로 설정합니다.
    });

    // Editor의 내용이 변경될 때마다 hidden input의 값을 업데이트
    editor.on('change', () => {
        document.querySelector('#content').value = editor.getMarkdown();
    });

    // 취소 버튼 클릭 시 상세 페이지로 이동
    document.querySelector('#cancelButton').addEventListener('click', function() {
        window.location.href = '/recommends'; // 상세 페이지로 이동
    });
});
