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
        String path = sc.getRealPath("/WEB-INF/html/");
        //パーサファクトリオブジェクトを生成し、"loop.html"を読み込む
        pf = new ParserFactory(path);
        pf.parser(Parser.HTML, "loop.html", "Shift_JIS");
    }

    public void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        doPost(req, res);
    }

    public void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        //ルート要素オブジェクトを取得する
        Element root = pf.element("loop");

        //表示用データのセット
        ArrayList<Hashtable<String, String>> rsVec = new ArrayList<Hashtable<String, String>>();
        for (int i = 0; i < 3; i++) {
            Hashtable<String, String> hash = new Hashtable<String, String>();
            hash.put("name", "KuroProject");
            hash.put("set", "Kuro Project オフィシャルページ");
            hash.put("url", "http://kuro.s26.xrea.com");
            rsVec.add(hash);
        }

        //赤字部分の動的ループのロジックを呼び出す。

        Element tag = root.element("tr", "name", "loop");

        Element xt2 = tag.element();

        //タグ検索
        Element tag_ = xt2.element("a");
        Element tag2_ = xt2.cxTag("name");
        Element tag3_ = xt2.cxTag("set");

        for (Hashtable<String, String> rs : rsVec) {
            //表示データの加工
            String NAME = rs.get("name");
            String SET = rs.get("set");
            if (SET.equals("")) {
                SET = " ";
            }
            String URL = rs.get("url");
            //表示ロジック
            tag2_.clone().content(NAME);
            tag_.clone().attribute("href", URL);
            tag3_.clone().content(SET);
            //出力ロジック
            xt2.flush();
        }

        //反映する
        root.flush();

        //HTTP出力する。
        HttpPrinter prt = new HttpPrinter(res);
        prt.print(root);
    }
}