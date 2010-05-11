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

import jp.kuro.meteor.hook.Hooker;
import jp.kuro.meteor.hook.Looper;

import java.util.List;
import java.util.LinkedHashMap;

/**
 * パーサ共通インタ－フェイス
 *
 * @author Yasumasa Ashida
 * @version 0.9.5.1
 * @since 2003/01/25 18:31:27
 */
public interface Parser {
    public static final int HTML = 0;
    public static final int XHTML = 1;
    public static final int HTML5 = 2;
    public static final int XHTML5 = 3;
    public static final int XML = 4;

    //void parse(String document);
    //void read(String filePath,String encoding);

    /**
     * 要素をコピーする
     * @param elm 要素
     * @return 要素
     */
    Element element(Element elm);

    /**
     * 要素名で要素を検索する。
     * 先頭から検索し、最初にヒットした要素の情報を返します。
     *
     * @param elmName 要素名
     * @return 要素
     */
    Element element(String elmName);

    /**
     * 要素名、属性(属性名="属性値")で要素を検索する。
     * 先頭から検索し、最初にヒットした要素の情報を返します。
     *
     * @param elmName   要素名
     * @param attrName  属性名
     * @param attrValue 属性値
     * @return 要素
     */
    Element element(String elmName, String attrName, String attrValue);

    /**
     * 属性(属性名="属性値")で要素を検索する。
     * 先頭から検索し、最初にヒットした要素の情報を返します。
     *
     * @param attrName  属性名
     * @param attrValue 属性値
     * @return 要素
     */
    Element element(String attrName, String attrValue);

    /**
     * 要素名、属性1と属性2(属性名="属性値")で要素を検索する。
     * 先頭から検索し、最初にヒットした要素の情報を返します。
     *
     * @param elmName    要素名
     * @param attrName1  属性名1
     * @param attrValue1 属性値1
     * @param attrName2  属性名2
     * @param attrValue2 属性値2
     * @return 要素
     */
    Element element(String elmName, String attrName1, String attrValue1, String attrName2, String attrValue2);

    /**
     * 属性1と属性2(属性名="属性値")で要素を検索する。
     * 先頭から検索し、最初にヒットした要素の情報を返します。
     *
     * @param attrName1  属性名1
     * @param attrValue1 属性値1
     * @param attrName2  属性名2
     * @param attrValue2 属性値2
     * @return 要素
     */
    Element element(String attrName1, String attrValue1, String attrName2, String attrValue2);

    /**
     * セレクタで要素を検索する
     * @param selector セレクタ
     * @return 要素
     */
    Element find(String selector);

    /**
     * 要素名を変更する
     * @param elm 要素
     * @param elmName 要素名
     */
    /*
    void setElementName(Element elm, String elmName);
    */

    /**
     * 属性をセットする。
     * 属性名で指定した属性が既にある場合は更新し、ない場合は追加する
     *
     * @param elm       要素
     * @param attrName  属性名
     * @param attrValue 属性値
     */
    Element attribute(Element elm, String attrName, String attrValue);

    /**
     * 要素から属性名で属性値を取得する
     *
     * @param elm      要素
     * @param attrName 属性名
     * @return 属性値
     */
    String attribute(Element elm, String attrName);

    /**
     * 要素から属性名で属性値を取得する
     *
     * @param attrName 属性名
     * @return 属性値
     */
    String attribute(String attrName);

    /**
     * 属性マップを取得する
     *
     * @param elm 要素
     * @return 属性マップ
     */
    AttributeMap attributeMap(Element elm);

    /**
     * 属性マップをセットする
     * @param elm 要素
     * @param attrMap 属性マップ
     * @return 要素
     */
    Element attributeMap(Element elm,AttributeMap attrMap);

    /**
     * 属性を削除する
     *
     * @param elm      要素
     * @param attrName 属性名
     */
    Element removeAttribute(Element elm, String attrName);

    /**
     * 属性を削除する
     *
     * @param attrName 属性名
     */
    void removeAttribute(String attrName);

    /**
     * 要素の内容をセットする
     *
     * @param elm       要素
     * @param content   要素の内容
     * @param entityRef エンティティ参照フラグ
     */
    Element content(Element elm, String content, boolean entityRef);

    /**
     * 要素の内容をセットする
     *
     * @param content   要素の内容
     * @param entityRef エンティティ参照フラグ
     */
    Element content(String content, boolean entityRef);

    /**
     * 要素の内容をセットする
     *
     * @param elm     要素
     * @param content 要素の内容
     */
    Element content(Element elm, String content);

    /**
     * 要素の内容をセットする
     *
     * @param content 要素の内容
     */
    Element content(String content);

    /**
     * 要素の内容を取得する
     *
     * @param elm 要素
     * @return 要素の内容
     */
    String content(Element elm);

    /**
     * 要素を削除する
     *
     * @param elm 要素
     */
    Element removeElement(Element elm);

    /**
     * 要素名とID属性でCX(コメント拡張)タグを検索する
     *
     * @param elmName 要素名
     * @param id      ID属性値
     * @return 要素
     */
    Element cxTag(String elmName, String id);

    /**
     * ID属性でCX(コメント拡張)タグを検索する
     *
     * @param id ID属性値
     * @return 要素
     */
    Element cxTag(String id);

    /**
     * 反映する
     */
    void flush();

    /**
     * 要素に対してHookerサブクラスに記述した処理を行う
     *
     * @param elm  要素
     * @param hook Hookerサブクラス
     */
    void execute(Element elm, Hooker hook);

    /**
     * 要素に対してLooperサブクラスに記述した処理を行う
     *
     * @param elm  要素
     * @param hook Looperサブクラス
     * @param list ループ処理用データ
     */
    void execute(Element elm, Looper hook, List list);

    /**
     * ルート要素を取得する
     *
     * @return ルート要素
     */
    RootElement rootElement();

    /**
     * ドキュメントを取得する
     *
     * @return ドキュメント
     */
    String document();


    int docType();

    LinkedHashMap<Integer, Element> elementCache();
}
