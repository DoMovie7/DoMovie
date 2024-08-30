//문서의 내용이 모두 로드되면 할일
document.addEventListener('DOMContentLoaded', function() {

	var slideWrap = document.querySelector('.indexslide-container');
	var slideContainer = document.querySelector('.indexslide-wrap');
	var slide = document.querySelectorAll('.indexslide');
	var navPrev = document.getElementById('prev');
	var navNext = document.getElementById('next');
	var currnetIndex = 0;
	var slidHeight = 0;
	var slideCount =slide.length;

	//슬라이드의 높이를 확인하여 부모의 높이로 지정하기
	
	for(var i=0;i<slideCount;i++){
		
		if(slidHeight<slide[i].offsetHeight){
			
			slidHeight =slide[i].offsetHeight;
		}
		
	}
	
	slideWrap.style.height =slidHeight+'px';
	slideContainer.style.height =slidHeight+'px';
	
	
	//슬라이드 옆으로 나열하기
	for(var a=0;a<slideCount; a++){
		slide[a].style.left = a * 100 +'%';
	}
	
	//슬라이드 이동 함수
	function goToslide(idx){
		slideContainer.style.left = -100 * idx + '%';
		slideContainer.classList.add('animated')
		
		currnetIndex=idx;
		
	}
	
	
	
	//버튼을 클릭하면 슬라이드 이동시키기
	/*
	navPrev.addEventListener('click',function(){
		goToslide(currnetIndex -1);
		console.log(currnetIndex);
	
	});
	
	
	navNext.addEventListener('click',function(){
		goToslide(currnetIndex +1);
		console.log(currnetIndex);
		
	});
	*/
	//마지막 슬라이드 후  next를 클릭하면 첫번째 슬라이드 보여주기
	navPrev.addEventListener('click',function(){
		if(currnetIndex == 0){
			goToslide(slideCount-1);
		}else{
			goToslide(currnetIndex -1);
		}
	});
	
	navNext.addEventListener('click',function(){
		if(currnetIndex == slideCount -1){
			goToslide(0);
		}else{
			goToslide(currnetIndex+1);
		}
	});




});

//변수지정

//api
/*function fetchAllMovies() {
    fetch('/movies/all')
        .then(response => response.json())
        .then(data => {
            const movieList = document.getElementById('movieList');
            movieList.innerHTML = '';

            data.movieListResult.movieList.forEach(movie => {
                movieList.innerHTML += `<p>${movie.movieNm} (${movie.prdtYear}) - ${movie.nationAlt}</p>`;
            });
        })
        .catch(error => console.error('Error fetching movies:', error));
}
*/

// movieInfo.js

// 영화 정보를 가져오는 함수
function fetchMovieInfo() {
    fetch('/movies/all') // 서버 API 엔드포인트에 요청
        .then(response => response.json()) // JSON 형태로 변환
        .then(data => renderMovieItems(data.movieListResult.movieList)) // 데이터를 렌더링 함수에 전달
        .catch(error => console.error('영화 정보를 가져오는 도중 오류가 발생했습니다:', error));
}

// 박스오피스 정보를 가져오는 함수
function fetchBoxOfficeInfo() {
    fetch('/movies/boxOffice') // 서버 API 엔드포인트에 요청
        .then(response => response.json()) // JSON 형태로 변환
        .then(data => renderBoxOfficeItems(data.boxOfficeResult.dailyBoxOfficeList)) // 데이터를 박스오피스 렌더링 함수에 전달
        .catch(error => console.error('박스오피스 정보를 가져오는 도중 오류가 발생했습니다:', error));
}

// 영화 항목을 렌더링하는 함수
function renderMovieItems(movieList) {
    const movieContainer = document.getElementById('movieContainer'); // 영화 아이템을 넣을 컨테이너 요소 선택
    movieContainer.innerHTML = ''; // 기존 내용을 지워서 초기화

    movieList.forEach(movie => {
        // 영화 항목을 HTML로 구성
        const movieItemHTML = `
            <div class="movie-item">
                <img src="${movie.image}" alt="${movie.movieNm}">
                <ul class="movie-info">
                    <li><span>${movie.movieNm}</span></li>
                    <li><span>${movie.prdtYear}</span></li>
                    <li><span>${movie.nationAlt}</span></li>
                </ul>
            </div>
        `;
        
        // 생성한 HTML을 컨테이너에 추가
        movieContainer.innerHTML += movieItemHTML;
    });
}

// 박스오피스 항목을 렌더링하는 함수
function renderBoxOfficeItems(boxOfficeList) {
    const boxOfficeContainer = document.getElementById('boxOfficeContainer'); // 박스오피스 아이템을 넣을 컨테이너 요소 선택
    boxOfficeContainer.innerHTML = ''; // 기존 내용을 지워서 초기화

    boxOfficeList.forEach(movie => {
        // 박스오피스 항목을 HTML로 구성
        const boxOfficeItemHTML = `
            <div class="movie-item animate__animated animate__flipInY">
                <h4>${movie.rank}. ${movie.movieNm}</h4>
                <ul class="box-office-info">
                    <li><span>개봉일: ${movie.openDt}</span></li>
                    <li><span>일일 관객수: ${movie.audiCnt}</span></li>
                    <li><span>누적 관객수: ${movie.audiAcc}</span></li>
                </ul>
            </div>
        `;
        
        // 생성한 HTML을 컨테이너에 추가
        boxOfficeContainer.innerHTML += boxOfficeItemHTML;
    });
}

// 페이지가 로드되면 영화 정보와 박스오피스 정보를 가져오는 함수 호출
document.addEventListener('DOMContentLoaded', () => {
    fetchMovieInfo();
    fetchBoxOfficeInfo();
});
