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

import java.io.File;
import java.util.HashMap;
import java.util.regex.Pattern;

/**
 * パーサファクトリクラス
 *
 * @author Yasumasa Ashida
 * @version 0.9.5.0
 * @since 2005/02/24 16:29:25
 */
public final class ParserFactory {

    //private static final String ABST_EXT_NAME = ".*";
    private static final String CURRENT_DIR = ".";
    private static final String COMMA = Pattern.quote(".");
    private static final String SLASH = "/";
    private static final String ENC_UTF8 = "UTF-8";

    private String baseDir;      // 基準ディレクトリ
    private String base_encoding; // デフォルトエンコーディング

    private HashMap<String,Parser> cache = new HashMap<String,Parser>();

    private String[] paths;
    private String relativeUrl;
    Parser pif;
    private StringBuilder sbuf = new StringBuilder();

    /**
     * コンストラクタ
     */
    public ParserFactory(){
        this.baseDir = CURRENT_DIR;
        this.base_encoding = ENC_UTF8;
    }

    /**
     * コンストラクタ
     * @param bsDir 基準ディレクトリ
     */
    public ParserFactory(String bsDir){
        this.baseDir = bsDir;
        this.base_encoding = ENC_UTF8;
    }

    /**
     * コンストラクタ
     * @param bsDir 基準ディレクトリ
     * @param bsEncoding デフォルトエンコーディング
     */
    public ParserFactory(String bsDir,String bsEncoding){
        this.baseDir = bsDir;
        this.base_encoding = bsEncoding;
    }

    /**
     * パーサを生成する。マークアップタイプがParser.HTMLならHTMLパーサ、<br>
     * Parser.XHTMLならXHTML用パーサ、Parser.HTML5ならHTML5用パーサ、
     * Parser.XHTML5ならXHTML5用パーサ、Parser.XMLならXML用パーサを生成。
     *
     * @param type     マークアップタイプ
     * @param relativePath     相対ファイルパス
     * @param encoding 文字エンコーディング
     * @return パーサファクトリ
     */
    public Parser parser(int type, String relativePath, String encoding) {

        paths = relativePath.split(File.separator);

        if(paths.length == 1){
            relativeUrl = paths[0].split(COMMA)[0];
        }else{
            sbuf.setLength(0);
            if (CURRENT_DIR.equals(paths[0])){
                paths[paths.length - 1] = paths[paths.length - 1].split(COMMA)[0];
                for(int i=1;i < paths.length;i++){
                    if(i == 1){
                        sbuf.append(paths[i]);
                    }else{
                        sbuf.append(SLASH).append(paths[i]);
                    }
                }

            }else{
                paths[paths.length - 1] = paths[paths.length - 1].split(COMMA)[0];
                for(int i = 0;i < paths.length;i++){
                    if(i == 1){
                        sbuf.append(paths[i]);
                    }else{
                        sbuf.append(SLASH).append(paths[i]);
                    }
                }
            }
            relativeUrl = sbuf.toString();
        }

        switch (type) {
            case Parser.HTML:
                ParserImpl html = new ParserImpl();
                html.read(new File(baseDir,relativePath).getAbsolutePath(), encoding);
                return cache.put(relativeUrl,html);
            case Parser.XHTML:
                jp.kuro.meteor.core.xhtml.ParserImpl xhtml = new jp.kuro.meteor.core.xhtml.ParserImpl();
                xhtml.read(new File(baseDir,relativePath).getAbsolutePath(), encoding);
                return cache.put(relativeUrl,xhtml);
            case Parser.HTML5:
                jp.kuro.meteor.core.html5.ParserImpl html5 = new jp.kuro.meteor.core.html5.ParserImpl();
                html5.read(new File(baseDir,relativePath).getAbsolutePath(), encoding);
                return cache.put(relativeUrl,html5);
            case Parser.XHTML5:
                jp.kuro.meteor.core.xhtml5.ParserImpl xhtml5 = new jp.kuro.meteor.core.xhtml5.ParserImpl();
                xhtml5.read(new File(baseDir,relativePath).getAbsolutePath(), encoding);
                return cache.put(relativeUrl,xhtml5);
            case Parser.XML:
                jp.kuro.meteor.core.xml.ParserImpl xml = new jp.kuro.meteor.core.xml.ParserImpl();
                xml.read(new File(baseDir,relativePath).getAbsolutePath(), encoding);
                return cache.put(relativeUrl,xml);
        }
        return null;
    }

    /**
     * パーサを生成する。マークアップタイプがParser.HTMLならHTML用パーサ、<br>
     * Parser.XHTMLならXHTML用パーサ、Parser.HTML5ならHTML5用パーサ、
     * Parser.XHTML5ならXHTML5用パーサ、Parser.XMLならXML用パーサを生成。
     *
     * @param type         マークアップタイプ
     * @param relativePath 相対ファイルパス
     * @return パーサ
     */
    public Parser parser(int type, String relativePath) {

        paths = relativePath.split(File.separator);

        if(paths.length == 1){
            relativeUrl = paths[0].split(COMMA)[0];
        }else{
            sbuf.setLength(0);
            if (CURRENT_DIR.equals(paths[0])){
                paths[paths.length - 1] = paths[paths.length - 1].split(COMMA)[0];
                for(int i=1;i < paths.length;i++){
                    if(i == 1){
                        sbuf.append(paths[i]);
                    }else{
                        sbuf.append(SLASH).append(paths[i]);
                    }
                }

            }else{
                paths[paths.length - 1] = paths[paths.length - 1].split(COMMA)[0];
                for(int i = 0;i < paths.length;i++){
                    if(i == 1){
                        sbuf.append(paths[i]);
                    }else{
                        sbuf.append(SLASH).append(paths[i]);
                    }
                }
            }
            relativeUrl = sbuf.toString();
        }

        switch (type) {
            case Parser.HTML:
                ParserImpl html = new ParserImpl();
                html.read(new File(baseDir,relativePath).getAbsolutePath(), base_encoding);
                return cache.put(relativeUrl,html);
            case Parser.XHTML:
                jp.kuro.meteor.core.xhtml.ParserImpl xhtml = new jp.kuro.meteor.core.xhtml.ParserImpl();
                xhtml.read(new File(baseDir,relativePath).getAbsolutePath(), base_encoding);
                return cache.put(relativeUrl,xhtml);
            case Parser.HTML5:
                jp.kuro.meteor.core.html5.ParserImpl html5 = new jp.kuro.meteor.core.html5.ParserImpl();
                html5.read(new File(baseDir,relativePath).getAbsolutePath(), base_encoding);
                return cache.put(relativeUrl,html5);
            case Parser.XHTML5:
                jp.kuro.meteor.core.xhtml5.ParserImpl xhtml5 = new jp.kuro.meteor.core.xhtml5.ParserImpl();
                xhtml5.read(new File(baseDir,relativePath).getAbsolutePath(), base_encoding);
                return cache.put(relativeUrl,xhtml5);
            case Parser.XML:
                jp.kuro.meteor.core.xml.ParserImpl xml = new jp.kuro.meteor.core.xml.ParserImpl();
                xml.read(new File(baseDir,relativePath).getAbsolutePath(), base_encoding);
                return cache.put(relativeUrl,xml);
        }
        return null;
    }

    /**
     * パーサファクトリを生成する。マークアップタイプがParserIF.HTMLならHTML用パーサファクトリ、<br>
     * Parser.XHTMLならXHTML用パーサファクトリ、Parser.XMLならXML用パーサファクトリを生成。
     *
     * @param type     マークアップタイプ
     * @param relativeUrl 相対URL
     * @param document ドキュメント
     * @return パーサ
     */
    public Parser parser_str(int type, String relativeUrl, String document) {
        //ParserFactory psf = new ParserFactory();

        switch (type) {
            case Parser.HTML:
                ParserImpl html = new ParserImpl();
                html.parse(document);
                return cache.put(relativeUrl,html);
            case Parser.XHTML:
                jp.kuro.meteor.core.xhtml.ParserImpl xhtml = new jp.kuro.meteor.core.xhtml.ParserImpl();
                xhtml.parse(document);
                return cache.put(relativeUrl,xhtml);
            case Parser.HTML5:
                jp.kuro.meteor.core.html5.ParserImpl html5 = new jp.kuro.meteor.core.html5.ParserImpl();
                html5.parse(document);
                return cache.put(relativeUrl,html5);
            case Parser.XHTML5:
                jp.kuro.meteor.core.xhtml5.ParserImpl xhtml5 = new jp.kuro.meteor.core.xhtml5.ParserImpl();
                xhtml5.parse(document);
                return cache.put(relativeUrl,xhtml5);
            case Parser.XML:
                jp.kuro.meteor.core.xml.ParserImpl xml = new jp.kuro.meteor.core.xml.ParserImpl();
                xml.parse(document);
                return cache.put(relativeUrl,xml);
        }

        return null;

    }

    /**
     * パーサを取得する
     * @param key キー
     * @return パーサ
     */
    public final Parser parser(String key) {
        pif = cache.get(key);

        if (pif instanceof jp.kuro.meteor.core.html5.ParserImpl) {
            return new jp.kuro.meteor.core.html5.ParserImpl(pif);
        } else if (pif instanceof jp.kuro.meteor.core.xhtml5.ParserImpl) {
            return new jp.kuro.meteor.core.xhtml5.ParserImpl(pif);
        } else if (pif instanceof ParserImpl) {
            return new ParserImpl(pif);
        } else if (pif instanceof jp.kuro.meteor.core.xhtml.ParserImpl) {
            return new jp.kuro.meteor.core.xhtml.ParserImpl(pif);
        } else if (pif instanceof jp.kuro.meteor.core.xml.ParserImpl) {
            return new jp.kuro.meteor.core.xml.ParserImpl(pif);
        }

        return null;

    }

    /**
     * パーサをセットする
     * @param key キー
     * @param pif パーサ
     */
    public final void parser(String key,Parser pif) {
        cache.put(key,pif);
    }

}
