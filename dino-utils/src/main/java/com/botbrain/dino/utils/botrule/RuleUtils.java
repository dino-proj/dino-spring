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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class RuleUtils {
  private static final Pattern HTML_TAG = Pattern.compile("<\\/?[\\s]*[a-z0-9]+[^>]*>",
      Pattern.CASE_INSENSITIVE | Pattern.DOTALL | Pattern.MULTILINE);

  private RuleUtils() {
  }

  public static List<StringRange> innerText(CharSequence html) {
    Matcher m = HTML_TAG.matcher(html);
    List<StringRange> strs = new ArrayList<>();
    int preStart = 0;
    while (m.find()) {
      if (preStart < m.start()) {
        strs.add(new StringRange(html, preStart, m.start()));
      }
      preStart = m.end();
    }
    if (preStart < html.length()) {
      strs.add(new StringRange(html, preStart, html.length()));
    }
    return strs;
  }

  public static class StringRange {
    private int startPos;
    private int endPos;
    private CharSequence str;

    public StringRange(CharSequence str, int startPos, int endPos) {
      this.str = str;
      this.startPos = startPos;
      this.endPos = endPos;
    }

    /**
     * @return the startPos
     */
    public int getStartPos() {
      return startPos;
    }

    /**
     * @return the str
     */
    public CharSequence getStr() {
      return str;
    }

    /**
     * @return the endPos
     */
    public int getEndPos() {
      return endPos;
    }

  }
}
