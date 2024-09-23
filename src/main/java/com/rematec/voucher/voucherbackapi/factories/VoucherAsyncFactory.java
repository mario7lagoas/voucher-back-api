package com.rematec.voucher.voucherbackapi.factories;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.io.Serializable;
import java.util.concurrent.Executor;

@Configuration
@EnableAsync
public class VoucherAsyncFactory implements Serializable {
    private static final long serialVersionUID = 1L;

    @Bean(name = "threadPollConfirmandoVoucherExecutor")
    public Executor threadPollConfirmandoVoucherExecutor() {
        return new ThreadPoolTaskExecutor();
    }

    @Bean(name = "threadPollCancelandoVoucherExecutor")
    public Executor threadPollCancelandoVoucherExecutor() {
        return new ThreadPoolTaskExecutor();
    }

    @Bean(name = "threadPollverificarPromocoesVencidasExecutor")
    public Executor threadPollverificarPromocoesVencidasExecutor() {
        return new ThreadPoolTaskExecutor();
    }

}
