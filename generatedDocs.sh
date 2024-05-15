#!/bin/bash

# 모든 모듈의 테스트 실행
./gradlew test

# 스니펫 수집
./gradlew collectSnippets

# 문서 생성
#./gradlew asciidoctorAll