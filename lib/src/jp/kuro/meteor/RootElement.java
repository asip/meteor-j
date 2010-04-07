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


/**
 * ルート要素クラス
 * @author Yasumasa Ashida
 * @since 2005/02/19 20:18:51
 * @version 0.9.0.0
 */
public class RootElement extends Element {
    private final static String EMPTY = "";

    //コンテントタイプ
    private String contentType;

    //改行コード
    private String kaigyoCode;
    //エンコーディング
    private String characterEncoding;

    //フック用文字列
    protected StringBuilder hookDocument = new StringBuilder();
    //フック判定フラグ
    private boolean hook = false;
    //モノフック判定フラグ
    private boolean monoHook = false;

    //親要素
    private Element element = null;

    //変更可能親要素
    private Element mutableElement;

    /**
     * コンストラクタ
     */
    public RootElement() {
        super(EMPTY);
    }

    /**
     * コンテントタイプを取得する
     * @return コンテントタイプ
     */
    public final String contentType() {
        return contentType;
    }

    /**
     * コンテントタイプをセットする
     * @param contentType コンテントタイプ
     */
    public final void setContentType(String contentType) {
        this.contentType = contentType;
    }

    /**
     * エンコーディングを取得する
     * @return エンコーディング
     */
    public final String characterEncoding() {
        return characterEncoding;
    }

    /**
     * エンコーディングをセットする
     * @param characterEncoding エンコーディング
     */
    public final void setCharacterEncoding(String characterEncoding) {
        this.characterEncoding = characterEncoding;
    }

    /**
     * 改行コードを取得する
     * @return 改行コード
     */
    public final String kaigyoCode() {
        return kaigyoCode;
    }

    /**
     * 改行コードをセットする
     * @param kaigyoCode 改行コード 
     */
    public void setKaigyoCode(String kaigyoCode) {
        this.kaigyoCode = kaigyoCode;
    }

    /**
     * フック文字列を取得する
     * @return フック文字列
     */
    public final StringBuilder hookDocument() {
        return hookDocument;
    }

    /**
     * フック文字列をセットする
     * @param hookDocument フック文字列
     */
    public final void setHookDocument(StringBuilder hookDocument) {
        this.hookDocument = hookDocument;
    }

    /**
     * フック判定フラグを取得する
     * @return フック判定フラグ
     */
    public final boolean hook() {
        return hook;
    }

    /**
     * フック判定フラグをセットする
     * @param hook フック判定フラグ
     */
    public final void setHook(boolean hook) {
        this.hook = hook;
    }

    /**
     * モノフック判定フラグを取得する
     * @return モノフック判定フラグ
     */
    public final boolean monoHook() {
        return monoHook;
    }

    /**
     * モノフック判定フラグをセットする
     * @param monoHook モノフック判定フラグ
     */
    public final void setMonoHook(boolean monoHook) {
        this.monoHook = monoHook;
    }

    /**
     * 親要素を取得する
     * @return 親要素
     */
    public final Element element() {
        return element;
    }

    /**
     * 親要素をセットする
     * @param elm 親要素
     */
    public final void setElement(Element elm) {
        this.element = elm;
    }

    /**
     * 変更可能親要素をセットする
     * @return 変更可能親要素
     */
    public Element mutableElement() {
        return mutableElement;
    }

    /**
     * 変更可能親要素をセットする
     * @param mutableElement 変更可能親要素
     */
    public void setMutableElement(Element mutableElement) {
        this.mutableElement = mutableElement;
    }
}
