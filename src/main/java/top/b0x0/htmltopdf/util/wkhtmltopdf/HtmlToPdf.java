package top.b0x0.htmltopdf.util.wkhtmltopdf;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Arrays;
import java.util.Properties;

/**
 * html转换pdf
 *
 * @author wcp
 * @date 2019/12/31
 */
public class HtmlToPdf {
    private static final Logger log = LoggerFactory.getLogger(HtmlToPdf.class);

    /**
     * wkhtmltopdf程序在系统中的路径
     */
    public static final String WKHTMLTOPDF_TOOL_PATH;

    static String actEnv;

    static {
        // dev,test,prd
        // 优先读取JVM参数 -Dbfp.oss.active=prd
        String vmOssActive = System.getProperty("bfp.oss.active");
        log.info("bfp.oss.active = [{}]", vmOssActive);
        String[] ossArr = {"dev", "test", "prd"};
        boolean isContains = Arrays.asList(ossArr).contains(vmOssActive);
        if (isContains) {
            actEnv = vmOssActive;
        } else {
            Properties properties = ConfigProperties.getProperties();
            actEnv = properties.getProperty("actEnv");
        }
        //开发、测试环境与生产环境 bucket区分开
        if ("prd".equals(actEnv)) {
            WKHTMLTOPDF_TOOL_PATH = "/usr/local/bin/wkhtmltopdf";
        } else if ("test".equals(actEnv)) {
            WKHTMLTOPDF_TOOL_PATH = "/usr/local/bin/wkhtmltopdf";
        } else if ("devLocal".equals(actEnv) || "dev".equals(actEnv)) {
            WKHTMLTOPDF_TOOL_PATH = "D:/devSoft/wkhtmltopdf/bin/wkhtmltopdf.exe";
        } else {
            WKHTMLTOPDF_TOOL_PATH = "/usr/local/bin/wkhtmltopdf";
        }

    }

    /**
     * html转pdf
     *
     * @param htmlPath html路径，可以是硬盘上的路径，也可以是网络路径
     * @param pdfPath  pdf保存路径
     * @return 转换成功返回true
     */
    public static boolean convert(String htmlPath, String pdfPath, String pdfName) {
        File file = new File(pdfPath);
        File parent = file.getParentFile();
        //如果pdf保存路径不存在，则创建路径
        if (!parent.exists()) {
            parent.mkdirs();
        }
        StringBuilder cmd = new StringBuilder();

        //指定wkhtmltopdf程序在系统中的路径
        cmd.append(WKHTMLTOPDF_TOOL_PATH);

        cmd.append(" ");
//        cmd.append("  --header-line");//页眉下面的线
//        cmd.append("  --header-center 这里是页眉这里是页眉这里是页眉这里是页眉 ");//页眉中间内容
//        cmd.append("  --margin-top 30mm ");//设置页面上边距 (default 10mm)
//        cmd.append(" --header-spacing 10 ");//    (设置页眉和内容的距离,默认0)
//        cmd.append(" --debug-javascript ");//显示javascript调试输出的信息
//        cmd.append(" --javascript-delay 3000 ");//延迟一定的毫秒等待javascript 执行完成(默认值是200)
//        cmd.append(" --window-status completed ");

        //指定html路径
        cmd.append(htmlPath);
        cmd.append(" ");
        //指定pdf路径
        cmd.append(pdfPath);

        boolean result = true;
        try {
            Process proc = Runtime.getRuntime().exec(cmd.toString());
            new HtmlToPdfInterceptor(proc.getErrorStream()).start();
            new HtmlToPdfInterceptor(proc.getInputStream()).start();
            proc.waitFor();
        } catch (Exception e) {
            result = false;
            e.printStackTrace();
        }

        // TODO
/*
        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(OSSUrlTime.endpoint, OSSUrlTime.accessKeyId, OSSUrlTime.accessKeySecret);
        // 上传文件流。
        try {
            InputStream inputStream = new FileInputStream(pdfPath);
            ossClient.putObject(OSSUrlTime.bucketName, pdfName, inputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            // 关闭OSSClient。
            ossClient.shutdown();
        }
*/

        return result;
    }

    public static void main(String[] args) {
//        HtmlToPdf.convert("https://fanyi.baidu.com/translate", "e:/wkhtmltopdf1.pdf");
        HtmlToPdf.convert("https://fanyi.baidu.com/translate", "d:/wkhtmltopdf2.pdf", "wkhtmltopdf2.pdf");
//        HtmlToPdf.convert("https://yey.efala.com/kindergarten/a/login;JSESSIONID=7d6d072bc98241b9b81a030277e71070", "e:/wkhtmltopdf1.pdf");
    }
}
