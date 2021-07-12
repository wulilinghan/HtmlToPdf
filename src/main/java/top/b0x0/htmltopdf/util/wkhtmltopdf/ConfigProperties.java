package top.b0x0.htmltopdf.util.wkhtmltopdf;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Properties;

/**
 * 模板配置
 * @author liurui
 * @date 2020/6/23
 */
public class ConfigProperties {

    private static final Logger logger = LoggerFactory.getLogger(ConfigProperties.class);
    private static final Properties prop = new Properties();
    static {
        try {
            prop.load(ConfigProperties.class.getClassLoader().getResourceAsStream("config.properties"));
        } catch (IOException e) {
            logger.warn("Load Properties Ex", e);
        }
    }
    public static Properties getProperties() {
        return prop;
    }
}
