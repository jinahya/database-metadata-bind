#!/usr/bin/env sh

set -eu

SCRIPT_DIR=$(CDPATH= cd -- "$(dirname -- "$0")" && pwd)
PROJECT_DIR=$(CDPATH= cd -- "${SCRIPT_DIR}/../.." && pwd)

(cd "${PROJECT_DIR}" && ./mvnw -Ptestcontainers,testcontainers-mysql clean verify)
