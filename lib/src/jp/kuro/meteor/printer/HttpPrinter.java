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

package jp.kuro.meteor.printer;

import jp.kuro.meteor.Parser;

import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.io.IOException;

/**
 * HTTPプリンタ
 *
 * @author Yasumasa Ashida
 * @version 0.9.4.2
 * @since 2004/12/05 13:11:58
 */
public class HttpPrinter {
    HttpServletResponse res;

    public HttpPrinter(HttpServletResponse res) {
        this.res = res;
    }

    public final void print(Parser pif) throws IOException {
        res.setContentType(pif.rootElement().contentType());
        PrintWriter pw = res.getWriter();
        pw.print(pif.rootElement().document());
        pw.flush();
        pw.close();
    }
}
