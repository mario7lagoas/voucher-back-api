package com.rematec.voucher.voucherbackapi.services;

import com.rematec.voucher.models.BuscandoListaPaginadaLoja200Response;
import com.rematec.voucher.models.LojaApiResponse;
import com.rematec.voucher.voucherbackapi.builders.LojaEntityBuilder;
import com.rematec.voucher.voucherbackapi.interfaces.mapper.VouckBackMapper;
import com.rematec.voucher.voucherbackapi.interfaces.repositories.ILojaRepository;
import com.rematec.voucher.voucherbackapi.models.entities.LojaEntity;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.List;

import static com.rematec.voucher.voucherbackapi.builders.LojaEntityBuilder.umaLojaEntity;
import static org.mockito.ArgumentMatchers.any;
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
    @DisplayName("Should Return A List LojaApiResponse Successfully")
    public void buscandoListaLojaCase1() {
        //having
        when(this.iLojaReposity.findAll()).thenReturn(new CollisionCheckStack<LojaEntity>());

        //when
        List<LojaApiResponse> lojaResponses = this.lojaService.buscandoListaLoja();

        //then
        Assertions.assertNotNull(lojaResponses);

    }

    @Test
    @DisplayName("Should Return A List LojaApiResponse paginator Successfully")
    public void buscandoListaPaginadaLojaCase1() {

        //having
        String cnpj = "11111111111111";
        Integer page = 0;
        Integer size = 10;

        Pageable pageable = PageRequest.of(page, size);
        List<LojaEntity> lojaEntities = Arrays.asList(umaLojaEntity().agora());
        Page<LojaEntity> lojaEntityPage = new PageImpl<>(lojaEntities, pageable, 1L);

        when(this.iLojaReposity.findByCnpjContaining(cnpj, PageRequest.of(page, size))).thenReturn(lojaEntityPage);
        when(this.mapper.pageLojasEntityToLojasPaginadaApiResponse(lojaEntityPage))
                .thenReturn(new BuscandoListaPaginadaLoja200Response());

        //when
        BuscandoListaPaginadaLoja200Response lojaResponses = this.lojaService.buscandoListaPaginadaLoja(cnpj, page, size);

        //then
        Assertions.assertNotNull(lojaResponses);

    }

    @Test
    @DisplayName("Should Return A List LojaApiResponse Ative Successfully")
    public void buscandoListaLojaAtivaCase1() {

        //having
        List<LojaEntity> lojaEntities = Arrays.asList(umaLojaEntity().agora());

        when(this.iLojaReposity.findByStatusTrue()).thenReturn(lojaEntities);

        //when
        List<LojaApiResponse> lojaApiResponses = this.lojaService.buscandoListaLojaAtiva();

        //then
        Assertions.assertNotNull(lojaApiResponses);
    }
}
