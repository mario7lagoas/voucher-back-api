package com.rematec.voucher.voucherbackapi.schedule;

import com.rematec.voucher.voucherbackapi.interfaces.repositories.IPromocaoRepository;
import com.rematec.voucher.voucherbackapi.models.entities.PromocaoEntity;
import com.rematec.voucher.voucherbackapi.models.enums.PromocaoStatusEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Configuration
@EnableScheduling
@EnableAsync
public class ScheduleConfig {

    @Autowired
    private IPromocaoRepository iPromocaoRepository;
    private static final String SCHEDULE_CONFIG_CRON = "00 30 * * * *";

    @Async
    @Scheduled(cron = SCHEDULE_CONFIG_CRON , zone = "America/Sao_Paulo")
    public void verificandoPromoçoesVencidas(){
        List<PromocaoEntity> promocaoEntities = this.iPromocaoRepository
                .findByFimLessThanAndPromocaoStatusNot(LocalDateTime.now(), PromocaoStatusEnum.FINALIZADA);
        if (promocaoEntities != null ){
            promocaoEntities.forEach( p -> {
                log.info("Promoção [{}] vencida dia [{}] . Inativando promoção automaticamente.",
                        p.getDescricao(), p.getFim());
                p.setPromocaoStatus(PromocaoStatusEnum.FINALIZADA);
                this.iPromocaoRepository.save(p);
            });
        }
    }

}
