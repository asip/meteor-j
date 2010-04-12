//標準API

import java.io.*;
import java.util.HashMap;

//ServletAPI
import javax.servlet.*;
import javax.servlet.http.*;

//Kuro API
import jp.kuro.meteor.*;
import jp.kuro.meteor.printer.HttpPrinter;
import jp.kuro.ur.*;

public class ValidateServlet extends HttpServlet {
    ParserFactory pf;
    Request anl;

    public void init(ServletConfig sConf) throws ServletException {
        ServletContext sc = sConf.getServletContext();

        String path = sc.getRealPath("/WEB-INF/html/validate.html");

        pf = ParserFactory.build(Parser.HTML, path, "Shift_JIS");

        anl = new Request();
    }

    public void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        doPost(req, res);
    }

    public void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        Parser xt = pf.parser();

        anl.setCharacterEncoding("Shift_JIS");

        HashMap query = anl.analyze(req).getQuery();

        Trigger tr = new Trigger();
        tr.add("textfield", new Validator1());
        tr.add("textfield2", new Validator2());
        tr.add("textfield2", new Validator3());

        if (query.get("submit") != null && !query.get("submit").equals("")) {
            Element tag = xt.element("name", "textfield");
            Element tag2 = xt.element("name", "textfield2");
            Element tag3 = xt.cxTag("warning");

            Result re = tr.validate(anl);

            if (!re.result()) {
                HashMap detail = re.detail();

                for (Object o : detail.keySet()) {
                    String key = (String) o;
                    ParamResult pr = (ParamResult) detail.get(key);
                    if (!pr.getResult()) {
                        xt.content(tag3, pr.getMessage());
                    }
                }
            }

            xt.attribute(tag, "value", (String) query.get("textfield"));
            xt.content(tag2, (String) query.get("textfield2"));

        }

        //反映する
        xt.flush();

        HttpPrinter prt = new HttpPrinter(res);
        prt.print(xt);

    }
}