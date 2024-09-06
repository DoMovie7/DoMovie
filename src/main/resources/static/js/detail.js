document.addEventListener('DOMContentLoaded', function () {
    const editorContent = document.getElementById('editor-content').value;

    const editor = new toastui.Editor({
        el: document.querySelector('#editor-container'),
        initialValue: editorContent || '내용이 없습니다.',
        viewer: true,
        height: '400px'
    });
});
