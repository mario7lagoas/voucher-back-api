package com.rematec.voucher.voucherbackapi.controlles;

import com.rematec.voucher.models.BuscandoListaPaginadaUsuario200Response;
import com.rematec.voucher.models.UpdateStatusApiRequest;
import com.rematec.voucher.models.UsuarioApiRequest;
import com.rematec.voucher.models.UsuarioApiResponse;
import com.rematec.voucher.models.UsuarioUpdateApiRequest;
import com.rematec.voucher.voucherbackapi.controllers.UsuarioController;
import com.rematec.voucher.voucherbackapi.services.VoucherBackFacade;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static com.rematec.voucher.voucherbackapi.builders.BuscandoListaPaginadaUsuario200ResponseBuilder.umaListaPaganidaUsuarioResponse;
import static com.rematec.voucher.voucherbackapi.builders.UpdateStatusApiRequestBuilder.umUpdateStatusApiRequest;
import static com.rematec.voucher.voucherbackapi.builders.UsuarioApiRequestBuilder.umUsuarioApiRequest;
import static com.rematec.voucher.voucherbackapi.builders.UsuarioApiResponseBuilder.umUsuarioApiResponse;
import static com.rematec.voucher.voucherbackapi.builders.UsuarioUpdateApiRequestBuilder.umUsuarioUpdateApiRequest;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UsuarioControllerTest {
    @InjectMocks
    private UsuarioController usuarioController;

    @Mock
    private VoucherBackFacade usuarioService;

    @Test
    @DisplayName("Should Call Get All UsuarioApiResponse And Return Code 200")
    public void buscandoListaUsuarioCase1(){
        //Having
        when(this.usuarioService.buscandoListaUsuario()).thenReturn(Collections.singletonList(umUsuarioApiResponse()
                .agora()));

        //when
        ResponseEntity<List<UsuarioApiResponse>> response = this.usuarioController.buscandoListaUsuario();

        //then
        Assertions.assertNotNull(response);

        assertThat(response.getStatusCode().value(), is(200));
        assertThat(response.getBody().size(), is(1));
        assertThat(response.getBody().get(0).getUserName(), is("Any Name"));
        assertThat(response.getBody().get(0).getEmail(), is("any@email.com"));

    }

    @Test
    @DisplayName("Should Call Get All UsuarioApiResponse paginator And Return Code 200")
    public void buscandoListaPaginadaUsuarioCase1() {

        //having
        String nome = "anyname";
        Integer page = 0;
        Integer size = 10;
        when(this.usuarioService.buscandoListaPaginadaUsuario(nome, page, size))
                .thenReturn(umaListaPaganidaUsuarioResponse().comUmUsuario().agora());

        //when
        ResponseEntity<BuscandoListaPaginadaUsuario200Response> response =
                this.usuarioController.buscandoListaPaginadaUsuario( page, size, nome);

        //then
        Assertions.assertNotNull(response);

        assertThat(response.getStatusCode().value(), is(200));
        assertThat(response.getBody().getUsuarios().size(), is(1));
        assertThat(response.getBody().getUsuarios().get(0).getUserName(), is("Any Name"));
        assertThat(response.getBody().getUsuarios().get(0).getEmail(), is("any@email.com"));

    }

    @Test
    @DisplayName("Should Call Get A UsuarioApiResponse By GUID And Return Code 200")
    public void buscandoUsuarioPeloGUIDCase1() {

        //having
        String guid = UUID.randomUUID().toString();

        when(this.usuarioService.buscandoUsuarioPeloGUID(guid)).thenReturn(umUsuarioApiResponse().guid(guid)
                .agora());

        //when
        ResponseEntity<UsuarioApiResponse> response = this.usuarioController.buscandoUsuarioPeloGUID(guid);

        //then
        Assertions.assertNotNull(guid);
        Assertions.assertNotNull(response);

        assertThat(response.getStatusCode().value(), is(200));
        assertThat(response.getBody().getGuid(), is(guid));
        assertThat(response.getBody().getUserName(), is("Any Name"));
        assertThat(response.getBody().getEmail(), is("any@email.com"));

    }

    @Test
    @DisplayName("Should Call End Point And Add New Usuario And Return Code 201")
    public void criandoUsuarioCase1() {
        //having
        UsuarioApiRequest request = umUsuarioApiRequest().userName("New User").comPerfis().agora();
        when(this.usuarioService.criandoUsuario(request))
                .thenReturn(umUsuarioApiResponse().userName("New User").agora());

        //when
        ResponseEntity<UsuarioApiResponse> response = this.usuarioController.criandoUsuario(request);

        //then
        Assertions.assertNotNull(request);
        Assertions.assertNotNull(response);

        assertThat(response.getStatusCode().value(), is(201));
        assertThat(response.getBody().getUserName(), is("New User"));
        assertThat(response.getBody().getEmail(), is("any@email.com"));

    }

    @Test
    @DisplayName("Should Call End Point And Update Loja And Return Code 202")
    public void alterandoUsuarioCase1() {

        //having
        String guid = UUID.randomUUID().toString();
        UsuarioUpdateApiRequest request = umUsuarioUpdateApiRequest().userName("Other").comPerfis().agora();

        when(this.usuarioService.alterandoUsuario(guid, request))
                .thenReturn(umUsuarioApiResponse().guid(guid).userName("Other").agora());

        //when
        ResponseEntity<UsuarioApiResponse> response = this.usuarioController.alterandoUsuario(guid,request);

        //then
        Assertions.assertNotNull(guid);
        Assertions.assertNotNull(request);
        Assertions.assertNotNull(response);

        assertThat(response.getStatusCode().value(), is(202));
        assertThat(response.getBody().getGuid(), is(guid));
        assertThat(response.getBody().getUserName(), is("Other"));
        assertThat(response.getBody().getEmail(), is("any@email.com"));

    }

    @Test
    @DisplayName("Should Call End Point And Delete Usuario And Return Code 204")
    public void apagandoUsuarioCase1() {

        //having
        String guid = UUID.randomUUID().toString();

        //when
        ResponseEntity<Void> response = this.usuarioController.apagandoUsuario(guid);

        //then
        Assertions.assertNotNull(guid);
        Assertions.assertNotNull(response);

        assertThat(response.getStatusCode().value(), is(204));

    }

    @Test
    @DisplayName("Should Call End Point And Patch Usuario And Return Code 202")
    public void alterandoStatusUsuarioCase1() {

        //having
        String guid = UUID.randomUUID().toString();
        UpdateStatusApiRequest request = umUpdateStatusApiRequest().status(false).agora();

        //when
        ResponseEntity<Void> response = this.usuarioController.alterandoStatusUsuario(guid, request);

        //then
        Assertions.assertNotNull(guid);
        Assertions.assertNotNull(response);

        assertThat(response.getStatusCode().value(), is(202));

    }


    @Test
    @DisplayName("Should Call End Point Post And String Usuario And Return Code 200")
    public void relatorioUsuarioCase1() {

        //having
        List<UsuarioApiResponse> usuarioApiResponse = Collections.singletonList(umUsuarioApiResponse().agora());

        //when
        ResponseEntity<String> response =
                this.usuarioController.relatorioUsuario(usuarioApiResponse);

        //then
        Assertions.assertNotNull(usuarioApiResponse);
        Assertions.assertNotNull(response);

        assertThat(response.getStatusCode().value(), is(200));

    }


}
