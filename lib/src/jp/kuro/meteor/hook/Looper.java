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

package jp.kuro.meteor.hook;

import jp.kuro.meteor.Element;
import jp.kuro.meteor.Parser;

import java.util.List;

/**
 * ループ専用フック処理クラス
 * @author Yasumasa Ashida
 * @version 0.9.0.0
 * @since 2006/04/16 19:37:20
 */
public abstract class Looper {

   /**
     * フック処理(実体-値渡し-)
     *
     * @param elm  Tagオブジェクト
     * @param pif   Parserオブジェクト
     * @param list Listオブジェクト
     */
    public final void doAction(Element elm, Parser pif, List list) {
        //要素ありタグの場合
        if (elm.empty()) {
            Parser pif2 = pif.child(elm);

            init(pif2);

            for (Object aList : list) {

                if (pif2.rootElement().hook()) {
                    pif2.rootElement().document(elm.mixedContent());
                } else if (pif2.rootElement().monoHook()) {
                    //pif2.rootElement().document(elm.document());
                }
                execute(pif2.rootElement().mutableElement(),aList);
                pif2.print();
            }
            pif2.flush();

            //todo
            //System.out.println(pif2.hookDocument());
        }
    }

    protected void init(Parser pif){};

    /**
     * フック処理を実装する
     *
     * @param item Objectオブジェクト
     */
    protected abstract void execute(Element elm,Object item);

}
