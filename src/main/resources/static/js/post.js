// 버튼 요소 가져오기
var topButton = document.getElementById("topButton");

// 스크롤 이벤트를 감지하여 버튼 표시/숨김 처리
window.onscroll = function () {
  if (document.body.scrollTop > 20 || document.documentElement.scrollTop > 20) {
    topButton.style.display = "block";
  } else {
    topButton.style.display = "none";
  }
};

// 버튼 클릭 시 페이지 상단으로 이동하는 함수 정의
function scrollToTop() {
  document.body.scrollTop = 0; // Safari
  document.documentElement.scrollTop = 0; // Chrome, Firefox, IE, Opera
}

// 버튼 클릭 이벤트 처리
topButton.addEventListener("click", scrollToTop);

///////////////////////////////////////////////////

function myProfilePage() {
  window.location.href = "profile.html";
}

function home() {
  window.location.href = "../templates/newspeed.html";
}

function showCommentInput(button) {
  var comment = button.closest(".comment"); // 부모 comment 요소 선택
  var commentModifyWrapper = comment.querySelector(".comment-modify-wrapper"); // comment 내에서 comment-modify-wrapper 요소 선택

  commentModifyWrapper.style.display = "block";
}

function hideCommentInput(button) {
  var commentModifyWrapper = button.closest(".comment-modify-wrapper");

  commentModifyWrapper.style.display = "none";
}

function reload() {
  location.reload();
}

function showPostModifyPage() {
  var postModifier = document.getElementsByClassName("post-modifier")[0];
  postModifier.style.display = "block";
  var contentWrapper = document.getElementsByClassName("content-wrapper")[0];
  contentWrapper.style.display = "none";
}

// 페이지가 로드되면 자동으로 실행되는 함수
window.addEventListener("DOMContentLoaded", function () {
  var postTitle = document.getElementById("section1-post-title");
  var postTitleModi = document.getElementById("post-title-modi");
  var postContent = document.getElementById("post-content");
  var postContentModi = document.getElementById("post-content-modi");
  postTitleModi.value = postTitle.textContent; // 포스트 제목을 입력란에 설정
  postContentModi.value = postContent.textContent; // 포스트 내용을 입력란에 설정
});
