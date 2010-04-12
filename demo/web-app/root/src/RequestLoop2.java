//Kuro API

import jp.kuro.meteor.*;
import jp.kuro.meteor.hook.Hooker;

//標準API
import java.util.*;

public class RequestLoop2 extends Hooker {
    HashMap hmap;

    public RequestLoop2(HashMap query) {
        hmap = query;
    }

    public void execute(Element xt) {
        HashMap query = hmap;

        //タグ検索
        Element tag = xt.child("name", "param1");
        Element tag2 = xt.child("name", "param2");

        String param1;
        String param2;

        for (int j = 0; j < 3; j++) {
            //表示ロジック
            if (query.get("submit") != null) {
                param1 = (String) ((ArrayList) query.get("param1")).get(j);
                param2 = (String) ((ArrayList) query.get("param2")).get(j);

                tag.attribute("value", param1);
                tag2.attribute("value", param2);
            }

            //出力ロジック
            xt.flush();
        }
    }
}