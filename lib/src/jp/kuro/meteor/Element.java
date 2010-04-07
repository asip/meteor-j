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
 * 要素情報保持クラス
 * @author Yasumasa Ashida
 * @version 0.9.0.0
 * 
 */
public class Element {
    private String name;
    private String attrs;
    private String mixedContent = "";
    private String pattern;
    private String document;
    private boolean empty;
    private boolean cx;
    private boolean mono;
    private boolean parent;
    private Parser parser;

    private String typeValue;

    /**
     * タグ名を引数とするコンストラクタ
     *
     * @param name 要素名
     */
    public Element(String name) {
        this.name = name;
    }


    public Element(Element elm) {
        this.name = elm.name();
        this.attrs = elm.attributes();
        this.mixedContent = elm.mixedContent();
        this.pattern = elm.pattern();
        this.document = elm.document();
        this.empty = elm.empty;
        this.cx = elm.cx;
        this.mono = elm.mono;
        this.parser = elm.parser;
    }

    /**
     * タグ全体をセットする
     *
     * @param document タグ全体
     */
    public final void document(String document) {
        this.document = document;
    }

    /**
     * タグ全体を取得する
     *
     * @return タグ名
     */
    public final String document() {
        return document;
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
     * @param attrs 属性
     */
    public final void attributes(String attrs) {
        this.attrs = attrs;
    }

    /**
     * 属性を取得する
     *
     * @return 属性
     */
    public final String attributes() {
        return attrs;
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


    //public final


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
     * @return パーサ
     */
    public final Parser parser() {
        return parser;
    }

    /**
     * パーサをセットする
     * @param parser パーサ
     */
    public final void parser(Parser parser) {
        this.parser = parser;
    }

    /**
     * 属性を編集する
     * @param attrName 属性名
     * @param attrValue 属性の値
     */
    public final void attribute(String attrName,String attrValue){
        this.parser.attribute(this,attrName,attrValue);
    }

    /**
     * 属性を取得する
     * @param attrName 属性名
     * @return 属性の値
     */
    public final String attribute(String attrName){
        return this.parser.attribute(this,attrName);
    }


    /**
     * 内容を編集する
     * @param content 内容
     */
    public final void content(String content){
        this.parser.content(this,content);
    }


    /**
     * 内容を編集する
     * @param content 内容
     * @param entityRef 実体参照フラグ
     */
    public final void content(String content,boolean entityRef){
        this.parser.content(this,content,entityRef);
    }


    /**
     * 内容を取得する
     * @return 内容
     */
    public final String content(){
        return this.parser.content(this);
    }


    /**
     * 親要素フラグを取得する
     * @return 親要素フラグ
     */
    public boolean parent() {
        return parent;
    }

    /**
     * 親要素フラグをセットする
     * @param parent 親要素フラグ
     */
    public void parent(boolean parent) {
        this.parent = parent;
    }

    /**
     * タイプ属性値を取得する
     * @return タイプ属性値
     */
    public String typeValue(){
        return typeValue;
    }

    /**
     * タイプ属性値をセットする
     * @param typeValue タイプ属性値
     */
    public void typeValue(String typeValue){
        this.typeValue = typeValue;
    }
}
