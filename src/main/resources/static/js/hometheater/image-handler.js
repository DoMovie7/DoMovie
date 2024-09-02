document.addEventListener('DOMContentLoaded', function() {
    const images = document.querySelectorAll('img');

    images.forEach(img => {
        img.onerror = function() {
            handleImageError(this);
        };

        // URL 인코딩 적용
        if (img.src.includes('/hometheater/')) {
            const parts = img.src.split('/hometheater/');
            if (parts.length > 1) {
                const encodedFilename = encodeURIComponent(parts[1]);
                img.src = `${parts[0]}/hometheater/${encodedFilename}`;
            }
        }
    });

    function handleImageError(img) {
        console.error('Image failed to load:', img.src);

        // 1. 대체 이미지 표시
        img.src = '/path/to/fallback-image.jpg';

        // 2. 이미지를 숨기고 텍스트로 대체
        // img.style.display = 'none';
        // img.insertAdjacentHTML('afterend', '<span>Image not available</span>');

        // 3. S3 URL 구조로 변경 시도
        // const filename = img.src.split('/').pop();
        // img.src = `https://your-bucket-name.s3.amazonaws.com/${filename}`;
    }
});