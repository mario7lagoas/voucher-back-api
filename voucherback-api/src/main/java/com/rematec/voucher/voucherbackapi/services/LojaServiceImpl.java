package com.rematec.voucher.voucherbackapi.services;

import com.rematec.voucher.voucherbackapi.exceptios.LojaCadastradaException;
import com.rematec.voucher.voucherbackapi.exceptios.LojaNaoEncontradaException;
import com.rematec.voucher.voucherbackapi.exceptios.NaoPermitidoExcluirLojaException;
import com.rematec.voucher.voucherbackapi.interfaces.mapper.VouckBackMapper;
import com.rematec.voucher.voucherbackapi.interfaces.repositories.ILojaRepository;
import com.rematec.voucher.voucherbackapi.interfaces.services.ILojaService;
import com.rematec.voucher.voucherbackapi.models.entities.LojaEntity;
import com.rematec.voucher.voucherbackapi.models.requests.LojaPrintRequest;
import com.rematec.voucher.voucherbackapi.models.requests.LojaRequest;
import com.rematec.voucher.voucherbackapi.models.requests.UpdateStatusResquest;
import com.rematec.voucher.voucherbackapi.models.response.LojaResponse;
import com.rematec.voucher.voucherbackapi.models.response.LojasPaginadaResponse;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;


@Service
@Transactional
@Slf4j
public class LojaServiceImpl implements ILojaService {

    @Autowired
    private ILojaRepository iLojaReposity;

    @Autowired
    private VouckBackMapper mapper;

    @Override
    public LojaResponse addLoja(LojaRequest lojaRequest) {
        if (iLojaReposity.findByCnpj(lojaRequest.getCnpj()).isPresent()) {
            throw new LojaCadastradaException("CNPJ Já cadastrado.");
        }

        LojaEntity lojaEntity = LojaEntity.builder()
                .guid(UUID.randomUUID().toString())
                .nome(lojaRequest.getNome())
                .status(lojaRequest.getStatus())
                .cnpj(lojaRequest.getCnpj().replaceAll("[^0-9]", ""))
                .identificacao(lojaRequest.getIdentificacao())
                .build();

        return mapper.lojaEntityToLojaResponse(iLojaReposity.save(lojaEntity));

    }

    @Override
    public List<LojaResponse> getAll() {
        return mapper.listLojaEntityToListLojaResponse(iLojaReposity.findAll());
    }

    @Override
    public LojaResponse updateLoja(String guid, LojaRequest lojaRequest) {
        LojaEntity lojaEntity = iLojaReposity.findByGuid(guid)
                .orElseThrow(() -> new LojaNaoEncontradaException("Loja não encontrada"));

        if (!lojaRequest.getCnpj().isEmpty())
            lojaEntity.setCnpj(lojaRequest.getCnpj());

        if (!lojaRequest.getIdentificacao().isEmpty())
            lojaEntity.setIdentificacao(lojaRequest.getIdentificacao());
        if (!lojaRequest.getNome().isEmpty())
            lojaEntity.setNome(lojaRequest.getNome());

        if (lojaRequest.getStatus() != null)
            lojaEntity.setStatus(lojaRequest.getStatus());

        return mapper.lojaEntityToLojaResponse(iLojaReposity.save(lojaEntity));
    }

    @Override
    public void apagarLoja(String guid) {
        LojaEntity lojaEntity = this.iLojaReposity.findByGuid(guid)
                .orElseThrow(() -> new LojaNaoEncontradaException("Loja não encontrada"));

        if (!this.iLojaReposity.findByPromocoesLojasGuid(guid).isEmpty())
            throw new NaoPermitidoExcluirLojaException("Loja não pode ser Excluida. Pois está associada a alguma promoção");

        this.iLojaReposity.delete(lojaEntity);
    }

    @Override
    public LojaResponse buscarLojaByGuid(String guid) {
        LojaEntity lojaEntity = this.iLojaReposity.findByGuid(guid)
                .orElseThrow(() -> new LojaNaoEncontradaException("Loja não encontrada"));

        return mapper.lojaEntityToLojaResponse(lojaEntity);
    }

    @Override
    public LojaResponse updateStatus(String guid, UpdateStatusResquest statusResquest) {

        LojaEntity lojaEntity = this.iLojaReposity.findByGuid(guid)
                .orElseThrow(() -> new LojaNaoEncontradaException("Loja não encontrada"));

        lojaEntity.setStatus(statusResquest.getStatus());
        log.info("Atualizando status da loja [{}]", lojaEntity.getNome());

        return mapper.lojaEntityToLojaResponse(iLojaReposity.save(lojaEntity));

    }
    @Override
    public List<LojaResponse> getLojasAtivas() {
        return mapper.listLojaEntityToListLojaResponse(iLojaReposity.findByStatusTrue());
    }
    @Override
    public LojasPaginadaResponse obterLojasPaginadas(String cnpj, int page, int size) {

        return mapper.pageLojasEntityToLojasPaginadaResponse(
                this.iLojaReposity.findByCnpjContaining(cnpj.replaceAll("[^0-9]", ""),
                        PageRequest.of(page, size) )
        );
    }

    @Override
    public String printLojas(List<LojaPrintRequest> prints) {
        try {
        Map<String, Object> parametros = new HashMap<>();
        parametros.put("REPORT_LOCALE", new Locale("pt", "BR"));
        parametros.put("logo", this.getClass().getResourceAsStream("/static/img/lojaRelatorio.jpg"));
        InputStream inputStream = this.getClass().getResourceAsStream("/relatorios/relatorio-de-lojas.jasper");

        JasperPrint jasperPrint = JasperFillManager.fillReport(inputStream, parametros,
                new JRBeanCollectionDataSource(prints));

        byte[] relatorio = JasperExportManager.exportReportToPdf(jasperPrint);

        String base64Pdf = "data:application/pdf;base64," + Base64.encodeBase64String(relatorio);

        return base64Pdf;
    } catch (
    JRException e) {
        throw new RuntimeException("Erro em gerar o PDF " + e);
    }

    }
}
