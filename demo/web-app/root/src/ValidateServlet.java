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

        String path = sc.getRealPath("/WEB-INF/html/");

        pf = new ParserFactory(path);
        pf.parser(Parser.HTML, "validate.html", "Shift_JIS");

        anl = new Request();
    }

    public void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        doPost(req, res);
    }

    public void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        Element root = pf.element("validate");

        anl.setCharacterEncoding("Shift_JIS");

        HashMap query = anl.analyze(req).getQuery();

        Trigger tr = new Trigger();
        tr.add("textfield", new Validator1());
        tr.add("textfield2", new Validator2());
        tr.add("textfield2", new Validator3());

        if (query.get("submit") != null && !query.get("submit").equals("")) {
            Element tag = root.element("name", "textfield");
            Element tag2 = root.element("name", "textfield2");
            Element tag3 = root.cxTag("warning");

            Result re = tr.validate(anl);

            if (!re.result()) {
                HashMap detail = re.detail();

                for (Object o : detail.keySet()) {
                    String key = (String) o;
                    ParamResult pr = (ParamResult) detail.get(key);
                    if (!pr.getResult()) {
                        tag3.content(pr.getMessage());
                    }
                }
            }

            tag.attribute("value", (String) query.get("textfield"));
            tag2.content((String) query.get("textfield2"));

        }

        //反映する
        root.flush();

        HttpPrinter prt = new HttpPrinter(res);
        prt.print(root);

    }
}