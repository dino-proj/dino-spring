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

import java.io.Serializable;
import java.util.HashSet;

public class CharTokenlizer implements Serializable {
  private static final long serialVersionUID = 1L;
  public static final char CN_SPACE = '\u3000';
  private HashSet<Character> skipChars = new HashSet<>();

  public CharTokenlizer(String skips) {
    if (skips != null) {
      skips.chars().forEach(ch -> skipChars.add(normalize((char) ch)));
    }
  }

  public boolean isSkip(char cur, CharSequence str, int pos) {
    if (skipChars.contains(cur)) {
      return true;
    }
    if (cur != ' ') {
      return false;
    }
    int prePos = pos - 1;
    while (prePos >= 0) {
      char pre = normalize(str.charAt(prePos--));
      if (isCJKCharacter(pre) || pre == ' ') { // 前面有过空格作为忽略字符处理，cjk后面的空格可以忽略。
        return true;
      } else if (isEnOrNumeric(pre)) {
        break;
      }
    }
    // 前面没内容，或者前面是字符及数字，则看后面是否有数字或者字符，有，则不忽略，否则都可以忽略
    int postPos = pos + 1;
    while (postPos < str.length()) {
      char post = normalize(str.charAt(postPos++));
      if (isCJKCharacter(post)) { // cjk前面的空格可以忽略。
        return true;
      } else if (isEnOrNumeric(post)) {
        return false;
      }
    }
    return false;
  }

  /**
   * 判断字符是否是一个单词的开始，包括英文字母，数字，-‘连接符
   * 
   * @param ch 字符
   * @return
   */
  public boolean isWordStart(CharSequence str, int pos) {
    if (pos == 0) {
      return true;
    }
    char ch = normalize(str.charAt(pos));
    if (!isEnOrNumericOrJoiner(ch)) {
      return true;
    }
    char pre = normalize(str.charAt(pos - 1));

    if (!isEnOrNumericOrJoiner(pre)) {
      return true;
    }
    if ((ch <= '9' && ch >= '0')) {
      return pre == '\''; // 数字前面不可以有单引号
    }
    if ((ch <= 'z' && ch >= 'a')) {
      return (pre == '\'' || pre == '-' || pre == '.'); // 单引号，﹣，. 不影响一个单词的起始
    }
    if (ch == '-' && pos + 1 < str.length()) {
      char post = normalize(str.charAt(pos + 1));
      return (post >= '0' && post <= '9');
    }

    return false;
  }

  /**
   * 判断一个字符是不是数字或者字母或者连接符('-.)
   * @param ch
   * @return
   */
  public boolean isEnOrNumericOrJoiner(char ch) {
    return isEnOrNumeric(ch) || ch == '\'' || ch == '-' || ch == '.';
  }

  /**
   * 判读字符是不是数字或者字母
   * @param ch
   * @return
   */
  public boolean isEnOrNumeric(char ch) {
    return (ch <= '9' && ch >= '0') || (ch >= 'a' && ch <= 'z');
  }

  /**
   * 判断字符是否是一个单词的结束，其后面的字符不能有字母，数字。
   * 
   * @param ch 字符
   * @return
   */
  public boolean isWordEnd(CharSequence str, int pos) {
    if (pos + 1 >= str.length()) {
      return true;
    }
    char ch = normalize(str.charAt(pos));
    if (!((ch <= '9' && ch >= '0') || (ch >= 'a' && ch <= 'z'))) {
      return true;
    }
    char post = normalize(str.charAt(pos + 1));
    return !((post <= '9' && post >= '0') || (post >= 'a' && post <= 'z'));
  }

  /**
   * 对字符进行归一化处理，将大写转小写，全角 to 半角，繁体字转简体字
   * 
   * @param chr
   * @return
   */

  public char normalize(char chr) {
    if (chr >= 'A' && chr <= 'Z') {
      return (char) (chr + 32);
    } else if (chr == CN_SPACE || chr == '\t') {
      return 0x20;
    } else if (chr >= '\uFF01' && chr <= '\uFF5E') { // 全角 to 半角
      return (char) (chr - '\uFEE0');// chr-('\uFF00'-'\u0020')
    }
    return CNConvert.big2simple(chr);
  }

  /**
   * 汉字，日文，韩文
   * 
   * @param ch
   * @return
   */
  public boolean isCJKCharacter(char ch) {
    Character.UnicodeBlock ub = Character.UnicodeBlock.of(ch);
    return (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
        || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
        || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
        || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
        || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS
        // 韩文字符集
        || ub == Character.UnicodeBlock.HANGUL_SYLLABLES || ub == Character.UnicodeBlock.HANGUL_JAMO
        || ub == Character.UnicodeBlock.HANGUL_COMPATIBILITY_JAMO
        // 日文字符集
        || ub == Character.UnicodeBlock.HIRAGANA // 平假名
        || ub == Character.UnicodeBlock.KATAKANA // 片假名
        || ub == Character.UnicodeBlock.KATAKANA_PHONETIC_EXTENSIONS);
  }

}
