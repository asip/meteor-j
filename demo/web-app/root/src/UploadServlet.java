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

        String path = sc.getRealPath("/WEB-INF/html/upload.html");
        path2 = sc.getRealPath("/WEB-INF/tmp/_");

        pf = ParserFactory.build(Parser.HTML, path, "Shift_JIS");

        anl = new Request();
    }

    public void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        doPost(req, res);
    }

    public void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        Parser xt = pf.parser();
        
        anl.setCharacterEncoding("Shift_JIS");

        HashMap query = anl.analyze(req).getQuery();

        if (query.get("submit") != null && !query.get("submit").equals("")) {

            FileStorage fs = (FileStorage) query.get("file");

            Element tag1 = xt.cxTag("file_path");
            Element tag2 = xt.cxTag("file_name");
            Element tag3 = xt.cxTag("mime_type");


            String path = fs.getUploadPath();
            if (path != null) {
                xt.content(tag1, path);
            } else {
                xt.content(tag1, "　");
            }

            xt.content(tag2, fs.getUploadName());
            xt.content(tag3, fs.getMimeType());

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

        HttpPrinter prt = new HttpPrinter(res);
        prt.print(xt);

    }
}