//標準API

import java.io.*;

//ServletAPI
import javax.servlet.*;
import javax.servlet.http.*;

//Kuro API
import jp.kuro.meteor.*;
import jp.kuro.meteor.printer.HttpPrinter;

public class HelloServlet extends HttpServlet {
    ParserFactory pf;

    public void init(ServletConfig sConf) throws ServletException {
        //"hello.html"の絶対パスを取得する
        ServletContext sc = sConf.getServletContext();
        String path = sc.getRealPath("/WEB-INF/html/");

        //パーサファクトリオブジェクトを生成し、"hello.html"を読み込む
        pf = new ParserFactory(path);
        pf.parser(Parser.HTML, "hello.html", "Shift_JIS");
    }

    public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        doPost(req, res);
    }

    public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        //Parserオブジェクトを取得する
        Parser xt = pf.parser("hello");
        //"Hello,World"を"こんにちは、世界！"に変更する
        Element tag = xt.cxTag("hello");
        tag.content("こんにちは、世界！");
        //反映する
        xt.flush();
        //HTTP出力する
        HttpPrinter prt = new HttpPrinter(res);
        prt.print(xt);
    }
}