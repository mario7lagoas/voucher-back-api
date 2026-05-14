package com.rematec.voucher.voucherbackapi.services;

import com.rematec.voucher.models.BuscandoListaFiltroVoucher200Response;
import com.rematec.voucher.models.ConsultaVoucherApiRequest;
import com.rematec.voucher.models.ConsultaVoucherApiResponse;
import com.rematec.voucher.voucherbackapi.mapper.VouckBackMapper;
import com.rematec.voucher.voucherbackapi.repositories.IPromocaoRepository;
import com.rematec.voucher.voucherbackapi.repositories.IVoucherRepository;
import com.rematec.voucher.voucherbackapi.models.filter.VoucherFiltro;
import com.rematec.voucher.voucherbackapi.utils.VoucherUtil;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;

import java.util.Collections;

import static com.rematec.voucher.voucherbackapi.builders.ConsultaVoucherApiRequestBuilder.umaConsultaVoucherApiRequest;
import static com.rematec.voucher.voucherbackapi.builders.PromocaoEntityBuilder.umaPromocaoEntity;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class VoucherServiceImplTest {

    @InjectMocks
    private VoucherServiceImpl voucherService;

    @Mock
    private IVoucherRepository iVoucherRepository;

    @Mock
    private IPromocaoRepository iPromocaoRepository;

    @Spy
    private VoucherUtil voucherUtil;

    @Spy
    private VouckBackMapper mapper;

    @Before("")
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    @DisplayName("Should Return A List ConsultaVoucherApiResponse Successfully")
    public void consultandoPromocoesCase1() {

        //having
        ConsultaVoucherApiRequest request = umaConsultaVoucherApiRequest().agora();

        when(this.iPromocaoRepository.findPromocoesAtivasComLojas(
                any(), any(), any(), any(), any())).thenReturn(Collections.singletonList(umaPromocaoEntity().comLoja().agora()));
        when(this.iVoucherRepository.existsVoucherAtivoParaClienteEPromocao(any(), any(), any())).thenReturn(false);

        //when
        ConsultaVoucherApiResponse response = this.voucherService.consultandoPromocoes(request);

        //then
        Assertions.assertNotNull(response);

    }

    @Test
    @DisplayName("Should Return A List VoucherApiResponse For Filter Successfully")
    public void buscandoListaFiltroVoucherCase1() {
        //having
        Integer page = 0;
        Integer size = 10;
        String descricao = "";
        String codigo = "";
        String clienteCpf = "";
        String pdv = "";
        String cupomResgate = "";
        String inicio = "";
        String fim = "";
        String voucherStatus = "";
        String filialCnpj = "";
        String tipoDesconto = "";

        when(this.iVoucherRepository.filtrar(any(VoucherFiltro.class), any(PageRequest.class)))
                .thenReturn(new BuscandoListaFiltroVoucher200Response());

        //when
        BuscandoListaFiltroVoucher200Response response = this.voucherService.buscandoListaFiltroVoucher(
                page, size, codigo, descricao, clienteCpf, pdv, cupomResgate, inicio, fim, voucherStatus, filialCnpj, tipoDesconto);

        //then
        Assertions.assertNotNull(response);
    }


}
