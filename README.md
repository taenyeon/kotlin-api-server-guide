# ***Kotlin-api-server-guide***

### ***제작 의도***

#### 나중에 kotlin 관련 프로젝트를 하게 될 경우, 기본 틀을 재사용하기 위해서 제작

### ***API 문서***

* ***asciiDocs*** 사용
    * WebMvcTest를 통해 Docs 제작에 필요한 snippet을 생성함.
        * snippet 양식은 ***/src/test/resources/org/springframework/restdocs/templates*** 에서 수정 가능.
        * 생성된 snippet는 ***/build/generated-snippets*** 에 저장됨.
    * 생성한 snippet들은 */src/docs/asciidoc/sample.adoc* 파일에서 추가하여 API 문서 제작.
    * 테스트를 통해 생성된 *sample.adoc* 파일은 build 시점에 */src/resources/static/sample.html* 과 같이 html 형태로 저장됨.

### ***Coverage Test***

* ***jacoco*** 사용
* ***context***를 통해 ***service*** 로직 케이스 분리 (최대한 전체 케이스에 대한 검증을 할 수 있도록 함.)
* ***it***을 통해 <span style="color:blue;">***SUCCESS***</span> or *<span style="color:red;">**ERROR***</span>(정의된 케이스)를
  표기
* <span style="color:red;">***추후에는 coverage 통과 여부 체크를 할 예정***</span>

### ***HTTP Test***

<span style="color:red;">***Authorization*** 발급이 필요한 경우, ***authorize.http***를 통해 발급</span> ***[authorize.http](http/authorize.http)***


* ***root*** 하위의 ***http*** 폴더 참고
* ***env*** 설정을 통해 환경 변수 분리 (현재는 local만 존재)
* 테스트 시, env를 꼭 확인해야함