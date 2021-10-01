// Copyright 2021 dinospring.cn
// 
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
// 
//     http://www.apache.org/licenses/LICENSE-2.0
// 
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.botbrain.dino.utils.trie;

/**
 * 不copy包装一个子字符串, 其子字符串也是不会copy字符数组
 * 
 */
public class CharSubSequence implements CharSequence {
  CharSequence str;
  int from;
  int len;

  CharSubSequence(CharSequence str, int from, int len) {
    this.str = str;
    this.from = from;
    this.len = len;
  }

  @Override
  public int length() {
    return len;
  }

  @Override
  public char charAt(int index) {
    return str.charAt(index + from);
  }

  @Override
  public CharSequence subSequence(int start, int end) {
    return new CharSubSequence(str, start + from, end - start);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder(this.len);
    sb.append(this.str);
    return sb.toString();
  }

}
