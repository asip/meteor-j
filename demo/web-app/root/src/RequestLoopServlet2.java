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

public class RequestLoopServlet2 extends HttpServlet {
    ParserFactory pf;
    Request anl;

    public void init(ServletConfig sConf) throws ServletException {
        //"request2.html"の絶対パスを取得する
        ServletContext sc = sConf.getServletContext();
        String path = sc.getRealPath("/WEB-INF/html/");
        //パーサファクトリオブジェクトを生成し、"request2.html"を読み込む
        pf = new ParserFactory(path);
        pf.parser(Parser.HTML, "request2.html", "Shift_JIS");
        //リクエストパラメータを取得する
        anl = new Request();
    }

    public void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        doPost(req, res);
    }

    public void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        //ルート要素オブジェクトを取得する
        Element root = pf.element("request2");

        //リクエストのエンコーディングを指定する
        anl.setCharacterEncoding("Shift_JIS");
        //リクエストパラメータがマルチの場合の返却タイプを指定
        anl.setMultiType(Request.MULTI_TYPE_LIST);
        //リクエストのパラメータを解析する
        anl.analyze(req);

        Trigger tr = new Trigger();
        tr.add("param1", new Validator1());
        tr.add("param1", new Validator3());
        tr.validate(anl);
        //リクエストのパラメータを取得する
        HashMap query = anl.getQuery();

        //赤字部分の動的ループのロジックを呼び出す。
        Element tag = root.element("tr", "name", "loop");
        tag.execute(new RequestLoop2(query));

        //反映する
        root.flush();

        //HTTP出力する。
        HttpPrinter prt = new HttpPrinter(res);
        prt.print(root);
    }
}