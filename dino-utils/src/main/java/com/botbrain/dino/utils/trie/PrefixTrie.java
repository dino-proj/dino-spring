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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.function.BinaryOperator;

public class PrefixTrie<T> implements Iterable<T> {

  private final Node<T> root;
  private final CharTokenlizer tokenlizer;
  private final boolean breakWord;

  public PrefixTrie(CharTokenlizer tokenlizer, boolean breakWord) {
    this.tokenlizer = tokenlizer;
    this.breakWord = breakWord;
    root = new Node<>();
  }

  /**
   * 将一个词串添加到前缀树上，如果这个词已经存在，这会覆盖原来的值
   *
   * @param word  要添加的单词，单词会被归一化处理，比如大写转小写等
   * @param value 这次词对应的值信息
   */
  public void addWord(CharSequence word, T value) {
    addWord(word, value, null);
  }

  /**
   * 将一个词串添加到前缀树上，如果这个词已经存在，这会覆盖原来的值
   *
   * @param word  要添加的单词，单词会被归一化处理，比如大写转小写等
   * @param value 这次词对应的值信息
   * @param onExist 当之前的值已经存在时的操作
   */
  public void addWord(CharSequence word, T value, BinaryOperator<T> onExist) {
    if (word == null || word.length() == 0) {
      throw new IllegalArgumentException("word must not be null or empty");
    }
    Node<T> current = root;
    for (int i = 0; i < word.length(); i++) {
      char ch = tokenlizer.normalize(word.charAt(i));
      if (tokenlizer.isSkip(ch, word, i)) {
        continue;
      }
      Node<T> next = current.next(ch);
      if (next == null) {
        next = new Node<>();
        current.addNext(ch, next);
      }
      current = next;
    }
    if (onExist != null && current.getValue() != null) {
      current.setValue(word, onExist.apply(current.getValue(), value));
    } else {
      current.setValue(word, value);
    }
  }

  /**
   * 最小匹配，当匹配到第一个时就返回，如果不能匹配则返回null
   *
   * @param str 要匹配的字符串信息，匹配过程中会跳过忽略词
   * @return 返回匹配到的模式串
   */
  public MatchValue<T> matchMin(CharSequence str) {
    return matchMin(str, 0, str.length());
  }

  /**
   * 最小匹配，当匹配到第一个时就返回，如果不能匹配则返回null
   *
   * @param str 要匹配的字符串信息，匹配过程中会跳过忽略词
   * @return 返回匹配到的模式串
   */
  public MatchValue<T> matchMin(CharSequence str, int startPos, int endPos) {
    Node<T> current = root;
    // 查看前面一个词字符是不是可以作为一个词的开始
    if (!this.breakWord && !tokenlizer.isWordStart(str, startPos)) {
      return null;
    }
    for (int i = startPos; i < endPos; i++) {
      char ch = tokenlizer.normalize(str.charAt(i));
      if (tokenlizer.isSkip(ch, str, i)) {
        if (i == startPos) {
          return null;
        }
        continue;
      }
      current = current.next(ch);
      if (current == null) {
        return null;
      }
      if (current.getValue() != null && (breakWord || tokenlizer.isWordEnd(str, i))) {
        return new MatchValue<>(current.getWord(), current.getValue(), str, startPos, i + 1);
      }
    }
    return null;
  }

  /**
   * 最大匹配，如果没有匹配任何字符，则返回null
   *
   * @param str
   * @return
   */
  public MatchValue<T> matchMax(CharSequence str) {
    return matchMax(str, 0, str.length());
  }

  /**
   * 最大匹配，如果没有匹配任何字符，则返回null
   *
   * @param str
   * @return
   */
  public MatchValue<T> matchMax(CharSequence str, int startPos, int endPos) {
    // 查看前面一个词字符是不是可以作为一个词的开始
    if (!this.breakWord && !tokenlizer.isWordStart(str, startPos)) {
      return null;
    }
    Node<T> lastNode = null;
    int lastPos = 0;
    Node<T> current = root;
    for (int i = startPos; i < endPos; i++) {
      char ch = tokenlizer.normalize(str.charAt(i));
      if (!tokenlizer.isSkip(ch, str, i)) {
        current = current.next(ch);
        if (current == null) {
          break;
        }
        if (current.getValue() != null && !breakWord && tokenlizer.isWordEnd(str, i)) {
          lastNode = current;
          lastPos = i + 1;
        }
      }
    }
    if (lastNode != null) {
      return new MatchValue<>(lastNode.getWord(), lastNode.getValue(), str, startPos, lastPos);
    } else {
      return null;
    }
  }

  /**
   * 完整匹配，只有当str全部匹配到一个节点时，才返回那个节点上的数据。
   *
   * @param str
   * @return
   */
  public T match(CharSequence str) {
    return match(str, 0, str.length());
  }

  /**
   * 完整匹配，只有当str全部匹配到一个节点时，才返回那个节点上的数据。
   *
   * @param str
   * @return
   */
  public T match(CharSequence str, int start, int end) {
    Node<T> current = root;
    for (int i = start; i < end; i++) {
      char ch = tokenlizer.normalize(str.charAt(i));
      if (tokenlizer.isSkip(ch, str, i)) {
        continue;
      }
      current = current.next(ch);
      if (current == null) {
        return null;
      }
    }
    return current.getValue();
  }

  public List<MatchValue<T>> matchAll(CharSequence str) {
    return matchAll(str, 0, str.length());
  }

  public List<MatchValue<T>> matchAll(CharSequence str, int startPos, int endPos) {
    List<MatchValue<T>> result = new ArrayList<>();
    if (!this.breakWord && !tokenlizer.isWordStart(str, startPos)) {
      return result;
    }
    int lastPos = 0;
    Node<T> current = root;
    for (int i = startPos; i < endPos; i++) {
      char ch = tokenlizer.normalize(str.charAt(i));
      if (tokenlizer.isSkip(ch, str, i)) {
        continue;
      }
      current = current.next(ch);
      if (current == null) {
        current = root.next(ch);
        if (current == null) {
          current = root;
        }
      } else if (current.getValue() != null && (breakWord || tokenlizer.isWordEnd(str, i))) {
        result.add(new MatchValue<>(current.getWord(), current.getValue(), str, startPos, lastPos));
        lastPos = i + 1;
      }
    }
    return result;
  }

  /**
   * 返回一个迭代器，这个迭代器可以遍历从头开始能匹配的词，长度从短到长 第一个结果等于
   * {@link #matchFirst(CharSequence)的结果， 最后一个结果等于 {
   *
   * @link #matchDeepest(CharSequence)}的结果 不会返回null
   *
   * @param str 要匹配的字符串
   * @return 返回一个迭代器，可以匹配所有的次，第一个是最短的匹配，最后一个是最长匹配
   */
  public Iterator<MatchValue<T>> matchIterator(CharSequence str) {
    return matchIterator(str, 0, str.length());
  }

  /**
   * 返回一个迭代器，这个迭代器可以遍历从头开始能匹配的词，长度从短到长 第一个结果等于
   * {@link #matchFirst(CharSequence)的结果， 最后一个结果等于 {
   *
   * @link #matchDeepest(CharSequence)}的结果 不会返回null
   *
   * @param str 要匹配的字符串
   * @return 返回一个迭代器，可以匹配所有的次，第一个是最短的匹配，最后一个是最长匹配
   */
  public Iterator<MatchValue<T>> matchIterator(CharSequence str, int startPos, int endPos) {
    return new Iter(str, startPos, endPos);
  }

  private class Iter implements Iterator<MatchValue<T>> {

    private MatchValue<T> next = null;
    private Node<T> current;
    private CharSequence str;
    private int startPos;
    private int endPos;
    private int curPos;

    public Iter(CharSequence str, int startPos, int endPos) {
      this.str = str;
      this.curPos = this.startPos = startPos;
      this.endPos = endPos;
      this.current = root;

      // 查看前面一个词字符是不是可以作为一个词的开始
      if (!breakWord && !tokenlizer.isWordStart(str, startPos)) {
        next = null;
      } else {
        prepareNext();
      }
    }

    private void prepareNext() {
      if (current == null || curPos >= endPos) {
        next = null;
        return;
      }
      for (; curPos < endPos; curPos++) {
        char ch = tokenlizer.normalize(str.charAt(curPos));
        if (tokenlizer.isSkip(ch, str, curPos)) {
          continue;
        }
        current = current.next(ch);
        if (current == null) {
          this.current = root.next(ch);
          if (current == null) {
            this.current = root;
            continue;
          }
        }
        if (current.getValue() != null && !breakWord && tokenlizer.isWordEnd(str, curPos)) {
          break;
        }
      }
      curPos++;
      if (current.getValue() != null) {
        next = new MatchValue<>(current.word, current.getValue(), str, startPos, curPos);
      } else {
        next = null;
      }
    }

    @Override
    public boolean hasNext() {
      return next != null;
    }

    @Override
    public MatchValue<T> next() {
      if (!hasNext()) {
        throw new NoSuchElementException();
      }
      MatchValue<T> cur = next;
      prepareNext();
      return cur;
    }

    @Override
    public void remove() {
      throw new UnsupportedOperationException();
    }

  }

  public static class MatchValue<TT> {
    private CharSequence word;
    private TT value;
    private int startPos;
    private int endPos;
    private CharSequence str;

    public MatchValue(CharSequence word, TT value, CharSequence str, int startPos, int endPos) {
      super();
      this.word = word;
      this.value = value;
      this.startPos = startPos;
      this.endPos = endPos;
      this.str = str;
    }

    public TT getValue() {
      return value;
    }

    public int getStartPos() {
      return startPos;
    }

    public int getEndPos() {
      return endPos;
    }

    public int getLength() {
      return endPos - startPos;
    }

    /**
     * 获取在目标字符串上的匹配结果，其中包含忽略词，比如“毛*泽定”，“*”作为忽略词也会包含在里面
     *
     * @return 匹配结果字符串
     */
    public CharSequence getMatched() {
      return str.subSequence(startPos, endPos);
    }

    /**
     * 获取原字典传入的原始词，这个词没有被做任何处理，大小写转换等都没有
     *
     * @return 请不要修改返回串里面的内容
     */
    public CharSequence getWord() {
      return word;
    }

  }

  private static class Node<T> {
    private T value;
    private Map<Character, Node<T>> children;
    private CharSequence word;

    public Node<T> next(char key) {
      return children == null ? null : children.get(key);
    }

    public synchronized void addNext(char key, Node<T> nextNode) {
      if (children == null) {
        children = new HashMap<>(4);
      }
      children.put(key, nextNode);
    }

    public boolean hasChildren() {
      return children != null;
    }

    public Collection<Node<T>> children() {
      return children.values();
    }

    public T getValue() {
      return value;
    }

    public CharSequence getWord() {
      return word;
    }

    public void setValue(CharSequence word, T value) {
      this.word = word;
      this.value = value;
    }

    public synchronized void clear() {
      value = null;
      children = null;
    }

  }

  @Override
  public Iterator<T> iterator() {
    return new PrefixTrieIterator(this.root);
  }

  private class PrefixTrieIterator implements Iterator<T> {
    private LinkedList<Node<T>> queue = new LinkedList<>();

    T value = null;

    PrefixTrieIterator(Node<T> startNode) {
      queue.push(startNode);
      findNext();
    }

    public boolean hasNext() {
      return value != null;
    }

    private void findNext() {
      T o = null;
      while (o == null && !queue.isEmpty()) {
        Node<T> n = queue.removeFirst();
        if (n.value != null) {
          o = n.value;
        }
        if (n.hasChildren()) {
          for (Node<T> e : n.children()) {
            queue.addFirst(e);
          }
        }
      }
      value = o;
    }

    public T next() {
      if (value == null) {
        throw new NoSuchElementException();
      }
      T v = value;
      findNext();
      return v;
    }

    @Override
    public void remove() {
      throw new UnsupportedOperationException();
    }

  }

  public void clear() {
    root.clear();
  }

}
