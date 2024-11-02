# 학습 관리 시스템(Learning Management System)

## 진행 방법

* 학습 관리 시스템의 수강신청 요구사항을 파악한다.
* 요구사항에 대한 구현을 완료한 후 자신의 github 아이디에 해당하는 브랜치에 Pull Request(이하 PR)를 통해 코드 리뷰 요청을 한다.
* 코드 리뷰 피드백에 대한 개선 작업을 하고 다시 PUSH한다.
* 모든 피드백을 완료하면 다음 단계를 도전하고 앞의 과정을 반복한다.

## 온라인 코드 리뷰 과정

* [텍스트와 이미지로 살펴보는 온라인 코드 리뷰 과정](https://github.com/next-step/nextstep-docs/tree/master/codereview)

## 요구사항

### 1단계 - 레거시 코드 리팩터링

#### 질문 삭제 요구사항

1. [x] 질문 삭제시 삭제 상태(boolean) 변경 -> question을 deleted -> true
2. [x] 로그인 사용자와 질문자가 같은 경우 삭제 가능
4. [x] 질문자와 모든답변자가 같은 경우 삭제 가능
3. [x] 답변 없는 경우 삭제 가능
7. [x] 질문자 답변자가 다른경우 답변 삭제 불가능
5. [x] 질문 삭제 시 답변도 삭제 필요
6. [x] 답변 삭제 시 삭제 상태(boolean) 변경 -> answer를 deleted -> true
8. [x] 질문삭제, 답변삭제는 DeleteHistory 저장

#### 리팩터링 요구사항

1. [x] QnaService - deleteQuestion() 메서드의 비즈니스 로직을 도메인에 구현
2. [x] (1)번 내용 리팩토링 시 TDD 로 구현 (단위테스트 필요)
3. [x] 모든 리팩토링 후 기존의 QnaServiceTest의 모든 테스트는 통과해야 함

#### 그외 요구사항

1. [x] 객체의 상태 데이터를 꺼내지(get)말고 메시지 보내기
2. [x] 일급 콜렉션 사용하기 - Question의 List를 일급 콜렉션으로
3. [x] 3개 이상의 인스턴스 변수를 가진 클래스를 쓰지 않기 - 인스턴스 변수의 수를 줄이자
4. [x] 도메인 모델에 setter 메서드 추가하지 않기

### 2단계 - 수강신청(도메인 모델)

#### 수강 신청 기능 요구사항

1. [x] 과정(course)에 여러개의 강의(Session) 생성 가능
    - 각 강의가 가져야할 것
        - start, end date - 강의 시작일, 종료일 추가
        - type - 유료 강의, 무료 강의
        - status - 강의 상태(준비중, 모집중, 종료)
2. [x] 무료 강의는 최대 수강 인원 제한 없음
3. [x] 유료 강의는 최대 수강 인원 제한 초과 시 예외 발생
4. [x] 유료 강의를 신청할 시 수강생이 결제한 금액과 수강료가 일치해야 수강 신청 가능
5. [x] 강의 신청시 강의가 모집중이 아니라면 예외 발생
6. [x] 이미지 생성 가능
    - 이미지 1MB 초과 시 예외 발생
    - 이미지 너비 300px, 높이 200px 초과 시 예외 발생
    - 이미지 너비 높이 3:2 비율 아닐 시 예외 발생
    - 이미지 타입 gif, jpg, jpeg, png, svg 아닐 시 예외 발생

#### 프로그래밍 요구사항

- 도메인 모델은 TDD로 구현
    - Service 클래스는 단위 테스트가 없어도 된다
