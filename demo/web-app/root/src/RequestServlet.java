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

        String path = sc.getRealPath("/WEB-INF/html/request.html");
        path2 = sc.getRealPath("/WEB-INF/");

        pf = ParserFactory.build(Parser.HTML, path, "Shift_JIS");
    }

    public void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        doPost(req, res);
    }

    public void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        Parser xt = pf.parser();

        anl = new Request();

        anl.setCharacterEncoding("Shift_JIS");

        HashMap query = anl.analyze(req).getQuery();

        if (query.get("submit") != null && !query.get("submit").equals("")) {
            Element tag = xt.element("name", "textfield");
            Element tag2 = xt.element("name", "textfield2");

            xt.attribute(tag, "value", (String) query.get("textfield"));
            xt.content(tag2, (String) query.get("textfield2"));
        }

        HttpPrinter prt = new HttpPrinter(res);
        prt.print(xt);

    }
}