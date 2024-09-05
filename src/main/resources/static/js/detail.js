document.addEventListener('DOMContentLoaded', function() {
    // 페이지가 'detail-page' 클래스가 있는지 확인하여 JS 코드를 실행
    if (document.body.classList.contains('detail-page')) {
        
        // Toast UI Editor 초기화
        const editor = new toastui.Editor({
            el: document.querySelector('#editor-container'), // 에디터를 적용할 요소 (컨테이너)
            height: '300px',                        // 에디터 영역의 높이 값 (OOOpx || auto)
            initialEditType: 'markdown',            // 최초로 보여줄 에디터 타입 (markdown || wysiwyg)
            initialValue: '',                       // 초기값을 빈 문자열로 설정합니다.
            previewStyle: 'vertical'                // 마크다운 프리뷰 스타일 (탭 형식 또는 수직 형식)
        });

        // 서버로부터 전달된 컨텐츠가 있는 경우 에디터에 설정
        const content = document.querySelector('#contentData').textContent.trim();
        if (content) {
            editor.setMarkdown(content);
        }
    }
});
