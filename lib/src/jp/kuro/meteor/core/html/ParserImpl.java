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
 * @version 0.9.0.0
 */
public class ParserImpl extends Kernel implements Parser {

    private static final String KAIGYO_CODE = "(\r?\n|\r)";
    private static final String NBSP_2 = "&nbsp;";
    private static final String BR_1 = "\r?\n|\r";
    private static final String BR_2 = "<br>";

    private static final String META = "META";
    private static final String META_S = "meta";

    //private static final String MATCH_TAG = "br|BR|hr|HR|img|IMG|input|INPUT|meta|META|base|BASE";
    private static final String[] MATCH_TAG = {"br","hr","img","input","meta","base"};
    //private static final String MATCH_TAG2 = "textarea|TEXTAREA|option|OPTION|pre|PRE";
    private static final String[] MATCH_TAG2 = {"textarea","option","pre"};

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

    private static final String[] ATTR_LOGIC = {"disabled","readonly","checked","selected","multiple"};
    private static final String OPTION = "option";
    private static final String SELECTED = "selected";
    private static final String INPUT = "input";
    private static final String CHECKED = "checked";
    private static final String RADIO = "radio";
    private static final String[] DISABLE_ELEMENT = {"input","textarea","select","optgroup"};
    private static final String DISABLED = "disabled";


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

    private static final Pattern pattern_selected_m = Pattern.compile(SELECTED_M);
    private static final Pattern pattern_selected_r = Pattern.compile(SELECTED_R);
    private static final Pattern pattern_checked_m = Pattern.compile(CHECKED_M);
    private static final Pattern pattern_checked_r = Pattern.compile(CHECKED_R);
    private static final Pattern pattern_disabled_m = Pattern.compile(DISABLED_M);
    private static final Pattern pattern_disabled_r = Pattern.compile(DISABLED_R);

    //private static final String TRUE = "true|TRUE";
    //private static final String FALSE = "false|FALSE";
    private static final String TRUE = "true";
    private static final String FALSE = "false";

    //private static final Pattern pattern_true = Pattern.compile(TRUE);
    //private static final Pattern pattern_false = Pattern.compile(FALSE);

    private static final String TYPE_L = "type";
    private static final String TYPE_U = "TYPE";

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
    private static final Pattern pattern_set_mono1 = Pattern.compile(SET_MONO_1);
    //private static final Pattern pattern_match_tag2 = Pattern.compile(MATCH_TAG2);

    /**
     * デフォルトコンストラクタ
     */
    public ParserImpl() {
        super();
    }

    /**
     * コピーコンストラクタ
     *
     * @param ps HTMLParserオブジェクト
     */
    public ParserImpl(Parser ps) {
        document(ps.document());
        root.setHookDocument(ps.rootElement().hookDocument());
        root.setHook(ps.rootElement().hook());
        root.setElement(ps.rootElement().element());
        root.setContentType(ps.rootElement().contentType());
    }

    /**
     * HTMLドキュメントをパースする
     *
     * @param document HTMLドキュメント
     */
    public void parse(String document) {
        super.document(document);

        analyzeML();
    }

    /**
     * HTMLドキュメントを取得する
     *
     * @return HTMLドキュメント
     */
    public final String document() {
        return super.document();
    }

    /**
     * ルート要素を取得する
     * @return ルート要素
     */
    public final RootElement rootElement(){
        return super.rootElement();
    }

    /**
     * フック時のスケールをセットする
     * @param size フック時のスケール
     */
    public final void size(int size) {
        super.size(size);
    }

    /**
     * エンコーディングをセットする
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
     * 要素名により、要素を検索する
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
        if (isMatch(MATCH_TAG,_elmName)) {
            //空要素タグの場合
            //空要素タグ検索用パターン
            sbuf.setLength(0);
            pattern_cc = sbuf.append(TAG_OPEN).append(_elmName)
                .append(TAG_SEARCH_1_4_2).toString();
            pattern = PatternCache.get(pattern_cc);
            //空要素タグ検索
            matcher = pattern.matcher(document());
            if(matcher.find()){
                elementWithoutContent(elmName);
            }else{
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
            if(matcher.find()){
                elementWithContent(elmName);
            }else{
                elm_ = null;
                throw new NoSuchElementException(elmName);
            }
        }

        return elm_;
    }

    /**
     * 空要素タグ検索
     *
     * @param elmName 要素名
     * @return 要素
     */
    protected final Element elementWithoutContent(String elmName) {

        elm_ = new Element(elmName);
        //属性
        elm_.attributes(matcher.group(1));
        //空要素タグ検索用パターン
        //sbuf.setLength(0);
        elm_.pattern(pattern_cc);
        //パーサ
        elm_.parser(this);
        
        return elm_;
    }

    /**
     * 要素名と属性により、要素を検索する
     *
     * @param elmName  要素名
     * @param attrName 属性名
     * @param attrValue 属性値
     * @return 要素
     */
    public final Element element(String elmName, String attrName, String attrValue) {

        //要素名にサポートしていない文字が含まれる場合
        //matcher = pattern_none.matcher(elmName);
        //if (matcher.find()) {
        //    return null;
        //}
        _elmName = escapeRegex(elmName);

        //属性名にサポートしていない文字が含まれる場合
        //matcher = pattern_none.matcher(attrName);
        //if (matcher.find()) {
        //    return null;
        //}
        _attrName = escapeRegex(attrName);

        //属性値にサポートしていない文字が含まれる場合
        //matcher = pattern_none.matcher(attrValue);
        //if (matcher.find()) {
        //    return null;
        //}
        _attrValue = escapeRegex(attrValue);

        //空要素タグの場合(<->要素ありタグの場合)
        //matcher = pattern_match_tag.matcher(_elmName);
        if (isMatch(MATCH_TAG,_elmName)) {
            //空要素タグ検索
            elementWithoutContent(elmName, attrName, attrValue);
        } else {
            //
            elementWithContent(elmName, attrName, attrValue);
        }
        //初期化
        //matcher.reset();

        return elm_;
    }

    /**
     * 内容ありタグ検索
     *
     * @param elmName   要素名
     * @param attrName  属性名
     * @param attrValue 属性値
     * @return 要素
     */
    protected final Element elementWithContent(String elmName, String attrName, String attrValue) {
        return super.elementWithContent(elmName, attrName, attrValue);
    }

    /**
     * 空要素タグ検索
     *
     * @param elmName   要素名
     * @param attrName  属性名
     * @param attrValue 属性値
     * @return 要素
     */
    protected final Element elementWithoutContent(String elmName, String attrName, String attrValue) {
        return _elementWithoutContent(elmName, attrName, attrValue, TAG_SEARCH_2_4_3);
    }


    /**
     * 属性により、要素を検索する
     *
     * @param attrName  属性名
     * @param attrValue 属性値
     * @return 要素
     */
    public final Element element(String attrName, String attrValue) {

        //属性名にサポートしていない文字が含まれる場合
        //matcher = pattern_none.matcher(attrName);
        //if (matcher.find()) {
        //    return null;
        //}
        _attrName = escapeRegex(attrName);

        //属性値にサポートしていない文字が含まれる場合
        //matcher = pattern_none.matcher(attrValue);
        //if (matcher.find()) {
        //    return null;
        //}
        _attrValue = escapeRegex(attrValue);

        sbuf.setLength(0);
        pattern_cc = sbuf.append(TAG_SEARCH_3_1).append(_attrName).append(ATTR_EQ)
                .append(_attrValue).append(TAG_SEARCH_2_4_4).toString();
        pattern = PatternCache.get(pattern_cc);

        matcher = pattern.matcher(document());

        if (matcher.find()) {
            element(matcher.group(1), attrName, attrValue);
        }else{
            elm_ = null;
            throw new NoSuchElementException(attrName,attrValue);
        }

        //初期化
        //matcher.reset();


        return elm_;
    }

    /**
     * 要素名と属性1と属性2により、要素を検索する
     *
     * @param elmName  要素名
     * @param attrName1 属性名1
     * @param attrValue1 属性値1
     * @param attrName2 属性名2
     * @param attrValue2 属性値2
     * @return 要素
     */
    public final Element element(String elmName,
                                     String attrName1,String attrValue1,String attrName2,String attrValue2) {


        //要素名にサポートしていない文字が含まれる場合
        //matcher = pattern_none.matcher(elmName);
        //if (matcher.find()) {
        //    return null;
        //}
        _elmName = escapeRegex(elmName);

        //属性名にサポートしていない文字が含まれる場合
        //matcher = pattern_none.matcher(attrName1);
        //if (matcher.find()) {
        //    return null;
        //}
        _attrName1 = escapeRegex(attrName1);

        //属性名にサポートしていない文字が含まれる場合
        //matcher = pattern_none.matcher(attrName2);
        //if (matcher.find()) {
        //    return null;
        //}
        _attrName2 = escapeRegex(attrName2);

        //属性値にサポートしていない文字が含まれる場合
        //matcher = pattern_none.matcher(attrValue1);
        //if (matcher.find()) {
        //    return null;
        //}
        _attrValue1 = escapeRegex(attrValue1);

        //属性値にサポートしていない文字が含まれる場合
        //matcher = pattern_none.matcher(attrValue2);
        //if (matcher.find()) {
        //    return null;
        //}
        _attrValue2 = escapeRegex(attrValue2);

        //空要素タグの場合(<->要素ありタグの場合)
        //matcher = pattern_match_tag.matcher(_elmName);
        if (isMatch(MATCH_TAG,_elmName)) {
            //空要素タグ検索
            elementWithoutContent(elmName, attrName1,attrValue1,attrName2,attrValue2);
        } else {
            //
            elementWithContent(elmName, attrName1,attrValue1,attrName2,attrValue2);
        }
        //初期化
        //matcher.reset();

        return elm_;
    }

    /**
     * 内容ありタグ検索
     *
     * @param elmName   要素名
     * @param attrName1  属性名1
     * @param attrValue1 属性値1
     * @param attrName2  属性名2
     * @param attrValue2 属性値2
     * @return 要素
     */
    protected final Element elementWithContent(String elmName,
                                                   String attrName1,String attrValue1,String attrName2,String attrValue2) {
        return super.elementWithContent(elmName, attrName1,attrValue1,attrName2,attrValue2);
    }

    /**
     * 空要素タグ検索
     *
     * @param elmName   要素名
     * @param attrName1  属性名1
     * @param attrValue1 属性値1
     * @param attrName2  属性名2
     * @param attrValue2 属性値2
     * @return 要素
     */
    protected final Element elementWithoutContent(String elmName,
                                                      String attrName1,String attrValue1,String attrName2,String attrValue2) {
        return _elementWithoutContent(elmName, attrName1,attrValue1,attrName2,attrValue2, TAG_SEARCH_2_4_3_2);
    }


    /**
     * 属性1と属性2により、要素を検索する
     *
     * @param attrName1  属性名1
     * @param attrValue1 属性値1
     * @param attrName2  属性名2
     * @param attrValue2 属性値2
     * @return 要素
     */
    public final Element element(String attrName1,String attrValue1,String attrName2,String attrValue2) {


        //属性名にサポートしていない文字が含まれる場合
        //matcher = pattern_none.matcher(attrName1);
        //if (matcher.find()) {
        //    return null;
        //}
        _attrName1 = escapeRegex(attrName1);
        _attrName2 = escapeRegex(attrName2);

        //属性値にサポートしていない文字が含まれる場合
        //matcher = pattern_none.matcher(attrValue1);
        //if (matcher.find()) {
        //    return null;
        //}
        _attrValue1 = escapeRegex(attrValue1);
        _attrValue2 = escapeRegex(attrValue2);

        sbuf.setLength(0);
        pattern_cc = sbuf.append(TAG_SEARCH_3_1_2_2).append(_attrName1).append(ATTR_EQ)
                .append(_attrValue1).append(TAG_SEARCH_2_6).append(_attrName2).append(ATTR_EQ)
                .append(_attrValue2).append(TAG_SEARCH_2_7).append(_attrName2).append(ATTR_EQ)
                .append(_attrValue2).append(TAG_SEARCH_2_6).append(_attrName1).append(ATTR_EQ)
                .append(_attrValue1).append(TAG_SEARCH_2_4_3_2).toString();
        pattern = PatternCache.get(pattern_cc);

        matcher = pattern.matcher(document());

        if (matcher.find()) {
            element(matcher.group(1), attrName1,attrValue1,attrName2,attrValue2);
        }else{
            elm_ = null;
            throw new NoSuchElementException(attrName1,attrValue1,attrName2,attrValue2);
        }

        //初期化
        //matcher.reset();


        return elm_;
    }

    /**
     * 要素名を編集する
     *
     * @param elm     要素
     * @param elmName 要素名
     */
    /*
    public final void setElementName(Element elm, String elmName) {

        if (!elm.cx()) {
            elm.name(elmName);
            //タグ検索用パターン
            pattern = PatternCache.get(elm.pattern());
            //タグ検索
            matcher = pattern.matcher(document());
            if (elm.empty()) {
                //要素ありタグの場合
                sbuf.setLength(0);
                document(matcher.replaceFirst(sbuf.append(TAG_OPEN)
                        .append(elm.name()).append(elm.attributes()).append(TAG_CLOSE)
                        .append(elm.mixedContent()).append(TAG_OPEN3).append(elm.name())
                        .append(TAG_CLOSE).toString()));
            } else {
                //空要素タグの場合
                sbuf.setLength(0);
                document(matcher.replaceFirst(sbuf.append(TAG_OPEN)
                        .append(elm.name()).append(elm.attributes()).append(TAG_CLOSE)
                        .toString()));
            }

            //パターンの更新
            if (!result.equals(elmName)) {
                sbuf.setLength(0);
                pattern_cc = sbuf.append(TAG_OPEN).append(result).toString();
                pattern = PatternCache.get(pattern_cc);
                matcher = pattern.matcher(elm.pattern());
                sbuf.setLength(0);
                elm.pattern(matcher.replaceAll(sbuf.append(TAG_OPEN).append(elmName).toString()));

                if (elm.empty()) {
                    sbuf.setLength(0);
                    pattern_cc = sbuf.append(TAG_OPEN4).append(result).toString();
                    pattern = PatternCache.get(pattern_cc);
                    matcher = pattern.matcher(elm.pattern());
                    sbuf.setLength(0);
                    elm.pattern(matcher.replaceAll(sbuf.append(TAG_OPEN4).append(elmName).toString()));
                }
            }

            //初期化
            //matcher.reset();
        }

    }
    */
    
    /**
     * 要素の属性を編集する
     *
     * @param elm       要素
     * @param attrName  属性名
     * @param attrValue 属性値
     */
    public final void attribute(Element elm, String attrName, String attrValue) {
        super.attribute(elm,attrName,attrValue);
    }

    protected String editAttributes_(Element elm,String attrName, String attrValue){


        //todo

        if((isMatch(OPTION,elm.name())) && isMatch(SELECTED,attrName)){
            attrValue = _editAttributes(elm,attrName,attrValue,pattern_selected_m,pattern_selected_r);
        }else if(isMatch(INPUT,elm.name()) && isMatch(CHECKED,attrName)
                && (isMatch(RADIO,this.attribute(elm,TYPE_L))
                || isMatch(RADIO,this.attribute(elm,TYPE_U)))){
            attrValue = _editAttributes(elm,attrName,attrValue,pattern_checked_m,pattern_checked_r);
        }else if(isMatch(DISABLE_ELEMENT,elm.name()) && isMatch(DISABLED,attrName)){
            attrValue = _editAttributes(elm,attrName,attrValue,pattern_disabled_m,pattern_disabled_r);
        }else{
            attrValue = super.editAttributes_(elm,attrName,attrValue);
        }

        return attrValue;

    }

    protected String _editAttributes(Element elm,String attrName, String attrValue,Pattern match,Pattern replace){

        //todo
        attrValue = escape(attrValue);

        if(isMatch(TRUE,attrValue)){

            pattern = match;

            matcher = pattern.matcher(elm.attributes());

            //attrName属性が存在しないなら追加
            if(!matcher.find()){
                //属性文字列の最後に新規の属性を追加する
                if (!EMPTY.equals(elm.attributes()) && !EMPTY.equals(elm.attributes().trim())) {
                    sbuf.setLength(0);
                    result = sbuf.append(SPACE).append(elm.attributes().trim())
                            .toString();
                } else {
                    result = EMPTY;
                }
                sbuf.setLength(0);
                result = sbuf.append(result).append(SPACE).append(attrName)
                        .toString();
                attrValue = escapeRegex(attrValue);
                attrValue = escapeRegex(attrValue);
            }else{
                result = elm.attributes();
            }
        }else if(isMatch(FALSE,attrValue)){

            pattern = match;

            matcher = pattern.matcher(elm.attributes());

            //attrName属性が存在するなら削除
            if(matcher.find()){
                attrValue = escapeRegex(attrValue);

                //属性の置換
                sbuf.setLength(0);

                pattern = replace;

                matcher = pattern.matcher(elm.attributes());

                result = matcher.replaceAll(EMPTY);

                attrValue = escapeRegex(attrValue);
            }else{
                result = elm.attributes();
            }
        }
        elm.attributes(result);

        return attrValue;

    }

    protected final void editDocument_(Element elm){
        editDocument_(elm,TAG_CLOSE);
    }

    /**
     * 要素の属性を編集する(属性値省略の場合)
     *
     * @param elm      要素
     * @param attrName 属性名
     */
    public final void setAttribute(Element elm, String attrName) {

        if (!elm.cx()) {
            //属性検索用パターン
            pattern = PatternCache.get(attrName);
            //属性検索
            matcher = pattern.matcher(elm.attributes());

            //検索対象属性の存在判定
            if (matcher.find()) {
                result = elm.attributes();
            } else {
                //属性文字列の最後に新規の属性を追加する
                if (!EMPTY.equals(elm.attributes()) && !EMPTY.equals(elm.attributes().trim())) {
                    sbuf.setLength(0);
                    result = sbuf.append(SPACE).append(elm.attributes().trim())
                            .toString();
                } else {
                    result = EMPTY;
                }
                sbuf.setLength(0);
                result = sbuf.append(result).append(SPACE).append(attrName)
                        .toString();
            }
            elm.attributes(result);
            //タグ検索用パターン
            pattern = PatternCache.get(elm.pattern());
            //タグ検索
            matcher = pattern.matcher(document());
            if (elm.empty()) {
                //要素ありタグの場合
                sbuf.setLength(0);
                document(matcher.replaceFirst(sbuf.append(TAG_OPEN)
                        .append(elm.name()).append(result).append(TAG_CLOSE)
                        .append(elm.mixedContent()).append(TAG_OPEN3).append(elm.name())
                        .append(TAG_CLOSE).toString()));
            } else {
                //空要素タグの場合
                sbuf.setLength(0);
                document(matcher.replaceFirst(sbuf.append(TAG_OPEN)
                        .append(elm.name()).append(result).append(TAG_CLOSE)
                        .toString()));
            }
            //初期化
            //matcher.reset();
        }

    }

    /**
     * 要素の属性を編集する
     *
     * @param attrName  属性名
     * @param attrValue 属性値
     */
    public void attribute(String attrName, String attrValue) {
        if(this.rootElement().hook() || this.rootElement().monoHook()){
            this.attribute(this.rootElement().mutableElement(),attrName,attrValue);
        }
    }

    /**
//     * 要素の属性を編集する(属性値省略の場合)
//     *
//     * @param attrName 属性名
//     */
//    public void attribute(String attrName) {
//        if(this.rootElement().hook() || this.rootElement().monoHook()){
//            this.attribute(this.rootElement().mutableElement(),attrName);
//        }
//    }

    /**
     * 要素の内容を属性名で検索し、属性値を得る
     *
     * @param elm 要素
     * @param attrName 属性名
     * @return 属性値
     */
    public final String attribute(Element elm, String attrName) {
        return (super.attribute(elm, attrName));
    }

    protected String getAttributeValue_(Element elm, String attrName){

        if((isMatch(SELECTED,attrName) && isMatch(OPTION,elm.name()))){
            return getAttributeValue_(elm,pattern_selected_m);
        }else if(isMatch(DISABLED,attrName) && isMatch(DISABLE_ELEMENT,elm.name())){
            return getAttributeValue_(elm,pattern_disabled_m);
        }else if(isMatch(CHECKED,attrName) && isMatch(INPUT,elm.name())
                && isMatch(RADIO,getType(elm))){
            return getAttributeValue_(elm,pattern_checked_m);
        }else{
            return super.getAttributeValue_(elm,attrName);
        }

    }


    private String getType(Element elm){
        if(elm.typeValue() != null){
            elm.typeValue(super.getAttributeValue_(elm,TYPE_L));
            if(elm.typeValue() != null){
                elm.typeValue(super.getAttributeValue_(elm,TYPE_U));
            }
        }
        return elm.typeValue();
    }

    protected String getAttributeValue_(Element elm,Pattern match_p){

        pattern =match_p;

        matcher = pattern.matcher(elm.attributes());

        if(matcher.find()){
            return TRUE;
        }else{
            return FALSE;
        }

    }

    /**
     * 要素の内容を属性名で検索し、属性値を得る
     *
     * @param attrName 属性名
     * @return 属性値
     */
    public String attribute(String attrName) {
        if(this.rootElement().hook() || this.rootElement().monoHook()){
            return this.attribute(this.rootElement().mutableElement(),attrName);
        }

        return null;
    }

    //todo
    /**
     * 属性マップを取得する
     * @param elm 要素
     * @return 属性マップ
     */
    public final AttributeMap attributeMap(Element elm){
       return super.attributeMap(elm);
    }

    /**
     * 属性マップを取得する
     * @return 属性マップ
     */
    public AttributeMap attributeMap() {
        if(this.rootElement().hook() || this.rootElement().monoHook()){
            return this.attributeMap(this.rootElement().mutableElement());
        }
        return null;
    }

    /**
     * 要素の属性を消す
     * @param elm 要素
     * @param attrName 属性名
     */
    public final void removeAttribute(Element elm, String attrName) {

        if (!elm.cx()) {
            result = elm.attributes();

            //属性検索用パターン
            sbuf.setLength(0);
            pattern = PatternCache.get(sbuf.append(attrName).append(ERASE_ATTR_1)
                    .toString());
            //属性検索
            matcher = pattern.matcher(elm.attributes());

            //検索対象属性の存在判定
            if (matcher.find()) {
                //属性の置換
                result = matcher.replaceFirst(EMPTY);
            } else {
                //属性検索用パターン
                sbuf.setLength(0);
                pattern = PatternCache.get(sbuf.append(attrName).append(GET_ATTR_1)
                        .toString());
                //属性検索
                matcher = pattern.matcher(elm.attributes());
                //検索対象属性の存在判定
                if (matcher.find()) {
                    //属性の置換
                    result = matcher.replaceFirst(EMPTY);
                }
            }

            elm.attributes(result);

            //タグ検索用パターン
            pattern = PatternCache.get(elm.pattern());
            //タグ検索
            matcher = pattern.matcher(document());

            if (elm.empty()) {
                //要素ありタグの場合
                sbuf.setLength(0);
                document(matcher.replaceFirst(sbuf.append(TAG_OPEN)
                        .append(elm.name()).append(result).append(TAG_CLOSE)
                        .append(elm.mixedContent()).append(TAG_OPEN3).append(elm.name())
                        .append(TAG_CLOSE).toString()));
            } else {
                //空要素タグの場合
                sbuf.setLength(0);
                document(matcher.replaceFirst(sbuf.append(TAG_OPEN)
                        .append(elm.name()).append(result).append(TAG_CLOSE)
                        .toString()));
            }

            //パターンの更新
            sbuf.setLength(0);
            pattern_cc = sbuf.append(attrName).append(SET_ATTR_1).toString();
            pattern = PatternCache.get(pattern_cc);
            matcher = pattern.matcher(elm.pattern());
            sbuf.setLength(0);
            elm.pattern(matcher.replaceFirst(EMPTY));

            //初期化
            //matcher.reset();
        }

    }

    /**
     * 要素の属性を消す
     *
     * @param attrName 属性名
     */
    public void removeAttribute(String attrName) {
        if(this.rootElement().hook() || this.rootElement().monoHook()){
            this.removeAttribute(this.rootElement().mutableElement(),attrName);
        }
    }

    public final void content(Element elm, String content, boolean entityRef) {
        super.content(elm, content, entityRef);
    }

    /**
     * 要素の内容をセットする
     *
     * @param elm     要素
     * @param content 要素の内容
     */
    public final void content(Element elm, String content) {
        super.content(elm, content);
    }

    /**
     * 要素の内容をセットする
     *
     * @param content 要素の内容
     */
    public void content(String content) {
        if(this.rootElement().monoHook()){
            this.content(this.rootElement().mutableElement(),content);
        }
    }

    /**
     * 要素の内容をセットする
     *
     * @param content 要素の内容
     * @param entityRef エンティティ参照フラグ
     */
    public void content(String content, boolean entityRef) {
        if(this.rootElement().monoHook()){
            this.content(this.rootElement().mutableElement(),content,entityRef);
        }
    }

    /**
     * 要素の内容を取得する
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
    public final void removeElement(Element elm) {
        super.removeElement(elm);
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
     * XHTMLを出力する
     */
    public final void print() {
        super.print();
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

    /**
     * 子パーサを取得する
     *
     * @param elm 要素
     * @return 子パーサ
     */
    public Parser child(Element elm) {
        return super.child(elm);
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
                            .append(elm.attributes()).append(TAG_CLOSE).toString();
                }
            }
            elm.document(pattern_cc);
        }
    }

    /**
     * 子パーサを親パーサに反映する
     */
    public final void flush(){
        super.flush();
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
        if (!isMatch(MATCH_TAG2,elmName)) {
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
        if (!isMatch(MATCH_TAG2,elmName)) {
            //「<br>」->「\r?\n」
            matcher＿ = pattern_br_2.matcher(element);
            element = matcher＿.replaceAll(this.root.kaigyoCode());

        }

		return element;
	}
}