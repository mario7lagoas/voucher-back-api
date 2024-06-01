package com.rematec.voucher.voucherbackapi.controlles;

import com.rematec.voucher.models.PerfilApiRequest;
import com.rematec.voucher.models.PerfilApiResponse;
import com.rematec.voucher.models.PerfilResumidoApiResponse;
import com.rematec.voucher.models.PerfilUpdateApiRequest;
import com.rematec.voucher.voucherbackapi.controllers.PerfilController;
import com.rematec.voucher.voucherbackapi.services.VoucherBackFacade;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static com.rematec.voucher.voucherbackapi.builders.PerfilApiRequestBuilder.umPerfilApiRequest;
import static com.rematec.voucher.voucherbackapi.builders.PerfilApiResponseBuilder.umPerfilApiResponse;
import static com.rematec.voucher.voucherbackapi.builders.PerfilResumidoApiResponseBuilder.umPerfilResumidoApiResponse;
import static com.rematec.voucher.voucherbackapi.builders.PerfilUpdateApiRequestBuilder.umPerfilUpdateApiRequest;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PerfilControllerTest {

    @InjectMocks
    private PerfilController perfilController;

    @Mock
    private VoucherBackFacade perfilService;

    @Test
    @DisplayName("Should Call Get All Perfis And Return Code 200")
    public void buscandoListaPerfilCase1() {
        //Having
        List<PerfilApiResponse> perfilApiResponse = Arrays.asList(umPerfilApiResponse().agora(),
                umPerfilApiResponse().nome("Two Perfil").guid("654321").agora());

        when(this.perfilService.buscandoListaPerfil()).thenReturn(perfilApiResponse);

        //when
        ResponseEntity<List<PerfilApiResponse>> result = this.perfilController.buscandoListaPerfil();

        //then
        Assertions.assertNotNull(result);

        assertThat(result.getStatusCode().value(), is(200));
        assertThat(result.getBody().size(), is(2));
        assertThat(result.getBody().get(0).getNome(), is("One Perfil"));
        assertThat(result.getBody().get(1).getNome(), is("Two Perfil"));
    }

    @Test
    @DisplayName("Should Call Get All PerfisResumido And Return Code 200")
    public void buscandoListaResumidoPerfilCase1() {
        //Having
        List<PerfilResumidoApiResponse> Response = Collections.singletonList(umPerfilResumidoApiResponse().agora());

        when(this.perfilService.buscandoListaResumidoPerfil()).thenReturn(Response);

        //when
        ResponseEntity<List<PerfilResumidoApiResponse>> result = this.perfilController.buscandoListaResumidoPerfil();

        //then
        Assertions.assertNotNull(result);

        assertThat(result.getStatusCode().value(), is(200));
        assertThat(result.getBody().size(), is(1));
        assertThat(result.getBody().get(0).getNome(), is("Any Perfil"));
    }


    @Test
    @DisplayName("Should Call Get A PerfilApiResponse By GUID And Return Code 200")
    public void buscandoPerfilPeloGUIDCase1() {

        //having
        String guid = UUID.randomUUID().toString();

        when(this.perfilService.buscandoPerfilPeloGUID(guid)).thenReturn(umPerfilApiResponse().guid(guid).agora());

        //when
        ResponseEntity<PerfilApiResponse> response = this.perfilController.buscandoPerfilPeloGUID(guid);

        //then
        Assertions.assertNotNull(guid);
        Assertions.assertNotNull(response);

        assertThat(response.getStatusCode().value(), is(200));
        assertThat(response.getBody().getGuid(), is(guid));
        assertThat(response.getBody().getNome(), is("One Perfil"));

    }

    @Test
    @DisplayName("Should Call Get A PerfilApiResponse By NomeD And Return Code 200")
    public void buscandoPerfilPeloNomeCase1() {

        //having
        String nome = "Any Perfil";

        when(this.perfilService.buscandoPerfilPeloNome(nome)).thenReturn(umPerfilApiResponse().nome(nome).agora());

        //when
        ResponseEntity<PerfilApiResponse> response = this.perfilController.buscandoPerfilPeloNome(nome);

        //then
        Assertions.assertNotNull(nome);
        Assertions.assertNotNull(response);

        assertThat(response.getStatusCode().value(), is(200));
        assertThat(response.getBody().getNome(), is(nome));

    }

    @Test
    @DisplayName("Should Call End Point And Add New Usuario And Return Code 201")
    public void criandoPerfilCase1() {
        //having
        PerfilApiRequest request = umPerfilApiRequest().nome("New Perfil").agora();
        when(this.perfilService.criandoPerfil(request)).thenReturn(umPerfilApiResponse().nome("New Perfil").agora());

        //when
        ResponseEntity<PerfilApiResponse> response = this.perfilController.criandoPerfil(request);

        //then
        Assertions.assertNotNull(request);
        Assertions.assertNotNull(response);

        assertThat(response.getStatusCode().value(), is(201));
        assertThat(response.getBody().getNome(), is("New Perfil"));

    }

    @Test
    @DisplayName("Should Call End Point And Update Perfil And Return Code 202")
    public void alterandoPerfilCase1() {
        //having
        String guid = UUID.randomUUID().toString();
        PerfilUpdateApiRequest request = umPerfilUpdateApiRequest().nome("Other").agora();

        when(this.perfilService.alterandoPerfil(guid, request))
                .thenReturn(umPerfilApiResponse().guid(guid).nome("Other").agora());

        //when
        ResponseEntity<PerfilApiResponse> response = this.perfilController.alterandoPerfil(guid, request);

        //then
        Assertions.assertNotNull(guid);
        Assertions.assertNotNull(request);
        Assertions.assertNotNull(response);

        assertThat(response.getStatusCode().value(), is(202));
        assertThat(response.getBody().getGuid(), is(guid));
        assertThat(response.getBody().getNome(), is("Other"));

    }

    @Test
    @DisplayName("Should Call End Point And Delete Perfil And Return Code 204")
    public void apagandoUsuarioCase1() {

        //having
        String guid = UUID.randomUUID().toString();

        //when
        ResponseEntity<Void> response = this.perfilController.apagandoPerfil(guid);

        //then
        Assertions.assertNotNull(guid);
        Assertions.assertNotNull(response);

        assertThat(response.getStatusCode().value(), is(204));

    }

}
