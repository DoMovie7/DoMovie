// 문서의 모든 콘텐츠가 로드된 후 실행되는 함수입니다.
document.addEventListener('DOMContentLoaded', function() {
    // Toast UI Editor를 초기화하고 설정합니다.
    const editor = new toastui.Editor({
        el: document.querySelector('#editor'), // 에디터를 적용할 HTML 요소를 선택합니다.
        height: '300px',                        // 에디터의 높이를 300px로 설정합니다.
        initialEditType: 'markdown',            // 에디터의 초기 모드를 마크다운 모드로 설정합니다.
        initialValue: '내용을 입력해 주세요.',     // 에디터에 초기 값으로 '내용을 입력해 주세요.'라는 텍스트를 설정합니다.
        previewStyle: 'vertical'                // 마크다운 미리보기 스타일을 수직 형식으로 설정합니다.
    });
    
    // 'submitButton' 버튼에 클릭 이벤트 리스너를 추가합니다.
    document.getElementById('submitButton').addEventListener('click', function(event) {
        // 기본 폼 제출 동작을 방지합니다.
        event.preventDefault();
        
        // 에디터의 내용을 마크다운 형식으로 가져옵니다.
        var editorContent = editor.getMarkdown(); 
        // 숨겨진 폼 필드('content')에 에디터의 내용을 설정합니다.
        document.getElementById('content').value = editorContent;
        // 폼을 제출합니다.
        document.getElementById('writeForm').submit();
    });

    // 'cancelButton' 버튼에 클릭 이벤트 리스너를 추가합니다.
    document.getElementById('cancelButton').addEventListener('click', function() {
        // '/recommends' URL로 이동하여 이전 페이지로 돌아갑니다.
        window.location.href = '/recommends'; // 이동할 페이지 URL로 변경 가능
    });
    
    // 폼 제출 시 추가 작업을 처리합니다.
    document.querySelector('#writeForm').addEventListener('submit', function(e) {
        // 에디터의 내용을 마크다운 형식으로 가져옵니다.
        const content = editor.getMarkdown();
        // 숨겨진 폼 필드('content')에 에디터의 내용을 설정합니다.
        document.querySelector('#content').value = content;
    });
    
    
    
    const posterFile=document.querySelector('#file');
    
    //파일이 선택되면 value 값이 변경된다.-change이벤트
	//posterFile.addEventListener("change", function(){});
	posterFile.addEventListener("change", fileuploadS3Temp);
	
	function fileuploadS3Temp(){
		//s3 temp 폴더 파일업로드해야함
		//FormData 객체를 사용하여 파일 데이터를 서버에 전송할수있다
		//const fileInput = document.getElementById('poster-file');
		const formData = new FormData();
		//console.log(posterFile.files[0]);
        formData.append('posterfile', posterFile.files[0]); // 파일 추가
		console.log("---");
		console.log(formData);
		
		var token = $("meta[name='_csrf']").attr("content");
		var header = $("meta[name='_csrf_header']").attr("content");
		
		//*
		//파일을 서버에 비동기 전송
		fetch("/recommends/temp-upload",{
			method: "POST",
			headers:{
				[header]:token
			},
			//csrf 적용시 POST인경우  headers에 토큰을 적용해야함, 파일전송시 Content-Type 지정하면 오류.
			//json 은 {key:value} 구조인데 key는 기본적으로 문자열처리됨 	
			//키를 변수처리하기위해 [header]: 대괄호([]) 안의 header는 객체의 키를 동적으로 설정하는 방식
			body: formData
		})
		//fetch로부터 받은 응답(Response 객체)을 처리
		.then(response=>response.json())
		.then(data=>{
			console.log(data.url);
			console.log(data.bucketKey);
			console.log(data.orgName);
			console.log(data.newName);
			//파일의 부모인 label태그의 백그라운드에 이미지 처리
			const fileLabel=posterFile.parentElement;
			fileLabel.style.backgroundImage=`url(${data.url})`;
			
			//key 와 orgName은 태그를 만들어서 추가
			let addTag=`
			<input type="hidden" name="bucketKey" value="${data.bucketKey}">
			<input type="hidden" name="orgName" value="${data.orgName}">
			<input type="hidden" name="newName" value="${data.newName}">
			`;
			//label태그의 자식요소로 addTag의 내용을 추가
			fileLabel.innerHTML += addTag;			
			
		})
		.catch(error=>{
			alert("파일업로드 실패! 서버연결을 확인하시고 다시 시도 해주세요!");
		})
		//*/
		
	}
    
});




