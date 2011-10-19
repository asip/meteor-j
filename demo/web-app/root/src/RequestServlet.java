//標準API

import java.io.*;
import java.util.HashMap;

//ServletAPI
import javax.servlet.*;
import javax.servlet.http.*;

//Kuro API
import jp.kuro.meteor.*;
import jp.kuro.meteor.printer.HttpPrinter;
import jp.kuro.ur.Request;

public class RequestServlet extends HttpServlet {
    ParserFactory pf;
    String path2;
    Request anl;

    public void init(ServletConfig sConf) throws ServletException {
        ServletContext sc = sConf.getServletContext();

        String path = sc.getRealPath("/WEB-INF/html/");
        path2 = sc.getRealPath("/WEB-INF/");
        pf = new ParserFactory(path);
        pf.parser(Parser.HTML, "request.html", "Shift_JIS");
    }

    public void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        doPost(req, res);
    }

    public void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        Element root = pf.element("request");

        anl = new Request();

        anl.setCharacterEncoding("Shift_JIS");

        HashMap query = anl.analyze(req).getQuery();

        if (query.get("submit") != null && !query.get("submit").equals("")) {
            Element tag = root.element("name", "textfield");
            Element tag2 = root.element("name", "textfield2");

            tag.attribute("value", (String) query.get("textfield"));
            tag2.content((String) query.get("textfield2"));
        }

        //反映する
        root.flush();

        HttpPrinter prt = new HttpPrinter(res);
        prt.print(root);

    }
}