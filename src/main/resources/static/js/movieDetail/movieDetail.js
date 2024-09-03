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

	//작성 버튼 동작 감지 비동기 요청
	commentBtn.addEventListener('click', function() {
       //클릭 했을 시점의 값을 가져옴
        const rating = document.querySelector('input[name="rating"]:checked').value;
        const movieId = document.querySelector('input[name="movieId"]').value;
        const comments = document.querySelector('input[name="comment"]').value;
        
        
       // 보낼 폼데이터 생성
		  const data = {
		            rating: rating,
		            movieId: movieId,
		            comments: comments
		        };
		
		//비동기 요청
		fetch('/movies/detail/write',{
			method: 'POST',
			headers: {
				[csrfHeader]: csrfToken,
				'content-Type': 'application/json'
			},                 
			body: JSON.stringify(data)
		})
		
		.then(data=>{
			
		})
		
		.catch(error =>{})
		
		.finally(data=>{});



		
		
	});
	
	


});// 끝