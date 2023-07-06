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

function showPassword() {
  var passwordInput = document.getElementById("password");
  if (passwordInput.type === "password") {
    passwordInput.type = "text";
  } else {
    passwordInput.type = "password";
  }
}

function showCheckPassword() {
  var passwordInput = document.getElementById("check-password");
  if (passwordInput.type === "password") {
    passwordInput.type = "text";
  } else {
    passwordInput.type = "password";
  }
}

function home() {
  window.location.href = "../templates/newspeed.html";
}

var password1 = document.getElementById("password");
var password2 = document.getElementById("check-password");
var comparePassword = document.getElementById("compare-password");

password1.addEventListener("input", compareInputs);
password2.addEventListener("input", compareInputs);

function compareInputs() {
  var val1 = password1.value;
  var val2 = password2.value;

  if (val1 === val2) {
    comparePassword.textContent = "비밀번호가 일치합니다.";
  } else {
    comparePassword.textContent = "비밀번호가 일치하지 않습니다.";
  }
}

$(document).ready(function () {
  $("#email-check-button").click(function () {
    var email = $("#email").val(); // 입력된 이메일 가져오기

    // AJAX를 통해 POST 요청 보내기
    $.ajax({
      url: "/api/user/signup/authentication", // API 엔드포인트 URL
      type: "POST",
      data: JSON.stringify({ email: email }), // 요청 데이터 설정
      contentType: "application/json", // 요청의 컨텐츠 타입 설정
      success: function () {
        alert("이메일 인증번호를 발급하였습니다! 인증번호를 확인해주세요!");
        console.log("이메일 인증번호 발급 성공");
      },
      error: function (xhr, status, error) {
        console.error("오류가 발생했습니다:", error);
      },
    });
  });

  $("#email-num-check-button").click(function () {
    var email = $("#email").val(); // 입력된 이메일 가져오기
    var num = $("#email-number").val(); // 입력된 번호 가져오기
    console.log(email);
    console.log(num);

    var data = {
      email: email,
      authKey: num
    };
    console.log(data);
    //AJAX를 통해 POST 요청 보내기
    $.ajax({
      url: "/api/user/signup/verification",
      type: "POST",
      data: JSON.stringify(data),
      contentType: "application/json",
      success: function (response) {
        console.log("인증번호 확인!");
        var statusCode = response.code;
        var message = response.data;

        $("#status-code").text("Status Code: " + statusCode);
        $("#message").text("Message: " + message);
      },
      error: function (response) {
        console.log("인증 실패");
        var statusCode = response.code;
        var msg = response.data;

        console.log(statusCode);
        console.log(msg);
      },
    });
  });
});
