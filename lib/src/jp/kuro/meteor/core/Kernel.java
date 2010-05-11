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

package jp.kuro.meteor.core;

//JAVA標準

import jp.kuro.meteor.AttributeMap;
import jp.kuro.meteor.Element;
import jp.kuro.meteor.Parser;
import jp.kuro.meteor.RootElement;
import jp.kuro.meteor.hook.Hooker;
import jp.kuro.meteor.hook.Looper;
import jp.kuro.meteor.core.util.AsyncStringBuffer;
import jp.kuro.meteor.core.util.PatternCache;
import jp.kuro.meteor.core.html.ParserImpl;
import jp.kuro.meteor.exception.NoSuchElementException;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
//import java.util.LinkedHashMap;
import java.util.List;
import java.util.LinkedHashMap;

/**
 * パーサコア
 *
 * @author Yasumasa Ashida
 * @version 0.9.5.1
 */
public abstract class Kernel implements Parser {


    //ルート要素
    protected RootElement root = null;
    //要素キャッシュ
    protected LinkedHashMap<Integer, Element> elementCache = new LinkedHashMap<Integer, Element>();
    //ドキュメントタイプ
    protected int docType;


    //カウンタ
    protected int counter;

    //正規表現用オブジェクト
    protected Pattern pattern = null;
    protected Pattern pattern_2 = null;
    protected Pattern pattern_1b = null;
    protected Matcher matcher = null;
    protected Matcher matcher＿ = null;

    protected Matcher matcher1 = null;
    protected Matcher matcher2 = null;


    //文字列バッファ
    protected AsyncStringBuffer sbuf = new AsyncStringBuffer();

    protected static final String EMPTY = "";
    protected static final String SPACE = " ";
    protected static final String DOUBLE_QUATATION = "\"";
    protected static final String TAG_OPEN = "<";
    protected static final String TAG_OPEN3 = "</";
    protected static final String TAG_OPEN4 = "<\\\\/";
    protected static final String TAG_CLOSE = ">";
    protected static final String TAG_CLOSE2 = "\\/>";
    protected static final String TAG_CLOSE3 = "/>";
    protected static final String ATTR_EQ = "=\"";
    //element
    //protected static final String TAG_SEARCH_1_1 = "([^<>]*)>(((?!(<\\/";
    protected static final String TAG_SEARCH_1_1 = "(|\\s[^<>]*)>(((?!(";
    //protected static final String TAG_SEARCH_1_2 = "))[\\w\\W])*)<\\/"; 
    protected static final String TAG_SEARCH_1_2 = "[^<>]*>))[\\w\\W])*)<\\/";
    //protected static final String TAG_SEARCH_1_2 = "[^<>]*>)).)*)<\\/";
    protected static final String TAG_SEARCH_1_3 = "(|\\s[^<>]*)\\/>";
    //protected static final String TAG_SEARCH_1_4 = "([^<>\\/]*)>";
    protected static final String TAG_SEARCH_1_4 = "(\\s[^<>\\/]*>|((?!([^<>]*\\/>))[^<>]*>))";
    protected static final String TAG_SEARCH_1_4_2 = "(|\\s[^<>]*)>";
    //protected static final String TAG_SEARCH_2_1 = "\\s([^<>]*";

    protected static final String TAG_SEARCH_2_1 = "(\\s[^<>]*";
    protected static final String TAG_SEARCH_2_1_2 = "(\\s[^<>]*(";
    //protected static final String TAG_SEARCH_2_2 = "\"[^<>]*)>(((?!(<\\/";
    protected static final String TAG_SEARCH_2_2 = "\"[^<>]*)>(((?!(";
    protected static final String TAG_SEARCH_2_2_2 = "\")[^<>]*)>(((?!(";
    protected static final String TAG_SEARCH_2_3 = "\"[^<>]*)";
    protected static final String TAG_SEARCH_2_3_2 = "\"[^<>]*)\\/>";
    protected static final String TAG_SEARCH_2_3_2_2 = "\")[^<>]*)\\/>";
    //protected static final String TAG_SEARCH_2_4 = "\"[^<>\\/]*>";
    protected static final String TAG_SEARCH_2_4 = "([^<>\\/]*>|((?!([^<>]*\\/>))[^<>]*>))";
    //protected static final String TAG_SEARCH_2_4_2 = "\"[^<>\\/]*)>";
    protected static final String TAG_SEARCH_2_4_2 = "([^<>\\/]*>|((?!([^<>]*\\/>))[^<>]*>)))";
    protected static final String TAG_SEARCH_2_4_2_2 = "\")([^<>\\/]*>|((?!([^<>]*\\/>))[^<>]*>)))";
    protected static final String TAG_SEARCH_2_4_2_3 = "\"";
    protected static final String TAG_SEARCH_2_4_3 = "\"[^<>]*)>";
    protected static final String TAG_SEARCH_2_4_3_2 = "\")[^<>]*)>";
    protected static final String TAG_SEARCH_2_4_4 = "\"[^<>]*>";

    protected static final String TAG_SEARCH_2_6 = "\"[^<>]*";
    protected static final String TAG_SEARCH_2_7 = "\"|";

    protected static final String TAG_SEARCH_3_1 = "<([^<>\"]*)\\s[^<>]*";
    protected static final String TAG_SEARCH_3_1_2 = "<([^<>\"]*)\\s([^<>]*";
    protected static final String TAG_SEARCH_3_1_2_2 = "<([^<>\"]*)\\s([^<>]*(";

    protected static final String TAG_SEARCH_3_2 = "\"[^<>]*\\/>";
    protected static final String TAG_SEARCH_3_2_2 = "\"[^<>]*)\\/>";
    protected static final String TAG_SEARCH_3_2_2_2 = "\")[^<>]*)\\/>";

    protected static final String TAG_SEARCH_4_1 = "([^<>\\/]*)>(";
    protected static final String TAG_SEARCH_4_2 = "[\\w\\W]*?<";
    //protected static final String TAG_SEARCH_4_2 = ".*?<";
    //protected static final String TAG_SEARCH_4_3 = "[^<>\\/]*>";
    protected static final String TAG_SEARCH_4_3 = "(\\s[^<>\\/]*>|((?!([^<>]*\\/>))[^<>]*>))";
    protected static final String TAG_SEARCH_4_4 = "<\\/";
    protected static final String TAG_SEARCH_4_5 = "[\\w\\W]*?<\\/";
    protected static final String TAG_SEARCH_4_6 = "[\\w\\W]*?)<\\/";
    //protected static final String TAG_SEARCH_4_7 = "\"[^<>\\/]*)>(";
    protected static final String TAG_SEARCH_4_7 = "\"([^<>\\/]*>|(?!([^<>]*\\/>))[^<>]*>))(";
    protected static final String TAG_SEARCH_4_7_2 = "\")([^<>\\/]*>|(?!([^<>]*\\/>))[^<>]*>))(";

    protected static final String TAG_SEARCH_NC_1_1 = "(?:|\\s[^<>]*)>((?!(";
    //protected static final String TAG_SEARCH_NC_1_2 = "[^<>]*>)).)*<\\/";
    protected static final String TAG_SEARCH_NC_1_2 = "[^<>]*>))[\\w\\W])*<\\/";
    protected static final String TAG_SEARCH_NC_1_3 = "(?:|\\s[^<>]*)\\/>";
    protected static final String TAG_SEARCH_NC_1_4 = "(?:\\s[^<>\\/]*>|((?!([^<>]*\\/>))[^<>]*>))";
    protected static final String TAG_SEARCH_NC_1_4_2 = "(?:|\\s[^<>])*>";
    
    protected static final String TAG_SEARCH_NC_2_1 = "\\s[^<>]*";
    protected static final String TAG_SEARCH_NC_2_1_2 = "\\s[^<>]*(?:";
    protected static final String TAG_SEARCH_NC_2_2 = "\"[^<>]*>((?!(";
    protected static final String TAG_SEARCH_NC_2_2_2 = "\")[^<>]*>((?!(";
    protected static final String TAG_SEARCH_NC_2_3 = "\"[^<>]*)";
    protected static final String TAG_SEARCH_NC_2_3_2 = "\"[^<>]*\\/>";
    protected static final String TAG_SEARCH_NC_2_3_2_2 = "\")[^<>]*\\/>";
    protected static final String TAG_SEARCH_NC_2_4 = "(?:[^<>\\/]*>|((?!([^<>]*\\/>))[^<>]*>))";
    protected static final String TAG_SEARCH_NC_2_4_2 = "(?:[^<>\\/]*>|((?!([^<>]*\\/>))[^<>]*>))";
    protected static final String TAG_SEARCH_NC_2_4_2_2 = "\")(?:[^<>\\/]*>|((?!([^<>]*\\/>))[^<>]*>))";
    protected static final String TAG_SEARCH_NC_2_4_2_3 = "\"";
    protected static final String TAG_SEARCH_NC_2_4_3 = "\"[^<>]*>";
    protected static final String TAG_SEARCH_NC_2_4_3_2 = "\")[^<>]*>";
    protected static final String TAG_SEARCH_NC_2_4_4 = "\"[^<>]*>";
    protected static final String TAG_SEARCH_NC_2_6 = "\"[^<>]*";
    protected static final String TAG_SEARCH_NC_2_7 = "\"|";
    protected static final String TAG_SEARCH_NC_3_1 = "<[^<>\"]*\\s[^<>]*";
    protected static final String TAG_SEARCH_NC_3_1_2 = "<([^<>\"]*)\\s(?:[^<>]*";
    protected static final String TAG_SEARCH_NC_3_1_2_2 = "<([^<>\"]*)\\s(?:[^<>]*(";
    protected static final String TAG_SEARCH_NC_3_2 = "\"[^<>]*\\/>";
    protected static final String TAG_SEARCH_NC_3_2_2 = "\"[^<>]*)\\/>";
    protected static final String TAG_SEARCH_NC_3_2_2_2 = "\")[^<>]*)\\/>";
    //protected static final String TAG_SEARCH_NC_4_1 = "(?:\\s[^<>\\/]*)>("
    //protected static final String TAG_SEARCH_NC_4_2 = ".*?<"
    //protected static final String TAG_SEARCH_NC_4_3 = "(?:\\s[^<>\\/]*>|((?!([^<>]*\\/>))[^<>]*>))"
    //protected static final String TAG_SEARCH_NC_4_4 = "<\\/"
    //protected static final String TAG_SEARCH_NC_4_5 = ".*?<\/"
    //protected static final String TAG_SEARCH_NC_4_6 = ".*?<\/"
    //protected static final String TAG_SEARCH_NC_4_7 = "\"(?:[^<>\\/]*>|(?!([^<>]*\\/>))[^<>]*>))("
    //protected static final String TAG_SEARCH_NC_4_7_2 = "\")(?:[^<>\\/]*>|(?!([^<>]*\\/>))[^<>]*>))("

    //#find
    private static final String PATTERN_FIND_1 = "^([^,\\[\\]#\\.]+)$";
    private static final String PATTERN_FIND_2_1 = "^#([^\\.,\\[\\]#][^,\\[\\]#]*)$";
    private static final String PATTERN_FIND_2_2 = "^\\.([^\\.,\\[\\]#][^,\\[\\]#]*)$";
    private static final String PATTERN_FIND_2_3 = "^\\[([^\\[\\],]+)=([^\\[\\],]+)\\]$";
    private static final String PATTERN_FIND_3 = "^([^\\.,\\[\\]#][^,\\[\\]#]*)\\[([^,\\[\\]]+)=([^,\\[\\]]+)\\]$";
    private static final String PATTERN_FIND_4 = "^\\[([^,]+)=([^,]+)\\]\\[([^,]+)=([^,]+)\\]$";
    private static final String PATTERN_FIND_5 = "^([^\\.,\\[\\]#][^,\\[\\]#]*)\\[([^,]+)=([^,]+)\\]\\[([^,]+)=([^,]+)\\]$";

    private static final Pattern pattern_find_1 = Pattern.compile(PATTERN_FIND_1);
    private static final Pattern pattern_find_2_1 = Pattern.compile(PATTERN_FIND_2_1);
    private static final Pattern pattern_find_2_2 = Pattern.compile(PATTERN_FIND_2_2);
    private static final Pattern pattern_find_2_3 = Pattern.compile(PATTERN_FIND_2_3);
    private static final Pattern pattern_find_3 = Pattern.compile(PATTERN_FIND_3);
    private static final Pattern pattern_find_4 = Pattern.compile(PATTERN_FIND_4);
    private static final Pattern pattern_find_5 = Pattern.compile(PATTERN_FIND_5);

    //attribute
    protected static final String SET_ATTR_1 = "=\"[^\"]*\"";
    //attribute
    protected static final String GET_ATTR_1 = "=\"([^\"]*)\"";
    //attributeMap
    //todo
    protected static final String GET_ATTRS_MAP = "([^\\s]*)=\"([^\"]*)\"";
    //removeAttribute
    protected static final String ERASE_ATTR_1 = "=\"[^\"]*\"\\s";

    //cxTag
    protected static final String SEARCH_CX_1 = "<!--\\s@";
    protected static final String SEARCH_CX_2 = "\\s([^<>]*id=\"";
    protected static final String SEARCH_CX_3 = "\"[^<>]*)-->(((?!(<!--\\s\\/@";
    protected static final String SEARCH_CX_4 = "))[\\w\\W])*)<!--\\s\\/@";
    protected static final String SEARCH_CX_5 = "\\s-->";
    protected static final String SEARCH_CX_6 = "<!--\\s@([^<>]*)\\s[^<>]*id=\"";

    protected static final String SET_CX_1 = "<!-- @";
    protected static final String SET_CX_2 = "-->";
    protected static final String SET_CX_3 = "<!-- /@";
    protected static final String SET_CX_4 = " -->";

    //setMonoInfo
    protected static final String SET_MONO_1 = "[^<>]*";

    private static final Pattern pattern_set_mono1 = Pattern.compile(SET_MONO_1);

    //clean
    protected static final String CLEAN_1 = "<!--\\s@[^<>]*\\s[^<>]*(\\s)*-->";
    protected static final String CLEAN_2 = "<!--\\s\\/@[^<>]*(\\s)*-->";
    //escape
    protected static final String AND_1 = "&";
    protected static final String AND_2 = "&amp;";
    protected static final String LT_1 = "<";
    protected static final String LT_2 = "&lt;";
    protected static final String GT_1 = ">";
    protected static final String GT_2 = "&gt;";
    protected static final String QO_2 = "&quot;";
    protected static final String AP_1 = "'";
    protected static final String AP_2 = "&apos;";
    protected static final String EN_1 = "\\\\";
    protected static final String EN_2 = "\\\\\\\\";
    protected static final String DOL_1 = "\\$";
    protected static final String DOL_2 = "\\\\\\$";
    protected static final String PLUS_1 = "\\+";
    protected static final String PLUS_2 = "\\\\\\+";


    protected static final Pattern pattern_get_attrs_map = Pattern.compile(GET_ATTRS_MAP);

    //todo
    protected static final String BRAC_OPEN_1 = "\\(";
    protected static final String BRAC_OPEN_2 = "\\\\\\(";
    protected static final String BRAC_CLOSE_1 = "\\)";
    protected static final String BRAC_CLOSE_2 = "\\\\\\)";
    protected static final String SBRAC_OPEN_1 = "\\[";
    protected static final String SBRAC_OPEN_2 = "\\\\\\[";
    protected static final String SBRAC_CLOSE_1 = "\\]";
    protected static final String SBRAC_CLOSE_2 = "\\\\\\]";
    protected static final String CBRAC_OPEN_1 = "\\{";
    protected static final String CBRAC_OPEN_2 = "\\\\\\{";
    protected static final String CBRAC_CLOSE_1 = "\\}";
    protected static final String CBRAC_CLOSE_2 = "\\\\\\}";
    protected static final String COMMA_1 = "\\.";
    protected static final String COMMA_2 = "\\\\\\.";
    protected static final String VLINE_1 = "\\|";
    protected static final String VLINE_2 = "\\\\\\|";
    protected static final String QMARK_1 = "\\?";
    protected static final String QMARK_2 = "\\\\\\?";
    protected static final String ASTERISK_1 = "\\*";
    protected static final String ASTERISK_2 = "\\\\\\*";

    private static final Pattern pattern_clean1 = Pattern.compile(CLEAN_1);
    private static final Pattern pattern_clean2 = Pattern.compile(CLEAN_2);

    private static final int ZERO = 0;
    //private static final int ONE = 1;
    //private static final int TWO = 2;
    //private static final int THREE = 3;
    private static final int FOUR = 4;
    //private static final int FIVE = 5;
    private static final int SIX = 6;
    //private static final int SEVEN = 7;

    protected String result = null;
    protected String pattern_cc = null;

    protected boolean res;
    protected boolean res1;
    protected boolean res2;

    private String pattern_cc_1;
    private String pattern_cc_1b;
    private String pattern_cc_1_1;
    private String pattern_cc_1_2;
    private String pattern_cc_2;
    private String pattern_cc_2_1;
    private String pattern_cc_2_2;

    protected int position = 0;
    protected int position2 = 0;

    protected String _elmName;
    protected String _attrName;
    protected String _attrValue;
    protected String _attrName1;
    protected String _attrValue1;
    protected String _attrName2;
    protected String _attrValue2;

    protected String _id;

    protected String _attributes;
    protected String _content;

    protected Element elm_;

    public Kernel() {
        root = new RootElement();
    }

    protected final void document(String document) {
        root.document(document);
    }

    public String document() {
        return root.document();
    }


    protected void size(int size) {
        root.hookDocument().setLength(size);
    }

    protected void setCharacterEncoding(String enc) {
        this.root.setCharacterEncoding(enc);
    }

    /**
     * 文字エンコーディングを取得する
     *
     * @return 文字エンコーディング
     */
    protected final String getCharacterEncoding() {
        return this.root.characterEncoding();
    }

    /**
     * ルート要素を取得する
     *
     * @return ルート要素
     */
    public RootElement rootElement() {
        return root;
    }

    /**
     * 要素キャッシュを取得する
     *
     * @return 要素キャッシュ
     */
    public final LinkedHashMap<Integer, Element> elementCache() {
        return elementCache;
    }

    public final int docType() {
        return docType;
    }


    /**
     * ファイルを読み込み、パースする
     *
     * @param filePath 入力ファイルの絶対パス
     * @param encoding 入力ファイルの文字コード
     */
    protected void read(String filePath, String encoding) {

        try {
            setCharacterEncoding(encoding);
            //ファイルのオープン
            FileInputStream fis = new FileInputStream(filePath);

            FileChannel channel = fis.getChannel();
            ByteBuffer bbuf = ByteBuffer.allocate((int) channel.size());
            //読込及び格納
            channel.read(bbuf);
            bbuf.clear();
            byte[] bytes = new byte[bbuf.capacity()];
            bbuf.get(bytes);
            channel.close();
            //ファイルのクローズ
            fis.close();

            this.document(new String(bytes, encoding));

        } catch (FileNotFoundException e) {
            //FileNotFoundException時の処理
            e.printStackTrace();
            this.document(EMPTY);
        } catch (Exception e) {
            //上記以外の例外時の処理
            e.printStackTrace();
            this.document(EMPTY);
        }
        //this.document(sb.toString());

        //System.out.println(this.document());
    }

    /**
     * 要素をコピーする
     * @param elm 要素
     * @return 要素
     */
    public Element element(Element elm){
        return shadow(elm);    
    }


    /**
     * 要素名で要素を検索する
     *
     * @param elmName 要素名
     * @return 要素
     */
    public Element element(String elmName) {

        //要素名にポートしていない文字が含まれる場合
        _elmName = escapeRegex(elmName);

        //空要素タグ検索用パターン
        sbuf.setLength(0);
        pattern_cc_1 = sbuf.append(TAG_OPEN).append(_elmName)
                .append(TAG_SEARCH_1_3).toString();
        pattern = PatternCache.get(pattern_cc_1);
        //空要素タグ検索
        matcher1 = pattern.matcher(this.document());
        res1 = matcher1.find();

        sbuf.setLength(0);
        pattern_cc_2 = sbuf.append(TAG_OPEN).append(_elmName)
                .append(TAG_SEARCH_1_1).append(elmName).append(TAG_SEARCH_1_2).
                append(_elmName).append(TAG_CLOSE).toString();
        pattern = PatternCache.get(pattern_cc_2);
        //要素ありタグ検索
        matcher2 = pattern.matcher(this.document());
        res2 = matcher2.find();

        if (res1 && res2) {
            if (matcher1.end() < matcher2.end()) {
                matcher = matcher1;
                pattern_cc = pattern_cc_1;
                elementWithout(elmName);

            } else if (matcher1.end() > matcher2.end()) {
                matcher = matcher2;
                pattern_cc = pattern_cc_2;
                elementWith(elmName);
            }
        } else if (res1 && !res2) {
            matcher = matcher1;
            pattern_cc = pattern_cc_1;
            elementWithout(elmName);
        //} else if (!res1 && res2) {
        } else if (res2) {
            matcher = matcher2;
            pattern_cc = pattern_cc_2;
            elementWith(elmName);
        } else {
            elm_ = null;
            throw new NoSuchElementException(elmName);
        }

        //初期化
        //matcher.reset();
        if (elm_ != null) {
            elm_.objectId(++counter);
            elementCache.put(elm_.objectId(), elm_);
        }

        return elm_;
    }

    protected Element elementWith(String elmName) {

        elm_ = new Element(elmName);
        //属性
        elm_.attributes(matcher.group(1));
        //内容
        elm_.mixedContent(matcher.group(2));
        //全体
        elm_.document(matcher.group(0));

        //要素ありタグ検索用パターン
        elm_.pattern(pattern_cc);

        //パーサ
        elm_.parser(this);

        return elm_;
    }

    protected Element elementWithout(String elmName) {

        elm_ = new Element(elmName);
        //属性
        elm_.attributes(matcher.group(1));
        //全体
        elm_.document(matcher.group(0));
        //空要素タグ検索用パターン
        elm_.pattern(pattern_cc);
        //パーサ
        elm_.parser(this);

        return elm_;
    }

    /**
     * 要素名と属性で要素を検索する
     *
     * @param elmName   要素の名前
     * @param attrName  属性名
     * @param attrValue 属性値
     * @return 要素
     */
    public Element element(String elmName, String attrName, String attrValue) {

        //要素名にサポートしていない文字が含まれる場合
        _elmName = escapeRegex(elmName);
        //属性名にサポートしていない文字が含まれる場合
        _attrName = escapeRegex(attrName);
        //属性値にサポートしていない文字が含まれる場合
        _attrValue = escapeRegex(attrValue);

        //空要素検索用パターン
        sbuf.setLength(0);
        pattern_cc_1 = sbuf.append(TAG_OPEN).append(_elmName).append(TAG_SEARCH_2_1)
                .append(_attrName).append(ATTR_EQ).append(_attrValue).append(TAG_SEARCH_2_3_2)
                .toString();

        pattern = PatternCache.get(pattern_cc_1);
        //空要素検索
        matcher1 = pattern.matcher(this.document());
        res1 = matcher1.find();

        //内容あり要素検索パターン
        sbuf.setLength(0);
        pattern_cc_2 = sbuf.append(TAG_OPEN).append(_elmName).append(TAG_SEARCH_2_1)
                .append(_attrName).append(ATTR_EQ).append(_attrValue)
                .append(TAG_SEARCH_2_2).append(_elmName).append(TAG_SEARCH_1_2)
                .append(_elmName).append(TAG_CLOSE).toString();


        pattern = PatternCache.get(pattern_cc_2);
        //内容あり要素検索
        matcher2 = pattern.matcher(this.document());
        res2 = matcher2.find();

        //System.out.println(elmName + ":" + attrName + ":" + attrValue);
        //System.out.println(res2);

        if (!res2) {
            res2 = elementWith_3_2();
            matcher2 = matcher;
            pattern_cc_2 = pattern_cc;
        }

        //System.out.println("2:" + res2);

        if (res1 && res2) {
            if (matcher1.start(0) < matcher2.start(0)) {
                matcher = matcher1;
                pattern_cc = pattern_cc_1;
                elementWithout_3(elmName);
            } else if (matcher1.start(0) > matcher2.start(0)) {
                matcher = matcher2;
                pattern_cc = pattern_cc_2;
                elementWith_3_1(elmName);
            }
        } else if (res1 && !res2) {
            matcher = matcher1;
            pattern_cc = pattern_cc_1;
            elementWithout_3(elmName);
        //} else if (!res1 && res2) {
        } else if (res2) {
            matcher = matcher2;
            pattern_cc = pattern_cc_2;
            elementWith_3_1(elmName);
        //} else if (!res1 && !res2) {
        } else {
            elm_ = null;
            throw new NoSuchElementException(elmName, attrName, attrValue);
        }

        if (elm_ != null) {
            elm_.objectId(++counter);
            elementCache.put(elm_.objectId(), elm_);
        }

        return elm_;
    }

    protected Element elementWith_3_1(String elmName) {
        if (matcher.groupCount() == FOUR) {
            //要素
            elm_ = new Element(elmName);
            //属性
            elm_.attributes(matcher.group(1));
            //内容
            elm_.mixedContent(matcher.group(2));
            //全体
            elm_.document(matcher.group(0));
            //内容あり要素検索用パターン
            sbuf.setLength(0);
            pattern_cc = sbuf.append(TAG_OPEN).append(_elmName).append(TAG_SEARCH_NC_2_1)
                    .append(_attrName).append(ATTR_EQ).append(_attrValue)
                    .append(TAG_SEARCH_NC_2_2).append(_elmName).append(TAG_SEARCH_NC_1_2)
                    .append(_elmName).append(TAG_CLOSE).toString();

            elm_.pattern(pattern_cc);

            elm_.parser(this);

        } else if (matcher.groupCount() == SIX) {
            //要素
            elm_ = new Element(elmName);
            //属性
            elm_.attributes(matcher.group(1)); //.chop
            //内容
            elm_.mixedContent(matcher.group(3));
            //全体
            elm_.document(matcher.group(0));
            //内容あり要素検索用パターン
            elm_.pattern(pattern_cc);

            elm_.parser(this);
        }
        return elm_;

    }


    protected boolean elementWith_3_2() {

        sbuf.setLength(0);
        pattern_cc_1 = sbuf.append(TAG_OPEN).append(_elmName)
                .append(TAG_SEARCH_2_1).append(_attrName).append(ATTR_EQ)
                .append(_attrValue).append(TAG_SEARCH_2_4_2).toString();

        sbuf.setLength(0);
        pattern_cc_1b = sbuf.append(TAG_OPEN).append(_elmName)
                .append(TAG_SEARCH_1_4).toString();

        sbuf.setLength(0);
        pattern_cc_1_1 = sbuf.append(TAG_OPEN).append(_elmName)
                .append(TAG_SEARCH_2_1).append(_attrName).append(ATTR_EQ)
                .append(_attrValue).append(TAG_SEARCH_4_7).toString();

        sbuf.setLength(0);
        pattern_cc_1_2 = sbuf.append(TAG_SEARCH_4_2).append(_elmName)
                .append(TAG_SEARCH_4_3).toString();

        sbuf.setLength(0);
        pattern_cc_2 = sbuf.append(TAG_SEARCH_4_4).append(_elmName).append(TAG_CLOSE).toString();

        sbuf.setLength(0);
        pattern_cc_2_1 = sbuf.append(TAG_SEARCH_4_5).append(_elmName).append(TAG_CLOSE).toString();

        sbuf.setLength(0);
        pattern_cc_2_2 = sbuf.append(TAG_SEARCH_4_6).append(_elmName).append(TAG_CLOSE).toString();

        pattern = PatternCache.get(pattern_cc_1);
        //要素ありタグ検索
        matcher = pattern.matcher(this.document());

        sbuf.setLength(0);

        pattern_2 = PatternCache.get(pattern_cc_2);
        pattern_1b = PatternCache.get(pattern_cc_1b);


        int cnt = 0;

        cnt = create_element_pattern(cnt);

        pattern_cc = sbuf.toString();

        //todo
        //System.out.println("[" + pattern_cc + "]");
        //System.out.println("TestPRE");


        if (pattern_cc.length() == ZERO || cnt != 0) {
            //return null;
            elm_ = null;
            //throw new NoSuchElementException(elmName,attrName,attrValue);
            return false;
        }

        pattern = PatternCache.get(pattern_cc);
        matcher = pattern.matcher(this.document());

        res = matcher.find();

        return res;
    }

    protected Element elementWithout_3(String elmName) {

        return _elementWithout_3_1(elmName, TAG_SEARCH_NC_2_3_2);
    }

    protected final Element _elementWithout_3_1(String elmName, String closer) {
        //要素
        elm_ = new Element(elmName);
        //属性
        elm_.attributes(matcher.group(1));
        //空要素タグ検索用パターン
        sbuf.setLength(0);
        pattern_cc = sbuf.append(TAG_OPEN).append(_elmName).append(TAG_SEARCH_NC_2_1)
                .append(_attrName).append(ATTR_EQ).append(_attrValue).
                append(closer).toString();
        elm_.pattern(pattern_cc);
        //パーサ
        elm_.parser(this);


        return elm_;
    }

    /**
     * 属性(属性名="属性値")で要素を検索する
     *
     * @param attrName  属性名
     * @param attrValue 属性値
     * @return 要素
     */
    public Element element(String attrName, String attrValue) {

        //属性名にサポートしていない文字が含まれる場合
        _attrName = escapeRegex(attrName);

        //属性値にサポートしていない文字が含まれる場合
        _attrValue = escapeRegex(attrValue);

        sbuf.setLength(0);
        pattern_cc = sbuf.append(TAG_SEARCH_3_1).append(_attrName).append(ATTR_EQ)
                .append(_attrValue).append(TAG_SEARCH_2_4_2_3).toString();
        pattern = PatternCache.get(pattern_cc);

        matcher = pattern.matcher(this.document());

        if (matcher.find()) {
            element(matcher.group(1), attrName, attrValue);
        } else {
            elm_ = null;
            throw new NoSuchElementException(attrName, attrValue);
        }

        //初期化
        if (elm_ != null) {
            elm_.objectId(++counter);
            elementCache.put(elm_.objectId(), elm_);
        }

        return elm_;
    }

    /**
     * 要素名と属性1と属性2で要素を検索する
     *
     * @param elmName    要素の名前
     * @param attrName1  属性名1
     * @param attrValue1 属性値2
     * @param attrName2  属性名2
     * @param attrValue2 属性値2
     * @return 要素
     */
    public Element element(String elmName,
                           String attrName1, String attrValue1, String attrName2, String attrValue2) {

        //要素名にサポートしていない文字が含まれる場合
        _elmName = escapeRegex(elmName);
        //属性名にサポートしていない文字が含まれる場合
        _attrName1 = escapeRegex(attrName1);
        //属性名にサポートしていない文字が含まれる場合
        _attrName2 = escapeRegex(attrName2);
        //属性値にサポートしていない文字が含まれる場合
        _attrValue1 = escapeRegex(attrValue1);
        //属性値にサポートしていない文字が含まれる場合
        _attrValue2 = escapeRegex(attrValue2);

        //空要素検索用パターン
        sbuf.setLength(0);
        pattern_cc_1 = sbuf.append(TAG_OPEN).append(_elmName).append(TAG_SEARCH_2_1_2)
                .append(_attrName1).append(ATTR_EQ).append(_attrValue1).append(TAG_SEARCH_2_6)
                .append(_attrName2).append(ATTR_EQ).append(_attrValue2).append(TAG_SEARCH_2_7)
                .append(_attrName2).append(ATTR_EQ).append(_attrValue2).append(TAG_SEARCH_2_6)
                .append(_attrName1).append(ATTR_EQ).append(_attrValue1).append(TAG_SEARCH_2_3_2_2)
                .toString();

        pattern = PatternCache.get(pattern_cc_1);
        //空要素検索
        matcher1 = pattern.matcher(root.document());
        res1 = matcher1.find();

        //内容あり要素検索パターン
        sbuf.setLength(0);
        pattern_cc_2 = sbuf.append(TAG_OPEN).append(_elmName).append(TAG_SEARCH_2_1_2)
                .append(_attrName1).append(ATTR_EQ).append(_attrValue1).append(TAG_SEARCH_2_6)
                .append(_attrName2).append(ATTR_EQ).append(_attrValue2).append(TAG_SEARCH_2_7)
                .append(_attrName2).append(ATTR_EQ).append(_attrValue2).append(TAG_SEARCH_2_6)
                .append(_attrName1).append(ATTR_EQ).append(_attrValue1).append(TAG_SEARCH_2_2_2)
                .append(_elmName).append(TAG_SEARCH_1_2).append(_elmName).append(TAG_CLOSE)
                .toString();

        pattern = PatternCache.get(pattern_cc_2);
        //内容あり要素検索
        matcher2 = pattern.matcher(root.document());
        res2 = matcher2.find();

        if (!res2) {
            res2 = elementWith_5_2();
            matcher2 = matcher;
            pattern_cc_2 = pattern_cc;
        }

        if (res1 && res2) {
            if (matcher1.start(0) < matcher2.start(0)) {
                matcher = matcher1;
                pattern_cc = pattern_cc_1;
                elementWithout_5(elmName);
            } else if (matcher1.start(0) > matcher2.start(0)) {
                matcher = matcher2;
                pattern_cc = pattern_cc_2;
                elementWith_5_1(elmName);
            }
        } else if (res1 && !res2) {
            matcher = matcher1;
            pattern_cc = pattern_cc_1;
            elementWithout_5(elmName);
        //} else if (!res1 && res2) {
        } else if (res2) {
            matcher = matcher2;
            pattern_cc = pattern_cc_2;
            elementWith_5_1(elmName);
        } else {
            elm_ = null;
            throw new NoSuchElementException(elmName, attrName1, attrValue1, attrName2, attrValue2);
        }

        if (elm_ != null) {
            elm_.objectId(++counter);
            elementCache.put(elm_.objectId(), elm_);
        }

        return elm_;
    }

    protected Element elementWith_5_1(String elmName) {
        if (matcher.groupCount() == FOUR) {
            //要素
            elm_ = new Element(elmName);
            //属性
            elm_.attributes(matcher.group(1));
            //内容
            elm_.mixedContent(matcher.group(2));
            //全体
            elm_.document(matcher.group(0));
            //内容あり要素検索用パターン
            sbuf.setLength(0);
            pattern_cc = sbuf.append(TAG_OPEN).append(_elmName).append(TAG_SEARCH_NC_2_1_2)
                    .append(_attrName1).append(ATTR_EQ).append(_attrValue1).append(TAG_SEARCH_NC_2_6)
                    .append(_attrName2).append(ATTR_EQ).append(_attrValue2).append(TAG_SEARCH_NC_2_7)
                    .append(_attrName2).append(ATTR_EQ).append(_attrValue2).append(TAG_SEARCH_NC_2_6)
                    .append(_attrName1).append(ATTR_EQ).append(_attrValue1).append(TAG_SEARCH_NC_2_2_2)
                    .append(_elmName).append(TAG_SEARCH_NC_1_2).append(_elmName).append(TAG_CLOSE).toString();

            elm_.pattern(pattern_cc);

            elm_.parser(this);

        } else if (matcher.groupCount() == SIX) {
            //要素
            elm_ = new Element(elmName);
            //属性
            elm_.attributes(matcher.group(1)); //.chop
            //内容
            elm_.mixedContent(matcher.group(3));
            //全体
            elm_.document(matcher.group(0));
            //内容あり要素検索用パターン
            elm_.pattern(pattern_cc);

            elm_.parser(this);
        }
        return elm_;

    }


    protected boolean elementWith_5_2() {

        sbuf.setLength(0);
        pattern_cc_1 = sbuf.append(TAG_OPEN).append(_elmName)
                .append(TAG_SEARCH_2_1_2).append(_attrName1).append(ATTR_EQ)
                .append(_attrValue1).append(TAG_SEARCH_2_6).append(_attrName2).append(ATTR_EQ)
                .append(_attrValue2).append(TAG_SEARCH_2_7).append(_attrName2).append(ATTR_EQ)
                .append(_attrValue2).append(TAG_SEARCH_2_6).append(_attrName1).append(ATTR_EQ)
                .append(_attrValue1).append(TAG_SEARCH_2_4_2_2).toString();

        //System.out.println("5[" + pattern_cc_1 +"]");

        sbuf.setLength(0);
        pattern_cc_1b = sbuf.append(TAG_OPEN).append(_elmName)
                .append(TAG_SEARCH_1_4).toString();

        sbuf.setLength(0);
        pattern_cc_1_1 = sbuf.append(TAG_OPEN).append(_elmName)
                .append(TAG_SEARCH_2_1_2).append(_attrName1).append(ATTR_EQ)
                .append(_attrValue1).append(TAG_SEARCH_2_6).append(_attrName2).append(ATTR_EQ)
                .append(_attrValue2).append(TAG_SEARCH_2_7).append(_attrName2).append(ATTR_EQ)
                .append(_attrValue2).append(TAG_SEARCH_2_6).append(_attrName1).append(ATTR_EQ)
                .append(_attrValue1).append(TAG_SEARCH_4_7_2).toString();

        //System.out.println("5[" + pattern_cc_1_1 +"]");

        sbuf.setLength(0);
        pattern_cc_1_2 = sbuf.append(TAG_SEARCH_4_2).append(_elmName)
                .append(TAG_SEARCH_4_3).toString();

        sbuf.setLength(0);
        pattern_cc_2 = sbuf.append(TAG_SEARCH_4_4).append(_elmName).append(TAG_CLOSE).toString();

        sbuf.setLength(0);
        pattern_cc_2_1 = sbuf.append(TAG_SEARCH_4_5).append(_elmName).append(TAG_CLOSE).toString();

        sbuf.setLength(0);
        pattern_cc_2_2 = sbuf.append(TAG_SEARCH_4_6).append(_elmName).append(TAG_CLOSE).toString();

        pattern = PatternCache.get(pattern_cc_1);
        //要素ありタグ検索
        matcher = pattern.matcher(this.document());

        sbuf.setLength(0);

        pattern_2 = PatternCache.get(pattern_cc_2);
        pattern_1b = PatternCache.get(pattern_cc_1b);


        int cnt = 0;

        cnt = create_element_pattern(cnt);

        pattern_cc = sbuf.toString();

        //todo
        //System.out.println("[" + pattern_cc + "]");
        //System.out.println("TestPRE");


        if (pattern_cc.length() == ZERO || cnt != 0) {
            //return null;
            elm_ = null;
            //throw new NoSuchElementException(elmName,attrName,attrValue);
            return false;
        }

        pattern = PatternCache.get(pattern_cc);
        matcher = pattern.matcher(this.document());

        res = matcher.find();

        return res;
    }

    protected Element elementWithout_5(String elmName) {

        return _elementWithout_5_1(elmName, TAG_SEARCH_2_3_2);
    }

    protected final Element _elementWithout_5_1(String elmName, String closer) {
        //要素
        elm_ = new Element(elmName);
        //属性
        elm_.attributes(matcher.group(1));
        //空要素タグ検索用パターン
        sbuf.setLength(0);
        pattern_cc = sbuf.append(TAG_OPEN).append(_elmName).append(TAG_SEARCH_NC_2_1_2)
                .append(_attrName1).append(ATTR_EQ).append(_attrValue1).append(TAG_SEARCH_NC_2_6)
                .append(_attrName2).append(ATTR_EQ).append(_attrValue2).append(TAG_SEARCH_NC_2_7)
                .append(_attrName2).append(ATTR_EQ).append(_attrValue2).append(TAG_SEARCH_NC_2_6)
                .append(_attrName1).append(ATTR_EQ).append(_attrValue1).append(closer).toString();
        elm_.pattern(pattern_cc);
        //パーサ
        elm_.parser(this);


        return elm_;
    }

    /**
     * 属性1と属性2(属性名="属性値")で要素を検索する
     *
     * @param attrName1  属性名1
     * @param attrValue1 属性値1
     * @param attrName2  属性名2
     * @param attrValue2 属性値2
     * @return 要素
     */
    public Element element(String attrName1, String attrValue1,
                           String attrName2, String attrValue2) {

        //属性名にサポートしていない文字が含まれる場合
        _attrName1 = escapeRegex(attrName1);
        //属性名にサポートしていない文字が含まれる場合
        _attrName2 = escapeRegex(attrName2);
        //属性値にサポートしていない文字が含まれる場合
        _attrValue1 = escapeRegex(attrValue1);
        //属性値にサポートしていない文字が含まれる場合
        _attrValue2 = escapeRegex(attrValue2);

        sbuf.setLength(0);
        pattern_cc = sbuf.append(TAG_SEARCH_3_1_2_2).append(_attrName1).append(ATTR_EQ)
                .append(_attrValue1).append(TAG_SEARCH_2_6).append(_attrName2).append(ATTR_EQ)
                .append(_attrValue2).append(TAG_SEARCH_2_7).append(_attrName2).append(ATTR_EQ)
                .append(_attrValue2).append(TAG_SEARCH_2_6).append(_attrName1).append(ATTR_EQ)
                .append(_attrValue1).append(TAG_SEARCH_2_4_2_3).toString();
        pattern = PatternCache.get(pattern_cc);

        matcher = pattern.matcher(this.document());

        if (matcher.find()) {
            element(matcher.group(1), attrName1, attrValue1, attrName2, attrValue2);
        } else {
            elm_ = null;
            throw new NoSuchElementException(attrName1, attrValue1, attrValue1, attrValue2);
        }

        //初期化
        if (elm_ != null) {
            elm_.objectId(++counter);
            elementCache.put(elm_.objectId(), elm_);
        }

        return elm_;
    }

    private int create_element_pattern(int cnt) {
        boolean res;

        position = 0;
        //position2=0;

        while ((res = matcher.find(position)) || cnt > 0) {
            if (res) {

                if (cnt > 0) {

                    position2 = matcher.end();
                    matcher = pattern_2.matcher(this.document());

                    res = matcher.find(position);

                    if (res) {

                        position = matcher.end();

                        if (position > position2) {

                            sbuf.append(pattern_cc_1_2);

                            cnt += 1;

                            position = position2;
                        } else {
                            cnt -= 1;

                            if (cnt != 0) {
                                sbuf.append(pattern_cc_2_1);
                            } else {
                                sbuf.append(pattern_cc_2_2);
                                break;
                            }
                        }
                    } else {

                        sbuf.append(pattern_cc_1_2);

                        cnt += 1;

                        position = position2;
                    }
                } else {
                    position = matcher.end();

                    sbuf.append(pattern_cc_1_1);

                    cnt += 1;
                }
            } else {
                if (cnt == 0) break;

                pattern = PatternCache.get(pattern_cc_2);
                matcher = pattern.matcher(this.document());

                res = matcher.find(position);

                if (res) {
                    cnt -= 1;

                    if (cnt != 0) {
                        sbuf.append(pattern_cc_2_1);
                    } else {
                        sbuf.append(pattern_cc_2_2);
                        break;
                    }

                    position = matcher.end();
                } else {
                    break;
                }
            }
            matcher = pattern_1b.matcher(this.document());

        }

        return cnt;

    }

    /**
     * セレクタで要素を検索する
     * @param selector セレクタ
     * @return 要素
     */
    public final Element find(String selector){

        if ((matcher = pattern_find_1.matcher(selector)).matches()) {
          element(matcher.group(1));
          if (elm_ != null) {
            elementCache.put(elm_.objectId(),elm_);
          }
        } else if ((matcher = pattern_find_2_1.matcher(selector)).matches()) {
          element("id",matcher.group(1));
          if (elm_ != null) {
            elementCache.put(elm_.objectId(),elm_);
          }
        } else if ((matcher = pattern_find_3.matcher(selector)).matches()) {
          element(matcher.group(1),matcher.group(2),matcher.group(3));
          if (elm_ != null) {
            elementCache.put(elm_.objectId(),elm_);
          }
        } else if ((matcher = pattern_find_5.matcher(selector)).matches()) {
          element(matcher.group(1),matcher.group(2),matcher.group(3),
                  matcher.group(4),matcher.group(5));
          if (elm_ != null) {
            elementCache.put(elm_.objectId(),elm_);
          }
        } else if ((matcher = pattern_find_2_3.matcher(selector)).matches()) {
          element(matcher.group(1),matcher.group(2));
          if (elm_ != null) {
            elementCache.put(elm_.objectId(),elm_);
          }
        } else if ((matcher = pattern_find_4.matcher(selector)).matches()) {
          element(matcher.group(1),matcher.group(2),matcher.group(3),
            matcher.group(4));
          if (elm_ != null) {
            elementCache.put(elm_.objectId(),elm_);
          }
        } else if ((matcher = pattern_find_2_2.matcher(selector)).matches()) {
          element("class",matcher.group(1));
          if (elm_ != null) {
            elementCache.put(elm_.objectId(),elm_);
          }
        } else {
          elm_ = null;
        }
        return elm_;
    }



    /**
     * 要素の属性を編集する
     *
     * @param elm       要素
     * @param attrName  属性名
     * @param attrValue 属性値
     */
    public Element attribute(Element elm, String attrName, String attrValue) {

        if (!elm.cx()) {
            attrValue = escape(attrValue);

            //属性群の更新
            editAttributes_(elm, attrName, attrValue);
        }
        return elm;
    }

    protected void editAttributes_(Element elm, String attrName, String attrValue) {
        attrValue = escape(attrValue);
        //属性検索用パターン
        //sbuf.setLength(0);
        //pattern = PatternCache.get(sbuf.append(attrName).append(SET_ATTR_1)
        //        .toString());

        //System.out.println(elm.attributes());
        //属性検索
        //matcher = pattern.matcher(elm.attributes());

        sbuf.setLength(0);
        //検索対象属性の存在判定
        //if (matcher.find()) {
        if (elm.attributes().indexOf(sbuf.append(SPACE).append(attrName)
                .append(ATTR_EQ).toString()) >= 0) {
            //todo
            //System.out.println(matcher.group(0));

            //attrValue = escapeRegex(attrValue);
            sbuf.setLength(0);
            pattern = PatternCache.get(sbuf.append(attrName).append(SET_ATTR_1)
                    .toString());
            matcher = pattern.matcher(elm.attributes());

            //属性の置換
            sbuf.setLength(0);
            elm.attributes(matcher.replaceFirst(sbuf.append(attrName)
                    .append(ATTR_EQ).append(attrValue).append(DOUBLE_QUATATION)
                    .toString()));
            //attrValue = escapeRegex(attrValue);
            //todo
            //System.out.println("res1:" + result);

        } else {
            //属性文字列の最後に新規の属性を追加する
            if (!EMPTY.equals(elm.attributes()) && !EMPTY.equals(elm.attributes().trim())) {
                sbuf.setLength(0);
                elm.attributes(sbuf.append(SPACE).append(elm.attributes().trim())
                        .toString());
            } else {
                elm.attributes(EMPTY);
            }
            sbuf.setLength(0);
            elm.attributes(sbuf.append(elm.attributes()).append(SPACE).append(attrName)
                    .append(ATTR_EQ).append(attrValue).append(DOUBLE_QUATATION)
                    .toString());
        }
    }

    protected void editDocument_(Element elm) {
        editDocument_(elm, TAG_CLOSE3);
    }

    protected final void editDocument_(Element elm, String closer) {
        //result = escapeRegex(elm.attributes());
        if (!elm.cx()) {
            _attributes = elm.attributes();

            //System.out.println("res:" + result);

            if (elm.empty()) {
                //要素ありタグの場合
                _content = elm.mixedContent();

                sbuf.setLength(0);
                elm.document(sbuf.append(TAG_OPEN).append(elm.name()).append(elm.attributes())
                        .append(TAG_CLOSE).append(_content).append(TAG_OPEN3).append(elm.name())
                        .append(TAG_CLOSE).toString());

            } else {
                //空要素タグの場合
                sbuf.setLength(0);
                elm.document(sbuf.append(TAG_OPEN).append(elm.name()).append(elm.attributes())
                        .append(closer).toString());
            }
        } else {
            _attributes = elm.attributes();
            _content = elm.mixedContent();

            sbuf.setLength(0);
            elm.document(sbuf.append(SET_CX_1).append(elm.name()).append(SPACE)
                    .append(_attributes).append(SET_CX_2).append(_content)
                    .append(SET_CX_3).append(elm.name()).append(SET_CX_4).toString());

        }

        //タグ置換
        pattern = PatternCache.get(elm.pattern());
        matcher = pattern.matcher(root.document());
        root.document(matcher.replaceFirst(elm.document()));
    }

    /*
    protected final void editPattern_(Element elm,String attrName,String attrValue){

        sbuf.setLength(0);
        pattern_cc = sbuf.append(attrName).append(SET_ATTR_1).toString();
        pattern = PatternCache.get(pattern_cc);
        matcher = pattern.matcher(elm.pattern());
        sbuf.setLength(0);
        elm.pattern(matcher.replaceFirst(sbuf.append(attrName).append(ATTR_EQ).append(attrValue)
                .append(DOUBLE_QUATATION).toString()));
    }
    */

    /**
     * 要素を属性名で検索し、属性値を得る
     *
     * @param elm      要素
     * @param attrName 属性名
     * @return 属性値
     */
    public String attribute(Element elm, String attrName) {

        return getAttributeValue_(elm, attrName);
    }

    protected String getAttributeValue_(Element elm, String attrName) {

        //属性検索用パターン
        sbuf.setLength(0);
        pattern = PatternCache.get(sbuf.append(attrName).append(GET_ATTR_1)
                .toString());
        matcher = pattern.matcher(elm.attributes());

        if (matcher.find()) {
            return unescape(matcher.group(1));
        }

        return null;

    }

    /**
     * 属性マップを取得する
     *
     * @param elm 要素
     * @return 属性マップ
     */
    public AttributeMap attributeMap(Element elm) {

        AttributeMap attrs = new AttributeMap();

        matcher = pattern_get_attrs_map.matcher(elm.attributes());

        while (matcher.find()) {
            attrs.store(matcher.group(1), unescape(matcher.group(2)));
        }
        attrs.setRecordable(true);

        return attrs;
    }

    /**
     * 要素の属性マップをセットする
     * @param elm 要素
     * @param attrMap 属性マップ
     * @return 要素
     */
    public Element attributeMap(Element elm,AttributeMap attrMap){
        if(!elm.cx()){
            for(String name:attrMap.names()){
                if(attrMap.changed(name)){
                    editAttributes_(elm,name,attrMap.fetch(name));
                }else{
                    removeAttributes_(elm,name);
                }
            }
        }
        return elm;
    }

    /**
     * 要素の内容をセットする
     *
     * @param elm       要素
     * @param content   要素の内容
     * @param entityRef エンティティ参照フラグ
     */
    public Element content(Element elm, String content, boolean entityRef) {

        //if (!elm.cx()) {

        //要素ありタグの場合
        if (elm.empty()) {


            if (entityRef) {
                content = escapeContent(content, elm.name());
                //content = escapeRegex(content);
            } else {
                //content = escapeRegex(content);
            }

            elm.mixedContent(content);
        }
        
        return elm;
    }

    /**
     * 要素の内容をセットする
     *
     * @param elm     要素
     * @param content 要素の内容
     */
    public Element content(Element elm, String content) {
        elm.mixedContent(escapeContent(content, elm.name()));
        return elm;
    }

    /**
     * 要素の内容を取得する
     *
     * @param elm 要素
     * @return 要素の内容
     */
    public String content(Element elm) {
        result = null;

        if (!elm.cx()) {
            //要素ありタグの場合
            if (elm.empty()) {
                result = unescapeContent(elm.mixedContent(), elm.name());
            }
        } else {
            result = unescapeContent(elm.mixedContent(), elm.name());
        }

        return result;
    }

    /**
     * 要素の属性を消す
     *
     * @param elm      要素
     * @param attrName 属性名
     */
    public Element removeAttribute(Element elm, String attrName) {

        if (!elm.cx()) {
            //result = elm.attributes();

            elm.documentSync(true);
            removeAttributes_(elm, attrName);

        }
        return elm;
    }

    private void removeAttributes_(Element elm, String attrName) {
        //属性検索用パターン
        sbuf.setLength(0);
        pattern = PatternCache.get(sbuf.append(attrName).append(ERASE_ATTR_1)
                .toString());
        //属性検索
        matcher = pattern.matcher(elm.attributes());
        elm.attributes(matcher.replaceFirst(EMPTY));
    }

    /**
     * 要素を消す
     *
     * @param elm 要素
     */
    public Element removeElement(Element elm) {

        //replace(elm, EMPTY);
        //elm.usable(false);
        elm.removed(true);
        return null;

    }

    public Element cxTag(String elmName, String id) {

        _elmName = escapeRegex(id);
        _id = escapeRegex(id);

        //CXタグ検索用パターン
        sbuf.setLength(0);
        pattern_cc = sbuf.append(SEARCH_CX_1).append(_elmName).append(SEARCH_CX_2)
                .append(_id).append(SEARCH_CX_3).append(_elmName).append(SEARCH_CX_4)
                .append(_elmName).append(SEARCH_CX_5).toString();
        pattern = PatternCache.get(pattern_cc);
        //CXタグ検索
        matcher = pattern.matcher(this.document());
        if (matcher.find()) {
            elm_ = new Element(elmName);
            elm_.cx(true);
            elm_.attributes(matcher.group(1));
            elm_.mixedContent(matcher.group(2));
            elm_.document(matcher.group(0));
            elm_.pattern(pattern_cc);
            elm_.parser(this);
        }

        if (elm_ != null) {
            elm_.objectId(++counter);
            elementCache.put(elm_.objectId(), elm_);
        }

        return elm_;
    }

    /**
     * ID属性でCXタグを検索する
     *
     * @param id ID属性値
     * @return 要素
     */
    public Element cxTag(String id) {

        _id = escapeRegex(id);

        sbuf.setLength(0);
        pattern_cc = sbuf.append(SEARCH_CX_6).append(_id)
                .append(DOUBLE_QUATATION).toString();
        pattern = PatternCache.get(pattern_cc);
        matcher = pattern.matcher(this.document());

        if (matcher.find()) {
            elm_ = cxTag(matcher.group(1), id);
        }else{
            elm_ = null;    
        }

        if (elm_ != null) {
            elm_.objectId(++counter);
            elementCache.put(elm_.objectId(), elm_);
        }

        return elm_;
    }

    /**
     * 要素を置換する
     *
     * @param elm             要素
     * @param replaceDocument 置換文字列
     */
    private void replace(Element elm, String replaceDocument) {

        //文字コード変換
        //replaceDocument = escapeRegex(replaceDocument);

        //タグ置換パターン
        pattern = PatternCache.get(elm.pattern());
        //タグ置換
        matcher = pattern.matcher(this.document());
        this.document(matcher.replaceFirst(replaceDocument));

        //初期化
        //matcher.reset();
    }

    private void reflect() {
        Element item;
        for (Integer id : elementCache.keySet().toArray(new Integer[elementCache.size()])) {
            item = elementCache.get(id);
            //System.out.println("id:" + id);
            if (item.usable()) {
                if(!item.removed()){
                    //System.out.println(item.name());
                    if (item.copy() != null) {
                        //System.out.println("loop:" + item.name());
                        //System.out.println("pattern:[" + item.pattern() + "]");
                        //System.out.println("doc:[" + item.copy().parser().rootElement().hookDocument().toString() +"]");
                        pattern = PatternCache.get(item.pattern());
                        matcher = pattern.matcher(root.document());
                        root.document(matcher.replaceFirst(
                                item.copy().parser().rootElement().hookDocument().toString()));
                        //item.copy.parser.element_cache.clear
                        item.copy(null);
                    } else {
                        editDocument_(item);
                        //edit_pattern_(item)
                    }
                }else{
                    replace(item, EMPTY);
                }
                item.usable(false);
            }
        }

    }

    /**
     * XMLをコンソールに出力する
     */
    public void flush() {
        if (root.element() != null) {
            if (root.element().origin().mono()) {
                //フック判定がTRUEの場合
                if (root.element().cx()) {
                    //hookdocument().append(SET_QUARK_3).append(tag.attributes())
                    // .append(SET_QUARK_4).append(document()).append(SET_QUARK_5);
                    //this.root.setHookDocument(this.root.hookDocument().append(SET_CX_1).append(element().name())
                    //        .append(SPACE).append(element().attributes()).append(SET_CX_2)
                    //        .append(document()).append(SET_CX_3).append(element().name())
                    //        .append(SET_CX_4));
                    this.root.setHookDocument(this.root.hookDocument().append(SET_CX_1)
                            .append(this.root.element().name())
                            .append(SPACE).append(this.root.element().attributes()).append(SET_CX_2)
                            .append(root.element().mixedContent()).append(SET_CX_3)
                            .append(this.root.element().name())
                            .append(SET_CX_4));
                } else {
                    //this.root.setHookDocument(this.root.hookDocument().append(TAG_OPEN).append(element().name())
                    //        .append(element().attributes()).append(TAG_CLOSE).append(document())
                    //        .append(TAG_OPEN3).append(element().name()).append(TAG_CLOSE));
                    this.root.setHookDocument(this.root.hookDocument().append(TAG_OPEN)
                            .append(this.root.element().name())
                            .append(root.element().attributes()).append(TAG_CLOSE)
                            .append(root.element().mixedContent()).append(TAG_OPEN3).append(root.element().name()).append(TAG_CLOSE));
                }
                this.root.setElement(Element.new_(this.root.element().origin(), this));
            } else {
                reflect();

                if (root.element().origin().cx()) {
                    root.setHookDocument(this.root.hookDocument().append(SET_CX_1)
                            .append(root.element().name()).append(SPACE).append(this.root.element().attributes())
                            .append(SET_CX_2).append(root.document()).append(SET_CX_3)
                            .append(root.element().name()).append(SET_CX_4));
                } else {
                    root.setHookDocument(this.root.hookDocument().append(TAG_OPEN)
                            .append(root.element().name()).append(this.root.element().attributes()).append(TAG_CLOSE)
                            .append(root.document()).append(TAG_OPEN3).append(root.element().name())
                            .append(TAG_CLOSE));
                }
                root.setElement(Element.new_(root.element().origin(), this));

            }
        } else {
            reflect();
            elementCache.clear();
            //フック判定がFALSEの場合
            clean();
        }
    }

    /**
     * Hookerクラスの処理を実行する
     *
     * @param elm  要素
     * @param hook Hookerオブジェクト
     */
    public void execute(Element elm, Hooker hook) {
        //if(!element().cx()){
        //フッククラスのメソッドを呼び出す
        hook.doAction(elm);
        //}
    }

    /**
     * Looperクラスの処理を実行する
     *
     * @param elm  要素
     * @param hook Hookerオブジェクト
     * @param list Listオブジェクト
     */
    public void execute(Element elm, Looper hook, List list) {
        //if(!element().cx()){
        //フッククラスのメソッドを呼び出す
        hook.doAction(elm,list);
        //}
    }

    /**
     * 要素をコピーする
     *
     * @param elm 要素
     * @return 要素
     */
    private Element shadow(Element elm) {
        if (elm.empty()) {
            Parser pif2;

            setMonoInfo(elm);

            pif2 = create(this);

            if (elm.mono()) {
                //要素ありタグの場合

                pif2.rootElement().document(elm.document());
                //pif2.rootElement().setMonoHook(true);
                //return pif2;
            } else {

                pif2.rootElement().document(elm.mixedContent());
                //pif2.rootElement().setHook(true);
                //return pif2;
            }

            elm_ = Element.new_(elm, pif2);

            return elm_;
        }

        return null;
    }

    private void setMonoInfo(Element elm) {
        boolean res;

        matcher = pattern_set_mono1.matcher(elm.mixedContent());
        res = matcher.matches();

        if (res) {
            elm.mono(true);
        }
    }

    protected final void clean() {

        //CX開始タグ置換
        pattern = pattern_clean1;
        matcher = pattern.matcher(document());
        this.document(matcher.replaceAll(EMPTY));
        //CX終了タグ置換
        pattern = pattern_clean2;
        matcher = pattern.matcher(document());
        this.document(matcher.replaceAll(EMPTY));

        //sbuf.setLength(0);
        //this.document(document() + "<!-- Powered by Meteor (C)Yasumasa Ashida -->");
        //初期化
        //matcher.reset();
    }

    /**
     * 正規表現対象文字を変換する
     *
     * @param str 入力文字列
     * @return 出力文字列
     */
    protected final String escapeRegex(String str) {
        //「\」->[\\]
        //matcher＿ = pattern_en.matcher(str);
        //str = matcher＿.replaceAll(EN_2);
        //「$」->「\$」
        //matcher＿ = pattern_dol.matcher(str);
        //str = matcher＿.replaceAll(DOL_2);
        //「+」->「\+」
        //matcher＿ = pattern_plus.matcher(str);
        //str = matcher＿.replaceAll(PLUS_2);

        //todo
        /* 2009/02/07
        //2005.10.17 ADD START
        //「(」->「\(」
        matcher＿ = pattern_brac_open.matcher(str);
        str = matcher＿.replaceAll(BRAC_OPEN_2);
        //「)」->「\)」
        matcher＿ = pattern_brac_close.matcher(str);
        str = matcher＿.replaceAll(BRAC_CLOSE_2);
        //「[」->「\[」
        matcher＿ = pattern_sbrac_open.matcher(str);
        str = matcher＿.replaceAll(SBRAC_OPEN_2);
        //「]」->「\]」
        matcher＿ = pattern_sbrac_close.matcher(str);
        str = matcher＿.replaceAll(SBRAC_CLOSE_2);
        //「{」->「\{」
        matcher＿ = pattern_cbrac_open.matcher(str);
        str = matcher＿.replaceAll(CBRAC_OPEN_2);
        //「}」->「\}」
        matcher＿ = pattern_cbrac_close.matcher(str);
        str = matcher＿.replaceAll(CBRAC_CLOSE_2);
        //「.」->「\.」
        matcher＿ = pattern_comma.matcher(str);
        str = matcher＿.replaceAll(COMMA_2);
        //「|」->「\|」
        matcher＿ = pattern_vline.matcher(str);
        str = matcher＿.replaceAll(VLINE_2);
        //「?」->「\?」
        matcher＿ = pattern_qmark.matcher(str);
        str = matcher＿.replaceAll(QMARK_2);
        //「*」->「\*」
        matcher＿ = pattern_asterisk.matcher(str);
        str = matcher＿.replaceAll(ASTERISK_2);
        //2005.10.17 ADD END
        */

        str = Pattern.quote(str);

        return str;
    }

    /**
     * @param content 入力文字列
     * @return 出力文字列
     */
    protected abstract String escape(String content);

    /**
     * @param content 入力文字列
     * @param elmName 要素名
     * @return 出力文字列
     */
    protected abstract String escapeContent(String content, String elmName);

    /**
     * @param element 入力文字列
     * @return 出力文字列
     */
    protected abstract String unescape(String element);

    /**
     * @param element 入力文字列
     * @param elmName 要素名
     * @return 出力文字列
     */
    protected abstract String unescapeContent(String element, String elmName);


    protected final boolean isMatch(Pattern pattern, String str) {
        matcher＿ = pattern.matcher(str);
        return matcher＿.matches();
    }


    protected final boolean isMatch(String[] pattern, String str) {
        str = str.toLowerCase();
        for (String item : pattern) {
            if (item.equals(str)) {
                return true;
            }
        }
        return false;
    }

    protected final boolean isMatch(String pattern, String str) {
        return pattern.equals(str.toLowerCase());
    }

    protected final Parser create(Parser pif) {
        if (pif instanceof jp.kuro.meteor.core.html5.ParserImpl){
            pif = new jp.kuro.meteor.core.html5.ParserImpl();
        } else if (pif instanceof jp.kuro.meteor.core.xhtml5.ParserImpl) {
            pif = new jp.kuro.meteor.core.xhtml5.ParserImpl();
        } else if (pif instanceof ParserImpl) {
            pif = new ParserImpl();
        } else if (pif instanceof jp.kuro.meteor.core.xhtml.ParserImpl) {
            pif = new jp.kuro.meteor.core.xhtml.ParserImpl();
        } else if (pif instanceof jp.kuro.meteor.core.xml.ParserImpl) {
            pif = new jp.kuro.meteor.core.xml.ParserImpl();
        }

        return pif;
    }
}