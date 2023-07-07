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

function addTag() {
  var tagInput = document.getElementById("tagInput");
  var tagContainer = document.getElementById("tagContainer");
  var tagsInput = document.getElementById("tagsInput");

  var tag = tagInput.value.trim();
  if (tag !== "") {
    var tagElement = document.createElement("span");
    tagElement.className = "tag";
    tagElement.textContent = tag;

    var closeButton = document.createElement("button");
    closeButton.className = "close";
    closeButton.textContent = "x";
    closeButton.addEventListener("click", function () {
      removeTag(tagElement, tag, tagsInput);
    });

    tagElement.appendChild(closeButton);
    tagContainer.appendChild(tagElement);
    tagInput.value = "";

    var currentTags = tagsInput.value.split(",");
    currentTags.push(tag);
    tagsInput.value = currentTags.join(",");
  }
}

function removeTag(tagElement, tag, tagsInput) {
  var tagContainer = tagElement.parentNode;
  tagContainer.removeChild(tagElement);

  var currentTags = tagsInput.value.split(",");
  var index = currentTags.indexOf(tag);
  if (index !== -1) {
    currentTags.splice(index, 1);
    tagsInput.value = currentTags.join(",");
  }
}

function myProfilePage() {
  window.location.href = "profile.html";
}

function home() {
  window.location.href = "../templates/newspeed.html";
}
