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


bin=`dirname "${BASH_SOURCE-$0}"`
CWD=`cd "$bin"; pwd`

function usage(){
  echo "USAGE:"
  echo "change-version <old version> <new version>"
  echo ""
  echo "example:"
  echo "change-version 1.1.0 1.1.1"
}

function isMac(){
  a=`uname  -a`
  return $a =~ "Darwin"
}

if [ $# -ne 2 ];
then
    usage
    exit
fi

OLD_VER=$1
NEW_VER=$2

for proj in "dino-dependencies-root" "dino-spring-assembly" "dino-utils" "dino-spring-boot-starter" "dino-spring-cloud-starter" "dino-spring-commons" "dino-spring-data" "dino-spring-core"
  do
    echo change "$proj" version to $NEW_VER
    cd "$CWD/$proj"
    if [ isMac ];then
      sed -i "" "s/<version>${OLD_VER}.RELEASE<\\/version>/<version>${NEW_VER}.RELEASE<\\/version>/g" pom.xml
      sed -i "" "s/<dino-spring.version>${OLD_VER}.RELEASE<\\/dino-spring.version>/<dino-spring.version>${NEW_VER}.RELEASE<\\/dino-spring.version>/g" pom.xml
    else
      sed -i "s/<version>${OLD_VER}.RELEASE<\\/version>/<version>${NEW_VER}.RELEASE<\\/version>/g" pom.xml
      sed -i "s/<dino-spring.version>${OLD_VER}.RELEASE<\\/dino-spring.version>/<dino-spring.version>${NEW_VER}.RELEASE<\\/dino-spring.version>/g" pom.xml
    fi
  done

echo "done!"