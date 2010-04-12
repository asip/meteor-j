import jp.kuro.meteor.hook.Hooker;
import jp.kuro.meteor.*;

public class TestHook extends Hooker {

    public void execute(Element xt) {
        Element tag = xt.child("tech", "mono", "mono");

        for (int i = 0; i < 5; i++) {
            tag.content("konkon" + i);
            tag.flush();
        }
    }

}
