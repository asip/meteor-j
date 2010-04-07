//Kuro API
import jp.kuro.meteor.*;
import jp.kuro.meteor.hook.Looper;

//標準API
import java.util.*;

public class LoopML extends Looper {

    Element tag;
    Element tag2;
    Element tag3;

    public void init(Parser xt){
        //タグ検索
        tag = xt.element("a");
        tag2 = xt.cxTag("name");
        tag3 = xt.cxTag("set");
    }

    public void execute(Element elm, Object obj) {
        //表示データの加工
        HashMap rec = (HashMap)obj;
        String NAME = (String) rec.get("name");
        String SET = (String) rec.get("set");
        if (SET.equals("")) {
            SET = " ";
        }
        String URL = (String) rec.get("url");
        //表示ロジック
        tag2.content(NAME);
        tag.attribute("href", URL);
        tag3.content(SET);

    }
}