package top.b0x0.htmltopdf.controller;

import cn.hutool.core.util.IdUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.b0x0.htmltopdf.util.wkhtmltopdf.BfpResult;
import top.b0x0.htmltopdf.util.wkhtmltopdf.HtmlToPdf;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.*;

/**
 * @author TANG
 * @date 2021-07-12
 * @since 1.8
 */
@RestController
public class ExportController {
    private static final Logger logger = LoggerFactory.getLogger(ExportController.class);

    private static final String PDF_RELATIVE_PATH = File.separator + "wkhtmltopdf";

    @Autowired(required = false)
    @Qualifier("webThreadPoolExecutor")
    private ThreadPoolExecutor webThreadPoolExecutor;


    /**
     * 审核报告导出pdf，需要wkhtmltopdf插件，插件位置在HtmlToPdf类中配置
     * 审核报告导出pdf
     *
     * @param content 页面代码,需编码后传
     * @param name    文件名
     * @param request /
     * @return /
     */
    @PostMapping("htmltopdf")
    public BfpResult auditReportExportPdf(@RequestParam(value = "content") String content,
                                          @RequestParam(value = "name") String name,
                                          HttpServletRequest request) throws InterruptedException, ExecutionException, TimeoutException {
//        String content = "<!DOCTYPE html>\n<html lang=\"en\">\n<head>\n    <meta charset=\"UTF-8\">\n    <title>Title</title>\n</head>\n<body>\n<h1>导出测试</h1>\n</body>\n</html>";
        content = "<!DOCTYPE html>\n<html lang=\"en\">\n<head>\n    <meta charset=\"UTF-8\">\n    <title>Title</title>\n</head>\n<body>\n<h1>导出测试</h1>\n</body>\n</html>";

        /*
        导出分页时出现图表被切割，在页面加以下代码可解决
        <style>
          div{
              page-break-inside: avoid;
          }
          </style>
         */
        if (StringUtils.isBlank(name)) {
            name = "auditReport" + IdUtil.simpleUUID();
        }

        String htmlName = name + ".html";

        String realPath = request.getSession().getServletContext().getRealPath(PDF_RELATIVE_PATH);
//        String contextPath = request.getSession().getServletContext().getContextPath();

        File file = new File(realPath);
        if (!file.exists()) {
            file.mkdirs();
        }

        logger.info("文件位置：{}", realPath);

        final String htmlPath = realPath + File.separator + htmlName;
        //  页面内容 转 静态文件
        boolean isToHtmlFile = false;
        try {
            isToHtmlFile = contentToHtmlFile(htmlPath, URLDecoder.decode(content, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        if (!isToHtmlFile) {
            return BfpResult.fail("导出失败");
        }

        String pdfName = name + ".pdf";
        //磁盘路径
        final String pdfPath = realPath + File.separator + pdfName;

        //  html 生成 pdf文件
        Future<?> future = webThreadPoolExecutor.submit(() -> {
            Object convert = null;
            try {
                convert = HtmlToPdf.convert(htmlPath, pdfPath, pdfName);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return BfpResult.ok(convert);
        });

        // 超时处理
        return BfpResult.ok(future.get(5 * 60L, TimeUnit.SECONDS));
    }

    /**
     * 输出html文件
     *
     * @param filePath
     * @param content
     * @return
     */
    private boolean contentToHtmlFile(String filePath, String content) {
        OutputStreamWriter os = null;
        BufferedWriter bw = null;
        try {
            File file = new File(filePath);
            os = new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8);
            bw = new BufferedWriter(os);
            bw.write(content);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bw != null) {
                try {
                    bw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;

    }
}
