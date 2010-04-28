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

package jp.kuro.meteor.core.xml;

//JAVA標準

import java.util.regex.*;
import java.util.List;

//パッケージ固有
import jp.kuro.meteor.*;
import jp.kuro.meteor.hook.Hooker;
import jp.kuro.meteor.hook.Looper;
import jp.kuro.meteor.core.Kernel;

/**
 * XML解析パーサ
 *
 * @author Yasumasa Ashida
 * @version 0.9.4.2
 */
public class ParserImpl extends Kernel implements Parser {

    private static final Pattern pattern_en_1 = Pattern.compile(EN_1);
    private static final Pattern pattern_and_1 = Pattern.compile(AND_1);
    private static final Pattern pattern_lt_1 = Pattern.compile(LT_1);
    private static final Pattern pattern_gt_1 = Pattern.compile(GT_1);
    private static final Pattern pattern_dq_1 = Pattern.compile(DOUBLE_QUATATION);
    private static final Pattern pattern_ap_1 = Pattern.compile(AP_1);
    private static final Pattern pattern_lt_2 = Pattern.compile(LT_2);
    private static final Pattern pattern_gt_2 = Pattern.compile(GT_2);
    private static final Pattern pattern_dq_2 = Pattern.compile(QO_2);
    private static final Pattern pattern_ap_2 = Pattern.compile(AP_2);
    private static final Pattern pattern_and_2 = Pattern.compile(AND_2);

    /**
     * デフォルトコンストラクタ
     */
    public ParserImpl() {
        super();
        this.docType = Parser.XML;
    }

    /**
     * コピーコンストラクタ
     *
     * @param ps HTMLParserオブジェクト
     */
    public ParserImpl(Parser ps) {
        document(ps.document());
        root.setHookDocument(ps.rootElement().hookDocument());
    }

    /**
     * ファイルを読み込み、パースする
     *
     * @param filePath 入力ファイルの絶対パス
     * @param encoding 入力ファイルの文字コード
     */
    public final void read(String filePath, String encoding) {
        super.read(filePath, encoding);
    }

    /**
     * XMLドキュメントをパースする
     *
     * @param document XMLドキュメント
     */
    public final void parse(String document) {
        super.document(document);
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
     * ドキュメントを取得する
     *
     * @return ドキュメント
     */
    public final String document() {
        return super.document();
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
     * 要素を属性名で検索し、属性値を得る
     *
     * @param elm      要素
     * @param attrName 属性名
     * @return 属性値
     */
    public final String attribute(Element elm, String attrName) {
        return (super.attribute(elm, attrName));
    }

    /**
     * 要素の内容を属性名で検索し、属性値を得る
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
        return super.attributeMap(elm);
    }

    /**
     * 属性マップを取得する
     *
     * @return 属性マップ
     */
    public AttributeMap attributeMap() {
        if (this.rootElement().element() != null) {
            return this.attributeMap(this.rootElement().element());
        }
        return null;
    }

    /**
     * 要素の属性を消す
     *
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

    /**
     * 特殊文字(エンティティ参照)の置換
     *
     * @param element 入力文字列
     * @return 出力文字列
     */
    protected final String escape(String element) {
        //Pattern pattern = null;
        //Matcher matcher = null;
        //特殊文字の置換
        //「\」->[\\]
        matcher＿ = pattern_en_1.matcher(element);
        element = matcher＿.replaceAll(EN_2);
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
        //「'」->「&apos;」
        matcher＿ = pattern_ap_1.matcher(element);
        element = matcher＿.replaceAll(AP_2);
        //初期化
        //matcher.reset();
        //pattern = null;
        //matcher = null;
        return element;
    }

    protected final String escapeContent(String element, String elmName) {

        return this.escape(element);
    }

    protected final String unescape(String element) {
        //Pattern pattern = null;
        //Matcher matcher = null;
        //特殊文字の復元
        //「\」->[\\]
        matcher＿ = pattern_en_1.matcher(element);
        element = matcher＿.replaceAll(EN_2);
        //「<」<-「&lt;」
        matcher＿ = pattern_lt_2.matcher(element);
        element = matcher＿.replaceAll(LT_1);
        //「>」<-「&gt;」
        matcher＿ = pattern_gt_2.matcher(element);
        element = matcher＿.replaceAll(GT_1);
        //「"」->「&quot;」
        matcher＿ = pattern_dq_2.matcher(element);
        element = matcher＿.replaceAll(DOUBLE_QUATATION);
        //「'」->「&apos;」
        matcher＿ = pattern_ap_2.matcher(element);
        element = matcher＿.replaceAll(AP_1);
        //「&」->「&amp;」
        matcher＿ = pattern_and_2.matcher(element);
        element = matcher＿.replaceAll(AND_1);
        //初期化
        //matcher.reset();
        //pattern = null;
        //matcher = null;
        return element;
    }

    protected final String unescapeContent(String element, String elmName) {

        return this.unescape(element);
    }
}
