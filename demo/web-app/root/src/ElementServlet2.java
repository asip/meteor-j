//標準API

import java.io.*;

//ServletAPI
import javax.servlet.*;
import javax.servlet.http.*;

//Kuro API
import jp.kuro.meteor.*;
import jp.kuro.meteor.printer.HttpPrinter;

public class ElementServlet2 extends HttpServlet {
    ParserFactory pf;

    public void init(ServletConfig sConf) throws ServletException {
        //"element2.html"の絶対パスを取得する
        ServletContext sc = sConf.getServletContext();
        String path = sc.getRealPath("/WEB-INF/html/element2.html");

        //パーサファクトリオブジェクトを生成し、"element2.html"を読み込む
        pf = ParserFactory.build(Parser.HTML, path, "Shift_JIS");
    }

    public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        doPost(req, res);
    }

    public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        //Parserオブジェクトを取得する
        Parser xt = pf.parser();
        //fontタグのcolor属性を消します。
        Element tag = xt.cxTag("hello");
        xt.content(tag, "こんにちは、世界！");
        //反映する
        xt.flush();
        //HTTP出力する
        HttpPrinter prt = new HttpPrinter(res);
        prt.print(xt);
    }
}