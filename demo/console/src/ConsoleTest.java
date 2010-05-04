import jp.kuro.meteor.*;

public class ConsoleTest {
    public static void main(String args[]) {

        try {

            ParserFactory pf = new ParserFactory("/Users/asip/project/kuro/meteor/demo/console/src");

            pf.parser(Parser.XML, "test.xml", "Shift_JIS");

            long before = System.currentTimeMillis();
            Parser xt = pf.parser("test");

            Element tagX = xt.element("momo");
            tagX.content("<>");

            //Element tag  = xt.element("test","manbo","manbo");

            //Element tag = xt.element("manbo", "manbo");
            //Element tag = null;

            //Element tag2 = xt.element("test","manbo","mango");
            Element tag2 = xt.element("manbo", "mango");
            //Element tag2 = xt.find("[manbo=mango]");
            //System.out.println(xt.attribute(tag,"manbo"));
            Element tagY = xt.element("kobe", "momo", "mono");
            //Element tagY = xt.find("kobe[momo=mono]");

            tagY.attribute("momo", "pono");
            tagY.attribute("momo", "pino");

            //System.out.println(xt.attribute(tagY,"momo"));
            //xt.attribute(tag,"manbo","mango");
            tag2.attribute("manbo", "okojo");
            Element tag = xt.element("test", "manbo", "mbo");
            //xt.content(tag,"<>\"'&\"");
            //System.out.println(xt.content(tag));
            //xt.execute(tag, (new TestHook()));

            Element elm2 = xt.element(tag);

            Element tag_ = elm2.child("tech", "mono", "mono");

            for (int i = 0; i < 10; i++) {
                elm2.attribute("test", "bb" + i);
                //System.out.println(elm2.attributes());
                tag_.clone().content("konkon" + i);
                elm2.flush();
            }

            tag_ = xt.element("tech", "mono", "mono2");
            elm2 = xt.element(tag_);

            for (int i = 0; i < 10; i++) {
                elm2.attribute("mono","" + i);
                elm2.content("konkon" + i);
                elm2.flush();
            }

            Element tag3 = xt.cxTag("mono");
            tag3.content("<>\"&mono");
            //xt.eraseQuarkTag(tag3);
            //xt.eraseQuarkTag("mono");
            //tag = xt.element("teo", "a", "aa");
            //xt.removeAttribute(tag,"b");
            //tag = xt.element("mink");

            //Element tag4 = xt.cxTag("jj");
            //xt.execute(tag4,(new TestHook()));


            Element tag5 = xt.element("potato", "id", "aa");

            //Element tag6 = xt.element("potato", "id", "bb");

            //System.out.println(tag5.name());
            //System.out.println(tag5.attributes());
            //System.out.println(tag5.content());

            //xt.attributeMap(tag5);

            tag5.attribute("id", "bb");
            //xt.content(tag5,"cc");
            tag5.attribute("id", "cc");

            //Element tag7 = xt.element("potato","id","aa","id2","bb");

            //Element tag8 = xt.element("hamachi","id","aa","id2","bb");

            System.out.println(tag5.attributes());
            //System.out.println(tag7.attributes());
            //System.out.println(xt.attribute(tag7,"lc"));

            //System.out.println(tag8.attributes());

            xt.flush();

            long after = System.currentTimeMillis();
            System.out.println(after-before);
            System.out.println(xt.document());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}