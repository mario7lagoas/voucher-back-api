package com.rematec.voucher.voucherbackapi.services;

import com.rematec.voucher.models.BuscandoListaPaginadaLoja200Response;
import com.rematec.voucher.models.LojaApiRequest;
import com.rematec.voucher.models.LojaApiResponse;
import com.rematec.voucher.models.LojaUpdateApiRequest;
import com.rematec.voucher.models.UpdateStatusApiRequest;
import com.rematec.voucher.voucherbackapi.exceptios.BadRequestException;
import com.rematec.voucher.voucherbackapi.exceptios.LojaCadastradaException;
import com.rematec.voucher.voucherbackapi.exceptios.LojaNaoEncontradaException;
import com.rematec.voucher.voucherbackapi.exceptios.NaoPermitidoExcluirLojaException;
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
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.rematec.voucher.voucherbackapi.builders.LojaApiRequestBuilder.umaLojaApiRequest;
import static com.rematec.voucher.voucherbackapi.builders.LojaEntityBuilder.umaLojaEntity;
import static com.rematec.voucher.voucherbackapi.builders.LojaUpdateApiRequestBuilder.umaLojaUpdateApiRequest;
import static com.rematec.voucher.voucherbackapi.builders.UpdateStatusApiRequestBuilder.umUpdateStatusApiRequest;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

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
    @DisplayName("Should Return A List LojaApiResponse Paginator Successfully")
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
    @DisplayName("Should Return A List LojaApiResponse Ative With user email Successfully")
    public void buscandoListaLojaAtivaCase1() {

        String email = "usuario@email.com";

        //having
        List<LojaEntity> lojaEntities = Collections.singletonList(umaLojaEntity().agora());

        when(this.iLojaReposity.findByStatusTrueAndUsuariosEmail(email)).thenReturn(lojaEntities);

        //when
        List<LojaApiResponse> lojaApiResponses = this.lojaService.buscandoListaLojaAtiva(email);

        //then
        Assertions.assertNotNull(lojaApiResponses);
    }

    @Test
    @DisplayName("Should Return A List LojaApiResponse Ative without user email Successfully")
    public void buscandoListaLojaAtivaCase3() {

        String email = "";

        //having
        List<LojaEntity> lojaEntities = Arrays.asList(umaLojaEntity().agora());

        when(this.iLojaReposity.findByStatusTrue()).thenReturn(lojaEntities);

        //when
        List<LojaApiResponse> lojaApiResponses = this.lojaService.buscandoListaLojaAtiva(email);

        //then
        Assertions.assertNotNull(lojaApiResponses);
    }

    @Test
    @DisplayName("Should Return A LojaApiResponse By GUID Successfully")
    public void buscandoLojaPeloGUIDCase1() {

        //having
        String guid = UUID.randomUUID().toString();
        LojaEntity lojaEntity = umaLojaEntity().agora();

        when(this.iLojaReposity.findByGuid(anyString())).thenReturn(Optional.of(lojaEntity));
        when(this.mapper.lojaEntityToLojaApiResponse(lojaEntity)).thenReturn(new LojaApiResponse());

        //when
        LojaApiResponse response = this.lojaService.buscandoLojaPeloGUID(guid);

        //then
        Assertions.assertNotNull(guid);
        Assertions.assertNotNull(response);
    }

    @Test
    @DisplayName("Should Thrown An Exception When Try To Get LojaApiResponse By GUID")
    public void buscandoLojaPeloGUIDCase2() {

        //having
        when(this.iLojaReposity.findByGuid(anyString())).thenReturn(Optional.empty());

        //when

        //then
        Exception exception = Assertions.assertThrows(LojaNaoEncontradaException.class,
                () -> this.lojaService.buscandoLojaPeloGUID(anyString()));

        assertThat(exception.getMessage(), is("Loja não encontrada."));
    }

    @Test
    @DisplayName("Should Create a Loja Successfully")
    public void criandoLojaCase1() {

        //having
        LojaApiRequest request = umaLojaApiRequest().agora();

        when(this.iLojaReposity.save(any(LojaEntity.class))).thenReturn(new LojaEntity());
        when(this.mapper.lojaEntityToLojaApiResponse(any(LojaEntity.class))).thenReturn(new LojaApiResponse());

        //when
        LojaApiResponse lojaResponse = this.lojaService.criandoLoja(request);

        //then
        Assertions.assertNotNull(request);
        Assertions.assertNotNull(lojaResponse);

        verify(this.iLojaReposity, times(1)).save(
                argThat(perfilArg -> perfilArg.getNome().equals("Loja001")
                        && perfilArg.getGuid() != null)
        );
    }

    @Test
    @DisplayName("Should Thrown An Exception When Try To Add Loja Exist")
    public void criandoLojaCase2() {

        //having
        LojaApiRequest request = umaLojaApiRequest().agora();
        LojaEntity lojaEntity = umaLojaEntity().agora();

        when(this.iLojaReposity.findByCnpj(anyString())).thenReturn(Optional.of(lojaEntity));

        //when

        //then
        Assertions.assertNotNull(request);
        Exception exception = Assertions.assertThrows(LojaCadastradaException.class,
                () -> this.lojaService.criandoLoja(request));

        assertThat(exception.getMessage(), is("CNPJ Já cadastrado."));
    }

    @Test
    @DisplayName("Should Thrown An Exception When Try To CNPJ Is Null")
    public void criandoLojaCase3() {

        //having
        LojaApiRequest request = umaLojaApiRequest().cnpj(null).agora();

        //when

        //then
        Assertions.assertNotNull(request);
        Exception exception = Assertions.assertThrows(BadRequestException.class,
                () -> this.lojaService.criandoLoja(request));

        assertThat(exception.getMessage(), is("CNPJ da loja obrigatório."));
    }

    @Test
    @DisplayName("Should Thrown An Exception When Try To CNPJ Is Empty")
    public void criandoLojaCase4() {

        //having
        LojaApiRequest request = umaLojaApiRequest().cnpj("").agora();

        //when

        //then
        Assertions.assertNotNull(request);
        Exception exception = Assertions.assertThrows(BadRequestException.class,
                () -> this.lojaService.criandoLoja(request));

        assertThat(exception.getMessage(), is("CNPJ da loja obrigatório."));
    }

    @Test
    @DisplayName("Should Thrown An Exception When Try To Nome Is Null")
    public void criandoLojaCase5() {

        //having
        LojaApiRequest request = umaLojaApiRequest().nome(null).agora();

        //when

        //then
        Assertions.assertNotNull(request);
        Exception exception = Assertions.assertThrows(BadRequestException.class,
                () -> this.lojaService.criandoLoja(request));

        assertThat(exception.getMessage(), is("Nome da loja obrigatório."));
    }

    @Test
    @DisplayName("Should Thrown An Exception When Try To Nome Is Empty")
    public void criandoLojaCase6() {

        //having
        LojaApiRequest request = umaLojaApiRequest().nome("").agora();

        //when

        //then
        Assertions.assertNotNull(request);
        Exception exception = Assertions.assertThrows(BadRequestException.class,
                () -> this.lojaService.criandoLoja(request));

        assertThat(exception.getMessage(), is("Nome da loja obrigatório."));
    }

    @Test
    @DisplayName("Should Thrown An Exception When Try To Identificacao Is Null")
    public void criandoLojaCase7() {

        //having
        LojaApiRequest request = umaLojaApiRequest().identificacao(null).agora();

        //when

        //then
        Assertions.assertNotNull(request);
        Exception exception = Assertions.assertThrows(BadRequestException.class,
                () -> this.lojaService.criandoLoja(request));

        assertThat(exception.getMessage(), is("Identificação da loja obrigatório."));
    }

    @Test
    @DisplayName("Should Thrown An Exception When Try To Identificacao Is Empty")
    public void criandoLojaCase8() {

        //having
        LojaApiRequest request = umaLojaApiRequest().identificacao("").agora();

        //when

        //then
        Assertions.assertNotNull(request);
        Exception exception = Assertions.assertThrows(BadRequestException.class,
                () -> this.lojaService.criandoLoja(request));

        assertThat(exception.getMessage(), is("Identificação da loja obrigatório."));
    }

    @Test
    @DisplayName("Should Update a Loja Successfully")
    public void alterandoLojaCase1() {
        //having
        LojaEntity lojaEntity = umaLojaEntity().agora();
        String guid = lojaEntity.getGuid();

        LojaUpdateApiRequest request = umaLojaUpdateApiRequest().nome("Other").agora();

        when(this.iLojaReposity.save(any(LojaEntity.class))).thenReturn(new LojaEntity());
        when(this.iLojaReposity.findByGuid(guid)).thenReturn(Optional.of(lojaEntity));
        when(this.mapper.lojaEntityToLojaApiResponse(any(LojaEntity.class))).thenReturn(new LojaApiResponse());

        //when
        LojaApiResponse lojaResponse = this.lojaService.alterandoLoja(guid, request);

        //then
        Assertions.assertNotNull(lojaResponse);

        verify(this.iLojaReposity, times(1)).save(
                argThat(lojaArg -> lojaArg.getNome().equals("Other")
                        && lojaArg.getGuid().equals(guid)
                )
        );
    }

    @Test
    @DisplayName("Should Thrown An Exception When Try To Update Loja And It Does Not Exist")
    public void alterandoLojaCase2() {
        //having
        String guid = UUID.randomUUID().toString();
        LojaUpdateApiRequest request = umaLojaUpdateApiRequest().agora();

        when(this.iLojaReposity.findByGuid(guid)).thenReturn(Optional.empty());

        //when

        //then
        Assertions.assertNotNull(request);
        Assertions.assertNotNull(guid);

        Exception exception = Assertions.assertThrows(LojaNaoEncontradaException.class,
                () -> this.lojaService.alterandoLoja(guid, request));

        assertThat(exception.getMessage(), is("Loja não encontrada."));
    }

    @Test
    @DisplayName("Should Thrown An Exception When Try To Update Loja And It Does Not Exist")
    public void alterandoLojaCase3() {
        //having
        String guid = UUID.randomUUID().toString();
        LojaEntity lojaEntity = umaLojaEntity().agora();
        LojaUpdateApiRequest request = umaLojaUpdateApiRequest().nome("Other").agora();

        when(this.iLojaReposity.findByGuid(guid)).thenReturn(Optional.of(new LojaEntity()));
        when(this.iLojaReposity.findByCnpj(request.getCnpj())).thenReturn(Optional.of(lojaEntity));
        //when

        //then
        Assertions.assertNotNull(request);
        Assertions.assertNotNull(guid);

        Exception exception = Assertions.assertThrows(LojaCadastradaException.class,
                () -> this.lojaService.alterandoLoja(guid, request));

        assertThat(exception.getMessage(), is("CNPJ Já cadastrado."));
    }

    @Test
    @DisplayName("Should Delete A Loja Successfully")
    public void apagandoLojaCase1() {
        //having
        String guid = UUID.randomUUID().toString();
        LojaEntity lojaEntity = umaLojaEntity().guid(guid).nome("Loja Delete").agora();

        when(this.iLojaReposity.findByGuid(guid)).thenReturn(Optional.of(lojaEntity));

        //when
         this.lojaService.apagandoLoja(guid);

        //then
        Assertions.assertNotNull(guid);

        verify(this.iLojaReposity, times(1)).delete(
                argThat(lojaArg -> lojaArg.getNome().equals("Loja Delete")
                        && lojaArg.getGuid().equals(guid)
                )
        );
    }

    @Test
    @DisplayName("Should Thrown An Exception When Try To Delete Loja And It Does Not Exist")
    public void apagandoLojaCase2() {
        //having
        String guid = UUID.randomUUID().toString();
        when(this.iLojaReposity.findByGuid(guid)).thenReturn(Optional.empty());

        //when

        //then
        Exception exception = Assertions.assertThrows(LojaNaoEncontradaException.class,
                () -> this.lojaService.apagandoLoja(guid));

        assertThat(exception.getMessage(), is("Loja não encontrada."));
    }

    @Test
    @DisplayName("Should Thrown An Exception When Try To Delete Loja And It Does In use")
    public void apagandoLojaCase3() {
        //having
       List<Optional<LojaEntity>> optionals =  Arrays.asList(Optional.of(umaLojaEntity().agora()));
        String guid = UUID.randomUUID().toString();

        LojaEntity lojaEntity = umaLojaEntity().guid(guid).nome("Loja em uso").agora();

        when(this.iLojaReposity.findByGuid(guid)).thenReturn(Optional.of(lojaEntity));
        when(this.iLojaReposity.findByPromocoesLojasGuid(guid)).thenReturn(optionals);

        //when

        //then
        Exception exception = Assertions.assertThrows(NaoPermitidoExcluirLojaException.class,
                () -> this.lojaService.apagandoLoja(guid));

        assertThat(exception.getMessage(), is("Loja não pode ser Excluida. Pois está associada a alguma promoção."));
    }

    @Test
    @DisplayName("Should Thrown An Exception When Try To Delete Loja And It Does In use for User")
    public void apagandoLojaCase4() {
        //having
        List<Optional<LojaEntity>> optionals =  Arrays.asList(Optional.of(umaLojaEntity().agora()));
        String guid = UUID.randomUUID().toString();

        LojaEntity lojaEntity = umaLojaEntity().guid(guid).nome("Loja em uso").agora();

        when(this.iLojaReposity.findByGuid(guid)).thenReturn(Optional.of(lojaEntity));
        when(this.iLojaReposity.findByUsuariosLojasGuid(guid)).thenReturn(optionals);

        //when

        //then
        Exception exception = Assertions.assertThrows(NaoPermitidoExcluirLojaException.class,
                () -> this.lojaService.apagandoLoja(guid));

        assertThat(exception.getMessage(), is("Loja não pode ser Excluida. Pois está associada a algum Usuário."));
    }



    @Test
    @DisplayName("Should Update Status a Loja Successfully")
    public void alterandoStatusLojaCase1() {
        //having
        LojaEntity lojaEntity = umaLojaEntity().agora();
        String guid = lojaEntity.getGuid();
        UpdateStatusApiRequest request = umUpdateStatusApiRequest().status(false).agora();

        when(this.iLojaReposity.save(any(LojaEntity.class))).thenReturn(new LojaEntity());
        when(this.iLojaReposity.findByGuid(guid)).thenReturn(Optional.of(lojaEntity));

        //when
        this.lojaService.alterandoStatusLoja(guid, request);

        //then

        verify(iLojaReposity, times(1)).save(
                argThat(lojaArg -> lojaArg.getStatus().equals(false)
                        && lojaArg.getGuid().equals(guid)
                )
        );
    }

    @Test
    @DisplayName("Should Thrown An Exception When Try To Update Status a Loja And It Does Not Exist")
    public void alterandoStatusLojaCase2() {
        //having
        String guid = UUID.randomUUID().toString();
        UpdateStatusApiRequest request = umUpdateStatusApiRequest().agora();

        when(this.iLojaReposity.findByGuid(guid)).thenReturn(Optional.empty());

        //when

        //then
        Exception exception = Assertions.assertThrows(LojaNaoEncontradaException.class,
                () -> this.lojaService.alterandoStatusLoja(guid, request));

        assertThat(exception.getMessage(), is("Loja não encontrada."));
    }


}
