document.querySelector('.naver-btn').addEventListener('click', () => {
    window.location.href = '/oauth2/authorization/naver';
});

document.querySelector('.kakao-btn').addEventListener('click', () => {
    window.location.href = '/oauth2/authorization/kakao';
});

document.querySelector('.google-btn').addEventListener('click', () => {
    window.location.href = '/oauth2/authorization/google';
});