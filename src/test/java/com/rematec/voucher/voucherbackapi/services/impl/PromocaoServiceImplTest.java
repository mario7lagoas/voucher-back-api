package com.rematec.voucher.voucherbackapi.services.impl;

import com.rematec.voucher.models.BuscandoListaPaginadaPromocao200Response;
import com.rematec.voucher.models.PromocaoApiRequest;
import com.rematec.voucher.models.PromocaoApiResponse;
import com.rematec.voucher.models.PromocaoUpdateApiRequest;
import com.rematec.voucher.voucherbackapi.exceptios.LojaNaoEncontradaException;
import com.rematec.voucher.voucherbackapi.exceptios.NaoPermitidoAlterarStatusException;
import com.rematec.voucher.voucherbackapi.exceptios.NaoPermitidoException;
import com.rematec.voucher.voucherbackapi.exceptios.PromocaoNaoEncontradaException;
import com.rematec.voucher.voucherbackapi.interfaces.mapper.VouckBackMapper;
import com.rematec.voucher.voucherbackapi.interfaces.repositories.IPromocaoRepository;
import com.rematec.voucher.voucherbackapi.models.entities.PromocaoEntity;
import com.rematec.voucher.voucherbackapi.models.enums.PromocaoStatusEnum;
import com.rematec.voucher.voucherbackapi.models.filter.PromocaoFiltro;
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

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.rematec.voucher.voucherbackapi.builders.BuscandoListaPaginadaPromocao200ResponseBuilder.umBuscandoListaPaginadaPromocao200Response;
import static com.rematec.voucher.voucherbackapi.builders.PromocaoApiRequestBuilder.umaPromocaoApiRequest;
import static com.rematec.voucher.voucherbackapi.builders.PromocaoEntityBuilder.umaPromocaoEntity;
import static com.rematec.voucher.voucherbackapi.builders.PromocaoUpdateApiRequestBuilder.umaPromocaoUpdateApiRequest;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

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

        when(this.iPromocaoRepository.findByDescricaoContaining(descricao, PageRequest.of(page, size))).thenReturn(promocaoEntityPage);
        when(this.mapper.pagePromocoesEntityToPromocoesApiPaginadaResponse(promocaoEntityPage)).thenReturn(new BuscandoListaPaginadaPromocao200Response());

        //when
        BuscandoListaPaginadaPromocao200Response responses = this.promocaoService.buscandoListaPaginadaPromocao(descricao, page, size);

        //then
        Assertions.assertNotNull(responses);

    }

    @Test
    @DisplayName("Should Create a Promoção Successfully")
    public void criandoPromocaoCase1() {
        //having
        PromocaoApiRequest request = umaPromocaoApiRequest().agora();

        when(this.iPromocaoRepository.save(any(PromocaoEntity.class))).thenReturn(new PromocaoEntity());
        when(this.mapper.promocaoEntityToPromocaoApiResponse(any(PromocaoEntity.class))).thenReturn(new PromocaoApiResponse());

        //when
        PromocaoApiResponse responses = this.promocaoService.criandoPromocao(request);

        //then
        Assertions.assertNotNull(request);
        Assertions.assertNotNull(responses);

        verify(this.iPromocaoRepository, times(1)).save(
                argThat(
                        PromocaoArg -> PromocaoArg.getDescricao().equals("Promocao 01")
                                && PromocaoArg.getGuid() != null)
        );
    }

    @Test
    @DisplayName("Should Thrown An Exception When Try To Date Fim Is Less Than Current Date")
    public void criandoPromocaoCase2() {
        //having
        PromocaoApiRequest request = umaPromocaoApiRequest()
                .inicio(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")).toString())
                .fim(LocalDateTime.now().minusDays(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")).toString())
                .agora();
        //when

        //then
        Assertions.assertNotNull(request);
        Exception exception = Assertions.assertThrows(NaoPermitidoException.class,
                () -> this.promocaoService.criandoPromocao(request));

        assertThat(exception.getMessage(), is("Fim da promoção menor que data atual"));
    }

    @Test
    @DisplayName("Should Thrown An Exception When Try To Date Inicio Is Greater Than Date Fim")
    public void criandoPromocaoCase3() {
        //having
        PromocaoApiRequest request = umaPromocaoApiRequest()
                .inicio(LocalDateTime.now().plusDays(2).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")).toString())
                .fim(LocalDateTime.now().plusDays(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")).toString())
                .agora();

        //when

        //then
        Assertions.assertNotNull(request);
        Exception exception = Assertions.assertThrows(NaoPermitidoException.class,
                () -> this.promocaoService.criandoPromocao(request));

        assertThat(exception.getMessage(), is("Data inicial maior que data final."));
    }

    @Test
    @DisplayName("Should Thrown An Exception When Try To Add A Promocao And Loja Not Exist")
    public void criandoPromocaoCase4() {
        //having
        PromocaoApiRequest request = umaPromocaoApiRequest().comLoja().agora();

        //when

        //then
        Assertions.assertNotNull(request);
        Exception exception = Assertions.assertThrows(LojaNaoEncontradaException.class,
                () -> this.promocaoService.criandoPromocao(request));

        assertThat(exception.getMessage(), is("Loja não encontrada."));
    }

    @Test
    @DisplayName("Should Update a Promocao Successfully")
    public void alterandoPromocaoCase1() {
        //having
        PromocaoEntity promocaoEntity = umaPromocaoEntity().agora();
        String guid = promocaoEntity.getGuid();

        PromocaoUpdateApiRequest request = umaPromocaoUpdateApiRequest().descricao("Other").agora();

        when(this.iPromocaoRepository.save(any(PromocaoEntity.class))).thenReturn(new PromocaoEntity());
        when(this.iPromocaoRepository.findByGuid(guid)).thenReturn(Optional.of(promocaoEntity));
        when(this.mapper.promocaoEntityToPromocaoApiResponse(any(PromocaoEntity.class))).thenReturn(new PromocaoApiResponse());

        //when
        PromocaoApiResponse responses = this.promocaoService.alterandoPromocao(guid, request);

        //then
        Assertions.assertNotNull(request);
        Assertions.assertNotNull(responses);

        verify(this.iPromocaoRepository, times(1)).save(
                argThat(
                        PromocaoArg -> PromocaoArg.getDescricao().equals("Other")
                                && PromocaoArg.getGuid().equals(guid))
        );
    }

    @Test
    @DisplayName("Should Thrown An Exception When Try To Update Promocao And It Does Not Exist")
    public void alterandoPromocaoCase2() {
        //having
        String guid = UUID.randomUUID().toString();
        PromocaoEntity promocaoEntity = umaPromocaoEntity().agora();

        PromocaoUpdateApiRequest request = umaPromocaoUpdateApiRequest().descricao("Other").agora();
        when(this.iPromocaoRepository.findByGuid(guid)).thenReturn(Optional.empty());

        //when

        //then
        Assertions.assertNotNull(request);
        Assertions.assertNotNull(guid);

        Exception exception = Assertions.assertThrows(PromocaoNaoEncontradaException.class,
                () -> this.promocaoService.alterandoPromocao(guid, request));

        assertThat(exception.getMessage(), is("Promoção não encontrada."));
    }

    @Test
    @DisplayName("Should Thrown An Exception When Try To Update Promocao And It Is Finished")
    public void alterandoPromocaoCase3() {
        //having
        PromocaoEntity promocaoEntity = umaPromocaoEntity().promocaoStatus(PromocaoStatusEnum.FINALIZADA).agora();
        String guid = promocaoEntity.getGuid();

        PromocaoUpdateApiRequest request = umaPromocaoUpdateApiRequest().descricao("Other").agora();
        when(this.iPromocaoRepository.findByGuid(guid)).thenReturn(Optional.of(promocaoEntity));
        //when

        //then
        Assertions.assertNotNull(request);
        Assertions.assertNotNull(guid);

        Exception exception = Assertions.assertThrows(NaoPermitidoAlterarStatusException.class,
                () -> this.promocaoService.alterandoPromocao(guid, request));

        assertThat(exception.getMessage(), is("Promoção já finalizada!"));
    }

    @Test
    @DisplayName("Should Thrown An Exception When Try To Update Promocao And To Date Fim Is Less Than Current Date")
    public void alterandoPromocaoCase4() {
        //having
        PromocaoEntity promocaoEntity = umaPromocaoEntity().agora();
        String guid = promocaoEntity.getGuid();

        PromocaoUpdateApiRequest request = umaPromocaoUpdateApiRequest()
                .inicio(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")).toString())
                .fim(LocalDateTime.now().minusDays(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")).toString())
                .agora();

        when(this.iPromocaoRepository.findByGuid(guid)).thenReturn(Optional.of(promocaoEntity));
        //when

        //then
        Assertions.assertNotNull(request);
        Assertions.assertNotNull(guid);

        Exception exception = Assertions.assertThrows(NaoPermitidoException.class,
                () -> this.promocaoService.alterandoPromocao(guid, request));

        assertThat(exception.getMessage(), is("Fim da promoção menor que data atual"));
    }

    @Test
    @DisplayName("Should Thrown An Exception When Try To Update Promocao And To Date Inicio Is Greater Than Date Fim")
    public void alterandoPromocaoCase5() {
        //having
        PromocaoEntity promocaoEntity = umaPromocaoEntity().agora();
        String guid = promocaoEntity.getGuid();

        PromocaoUpdateApiRequest request = umaPromocaoUpdateApiRequest()
                .inicio(LocalDateTime.now().plusDays(2).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")).toString())
                .fim(LocalDateTime.now().plusDays(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")).toString())
                .agora();

        when(this.iPromocaoRepository.findByGuid(guid)).thenReturn(Optional.of(promocaoEntity));
        //when

        //then
        Assertions.assertNotNull(request);
        Assertions.assertNotNull(guid);

        Exception exception = Assertions.assertThrows(NaoPermitidoException.class,
                () -> this.promocaoService.alterandoPromocao(guid, request));

        assertThat(exception.getMessage(), is("Data inicial maior que data final."));
    }

    /*
    @Test
    @DisplayName("Should Return A List PromocaoApiResponse for filtro Successfully")
    public void buscandoListaFiltroPromocao() {
        //having
        Integer page = 0;
        Integer size = 10;

     */

      /*  when(this.iPromocaoRepository.filtrar(PromocaoFiltro.builder().build(), pageable))
                .thenReturn(umBuscandoListaPaginadaPromocao200Response().agora());

       */
    /*
        List<PromocaoEntity> entities = Arrays.asList(umaPromocaoEntity().agora());
        Pageable pageable = PageRequest.of(page, size);
        Page<PromocaoEntity> promocaoEntityPage = new PageImpl<>(entities, pageable, 1l);

        doReturn(umBuscandoListaPaginadaPromocao200Response().agora())
                .when(this.iPromocaoRepository).filtrar(PromocaoFiltro.builder().promocaoStatus("PROGRESSO").build(),
                        PageRequest.of(page, size));
        doNothing().when(this.voucherUtil).verificarPromocoesVencidas();

     */
       /* when(this.mapper.pagePromocoesEntityToBuscandoListaPaginadaPromocao200Response( new PageImpl<>(entities, pageable, 1l)))
                .thenReturn(new BuscandoListaPaginadaPromocao200Response());

        */
    /*
        doReturn(new BuscandoListaPaginadaPromocao200Response()).when(this.mapper)
                .pagePromocoesEntityToBuscandoListaPaginadaPromocao200Response( new PageImpl<>(entities, pageable, 1l));

        //when
        BuscandoListaPaginadaPromocao200Response responses =
                this.promocaoService.buscandoListaFiltroPromocao("", "", "PROGRESSO", "", "", page, size);

        //then
        Assertions.assertNotNull(responses);
    }
    */


}
