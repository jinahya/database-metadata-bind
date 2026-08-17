#!/usr/bin/env sh

###
# #%L
# database-metadata-bind
# %%
# Copyright (C) 2011 - 2026 Jinahya, Inc.
# %%
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
# 
#      http://www.apache.org/licenses/LICENSE-2.0
# 
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
# #L%
###

# Runs the TestContainers integration tests for every supported database.
#
# Unlike a plain `set -e` loop, this keeps going when one database fails, so a
# single flaky/unavailable engine does not hide results from the others. Each
# profile's full output is tee'd to target/testcontainers-<profile>.log for
# later inspection (e.g. mining getXxx metadata behavior across drivers), and a
# pass/fail summary is printed at the end. Exits non-zero if any profile failed.

set -u

SCRIPT_DIR=$(CDPATH= cd -- "$(dirname -- "$0")" && pwd)
PROJECT_DIR=$(CDPATH= cd -- "${SCRIPT_DIR}/../../.." && pwd)

scripts='
_testcontainers-db2.sh
_testcontainers-mariadb.sh
_testcontainers-mssqlserver.sh
_testcontainers-mysql.sh
_testcontainers-oracle-free.sh
_testcontainers-oracle-xe.sh
_testcontainers-postgresql.sh
'

mkdir -p "${PROJECT_DIR}/target"

summary=''
failed=0

statusfile="$(mktemp)"
trap 'rm -f "$statusfile"' EXIT

for script in $scripts; do
  profile=${script#_}
  profile=${profile%.sh}
  log="${PROJECT_DIR}/target/testcontainers-${profile}.log"
  echo "==> ${SCRIPT_DIR}/$script (log: $log)"
  # capture mvnw's real exit status (a pipe would only report tee's) while still
  # streaming its output to both the terminal and the per-profile log file
  { "${SCRIPT_DIR}/$script" 2>&1; echo "$?" > "$statusfile"; } | tee "$log"
  if [ "$(cat "$statusfile")" -eq 0 ]; then
    status='PASS'
  else
    status='FAIL'
    failed=$((failed + 1))
  fi
  summary="${summary}  ${status}  ${profile}\n"
done

echo
echo '==================== TestContainers summary ===================='
printf '%b' "$summary"
echo '================================================================'

if [ "$failed" -ne 0 ]; then
  echo "$failed profile(s) failed." >&2
  exit 1
fi
