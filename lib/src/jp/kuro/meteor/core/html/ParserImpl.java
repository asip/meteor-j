//
//Meteor -  A lightweight (X)HTML(5) & XML parser
// Copyright (C) 2002-2011 Yasumasa Ashida.
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

package jp.kuro.meteor.core.html;

//パッケージ固有

import jp.kuro.meteor.AttributeMap;
import jp.kuro.meteor.Element;
import jp.kuro.meteor.Parser;
import jp.kuro.meteor.RootElement;
import jp.kuro.meteor.core.Kernel;
import jp.kuro.meteor.core.util.PatternCache;
import jp.kuro.meteor.exception.NoSuchElementException;
import jp.kuro.meteor.hook.Hooker;
import jp.kuro.meteor.hook.Looper;

import java.util.List;
import java.util.regex.Pattern;

/*
 * HTMLパーサ
 * @author Yasumasa Ashida
 * @version 0.9.7.0
 */
public class ParserImpl extends Kernel implements Parser {

    private static final String KAIGYO_CODE = "(\r?\n|\r)";
    private static final String NBSP_2 = "&nbsp;";
    //private static final String NBSP_3 = "nbsp";
    private static final String BR_1 = "\r?\n|\r";
    private static final String BR_2 = "<br>";

    protected static final String META = "META";
    protected static final String META_S = "meta";

    //private static final String MATCH_TAG = "br|BR|hr|HR|img|IMG|input|INPUT|meta|META|base|BASE";
    //内容のない要素
    private static String[] MATCH_TAG = {"br", "hr", "img", "input", "meta", "base"};

    //private static final String MATCH_TAG2 = "textarea|TEXTAREA|option|OPTION|pre|PRE";
    //改行を<br>に変換する必要のない要素
    private static final String[] MATCH_TAG2 = {"textarea", "option", "pre"};

    //入れ子にできない要素
    private static String[] MATCH_TAG_SNG = {"texarea", "select","option", "form","fieldset"};

    protected static final String HTTP_EQUIV = "http-equiv";
    protected static final String CONTENT_TYPE = "Content-Type";
    protected static final String CONTENT = "content";

    //attribute
    //private static final String OPTION = "option|OPTION";
    //private static final String SELECTED = "selected|SELECTED";
    //private static final String INPUT = "input|INPUT";
    //private static final String CHECKED = "checked|CHECKED";
    //private static final String RADIO = "radio|RADIO";
    //private static final String DISABLE_ELEMENT = "input|INPUT|textarea|TEXTAREA|select|SELECT";
    //private static final String DISABLED = "disabled|DISABLED";

    //論理値で指定する属性
    private static String[] ATTR_LOGIC = {"disabled", "readonly", "checked", "selected", "multiple"};

    protected static final String OPTION = "option";
    protected static final String SELECTED = "selected";
    protected static final String INPUT = "input";
    protected static final String CHECKED = "checked";
    protected static final String RADIO = "radio";

    //diabled属性のある要素
    private static final String[] DISABLE_ELEMENT = {"input", "textarea", "select", "optgroup"};

    protected static final String DISABLED = "disabled";

    //readonly属性のあるinput要素のタイプ
    protected static final String[] READONLY_TYPE = {"text", "password"};

    protected static final String TEXTAREA = "textarea";
    protected static final String READONLY = "readonly";
    protected static final String SELECT = "select";
    protected static final String MULTIPLE = "multiple";

    //private static final Pattern pattern_option = Pattern.compile(OPTION);
    //private static final Pattern pattern_selected = Pattern.compile(SELECTED);
    //private static final Pattern pattern_input = Pattern.compile(INPUT);
    //private static final Pattern pattern_checked = Pattern.compile(CHECKED);
    //private static final Pattern pattern_radio = Pattern.compile(RADIO);
    //private static final Pattern pattern_disable_element = Pattern.compile(DISABLE_ELEMENT);
    //private static final Pattern pattern_disabled = Pattern.compile(DISABLED);

    private static final String SELECTED_M = "\\sselected\\s|\\sselected$|\\sSELECTED\\s|\\sSELECTED$";
    private static final String SELECTED_R = "selected\\s|selected$|SELECTED\\s|SELECTED$";
    private static final String CHECKED_M = "\\schecked\\s|\\schecked$|\\sCHECKED\\s|\\sCHECKED$";
    private static final String CHECKED_R = "checked\\s|checked$|CHECKED\\s|CHECKED$";
    private static final String DISABLED_M = "\\sdisabled\\s|\\sdisabled$|\\sDISABLED\\s|\\sDISABLED$";
    private static final String DISABLED_R = "disabled\\s|disabled$|DISABLED\\s|DISABLED$";
    private static final String READONLY_M = "\\sreadonly\\s|\\sreadonly$|\\sREADONLY\\s|\\sREADONLY$";
    private static final String READONLY_R = "readonly\\s|readonly$|READONLY\\s|READONLY$";
    private static final String MULTIPLE_M = "\\smultiple\\s|\\smultiple$|\\sMULTIPLE\\s|\\sMULTIPLE$";
    private static final String MULTIPLE_R = "multiple\\s|multiple$|MULTIPLE\\s|MULTIPLE$";

    protected static final Pattern pattern_selected_m = Pattern.compile(SELECTED_M);
    protected static final Pattern pattern_selected_r = Pattern.compile(SELECTED_R);
    protected static final Pattern pattern_checked_m = Pattern.compile(CHECKED_M);
    protected static final Pattern pattern_checked_r = Pattern.compile(CHECKED_R);
    protected static final Pattern pattern_disabled_m = Pattern.compile(DISABLED_M);
    protected static final Pattern pattern_disabled_r = Pattern.compile(DISABLED_R);
    protected static final Pattern pattern_readonly_m = Pattern.compile(READONLY_M);
    protected static final Pattern pattern_readonly_r = Pattern.compile(READONLY_R);
    protected static final Pattern pattern_multiple_m = Pattern.compile(MULTIPLE_M);
    protected static final Pattern pattern_multiple_r = Pattern.compile(MULTIPLE_R);


    //private static final String TRUE = "true|TRUE";
    //private static final String FALSE = "false|FALSE";
    private static final String TRUE = "true";
    private static final String FALSE = "false";

    //private static final Pattern pattern_true = Pattern.compile(TRUE);
    //private static final Pattern pattern_false = Pattern.compile(FALSE);

    protected static final String TYPE_L = "type";
    protected static final String TYPE_U = "TYPE";

    //private static final String _TRUE = "true";
    //private static final String _FALSE = "false";

    private static final Pattern pattern_and_1 = Pattern.compile(AND_1);
    private static final Pattern pattern_lt_1 = Pattern.compile(LT_1);
    private static final Pattern pattern_gt_1 = Pattern.compile(GT_1);
    private static final Pattern pattern_dq_1 = Pattern.compile(DOUBLE_QUATATION);
    private static final Pattern pattern_space_1 = Pattern.compile(SPACE);
    private static final Pattern pattern_br_1 = Pattern.compile(BR_1);
    private static final Pattern pattern_lt_2 = Pattern.compile(LT_2);
    private static final Pattern pattern_gt_2 = Pattern.compile(GT_2);
    private static final Pattern pattern_dq_2 = Pattern.compile(QO_2);
    private static final Pattern pattern_space_2 = Pattern.compile(NBSP_2);
    private static final Pattern pattern_and_2 = Pattern.compile(AND_2);
    private static final Pattern pattern_br_2 = Pattern.compile(BR_2);

    //private static final Pattern pattern_match_tag = Pattern.compile(MATCH_TAG);
    //private static final Pattern pattern_match_tag2 = Pattern.compile(MATCH_TAG2);

    private static final String GET_ATTRS_MAP2 = "\\s(disabled|readonly|checked|selected|multiple)";
    private static final Pattern pattern_get_attrs_map2 = Pattern.compile(GET_ATTRS_MAP2);

    /**
     * デフォルトコンストラクタ
     */
    public ParserImpl() {
        super();
        this.docType = Parser.HTML;
    }

    /**
     * コピーコンストラクタ
     *
     * @param ps HTMLParserオブジェクト
     */
    public ParserImpl(Parser ps) {
        setDocument(ps.document());
        document_hook = ps.documentHook();
        root.setContentType(ps.rootElement().contentType());
        root.setKaigyoCode(ps.rootElement().kaigyoCode());
    }

    /**
     * HTMLドキュメントをパースする
     *
     * @param document HTMLドキュメント
     */
    public void parse(String document) {
        super.setDocument(document);

        analyzeML();
    }

    /**
     * HTMLドキュメントを取得する
     *
     * @return HTMLドキュメント
     */
    public final String setDocument() {
        return super.document();
    }

    /**
     * ルート要素を取得する
     *
     * @return ルート要素
     */
    public final RootElement rootElement() {
        return super.rootElement();
    }

    /**
     * フック時のスケールをセットする
     *
     * @param size フック時のスケール
     */
    public final void size(int size) {
        super.size(size);
    }

    /**
     * エンコーディングをセットする
     *
     * @param enc エンコーディング
     */
    public final void setCharacterEncoding(String enc) {
        super.setCharacterEncoding(enc);
    }

    /**
     * ファイルを読み込み、パースする
     *
     * @param filePath 入力ファイルの絶対パス
     * @param encoding 入力ファイルの文字コード
     */
    public final void read(String filePath, String encoding) {
        super.read(filePath, encoding);

        analyzeML();
    }

    private void analyzeML() {
        //content-typeの取得
        analyzeContentType();

        //改行コードの取得
        analyzeKaigyoCode();
    }


    protected void analyzeContentType() {
        element(META_S, HTTP_EQUIV, CONTENT_TYPE);

        if (elm_ == null) {
            element(META, HTTP_EQUIV, CONTENT_TYPE);
        }

        if (elm_ != null) {
            this.root.setContentType(attribute(elm_, CONTENT));
        } else {
            this.root.setContentType(EMPTY);
        }
    }

    protected final void analyzeKaigyoCode() {

        //改行コード取得
        pattern = Pattern.compile(KAIGYO_CODE);
        matcher = pattern.matcher(document());
        if (matcher.find()) {
            this.root.setKaigyoCode(matcher.group(1));
        }

    }

    /**
     * 要素名で要素を検索する
     *
     * @param elmName 要素名
     * @return 要素
     */
    public final Element element(String elmName) {

        //要素名にサポートしていない文字が含まれる場合
        //matcher = pattern_none.matcher(elmName);
        //if (matcher.find()) {
        //    return null;
        //}
        _elmName = escapeRegex(elmName);

        //空要素タグの場合(<->要素ありタグの場合)
        //matcher = pattern_match_tag.matcher(_elmName);
        if (isMatch(MATCH_TAG, _elmName)) {
            //空要素タグの場合
            //空要素タグ検索用パターン
            sbuf.setLength(0);
            pattern_cc = sbuf.append(TAG_OPEN).append(_elmName)
                    .append(TAG_SEARCH_1_4_2).toString();
            pattern = PatternCache.get(pattern_cc);
            //空要素タグ検索
            matcher = pattern.matcher(document());
            if (matcher.find()) {
                elementWithout(elmName);
            } else {
                elm_ = null;
                throw new NoSuchElementException(elmName);
            }
        } else {
            //内容ありタグの場合
            sbuf.setLength(0);
            pattern_cc = sbuf.append(TAG_OPEN).append(_elmName)
                    .append(TAG_SEARCH_1_1).append(elmName).append(TAG_SEARCH_1_2).
                    append(_elmName).append(TAG_CLOSE).toString();
            pattern = PatternCache.get(pattern_cc);
            //要素ありタグ検索
            matcher = pattern.matcher(this.document());
            if (matcher.find()) {
                elementWith(elmName);
            } else {
                elm_ = null;
                throw new NoSuchElementException(elmName);
            }
        }

        if (elm_ != null) {
            elm_.objectId(++counter);
            elementCache.put(elm_.objectId(), elm_);
        }

        return elm_;
    }

    /**
     * 空要素タグ検索
     *
     * @param elmName 要素名
     * @return 要素
     */
    protected final Element elementWithout(String elmName) {

        elm_ = new Element(elmName);
        //属性
        elm_.attributes(matcher.group(1));
        //空要素タグ検索用パターン
        //sbuf.setLength(0);
        elm_.pattern(pattern_cc);
        //タグ全体
        elm_.document(matcher.group(0));
        //パーサ
        elm_.parser(this);

        return elm_;
    }

    /**
     * 要素名と属性で要素を検索する
     *
     * @param elmName   要素名
     * @param attrName  属性名
     * @param attrValue 属性値
     * @return 要素
     */
    public final Element element(String elmName, String attrName, String attrValue) {

        //要素名にサポートしていない文字が含まれる場合
        _elmName = escapeRegex(elmName);

        //属性名にサポートしていない文字が含まれる場合
        _attrName = escapeRegex(attrName);

        //属性値にサポートしていない文字が含まれる場合
        _attrValue = escapeRegex(attrValue);

        //空要素の場合(<->内容あり要素の場合)
        if (isMatch(MATCH_TAG, elmName)) {
            //空要素検索パターン
            sbuf.setLength(0);
            pattern_cc = sbuf.append(TAG_OPEN).append(_elmName).append(TAG_SEARCH_2_1).append(_attrName)
                    .append(ATTR_EQ).append(_attrValue).append(TAG_SEARCH_2_4_3).toString();

            pattern = PatternCache.get(pattern_cc);
            //空要素検索
            matcher = pattern.matcher(root.document());
            res = matcher.find();
            if (res) {
                elementWithout_3(elmName);
            } else {
                elm_ = null;
                throw new NoSuchElementException(elmName, attrName, attrValue);
            }
        } else {
            //内容あり要素検索パターン
            sbuf.setLength(0);
            pattern_cc = sbuf.append(TAG_OPEN).append(_elmName).append(TAG_SEARCH_2_1).append(_attrName)
                    .append(ATTR_EQ).append(_attrValue).append(TAG_SEARCH_2_2).append(_elmName)
                    .append(TAG_SEARCH_1_2).append(_elmName).append(TAG_CLOSE).toString();

            pattern = PatternCache.get(pattern_cc);
            //内容あり要素検索
            matcher = pattern.matcher(root.document());
            res = matcher.find();

            if (!res && !isMatch(MATCH_TAG_SNG, elmName)) {
                res = elementWith_3_2();
            }

            if (res) {
                elementWith_3_1(elmName);
            } else {
                elm_ = null;
                throw new NoSuchElementException(elmName, attrName, attrValue);
            }
        }

        if (elm_ != null) {
            elm_.objectId(++counter);
            elementCache.put(elm_.objectId(), elm_);
        }

        return elm_;
    }

    /**
     * 空要素タグ検索
     *
     * @param elmName 要素名
     * @return 要素
     */
    protected final Element elementWithout_3(String elmName) {
        return _elementWithout_3_1(elmName, TAG_SEARCH_NC_2_4_3);
    }


    /**
     * 属性で要素を検索する
     *
     * @param attrName  属性名
     * @param attrValue 属性値
     * @return 要素
     */
    public final Element element(String attrName, String attrValue) {

        //属性名にサポートしていない文字が含まれる場合
        _attrName = escapeRegex(attrName);

        //属性値にサポートしていない文字が含まれる場合
        _attrValue = escapeRegex(attrValue);

        sbuf.setLength(0);
        pattern_cc = sbuf.append(TAG_SEARCH_3_1).append(_attrName).append(ATTR_EQ)
                .append(_attrValue).append(TAG_SEARCH_2_4_4).toString();
        pattern = PatternCache.get(pattern_cc);

        matcher = pattern.matcher(document());

        if (matcher.find()) {
            element(matcher.group(1), attrName, attrValue);
        } else {
            elm_ = null;
            throw new NoSuchElementException(attrName, attrValue);
        }

        return elm_;
    }

    /**
     * 要素名と属性１・属性２で要素を検索する
     *
     * @param elmName    要素名
     * @param attrName1  属性名1
     * @param attrValue1 属性値1
     * @param attrName2  属性名2
     * @param attrValue2 属性値2
     * @return 要素
     */
    public final Element element(String elmName,
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

        //空要素の場合(<->内容あり要素の場合)
        if (isMatch(MATCH_TAG, elmName)) {
            //空要素検索パターン
            sbuf.setLength(0);
            pattern_cc = sbuf.append(TAG_OPEN).append(_elmName).append(TAG_SEARCH_2_1_2)
                    .append(_attrName1).append(ATTR_EQ).append(_attrValue1)
                    .append(TAG_SEARCH_2_6).append(_attrName2).append(ATTR_EQ)
                    .append(_attrValue2).append(TAG_SEARCH_2_7).append(_attrName2)
                    .append(ATTR_EQ).append(_attrValue2).append(TAG_SEARCH_2_6)
                    .append(attrName1).append(ATTR_EQ).append(_attrValue1)
                    .append(TAG_SEARCH_2_4_3_2).toString();

            pattern = PatternCache.get(pattern_cc);
            //空要素検索
            matcher = pattern.matcher(root.document());
            res = matcher.find();

            if (res) {
                elementWithout_5(elmName);
            } else {
                elm_ = null;
                throw new NoSuchElementException(elmName, attrName1, attrValue1, attrName2, attrValue2);
            }
        } else {
            //内容あり要素検索パターン
            sbuf.setLength(0);
            pattern_cc = sbuf.append(TAG_OPEN).append(elmName).append(TAG_SEARCH_2_1_2).append(_attrName1)
                    .append(ATTR_EQ).append(_attrValue1).append(TAG_SEARCH_2_6).append(_attrName2)
                    .append(ATTR_EQ).append(_attrValue2).append(TAG_SEARCH_2_7).append(_attrName2)
                    .append(ATTR_EQ).append(_attrValue2).append(TAG_SEARCH_2_6).append(_attrName1)
                    .append(ATTR_EQ).append(_attrValue1).append(TAG_SEARCH_2_2_2).append(_elmName)
                    .append(TAG_SEARCH_1_2).append(_elmName).append(TAG_CLOSE).toString();

            pattern = PatternCache.get(pattern_cc);
            //内容あり要素検索
            matcher = pattern.matcher(root.document());
            res = matcher.find();

            if (!res && !isMatch(MATCH_TAG_SNG, elmName)) {
                res = elementWith_5_2();
            }

            if (res) {
                elementWith_5_1(elmName);
            } else {
                elm_ = null;
                throw new NoSuchElementException(elmName, attrName1, attrValue1, attrName2, attrValue2);
            }
        }

        if (elm_ != null) {
            elm_.objectId(++counter);
            elementCache.put(elm_.objectId(), elm_);
        }

        return elm_;
    }

    /**
     * 空要素タグ検索
     *
     * @param elmName 要素名
     * @return 要素
     */
    protected final Element elementWithout_5(String elmName) {
        return _elementWithout_5_1(elmName, TAG_SEARCH_2_4_3_2);
    }

    /**
     * 要素の属性を編集する
     *
     * @param elm       要素
     * @param attrName  属性名
     * @param attrValue 属性値
     */
    public final Element attribute(Element elm, String attrName, String attrValue) {
        return super.attribute(elm, attrName, attrValue);
    }

    protected void editAttributes_(Element elm, String attrName, String attrValue) {

        //todo

        if (isMatch(SELECTED, attrName) && isMatch(OPTION, elm.name())) {
            _editAttributes(elm, attrName, attrValue, pattern_selected_m, pattern_selected_r);
        } else if (isMatch(MULTIPLE, attrName) && isMatch(SELECT, elm.name())) {
            _editAttributes(elm, attrName, attrValue, pattern_multiple_m, pattern_multiple_r);
        } else if (isMatch(DISABLED, attrName) && isMatch(DISABLE_ELEMENT, elm.name())) {
            _editAttributes(elm, attrName, attrValue, pattern_disabled_m, pattern_disabled_r);
        } else if (isMatch(CHECKED, attrName) && isMatch(INPUT, elm.name())
                && (isMatch(RADIO, this.attribute(elm, TYPE_L))
                || isMatch(RADIO, this.attribute(elm, TYPE_U)))) {
            _editAttributes(elm, attrName, attrValue, pattern_checked_m, pattern_checked_r);
        } else
        if (isMatch(READONLY, attrName) && (isMatch(TEXTAREA, elm.name()) || (isMatch(INPUT, elm.name()) && isMatch(READONLY_TYPE, getType(elm))))) {
            _editAttributes(elm, attrName, attrValue, pattern_readonly_m, pattern_readonly_r);
        } else {
            super.editAttributes_(elm, attrName, attrValue);
        }

    }

    protected void _editAttributes(Element elm, String attrName, String attrValue, Pattern match, Pattern replace) {

        //todo
        attrValue = escape(attrValue);

        if (isMatch(TRUE, attrValue)) {

            pattern = match;

            matcher = pattern.matcher(elm.attributes());

            //attrName属性が存在しないなら追加
            if (!matcher.find()) {
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
                        .toString());
            }
        } else if (isMatch(FALSE, attrValue)) {

            pattern = replace;

            matcher = pattern.matcher(elm.attributes());

            elm.attributes(matcher.replaceAll(EMPTY));
        }
    }

    protected final void editDocument_(Element elm) {
        editDocument_(elm, TAG_CLOSE);
    }

    /**
     * 要素の内容を属性名で検索し、属性値を得る
     *
     * @param elm      要素
     * @param attrName 属性名
     * @return 属性値
     */
    public final String attribute(Element elm, String attrName) {
        return (super.attribute(elm, attrName));
    }

    protected String getAttributeValue_(Element elm, String attrName) {

        if ((isMatch(SELECTED, attrName) && isMatch(OPTION, elm.name()))) {
            return getAttributeValue_(elm, pattern_selected_m);
        } else if ((isMatch(MULTIPLE, attrName) && isMatch(SELECT, elm.name()))) {
            return getAttributeValue_(elm, pattern_multiple_m);
        } else if (isMatch(DISABLED, attrName) && isMatch(DISABLE_ELEMENT, elm.name())) {
            return getAttributeValue_(elm, pattern_disabled_m);
        } else if (isMatch(CHECKED, attrName) && isMatch(INPUT, elm.name())
                && isMatch(RADIO, getType(elm))) {
            return getAttributeValue_(elm, pattern_checked_m);
        } else
        if (isMatch(READONLY, attrName) && (isMatch(TEXTAREA, elm.name()) || (isMatch(INPUT, elm.name()) && isMatch(READONLY_TYPE, getType(elm))))) {
            return getAttributeValue_(elm, pattern_readonly_m);
        } else {
            return super.getAttributeValue_(elm, attrName);
        }

    }


    protected final String getType(Element elm) {
        if (elm.typeValue() != null) {
            elm.typeValue(super.getAttributeValue_(elm, TYPE_L));
            if (elm.typeValue() != null) {
                elm.typeValue(super.getAttributeValue_(elm, TYPE_U));
            }
        }
        return elm.typeValue();
    }

    protected String getAttributeValue_(Element elm, Pattern match_p) {

        pattern = match_p;

        matcher = pattern.matcher(elm.attributes());

        if (matcher.find()) {
            return TRUE;
        } else {
            return FALSE;
        }

    }

    //todo
    /**
     * 属性マップを取得する
     *
     * @param elm 要素
     * @return 属性マップ
     */
    public final AttributeMap attributeMap(Element elm) {
        AttributeMap attrs = new AttributeMap();

        matcher = pattern_get_attrs_map.matcher(elm.attributes());

        while (matcher.find()) {
            attrs.store(matcher.group(1), unescape(matcher.group(2)));
        }

        matcher = pattern_get_attrs_map2.matcher(elm.attributes());

        while (matcher.find()) {
            attrs.store(matcher.group(1), TRUE);
        }

        attrs.setRecordable(true);

        return attrs;
    }

    /**
     * 要素の属性を消す
     *
     * @param elm      要素
     * @param attrName 属性名
     */
    public final void removeAttribute_(Element elm, String attrName) {

        //検索対象属性の論理型是非判定
        if (!isMatch(ATTR_LOGIC, attrName)) {
            //属性検索用パターン
            sbuf.setLength(0);
            pattern = PatternCache.get(sbuf.append(attrName).append(ERASE_ATTR_1).toString());
            matcher = pattern.matcher(elm.attributes());
            elm.attributes(matcher.replaceFirst(EMPTY));
        } else {
            //属性検索用パターン
            pattern = PatternCache.get(attrName);
            matcher = pattern.matcher(elm.attributes());
            elm.attributes(matcher.replaceFirst(EMPTY));
        }
    }

    public final Element content(Element elm, String content, boolean entityRef) {
        return super.content(elm, content, entityRef);
    }

    /**
     * 要素の内容をセットする
     *
     * @param elm     要素
     * @param content 要素の内容
     */
    public final Element content(Element elm, String content) {
        return super.content(elm, content);
    }

    /**
     * 要素の内容を取得する
     *
     * @param elm 要素
     * @return 要素の内容
     */
    public final String content(Element elm) {
        return (super.content(elm));
    }

    /**
     * 要素を消す
     *
     * @param elm 要素
     */
    public final Element removeElement(Element elm) {
        return super.removeElement(elm);
    }

    /**
     * @param elmName 要素名
     * @param id      識別名
     * @return 要素
     */
    public final Element cxTag(String elmName, String id) {
        return super.cxTag(elmName, id);
    }

    /**
     * ID属性でCXタグを検索する
     *
     * @param id ID属性値
     * @return 要素
     */
    public final Element cxTag(String id) {
        return super.cxTag(id);
    }

    /**
     * 反映する
     */
    public final void flush() {
        super.flush();
    }

    /**
     * フッククラスに処理を委譲する
     *
     * @param elm  要素
     * @param hook Hookerオブジェクト
     */
    public void execute(Element elm, Hooker hook) {
        super.execute(elm, hook);
    }

    /**
     * フッククラスに処理を委譲する
     *
     * @param elm  要素
     * @param hook Hookerオブジェクト
     * @param list Listオブジェクト
     */
    public void execute(Element elm, Looper hook, List list) {
        super.execute(elm, hook, list);
    }

    protected final String escape(String element) {

        //特殊文字の置換
        //「&」->「&amp;」
        matcher＿ = pattern_and_1.matcher(element);
        element = matcher＿.replaceAll(AND_2);
        //「<」->「&lt;」
        matcher＿ = pattern_lt_1.matcher(element);
        element = matcher＿.replaceAll(LT_2);
        //「>」->「&gt;」
        matcher＿ = pattern_gt_1.matcher(element);
        element = matcher＿.replaceAll(GT_2);
        //「"」->「&quot;」
        matcher＿ = pattern_dq_1.matcher(element);
        element = matcher＿.replaceAll(QO_2);
        //「 」->「&nbsp;」
        matcher＿ = pattern_space_1.matcher(element);
        element = matcher＿.replaceAll(NBSP_2);
        //初期化
        //matcher.reset();

        return element;
    }

    protected final String escapeContent(String element, String elmName) {
        element = escape(element);

        //matcher＿ = pattern_match_tag2.matcher(elmName);
        if (!isMatch(MATCH_TAG2, elmName)) {
            //「\r?\n」->「<br>」
            matcher＿ = pattern_br_1.matcher(element);
            element = matcher＿.replaceAll(BR_2);
            //初期化
            //matcher.reset();
        }

        return element;
    }

    protected final String unescape(String element) {

        //特殊文字の復元

        //「<」<-「&lt;」
        matcher＿ = pattern_lt_2.matcher(element);
        element = matcher＿.replaceAll(LT_1);
        //「>」<-「&gt;」
        matcher＿ = pattern_gt_2.matcher(element);
        element = matcher＿.replaceAll(GT_1);
        //「"」<-「&quot;」
        matcher＿ = pattern_dq_2.matcher(element);
        element = matcher＿.replaceAll(DOUBLE_QUATATION);
        //「 」<-「&nbsp;」
        matcher＿ = pattern_space_2.matcher(element);
        element = matcher＿.replaceAll(SPACE);
        //「&」<-「&amp;」
        matcher＿ = pattern_and_2.matcher(element);
        element = matcher＿.replaceAll(AND_1);

        return element;
    }

    protected final String unescapeContent(String element, String elmName) {
        element = unescape(element);

        //matcher＿ = pattern_match_tag2.matcher(elmName);
        if (!isMatch(MATCH_TAG2, elmName)) {
            //「<br>」->「\r?\n」
            matcher＿ = pattern_br_2.matcher(element);
            element = matcher＿.replaceAll(this.root.kaigyoCode());

        }

        return element;
    }
}