#!/bin/sh

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

set -eu

SCRIPT_DIR=$(CDPATH= cd -- "$(dirname -- "$0")" && pwd)
PROJECT_DIR=$(CDPATH= cd -- "${SCRIPT_DIR}/../../.." && pwd)
MVN="${MVN:-./mvnw}"
IMAGE="${IMAGE:-postgres:17}"
HOST="${HOST:-127.0.0.1}"
PORT="${PORT:-5432}"
DATABASE="${DATABASE:-metadata_bind}"
POSTGRES_USER="${POSTGRES_USER:-metadata_bind}"
PASSWORD="${PASSWORD:-metadata_bind}"
CONTAINER_NAME="${CONTAINER_NAME:-database-metadata-bind-external-postgresql}"
URL="${URL:-jdbc:postgresql://${HOST}:${PORT}/${DATABASE}}"
WAIT_SECONDS="${WAIT_SECONDS:-60}"
PLATFORM="${PLATFORM:-}"
CONTAINER_ID=""

cleanup() {
  status=$?
  if [ -n "${CONTAINER_ID}" ]; then
    docker rm -f "${CONTAINER_ID}" >/dev/null 2>&1 || true
  fi
  exit "${status}"
}

trap cleanup EXIT INT TERM

cat <<EOF
ExternalIT command:
cd '${PROJECT_DIR}' && ${MVN} -Pfailsafe,external,external-postgresql -Dit.test=ExternalIT -Durl='${URL}' -Duser='${POSTGRES_USER}' -Dpassword='${PASSWORD}' test-compile failsafe:integration-test

Starting ${IMAGE}.
Press Ctrl+C to stop the test and remove the container.

EOF

if [ -n "${PLATFORM}" ]; then
  CONTAINER_ID=$(docker run -d --rm \
    --platform "${PLATFORM}" \
    --name "${CONTAINER_NAME}" \
    -p "${PORT}:5432" \
    -e "POSTGRES_DB=${DATABASE}" \
    -e "POSTGRES_USER=${POSTGRES_USER}" \
    -e "POSTGRES_PASSWORD=${PASSWORD}" \
    "${IMAGE}")
else
  CONTAINER_ID=$(docker run -d --rm \
    --name "${CONTAINER_NAME}" \
    -p "${PORT}:5432" \
    -e "POSTGRES_DB=${DATABASE}" \
    -e "POSTGRES_USER=${POSTGRES_USER}" \
    -e "POSTGRES_PASSWORD=${PASSWORD}" \
    "${IMAGE}")
fi

echo "Started container ${CONTAINER_ID}."
echo "Waiting for PostgreSQL to accept connections..."

i=0
until docker exec "${CONTAINER_ID}" pg_isready -h 127.0.0.1 -U "${POSTGRES_USER}" -d "${DATABASE}" >/dev/null 2>&1; do
  i=$((i + 1))
  if [ "${i}" -ge "${WAIT_SECONDS}" ]; then
    docker logs "${CONTAINER_ID}" || true
    echo "PostgreSQL did not become ready within ${WAIT_SECONDS} seconds." >&2
    exit 1
  fi
  sleep 1
done

echo "PostgreSQL is ready."

(cd "${PROJECT_DIR}" && "${MVN}" -Pfailsafe,external,external-postgresql \
  -Dit.test=ExternalIT \
  -Durl="${URL}" \
  -Duser="${POSTGRES_USER}" \
  -Dpassword="${PASSWORD}" \
  test-compile failsafe:integration-test)
