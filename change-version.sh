#!/usr/bin/env bash
# Copyright 2021 dinodev.cn
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
  echo "change-version <new version>"
  echo ""
  echo "example:"
  echo "change-version 1.1.1"
}

function isMac(){
  a=`uname  -a`
  return $a =~ "Darwin"
}

if [ $# -ne 1 ];
then
    usage
    exit
fi

NEW_VER=$1

for proj in "dino-dependencies-root" "dino-spring-assembly" "dino-spring-boot-starter-parent"
  do
    echo change "$proj" version to $NEW_VER
    cd "$CWD/$proj"
    mvn install -DskipTests
    mvn versions:set -DnewVersion=${NEW_VER} -DgenerateBackupPoms=false
    mvn versions:set-property -DnewVersion=${NEW_VER} -Dproperty=dino-spring.version -DgenerateBackupPoms=false
    mvn install -DskipTests
  done

for proj in "dino-spring-cloud-starter-parent" "dino-spring-commons" "dino-spring-auth" "dino-spring-data" "dino-spring-core"
  do
    echo change "$proj" version to $NEW_VER
    cd "$CWD/$proj"
    mvn versions:set -DnewVersion=${NEW_VER} -DgenerateBackupPoms=false
    mvn versions:update-parent -DparentVersion=${NEW_VER} -DgenerateBackupPoms=false
  done

echo "done!"