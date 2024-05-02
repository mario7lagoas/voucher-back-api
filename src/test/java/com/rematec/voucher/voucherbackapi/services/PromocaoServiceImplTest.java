package com.rematec.voucher.voucherbackapi.services;

import com.rematec.voucher.voucherbackapi.interfaces.mapper.VouckBackMapper;
import com.rematec.voucher.voucherbackapi.interfaces.repositories.IPromocaoRepository;
import com.rematec.voucher.voucherbackapi.models.entities.PromocaoEntity;
import com.rematec.voucher.voucherbackapi.models.response.PromocaoResponse;
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
public class PromocaoServiceImplTest {

    @InjectMocks
    private PromocaoServiceImpl promocaoService;

    @Mock
    private IPromocaoRepository iPromocaoRepository;

    @Spy
    private VouckBackMapper mapper;
    @Spy
    private VoucherUtil voucherUtil;

    @Before("")
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    @DisplayName("Should Return A List PromocaoResponse Successfully")
    public void getAllPromocoesCase1(){
        //having
        when(this.iPromocaoRepository.findAll()).thenReturn(new CollisionCheckStack<PromocaoEntity>());

        //when
        List<PromocaoResponse> promocaoResponses = this.promocaoService.getAllPromocoes();

        //then
        Assertions.assertNotNull(promocaoResponses);

    }


}
