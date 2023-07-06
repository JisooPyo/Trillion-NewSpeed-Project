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

///////////////////////////////////////////////////////////////////

function loginPage() {
  window.location.href = "/api/user/login-page";
}

function signupPage() {
  window.location.href = "/api/user/signup";
}

function myProfilePage() {
  window.location.href = "profile.html";
}

function writeNewPost() {
  window.location.href = "/api/newPost";
}

/*
// 유저 로그인 상태에 따라 스타일 변경

var userLoggedIn = true; // 유저가 로그인한 상태라고 가정

var myProfileButton = document.getElementById("my-profile");
var writeNewPostButton = document.getElementById("write-new-post");
var loginButton = document.getElementsByClassName("login-button")[0];
var signupButton = document.getElementsByClassName("signup-button")[0];

if (userLoggedIn) {
  myProfileButton.style.display = "block"; // 보이도록 스타일 변경
  writeNewPostButton.style.display = "block";
  loginButton.style.display = "none";
  signupButton.style.display = "none";
} else {
  myProfileButton.style.display = "none"; // 숨기도록 스타일 변경
  writeNewPostButton.style.display = "none";
  loginButton.style.display = "block";
  signupButton.style.display = "block";
}
*/
