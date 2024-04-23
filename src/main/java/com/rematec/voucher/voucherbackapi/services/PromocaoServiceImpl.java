package com.rematec.voucher.voucherbackapi.services;

import com.rematec.voucher.voucherbackapi.exceptios.NaoPermitidoAlterarStatusException;
import com.rematec.voucher.voucherbackapi.exceptios.PromocaoNaoEncontradaException;
import com.rematec.voucher.voucherbackapi.interfaces.mapper.VouckBackMapper;
import com.rematec.voucher.voucherbackapi.interfaces.repositories.ILojaRepository;
import com.rematec.voucher.voucherbackapi.interfaces.repositories.IPromocaoRepository;
import com.rematec.voucher.voucherbackapi.interfaces.services.IPromocaoService;
import com.rematec.voucher.voucherbackapi.models.entities.PromocaoEntity;
import com.rematec.voucher.voucherbackapi.models.enums.PromocaoStatusEnum;
import com.rematec.voucher.voucherbackapi.models.enums.TipoDescontoEnum;
import com.rematec.voucher.voucherbackapi.models.filter.PromocaoFiltro;
import com.rematec.voucher.voucherbackapi.models.requests.PromocaoRequest;
import com.rematec.voucher.voucherbackapi.models.requests.PromocaoUpdateRequest;
import com.rematec.voucher.voucherbackapi.models.response.PromocaoResponse;
import com.rematec.voucher.voucherbackapi.models.response.PromocoesPaginadaResponse;
import com.rematec.voucher.voucherbackapi.utils.VoucherUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class PromocaoServiceImpl implements IPromocaoService {

    @Autowired
    private IPromocaoRepository iPromocaoRepository;

    @Autowired
    private ILojaRepository iLojaReposity;

    @Autowired
    private VouckBackMapper mapper;

    @Autowired
    private VoucherUtil voucherUtil;

    @Override
    public List<PromocaoResponse> getAllPromocoes() {
        return mapper.listPromocaoEntityToListPromocaoResponse(iPromocaoRepository.findAll());
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
                .discontoPercentual(promocaoRequest.getDiscontoPercentual() != null ?
                        promocaoRequest.getDiscontoPercentual() : BigDecimal.ZERO)
                .discontoValor(promocaoRequest.getDiscontoValor() != null ?
                        promocaoRequest.getDiscontoValor() : BigDecimal.ZERO)
                .diasValidadeVoucher(promocaoRequest.getDiasValidadeVoucher())
                .tipoDesconto(TipoDescontoEnum.valueOf(promocaoRequest.getTipoDesconto()))
                .lojas(voucherUtil.getListGuidLojasToListLojasEntity(promocaoRequest.getLojas()))
                .build();

        return mapper.promocaoEntityTopromocaoResponse(iPromocaoRepository.save(promocaoEntity));
    }

    @Override
    public PromocaoResponse alterarPromocao(String guid, PromocaoUpdateRequest promocaoUpdateRequest) {
        PromocaoEntity promocaoEntity = iPromocaoRepository.findByGuid(guid)
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

        if (promocaoUpdateRequest.getDiscontoValor() != null && promocaoUpdateRequest.getDiscontoValor()
                .compareTo(BigDecimal.ZERO) > 0) {
            promocaoEntity.setDiscontoValor(promocaoUpdateRequest.getDiscontoValor());
            promocaoEntity.setDiscontoPercentual(BigDecimal.ZERO);
        }

        if (promocaoUpdateRequest.getDiscontoPercentual() != null && promocaoUpdateRequest
                .getDiscontoPercentual().compareTo(BigDecimal.ZERO) > 0) {
            promocaoEntity.setDiscontoValor(BigDecimal.ZERO);
            promocaoEntity.setDiscontoPercentual(promocaoUpdateRequest.getDiscontoPercentual());
        }

        if (promocaoUpdateRequest.getFim() != null)
            promocaoEntity.setFim(promocaoUpdateRequest.getFim());
        if (promocaoUpdateRequest.getLojas() != null) {
            promocaoEntity.getLojas().clear();
            promocaoEntity.getLojas().addAll(voucherUtil.getListGuidLojasToListLojasEntity(promocaoUpdateRequest.getLojas()));
        } else {
            promocaoEntity.setLojas(null);
        }

        return mapper.promocaoEntityTopromocaoResponse(iPromocaoRepository.save(promocaoEntity));
    }

    @Override
    public void apagarPromocao(String guid) {
        PromocaoEntity promocaoEntity = this.iPromocaoRepository.findByGuid(guid)
                .orElseThrow(() -> new PromocaoNaoEncontradaException("Promoção não encontrada."));

        iPromocaoRepository.delete(promocaoEntity);
    }

    @Override
    public PromocaoResponse buscarPromocaoByGuid(String guid) {
        PromocaoEntity promocaoEntity = this.iPromocaoRepository.findByGuid(guid)
                .orElseThrow(() -> new PromocaoNaoEncontradaException("Promoção não encontrada."));

        return mapper.promocaoEntityTopromocaoResponse(promocaoEntity);
    }

    @Override
    public PromocoesPaginadaResponse obterPromocoesPaginadas(String descricao, int page, int size) {

        return mapper.pagePromocoesEntityToPromocoesPaginadaResponse(
                this.iPromocaoRepository.findByDescricaoContaining(descricao, PageRequest.of(page, size)));

    }

    @Override
    public PromocoesPaginadaResponse promocaoFiltro(String descricao, String tipoDesconto, String promocaoStatus, LocalDate inicio, LocalDate fim, int page, int size) {

        voucherUtil.verificarPromocoesVencidas();

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
        if(!promocaoEntity.getPromocaoStatus().equals(PromocaoStatusEnum.PROGRESSO))
            throw new NaoPermitidoAlterarStatusException("Status da promoção não pode ser alterado.");
        promocaoEntity.setPromocaoStatus(PromocaoStatusEnum.ATIVA);
        promocaoEntity.setAutorAlteracao(nomeAutorizador);
        this.iPromocaoRepository.save(promocaoEntity);
    }

}
