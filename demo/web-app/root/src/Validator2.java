import jp.kuro.ur.*;

/**
 *
 * @author Yasumasa Ashida
 * @since 2003/02/04 8:32:45
 */
public class Validator2 implements Validator {
    public final ParamResult validate(Object obj) {
        ParamResult pr = new ParamResult();

        String tmp = (String) obj;

        if (tmp.getBytes().length > 20) {
            pr.setResult(false);
            pr.setMessage("メモ２が半角20文字を超えています");
        } else {
            pr.setResult(true);
        }

        return pr;
    }
}
