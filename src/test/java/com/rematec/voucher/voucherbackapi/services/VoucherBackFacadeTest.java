package com.rematec.voucher.voucherbackapi.services;

import com.rematec.voucher.models.BuscandoListaPaginadaLoja200Response;
import com.rematec.voucher.models.BuscandoListaPaginadaPromocao200Response;
import com.rematec.voucher.models.BuscandoListaPaginadaUsuario200Response;
import com.rematec.voucher.models.LojaApiRequest;
import com.rematec.voucher.models.LojaApiResponse;
import com.rematec.voucher.models.LojaUpdateApiRequest;
import com.rematec.voucher.models.PerfilApiRequest;
import com.rematec.voucher.models.PerfilApiResponse;
import com.rematec.voucher.models.PerfilResumidoApiResponse;
import com.rematec.voucher.models.PerfilUpdateApiRequest;
import com.rematec.voucher.models.PromocaoApiRequest;
import com.rematec.voucher.models.PromocaoApiResponse;
import com.rematec.voucher.models.PromocaoUpdateApiRequest;
import com.rematec.voucher.models.UpdateStatusApiRequest;
import com.rematec.voucher.models.UsuarioApiRequest;
import com.rematec.voucher.models.UsuarioApiResponse;
import com.rematec.voucher.models.UsuarioUpdateApiRequest;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.aspectj.lang.annotation.Before;
import org.glassfish.jaxb.runtime.v2.util.CollisionCheckStack;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static com.rematec.voucher.voucherbackapi.builders.LojaApiRequestBuilder.umaLojaApiRequest;
import static com.rematec.voucher.voucherbackapi.builders.LojaApiResponseBuilder.umaLojaApiResponse;
import static com.rematec.voucher.voucherbackapi.builders.LojaUpdateApiRequestBuilder.umaLojaUpdateApiRequest;
import static com.rematec.voucher.voucherbackapi.builders.PerfilApiRequestBuilder.umPerfilApiRequest;
import static com.rematec.voucher.voucherbackapi.builders.PerfilUpdateApiRequestBuilder.umPerfilUpdateApiRequest;
import static com.rematec.voucher.voucherbackapi.builders.PromocaoApiRequestBuilder.umaPromocaoApiRequest;
import static com.rematec.voucher.voucherbackapi.builders.PromocaoApiResponseBuilder.umaPromocaoApiResponse;
import static com.rematec.voucher.voucherbackapi.builders.PromocaoUpdateApiRequestBuilder.umaPromocaoUpdateApiRequest;
import static com.rematec.voucher.voucherbackapi.builders.UpdateStatusApiRequestBuilder.umUpdateStatusApiRequest;
import static com.rematec.voucher.voucherbackapi.builders.UsuarioApiRequestBuilder.umUsuarioApiRequest;
import static com.rematec.voucher.voucherbackapi.builders.UsuarioUpdateApiRequestBuilder.umUsuarioUpdateApiRequest;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class VoucherBackFacadeTest {
    @InjectMocks
    private VoucherBackFacade voucherBackFacade;

    @Mock
    private UsuarioServiceImpl usuarioService;

    @Mock
    private PerfilServiceImpl perfilService;

    @Mock
    private LojaServiceImpl lojaService;

    @Mock
    private PromocaoServiceImpl promocaoService;

    @Before("")
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    @DisplayName("Should Return A List UsuarioApiResponse Successfully")
    public void buscandoListaUsuarioCase1() {

        //having
        when(this.usuarioService.buscandoListaUsuario()).thenReturn(new CollisionCheckStack<UsuarioApiResponse>());

        //when
        List<UsuarioApiResponse> responses = this.voucherBackFacade.buscandoListaUsuario();

        //then
        Assertions.assertNotNull(responses);
    }

    @Test
    @DisplayName("Should Return A List UsuarioApiResponse paginator Successfully")
    public void buscandoListaPaginadaUsuarioCase1() {
        //having
        String nome = "anyname";
        Integer page = 0;
        Integer size = 10;
        when(this.usuarioService.buscandoListaPaginadaUsuario(nome, page, size))
                .thenReturn(new BuscandoListaPaginadaUsuario200Response());

        //when
        BuscandoListaPaginadaUsuario200Response responses = this.voucherBackFacade.buscandoListaPaginadaUsuario(
                nome, page, size);

        //then
        Assertions.assertNotNull(responses);
    }

    @Test
    @DisplayName("Should Return A UsuarioApiResponse By GUID Successfully")
    public void buscandoUsuarioPeloGUIDCase1() {

        //having
        String guid = UUID.randomUUID().toString();
        when(this.usuarioService.buscandoUsuarioPeloGUID(guid)).thenReturn(new UsuarioApiResponse());

        //when
        UsuarioApiResponse response = this.voucherBackFacade.buscandoUsuarioPeloGUID(guid);

        //then
        Assertions.assertNotNull(guid);
        Assertions.assertNotNull(response);
    }

    @Test
    @DisplayName("Should Create a Usuario Successfully")
    public void criandoUsuarioCase1() {

        //having
        UsuarioApiRequest request = umUsuarioApiRequest().userName("New User").comPerfis().agora();

        when(this.usuarioService.criandoUsuario(request)).thenReturn(new UsuarioApiResponse());

        //when

        UsuarioApiResponse response = this.voucherBackFacade.criandoUsuario(request);

        //then
        Assertions.assertNotNull(request);
        Assertions.assertNotNull(response);

    }

    @Test
    @DisplayName("Should Update a Loja Successfully")
    public void alterandoUsuarioCase1() {

        //having
        String guid = UUID.randomUUID().toString();
        UsuarioUpdateApiRequest request = umUsuarioUpdateApiRequest().userName("Other").comPerfis().agora();

        when(this.usuarioService.alterandoUsuario(guid, request)).thenReturn(new UsuarioApiResponse());

        //when
        UsuarioApiResponse response = this.voucherBackFacade.alterandoUsuario(guid, request);

        //then
        Assertions.assertNotNull(guid);
        Assertions.assertNotNull(request);
        Assertions.assertNotNull(response);

    }

    @Test
    @DisplayName("Should Delete A Usuario Successfully")
    public void apagandoUsuarioCase1() {

        //having
        String guid = UUID.randomUUID().toString();

        //when
        this.voucherBackFacade.apagandoUsuario(guid);

        //then
        Assertions.assertNotNull(guid);
    }

    @Test
    @DisplayName("Should Update Status A Usuario Successfully")
    public void alterandoStatusUsuarioCase1() {

        //having
        String guid = UUID.randomUUID().toString();
        UpdateStatusApiRequest request = umUpdateStatusApiRequest().status(false).agora();

        //when
        this.voucherBackFacade.alterandoStatusUsuario(guid, request);

        //then
        Assertions.assertNotNull(guid);
        Assertions.assertNotNull(request);
    }

    @Test
    @DisplayName("Should Return A String PromocaoApiResponse Base64 Successfully")
    public void reportCase1() {
        //having
        JRBeanCollectionDataSource collectionDataSource = new JRBeanCollectionDataSource(
                Collections.singletonList(umaPromocaoApiResponse().comLoja().agora()));
        //when
        String report = this.voucherBackFacade.report(collectionDataSource, "promocoes");

        //then
        Assertions.assertNotNull(report);
    }

    @Test
    @DisplayName("Should Return A List PerfilApiResponse Successfully")
    public void buscandoListaPerfilCase1() {

        //having
        when(this.perfilService.buscandoListaPerfil()).thenReturn(new CollisionCheckStack<PerfilApiResponse>());

        //when
        List<PerfilApiResponse> responses = this.voucherBackFacade.buscandoListaPerfil();

        //then
        Assertions.assertNotNull(responses);

    }

    @Test
    @DisplayName("Should Return A List PerfilResumidoApiResponse Successfully")
    public void buscandoListaResumidoPerfilCase1() {

        //having
        when(this.perfilService.buscandoListaResumidoPerfil())
                .thenReturn(new CollisionCheckStack<PerfilResumidoApiResponse>());

        //when
        List<PerfilResumidoApiResponse> responses = this.voucherBackFacade.buscandoListaResumidoPerfil();

        //then
        Assertions.assertNotNull(responses);

    }

    @Test
    @DisplayName("Should Create a Perfil Successfully")
    public void criandoPerfilCase1() {

        //having
        PerfilApiRequest request = umPerfilApiRequest().comRoles().agora();
        when(this.perfilService.criandoPerfil(request)).thenReturn(new PerfilApiResponse());

        //when
        PerfilApiResponse response = this.voucherBackFacade.criandoPerfil(request);

        //then
        Assertions.assertNotNull(request);
        Assertions.assertNotNull(response);
    }

    @Test
    @DisplayName("Should Return A Perfil By Guid Successfully")
    public void buscandoPerfilPeloGUIDCase1() {
        //having
        String guid = UUID.randomUUID().toString();

        when(this.perfilService.buscandoPerfilPeloGUID(guid)).thenReturn(new PerfilApiResponse());

        //when
        PerfilApiResponse response = this.voucherBackFacade.buscandoPerfilPeloGUID(guid);

        //then
        Assertions.assertNotNull(guid);
        Assertions.assertNotNull(response);

    }

    @Test
    @DisplayName("Should Return A Perfil By Nome Successfully")
    public void buscandoPerfilPeloNomeCase1() {

        //having
        String perfil = "My Perfil";
        when(this.perfilService.buscandoPerfilPeloNome(perfil)).thenReturn(new PerfilApiResponse());

        //when
        PerfilApiResponse response = this.voucherBackFacade.buscandoPerfilPeloNome(perfil);

        //then
        Assertions.assertNotNull(perfil);
        Assertions.assertNotNull(response);

    }

    @Test
    @DisplayName("Should Update a Perfil status Successfully")
    public void alterandoPerfilCase1() {

        //having
        String guid = UUID.randomUUID().toString();
        PerfilUpdateApiRequest request = umPerfilUpdateApiRequest().setNome("Other").agora();

        when(this.perfilService.alterandoPerfil(guid, request)).thenReturn(new PerfilApiResponse());

        //when
        PerfilApiResponse response = this.voucherBackFacade.alterandoPerfil(guid, request);

        //then
        Assertions.assertNotNull(guid);
        Assertions.assertNotNull(request);
        Assertions.assertNotNull(response);
    }

    @Test
    @DisplayName("Should Delete A Perfil Successfully")
    public void apagandoPerfilCase1() {

        //having
        String guid = UUID.randomUUID().toString();

        //when
        this.voucherBackFacade.apagandoPerfil(guid);

        //then
        Assertions.assertNotNull(guid);
    }

    @Test
    @DisplayName("Should Return A List LojaApiResponse Successfully")
    public void buscandoListaLojaCase1() {

        //having
        when(this.lojaService.buscandoListaLoja()).thenReturn(new CollisionCheckStack<LojaApiResponse>());

        //when
        List<LojaApiResponse> responses = this.voucherBackFacade.buscandoListaLoja();

        //then
        Assertions.assertNotNull(responses);
    }

    @Test
    @DisplayName("Should Return A List LojaApiResponse Ative With user email Successfully")
    public void buscandoListaLojaAtivaCase1() {

        //having
        String email = "usuario@email.com";
        when(this.lojaService.buscandoListaLojaAtiva(email)).thenReturn(new CollisionCheckStack<LojaApiResponse>());

        //when
        List<LojaApiResponse> responses = this.voucherBackFacade.buscandoListaLojaAtiva(email);

        //then
        Assertions.assertNotNull(email);
        Assertions.assertNotNull(responses);

    }

    @Test
    @DisplayName("Should Return A List LojaApiResponse Paginator Successfully")
    public void buscandoListaPaginadaLojaCase1() {

        //having
        String cnpj = "11111111111111";
        Integer page = 0;
        Integer size = 10;
        when(this.lojaService.buscandoListaPaginadaLoja(cnpj, page, size))
                .thenReturn(new BuscandoListaPaginadaLoja200Response());

        //when
        BuscandoListaPaginadaLoja200Response lojaResponses = this.voucherBackFacade.buscandoListaPaginadaLoja(
                cnpj, page, size);

        //then
        Assertions.assertNotNull(lojaResponses);

    }

    @Test
    @DisplayName("Should Return A LojaApiResponse By GUID Successfully")
    public void buscandoLojaPeloGUIDCase1() {

        //having
        String guid = UUID.randomUUID().toString();

        when(this.lojaService.buscandoLojaPeloGUID(guid)).thenReturn(new LojaApiResponse());

        //when
        LojaApiResponse response = this.voucherBackFacade.buscandoLojaPeloGUID(guid);

        //then
        Assertions.assertNotNull(guid);
        Assertions.assertNotNull(response);

    }

    @Test
    @DisplayName("Should Create a Loja Successfully")
    public void criandoLojaCase1() {

        //having
        LojaApiRequest request = umaLojaApiRequest().agora();

        when(this.lojaService.criandoLoja(request)).thenReturn(new LojaApiResponse());

        //when
        LojaApiResponse response = this.voucherBackFacade.criandoLoja(request);

        //then
        Assertions.assertNotNull(request);
        Assertions.assertNotNull(response);

    }

    @Test
    @DisplayName("Should Update a Loja Successfully")
    public void alterandoLojaCase1() {

        //having
        String guid = UUID.randomUUID().toString();
        LojaUpdateApiRequest request = umaLojaUpdateApiRequest().nome("Other").agora();

        when(this.lojaService.alterandoLoja(guid, request)).thenReturn(new LojaApiResponse());

        //when
        LojaApiResponse response = this.voucherBackFacade.alterandoLoja(guid, request);

        //then
        Assertions.assertNotNull(guid);
        Assertions.assertNotNull(request);
        Assertions.assertNotNull(response);

    }

    @Test
    @DisplayName("Should Delete A Loja Successfully")
    public void apagandoLojaCase1() {
        //having
        String guid = UUID.randomUUID().toString();

        //when
        this.voucherBackFacade.apagandoLoja(guid);

        //then
        Assertions.assertNotNull(guid);
    }

    @Test
    @DisplayName("Should Update Status a Loja Successfully")
    public void alterandoStatusLojaCase1() {
        //having
        String guid = UUID.randomUUID().toString();
        UpdateStatusApiRequest request = umUpdateStatusApiRequest().status(false).agora();

        //when
        this.voucherBackFacade.alterandoStatusLoja(guid, request);

        //then
        Assertions.assertNotNull(guid);
        Assertions.assertNotNull(request);

    }

    @Test
    @DisplayName("Should Return A String lojaApiResponse Base64 Successfully")
    public void reportCase2() {

        //having
        JRBeanCollectionDataSource collectionDataSource = new JRBeanCollectionDataSource(
                Collections.singletonList(umaLojaApiResponse().agora()));
        //when
        String report = this.voucherBackFacade.report(collectionDataSource, "lojas");

        //then
        Assertions.assertNotNull(report);
    }


    @Test
    @DisplayName("Should Return A List PromocaoApiResponse Successfully")
    public void buscandoListaPromocaoCase1() {

        //having
        when(this.promocaoService.buscandoListaPromocao()).thenReturn(new CollisionCheckStack<PromocaoApiResponse>());

        //when
        List<PromocaoApiResponse> responses = this.voucherBackFacade.buscandoListaPromocao();

        //then
        Assertions.assertNotNull(responses);
    }

    @Test
    @DisplayName("Should Return A PromocaoApiResponse By GUID Successfully")
    public void buscandoPromocaoPeloGUIDCase1() {

        //having
        String guid = UUID.randomUUID().toString();
        when(this.promocaoService.buscandoPromocaoPeloGUID(guid)).thenReturn(new PromocaoApiResponse());

        //when
        PromocaoApiResponse response = this.voucherBackFacade.buscandoPromocaoPeloGUID(guid);

        //then
        Assertions.assertNotNull(guid);
        Assertions.assertNotNull(response);
    }

    @Test
    @DisplayName("Should Create a Promoção Successfully")
    public void criandoPromocaoCase1() {

        //having
        PromocaoApiRequest request = umaPromocaoApiRequest().agora();

        when(this.promocaoService.criandoPromocao(request)).thenReturn(new PromocaoApiResponse());

        //when
        PromocaoApiResponse response = this.voucherBackFacade.criandoPromocao(request);

        //then
        Assertions.assertNotNull(request);
        Assertions.assertNotNull(response);

    }

    @Test
    @DisplayName("Should Update A Promoção Successfully")
    public void alterandoPromocaoCase1() {

        //having
        String guid = UUID.randomUUID().toString();
        PromocaoUpdateApiRequest request = umaPromocaoUpdateApiRequest().agora();

        when(this.promocaoService.alterandoPromocao(guid, request)).thenReturn(new PromocaoApiResponse());

        //when
        PromocaoApiResponse response = this.voucherBackFacade.alterandoPromocao(guid, request);

        //then
        Assertions.assertNotNull(guid);
        Assertions.assertNotNull(request);
        Assertions.assertNotNull(response);

    }

    @Test
    @DisplayName("Should Ative A Promoção Successfully")
    public void ativandoPromocaoCase1() {

        //having
        String guid = UUID.randomUUID().toString();
        String autor = "Any Athor";

        //when
        this.voucherBackFacade.ativandoPromocao(guid, autor);

        //then
        Assertions.assertNotNull(guid);
        Assertions.assertNotNull(autor);

    }

    @Test
    @DisplayName("Should Delete A Promoção Successfully")
    public void apagandoPromocaoCase1() {

        //having
        String guid = UUID.randomUUID().toString();

        //when
        this.voucherBackFacade.apagandoPromocao(guid);

        //then
        Assertions.assertNotNull(guid);
    }

    @Test
    @DisplayName("Should Return A List PromocaoApiResponse paginator Successfully")
    public void buscandoListaPaginadaPromocaoCase1() {
        //having
        String descricao = "Any Promotion";
        Integer page = 0;
        Integer size = 10;
        when(this.promocaoService.buscandoListaPaginadaPromocao(descricao, page, size))
                .thenReturn(new BuscandoListaPaginadaPromocao200Response());

        //when
        BuscandoListaPaginadaPromocao200Response responses = this.voucherBackFacade.buscandoListaPaginadaPromocao(
                descricao, page, size);

        //then
        Assertions.assertNotNull(responses);
    }

    @Test
    @DisplayName("Should Return A List PromocaoApiResponse Filter Successfully")
    public void  buscandoListaFiltroPromocao() {
        //having
        String descricao = "Any Promotion";
        String tipo = "PERCENTUAL";
        String status = "ATIVA";
        String inicio = "";
        String fim = "";
        Integer page = 0;
        Integer size = 10;
        String email = "";
        when(this.promocaoService.buscandoListaFiltroPromocao(descricao, tipo, status, inicio, fim, page, size, email))
                .thenReturn(new BuscandoListaPaginadaPromocao200Response());

        //when
        BuscandoListaPaginadaPromocao200Response responses = this.voucherBackFacade.buscandoListaFiltroPromocao(
                descricao, tipo, status, inicio, fim, page, size, email);

        //then
        Assertions.assertNotNull(responses);
    }

}
