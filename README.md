# TDD 로 만드는 설문조사(Polls) 웹앱 — Spring Boot 실습

테스트를 **먼저** 작성하고 그 테스트를 통과시키는 코드를 짜는 **TDD(Test Driven Development)** 를
Spring Boot 로 직접 해보는 실습 프로젝트다.

흐름은 강의와 같다:
**기능 테스트(Selenium) 개념을 잡는다 → JUnit 5 로 단위 테스트를 익힌다 → Spring MVC + Thymeleaf + JPA 로 설문조사 앱을 test-first 로 구현한다 → `./gradlew test` 로 전부 통과(green)시킨다.**

## 준비물
- **JDK 21** — `java -version` 으로 확인 (21이 아니면 아래 JAVA_HOME 안내 참고)
- 그 외 도구는 불필요. Gradle 은 동봉된 Wrapper(`./gradlew`)가 알아서 받아온다.

## 실행 순서

```bash
# 0) (시스템 기본 java 가 21이 아니라면) JDK 21 지정
export JAVA_HOME=$(/usr/libexec/java_home -v 21)   # macOS

# 1) 테스트 전체 실행 — 19개가 모두 통과(green)하는지 확인
./gradlew test

# 2) 서버 기동 (데모 데이터 포함) → http://localhost:8080/polls/
./gradlew bootRun --args='--spring.profiles.active=demo'

# 3) 정상 동작 자동 검증 (JDK21 / 테스트 19개 / 8080 기동·렌더링, 5개 체크)
./verify.sh
```

## 폴더 구조
```
dingco-tdd/
├─ build.gradle                         # Spring Boot 3.3.5 / Java 21 / Gradle-Groovy
├─ settings.gradle
├─ gradlew, gradlew.bat, gradle/        # Gradle Wrapper (별도 설치 불필요)
├─ src/main/java/com/dingcolabs/dingcotdd/
│  ├─ DingcotddApplication.java         # 애플리케이션 진입점
│  └─ polls/
│     ├─ Question.java                  # @Entity 질문 (wasPublishedRecently 포함)
│     ├─ Choice.java                    # @Entity 보기 (Question 과 N:1)
│     ├─ QuestionRepository.java        # Spring Data JPA Repository
│     ├─ ChoiceRepository.java
│     ├─ PollsController.java           # /polls/ , /polls/{id}/ , /polls/{id}/results/
│     └─ DataSeeder.java                # demo 프로파일용 샘플 데이터
├─ src/main/resources/
│  ├─ application.properties            # H2 in-memory + JPA + H2 콘솔
│  └─ templates/polls/                  # Thymeleaf 뷰
│     ├─ index.html  ├─ detail.html  └─ result.html
├─ src/test/java/com/dingcolabs/dingcotdd/
│  ├─ StringMethodsTest.java            # JUnit 5 기본 예제 (3)
│  └─ polls/
│     ├─ QuestionModelTest.java         # wasPublishedRecently 단위 테스트 (3)
│     ├─ QuestionIndexViewTests.java    # 목록 페이지 MockMvc 테스트 (6)
│     ├─ QuestionDetailViewTests.java   # 세부 페이지 MockMvc 테스트 (4)
│     └─ QuestionResultViewTests.java   # 결과 페이지 MockMvc 테스트 (3)
├─ verify.sh                            # 정상 동작 자동 검증 (5개 체크)
└─ screenshots/                         # 강의 교재에서 쓰는 실행 화면 캡처
```

## 기술 스택
- **Spring Boot 3.3.5** (Spring Web · Thymeleaf · Spring Data JPA)
- **Java 21**, **Gradle**(Groovy DSL)
- **H2** in-memory DB (별도 설치 불필요, 콘솔 `/h2-console`)
- 테스트: **JUnit 5** + `@SpringBootTest` + **MockMvc** (실제 브라우저 없이 HTTP 레이어 검증)
- 기능 테스트(Selenium)는 개념 설명용으로 교재에 등장하며, 자동 검증은 MockMvc 로 대체

## 검증 결과 (실제 실행 확인됨)
`./verify.sh` 실행 시 5개 체크 모두 통과:
- Java 21 사용 중 ✅
- `./gradlew test` → **BUILD SUCCESSFUL**, 테스트 **19개** 전부 통과 ✅
- `http://localhost:8080/polls/` → **200 OK** ✅
- 질문 목록(`<ul>`) 정상 렌더링 ✅

## JDK 21 이 아니라서 빌드가 안 될 때
시스템 기본 `java` 가 더 높은 버전(예: 25)이면 Gradle 8.x 가
`Unsupported class file major version ...` 로 실패한다. JDK 21 을 지정하면 해결된다.

```bash
export JAVA_HOME=$(/usr/libexec/java_home -v 21)   # macOS
# Windows: 시스템 환경변수 JAVA_HOME 을 JDK 21 경로로 설정
```
