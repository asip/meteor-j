import jp.kuro.meteor.hook.Hooker;
import jp.kuro.meteor.*;

public class TestHook extends Hooker {

    public void execute(Parser xt){
        Element tag= xt.element("tech","mono","mono");

        for(int i=0;i<5;i++){
            xt.content(tag,"konkon" + i);
            xt.print();
        }
    }

}
