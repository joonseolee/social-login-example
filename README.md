# social-login-example

다양한 소셜로그인들을 예제로 일단 만들어서 나중에 써먹을려고함.  

* [x] Google
* [x] Naver
* [x] Kakao
* [ ] apple
* [x] github
* [x] facebook
* [ ] payco

## Google

구글을 맨처음 테스트해보았기때문에 다른 서비스에서도 동일하겠지만 구글 기준 설명하자면 예를들어 frontend, backend 서버를 따로 둔다고 가정할경우  
리다이렉트 주소명은 백엔드쪽으로 물어야한다. 그래야 oauth 를 처리하는 백엔드쪽으로 리다이렉트가 되기때문에 이 부분에서 삽질을 많이함..ㅠ 

## Github

github 는 이상하게 `email` 주소가 `null` 로 오는 이슈 발생.   
위에 이슈를 확인했는데 별도의 http api 를 호출해서 가져오는 방법밖에 없었다.  
[이슈](https://github.com/nextauthjs/next-auth/issues/374) 를 통해서 확인이 가능한데 간단하다.  
`UserRequest` 에 나온 `Token` 값을 가지고 조회하면 된다.  
```
https://api.github.com/user/emails
- header Bearer USER_TOKEN 
```

## Facebook 

## Payco

페이코는 이상하게 `url` 등록하는데 유효한 `url`을 입력해달라고 에러가 계속 나온다.  
그것을 찾아보기전까지는 보류해야할듯.

## Apple


***

## 출처

1. https://loosie.tistory.com/301?category=932704
