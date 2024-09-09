document.addEventListener('DOMContentLoaded', function() {
   
	
   //요청을 위한 토큰 얻어오기 위함
   const csrfToken = document.querySelector('meta[name="_csrf"]').getAttribute('content');
   const csrfHeader = document.querySelector('meta[name="_csrf_header"]').getAttribute('content');
    
    console.log(csrfToken);
    console.log(csrfHeader);
     
    //html이 로드 되면 실행 될 요청
    const movieId = document.querySelector('.movieId').textContent;
    
    
    Promise.all([
		findUserComments(movieId),
    	findAllComments(movieId),
    	findAverageRating(movieId)

	]).then(() => {
		setTimeout(() => {
			initializeRating();
			writeComment();
			updateUserComments();
			deleteUserComments();
		}, 100);



	}).catch(error => {
        console.error('Error loading comments:', error);
    });
    
    
    
	 // 리뷰 작성 버튼 클릭 이벤트
	 
	function writeComment() {

		const commentBtn = document.querySelector('.commentBtn')
		


		if (commentBtn != null) {
			

			commentBtn.addEventListener('click', function() {
				// 사용자 입력 데이터 가져오기

				const rating = document.querySelector('input[name="rating"]:checked').value;
				const comments = document.querySelector('input[name="comment"]').value;

				// 서버로 전송할 데이터 객체 생성
				const data = { rating, movieId, comments };

				// 서버로 POST 요청 보내기
				fetch("/movies/detail/comment/usercomments", {
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


						// 입력 필드 초기화
						//document.querySelector('input[name="comment"]').value = '';
						//initStars(); // 별점 초기화
						
							
						 Promise.all([
							findUserComments(movieId),
					    	findAllComments(movieId),
					    	findAverageRating(movieId)
					
						]).then(() => {
							setTimeout(() => {
								initializeRating();
								writeComment();
								updateUserComments();
								deleteUserComments();
							}, 100);
					
					
					
						}).catch(error => {
					        console.error('Error loading comments:', error);
					    });

					})
					.catch(error => {
						console.error('Error:', error);
						alert('리뷰 작성 중 오류가 발생했습니다. 다시 시도해 주세요.');
					});
			});



		}



	}/*별점남기기끝*/
	
	
	//유저 코멘트 수정하기
 function updateUserComments(){
	
	const updateBtn= document.querySelector('.updateBtn');
	
	if(updateBtn != null){
		
		const updateWriteWrap= document.querySelector('.update-write-comment');
        const userRatingWrap= document.querySelector('.user-rating-wrap');
        const submitUpdateBtn= document.querySelector('.submitUpdateBtn');
        const cancleUpdateBtn= document.querySelector('.cancleUpdateBtn');
        const form = document.querySelector('.update-write-comment form');
		
		updateBtn.addEventListener('click',function(){
			
			updateWriteWrap.style.display ='block';
			userRatingWrap.style.display='none';
			//업데이트 취소버튼
			cancleUpdateBtn.addEventListener('click',function(){
				
				updateWriteWrap.style.display ='none';
				userRatingWrap.style.display='block';
				form.reset();
				
			})
			
			//수정 제출 버튼
			submitUpdateBtn.addEventListener('click',function(){
				
				// 사용자 입력 데이터 가져오기
                const movieId = document.querySelector('.movieId').textContent;
				const rating = document.querySelector('input[name="rating"]:checked').value;
				const comments = document.querySelector('input[name="comment"]').value;

				// 서버로 전송할 데이터 객체 생성
				const data = { rating, movieId, comments };

				// 서버로 put 요청 보내기
				fetch("/movies/detail/comment/usercomments", {
					method: 'PUT',
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


						// 입력 필드 초기화
						//document.querySelector('input[name="comment"]').value = '';
						//initStars(); // 별점 초기화
						
						 Promise.all([
							findUserComments(movieId),
					    	findAllComments(movieId),
					    	findAverageRating(movieId)
					    	
						]).then(() => {
							setTimeout(() => {
								initializeRating();
								writeComment();
								updateUserComments();
								deleteUserComments();
							}, 100);
					
					
					
						}).catch(error => {
					        console.error('Error loading comments:', error);
					    });

					})
					.catch(error => {
						console.error('Error:', error);
						alert('리뷰 작성 중 오류가 발생했습니다. 다시 시도해 주세요.');
					});
				
				
				
			})
			
		});
	}
	
	
 }
 
 //유저 코멘트 삭제하기
  function deleteUserComments(){
	
	const updateBtn= document.querySelector('.deleteBtn');

	  if (updateBtn != null) {
		
		updateBtn.addEventListener('click',function(){
			  Swal.fire({
			  title: '리뷰를 정말 삭제하시겠습니까?',
			  text: '다시 되돌릴 수 없습니다.',
			  icon: 'warning',

			  showCancelButton: true, // cancel버튼 보이기. 기본은 원래 없음
			  confirmButtonColor: '#DB242D', // confrim 버튼 색깔 지정
			  cancelButtonColor: '#040707', // cancel 버튼 색깔 지정
			  confirmButtonText: '삭제', // confirm 버튼 텍스트 지정
			  cancelButtonText: '취소', // cancel 버튼 텍스트 지정 

		  }).then(result => {
	
			  
			
			  if (result.isConfirmed) { // 만약 모달창에서 confirm 버튼을 눌렀다면
			  
			       const movieId = document.querySelector('.movieId').textContent;
			      console.log("리뷰삭제 시작중");
			       
			     fetch("/movies/detail/comment/usercomments", {
					method: 'DELETE',
					headers: {
						[csrfHeader]: csrfToken,
						'Content-Type': 'application/json'
					},
					body: JSON.stringify(movieId)
				})
					.then(response => {
						if (!response.ok) {
							throw new Error('Network response was not ok');
						}
						return response.text(); 
					})
					.then(html => {

                            console.log("리뷰삭제 성공함");
						// 입력 필드 초기화
						//document.querySelector('input[name="comment"]').value = '';
						//initStars(); // 별점 초기화
						
						 Promise.all([
							findUserComments(movieId),
					    	findAllComments(movieId),
					    	findAverageRating(movieId)
					    	
						]).then(() => {
							setTimeout(() => {
								initializeRating();
								writeComment();
								updateUserComments();
								deleteUserComments();
							}, 100);
					
					
					
						}).catch(error => {
					        console.error('Error loading comments:', error);
					    });
					    
					     Swal.fire('삭제가 완료되었습니다.', '언제든 다시 리뷰를 작성 할 수 있습니다.', 'success');

					})
					.catch(error => {
						console.error('Error:', error);
						alert('리뷰 작성 중 오류가 발생했습니다. 다시 시도해 주세요.');
					});
			       
			       
			  
			  
			     

				 
			  }
		  });

			
		})


	}
	
	
 }
  

//유저 코멘트 불러오기
	function findUserComments(movieId) {

		fetch(`/movies/detail/${movieId}/usercomments`, {
			method: 'GET',
			headers: {
				'Accept': 'text/html'
			}

		})
			.then(response => {
				if (!response.ok) {
					throw new Error('Network response was not ok');
				}
				return response.text();
			})
			.then(html => {
				document.querySelector('.user-comments').innerHTML = html;
			})
			.catch(error => {
				console.error('Error:', error);
				alert('유저 댓글을 불러오는 중 오류가 발생했습니다.');
			});



	}

	//전체 코멘트 불러오기 

	function findAllComments(movieId) {

		fetch(`/movies/detail/${movieId}/comments`, {
			method: 'GET',
			headers: {
				'Accept': 'text/html'
			}

		})
			.then(response => {
				if (!response.ok) {
					throw new Error('Network response was not ok');
				}
				return response.text();
			})
			.then(html => {
				document.querySelector('.comments-list').innerHTML = html;
			})
			.catch(error => {
				console.error('Error:', error);
				alert('전체 댓글을 불러오는 중 오류가 발생했습니다.');
			});

	}
	
	//평균 점수 불러오기 
	function findAverageRating(movieId) {
		

		fetch(`/movies/detail/${movieId}/average`, {
			method: 'GET',
			headers: {
				'Accept': 'text/html'
			}

		})
			.then(response => {
				if (!response.ok) {
					throw new Error('Network response was not ok');
				}
				return response.text();
			})
			.then(html => {
				document.querySelector('.stararea-wrap').innerHTML = html;
			})
			.catch(error => {
				console.error('Error:', error);
				alert('전체 댓글을 불러오는 중 오류가 발생했습니다.');
			});

	}











//별점 선택 함수
function initializeRating() {
	console.log('initializeRating function called');
	const rateWrap = document.querySelectorAll('.rating');
	 console.log('Rate Wrap elements:', rateWrap);
	 
	 if (rateWrap.length === 0) {
        console.error('Rating elements not found');
        // 요소가 없을 때 재시도 로직 추가
        setTimeout(initializeRating, 100); // 100ms 후에 다시 시도
        return;
    }
	
	if(rateWrap != null){
		
		const	label = document.querySelectorAll('.rating .rating__label');
	const	input = document.querySelectorAll('.rating .rating__input');
	const	labelLength = label.length;
	const	opacityHover = '0.5';

	console.log('Rate Wrap elements:', rateWrap);
	console.log('Label elements:', label);
	console.log('Input elements:', input);

	if (rateWrap.length === 0 || label.length === 0 || input.length === 0) {
		console.error('Rating elements not found');
		return;
	}


		//별점 등록 함수 
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




	}

}




	
   
    
	

});// 끝



