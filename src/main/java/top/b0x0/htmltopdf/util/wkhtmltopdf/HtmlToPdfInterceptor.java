package top.b0x0.htmltopdf.util.wkhtmltopdf;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/**
 * 清理输入流缓存的线程
 *
 * @author wcp
 * @date 2019/12/31
 */
public class HtmlToPdfInterceptor extends Thread {
    private InputStream is;

    public HtmlToPdfInterceptor(InputStream is){
        this.is = is;
    }

    @Override
    public void run(){
        try{
            InputStreamReader isr = new InputStreamReader(is, StandardCharsets.UTF_8);
            BufferedReader br = new BufferedReader(isr);
            String line = null;
            while ((line = br.readLine()) != null) {
                //输出内容
                System.out.println(line.toString());
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
