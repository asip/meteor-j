//標準API
import java.io.*;
import java.util.*;
//ServletAPI
import javax.servlet.*;
import javax.servlet.http.*;
//Kuro API
import jp.kuro.meteor.*;
import jp.kuro.meteor.printer.HttpPrinter;

public class LoopServletHTML3_1 extends HttpServlet {
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
        ArrayList<Hashtable<String,String>> rsVec = new ArrayList<Hashtable<String,String>>();
        for (int i = 0; i < 3; i++) {
            Hashtable<String,String> hash = new Hashtable<String,String>();
            hash.put("name", "KuroProject");
            hash.put("set", "Kuro Project オフィシャルページ");
            hash.put("url", "http://kuro.s26.xrea.com");
            rsVec.add(hash);
        }

        //赤字部分の動的ループのロジックを呼び出す。

        Element tag = xt.element("tr", "name", "loop");

        Parser xt2 = xt.child(tag);

        //タグ検索
        Element tag_ = xt2.element("a");
        Element tag2_ = xt2.cxTag("name");
        Element tag3_ = xt2.cxTag("set");

        for(Hashtable<String,String> rs:rsVec){
            //表示データの加工
            String NAME = rs.get("name");
            String SET = rs.get("set");
            if (SET.equals("")) {
                SET = " ";
            }
            String URL = rs.get("url");
            //表示ロジック
            xt2.content(tag2_, NAME);
            xt2.attribute(tag, "href", URL);
            xt2.content(tag3_, SET);
            //出力ロジック
            xt2.print();
        }
        xt2.flush();

        //HTTP出力する。
        HttpPrinter prt = new HttpPrinter(res);
        prt.print(xt);
    }
}