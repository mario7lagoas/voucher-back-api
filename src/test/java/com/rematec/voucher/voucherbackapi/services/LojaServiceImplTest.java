package com.rematec.voucher.voucherbackapi.services;

import com.rematec.voucher.voucherbackapi.interfaces.mapper.VouckBackMapper;
import com.rematec.voucher.voucherbackapi.interfaces.repositories.ILojaRepository;
import com.rematec.voucher.voucherbackapi.models.entities.LojaEntity;
import com.rematec.voucher.voucherbackapi.models.response.LojaResponse;
import com.rematec.voucher.voucherbackapi.utils.VoucherUtil;
import org.aspectj.lang.annotation.Before;
import org.glassfish.jaxb.runtime.v2.util.CollisionCheckStack;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class LojaServiceImplTest {

    @InjectMocks
    private LojaServiceImpl lojaService;

    @Mock
    private ILojaRepository iLojaReposity;

    @Spy
    private VouckBackMapper mapper;

    @Spy
    private VoucherUtil voucherUtil;

    @Before("")
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }


    @Test
    @DisplayName("Should Return A List LojaResponse Successfully")
    public void getAllCase1() {
        //having
        when(this.iLojaReposity.findAll()).thenReturn(new CollisionCheckStack<LojaEntity>());

        //when
        List<LojaResponse> lojaResponses = this.lojaService.getAll();

        //then
        Assertions.assertNotNull(lojaResponses);

    }
}
