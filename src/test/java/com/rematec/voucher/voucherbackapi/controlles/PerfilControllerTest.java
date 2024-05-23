package com.rematec.voucher.voucherbackapi.controlles;

import com.rematec.voucher.models.PerfilApiResponse;
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
import java.util.List;

import static com.rematec.voucher.voucherbackapi.builders.PerfilApiResponseBuilder.umPerfilApiResponse;
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

}
