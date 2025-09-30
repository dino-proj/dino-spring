#!/usr/bin/env bash
# Copyright 2025 dinosdev.cn.
# SPDX-License-Identifier: Apache-2.0

# 设置脚本在遇到错误时退出
set -e

MVN_OPT='--errors --no-transfer-progress --batch-mode'
BUILD_OPT='-Dmaven.test.skip=true -P publish'

projects=(
  "dino-dependencies-root"
  "dino-spring-assembly"
  "dino-spring-boot-starter-parent"
  "dino-spring-cloud-starter-parent"
  "dino-spring-commons"
  "dino-spring-data"
  "dino-spring-auth"
  "dino-spring-core"
)

 # 循环遍历项目数组
for project in "${projects[@]}"; do
  # 进入项目目录
  cd "$project"
  # 输出开始打包信息
  echo "=============== 开始打包 $project ==============="
  # 执行mvn命令
  mvn clean package install $MVN_OPT $BUILD_OPT
  # 返回到上一级目录
  cd ..
done

echo "所有项目打包完成。"