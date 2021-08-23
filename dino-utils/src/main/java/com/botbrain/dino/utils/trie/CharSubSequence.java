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
