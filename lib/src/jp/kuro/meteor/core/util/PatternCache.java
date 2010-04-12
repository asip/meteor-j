//
//Meteor -  A lightweight (X)HTML & XML parser
// Copyright (C) 2002-2010 Yasumasa Ashida.
//
// This program is free software; you can redistribute it and/or modify
// it under the terms of the GNU Lesser General Public License as published by
// the Free Software Foundation; either version 2.1 of the License, or
// (at your option) any later version.
//
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU Lesser General Public License for more details.
//
// You should have received a copy of the GNU Lesser General Public License
// along with this program; if not, write to the Free Software
// Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
//

package jp.kuro.meteor.core.util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

/**
 * 正規表現パターンキャッシュ
 *
 * @author Yasumasa Ashida
 * @author Yasumasa Ashida
 * @version 0.9.3.3
 * @since 2007/09/30 17:49:21
 */
public class PatternCache {

    /**
     * 正規表現キャッシュ（キー：正規表現文字列、値：Pattern オブジェクト）
     */
    private static final Map<String, Pattern> regexCache =
            new ConcurrentHashMap<String, Pattern>();

    /**
     * リテラルキャッシュ（キー：リテラル文字列、値：Pattern オブジェクト）
     */
    private static final Map<String, Pattern> literalCache =
            new ConcurrentHashMap<String, Pattern>();

    /**
     * コンストラクタです。生成不可。
     */
    private PatternCache() {
    }

    /**
     * 正規表現パターンを取得します。
     *
     * @param regex 正規表現文字列
     * @return Pattern オブジェクト
     */
    public static Pattern get(String regex) {

        Pattern pattern = regexCache.get(regex);
        if (pattern == null) {
            pattern = Pattern.compile(regex);
            regexCache.put(regex, pattern);
        }
        return pattern;
    }

    /**
     * リテラル パターンを取得します。
     *
     * @param target リテラル文字列
     * @return Pattern オブジェクト
     */
    public static Pattern getLiteral(String target) {

        Pattern pattern = literalCache.get(target);
        if (pattern == null) {
            pattern = Pattern.compile(target, Pattern.LITERAL);
            literalCache.put(target, pattern);
        }
        return pattern;
    }
}
