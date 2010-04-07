//標準API
import java.io.*;

//ServletAPI
import javax.servlet.*;
import javax.servlet.http.*;

//Kuro API
import jp.kuro.meteor.*;
import jp.kuro.meteor.printer.HttpPrinter;

public class ElementServlet1 extends HttpServlet {
    ParserFactory pf;

    public void init(ServletConfig sConf) throws ServletException {
        //"element1.html"の絶対パスを取得する
        ServletContext sc = sConf.getServletContext();
        String path = sc.getRealPath("/WEB-INF/html/element1.html");

        //パーサファクトリオブジェクトを生成し、"element1.html"を読み込む
        pf = ParserFactory.build(Parser.HTML, path, "Shift_JIS");
    }

    public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        doPost(req, res);
    }

    public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        //Parserオブジェクトを取得する
        Parser xt = pf.parser();
        //"Hello,World"を"こんにちは、世界！"に変更します。
        Element tag = xt.element("font", "id", "hello");
        xt.content(tag, "こんにちは、世界");
        //HTTP出力する
        HttpPrinter prt = new HttpPrinter(res);
        prt.print(xt);
    }
}