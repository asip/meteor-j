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

package jp.kuro.meteor.exception;

import java.util.LinkedHashMap;

/**
 * 要素未捕捉例外クラス
 *
 * @author Yasumasa Ashida
 * @version 0.9.3.7
 * @since 2007/07/28 17:33:44
 */
public class NoSuchElementException extends RuntimeException {

    private String elmName;
    private String attrName;
    private String attrValue;
    private String attrName2;
    private String attrValue2;

    private int type;

    private String message;

    public NoSuchElementException(String elmName) {
        this.elmName = elmName;
        this.type = 1;
    }

    public NoSuchElementException(String elmName, String attrName, String attrValue) {
        this.elmName = elmName;
        this.attrName = attrName;
        this.attrValue = attrValue;
        this.type = 2;
    }

    public NoSuchElementException(String elmName, String attrName, String attrValue, String attrName2, String attrValue2) {
        this.elmName = elmName;
        this.attrName = attrName;
        this.attrValue = attrValue;
        this.attrName2 = attrName2;
        this.attrValue2 = attrValue2;
        this.type = 3;
    }

    public NoSuchElementException(String attrName, String attrValue) {
        this.attrName = attrName;
        this.attrValue = attrValue;
        this.type = 4;
    }

    public NoSuchElementException(String attrName, String attrValue, String attrName2, String attrValue2) {
        this.attrName = attrName;
        this.attrValue = attrValue;
        this.attrName2 = attrName2;
        this.attrValue2 = attrValue2;
        this.type = 5;
    }

    public String getMessage() {
        if (type == 1) {
            this.message = this.elmName + " element not found";
        } else if (type == 2) {
            this.message = this.elmName + " element(" + this.attrName + "='" + this.attrValue + "') not found";
        } else if (type == 3) {
            this.message = this.elmName + " element(" + this.attrName + "='" + this.attrValue
                    + "'," + this.attrName2 + "='" + this.attrValue2 + "') not found";
        } else if (type == 4) {
            this.message = "element(" + this.attrName + "='" + this.attrValue + "') not found";
        } else if (type == 5) {
            this.message = "element(" + this.attrName + "='" + this.attrValue
                    + "'," + this.attrName2 + "='" + this.attrValue2 + "') not found";
        }

        return message;
    }

    public String getElementName() {
        return this.elmName;
    }

    public LinkedHashMap getAtributes() {
        LinkedHashMap<String, String> attributes = new LinkedHashMap<String, String>();
        if (this.type == 2 || this.type == 3 || this.type == 4 || this.type == 5) {
            attributes.put(this.attrName, this.attrValue);
        }
        if (this.type == 3 || this.type == 5) {
            attributes.put(this.attrName2, this.attrValue2);
        }
        return attributes;
    }

}
