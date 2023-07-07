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

function writeNewPost() {
  window.location.href = "/api/newPost";
}

document.getElementById("my-profile").addEventListener("click", function () {
  var jwt = localStorage.getItem("jwt");

  fetch("/api/user/profile-page", {
    method: "GET",
    headers: {
      Authorization: jwt,
    },
  }).then(function (response) {
    response.text().then(function (text) {
      document.querySelector("html").innerHTML = text;
    });
  });
});
