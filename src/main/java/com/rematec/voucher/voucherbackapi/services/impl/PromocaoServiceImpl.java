package com.rematec.voucher.voucherbackapi.services.impl;

import com.rematec.voucher.models.BuscandoListaPaginadaPromocao200Response;
import com.rematec.voucher.models.PromocaoApiResponse;
import com.rematec.voucher.voucherbackapi.exceptios.NaoPermitidoAlterarStatusException;
import com.rematec.voucher.voucherbackapi.exceptios.PromocaoNaoEncontradaException;
import com.rematec.voucher.voucherbackapi.interfaces.mapper.VouckBackMapper;
import com.rematec.voucher.voucherbackapi.interfaces.repositories.IPromocaoRepository;
import com.rematec.voucher.voucherbackapi.models.entities.PromocaoEntity;
import com.rematec.voucher.voucherbackapi.models.enums.PromocaoStatusEnum;
import com.rematec.voucher.voucherbackapi.models.enums.TipoDescontoEnum;
import com.rematec.voucher.voucherbackapi.models.filter.PromocaoFiltro;
import com.rematec.voucher.voucherbackapi.models.requests.PromocaoRequest;
import com.rematec.voucher.voucherbackapi.models.requests.PromocaoUpdateRequest;
import com.rematec.voucher.voucherbackapi.models.response.PromocaoResponse;
import com.rematec.voucher.voucherbackapi.models.response.PromocoesPaginadaResponse;
import com.rematec.voucher.voucherbackapi.services.PromocaoService;
import com.rematec.voucher.voucherbackapi.utils.VoucherUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class PromocaoServiceImpl extends PromocaoService {

    @Autowired
    private IPromocaoRepository iPromocaoRepository;

    @Autowired
    private VouckBackMapper mapper;

    @Autowired
    private VoucherUtil voucherUtil;

    @Override
    public List<PromocaoApiResponse> buscandoListaPromocao() {
        return this.mapper.listPromocaoEntityToListPromocaoApiResponse(this.iPromocaoRepository.findAll());
    }

    public BuscandoListaPaginadaPromocao200Response buscandoListaPaginadaPromocao(String descricao, Integer page,
                                                                                  Integer size) {
        return this.mapper.pagePromocoesEntityToPromocoesApiPaginadaResponse(
                this.iPromocaoRepository.findByDescricaoContaining(descricao, PageRequest.of(page, size)));

    }

    @Override
    public List<PromocaoResponse> getAllPromocoes() {
        return this.mapper.listPromocaoEntityToListPromocaoResponse(this.iPromocaoRepository.findAll());
    }

    @Override
    public PromocaoResponse addPromocao(PromocaoRequest promocaoRequest) {
        PromocaoEntity promocaoEntity = PromocaoEntity.builder()
                .guid(UUID.randomUUID().toString())
                .descricao(promocaoRequest.getDescricao())
                .promocaoStatus(PromocaoStatusEnum.PROGRESSO)
                .inicio(promocaoRequest.getInicio())
                .fim(promocaoRequest.getFim())
                .autorAlteracao(promocaoRequest.getAutorAlteracao())
                .valorMinimoParaDisparo(promocaoRequest.getValorMinimoParaDisparo())
                .descontoPercentual(promocaoRequest.getDescontoPercentual() != null ?
                        promocaoRequest.getDescontoPercentual() : BigDecimal.ZERO)
                .descontoValor(promocaoRequest.getDescontoValor() != null ?
                        promocaoRequest.getDescontoValor() : BigDecimal.ZERO)
                .valorMaximoDesconto(this.voucherUtil.getValorMaximoDesconto(promocaoRequest))
                .diasValidadeVoucher(promocaoRequest.getDiasValidadeVoucher())
                .tipoDesconto(TipoDescontoEnum.valueOf(promocaoRequest.getTipoDesconto()))
                .lojas(this.voucherUtil.getListGuidLojasToListLojasEntity(promocaoRequest.getLojas()))
                .build();

        return this.mapper.promocaoEntityTopromocaoResponse(this.iPromocaoRepository.save(promocaoEntity));
    }

    @Override
    public PromocaoResponse alterarPromocao(String guid, PromocaoUpdateRequest promocaoUpdateRequest) {
        PromocaoEntity promocaoEntity = this.iPromocaoRepository.findByGuid(guid)
                .orElseThrow(() -> new PromocaoNaoEncontradaException("Promoção não encontrada."));

        if (promocaoEntity.getPromocaoStatus().equals(PromocaoStatusEnum.FINALIZADA))
            throw new NaoPermitidoAlterarStatusException("Promoção já finalizada!");

        if (promocaoUpdateRequest.getDescricao() != null && !promocaoUpdateRequest.getDescricao().isEmpty())
            promocaoEntity.setDescricao(promocaoUpdateRequest.getDescricao());
        if (promocaoUpdateRequest.getAutorAlteracao() != null && !promocaoUpdateRequest.getAutorAlteracao().isEmpty())
            promocaoEntity.setAutorAlteracao(promocaoUpdateRequest.getAutorAlteracao());

        if (promocaoUpdateRequest.getPromocaoStatus() != null && !promocaoUpdateRequest.getPromocaoStatus().isEmpty())
            promocaoEntity.setPromocaoStatus(PromocaoStatusEnum.valueOf(promocaoUpdateRequest.getPromocaoStatus()));

        if (promocaoUpdateRequest.getTipoDesconto() != null && !promocaoUpdateRequest.getTipoDesconto().isEmpty())
            promocaoEntity.setTipoDesconto(TipoDescontoEnum.valueOf(promocaoUpdateRequest.getTipoDesconto()));

        if (promocaoUpdateRequest.getInicio() != null)
            promocaoEntity.setInicio(promocaoUpdateRequest.getInicio());

        if (promocaoUpdateRequest.getValorMinimoParaDisparo() != null)
            promocaoEntity.setValorMinimoParaDisparo(promocaoUpdateRequest.getValorMinimoParaDisparo());

        if (promocaoUpdateRequest.getDiasValidadeVoucher() != null)
            promocaoEntity.setDiasValidadeVoucher(promocaoUpdateRequest.getDiasValidadeVoucher());

        if (promocaoUpdateRequest.getValorMaximoDesconto() != null)
            promocaoEntity.setValorMaximoDesconto(this.voucherUtil.getValorMaximoDesconto(promocaoUpdateRequest));

        if (promocaoUpdateRequest.getDescontoValor() != null && promocaoUpdateRequest.getDescontoValor()
                .compareTo(BigDecimal.ZERO) > 0) {
            promocaoEntity.setDescontoValor(promocaoUpdateRequest.getDescontoValor());
            promocaoEntity.setDescontoPercentual(BigDecimal.ZERO);
        }

        if (promocaoUpdateRequest.getDescontoPercentual() != null && promocaoUpdateRequest
                .getDescontoPercentual().compareTo(BigDecimal.ZERO) > 0) {
            promocaoEntity.setDescontoValor(BigDecimal.ZERO);
            promocaoEntity.setDescontoPercentual(promocaoUpdateRequest.getDescontoPercentual());
        }

        if (promocaoUpdateRequest.getFim() != null)
            promocaoEntity.setFim(promocaoUpdateRequest.getFim());
        if (promocaoUpdateRequest.getLojas() != null) {
            promocaoEntity.getLojas().clear();
            promocaoEntity.getLojas().addAll(
                    this.voucherUtil.getListGuidLojasToListLojasEntity(promocaoUpdateRequest.getLojas())
            );
        } else {
            promocaoEntity.setLojas(null);
        }

        return this.mapper.promocaoEntityTopromocaoResponse(this.iPromocaoRepository.save(promocaoEntity));
    }

    @Override
    public void apagarPromocao(String guid) {
        PromocaoEntity promocaoEntity = this.iPromocaoRepository.findByGuid(guid)
                .orElseThrow(() -> new PromocaoNaoEncontradaException("Promoção não encontrada."));

        this.iPromocaoRepository.delete(promocaoEntity);
    }

    @Override
    public PromocaoResponse buscarPromocaoByGuid(String guid) {
        PromocaoEntity promocaoEntity = this.iPromocaoRepository.findByGuid(guid)
                .orElseThrow(() -> new PromocaoNaoEncontradaException("Promoção não encontrada."));

        return this.mapper.promocaoEntityTopromocaoResponse(promocaoEntity);
    }

    @Override
    public PromocoesPaginadaResponse obterPromocoesPaginadas(String descricao, int page, int size) {

        return this.mapper.pagePromocoesEntityToPromocoesPaginadaResponse(
                this.iPromocaoRepository.findByDescricaoContaining(descricao, PageRequest.of(page, size)));

    }

    @Override
    public PromocoesPaginadaResponse promocaoFiltro(String descricao, String tipoDesconto, String promocaoStatus,
                                                    LocalDate inicio, LocalDate fim, int page, int size) {

        this.voucherUtil.verificarPromocoesVencidas();

        PromocaoFiltro filtro = PromocaoFiltro.builder()
                .descricao(descricao)
                .tipoDesconto(tipoDesconto)
                .promocaoStatus(promocaoStatus)
                .inicio(inicio)
                .fim(fim)
                .build();

        return this.iPromocaoRepository.filtrar(filtro, PageRequest.of(page, size));
    }

    @Override
    public void ativarPromocao(String guid, String nomeAutorizador) {

        PromocaoEntity promocaoEntity = iPromocaoRepository.findByGuid(guid)
                .orElseThrow(() -> new PromocaoNaoEncontradaException("Promoção não encontrada."));
        if (!promocaoEntity.getPromocaoStatus().equals(PromocaoStatusEnum.PROGRESSO))
            throw new NaoPermitidoAlterarStatusException("Status da promoção não pode ser alterado.");
        promocaoEntity.setPromocaoStatus(PromocaoStatusEnum.ATIVA);
        promocaoEntity.setAutorAlteracao(nomeAutorizador);
        this.iPromocaoRepository.save(promocaoEntity);
    }

}
