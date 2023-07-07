$(document).ready(function () {
  // $("#login").show();
  // $("#find-id-screen").hide();
  // $("#find-pw-screen").hide();

  // id 찾기 링크 눌렀을 때
  $(".forgot-id").click(function (event) {
    event.preventDefault(); // 기본 동작인 페이지 이동을 막기 위해 호출합니다.

    $("#login").hide();
    $("#find-id-screen").show();
    $("#find-pw-screen").hide();
  });

  // 비밀번호 찾기 링크 눌렀을 때
  $(".forgot-password").click(function (event) {
    event.preventDefault(); // 기본 동작인 페이지 이동을 막기 위해 호출합니다.

    $("#login").hide();
    $("#find-id-screen").hide();
    $("#find-pw-screen").show();
  });

  // 로그인 링크 눌렀을 때
  $(".login").click(function (event) {
    event.preventDefault(); // 기본 동작인 페이지 이동을 막기 위해 호출합니다.

    $("#login").show();
    $("#find-id-screen").hide();
    $("#find-pw-screen").hide();
  });

  ////////////////////////////////////////////

  // id 찾기 버튼 눌렀을 때
  $("#find-id-btn").click(function () {
    var username = $("#findid-username").val();
    var email = $("#findid-email").val();
    var data = {
      username: username,
      email: email,
    };
    console.log(data);

    $.ajax({
      url: "/api/user/findid",
      type: "POST",
      data: JSON.stringify(data), // 요청 데이터 설정
      contentType: "application/json", // 요청의 컨텐츠 타입 설정
      success: function (response) {
        var userId = response.userId;
        alert("ID는 " + userId + "입니다.");
        console.log("ID찾기 성공!");
      },
      error: function (xhr, status, error) {
        if (xhr.responseJSON) {
          var errorMessage = xhr.responseJSON.msg;
          var statusCode = xhr.responseJSON.statusCode;
          // 예외 처리 메시지와 상태 코드를 활용
          alert(
            "Error status code: " +
              statusCode +
              "\n" +
              "Error message: " +
              errorMessage
          );
        }
      },
    });
  });

  // 비밀번호 찾기 버튼 눌렀을 때
  $("#find-pw-btn").click(function () {
    var userId = $("#findpw-userid").val();
    var username = $("#findpw-username").val();
    var email = $("#findpw-email").val();
    var data = {
      userId: userId,
      username: username,
      email: email,
    };
    console.log(data);

    $.ajax({
      url: "/api/user/findpw",
      type: "POST",
      data: JSON.stringify(data), // 요청 데이터 설정
      contentType: "application/json", // 요청의 컨텐츠 타입 설정
      success: function (response) {
        console.log("PW찾기 성공!");
        var responseMessage = response.msg;
        var responseCode = response.statusCode;
        alert(responseCode + " : " + responseMessage);
      },
      error: function (xhr, status, error) {
        console.log("PW 찾기 실패!");
        if (xhr.responseJSON) {
          var errorMessage = xhr.responseJSON.msg;
          var statusCode = xhr.responseJSON.statusCode;
          // 예외 처리 메시지와 상태 코드를 활용
          alert(
            "Error status code: " +
              statusCode +
              "\n" +
              "Error message: " +
              errorMessage
          );
        }
      },
    });
  });

  // 로그인 버튼 눌렀을 때
  $("#login-btn").click(function () {
    var userId = $("#login-userid").val();
    var password = $("#login-password").val();
    var data = {
      userId: userId,
      password: password,
    };

    $.ajax({
      url: "/api/user/login",
      type: "POST",
      data: JSON.stringify(data),
      contentType: "application/json",
      success: function (response, textStatus, xhr) {
        console.log("로그인 성공!");
        var token = xhr.getResponseHeader("Authorization");
        if (token) {
          // JWT 저장
          localStorage.setItem("jwt", token);
          alert("로그인에 성공했습니다.");
          window.location.href = "/";
        }
      },
      error: function (xhr, status, error) {
        console.log("로그인 실패!");
        if (xhr.responseJSON) {
          var errorMessage = xhr.responseJSON.msg;
          var statusCode = xhr.responseJSON.statusCode;
          alert(
            "Error status code: " +
              statusCode +
              "\n" +
              "Error message: " +
              errorMessage
          );
        }
      },
    });
  });
});
