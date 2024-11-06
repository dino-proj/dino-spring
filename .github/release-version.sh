#!/usr/bin/env bash
# Copyright 2024 dinosdev.cn.
# SPDX-License-Identifier: Apache-2.0


bin=`dirname "${BASH_SOURCE-$0}"`
CWD=`cd "$bin"; pwd`

function usage(){
  echo "USAGE:"
  echo "release-version <new version>"
  echo ""
  echo "example:"
  echo "release-version 1.1.1"
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
    cd "$CWD/../$proj"
    mvn install -DskipTests
    mvn versions:set -DnewVersion=${NEW_VER} -DgenerateBackupPoms=false
    mvn versions:set-property -DnewVersion=${NEW_VER} -Dproperty=dino-spring.version -DgenerateBackupPoms=false
    mvn install -DskipTests
  done

for proj in "dino-spring-cloud-starter-parent" "dino-spring-commons" "dino-spring-auth" "dino-spring-data" "dino-spring-core"
  do
    echo change "$proj" version to $NEW_VER
    cd "$CWD/../$proj"
    mvn versions:set -DnewVersion=${NEW_VER} -DgenerateBackupPoms=false
    mvn versions:update-parent -DparentVersion=${NEW_VER} -DgenerateBackupPoms=false
  done

# change version in ./README.md
echo "change version in README.md"
cd "$CWD/.."
sed -i '' -E '/<parent>/,/<\/parent>/ s/(<version>)[^<]+(<\/version>)/\1'"$NEW_VER"'\2/' README.md

# git commit
echo "git commit"
git add .
git commit -m "release version $NEW_VER"

# git tag
echo "git tag"
git tag -a "RELEASE-$NEW_VER" -m "release version $NEW_VER"

# git push main and tag
echo "git push"
git push Github main
git push Github "RELEASE-$NEW_VER"

echo "done!"

