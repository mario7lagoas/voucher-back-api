package com.rematec.voucher.voucherbackapi.utils;

import com.jayway.jsonpath.Option;
import com.rematec.voucher.voucherbackapi.interfaces.repositories.ILojaRepository;
import com.rematec.voucher.voucherbackapi.models.entities.LojaEntity;
import com.rematec.voucher.voucherbackapi.models.requests.Guid;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class VoucherUtilTest {

    @InjectMocks
    private VoucherUtil voucherUtil;

    @Mock
    private ILojaRepository iLojaReposity;

    @Before("")
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    @DisplayName("Should Return A LojaEntity Is null")
    public void getListGuidLojasToListLojasEntityCase1() {

        //having
        List<Guid> guidList = null;

        //when
        List<LojaEntity> lojaEntities = this.voucherUtil.getListGuidLojasToListLojasEntity(guidList);

        //then
        Assertions.assertNull(lojaEntities);

    }

    @Test
    @DisplayName("Should Return A  List LojaEntity Successfully")
    public void getListGuidLojasToListLojasEntityCase2() {

        //having
        List<Guid> guidList =  Arrays.asList(Guid.builder().guid("1234").build());

        LojaEntity lojaEntity = LojaEntity.builder()
                .id(1L).cnpj("123456").guid("1234").cnpj("123456").identificacao("Lj 01").build();
        when(this.iLojaReposity.findByGuid(anyString())).thenReturn(Optional.of(lojaEntity));

        //when
        List<LojaEntity> lojaEntitiesReturn = this.voucherUtil.getListGuidLojasToListLojasEntity(guidList);

        //then
        Assertions.assertNotNull(lojaEntitiesReturn);

    }
}
