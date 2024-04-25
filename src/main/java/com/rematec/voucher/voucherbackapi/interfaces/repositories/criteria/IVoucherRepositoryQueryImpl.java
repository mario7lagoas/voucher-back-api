package com.rematec.voucher.voucherbackapi.interfaces.repositories.criteria;

import com.rematec.voucher.voucherbackapi.interfaces.mapper.VouckBackMapper;
import com.rematec.voucher.voucherbackapi.models.entities.VoucherEntity;
import com.rematec.voucher.voucherbackapi.models.filter.VoucherFiltro;
import com.rematec.voucher.voucherbackapi.models.response.VouchersPaginadaResponse;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.From;
import jakarta.persistence.criteria.Order;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class IVoucherRepositoryQueryImpl implements IVoucherRepositoryQuery {

    @PersistenceContext
    private EntityManager manager;

    @Autowired
    private VouckBackMapper mapper;

    @Override
    public VouchersPaginadaResponse filtrar(VoucherFiltro voucherFiltro, Pageable page) {
        From<?, ?> orderByFromEntity = null;
        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<VoucherEntity> criteriaQuery = builder.createQuery(VoucherEntity.class);

        Root<VoucherEntity> root = criteriaQuery.from(VoucherEntity.class);

        Predicate[] predicates = criarRestricted(voucherFiltro, builder, root);
        criteriaQuery.where(predicates);

        orderByFromEntity = root;

        List<Order> orderList = new ArrayList();

        //orderList.add(builder.asc(orderByFromEntity.get("voucherStatus")));
        orderList.add(builder.desc(orderByFromEntity.get("dataAtualizacao")));
        orderList.add(builder.desc(orderByFromEntity.get("filialCnpj")));

        criteriaQuery.orderBy(orderList);

        TypedQuery<VoucherEntity> query = manager.createQuery(criteriaQuery);

        additionalRestrictedDePaginate(query, page);

        return mapper.pageVouchersEntityToVouchersPaginadaResponse(new PageImpl<>(query.getResultList(),
                page, total(voucherFiltro)));
    }

    private Long total(VoucherFiltro voucherFiltro) {

        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<Long> criteria = builder.createQuery(Long.class);
        Root<VoucherEntity> root = criteria.from(VoucherEntity.class);

        Predicate[] predicates = criarRestricted(voucherFiltro, builder, root);
        criteria.where(predicates);
        criteria.select(builder.count(root));
        return manager.createQuery(criteria).getSingleResult();
    }

    private void additionalRestrictedDePaginate(TypedQuery<VoucherEntity> query, Pageable page) {
        int pagingActual = page.getPageNumber();
        int totalDeRegistryForPaging = page.getPageSize();
        int primerRegistryDaPaging = pagingActual * totalDeRegistryForPaging;

        query.setFirstResult(primerRegistryDaPaging);
        query.setMaxResults(totalDeRegistryForPaging);

    }

    private Predicate[] criarRestricted(VoucherFiltro voucherFiltro, CriteriaBuilder builder, Root<VoucherEntity> root) {

        List<Predicate> predicates = new ArrayList<>();

        if (!StringUtils.isEmpty(voucherFiltro.getDescricao())) {
            predicates.add(builder.like(
                    builder.lower(root.get("descricao")), "%"
                            + voucherFiltro.getDescricao()
                            .toUpperCase() + "%")
            );
        }

        if (voucherFiltro.getInicio() != null)
            predicates.add(builder.greaterThanOrEqualTo(root.get("inicio"),
                    voucherFiltro.getInicio()));

        if (voucherFiltro.getFim() != null)
            predicates.add(builder.lessThanOrEqualTo(root.get("fim"), voucherFiltro.getFim()));

        if (voucherFiltro.getCodigo() != null && !voucherFiltro.getCodigo().isEmpty())
            predicates.add(builder.equal(root.get("codigo"), voucherFiltro.getCodigo()));

        if (voucherFiltro.getClienteCpf() != null && !voucherFiltro.getClienteCpf().isEmpty())
            predicates.add(builder.equal(root.get("clienteCpf"), voucherFiltro.getClienteCpf()));

        if (voucherFiltro.getPdv() != null && !voucherFiltro.getPdv().isEmpty())
            predicates.add(builder.equal(root.get("pdv"), voucherFiltro.getPdv()));

        if (voucherFiltro.getVoucherStatus() != null && !voucherFiltro.getVoucherStatus().isEmpty())
            predicates.add(builder.equal(root.get("voucherStatus"), voucherFiltro.getVoucherStatus()));

        if (voucherFiltro.getCupomResgate() != null && !voucherFiltro.getCupomResgate().isEmpty())
            predicates.add(builder.equal(root.get("cupomResgate"), voucherFiltro.getPdv()));

        if (voucherFiltro.getFilialCnpj() != null && !voucherFiltro.getFilialCnpj().isEmpty())
            predicates.add(builder.equal(root.get("filialCnpj"), voucherFiltro.getFilialCnpj()));

        if (voucherFiltro.getTipoDesconto() != null && !voucherFiltro.getTipoDesconto().isEmpty())
            predicates.add(builder.equal(root.get("tipoDesconto"), voucherFiltro.getTipoDesconto()));

        return predicates.toArray(new Predicate[predicates.size()]);

    }

}
