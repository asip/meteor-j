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
import javax.servlet.http.HttpServletRequest;
import java.io.PrintWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.GZIPOutputStream;

/**
 * HTTPプリンタ(GZIP圧縮)
 *
 * @author Yasumasa Ashida
 * @version 0.9.5.0
 * @since 2004/12/05 13:11:58
 */
public class GZipHttpPrinter {
    private static final String EMPTY = "";

    private static final String ACCEPT_ENCODING = "Accept-Encoding";
    private static final String X_GZIP = "x-gzip";
    private static final String GZIP = "gzip";
    private static final String CONTENT_ENCODING = "Content-Encoding";

    HttpServletRequest req;
    HttpServletResponse res;

    public GZipHttpPrinter(HttpServletRequest req, HttpServletResponse res) {
        this.req = req;
        this.res = res;
    }

    public final void print(Parser pif) throws IOException {

        String acceptEncoding;
        String transferEncoding;

        acceptEncoding = req.getHeader(ACCEPT_ENCODING);

        if (acceptEncoding == null) {
            acceptEncoding = EMPTY;
        }

        acceptEncoding = acceptEncoding.toLowerCase();

        if (acceptEncoding.indexOf(X_GZIP) >= 0) {
            transferEncoding = X_GZIP;
        } else {
            if (acceptEncoding.indexOf(GZIP) >= 0) {
                transferEncoding = GZIP;
            } else {
                transferEncoding = EMPTY;
            }
        }

        res.setContentType(pif.rootElement().contentType());

        if (!EMPTY.equals(transferEncoding) && pif.rootElement().characterEncoding() != null) {

            res.setHeader(CONTENT_ENCODING, transferEncoding);

            OutputStream osr = res.getOutputStream();
            GZIPOutputStream gos = new GZIPOutputStream(osr);

            gos.write(pif.rootElement().document().getBytes(pif.rootElement().characterEncoding()));

            gos.flush();
            gos.close();

        } else {
            PrintWriter pw = res.getWriter();
            pw.print(pif.rootElement().document());
            pw.flush();
            pw.close();

        }
    }
}
