import jp.kuro.meteor.*;
import jp.kuro.meteor.hook.Hooker;

public class LoopML2 extends Hooker {
    public void execute(Element xt) {

        for (int j = 0; j < 3; j++) {
            xt.attribute("value", Integer.toString(j));
            xt.content("test" + Integer.toString(j));
            xt.flush();
        }
    }
}