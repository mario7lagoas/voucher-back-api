package com.rematec.voucher.voucherbackapi.utils;

import com.rematec.voucher.voucherbackapi.exceptios.VoucherEmUsoException;
import com.rematec.voucher.voucherbackapi.exceptios.VoucherUtilizadoException;
import com.rematec.voucher.voucherbackapi.interfaces.repositories.ILojaRepository;
import com.rematec.voucher.voucherbackapi.models.entities.LojaEntity;
import com.rematec.voucher.voucherbackapi.models.entities.VoucherEntity;
import com.rematec.voucher.voucherbackapi.models.enums.VoucherPromocaoStatusEnum;
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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
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

    @Test
    @DisplayName("Should Return A boolen False")
    public void checkDataNullAndEmptyCase1(){
        //having
        String data = "";

        //when
        boolean retorno =  this.voucherUtil.checkDataNullAndEmpty(data);

        //then
        Assertions.assertNotNull(retorno);
        Assertions.assertFalse(retorno);

    }

    @Test
    @DisplayName("Should Return A boolen False")
    public void checkDataNullAndEmptyCase2(){
        //having
        String data = null;

        //when
        boolean retorno =  this.voucherUtil.checkDataNullAndEmpty(data);

        //then
        Assertions.assertNotNull(retorno);
        Assertions.assertFalse(retorno);
    }

    @Test
    @DisplayName("Should Return A boolen True")
    public void checkDataNullAndEmptyCase3(){
        //having
        String data = "Any String";

        //when
        boolean retorno =  this.voucherUtil.checkDataNullAndEmpty(data);

        //then
        Assertions.assertNotNull(retorno);
        Assertions.assertTrue(retorno);
    }

    @Test
    @DisplayName("Should Return A Code Voucher Successfully")
    public void gerarCodigoVoucherCase1(){
        //having
        String pdv = "001";

        //when
        String voucher = this.voucherUtil.gerarCodigoVoucher(pdv);

        //then
        Assertions.assertNotNull(voucher);

    }

    @Test
    @DisplayName("Should Thrown An Exception When Try To Get Perfil Promocao Status EM_USO")
    public void checkStatusVoucherCase1(){
        //having
        String loja = "Loja 002";

        VoucherEntity voucherEntity = VoucherEntity.builder()
                .promocaoStatus(VoucherPromocaoStatusEnum.EM_USO)
                .filialCnpjResgate("123456789")
                .pdvResgate("001")
                .cupomResgate("0001")
                .build();

        LojaEntity lojaEntity = LojaEntity.builder().nome("Loja 002").build();

        //when
        when(this.iLojaReposity.findByCnpj(anyString())).thenReturn(Optional.of(lojaEntity));

        //then
        Exception exception = Assertions.assertThrows(VoucherEmUsoException.class,
                ()-> this.voucherUtil.checkStatusVoucher(voucherEntity));
        assertThat(exception.getMessage(), is("Em uso no PDV [" + voucherEntity.getPdvResgate()
                + "] - Filial [" + loja + "] - Cupom [" + voucherEntity.getCupomResgate() + "]"));

    }

    @Test
    @DisplayName("Should Thrown An Exception When Try To Get Perfil Promocao Status UTILIZADO")
    public void checkStatusVoucherCase2(){
        //having
        String loja = "Loja 002";

        VoucherEntity voucherEntity = VoucherEntity.builder()
                .promocaoStatus(VoucherPromocaoStatusEnum.UTILIZADO)
                .filialCnpjResgate("123456789")
                .pdvResgate("001")
                .cupomResgate("0001")
                .build();

        LojaEntity lojaEntity = LojaEntity.builder().nome("Loja 002").build();

        //when
        when(this.iLojaReposity.findByCnpj(anyString())).thenReturn(Optional.of(lojaEntity));

        //then
        Exception exception = Assertions.assertThrows(VoucherUtilizadoException.class,
                ()-> this.voucherUtil.checkStatusVoucher(voucherEntity));
        assertThat(exception.getMessage(), is("Utilizado [" + voucherEntity.getPdvResgate()
                + "] - Filial [" + loja + "] - Cupom [" + voucherEntity.getCupomResgate() + "]"));

    }

    @Test
    @DisplayName("Should Return A LojaNome Successfully")
    public void getLojaNomeCase1(){

        //having
        Optional<LojaEntity> lojaEntity = Optional.ofNullable(LojaEntity.builder().nome("Loja 001").build());

        //when
        when(this.iLojaReposity.findByCnpj(anyString())).thenReturn(lojaEntity);

        String lojaNome = this.voucherUtil.getLojaNome(anyString());

        //then
        Assertions.assertNotNull(lojaNome);
        assertThat(lojaNome, is("Loja 001"));

    }


}
