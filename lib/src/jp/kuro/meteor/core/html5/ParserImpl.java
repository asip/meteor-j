package jp.kuro.meteor.core.html5;

import jp.kuro.meteor.Parser;
import jp.kuro.meteor.Element;

import java.util.regex.Pattern;

/**
 * HTML5パーサ
 * @author Yasumasa Ash1da
 * @since 2010/04/25 18:05:02
 * 
 */
public class ParserImpl extends jp.kuro.meteor.core.html.ParserImpl implements Parser {


    private static final String CHARSET = "charset";
    private static final String UTF8 = "utf-8";
    //内容のない要素
    protected static final String[] MATCH_TAG = {"br","hr","img","meta","base","embed","command","keygen"};
    //入れ子にできない要素
    protected static final String[] MATCH_TAG_SNG = {"textarea","select","option","form","fieldset",
            "figure","figcaption","video","audio","progress","meter","time","ruby","rt","rp",
            "datalist","output"};
    //論理値で指定する属性
    protected static final String[] ATTR_LOGIC = {"disabled","readonly","checked","selected",
            "multiple","required"};
    //disabled属性のある要素
    private static final String[] DISABLE_ELEMENT = {"input","textarea","select","optgroup",
            "fieldset"};
    //required属性のある要素
    private static final String[] REQUIRE_ELEMENT = {"input","textarea"};

    private static final String REQUIRED = "required";

    private static final String REQUIRED_M = "\\srequired\\s|\\srequired$|\\sREQUIRED\\s|\\sREQUIRED$";
    //REQUIRED_M = [' required ',' required',' REQUIRED ',' REQUIRED']
    private static final String REQUIRED_R = "required\\s|required$|REQUIRED\\s|REQUIRED$";

    private static final Pattern pattern_required_m = Pattern.compile(REQUIRED_M);
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

    protected final void editAttributes_(Element elm, String attrName, String attrValue) {

        //todo

        if (isMatch(SELECTED, attrName) && isMatch(OPTION, elm.name())) {
            _editAttributes(elm, attrName, attrValue, pattern_selected_m, pattern_selected_r);
        } else if (isMatch(MULTIPLE, attrName) && isMatch(SELECT, elm.name())) {
            _editAttributes(elm, attrName, attrValue, pattern_multiple_m, pattern_multiple_r);
        } else if (isMatch(DISABLED, attrName) && isMatch(DISABLE_ELEMENT, elm.name())) {
            _editAttributes(elm, attrName, attrValue, pattern_disabled_m, pattern_disabled_r);
        } else if (isMatch(CHECKED, attrName) && isMatch(INPUT, elm.name())
                && (isMatch(RADIO, this.attribute(elm, TYPE_L))
                || isMatch(RADIO, this.attribute(elm, TYPE_U)))) {
            _editAttributes(elm, attrName, attrValue, pattern_checked_m, pattern_checked_r);
        } else
        if (isMatch(READONLY, attrName) && (isMatch(TEXTAREA, elm.name()) || (isMatch(INPUT, elm.name()) && isMatch(READONLY_TYPE, getType(elm))))) {
            _editAttributes(elm, attrName, attrValue, pattern_readonly_m, pattern_readonly_r);
        } else if(isMatch(REQUIRED, attrName) && isMatch(REQUIRE_ELEMENT, elm.name())){
            _editAttributes(elm, attrName, attrValue, pattern_required_m, pattern_required_r);
        } else {
            super.editAttributes_(elm, attrName, attrValue);
        }

    }

    protected final String getAttributeValue_(Element elm, String attrName) {

        if ((isMatch(SELECTED, attrName) && isMatch(OPTION, elm.name()))) {
            return getAttributeValue_(elm, pattern_selected_m);
        } else if ((isMatch(MULTIPLE, attrName) && isMatch(SELECT, elm.name()))) {
            return getAttributeValue_(elm, pattern_multiple_m);
        } else if (isMatch(DISABLED, attrName) && isMatch(DISABLE_ELEMENT, elm.name())) {
            return getAttributeValue_(elm, pattern_disabled_m);
        } else if (isMatch(CHECKED, attrName) && isMatch(INPUT, elm.name())
                && isMatch(RADIO, getType(elm))) {
            return getAttributeValue_(elm, pattern_checked_m);
        } else
        if (isMatch(READONLY, attrName) && (isMatch(TEXTAREA, elm.name()) || (isMatch(INPUT, elm.name()) && isMatch(READONLY_TYPE, getType(elm))))) {
            return getAttributeValue_(elm, pattern_readonly_m);
        } else if(isMatch(REQUIRED, attrName) && isMatch(REQUIRE_ELEMENT, elm.name())){
            return getAttributeValue_(elm, pattern_required_m);
        } else {
            return super.getAttributeValue_(elm, attrName);
        }

    }
}
