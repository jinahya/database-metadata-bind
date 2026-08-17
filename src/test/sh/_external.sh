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

# Runs every external database integration test backed by a local Docker
# container. Each script owns its container lifecycle and preserves target/
# artifacts by intentionally avoiding Maven's clean phase.

set -u

SCRIPT_DIR=$(CDPATH= cd -- "$(dirname -- "$0")" && pwd)
PROJECT_DIR=$(CDPATH= cd -- "${SCRIPT_DIR}/../../.." && pwd)

scripts='
_external-mariadb.sh
_external-mysql.sh
_external-oracle.sh
_external-postgresql.sh
'

mkdir -p "${PROJECT_DIR}/target"

summary=''
failed=0

statusfile="$(mktemp)"
trap 'rm -f "$statusfile"' EXIT

for script in $scripts; do
  name=${script#_external-}
  name=${name%.sh}
  log="${PROJECT_DIR}/target/external-${name}.log"
  echo "==> ${SCRIPT_DIR}/$script (log: $log)"
  { "${SCRIPT_DIR}/$script" 2>&1; echo "$?" > "$statusfile"; } | tee "$log"
  if [ "$(cat "$statusfile")" -eq 0 ]; then
    status='PASS'
  else
    status='FAIL'
    failed=$((failed + 1))
  fi
  summary="${summary}  ${status}  ${name}\n"
done

echo
echo '==================== External summary ===================='
printf '%b' "$summary"
echo '=========================================================='

if [ "$failed" -ne 0 ]; then
  echo "$failed external(s) failed." >&2
  exit 1
fi
