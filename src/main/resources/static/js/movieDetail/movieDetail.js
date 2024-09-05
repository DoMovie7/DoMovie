document.addEventListener('DOMContentLoaded', function() {
   //별점 기능 변수 
	const rateWrap = document.querySelectorAll('.rating'),
		label = document.querySelectorAll('.rating .rating__label'),
		input = document.querySelectorAll('.rating .rating__input'),
		labelLength = label.length,
		opacityHover = '0.5';
		
   //코멘트 작성 비동기 요청 변수
	const commentBtn = document.querySelector('.commentBtn')
	
   
   const csrfToken = document.querySelector('meta[name="_csrf"]').getAttribute('content');
    const csrfHeader = document.querySelector('meta[name="_csrf_header"]').getAttribute('content');
    
    console.log(csrfToken);
     console.log(csrfHeader);


   
	let stars = document.querySelectorAll('.rating .star-icon');

	checkedRate();

	rateWrap.forEach(wrap => {
		wrap.addEventListener('mouseenter', () => {
			stars = wrap.querySelectorAll('.star-icon');

			stars.forEach((starIcon, idx) => {
				starIcon.addEventListener('mouseenter', () => {
					initStars();
					filledRate(idx, labelLength);

					for (let i = 0; i < stars.length; i++) {
						if (stars[i].classList.contains('filled')) {
							stars[i].style.opacity = opacityHover;
						}
					}
				});

				starIcon.addEventListener('mouseleave', () => {
					starIcon.style.opacity = '1';
					checkedRate();
				});

				wrap.addEventListener('mouseleave', () => {
					starIcon.style.opacity = '1';
				});
			});
		});
	});

	function filledRate(index, length) {
		if (index <= length) {
			for (let i = 0; i <= index; i++) {
				stars[i].classList.add('filled');
			}
		}
	}

	function checkedRate() {
		let checkedRadio = document.querySelectorAll('.rating input[type="radio"]:checked');


		initStars();
		checkedRadio.forEach(radio => {
			let previousSiblings = prevAll(radio);

			for (let i = 0; i < previousSiblings.length; i++) {
				previousSiblings[i].querySelector('.star-icon').classList.add('filled');
			}

			radio.nextElementSibling.classList.add('filled');

			function prevAll() {
				let radioSiblings = [],
					prevSibling = radio.parentElement.previousElementSibling;

				while (prevSibling) {
					radioSiblings.push(prevSibling);
					prevSibling = prevSibling.previousElementSibling;
				}
				return radioSiblings;
			}
		});
	}

	function initStars() {
		for (let i = 0; i < stars.length; i++) {
			stars[i].classList.remove('filled');
		}
	}

	 // 리뷰 작성 버튼 클릭 이벤트
    commentBtn.addEventListener('click', function() {
        // 사용자 입력 데이터 가져오기
        const rating = document.querySelector('input[name="rating"]:checked').value;
        const movieId = document.querySelector('input[name="movieId"]').value;
        const comments = document.querySelector('input[name="comment"]').value;
        
        // 서버로 전송할 데이터 객체 생성
        const data = { rating, movieId, comments };
        
        // 서버로 POST 요청 보내기
        fetch("/movies/detail/comment/write", {
            method: 'POST',
            headers: {
                [csrfHeader]: csrfToken,
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(data)
        })
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.text(); // 서버에서 HTML 조각을 반환하므로 text()로 처리
        })
        .then(html => {
            // 반환된 HTML을 파싱
            const parser = new DOMParser();
            const doc = parser.parseFromString(html, 'text/html');
            
            // 사용자 리뷰 섹션 업데이트
            const userReviewSection = doc.querySelector('[th\\:fragment="userReviewSection"]');
            if (userReviewSection) {
                document.querySelector('.user-comments-wrap').outerHTML = userReviewSection.outerHTML;
            }
            
            // 전체 리뷰 목록 업데이트
            const reviewList = doc.querySelector('[th\\:fragment="reviewList"]');
            if (reviewList) {
                document.querySelector('.show-comment').outerHTML = reviewList.outerHTML;
            }

            // 입력 필드 초기화
            document.querySelector('input[name="comment"]').value = '';
            initStars(); // 별점 초기화
        })
        .catch(error => {
            console.error('Error:', error);
            alert('리뷰 작성 중 오류가 발생했습니다. 다시 시도해 주세요.');
        });
    });
    
    // 페이지네이션 처리
    document.body.addEventListener('click', function(e) {
        if (e.target.classList.contains('page-link')) {
            e.preventDefault();
            const page = e.target.getAttribute('data-page');
            loadReviews(page);
        }
    });

    function loadReviews(page) {
        const movieId = document.querySelector('input[name="movieId"]').value;
        fetch(`/movies/detail/${movieId}?page=${page}`)
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.text();
        })
        .then(html => {
            const parser = new DOMParser();
            const doc = parser.parseFromString(html, 'text/html');
            
            const newReviewList = doc.querySelector('.show-comment');
            if (newReviewList) {
                document.querySelector('.show-comment').outerHTML = newReviewList.outerHTML;
            }
        })
        .catch(error => {
            console.error('Error:', error);
            alert('리뷰 목록을 불러오는 중 오류가 발생했습니다.');
        });
    }

});// 끝