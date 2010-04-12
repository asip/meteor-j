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

package jp.kuro.meteor;

import jp.kuro.meteor.core.util.AsyncStringBuffer;

/**
 * 要素情報保持クラス
 *
 * @author Yasumasa Ashida
 * @version 0.9.3.4
 */
public class Element {
    //要素名
    private String name;
    //属性群
    private String attributes;
    //内容
    private String mixedContent = "";
    //パターン
    private String pattern;
    //ドキュメント更新フラグ
    private boolean documentSync;
    //タグ全体
    private String doc;
    //内容存在フラグ
    private boolean empty;
    //コメント拡張タグフラグ
    private boolean cx;
    //子要素存在フラグ
    private boolean mono;
    //private boolean parent;
    //パーサ
    private Parser parser;
    //タイプ属性
    private String typeValue;
    //パターン変更用属性マップ
    //private AttributeMap arguments;
    //有効・無効フラグ
    private boolean usable;
    //原本ポインタ
    private Element origin;
    //複製ポインタ
    private Element copy;
    //オブジェクトID
    private Integer ObjectId;

    //文字列バッファ
    private AsyncStringBuffer sbuf = new AsyncStringBuffer();

    private static final String SPACE = " ";
    private static final String TAG_OPEN = "<";
    private static final String TAG_OPEN3 = "</";
    private static final String TAG_CLOSE = ">";
    protected static final String TAG_CLOSE3 = "/>";
    protected static final String SET_CX_1 = "<!-- @";
    protected static final String SET_CX_2 = "-->";
    protected static final String SET_CX_3 = "<!-- /@";
    protected static final String SET_CX_4 = " -->";


    /**
     * タグ名を引数とするコンストラクタ
     *
     * @param name 要素名
     */
    public Element(String name) {
        this.name = name;
        this.usable = true;
    }

    /**
     * コピーコンストラクタ
     *
     * @param elm 要素
     */
    public Element(Element elm) {
        this.name = elm.name();
        this.attributes = elm.attributes();
        //this.mixedContent = elm.mixedContent();
        this.pattern = elm.pattern();
        this.doc = elm.document();
        this.empty = elm.empty();
        this.cx = elm.cx();
        this.mono = elm.mono();
        this.parser = elm.parser();
        //this.usable = true;
        this.origin = elm;
        this.usable = true;
        //elm.copy(this);
    }


    /**
     * 要素、パーサを引数とするコンストラクタ
     *
     * @param elm 要素
     * @param ps  パーサ
     */
    public Element(Element elm, Parser ps) {
        this.name = elm.name();
        this.attributes = elm.attributes();
        this.mixedContent = elm.mixedContent();
        this.pattern = elm.pattern();
        this.doc = elm.document();
        this.empty = elm.empty();
        this.cx = elm.cx();
        this.mono = elm.mono();
        this.parser = ps;
        //this.arguments = new AttributeMap(elm.arguments());
        //this.usable = false
        this.origin = elm;
        elm.copy = this;
    }


    /**
     * タグ全体をセットする
     *
     * @param document タグ全体
     */
    public final void document(String document) {
        this.documentSync = false;
        this.doc = document;
    }

    /**
     * タグ全体を取得する
     *
     * @return タグ名
     */
    public final String document() {
        if (documentSync) {
            documentSync = false;
            switch (parser.docType()) {
                case Parser.HTML:
                    if (cx) {
                        sbuf.setLength(0);
                        doc = sbuf.append(SET_CX_1).append(name).append(SPACE).append(attributes).append(SET_CX_2)
                                .append(mixedContent).append(SET_CX_3).append(name).append(SET_CX_4).toString();
                    } else {
                        if (empty) {
                            sbuf.setLength(0);
                            doc = sbuf.append(TAG_OPEN).append(name).append(attributes).append(TAG_CLOSE)
                                    .append(mixedContent).append(TAG_OPEN3).append(name).append(TAG_CLOSE).toString();

                        } else {
                            sbuf.setLength(0);
                            doc = sbuf.append(TAG_OPEN).append(name).append(attributes).append(TAG_CLOSE).toString();
                        }
                    }
                    break;
                case Parser.XHTML:
                case Parser.XML:
                    if (cx) {
                        sbuf.setLength(0);
                        doc = sbuf.append(SET_CX_1).append(name).append(SPACE).append(attributes).append(SET_CX_2)
                                .append(mixedContent).append(SET_CX_3).append(name).append(SET_CX_4).toString();

                    } else {
                        if (empty) {
                            sbuf.setLength(0);
                            doc = sbuf.append(TAG_OPEN).append(name).append(attributes).append(TAG_CLOSE)
                                    .append(mixedContent).append(TAG_OPEN3).append(name).append(TAG_CLOSE).toString();

                        } else {
                            sbuf.setLength(0);
                            doc = sbuf.append(TAG_OPEN).append(name).append(attributes).append(TAG_CLOSE3).toString();
                        }
                    }
                    break;
            }
        }
        return doc;
    }

    /**
     * タグ名をセットする
     *
     * @param name タグ名
     */
    public final void name(String name) {
        this.name = name;
    }

    /**
     * タグ名を取得する
     *
     * @return タグ名
     */
    public final String name() {
        return name;
    }

    /**
     * 属性をセットする
     *
     * @param attributes 属性
     */
    public final void attributes(String attributes) {
        this.attributes = attributes;
    }

    /**
     * 属性を取得する
     *
     * @return 属性
     */
    public final String attributes() {
        return attributes;
    }

    /**
     * 要素をセットする
     *
     * @param mixedContent 要素
     */
    public final void mixedContent(String mixedContent) {
        this.mixedContent = mixedContent;
        this.empty = true;
    }


    /**
     * 要素を取得する
     *
     * @return 要素
     */
    public final String mixedContent() {
        return mixedContent;
    }

    /**
     * パターンをセットする
     *
     * @param pattern パターン
     */
    public final void pattern(String pattern) {
        this.pattern = pattern;
    }

    /**
     * パターンを取得する
     *
     * @return パターン
     */
    public final String pattern() {
        return pattern;
    }

    /**
     * ドキュメント更新フラグを取得する
     *
     * @return ドキュメント更新フラグ
     */
    public final boolean documentSync() {
        return documentSync;
    }

    /**
     * ドキュメント更新フラグをセットする
     *
     * @param documentSync ドキュメント更新フラグ
     */
    public final void documentSync(boolean documentSync) {
        this.documentSync = documentSync;
    }

    /**
     * 内容存在フラグを取得する
     *
     * @return 内容存在フラグ
     */
    public final boolean empty() {
        return empty;
    }

    /**
     * タグ状態をセットする
     *
     * @param cx タグ状態
     */
    public final void cx(boolean cx) {
        this.cx = cx;
    }

    /**
     * タグ状態を取得する
     *
     * @return タグ状態
     */
    public final boolean cx() {
        return cx;
    }

    /**
     * 要素値状態をセットする
     *
     * @param mono 要素値状態
     */
    public final void mono(boolean mono) {
        this.mono = mono;
    }

    /**
     * 要素値状態を取得する
     *
     * @return 要素値状態
     */
    public final boolean mono() {
        return mono;
    }

    /**
     * パーサを取得する
     *
     * @return パーサ
     */
    public final Parser parser() {
        return parser;
    }

    /**
     * パーサをセットする
     *
     * @param parser パーサ
     */
    public final void parser(Parser parser) {
        this.parser = parser;
    }

    /**
     * 要素をコピーする
     * @param elm 要素
     * @return 要素
     */
    public Element child(Element elm){
        return parser.element(elm);
    }

    /**
     * 要素名で要素を検索する
     *
     * @param elmName 要素名
     * @return 要素
     */
    public Element child(String elmName) {
        return parser.element(elmName);
    }

    /**
     * 要素名・属性名・属性値で要素を検索する
     *
     * @param elmName   要素名
     * @param attrName  属性名
     * @param attrValue 属性値
     * @return 要素
     */
    public Element child(String elmName, String attrName, String attrValue) {
        return parser.element(elmName, attrName, attrValue);
    }

    /**
     * 属性名・属性値で検索する
     *
     * @param attrName  属性名
     * @param attrValue 属性値
     * @return 要素
     */
    public Element child(String attrName, String attrValue) {
        return parser.element(attrName, attrValue);
    }

    /**
     * 要素名・属性名１・属性値１・属性名２・属性値２で要素を検索する
     *
     * @param elmName    要素名
     * @param attrName1  属性名１
     * @param attrValue1 属性値１
     * @param attrName2  属性名２
     * @param attrValue2 属性値２
     * @return 要素
     */
    public Element child(String elmName, String attrName1, String attrValue1, String attrName2, String attrValue2) {
        return parser.element(elmName, attrName1, attrValue1, attrName2, attrValue2);
    }

    /**
     * 属性名１・属性値１・属性名２・属性値２で要素を検索する
     *
     * @param attrName1  属性名１
     * @param attrValue1 属性値１
     * @param attrName2  属性名２
     * @param attrValue2 属性値２
     * @return 要素
     */
    public Element child(String attrName1, String attrValue1, String attrName2, String attrValue2) {
        return parser.element(attrName1, attrValue1, attrName2, attrValue2);
    }

    /**
     * 属性を編集する
     *
     * @param attrName  属性名
     * @param attrValue 属性の値
     */
    public final void attribute(String attrName, String attrValue) {
        this.parser.attribute(this, attrName, attrValue);
    }

    /**
     * 属性を取得する
     *
     * @param attrName 属性名
     * @return 属性の値
     */
    public final String attribute(String attrName) {
        return this.parser.attribute(this, attrName);
    }


    /**
     * 内容を編集する
     *
     * @param content 内容
     */
    public final void content(String content) {
        this.parser.content(this, content);
    }


    /**
     * 内容を編集する
     *
     * @param content   内容
     * @param entityRef 実体参照フラグ
     */
    public final void content(String content, boolean entityRef) {
        this.parser.content(this, content, entityRef);
    }


    /**
     * 内容を取得する
     *
     * @return 内容
     */
    public final String content() {
        return this.parser.content(this);
    }

    /**
     * タイプ属性値を取得する
     *
     * @return タイプ属性値
     */
    public final String typeValue() {
        return typeValue;
    }

    /**
     * タイプ属性値をセットする
     *
     * @param typeValue タイプ属性値
     */
    public final void typeValue(String typeValue) {
        this.typeValue = typeValue;
    }

    /**
     * 有効・無効フラグを取得する
     *
     * @return 有効・無効フラグ
     */
    public final boolean usable() {
        return usable;
    }

    /**
     * 有効・無効フラグをセットする
     *
     * @param usable 有効・無効フラグ
     */
    public final void usable(boolean usable) {
        this.usable = usable;
    }

    /**
     * 原本ポインタを取得する
     *
     * @return 原本ポインタ
     */
    public final Element origin() {
        return origin;
    }

    /**
     * 原本ポインタをセットする
     *
     * @param origin 原本ポインタ
     */
    public final void origin(Element origin) {
        this.origin = origin;
    }

    /**
     * 複製ポインタを取得する
     *
     * @return 複製ポインタ
     */
    public final Element copy() {
        return copy;
    }

    /**
     * 複製ポインタをセットする
     *
     * @param copy 複製ポインタ
     */
    public final void copy(Element copy) {
        this.copy = copy;
    }

    /**
     * オブジェクトIDを取得する
     *
     * @return オブジェクトID
     */
    public Integer objectId() {
        return ObjectId;
    }

    /**
     * オブジェクトIDをセットする
     *
     * @param objectId オブジェクトID
     */
    public void objectId(Integer objectId) {
        ObjectId = objectId;
    }

    /**
     * コピーを作成する
     *
     * @param elm 要素
     * @param ps  パーサ
     * @return 要素
     */
    public static Element new_(Element elm, Parser ps) {
        Element _obj = ps.rootElement().element();
        if (_obj != null) {
            _obj.attributes(elm.attributes());
            _obj.mixedContent(elm.mixedContent());
            //_obj.pattern(elm.pattern());
            _obj.document(elm.document());
            //_obj.arguments = new AttributeMap(elm.arguments());
            return _obj;
        } else {
            _obj = new Element(elm, ps);
            ps.rootElement().setElement(_obj);
            return _obj;
        }
    }

    /**
     * 複製する
     *
     * @return 要素
     */
    public final Element clone() {
        Element obj_ = this.parser.elementCache().get(this.objectId());
        if (obj_ != null) {
            obj_.attributes = this.attributes;
            obj_.mixedContent = this.mixedContent;
            //obj.pattern = this.pattern
            obj_.document(this.document());
            //obj.arguments = new AttributeMap(this.arguments)
            obj_.usable = true;
            return obj_;
        } else {
            obj_ = new Element(this);
            this.parser.elementCache().put(this.objectId(), obj_);
            return obj_;
        }
    }


    /**
     * 反映する
     */
    public final void flush() {
        parser.flush();
    }

    /**
     * コメント拡張タグを取得する
     *
     * @param elmName 要素名
     * @param id      ID
     * @return 要素
     */
    public Element cxTag(String elmName, String id) {
        return parser.cxTag(elmName, id);
    }

    /**
     * コメント拡張タグを取得する
     *
     * @param id ID
     * @return 要素
     */
    public Element cxTag(String id) {
        return parser.cxTag(id);
    }
    
}
