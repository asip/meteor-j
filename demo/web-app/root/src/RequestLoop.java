//Kuro API
import jp.kuro.meteor.*;
import jp.kuro.meteor.hook.Hooker;

//標準API
import java.util.*;

public class RequestLoop extends Hooker {
    HashMap hmap;

    public RequestLoop(HashMap query) {
        this.hmap = query;
    }

    public void execute(Parser xt) {
        HashMap query = hmap;

        //タグ検索
        Element tag = xt.element("name", "param1");
        Element tag2 = xt.element("name", "param2");

        String param1;
        String param2;

        for (int j = 0; j < 3; j++) {
            //表示ロジック
            if (query.get("submit") != null) {
                param1 = (String) ((ArrayList) query.get("param1")).get(j);
                param2 = (String) ((ArrayList) query.get("param2")).get(j);

                xt.attribute(tag, "value", param1);
                xt.attribute(tag2, "value", param2);
            }

            //出力ロジック
            xt.print();
        }
    }
}