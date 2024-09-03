document.addEventListener('DOMContentLoaded', function() {
    // toastui.Editor 에디터를 초기화하고 설정합니다.
    const editor = new toastui.Editor({
        el: document.querySelector('#editor'), // 에디터를 적용할 요소 (컨테이너)
        height: '300px',                        // 에디터 영역의 높이 값 (OOOpx || auto)
        initialEditType: 'markdown',            // 최초로 보여줄 에디터 타입 (markdown || wysiwyg)
        initialValue: '내용을 입력해 주세요.',     // 내용의 초기 값으로, 반드시 마크다운 문자열 형태여야 함
        previewStyle: 'vertical'                // 마크다운 프리뷰 스타일 (탭 형식 또는 수직 형식)
    });
	
    // 폼 제출 처리를 담당합니다.
    document.getElementById('submitButton').addEventListener('click', function(event) {
        // 기본 폼 제출을 방지합니다.
        event.preventDefault();
        
        // 에디터의 내용을 가져와서 숨겨진 폼 필드에 설정합니다.
        var editorContent = editor.getMarkdown(); 
        document.getElementById('content').value = editorContent;
        // 폼을 제출합니다.
        document.getElementById('writeForm').submit();
    });

    // 취소 버튼 클릭 시, 이전 페이지로 이동합니다.
    document.getElementById('cancelButton').addEventListener('click', function() {
        window.location.href = '/recommends'; // 이동할 페이지 URL로 변경
    });
    document.querySelector('#writeForm').addEventListener('submit', function(e) {
        const content = editor.getMarkdown();
        document.querySelector('#content').value = content;
    });
});
