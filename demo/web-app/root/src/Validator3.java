import jp.kuro.ur.*;

public class Validator3 implements Validator {
    public final ParamResult validate(Object obj) {
        ParamResult pr = new ParamResult();

        String tmp = (String) obj;

        if (tmp.getBytes().length == 0) {
            pr.setResult(false);
            pr.setMessage("メモ２は必須入力項目です");
        } else {
            pr.setResult(true);
        }

        return pr;
    }
}
