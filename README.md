# 왜 Mysql 인가 ?
## 1. 여전히 사용량이 높은 RDB

DB 엔진 랭킹 사이트 : https://db-engines.com/en/ranking

위사이트를 보면 1~4 위는 여전히 RDB 인것을 확인할 수 있다<br>
하지만 RDB 뿐 아니라 다른 종류의 데이터베이스도 또한 알야한다<br>

Mysql 은 RDB 중에서 가장 인기가 많은 오픈소스 관계형 데이터 베이스이다<br>
뿐만 아니라 국내외 대기업 IT 회사에서 가장 많이 사용된다<br>

공식 홈페이지 :<br>
http://github.com/mysql/mysql-server<br>
https://dev.mysql.com/doc/internals/en/guided-tour.html<br>
https://dev.mysql.com/doc/dev/mysql-server/latest/<br>

## 2. Mysql 아키텍처

![Mysql 아키텍처](README_IMG/mysql_architecture.png)

서버로부터 요청은 Mysql 엔진, 스토리지 엔진, 운영체제를 통해 디스크에 접근하여 데이터를 서버에 전달하게 된다<br>
Mysql 엔진은 사람으로 치면 두뇌, 스토리지 엔진은 동작을 수행하는 팔과 다리라고 생각하면 된다<br>
Mysql 엔진은 접속을 처리하는 커넥션 핸들러. 쿼리파서, 전처리기, 옵티마이저, 쿼리실행기 등의 구성요소가 존재한다<br>

### MYSQL 엔진
- 쿼리파서
  - SQL 을 파싱하여 Syntax Tree 를 만듬
  - 이과정에서 SQL 문법검사가 이루어짐
- 전처리기
  - 쿼리파서에서 만든 Tree 를 바탕으로 전처리 시작
  - 테이블이나 컬럼 존재여부, 접근권한등 Semantic 오류 검사
  - 쿼리파서, 전처리기는 컴파일 과정과 매우 유사하지만 SQL 은 프로그래밍처럼 컴파일 타임에서 검사할수 없어 매번 구문평가를 진행한다
- 옵티마이저
  - 쿼리를 처리하기위한 여러가지 방법을 만들고, 각 방법들의 비용정보와 테이블의 통계정보를 이용하여 비용을 산정
  - 테이블 순서, 불필요한 조건 제거, 통계정보를 바탕으로 전략을 결정한다 (**실행계획 수립**)
  - 옵티마이저가 선택하는 전략에따라 성능이 많이 달라진다 EXPLAIN 을 통해 옵티마이저가 어떤 결정을 했는지 개발자가 확인할 수 있다
- 쿼리실행기
  - 옵티마이저가 결정한 실행계획을 스토리지 엔진에 Handler API 를 통해 전달한다

> 쿼리캐시

쿼리캐시란 SQL 의 실행결과를 메모리에 캐시하고, 동일 SQL 쿼리가 실행되면 테이블을 읽지 않고 즉시 결과를 반환하기 때문에 빠른 성능을 보였다<br>
Mysql 5 버전까지는 쿼리캐시 기능을 제공하였지만 Mysql 8 버전에서는 해당 기능이 삭제되었다<br>

이유는 테이블 데이터가 변경되면 캐시되어있는 데이터도 함꼐 변경되어야하는데 캐시, 스토리지 상의 동기화시 많은 이슈가 발생하였기 때문이다<br>
<br>
Oracle 에 쿼리캐시와 비슷한 개념이 존재한다
- 소프트 파싱
  - SQL 실행계획을 캐시에서 찾아 옵티마이저 과정을 생략하고 실행단계로 넘어감
- 하드 파싱
  - 옵티마이저 과정을 생략하지않고 실행단계로 넘어감

Mysql 은 실행결과를 캐싱하는 쿼리캐싱, Oracle 은 실행계획을 캐싱하는 소프트 파싱을 제공한다
두 RDB 의 캐시 전략은 범위가 다르다는 점이 있다

하지만 캐시도입은 만료정책을 고려해야한다

쿼리캐시는 소프트캐시에 비해 조회성능은 높지만 캐시데이터에대한 관리(동기화) 비용이 많이 소모된다  
소프트파싱은 쓰기 전략에 높은 성능을 제공한다

### 스토리지 엔진

디스크에서 데이터를 가져오거나 저장하는 역할을 수행한다  
MYSQL 8 버전부터 InnoDB 엔진을 디폴트로 사용한다

InnoDB 의 핵심 키워드는 (인덱스 part) Clustered Index / (트랜젝션 part) Redo-Undo, Buffer pool  

## 3. 정규화 - 비정규화
데이터베이스 정규화 참고 : https://mangkyu.tistory.com/110  

- 정규화
  - 정규화란 결국 데이터 중복을 최소화시키는게 궁극적인 목표이다
  - 책을 쓰는 작가라고 생각해보면 주인공이름을 읽을때 한곳에서 관리한다면 추후 수정에 용이할 것이다
  - 데이터 조회에는 장점이 있지만 쓰기 관점에선 원본데이터를 참조해야한다는 점에서 단점이 있다
- 비정규화
  - 읽기의 성능을 올리기 위해 중복을 허용하는 것을 말한다
  - 데이터 조회에는 단점이 있지만 쓰기 관점에서는 장점이 있다

**결국 읽기와 쓰기사이의 트레이드오프를 고민하는것**  

### 중복된 데이터라면 반드시 정규화를 해야하는가 ?
- 정규화도 일종의 비용이다. (읽기 비용을 지불하고 쓰기 비용을 줄이는것)

### 정규화시 고려해야 하는것
- 얼마나 빠르게 데이터의 최신성을 보장해야하는가 ?
- 히스토리성 데이터는 오히려 정규화의 필요성이 떨어진다
- 데이터 변경 주기는 어떻게 되는가 ?
- 객체 (테이블) 탐색 깊이가 얼마나 깊은

### 정규화를 하기로 했다면 데이터를 어떻게 가져올 것인가 ?
- 보통 테이블 조인을 바로 생각하는데 이는 고민해볼 문제이다
  - 테이블 조인은 서로 다른 테이블의 결합도를 높히는 것이다
- 조회시엔 성능이 좋은 In memory DB 같은 캐싱과 별도 DB 를 이용하는 방법이 있다
- 읽기쿼리가 한번더 발생하는 것은 그렇게 큰 부담이 아닐 수 있다