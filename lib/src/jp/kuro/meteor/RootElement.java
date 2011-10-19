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

package jp.kuro.meteor;


/**
 * ルート要素クラス
 *
 * @author Yasumasa Ashida
 * @version 0.9.7.0
 * @since 2005/02/19 20:18:51
 */
public class RootElement extends Element {
    private final static String EMPTY = "";

    //コンテントタイプ
    private String contentType;
    //文字セット
    private String charset;
    //改行コード
    private String kaigyoCode;
    //エンコーディング
    private String characterEncoding;

    //フック用文字列
    //protected StringBuilder hookDocument = new StringBuilder();

    //ドキュメント
    private String document;

    //変更可能要素
    //private Element element = null;

    /**
     * デフォルトコンストラクタ
     */
    public RootElement(){
        super();
    }

    /**
     * コンテントタイプを取得する
     *
     * @return コンテントタイプ
     */
    public final String contentType() {
        return contentType;
    }

    /**
     * コンテントタイプをセットする
     *
     * @param contentType コンテントタイプ
     */
    public final void setContentType(String contentType) {
        this.contentType = contentType;
    }

    /**
     * 文字セットを取得する
     * @return 文字セット
     */
    public final String charset(){
        return charset;
    }

    /**
     * 文字セットをセットする
     * @param charset 文字セット
     */
    public final void setCharset(String charset){
        this.charset = charset;
    }

    /**
     * エンコーディングを取得する
     *
     * @return エンコーディング
     */
    public final String characterEncoding() {
        return characterEncoding;
    }

    /**
     * エンコーディングをセットする
     *
     * @param characterEncoding エンコーディング
     */
    public final void setCharacterEncoding(String characterEncoding) {
        this.characterEncoding = characterEncoding;
    }

    /**
     * 改行コードを取得する
     *
     * @return 改行コード
     */
    public final String kaigyoCode() {
        return kaigyoCode;
    }

    /**
     * 改行コードをセットする
     *
     * @param kaigyoCode 改行コード
     */
    public void setKaigyoCode(String kaigyoCode) {
        this.kaigyoCode = kaigyoCode;
    }

    /**
     * フック文字列を取得する
     *
     * @return フック文字列
     */
    /* public final StringBuilder hookDocument() {
        return hookDocument;
    }*/

    /**
     * フック文字列をセットする
     *
     * @param hookDocument フック文字列
     */
    /* public final void setHookDocument(StringBuilder hookDocument) {
        this.hookDocument = hookDocument;
    }*/

    /* public String setDocument() {
        return setDocument;
    } */

    /* public void setDocument(String setDocument) {
        this.setDocument = setDocument;
    } */

    /**
     * 要素を取得する
     *
     * @return 要素
     */
    /* public final Element element() {
        return element;
    } */

    /**
     * 要素をセットする
     *
     * @param elm 要素
     */
    /* public final void setElement(Element elm) {
        this.element = elm;
    } */

}
