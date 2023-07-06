# Trillion "Exercise Blog Project"

## 기능

### 구현해야 할 필수 기능
#### [ 사용자 인증 기능 ]
< 회원가입 기능 >
- 새로운 사용자가 ID와 비밀번호의 형태로 서비스에 가입할 수 있어야 합니다.
- 이 때, 비밀번호는 안전하게 암호화되어 저장되어야 합니다!

< 로그인 및 로그아웃 기능 >
- 사용자는 자신의 계정으로 서비스에 로그인하고 로그아웃할 수 있어야 합니다.
  <br/>  <br/>

#### [ 프로필 관리 ]
< 프로필 수정 기능 >
- 이름, 한 줄 소개와 같은 기본적인 정보를 볼 수 있어야 하며 수정할 수 있어야 합니다.
- 비밀번호 수정 시에는 비밀번호를 한 번 더 입력받는 과정이 필요합니다.
  <br/>  <br/>

#### [ 게시물 CRUD 기능 ]
< 게시물 작성, 조회, 수정, 삭제 기능 >
- 게시물 조회를 제외한 나머지 기능들은 전부 인가(Authorization) 개념이 적용되어야 하며 이는 JWT와 같은 토큰으로 검증이 되어야 할 것입니다.
- 예컨대, 내가 작성한 글을 남이 삭제할 수는 없어야 하고 오로지 본인만 삭제할 수 있어야겠죠?

< 게시물 작성, 수정, 삭제 시 새로고침 기능 >
- 프론트엔드에서 게시물 작성, 수정 및 삭제를 할 때마다 조회 API를 다시 호출하여 자연스럽게 최신의 게시물 내용을 화면에 보여줄 수 있도록 해야 합니다!
  <br/>  <br/>

#### [ 뉴스 피드 기능 ]
< 뉴스 피드 페이지 >
- 사용자가 다른 사용자의 게시물을 한 눈에 볼 수 있는 뉴스 피드 페이지가 있어야 합니다.
  <br/>
  <br/>


### 추가 구현 기능
#### [ 댓글 CRUD 기능 ]
< 댓글 작성, 조회, 수정, 삭제 기능 >
- 사용자는 게시물에 댓글을 작성할 수 있고 본인의 댓글은 수정 및 삭제를 할 수 있어야 합니다.
- 또한, 게시물과 마찬가지로 댓글 조회를 제외한 나머지 기능들은 인가(Authorization)개념이 적용되어야 합니다.

< 댓글 작성, 수정, 삭제 시 새로고침 기능 >
- 프론트엔드에서 댓글 작성, 수정 및 삭제를 할 때마다 조회 API를 다시 호출하여 자연스럽게 최신의 댓글 목록을 화면에 보여줄 수 있도록 해야 합니다!
  <br/>  <br/>

#### [ 댓글 CRUD 기능 ]
< 댓글 작성, 조회, 수정, 삭제 기능 >
- 사용자는 게시물에 댓글을 작성할 수 있고 본인의 댓글은 수정 및 삭제를 할 수 있어야 합니다.
- 또한, 게시물과 마찬가지로 댓글 조회를 제외한 나머지 기능들은 인가(Authorization)개념이 적용되어야 합니다.

< 댓글 작성, 수정, 삭제 시 새로고침 기능 >
- 프론트엔드에서 댓글 작성, 수정 및 삭제를 할 때마다 조회 API를 다시 호출하여 자연스럽게 최신의 댓글 목록을 화면에 보여줄 수 있도록 해야 합니다!
  <br/>  <br/>

#### [ 좋아요 기능 ]
< 게시물 및 댓글 좋아요/좋아요 취소 기능 >
- 사용자가 게시물이나 댓글에 좋아요를 남기거나 취소할 수 있어야 합니다.
- 이 때, 본인이 작성한 게시물과 댓글에 좋아요는 남길 수 없도록 해봅니다!
  <br/>  <br/>

#### [ 프론트엔드 만들어보기 ]
< 백엔드에서 제공하는 API를 통해 서버와 통신하는 프론트엔드를 구현합니다. >
- 와이어프레임에 나온 명세를 최대한 구현해보면 금상첨화겠죠?
- 웹개발 종합반에서 배웠던 부트스트랩을 활용해봐도 좋아요~
  <br/>  <br/>

#### [ 이메일 가입 및 인증 기능 ]
< 이메일 가입 시 이메일 인증 기능을 포함하는 것이 좋습니다. >
  <br/>  
  <br/>

### 명예의 전당 - 슈퍼 개발자(?)로서의 초석을 다져봅시다!(?)
#### [ 소셜 로그인 기능 구현 ]
- https://developers.naver.com/docs/login/devguide/devguide.md
- https://developers.kakao.com/docs/latest/ko/kakaologin/common
<br/>를 참고하여 네이버 로그인, 카카오 로그인을 구현해보자.
 <br/>  <br/>

#### [ 프로필에 사진 업로드 기능 구현 ]
- 프로필 사진을 저장할 때는 반드시 AWS S3를 이용해주세요!
  <br/>  <br/>

#### [ 게시물에 멀티미디어 지원 기능 구현 ]
- 게시물 본문에 사진이나 영상 등의 미디어를 포함할 수 있다면 금상첨화겠죠?
- 또한, 게시물 수정시에도 첨부된 미디어가 수정될 수 있으면 좋습니다.
  <br/>  <br/>

#### [ 팔로우 기능 구현 ]
- 특정 사용자를 팔로우/언팔로우를 할 수 있으면 너무 좋습니다.
- 팔로우 기능이 구현되었다면 뉴스 피드에 팔로우하는 사용자의 게시물을 볼 수 있어야 하겠죠?
  <br/>  <br/>

#### [ HTTP를 HTTPS로 업그레이드 하기 ]
- HTTPS를 적용하여 보안이 강화된 웹 페이지를 제공해보도록 합니다!
  <br/>  <br/>
  <br/>
  <br/>

  
## ERD(Entity Relationship Diagram)
#### 첫 ERD
![1](https://github.com/JisooPyo/Trillion-NewSpeed-Project/assets/131599243/66631271-0185-4b7f-bae0-f5e51f172138)
<br/><br/>

#### 수정된 ERD_1(댓글 좋아요 Entity 추가)
![2](https://github.com/JisooPyo/Trillion-NewSpeed-Project/assets/131599243/4fac6885-3d56-4c70-9a9f-952533fb0d51)
<br/><br/>

#### 수정된 ERD_2(포스트 좋아요 Entity 추가)
![3](https://github.com/JisooPyo/Trillion-NewSpeed-Project/assets/131599243/60510c72-ff2d-45bb-8f43-a8adad3d19b1)
<br/><br/>
<br/>
<br/>


## 와이어프레임
#### 회원가입 화면
![01회원가입](https://github.com/JisooPyo/Trillion-NewSpeed-Project/assets/131599243/d79d523c-0646-4b1b-a191-1efbfd5a600a)

#### 로그인 화면
![02로그인](https://github.com/JisooPyo/Trillion-NewSpeed-Project/assets/131599243/9fcaada7-39e8-4346-ac50-4c317d1c1798)

#### 프로필 페이지(수정, 저장, 자기가 쓴 글 목록 조회 가능)
![03프로필페이지](https://github.com/JisooPyo/Trillion-NewSpeed-Project/assets/131599243/efe9af69-1c13-4068-9357-bbc9dc324ddf)

#### 뉴스피드 화면(홈 화면) - 전체 글 조회 가능
![04뉴스피드 홈화면](https://github.com/JisooPyo/Trillion-NewSpeed-Project/assets/131599243/f127b763-e094-45c9-b4bf-3b35027f1e9e)

#### 포스트 글 조회화면
![05포스트글 조회화면](https://github.com/JisooPyo/Trillion-NewSpeed-Project/assets/131599243/1ed72204-93f8-43ff-95af-73d2c637c476)

#### 포스트 글 작성화면
![06포스트글작성](https://github.com/JisooPyo/Trillion-NewSpeed-Project/assets/131599243/401c3492-4ca0-40e2-9b8e-1bfd2243a0e4)

#### 포스트 글 수정화면
![07포스트글수정](https://github.com/JisooPyo/Trillion-NewSpeed-Project/assets/131599243/1c2c35f2-03e5-4723-bcaa-e7f43d20c481)

<br/><br/>
<br/>
<br/>


## API 명세
### User
<img width="669" alt="api0" src="https://github.com/JisooPyo/Trillion-NewSpeed-Project/assets/131599243/c7c11d64-3a10-4939-90ea-0f9b1c654657">

<br>

### Post
<img width="750" alt="api1" src="https://github.com/JisooPyo/Trillion-NewSpeed-Project/assets/131599243/7ba407de-4a67-4744-bea9-36d613ac47b6">

<br>

### Comment
<img width="809" alt="api2" src="https://github.com/JisooPyo/Trillion-NewSpeed-Project/assets/131599243/59ed132e-1f02-4f85-bc64-a91ca73cc1ee">
<img width="809" alt="api3" src="https://github.com/JisooPyo/Trillion-NewSpeed-Project/assets/131599243/8216c219-99b9-4c90-a142-5325f156465d">

<br/><br/>
<br/>
<br/>


## 역할 분담
- 우진 : User, 회원가입/로그인
- 행복 : Post, 블로그 글(포스트)
- 해나 : Comment, 댓글
- 지수 : 풀 리퀘스트 승인 / 프론트 구상
- 지상 : 개인 과제 구현 후 추가 기능 구현 담당
