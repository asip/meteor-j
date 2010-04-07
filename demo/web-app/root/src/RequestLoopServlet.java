//標準API
import java.io.*;
import java.util.*;
//ServletAPI
import javax.servlet.*;
import javax.servlet.http.*;
//Kuro API
import jp.kuro.meteor.*;
import jp.kuro.meteor.printer.HttpPrinter;
import jp.kuro.ur.*;

public class RequestLoopServlet extends HttpServlet {
    ParserFactory pf;
    Request anl;

    public void init(ServletConfig sConf) throws ServletException {
        //"request2.html"の絶対パスを取得する
        ServletContext sc = sConf.getServletContext();
        String path = sc.getRealPath("/WEB-INF/html/request2.html");
        //パーサファクトリオブジェクトを生成し、"request2.html"を読み込む
        pf = ParserFactory.build(Parser.HTML, path, "Shift_JIS");

        //リクエストオブジェクトを生成する
        anl = new Request();
    }

    public void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        doPost(req, res);
    }

    public void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        //htオブジェクトのコピーを取得する
        Parser xt = pf.parser();


        //リクエストのエンコーディングを指定する
        anl.setCharacterEncoding("Shift_JIS");
        //リクエストパラメータがマルチの場合の返却タイプを指定
        anl.setMultiType(Request.MULTI_TYPE_LIST);
        //リクエストのパラメータを取得する
        HashMap query = anl.analyze(req).getQuery();

        //赤字部分の動的ループのロジックを呼び出す。
        Element tag = xt.element("tr", "name", "loop");
        xt.execute(tag, (new RequestLoop(query)));
        //HTTP出力する。
        HttpPrinter prt = new HttpPrinter(res);
        prt.print(xt);
    }
}