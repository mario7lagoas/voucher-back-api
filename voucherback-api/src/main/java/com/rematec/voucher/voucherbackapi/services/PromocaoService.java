package com.rematec.voucher.voucherbackapi.services;

import com.rematec.voucher.voucherbackapi.exceptios.LojaNaoEncontradaException;
import com.rematec.voucher.voucherbackapi.exceptios.PromocaoNaoEncontradaException;
import com.rematec.voucher.voucherbackapi.interfaces.mapper.VouckBackMapper;
import com.rematec.voucher.voucherbackapi.interfaces.repositories.ILojaReposity;
import com.rematec.voucher.voucherbackapi.interfaces.repositories.IPromocaoRepository;
import com.rematec.voucher.voucherbackapi.models.entities.LojaEntity;
import com.rematec.voucher.voucherbackapi.models.entities.PromocaoEntity;
import com.rematec.voucher.voucherbackapi.models.enums.PromocaoStatusEnum;
import com.rematec.voucher.voucherbackapi.models.enums.TipoDescontoEnum;
import com.rematec.voucher.voucherbackapi.models.requests.Guid;
import com.rematec.voucher.voucherbackapi.models.requests.PromocaoRequest;
import com.rematec.voucher.voucherbackapi.models.requests.PromocaoUpdateRequest;
import com.rematec.voucher.voucherbackapi.models.response.PromocaoResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class PromocaoService {

    @Autowired
    private IPromocaoRepository iPromocaoRepository;

    @Autowired
    private ILojaReposity iLojaReposity;

    @Autowired
    private VouckBackMapper mapper;

    public List<PromocaoResponse> getAllPromocoes() {

        return mapper.listPromocaoEntitytoListPromocaoResponse(iPromocaoRepository.findAll());

    }

    public PromocaoResponse addPromocao(PromocaoRequest promocaoRequest) {

        PromocaoEntity promocaoEntity = PromocaoEntity.builder()
                .guid(UUID.randomUUID().toString())
                .descricao(promocaoRequest.getDescricao())
                .promocaoStatus(PromocaoStatusEnum.PROGRESSO)
                .inicio(promocaoRequest.getInicio())
                .fim(promocaoRequest.getFim())
                .tipoDesconto(TipoDescontoEnum.valueOf(promocaoRequest.getTipoDesconto()))
                .lojas(addLojas(promocaoRequest.getLojas()))
                .build();

        return mapper.promocaoEntitytopromocaoResponse(iPromocaoRepository.save(promocaoEntity));
    }

    private List<LojaEntity> addLojas(List<Guid> lojas) {

        return lojas != null ? lojas.stream()
                .map(loja -> iLojaReposity.findByGuid(loja.getGuid()).get())
                .toList() : null;
    }

    public PromocaoResponse alterarPromocao(String guid, PromocaoUpdateRequest promocaoUpdateRequest) {
        PromocaoEntity promocaoEntity = iPromocaoRepository.findByGuid(guid)
                .orElseThrow(()-> new PromocaoNaoEncontradaException("Promoção não encontrada."));

        if (!promocaoUpdateRequest.getDescricao().isEmpty() )
            promocaoEntity.setDescricao(promocaoUpdateRequest.getDescricao());

        if (!promocaoUpdateRequest.getPromocaoStatus().isEmpty())
            promocaoEntity.setPromocaoStatus(PromocaoStatusEnum.valueOf(promocaoUpdateRequest.getPromocaoStatus()));

        if (!promocaoUpdateRequest.getTipoDesconto().isEmpty())
            promocaoEntity.setTipoDesconto(TipoDescontoEnum.valueOf(promocaoUpdateRequest.getTipoDesconto()));

        if (promocaoUpdateRequest.getInicio() != null)
            promocaoEntity.setInicio(promocaoUpdateRequest.getInicio());

        if (promocaoUpdateRequest.getFim() != null)
            promocaoEntity.setFim(promocaoUpdateRequest.getFim());
        if (promocaoUpdateRequest.getLojas() != null) {
            promocaoEntity.getLojas().addAll(addLojas(promocaoUpdateRequest.getLojas()));
        }else {
            promocaoEntity.setLojas(null);
        }

        return mapper.promocaoEntitytopromocaoResponse(iPromocaoRepository.save(promocaoEntity));
    }

    public void apagarPromocao(String guid) {
        PromocaoEntity promocaoEntity = this.iPromocaoRepository.findByGuid(guid)
                .orElseThrow(()-> new PromocaoNaoEncontradaException("Promoção não encontrada."));

        iPromocaoRepository.delete(promocaoEntity);
    }

    public PromocaoResponse buscarPromocaoByGuid(String guid) {
        PromocaoEntity promocaoEntity = this.iPromocaoRepository.findByGuid(guid)
                .orElseThrow(()-> new PromocaoNaoEncontradaException("Promoção não encontrada."));

        return mapper.promocaoEntitytopromocaoResponse(promocaoEntity);
    }
}
