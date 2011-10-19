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

package jp.kuro.meteor.core.util;

/**
 * 非同期StringBuffer
 *
 * @author Yasumasa Ashida
 * @author Yasumasa Ashida
 * @version 0.9.7.0
 */
public final class AsyncStringBuffer {
    private final int INIT_SIZE = 32;
    private final int MORE_SIZE = 32;
    private char[] data;
    private int count;

    /**
     * デフォルトコンストラクタ
     */
    public AsyncStringBuffer() {
        data = new char[INIT_SIZE];
    }

    /**
     * 初期サイズを引数とするコンストラクタ
     *
     * @param initSize 初期サイズ
     */
    public AsyncStringBuffer(int initSize) {
        data = new char[initSize];
    }

    /**
     * @param c
     */
    public final void append(char c) {
        if (count + 1 >= data.length) {
            data = ensureCapacity(data, count + 1);
            data[count++] = c;
        }
    }

    /**
     * @param str
     */
    public final AsyncStringBuffer append(String str) {
        int len = str.length();
        if (count + len >= data.length) {
            data = ensureCapacity(data, count + len);
        }
        str.getChars(0, len, data, count);
        count += len;
        return this;
    }

    /**
     *
     * @return
     */
    /*public final int length(){
        return count;
    }*/

    /**
     * Stringオブジェクトを取得する
     *
     * @return Stringオブジェクト
     */
    public final String toString() {
        return new String(data, 0, count);
    }

    /**
     * @param resetSize
     */
    public final void setLength(int resetSize) {
        data = new char[100];
        count = 0;
    }

    /**
     * @param buf 文字配列
     * @param min
     * @return 文字配列
     */
    private char[] ensureCapacity(char[] buf, int min) {
        int newcap = buf.length + MORE_SIZE;
        if (min > newcap) newcap = min;
        char[] newbuf = new char[newcap];
        System.arraycopy(buf, 0, newbuf, 0, buf.length);
        return newbuf;
    }

}
