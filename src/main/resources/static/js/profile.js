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

// 초기에 숨겨야 할 요소들을 숨김 처리
var nameInput = document.getElementById("name");
var introduceTextarea = document.getElementById("introduce");
var profileModifyCancelButton = document.getElementById(
  "profile-modify-cancel"
);
var profilePictureModifyButton = document.getElementById("picAddButton");
var profileCompleteButton = document.getElementById("complete-button");
var profileModifyButton = document.getElementById("profile-modify");
var myPostListTitle = document.getElementsByClassName("my-post-list-title")[0];
var myPostList = document.getElementsByClassName("my-post-list")[0];

nameInput.style.display = "none";
introduceTextarea.style.display = "none";
profileModifyCancelButton.style.display = "none";
profilePictureModifyButton.style.display = "none";
profileCompleteButton.style.display = "none";

// 프로필 수정 버튼 요소 가져오기

// 프로필 수정 버튼 클릭 이벤트 처리
profileModifyButton.addEventListener("click", function () {
  // input 요소와 textarea 요소를 보이도록 스타일 변경
  nameInput.style.display = "block";
  introduceTextarea.style.display = "block";
  profileModifyCancelButton.style.display = "block";
  profilePictureModifyButton.style.display = "block";
  profileCompleteButton.style.display = "block";
  profileModifyButton.style.display = "none";
  myPostList.style.display = "none";
  myPostListTitle.style.display = "none";
});

profileModifyCancelButton.addEventListener("click", function () {
  nameInput.style.display = "none";
  introduceTextarea.style.display = "none";
  profileModifyCancelButton.style.display = "none";
  profilePictureModifyButton.style.display = "none";
  profileCompleteButton.style.display = "none";
  profileModifyButton.style.display = "block";
  myPostList.style.display = "block";
  myPostListTitle.style.display = "block";
});

profileCompleteButton.addEventListener("click", function () {
  location.reload();
});

function writeNewPost() {
  window.location.href = "new-post.html";
}

function home() {
  window.location.href = "../templates/newspeed.html";
}
