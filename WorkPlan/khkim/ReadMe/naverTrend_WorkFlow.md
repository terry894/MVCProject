# WorkFlow

## 주식데이터 
### 일정
STEP1(5일) -> STEP2(2일) -> STEP3(2일) -> STEP4(5~7일)

### STEP1. 데이터 수집  
- 증권사 API
  - 트렌드 검색 결과 데이터 수집 가능 확인 = 일 1000회, 광고시 제한신청, 제휴권한 ----- (완료)
    <img src="https://user-images.githubusercontent.com/16996054/67345323-93227b00-f575-11e9-836e-befc3839f656.jpg" width="70%">
    - 한글입력 문제있음 -> System.in encoding 문제
    - parsing 된 결과를 ResutData 에 저장
    - refactoring 요함 
  - 키움 API 를 사용. KOAStudioSA 를 사용하여 데이터 수집 가능 확인 ----- (진행중)
    
- 웹크롤링(차선책)

### STEP2. 데이터 모델링 및 테이블화
- DB 에 저장 요소
    - 트렌드
    - 키움API
- 주가데이터
- 거래량
- 트렌드 검색결과 
  - 관련검색어 및 수치와 거래량 및 주가와의 상관관계 

### STEP3. 데이터 전달
- JDBC 로직 구현
- 데이터 전달방법

### STEP4. 알고리즘 
- 데이터를 배합하고 과거 시뮬레이션으로 알고리즘 
- 테스팅을 위한 API 설계

### 요구사항
- 관련검색어는?
