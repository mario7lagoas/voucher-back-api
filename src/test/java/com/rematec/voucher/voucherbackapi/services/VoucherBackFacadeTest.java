package com.rematec.voucher.voucherbackapi.services;

import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;

import static com.rematec.voucher.voucherbackapi.builders.LojaApiResponseBuilder.umaLojaApiResponse;
import static com.rematec.voucher.voucherbackapi.builders.PromocaoApiResponseBuilder.umaPromocaoApiResponse;

@ExtendWith(MockitoExtension.class)
public class VoucherBackFacadeTest {
    @InjectMocks
    private VoucherBackFacade voucherBackFacade;

    @Before("")
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }


    @Test
    @DisplayName("Should Return A String PromocaoApiResponse Base64 Successfully")
    public void reportCase1(){
        //having

        JRBeanCollectionDataSource collectionDataSource = new JRBeanCollectionDataSource(
                Collections.singletonList(umaPromocaoApiResponse().comLoja().agora()));
        //when

        String report = this.voucherBackFacade.report(collectionDataSource , "promocoes");
        //then
        Assertions.assertNotNull(report);
    }

    @Test
    @DisplayName("Should Return A String lojaApiResponse Base64 Successfully")
    public void reportCase2(){
        //having

        JRBeanCollectionDataSource collectionDataSource = new JRBeanCollectionDataSource(
                Collections.singletonList(umaLojaApiResponse().agora()));
        //when

        String report = this.voucherBackFacade.report(collectionDataSource , "lojas");
        //then
        Assertions.assertNotNull(report);
    }
}
