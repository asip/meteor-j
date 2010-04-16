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

package jp.kuro.meteor.core.xhtml;

//パッケージ固有

import jp.kuro.meteor.*;
import jp.kuro.meteor.hook.Hooker;
import jp.kuro.meteor.hook.Looper;
import jp.kuro.meteor.core.Kernel;

//JAVA 標準
import java.util.regex.*;
import java.util.List;

/*
 * XHTMLタグ解析パーサ
 * @author Yasumasa Ashida
 * @version 0.9.3.5
 */
public class ParserImpl extends Kernel implements Parser {

    private static final String KAIGYO_CODE = "(\r?\n|\r)";
    private static final String NBSP_2 = "&nbsp;";
    private static final String BR_1 = "\r?\n|\r";
    private static final String BR_2 = "<br/>";
    private static final String BR_3 = "<br\\/>";

    private static final String META = "META";
    private static final String META_S = "meta";

    //private static final String MATCH_TAG2 = "textarea|TEXTAREA|option|OPTION|pre|PRE";
    private static final String[] MATCH_TAG2 = {"textarea", "option", "pre"};

    private static final String HTTP_EQUIV = "http-equiv";
    private static final String CONTENT_TYPE = "Content-Type";
    private static final String CONTENT = "content";

    //attribute
    //private static final String OPTION = "option|OPTION";
    //private static final String SELECTED = "selected|SELECTED";
    //private static final String INPUT = "input|INPUT";
    //private static final String CHECKED = "checked|CHECKED";
    //private static final String RADIO = "radio|RADIO";
    //private static final String DISABLE_ELEMENT = "input|INPUT|textarea|TEXTAREA|select|SELECT";
    //private static final String DISABLED = "disabled|DISABLED";

    private static final String[] ATTR_LOGIC = {"disabled", "readonly", "checked", "selected", "multiple"};
    private static final String OPTION = "option";
    private static final String SELECTED = "selected";
    private static final String INPUT = "input";
    private static final String CHECKED = "checked";
    private static final String RADIO = "radio";
    private static final String[] DISABLE_ELEMENT = {"input", "textarea", "select", "optgroup"};
    private static final String DISABLED = "disabled";
    private static final String[] READONLY_TYPE = {"text", "password"};
    private static final String TEXTAREA = "textarea";
    private static final String READONLY = "readonly";
    private static final String SELECT = "select";
    private static final String MULTIPLE = "multiple";

    private static final String SELECTED_M = "\\sselected=\"[^\"]*\"\\s|\\sselected=\"[^\"]*\"$|"
            + "\\sSELECTED=\"[^\"]*\"\\s|\\sSELECTED=\"[^\"]*\"$";
    private static final String SELECTED_M1 = "\\sselected=\"([^\"]*)\"\\s|\\sselected=\"([^\"]*)\"$|"
            + "\\sSELECTED=\"([^\"]*)\"\\s|\\sSELECTED=\"([^\"]*)\"$";
    private static final String SELECTED_R = "selected=\"[^\"]*\"|SELECTED=\"[^\"]*\"";
    private static final String SELECTED_U = "selected=\"true\"";
    private static final String CHECKED_M = "\\schecked=\"[^\"]*\"\\s|\\schecked=\"[^\"]*\"$|"
            + "\\sCHECKED=\"[^\"]*\"\\s|\\sCHECKED=\"[^\"]*\"$";
    private static final String CHECKED_M1 = "\\schecked=\"([^\"]*)\"\\s|\\schecked=\"([^\"]*)\"$|"
            + "\\sCHECKED=\"([^\"]*)\"\\s|\\sCHECKED=\"([^\"]*)\"$";
    private static final String CHECKED_R = "checked=\"[^\"]*\"|CHECKED=\"[^\"]*\"";
    private static final String CHECKED_U = "checked=\"true\"";
    private static final String DISABLED_M = "\\sdisabled=\"[^\"]*\"\\s|\\sdisabled=\"[^\"]*\"$|"
            + "\\sDISABLED=\"[^\"]*\"\\s|\\sDISABLED=\"[^\"]*\"$";
    private static final String DISABLED_M1 = "\\sdisabled=\"([^\"]*)\"\\s|\\sdisabled=\"([^\"]*)\"$|"
            + "\\sDISABLED=\"([^\"]*)\"\\s|\\sDISABLED=\"([^\"]*)\"$";
    private static final String DISABLED_R = "disabled=\"[^\"]*\"|DISABLED=\"[^\"]*\"";
    private static final String DISABLED_U = "disabled=\"true\"";
    private static final String READONLY_M = "\\sreadonly=\"[^\"]*\"\\s|\\sreadonly=\"[^\"]*\"$";
    private static final String READONLY_M1 = "\\sreadonly=\"([^\"]*)\"\\s|\\sreadonly=\"([^\"]*)\"$";
    private static final String READONLY_R = "readonly=\"[^\"]*\"";
    private static final String READONLY_U = "readonly=\"readonly\"";
    private static final String MULTIPLE_M = "\\smultiple=\"[^\"]*\"\\s|\\smultiple=\"[^\"]*\"$";
    private static final String MULTIPLE_M1 = "\\smultiple=\"([^\"]*)\"\\s|\\smultiple=\"([^\"]*)\"$";
    private static final String MULTIPLE_R = "multiple=\"[^\"]*\"";
    private static final String MULTIPLE_U = "multiple=\"multiple\"";

    private static final Pattern pattern_selected_m = Pattern.compile(SELECTED_M);
    private static final Pattern pattern_selected_m1 = Pattern.compile(SELECTED_M1);
    private static final Pattern pattern_selected_r = Pattern.compile(SELECTED_R);
    private static final Pattern pattern_checked_m = Pattern.compile(CHECKED_M);
    private static final Pattern pattern_checked_m1 = Pattern.compile(CHECKED_M1);
    private static final Pattern pattern_checked_r = Pattern.compile(CHECKED_R);
    private static final Pattern pattern_disabled_m = Pattern.compile(DISABLED_M);
    private static final Pattern pattern_disabled_m1 = Pattern.compile(DISABLED_M1);
    private static final Pattern pattern_disabled_r = Pattern.compile(DISABLED_R);
    private static final Pattern pattern_readonly_m = Pattern.compile(READONLY_M);
    private static final Pattern pattern_readonly_m1 = Pattern.compile(READONLY_M1);
    private static final Pattern pattern_readonly_r = Pattern.compile(READONLY_R);
    private static final Pattern pattern_multiple_m = Pattern.compile(MULTIPLE_M);
    private static final Pattern pattern_multiple_m1 = Pattern.compile(MULTIPLE_M1);
    private static final Pattern pattern_multiple_r = Pattern.compile(MULTIPLE_R);

    private static final String TRUE = "true";
    private static final String FALSE = "false";

    private static final String TYPE_L = "type";
    private static final String TYPE_U = "TYPE";

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
    private static final Pattern pattern_br_2 = Pattern.compile(BR_3);

    private static final Pattern pattern_set_mono1 = Pattern.compile(SET_MONO_1);


    /**
     * デフォルトコンストラクタ
     */
    public ParserImpl() {
        super();
        this.docType = Parser.XHTML;
    }

    /**
     * コピーコンストラクタ
     *
     * @param ps XHTMLParserオブジェクト
     */
    public ParserImpl(Parser ps) {
        document(ps.document());
        root.setHookDocument(ps.rootElement().hookDocument());
        root.setContentType(ps.rootElement().contentType());
        root.setKaigyoCode(ps.rootElement().kaigyoCode());

    }

    /**
     * ドキュメントをパースする
     *
     * @param document ドキュメント
     */
    public final void parse(String document) {
        super.document(document);

        analyzeML();
    }

    /**
     * ドキュメントを取得する
     *
     * @return ドキュメント
     */
    public final String document() {
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

    protected final void analyzeContentType() {

        Element tag = element(META_S, HTTP_EQUIV, CONTENT_TYPE);

        if (tag == null) {
            tag = element(META, HTTP_EQUIV, CONTENT_TYPE);
        }

        if (tag != null) {
            this.root.setContentType(attribute(tag, CONTENT));
        } else {
            this.root.setContentType(EMPTY);
        }
    }

    protected final void analyzeKaigyoCode() {
        pattern = Pattern.compile(KAIGYO_CODE);
        matcher = pattern.matcher(document());
        if (matcher.find()) {
            this.root.setKaigyoCode(matcher.group(1));
        }
        //初期化
        ////matcher.reset();
    }

    /**
     * 要素名により、要素を検索する
     *
     * @param elmName 要素名
     * @return 要素
     */
    public final Element element(String elmName) {
        return (super.element(elmName));
    }

    /**
     * 要素名と属性により、要素を検索する
     *
     * @param elmName   要素名
     * @param attrName  属性名
     * @param attrValue 属性値
     * @return 要素
     */
    public final Element element(String elmName, String attrName, String attrValue) {
        return (super.element(elmName, attrName, attrValue));
    }

    /**
     * 属性により、要素を検索する
     *
     * @param attrName  属性名
     * @param attrValue 属性値
     * @return 要素
     */
    public final Element element(String attrName, String attrValue) {
        return super.element(attrName, attrValue);
    }

    /**
     * 要素名と属性1と属性2により、要素を検索する
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
        return (super.element(elmName, attrName1, attrValue1, attrName2, attrValue2));
    }

    /**
     * 属性により、要素を検索する
     *
     * @param attrName1  属性名1
     * @param attrValue1 属性値1
     * @param attrName2  属性名2
     * @param attrValue2 属性値2
     * @return 要素
     */
    public final Element element(String attrName1, String attrValue1, String attrName2, String attrValue2) {
        return super.element(attrName1, attrValue1, attrName2, attrValue2);
    }

    /**
     * 要素名を編集する
     *
     * @param elm     要素
     * @param elmName 要素名
     */
    /*
    public final void setElementName(Element elm, String elmName) {
        super.setElementName(elm, elmName);
    }
    */

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

//    public final void attribute(Element elm, String attrName) {
//    }

    protected void editAttributes_(Element elm, String attrName, String attrValue) {

        //todo
        if (isMatch(SELECTED, attrName) && isMatch(OPTION, elm.name())) {
            editAttributes_(elm, attrValue, pattern_selected_m, pattern_selected_r, SELECTED_U);
        } else if (isMatch(MULTIPLE, attrName) && isMatch(SELECT, elm.name())) {
            editAttributes_(elm, attrValue, pattern_multiple_m, pattern_multiple_r, MULTIPLE_U);
        } else if (isMatch(CHECKED, attrName) && isMatch(INPUT, elm.name())
                && (isMatch(RADIO, this.attribute(elm, TYPE_L))
                || isMatch(RADIO, this.attribute(elm, TYPE_U)))) {
            editAttributes_(elm, attrValue, pattern_checked_m, pattern_checked_r, CHECKED_U);
        } else if (isMatch(DISABLE_ELEMENT, elm.name()) && isMatch(DISABLED, attrName)) {
            editAttributes_(elm, attrValue, pattern_disabled_m, pattern_disabled_r, DISABLED_U);
        } else
        if (isMatch(READONLY, attrName) && (isMatch(TEXTAREA, elm.name()) || (isMatch(INPUT, elm.name()) && isMatch(READONLY_TYPE, getType(elm))))) {
            editAttributes_(elm, attrValue, pattern_readonly_m, pattern_readonly_r, READONLY_U);
        } else {
            super.editAttributes_(elm, attrName, attrValue);
        }

        //return attrValue;

    }

    protected void editAttributes_(Element elm, String attrValue, Pattern match
            , Pattern replaceRegex, String replaceUpdate) {

        //todo
        attrValue = escape(attrValue);

        if (isMatch(TRUE, attrValue)) {

            pattern = match;

            matcher = pattern.matcher(elm.attributes());

            //attrName属性が存在しないなら追加
            if (!matcher.find()) {
                //属性文字列の最後に新規の属性を追加する
                if (!(elm.attributes()).equals(EMPTY)) {
                    sbuf.setLength(0);
                    elm.attributes(sbuf.append(SPACE).append(elm.attributes().trim())
                            .toString());
                }
                sbuf.setLength(0);
                elm.attributes(sbuf.append(elm.attributes()).append(SPACE).append(replaceUpdate)
                        .toString());
            } else {

                //属性の置換
                //sbuf.setLength(0);

                pattern = replaceRegex;
                matcher = pattern.matcher(elm.attributes());

                elm.attributes(matcher.replaceAll(replaceUpdate));

            }
        } else if (isMatch(FALSE, attrValue)) {

            pattern = replaceRegex;
            matcher = pattern.matcher(elm.attributes());
            elm.attributes(matcher.replaceAll(EMPTY));
        }

    }

    ///**
    // * 要素の属性を編集する
    // *
    // * @param attrName  属性名
    // * @param attrValue 属性値
    // */
    //public Element attribute(String attrName, String attrValue) {
    //    if (this.rootElement().element() != null) {
    //        return this.attribute(this.rootElement().element(), attrName, attrValue);
    //    }
    //
    //    return null;
    //}

    /**
     * 属性値を取得する
     *
     * @param elm      要素
     * @param attrName 属性名
     * @return 属性値
     */
    public final String attribute(Element elm, String attrName) {
        return (super.attribute(elm, attrName));
    }

    private String getType(Element elm) {
        if (elm.typeValue() != null) {
            elm.typeValue(super.getAttributeValue_(elm, TYPE_L));
            if (elm.typeValue() != null) {
                elm.typeValue(super.getAttributeValue_(elm, TYPE_U));
            }
        }
        return elm.typeValue();
    }

    protected String getAttributeValue_(Element elm, String attrName) {

        if (isMatch(SELECTED, attrName) && isMatch(OPTION, elm.name())) {
            return getAttributeValue_(elm, pattern_selected_m1);
        } else if (isMatch(MULTIPLE, attrName) && isMatch(SELECT, elm.name())) {
            return getAttributeValue_(elm, pattern_multiple_m1);
        } else if (isMatch(CHECKED, attrName) && isMatch(INPUT, elm.name())
                && (isMatch(RADIO, this.attribute(elm, TYPE_L))
                || isMatch(RADIO, this.attribute(elm, TYPE_U)))) {
            return getAttributeValue_(elm, pattern_checked_m1);
        } else if (isMatch(DISABLE_ELEMENT, elm.name()) && isMatch(DISABLED, attrName)) {
            return getAttributeValue_(elm, pattern_disabled_m1);
        } else
        if (isMatch(READONLY, attrName) && (isMatch(TEXTAREA, elm.name()) || (isMatch(INPUT, elm.name()) && isMatch(READONLY_TYPE, getType(elm))))) {
            return getAttributeValue_(elm, pattern_readonly_m1);
        } else {
            return super.getAttributeValue_(elm, attrName);
        }

    }

    protected String getAttributeValue_(Element elm, Pattern match) {

        pattern = match;

        matcher = pattern.matcher(elm.attributes());

        if (matcher.find()) {
            if (matcher.group(1) != null) {
                return matcher.group(1);
            } else if (matcher.group(2) != null) {
                return matcher.group(2);
            } else if (matcher.group(3) != null) {
                return matcher.group(3);
            } else if (matcher.group(4) != null) {
                return matcher.group(4);
            } else {
                return null;
            }
        } else {
            return FALSE;
        }

    }

    /**
     * 属性値を取得する
     *
     * @param attrName 属性名
     * @return 属性値
     */
    public String attribute(String attrName) {
        if (this.rootElement().element() != null) {
            return this.attribute(this.rootElement().element(), attrName);
        }

        return null;
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

            if (isMatch(ATTR_LOGIC, matcher.group(1))
                    && matcher.group(1).equals(matcher.group(2))) {
                attrs.store(matcher.group(1), TRUE);
            } else {
                attrs.store(matcher.group(1), unescape(matcher.group(2)));
            }

        }
        attrs.setRecordable(true);

        return attrs;
    }

    ///**
    // * 属性マップを取得する
    // *
    // * @return 属性マップ
    // */
    //public AttributeMap attributeMap() {
    //    if (this.rootElement().element() != null) {
    //        return this.attributeMap(this.rootElement().element());
    //    }
    //    return null;
    //}

    /**
     * @param elm      要素
     * @param attrName 属性名
     */
    public final Element removeAttribute(Element elm, String attrName) {
        return super.removeAttribute(elm, attrName);
    }

    /**
     * 要素の属性を消す
     *
     * @param attrName 属性名
     */
    public void removeAttribute(String attrName) {
        if (this.rootElement().element() != null) {
            this.removeAttribute(this.rootElement().element(), attrName);
        }
    }

    /**
     * @param elm       要素
     * @param content   要素の内容
     * @param entityRef エンティティ参照フラグ
     */
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
     * 要素の内容をセットする
     *
     * @param content 要素の内容
     */
    public Element content(String content) {
        if (this.rootElement().element() != null) {
            return this.content(this.rootElement().element(), content);
        }
        return null;
    }

    /**
     * 要素の内容をセットする
     *
     * @param content   要素の内容
     * @param entityRef エンティティ参照フラグ
     */
    public Element content(String content, boolean entityRef) {
        if (this.rootElement().element() != null) {
            return this.content(this.rootElement().element(), content, entityRef);
        }
        return null;
    }

    /**
     * 要素の内容を取得する
     *
     * @param elm Elementオブジェクト
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
     * コメント拡張タグ要素を取得する
     *
     * @param elmName 要素名
     * @param id      識別名
     * @return 要素
     */
    public final Element cxTag(String elmName, String id) {
        return super.cxTag(elmName, id);
    }

    /**
     * 識別名でコメント拡張タグ要素を検索する
     *
     * @param id 識別名
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

    protected final void setMonoInfo(Element elm) {
        boolean res;

        matcher = pattern_set_mono1.matcher(elm.mixedContent());
        res = matcher.matches();

        elm.mono(res);

        if (res) {
            sbuf.setLength(0);
            if (elm.cx()) {
                pattern_cc = sbuf.append(SET_CX_1).append(elm.name())
                        .append(SPACE).append(elm.attributes()).append(SET_CX_2)
                        .append(elm.mixedContent()).append(SET_CX_3).append(elm.name())
                        .append(SET_CX_4).toString();
            } else {
                if (elm.empty()) {
                    //フック判定がTRUEの場合
                    pattern_cc = sbuf.append(TAG_OPEN).append(elm.name())
                            .append(elm.attributes()).append(TAG_CLOSE)
                            .append(elm.mixedContent()).append(TAG_OPEN3)
                            .append(elm.name()).append(TAG_CLOSE).toString();
                } else {
                    pattern_cc = sbuf.append(TAG_OPEN).append(elm.name())
                            .append(elm.attributes()).append(TAG_CLOSE3).toString();
                }
            }
            elm.document(pattern_cc);
        }
    }

    protected final String escape(String element) {
        //Pattern pattern = null;
        //Matcher matcher = null;
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
        //pattern = null;
        //matcher = null;
        return element;
    }

    protected final String escapeContent(String element, String elmName) {
        element = escape(element);

        /*if(!elmName.equals(TEXTAREA_S) && !elmName.equals(TEXTAREA_W) &&
                !elmName.equals(OPTION_S) && !elmName.equals(OPTION_W) &&
                !elmName.equals(PRE_S) && !elmName.equals(PRE_W)){*/
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
        //Pattern pattern = null;
        //Matcher matcher = null;
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
        //初期化
        //matcher.reset();
        //pattern = null;
        //matcher = null;
        return element;
    }

    protected final String unescapeContent(String element, String elmName) {
        element = unescape(element);
        //「<br>」->「\r?\n」
        /*if(!elmName.equals(TEXTAREA_S) && !elmName.equals(TEXTAREA_W) &&
                !elmName.equals(OPTION_S) && !elmName.equals(OPTION_W) &&
                !elmName.equals(PRE_S) && !elmName.equals(PRE_W)){*/
        //matcher＿ = pattern_match_tag2.matcher(elmName);
        if (!isMatch(MATCH_TAG2, elmName)) {
            matcher＿ = pattern_br_2.matcher(element);
            element = matcher＿.replaceAll(this.root.kaigyoCode());
            //初期化
            //matcher.reset();
        }

        return element;
    }
}