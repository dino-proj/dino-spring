#!/usr/bin/env bash
# Copyright 2021 dinospring.cn
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#     http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.

WEBHOOK_URL="https://open.feishu.cn/open-apis/bot/v2/hook/8b94c66d-23ce-44bf-af64-db63264969ee"

stdin_read() {
  while read line
  do
    echo "$line"
  done < "${1:-/dev/stdin}"
}

json() {
  JSON_RAW=$1
  echo json $JSON_RAW
  JSON_RAW=${JSON_RAW//\\/\\\\} # \
  JSON_RAW=${JSON_RAW//\//\\\/} # /
  JSON_RAW=${JSON_RAW//\'/\\\'} # ' (not strictly needed ?)
  JSON_RAW=${JSON_RAW//\"/\\\"} # "
  JSON_RAW=${JSON_RAW//   /\\t} # \t (tab)
  JSON_RAW=${JSON_RAW//
/\\\n} # \n (newline)
  JSON_RAW=${JSON_RAW//^M/\\\r} # \r (carriage return)
  JSON_RAW=${JSON_RAW//^L/\\\f} # \f (form feed)
  JSON_RAW=${JSON_RAW//^H/\\\b} # \b (backspace)
  echo $JSON_RAW
}


MSG=`stdin_read`
echo $MSG

MSG=`json "$MSG"`
echo $MSG

curl -X POST \
  -H "Content-Type: application/json" \
	-d "{\"msg_type\":\"text\",\"content\":{\"text\":\"$MSG\"}}" \
  "$WEBHOOK_URL"