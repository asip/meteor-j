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

import java.util.HashMap;
//import java.util.ArrayList;

/**
 * 属性マップクラス
 *
 * @author Yasumasa Ashida
 * @version 0.9.4.2
 * @since 2006/03/26 18:11:47
 */
public class AttributeMap {
    private HashMap<String, Attribute> map;
    //private ArrayList<String> names;
    private boolean recordable;

    /**
     * デフォルトコンストラクタ
     */
    public AttributeMap() {
        map = new HashMap<String, Attribute>();
        //names = new ArrayList<String>();
    }


    public AttributeMap(AttributeMap attrMap) {
        map = attrMap.map;
        recordable = attrMap.recordable;
    }

    /**
     * 属性をセットする
     *
     * @param name  属性名
     * @param value 属性値
     */
    public final void store(String name, String value) {
        Attribute attr;

        if (map.get(name) == null) {
            //属性の追加
            attr = new Attribute();
            attr.setName(name);
            attr.setValue(value);
            if (recordable) {
                attr.setChanged(true);
            }
            map.put(name, attr);
            //names.add(name);
        } else {
            //属性の変更
            attr = map.get(name);
            attr.setValue(value);
            if (recordable) {
                if (!attr.value().equals(value)) {
                    attr.setChanged(true);
                    attr.setRemoved(false);
                }
            }

            map.put(name, attr);
        }

    }

    /**
     * 属性を削除する
     *
     * @param name 属性名
     */
    public final void delete(String name) {
        if (recordable && map.containsKey(name)) {
            map.get(name).setChanged(false);
            map.get(name).setRemoved(true);
        }
    }

    /**
     * 属性名配列を取得する
     *
     * @return 属性名配列
     */
    public final String[] names() {
        //String[] nmArray = new String[names.size()];
        //for(int i=0;i < names.size();i++){
        //    nmArray[i] = names.get(i);
        //}
        //return nmArray;
        //String[] nmArray = new String[0];
        //nmArray = map.keySet().toArray(nmArray);
        return map.keySet().toArray(new String[map.keySet().size()]);
    }

    /**
     * 属性値を取得する
     *
     * @param name 属性名
     * @return 属性値
     */
    public final String fetch(String name) {
        if (map.get(name) != null && !map.get(name).removed()) {
            return map.get(name).value();
        } else {
            return null;
        }
    }

    /**
     * 変更フラグを取得する
     *
     * @param name 属性値
     * @return 変更フラグ
     */
    public final boolean changed(String name) {
        return map.get(name).changed();
    }


    /**
     * 変更記録フラグを取得する
     *
     * @return 変更記録フラグ
     */
    public boolean recordable() {
        return recordable;
    }

    /**
     * 変更記録フラグをセットする
     *
     * @param recordable 変更記録フラグ
     */
    public void setRecordable(boolean recordable) {
        this.recordable = recordable;
    }

    /**
     * 属性クラス
     */
    class Attribute {

        //属性名
        private String name;
        //属性値
        private String value;
        //変更フラグ
        private boolean changed;
        //削除フラグ
        private boolean removed;

        /**
         * 属性名を取得する
         *
         * @return 属性名
         */
        public String name() {
            return name;
        }

        /**
         * 属性名をセットする
         *
         * @param name 属性名
         */
        public void setName(String name) {
            this.name = name;
        }

        /**
         * 属性値を取得する
         *
         * @return 属性値
         */
        public String value() {
            return value;
        }

        /**
         * 属性値をセットする
         *
         * @param value 属性値
         */
        public void setValue(String value) {
            this.value = value;
        }

        /**
         * 変更フラグを取得する
         *
         * @return 変更フラグ
         */
        public boolean changed() {
            return changed;
        }

        /**
         * 変更フラグをセットする
         *
         * @param changed 変更フラグ
         */
        public void setChanged(boolean changed) {
            this.changed = changed;
        }

        /**
         * 削除フラグを取得する
         *
         * @return 削除フラグ
         */
        public boolean removed() {
            return removed;
        }

        /**
         * 削除フラグをセットする
         *
         * @param removed 削除フラグ
         */
        public void setRemoved(boolean removed) {
            this.removed = removed;
        }
    }
}
