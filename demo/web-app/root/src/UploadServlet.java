//標準API

import java.io.*;
import java.util.HashMap;

//ServletAPI
import javax.servlet.*;
import javax.servlet.http.*;

//Kuro API
import jp.kuro.meteor.*;
import jp.kuro.meteor.printer.HttpPrinter;
import jp.kuro.ur.*;

public class UploadServlet extends HttpServlet {
    ParserFactory pf;
    String path2;
    Request anl;

    public void init(ServletConfig sConf) throws ServletException {
        ServletContext sc = sConf.getServletContext();

        String path = sc.getRealPath("/WEB-INF/html/");
        path2 = sc.getRealPath("/WEB-INF/tmp/_");
        pf = new ParserFactory(path);
        pf.parser(Parser.HTML, "upload.html", "Shift_JIS");

        anl = new Request();
    }

    public void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        doPost(req, res);
    }

    public void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        Element root = pf.element("upload");

        anl.setCharacterEncoding("Shift_JIS");

        HashMap query = anl.analyze(req).getQuery();

        if (query.get("submit") != null && !query.get("submit").equals("")) {

            FileStorage fs = (FileStorage) query.get("file");

            Element tag1 = root.cxTag("file_path");
            Element tag2 = root.cxTag("file_name");
            Element tag3 = root.cxTag("mime_type");


            String path = fs.getUploadPath();
            if (path != null) {
                tag1.content(path);
            } else {
                tag1.content("　");
            }

            tag2.content(fs.getUploadName());
            tag3.content(fs.getMimeType());

            byte[] bytes = fs.getData();

            if (bytes.length > 0) {
                FileOutputStream fos = new FileOutputStream(path2 + fs.getUploadName());
                DataOutputStream dos = new DataOutputStream(fos);
                BufferedOutputStream bos = new BufferedOutputStream(dos);

                bos.write(bytes);

                bos.close();
                dos.close();
                fos.close();
            }

        }

        //反映する
        root.flush();

        HttpPrinter prt = new HttpPrinter(res);
        prt.print(root);

    }
}