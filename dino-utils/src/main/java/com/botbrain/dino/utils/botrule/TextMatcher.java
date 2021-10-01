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

package com.botbrain.dino.utils.botrule;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import com.botbrain.dino.utils.trie.CharTokenlizer;
import com.botbrain.dino.utils.trie.PrefixTrie;
import com.botbrain.dino.utils.trie.PrefixTrie.MatchValue;

public class TextMatcher<T extends Rule> {

  private final PrefixTrie<TrieNode> trie;
  private static final AtomicLong ID = new AtomicLong(2L);

  public TextMatcher(CharTokenlizer tokenlizer, boolean breakWord) {
    trie = new PrefixTrie<>(tokenlizer, breakWord);
  }

  /**
   * 添加规则，必须出现的词，当参数传递多个词事，这些词必须都得同时出现
   * 注意：关键词个数不能超过64个
   * 
   * @param r
   * @param words 多个词是与的关系，表示同时存在， 例如"人工智能"，表示一个完整连续的词，
   *              而"人工","智能"，则表示两个词必须都得出现，但是可以不连续，也没有先后出现顺序关系
   */
  public void include(T r, String... words) {
    addRule(r, 0, words);
  }

  /**
   * 添加规则，必须出现的词，当参数传递多个词事，这些词必须不同时出现，只要有一个不出现都是可以的
   * 
   * @param r
   * @param words 多个词是与的关系，表示同时存在， 例如"人工智能"，表示一个完整连续的词，
   *              而"人工","智能"，则表示两个词必须都得出现，但是可以不连续，也没有先后出现顺序关系
   */
  public void exclude(T r, String... words) {
    addRule(r, 1, words);
  }

  private void addRule(T r, int mask, String... words) {
    if (words.length > 64) {
      throw new IllegalArgumentException("关键词不能超过64个");
    }
    if (words.length == 0) {
      return;
    }
    long groupId = ID.incrementAndGet();

    for (String str : words) {
      trie.addWord(str, new TrieNode(r, groupId, words.length, mask));
    }
  }

  public List<HitInfo> match(CharSequence text) {
    List<MatchValue<TrieNode>> matches = new ArrayList<>();
    // 第一步，匹配规则
    for (int i = 0; i < text.length(); i++) {
      MatchValue<TrieNode> m = trie.matchMin(text, i, text.length());
      if (m != null) {
        matches.add(m);
      }
    }
    // 第二步，将多词匹配不完整的剔除掉
    // TODO 未完成
    return new ArrayList<>();
  }

  public class HitInfo {
    int startPos;
    int endPos;
    T rule;
    boolean isGroup;
  }

  protected class TrieNode {
    private T rule;
    private long groupId;
    private int length;
    private int wordMask;

    TrieNode(T rule, long groupId, int length, int wordMask) {
      this.rule = rule;
      this.groupId = groupId;
      this.length = length;
      this.wordMask = wordMask;
    }

    public T getRule() {
      return rule;
    }

    /**
     * @return the groupId
     */
    public long getGroupId() {
      return groupId;
    }

    /**
     * @return the length
     */
    public int getLength() {
      return length;
    }

    /**
     * @return the wordMask
     */
    public int getWordMask() {
      return wordMask;
    }
  }
}
