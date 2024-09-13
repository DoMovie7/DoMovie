# 3차 프로젝트 “DOMOVIE”

---

Open API를 활용한 영화 커뮤니티 웹 사이트 구현프로젝트입니다.
Spring Boot를 활용하여 "영화 커뮤니티" 웹사이트를 구현하였습니다.

## 📝프로젝트 소개

---

이 프로젝트는 Spring Boot를 활용하여 "영화 커뮤니티" 웹사이트를 구현한 것입니다. 이 웹사이트는 최신 영화 정보와 박스오피스 순위, 곧 개봉 예정작, 장르별 추천 등 다양한 기능을 통해 사용자들이 영화를 손쉽게 탐색할 수 있도록 설계되었습니다.

특히, 외부 Open API와의 효율적인 비동기 통신을 통해 사용자에게 실시간으로 업데이트되는 정보를 제공합니다. 이를 통해 대규모 데이터 요청에도 빠른 응답 속도를 유지하며, 최적화된 캐싱 전략을 적용해 서버 부담을 최소화했습니다.

사용자 편의를 위한 기능으로는 **영화 추천 게시판**을 통해 영화에 대한 의견을 나눌 수 있고, **챗봇** 기능을 활용하여 손쉽게 영화 정보를 검색할 수 있습니다. 또한, **소셜 로그인** 기능을 지원하여 사용자들이 간편하게 로그인하고 개인화된 서비스를 이용할 수 있도록 구현했습니다.

이 프로젝트는 사용자 경험을 극대화하고, 최신 기술을 적용한 최적의 백엔드 아키텍처로 구축되어, 안정성과 확장성을 동시에 만족하는 완성도 높은 웹사이트입니다.

## ⏱개발 기간

---

- 2024.08.28(수) ~ 2024.09.10(화)
- 프로젝트 컨셉 회의
- 아이디어 노트 작성 및 최종 결정
- 구현할 기능 정리
- 기능 설계
- 설계 발표
- 발표 평가

## 👩‍💻개발자 소개

---

- 박도현 : 팀장 / 로그인, 회원가입
- 유해리 : 부팀장 / 메인페이지
- 이한빛 : 홈시어터 게시판
- 박효재 : 영화추천 게시판
- 전송희 : 상세페이지
- 김도영 : 챗봇 / 채팅
- 윤민석 : 마이페이지

## 💻개발환경

---

| 프론트엔드 | 백엔드 | 협업툴 |
| --- | --- | --- |
| HTML | java 17 | git |
| CSS | Spring Boot 3.3.3 | github |
| JavaScript | Spring Data JPA 3.3.1 | notion |
| Thymeleaf 3.1 | MyBatis 4.0.0 | padlet |
| jQuery 3.7.1 | MariaDB |  |

## 🔧주요 기능

---

- 🔐로그인, 회원가입
    - 사용자 인증 및 관리
        - 다중 소 로그인 지
            - OAuth2를 통한 Google, Naver, Kakao 플랫폼 연동
            - 각 플랫폼의 사용자 정보와 시스템 연동
    - 계정 복구
        - 아이디 찾기 기능
            - 등록된 사용자 정보를 기반으로 아이디 목록 조회 가능
        - 비밀번호 재설정
            - Google SMTP API를 활용한 이메일 발송 시스템 구현
            - 안전한 비밀번호 재설정 프로세스 제공
    - 회원가입 프로세스
        - 이메일 중복 확인 기능
        - 사용자 정보에 대한 철저한 유효성 검사
        - 안전한 사용자 데이터 관리 시스템 구현
    - 보안 강화
        - Spring Security를 활용한 로그인 시스템 구현
        - 사용자 인증 및 권한 관리의 안전성 확보

      
- 🎥메인페이지
    - Open API를 활용하여 메인페이지에 영화 리스트를 출력
    - 필터링 및 정렬 로직을 추가해 사용자에게 유의미한 데이터를 빠르게 제공
    - 영화 검색창에서 키워드를 입력하여 영화 검색 가능
    - 입력된 키워드 기반으로 연관검색어 동적 업데이트

      
- 📺홈시어터 게시판
    - 게시판 CRUD 구현
    - 이미지 S3 업로드 (게시글이 삭제될 때, S3 이미지삭제)
    - 홈시어터 카테고리
    - 수정 , 삭제 (sec:authorize, th:if) 조건으로 해당 사용자만 가능하게 구현

      
- 📝영화추천 게시판
    - 게시판 CRUD 구현
    - 이미지 S3 업로드 (게시글이 삭제될 때, S3 이미지삭제)
    - 영화 장르 카테고리
    - 수정 , 삭제 (sec:authorize, th:if) 조건으로 해당 사용자만 가능하게 구현

      
- 🎬상세페이지
    - RestTemplate을 사용하여 KMDB API와 통신하여 상세 정보 출력
    - 전체 리뷰 페이지 네이션
    - 비동기 요청과 마이바티스를 통한 별점 리뷰 crud
    - crud시 전체리뷰와 영화 평균 평점 실시간 반영

      
- 💬챗봇 / 채팅
    - 자주 묻는 질문 / 1:1 문의 채팅 / AI 영화 추천
    - WebSocket 을 활용한 실시간 챗봇 & 채팅 구현
    - Stomp를 활용한 챗봇 & 채팅 고도화
    - SockJS를 활용한 브라우저 호환성 향상
 
      
- 💁마이페이지
    - fetch api를 사용하여 이미지와 닉네임 비동기로 변경 가능
    - 비밀번호 변경시 현재 비밀번호, 새 비밀번호 및 새 비밀번호 확인을 적용
    - 나의 등급 탭에서 로그인 된 사용자가 등록한 게시글 수에 따른 등급제 부여
    - 내가 쓴 글 탭에서 사용자가 등록했던 게시글 목록 확인, 해당 게시글 클릭 시 수정 및 삭제 가능
