const token = document.querySelector('meta[name="_csrf"]').getAttribute('content');
const header = document.querySelector('meta[name="_csrf_header"]').getAttribute('content');

document.addEventListener('DOMContentLoaded', () => {
    // 검색 버튼 클릭 시 submitSearch 함수 호출
    document.getElementById('search-button').addEventListener('click', submitSearch);

    // 입력 필드에서 엔터 키를 눌렀을 때 submitSearch 함수 호출
    document.getElementById('keyword').addEventListener('keyup', function(event) {
        if (event.key === 'Enter') {
            submitSearch();
        }
    });
});
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




// 영화 검색 함수
document.addEventListener('DOMContentLoaded', function() {
    const searchForm = document.getElementById('search-form');
    
    searchForm.addEventListener('submit', function(event) {
        event.preventDefault(); // 기본 폼 제출 동작을 막음
        searchMovies(); // 영화 검색
    });

    // 페이지 로드 시 검색 트렌드를 가져옴
    fetchSearchTrends();
});

// 영화 검색 함수
function searchMovies() {
    const keyword = document.getElementById('keyword').value.trim();
    if (keyword) {
        fetch(`/movies/search?keyword=${encodeURIComponent(keyword)}`)
            .then(response => {
                if (!response.ok) {
                    throw new Error('Network response was not ok');
                }
                if (response.status === 204) {
                    return Promise.resolve(null);
                }
                return response.text();
            })
            .then(text => {
                if (text === null || text.trim() === '') {
                    return [];
                }
                return JSON.parse(text);
            })
            .then(data => {
                let movieList = document.getElementById('movie-list');
                movieList.innerHTML = '';

                if (data && data.length > 0) {
                    // 검색 결과가 있을 때 h2 태그를 표시
                    document.getElementById('search-results-title').style.display = 'block';
                    
                    data.forEach(movie => {
                        const movieItem = document.createElement('div');
                        movieItem.classList.add('movie-item');
                        movieItem.innerHTML = `
                            <a href="/movies/detail/${movie.DOCID}">
                                <img src="${movie.posterUrl || 'path/to/default/image.jpg'}" alt="${cleanTitle(movie.title)} 포스터">
                            </a>
                            <p>${cleanTitle(movie.title)}</p>
                            <p>개봉일: ${movie.dotDate || '정보 없음'}</p>
                            <p>국가: ${movie.nation || '정보 없음'}</p>
                            <p>장르: ${movie.genre || '정보 없음'}</p>
                            <p class="rating-badge">${movie.rating || '정보 없음'}</p>
                        `;
                        movieList.appendChild(movieItem);
                    });
                } else {
                    movieList.innerHTML = '<p>검색 결과가 없습니다.</p>';
                    // 검색 결과가 없을 때 h2 태그를 숨김
                    document.getElementById('search-results-title').style.display = 'none';
                }
            })
            .catch(error => {
                console.error('Error:', error);
                document.getElementById('movie-list').innerHTML = '<p>검색 중 오류가 발생했습니다.</p>';
                // 오류 발생 시에도 h2 태그를 숨김
                document.getElementById('search-results-title').style.display = 'none';
            });
    }
}

function cleanTitle(title) {
    // !HS와 !HE를 제거하거나, 필요한 경우 강조 표시로 변환합니다.
    return title.replace(/!HS/g, '<strong>').replace(/!HE/g, '</strong>');
}
function cleanTitle(title) {
    // !HS와 !HE를 제거하거나, 필요한 경우 강조 표시로 변환합니다.
    return title.replace(/!HS/g, '<strong>').replace(/!HE/g, '</strong>');
    
/*고급기능*/
// 사용자 검색 행동을 기록하는 함수
// HTML 문서가 완전히 로드된 후 실행
}

function submitSearch() {
    console.log("111111");
    const searchQuery = document.getElementById('keyword').value;

    if (!searchQuery) {
        alert("Please enter a search query!");
        return;
    }

    const userSearchBehavior = {
        searchQuery: searchQuery,
        searchTime: new Date().toISOString(),
        clickedResult: null,
        sessionDuration: 0
    };

    fetch('/api/user/search', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            [header]: token // CSRF 토큰을 헤더에 추가
        },
        body: JSON.stringify(userSearchBehavior)
    })
    .then(async response => {
        if (!response.ok) {
            throw new Error('Failed to record search behavior');
        }

        // 응답 본문이 있는지 확인 (204 No Content 등 처리)
        const text = await response.text();
        return text ? JSON.parse(text) : {};
    })
    .then(data => {
        // 서버로부터 받은 데이터가 유효한지 확인
        console.log('Received data:', data);
        //alert('Search behavior recorded successfully');
        fetchSearchTrends(); // 검색 트렌드 새로 고침
    })
    .catch(error => {
        console.error('Error:', error);
    });
}

// 검색 트렌드를 서버에서 가져와 화면에 표시하는 함수
function fetchSearchTrends() {
    fetch('/api/trends')
        .then(response => response.json())
        .then(trends => {
            const trendsList = document.getElementById('trendsList');
            trendsList.innerHTML = ''; // 기존 목록 초기화

            trends.forEach(trend => {
                const anchor = document.createElement('a');
                //anchor.href = "#"; // 트렌드 항목에 대한 링크가 필요하다면 수정
                const span = document.createElement('span');
                span.textContent = `${trend.keyword} (Searched ${trend.frequency} times)`;

                anchor.appendChild(span);
                trendsList.appendChild(anchor);
            });
        })
        .catch(error => {
            console.error('Error fetching search trends:', error);
        });
}



// movieInfo.js
// 클라이언트 측 코드 (JavaScript)
function fetchMovieInfo() {
    fetch('/movies/new')
        .then(response => response.text())
        .then(data => {
            if (data.error) {
                console.error('Error:', data.error);
                return;
            }
            const movieContainer = document.getElementById('movieContainer');
            movieContainer.innerHTML =data;
        })
        .catch(error => console.error('영화 정보를 가져오는 도중 오류가 발생했습니다:', error));
}

function fetchupcomingMovieInfo() {
    fetch('/movies/upcoming')
        .then(response => response.text())
        .then(data => {
            if (data.error) {
                console.error('Error:', data.error);
                return;
            }
            const movieContainer = document.getElementById('movieContainer3');
            movieContainer.innerHTML =data;
        })
        .catch(error => console.error('영화 정보를 가져오는 도중 오류가 발생했습니다:', error));
}

function fetchBoxOfficeInfo() {
    fetch('/movies/boxOffice')
        .then(response => response.text())
        .then(data => {
            if (data.error) {
                console.error('Error:', data.error);
                return;
            }
			const boxOfficeContainer = document.getElementById('boxOfficeContainer');
			boxOfficeContainer.innerHTML =data;
        })
        .catch(error => console.error('박스오피스 정보를 가져오는 도중 오류가 발생했습니다:', error));
}

function fetchHorrorMovieInfo() {
    fetch('/movies/horror')
        .then(response => response.text())
        .then(data => {
            if (data.error) {
                console.error('Error:', data.error);
                return;
            }
            const movieContainer2 = document.getElementById('movieContainer2');
            movieContainer2.innerHTML =data;
        })
        .catch(error => console.error('영화 정보를 가져오는 도중 오류가 발생했습니다:', error));
}
function fetchAnimationMovieInfo() {
    fetch('/movies/animation')
        .then(response => response.text())
        .then(data => {
            if (data.error) {
                console.error('Error:', data.error);
                return;
            }
            const movieContainer2 = document.getElementById('movieContainer4');
            movieContainer2.innerHTML =data;
        })
        .catch(error => console.error('영화 정보를 가져오는 도중 오류가 발생했습니다:', error));
}


    fetchMovieInfo();
    fetchBoxOfficeInfo();
    fetchHorrorMovieInfo();
	fetchupcomingMovieInfo();
	fetchAnimationMovieInfo();

