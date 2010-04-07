//標準API
import java.io.*;
import java.util.*;
//ServletAPI
import javax.servlet.*;
import javax.servlet.http.*;
//Kuro API
import jp.kuro.meteor.*;
import jp.kuro.meteor.printer.HttpPrinter;

public class LoopServletHTML extends HttpServlet {
    ParserFactory pf;

    public void init(ServletConfig sConf) throws ServletException {
        //"loop.html"の絶対パスを取得する
        ServletContext sc = sConf.getServletContext();
        String path = sc.getRealPath("/WEB-INF/html/loop.html");
        //パーサファクトリオブジェクトを生成し、"loop.html"を読み込む
        pf = ParserFactory.build(Parser.HTML, path, "Shift_JIS");
    }

    public void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        doPost(req, res);
    }

    public void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        //Parserオブジェクトのコピーを取得する
        Parser xt = pf.parser();

        //表示用データのセット
        ArrayList<HashMap> rsVec = new ArrayList<HashMap>();
        for (int i = 0; i < 3; i++) {
            HashMap<String,String> hash = new HashMap<String,String>();
            hash.put("name", "KuroProject");
            hash.put("set", "Kuro Project オフィシャルページ");
            hash.put("url", "http://kuro.s26.xrea.com");
            rsVec.add(hash);
        }

        //赤字部分の動的ループのロジックを呼び出す。

        Element tag = xt.element("tr", "name", "loop");
        xt.execute(tag, new LoopML(), rsVec);
        //HTTP出力する。
        HttpPrinter prt = new HttpPrinter(res);
        prt.print(xt);
    }
}