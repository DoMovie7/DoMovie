
// Toast UI Editor를 초기화하고 설정합니다.
const editor = new toastui.Editor({
    el: document.querySelector('#editor'), // 에디터를 적용할 HTML 요소를 선택합니다.
    height: '300px', // 에디터의 높이를 300px로 설정합니다.
    initialEditType: 'markdown', // 에디터의 초기 모드를 마크다운 모드로 설정합니다.
    initialValue: '내용을 입력해 주세요.', // 에디터에 초기 값으로 '내용을 입력해 주세요.'라는 텍스트를 설정합니다.
    previewStyle: 'vertical' // 마크다운 미리보기 스타일을 수직 형식으로 설정합니다.
});

// 'cancelButton' 버튼에 클릭 이벤트 리스너를 추가합니다.
document.getElementById('cancelButton').addEventListener('click', function () {
// '/recommends' URL로 이동하여 이전 페이지로 돌아갑니다.
    window.location.href = '/hometheater/list'; // 이동할 페이지 URL로 변경 가능
});
// 폼 제출 시 추가 작업을 처리합니다.
document.querySelector('#writeForm').addEventListener('submit', function (e) {
    // 에디터의 내용을 마크다운 형식으로 가져옵니다.
    const content = editor.getMarkdown();
    // 숨겨진 폼 필드('content')에 에디터의 내용을 설정합니다.
    document.querySelector('#content').value = content;
});

/****************************************************************************************/
/******S3 파일업로드 처리*******************************************************************/
/****************************************************************************************/
// 모든 'item-img' 클래스를 가진 요소를 선택합니다.

function fileuploadS3Temp(inputElement) {
    const filesLength= inputElement.files.length;
    console.log("filesLength:",filesLength);
    if(filesLength==0)return;
    //const inputElement = inputTag.target;
    const file = inputElement.files[0];
    const isDefault = inputElement.dataset.isdefault;
    //s3 temp 폴더 파일업로드해야함
    //FormData 객체를 사용하여 파일 데이터를 서버에 전송할수있다
    const formData = new FormData();
    formData.append('itemImage', file); // 파일 추가

    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");
    //파일을 서버에 비동기 전송
    fetch("/hometheater/temp-upload", {
        method: "POST",
        headers: {
            [header]: token
        }, //[header]: 대괄호([]) 안의 header는 객체의 키를 동적으로 설정하는 방식. 즉, header 변수의 값이 객체의 키로 사용

        //token: header 키에 대한 값입니다. 이 값은 token 변수의 값이 됩니다. 대괄호([])를 사용하지 않고 객체를 정의하면, 객체의 키가 문자열로 고정되어 있어 동적 키 설정이 불가능
        body: formData
    })
        //fetch로부터 받은 응답(Response 객체)을 처리
        .then(response => response.json())
        .then(data => {
            console.log(data.url);
            console.log(data.bucketKey);
            console.log(data.orgName);
            //파일의 부모인 label태그의 백그라운드에 이미지 처리
            const fileLabel = inputElement.parentElement;
            fileLabel.style.backgroundImage = `url(${data.url})`;
            //key 와 orgName은 태그를 만들어서 추가
            let addTag = `
            <input type="hidden" name="bucketKey" value="${data.bucketKey}">
            <input type="hidden" name="orgName" value="${data.orgName}">
            <input type="hidden" name="newName" value="${data.newName}">
            <input type="hidden" name="isDefault" value="${isDefault}">
            `;

            //label태그의 자식요소로 addTag의 내용을 추가
            const fileData=$(inputElement).siblings(".file-data");
            const fileDataLength=fileData.children().length;
            console.log("fileDataLength:",fileDataLength);
            if(fileDataLength==0)fileData.append(addTag);
            if(isDefault=="false")addImageInput(inputElement);
        })
        .catch(error => {
            //alert("파일업로드 실패! 서버연결을 확인하시고 다시 시도 해주세요!");
        })
}


function addImageInput(inputElement) {

    const label=$(inputElement).parent();
    const idx=label.index();
    const subImages=$(".sub-img");
    const lastIdx=subImages.length-1;

    //총3개까지만 || 앞에꺼를 수정하면 여기서 종료
    if(lastIdx >=2 || idx < lastIdx) return;
    // HTML 문자열 생성
    const newInputHTML = `
    <label class="posterLabel" style="width: 200px; height: 200px;">
        <span>+</span>
        <input type="file" onchange="fileuploadS3Temp(this)" class="item-img sub-img" data-isdefault="false" accept="image/*" style="display: none">
        <div class="file-data"></div>
    </label>
    `;
    // 새로운 요소를 DOM에 추가
    const subImagesContainer= $("#sub-images");
    subImagesContainer.append(newInputHTML);

}



