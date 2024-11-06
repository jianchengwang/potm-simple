package org.example.potm.framework.config.task;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.Optional;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author jianchengwang
 * @date 2023/3/30
 */
@Configuration
public class TaskExecutorConfigure implements AsyncConfigurer {

    /**
     * 获取当前机器的核数, 不一定准确 请根据实际场景 CPU密集 || IO 密集
     */
    public static final int cpuNum = Runtime.getRuntime().availableProcessors();

    @Value("${thread.pool.corePoolSize:}")
    private Optional<Integer> corePoolSize;

    @Value("${thread.pool.maxPoolSize:}")
    private Optional<Integer> maxPoolSize;

    @Value("${thread.pool.queueCapacity:}")
    private Optional<Integer> queueCapacity;

    @Value("${thread.pool.awaitTerminationSeconds:}")
    private Optional<Integer> awaitTerminationSeconds;

    @Override
    @Bean
    @Primary
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        // 核心线程大小 默认区 CPU 数量
        taskExecutor.setCorePoolSize(corePoolSize.orElse(cpuNum));
        // 最大线程大小 默认区 CPU * 2 数量
        taskExecutor.setMaxPoolSize(maxPoolSize.orElse(cpuNum * 2));
        // 队列最大容量
        taskExecutor.setQueueCapacity(queueCapacity.orElse(500));
        taskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        taskExecutor.setWaitForTasksToCompleteOnShutdown(true);
        taskExecutor.setAwaitTerminationSeconds(awaitTerminationSeconds.orElse(60));
        taskExecutor.setThreadNamePrefix("Leakage-Thread-");
        taskExecutor.initialize();
        return taskExecutor;
    }
}

