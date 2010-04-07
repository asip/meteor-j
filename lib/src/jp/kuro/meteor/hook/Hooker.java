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

/**
 * フック処理クラス
 * @author Yasumasa Ashida
 * @version 0.9.0.0
 *
 */
public abstract class Hooker {
    /**
     * デフォルトコンストラクタ
     */
    /*public Hooker() {
    }*/

    /**
     * フック処理(実体)
     *
     * @param elm Tagオブジェクト
     * @param pif  Parserオブジェクト
     */
    public final void doAction(Element elm, Parser pif) {
        //要素ありタグの場合
        if (elm.empty()) {
            Parser pif2;

            pif2 = pif.child(elm);
            execute(pif2);
            pif2.flush();
        }
    }

    /**
     * フック処理を実装する
     *
     * @param pif Parserオブジェクト
     */
    public abstract void execute(Parser pif);
	
}