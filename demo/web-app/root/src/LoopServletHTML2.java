//標準API

import java.io.*;

//ServletAPI
import javax.servlet.*;
import javax.servlet.http.*;

//Kuro API
import jp.kuro.meteor.*;
import jp.kuro.meteor.printer.HttpPrinter;

public class LoopServletHTML2 extends HttpServlet {
    ParserFactory pf;
    String path2;
    //Analyzer anl;

    public void init(ServletConfig sConf) throws ServletException {
        ServletContext sc = sConf.getServletContext();

        //anl = new Analyzer();

        String path = sc.getRealPath("/WEB-INF/html/loop2.html");
        path2 = sc.getRealPath("/WEB-INF/");

        //パーサファクトリオブジェクトを生成し、"loop2.html"を読み込む
        pf = ParserFactory.build(Parser.HTML, path, "Shift_JIS");
    }

    public void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        doPost(req, res);
    }

    public void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        //Parserオブジェクトを取得する
        Parser xt = pf.parser();

        //anl.setCharacterEncoding("Shift_JIS");
        //req.setCharacterEncoding("Shift_JIS");

        //System.out.println(anl.analyze(req));
        //System.out.println(req.getParameter("textfield"));
        //System.out.println(req.getParameter("textfield2"));
        //System.out.println(req.getParameter("submit"));

        Element tag = xt.element("option", "value", "test");

        xt.execute(tag, new LoopML2());

        //反映する
        xt.flush();

        HttpPrinter prt = new HttpPrinter(res);
        prt.print(xt);

    }
}