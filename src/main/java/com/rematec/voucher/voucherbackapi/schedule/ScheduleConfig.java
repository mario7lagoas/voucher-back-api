package com.rematec.voucher.voucherbackapi.schedule;

import com.rematec.voucher.voucherbackapi.interfaces.repositories.IPromocaoRepository;
import com.rematec.voucher.voucherbackapi.models.entities.PromocaoEntity;
import com.rematec.voucher.voucherbackapi.models.enums.PromocaoStatusEnum;
import com.rematec.voucher.voucherbackapi.utils.VoucherUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Slf4j
@Configuration
@EnableScheduling
@EnableAsync
public class ScheduleConfig {

    @Autowired
    private VoucherUtil voucherUtil;
    private static final String SCHEDULE_CONFIG_CRON = "00 0/30 * * * *";

    @Async
    @Scheduled(cron = SCHEDULE_CONFIG_CRON, zone = "America/Sao_Paulo")
    public void verificandoPromoçoesVencidas() {
        voucherUtil.verificarPromocoesVencidias();

    }

}
