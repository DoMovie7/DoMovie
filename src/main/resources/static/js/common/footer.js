function openNewWindow() {
    var width = 800; // 원하는 너비
    var height = 600; // 원하는 높이
    var left = (screen.width - width) / 2;
    var top = (screen.height - height) / 2;

    var newWindow = window.open('/admin/faqs', '관리자 콘솔', 
        'width=' + width + 
        ',height=' + height + 
        ',left=' + left + 
        ',top=' + top + 
        ',resizable=yes,scrollbars=yes,status=yes');
}