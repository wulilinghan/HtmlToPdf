package top.b0x0.htmltopdf.config;

import cn.hutool.core.thread.NamedThreadFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 自定义线程池
 *
 * @author TANG
 */
@Component
@Configuration
@ConfigurationProperties("executor.pool")
public class ExecutorConfig {
    private final static Logger log = LoggerFactory.getLogger(ExecutorConfig.class);

    private Integer corePoolSize;
    private Integer maxPoolSize;
    private Integer queueCapacity;
    private Integer keepAliveSecond;
    private String threadNamePrefix;

    /**
     * java.util.concurrent.ThreadPoolExecutor;
     *
     * @return ThreadPoolExecutor
     */
    @Bean("webThreadPoolExecutor")
    public ThreadPoolExecutor webThreadPoolExecutor() {
        log.info("开始构建webThreadPoolExecutor....");
        return new ThreadPoolExecutor(
                corePoolSize,
                maxPoolSize,
                keepAliveSecond,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(queueCapacity),
                new NamedThreadFactory(threadNamePrefix, false),
                new ThreadPoolExecutor.CallerRunsPolicy());
    }

    public Integer getCorePoolSize() {
        return corePoolSize;
    }

    public void setCorePoolSize(Integer corePoolSize) {
        this.corePoolSize = corePoolSize;
    }

    public Integer getMaxPoolSize() {
        return maxPoolSize;
    }

    public void setMaxPoolSize(Integer maxPoolSize) {
        this.maxPoolSize = maxPoolSize;
    }

    public Integer getQueueCapacity() {
        return queueCapacity;
    }

    public void setQueueCapacity(Integer queueCapacity) {
        this.queueCapacity = queueCapacity;
    }

    public Integer getKeepAliveSecond() {
        return keepAliveSecond;
    }

    public void setKeepAliveSecond(Integer keepAliveSecond) {
        this.keepAliveSecond = keepAliveSecond;
    }

    public String getThreadNamePrefix() {
        return threadNamePrefix;
    }

    public void setThreadNamePrefix(String threadNamePrefix) {
        this.threadNamePrefix = threadNamePrefix;
    }
}
