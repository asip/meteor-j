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

import jp.kuro.meteor.core.html.ParserImpl;

/**
 * パーサファクトリクラス
 *
 * @author Yasumasa Ashida
 * @version 0.9.3.4
 * @since 2005/02/24 16:29:25
 */
public final class ParserFactory {
    private Parser pif = null;

    /**
     * パーサファクトリを生成する。マークアップタイプがParserIF.HTMLならHTML用パーサファクトリ、<br>
     * Parser.XHTMLならXHTML用パーサファクトリ、Parser.XMLならXML用パーサファクトリを生成。
     *
     * @param type     マークアップタイプ
     * @param path     ファイルパス
     * @param encoding 文字エンコーディング
     * @return パーサファクトリ
     */
    public static ParserFactory build(int type, String path, String encoding) {
        ParserFactory psf = new ParserFactory();

        switch (type) {
            case Parser.HTML:
                ParserImpl html = new ParserImpl();
                html.read(path, encoding);
                psf.setParser(html);
                break;
            case Parser.XHTML:
                jp.kuro.meteor.core.xhtml.ParserImpl xhtml = new jp.kuro.meteor.core.xhtml.ParserImpl();
                xhtml.read(path, encoding);
                psf.setParser(xhtml);
                break;
            case Parser.XML:
                jp.kuro.meteor.core.xml.ParserImpl xml = new jp.kuro.meteor.core.xml.ParserImpl();
                xml.read(path, encoding);
                psf.setParser(xml);
                break;
        }

        return psf;

    }

    /**
     * パーサファクトリを生成する。マークアップタイプがParserIF.HTMLならHTML用パーサファクトリ、<br>
     * Parser.XHTMLならXHTML用パーサファクトリ、Parser.XMLならXML用パーサファクトリを生成。
     *
     * @param type     マークアップタイプ
     * @param document ドキュメント
     * @return パーサファクトリ
     */
    public static ParserFactory build(int type, String document) {
        ParserFactory psf = new ParserFactory();

        switch (type) {
            case Parser.HTML:
                ParserImpl html = new ParserImpl();
                html.parse(document);
                psf.setParser(html);
                break;
            case Parser.XHTML:
                jp.kuro.meteor.core.xhtml.ParserImpl xhtml = new jp.kuro.meteor.core.xhtml.ParserImpl();
                xhtml.parse(document);
                psf.setParser(xhtml);
                break;
            case Parser.XML:
                jp.kuro.meteor.core.xml.ParserImpl xml = new jp.kuro.meteor.core.xml.ParserImpl();
                xml.parse(document);
                psf.setParser(xml);
                break;
        }

        return psf;

    }

    /**
     * パーサをセットする
     *
     * @param pif パーサ
     */
    public final void setParser(Parser pif) {
        this.pif = pif;
    }


    /**
     * パーサを取得する
     *
     * @return パーサ
     */
    public final Parser parser() {
        Parser pif2 = null;

        if (pif instanceof ParserImpl) {
            pif2 = new ParserImpl(pif);
        } else if (pif instanceof jp.kuro.meteor.core.xhtml.ParserImpl) {
            pif2 = new jp.kuro.meteor.core.xhtml.ParserImpl(pif);
        } else if (pif instanceof jp.kuro.meteor.core.xml.ParserImpl) {
            pif2 = new jp.kuro.meteor.core.xml.ParserImpl(pif);
        }

        return pif2;

    }

}
