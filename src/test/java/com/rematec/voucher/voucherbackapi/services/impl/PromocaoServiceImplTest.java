package com.rematec.voucher.voucherbackapi.services.impl;

import com.rematec.voucher.models.BuscandoListaPaginadaPromocao200Response;
import com.rematec.voucher.models.PromocaoApiResponse;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.List;

import static com.rematec.voucher.voucherbackapi.builders.PromocaoEntityBuilder.umaPromocaoEntity;
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
    @DisplayName("Should Return A List PromocaoApiResponse Successfully")
    public void buscandoListaPromocaoCase1() {
        //having
        when(this.iPromocaoRepository.findAll()).thenReturn(new CollisionCheckStack<PromocaoEntity>());

        //when
        List<PromocaoApiResponse> responses = this.promocaoService.buscandoListaPromocao();

        //then
        Assertions.assertNotNull(responses);

    }

    @Test
    @DisplayName("Should Return A List PromocaoApiResponse Paginator Successfully")
    public void buscandoListaPaginadaPromocaoCase1() {
        //having
        String descricao = "Promocao";
        Integer page = 0;
        Integer size = 10;

        Pageable pageable = PageRequest.of(page, size);

        List<PromocaoEntity> entities = Arrays.asList(umaPromocaoEntity().agora());
        Page<PromocaoEntity> promocaoEntityPage = new PageImpl<>(entities, pageable, 1l);

        when(this.iPromocaoRepository.findByDescricaoContaining(descricao, PageRequest.of(page, size)))
                .thenReturn(promocaoEntityPage);
        when(this.mapper.pagePromocoesEntityToPromocoesApiPaginadaResponse(promocaoEntityPage))
                .thenReturn(new BuscandoListaPaginadaPromocao200Response());

        //when
        BuscandoListaPaginadaPromocao200Response responses =
                this.promocaoService.buscandoListaPaginadaPromocao(descricao, page, size);

        //then
        Assertions.assertNotNull(responses);

    }


    @Test
    @DisplayName("Should Return A List PromocaoResponse Successfully")
    public void getAllPromocoesCase1() {
        //having
        when(this.iPromocaoRepository.findAll()).thenReturn(new CollisionCheckStack<PromocaoEntity>());

        //when
        List<PromocaoResponse> promocaoResponses = this.promocaoService.getAllPromocoes();

        //then
        Assertions.assertNotNull(promocaoResponses);

    }


}
