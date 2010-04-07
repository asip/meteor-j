import jp.kuro.ur.*;

public class Validator1 implements Validator {
    public final ParamResult validate(Object obj) {
        ParamResult pr = new ParamResult();

        String tmp = (String) obj;

        if (tmp.getBytes().length > 10) {
            pr.setResult(false);
            pr.setMessage("メモ１が半角10文字を超えています");
        } else {
            pr.setResult(true);
        }

        return pr;
    }
}
