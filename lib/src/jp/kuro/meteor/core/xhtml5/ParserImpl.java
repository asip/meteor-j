package jp.kuro.meteor.core.xhtml5;

import jp.kuro.meteor.Parser;
import jp.kuro.meteor.Element;

import java.util.regex.Pattern;

/**
 * XHTML5パーサ
 * @author Yasumasa Ashida
 * @since 2010/04/25 18:18:18
 *
 */
public class ParserImpl extends jp.kuro.meteor.core.xhtml.ParserImpl implements Parser {

    private static final String CHARSET = "charset";
    private static final String UTF8 = "utf-8";
    
    //論理値で指定する属性
    protected static final String[] ATTR_LOGIC = {"disabled","readonly","checked","selected",
            "multiple","required"};
    //disabled属性のある要素
    private static final String[] DISABLE_ELEMENT = {"input","textarea","select","optgroup",
            "fieldset"};
    //required属性のある要素
    private static final String[] REQUIRE_ELEMENT = {"input","textarea"};

    private static final String REQUIRED = "required";

    private static final String REQUIRED_M = "\\srequired=\"[^\"]*\"\\s|\\srequired=\"[^\"]*\"$";
    private static final String REQUIRED_M1 = "\\srequired=\"([^\"]*)\"\\s|\\srequired=\"([^\"]*)\"$";
    private static final String REQUIRED_R = "required=\"[^\"]*\"";
    private static final String REQUIRED_U = "required=\"required\"";

    private static final Pattern pattern_required_m = Pattern.compile(REQUIRED_M);
    private static final Pattern pattern_required_m1 = Pattern.compile(REQUIRED_M1);
    private static final Pattern pattern_required_r = Pattern.compile(REQUIRED_R);

    /**
     * コンストラクタ
     */
    public ParserImpl(){
        super();
        this.docType = Parser.HTML5;
    }

    /**
     * コピーコンストラクタ
     * @param ps パーサ
     */
    public ParserImpl(Parser ps){
        super(ps);
        this.docType = Parser.HTML5;
        this.root.setCharset(ps.rootElement().charset());
    }


    protected final void analyzeContentType() {
        element(META_S, HTTP_EQUIV, CONTENT_TYPE);

        if (elm_ == null) {
            element(META, HTTP_EQUIV, CONTENT_TYPE);
        }

        if (elm_ != null) {
            this.root.setContentType(attribute(elm_, CONTENT));
            this.root.setCharset(attribute(elm_,CHARSET));
            if(this.root.charset() == null){
                this.root.setCharset(UTF8);
            }

        } else {
            this.root.setContentType(EMPTY);
            this.root.setCharset(UTF8);
        }
    }

    protected void editAttributes_(Element elm, String attrName, String attrValue) {

        //todo
        if (isMatch(SELECTED, attrName) && isMatch(OPTION, elm.name())) {
            editAttributes_(elm, attrValue, pattern_selected_m, pattern_selected_r, SELECTED_U);
        } else if (isMatch(MULTIPLE, attrName) && isMatch(SELECT, elm.name())) {
            editAttributes_(elm, attrValue, pattern_multiple_m, pattern_multiple_r, MULTIPLE_U);
        } else if (isMatch(CHECKED, attrName) && isMatch(INPUT, elm.name())
                && (isMatch(RADIO, this.attribute(elm, TYPE_L))
                || isMatch(RADIO, this.attribute(elm, TYPE_U)))) {
            editAttributes_(elm, attrValue, pattern_checked_m, pattern_checked_r, CHECKED_U);
        } else if (isMatch(DISABLE_ELEMENT, elm.name()) && isMatch(DISABLED, attrName)) {
            editAttributes_(elm, attrValue, pattern_disabled_m, pattern_disabled_r, DISABLED_U);
        } else
        if (isMatch(READONLY, attrName) && (isMatch(TEXTAREA, elm.name()) || (isMatch(INPUT, elm.name()) && isMatch(READONLY_TYPE, getType(elm))))) {
            editAttributes_(elm, attrValue, pattern_readonly_m, pattern_readonly_r, READONLY_U);
        } else if(isMatch(REQUIRED, attrName) && isMatch(REQUIRE_ELEMENT, elm.name())){
            editAttributes_(elm, attrValue, pattern_required_m, pattern_required_r, REQUIRED_U);
        } else {
            super.editAttributes_(elm, attrName, attrValue);
        }

        //return attrValue;

    }

    protected String getAttributeValue_(Element elm, String attrName) {

        if (isMatch(SELECTED, attrName) && isMatch(OPTION, elm.name())) {
            return getAttributeValue_(elm, pattern_selected_m1);
        } else if (isMatch(MULTIPLE, attrName) && isMatch(SELECT, elm.name())) {
            return getAttributeValue_(elm, pattern_multiple_m1);
        } else if (isMatch(CHECKED, attrName) && isMatch(INPUT, elm.name())
                && (isMatch(RADIO, this.attribute(elm, TYPE_L))
                || isMatch(RADIO, this.attribute(elm, TYPE_U)))) {
            return getAttributeValue_(elm, pattern_checked_m1);
        } else if (isMatch(DISABLE_ELEMENT, elm.name()) && isMatch(DISABLED, attrName)) {
            return getAttributeValue_(elm, pattern_disabled_m1);
        } else
        if (isMatch(READONLY, attrName) && (isMatch(TEXTAREA, elm.name()) || (isMatch(INPUT, elm.name()) && isMatch(READONLY_TYPE, getType(elm))))) {
            return getAttributeValue_(elm, pattern_readonly_m1);
        } else if(isMatch(REQUIRED, attrName) && isMatch(REQUIRE_ELEMENT, elm.name())){
            return getAttributeValue_(elm, pattern_required_m1);
        } else {
            return super.getAttributeValue_(elm, attrName);
        }

    }
}