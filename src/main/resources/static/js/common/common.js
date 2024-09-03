document.addEventListener('DOMContentLoaded', function() {
  const header = document.querySelector('header');
  const scrollThreshold = 100; // 스크롤 임계값 (픽셀)

  window.addEventListener('scroll', function() {
    if (window.scrollY > scrollThreshold) {
      header.classList.add('scrolled');
    } else {
      header.classList.remove('scrolled');
    }
  });
});