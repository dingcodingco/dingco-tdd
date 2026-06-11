#!/usr/bin/env bash
#
# verify.sh — TDD 실습 프로젝트가 "정상 동작" 하는지 자동으로 검증한다.
#   1) JDK 21 확인  2) ./gradlew test 가 28개 테스트 전부 통과  3) bootRun 으로 :8080 기동·응답
#
# 사전 조건: JDK 21 설치. (시스템 기본 java 가 21이 아니면 JAVA_HOME 을 21로 지정)
set -uo pipefail
cd "$(dirname "$0")"

PASS=0; FAIL=0
ok()  { echo "  ✅ $1"; PASS=$((PASS+1)); }
bad() { echo "  ❌ $1"; FAIL=$((FAIL+1)); }

echo "=== 1) JDK 21 확인 ==="
JV=$("${JAVA_HOME:+$JAVA_HOME/bin/}java" -version 2>&1 | head -1)
echo "     $JV"
if echo "$JV" | grep -q '"21'; then
  ok "Java 21 사용 중"
else
  bad "Java 21 이 아니다. JAVA_HOME 을 JDK 21 로 지정 후 다시 실행하세요 (예: export JAVA_HOME=\$(/usr/libexec/java_home -v 21))"
fi

echo "=== 2) ./gradlew test (28개 테스트) ==="
if ./gradlew clean test --console=plain >/tmp/dingcotdd-test.log 2>&1; then
  ok "BUILD SUCCESSFUL — 테스트 통과"
  # 테스트 리포트에서 실제 실행/실패 수 확인
  REPORT=$(find build/test-results/test -name '*.xml' 2>/dev/null | xargs grep -h 'testsuite' 2>/dev/null)
  TOTAL=$(find build/test-results/test -name 'TEST-*.xml' 2>/dev/null \
            | xargs sed -n 's/.*tests="\([0-9]*\)".*/\1/p' 2>/dev/null \
            | awk '{s+=$1} END{print s}')
  echo "     실행된 테스트 수: ${TOTAL:-?}"
  if [[ "${TOTAL:-0}" -ge 28 ]]; then ok "테스트 28개 이상 실행됨"; else bad "기대한 28개보다 적게 실행됨 (${TOTAL:-0})"; fi
else
  bad "테스트 실패 — /tmp/dingcotdd-test.log 확인"
  tail -20 /tmp/dingcotdd-test.log | sed 's/^/     /'
fi

echo "=== 3) bootRun 기동 → http://localhost:8080/polls/ 응답 확인 ==="
./gradlew bootRun --args='--spring.profiles.active=demo' --console=plain >/tmp/dingcotdd-boot.log 2>&1 &
BOOT_PID=$!
# 기동 대기 (최대 60초)
UP=""
for i in $(seq 1 60); do
  if curl -s -o /dev/null -w '%{http_code}' http://localhost:8080/polls/ 2>/dev/null | grep -q 200; then UP=1; break; fi
  sleep 1
done
if [[ -n "$UP" ]]; then
  ok "http://localhost:8080/polls/ → 200 OK (Spring Boot 정상 기동)"
  BODY=$(curl -s http://localhost:8080/polls/)
  if echo "$BODY" | grep -q "<ul>"; then ok "질문 목록(<ul>) 렌더링 확인"; else bad "목록 렌더링 확인 실패"; fi
else
  bad "8080 기동/응답 실패 — /tmp/dingcotdd-boot.log 확인"
fi
# 서버 정리
kill "$BOOT_PID" 2>/dev/null
pkill -f 'DingcotddApplication' 2>/dev/null
sleep 1

echo ""
echo "=== 결과: ✅ $PASS  /  ❌ $FAIL ==="
[[ "$FAIL" -eq 0 ]] && echo "모든 검증 통과 🎉" || echo "실패한 항목을 확인하세요."
exit "$FAIL"
