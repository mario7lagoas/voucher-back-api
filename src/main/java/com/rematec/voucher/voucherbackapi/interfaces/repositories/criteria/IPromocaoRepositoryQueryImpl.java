package com.rematec.voucher.voucherbackapi.interfaces.repositories.criteria;

import com.rematec.voucher.voucherbackapi.interfaces.mapper.VouckBackMapper;
import com.rematec.voucher.voucherbackapi.models.entities.PromocaoEntity;
import com.rematec.voucher.voucherbackapi.models.filter.PromocaoFiltro;
import com.rematec.voucher.voucherbackapi.models.response.PromocoesPaginadaResponse;
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

public class IPromocaoRepositoryQueryImpl implements IPromocaoRepositoryQuery {

    @PersistenceContext
    private EntityManager manager;

    @Autowired
    private VouckBackMapper mapper;

    @Override
    public PromocoesPaginadaResponse filtrar(PromocaoFiltro promocaoFiltro, Pageable page) {
        From<?, ?> orderByFromEntity = null;
        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<PromocaoEntity> criteriaQuery = builder.createQuery(PromocaoEntity.class);

        Root<PromocaoEntity> root = criteriaQuery.from(PromocaoEntity.class);

        Predicate[] predicates = criarRestricted(promocaoFiltro, builder, root);
        criteriaQuery.where(predicates);

        orderByFromEntity = root;

        List<Order> orderList = new ArrayList();

        orderList.add(builder.asc(orderByFromEntity.get("promocaoStatus")));
        orderList.add(builder.desc(orderByFromEntity.get("fim")));
        orderList.add(builder.desc(orderByFromEntity.get("dataCadastro")));

        criteriaQuery.orderBy(orderList);

        TypedQuery<PromocaoEntity> query = manager.createQuery(criteriaQuery);

        additionalRestrictedDePaginate(query, page);

        return mapper.pagePromocoesEntityToPromocoesPaginadaResponse(new PageImpl<>(query.getResultList(),
                page, total(promocaoFiltro)));
    }

    private Long total(PromocaoFiltro promocaoFiltro) {

        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<Long> criteria = builder.createQuery(Long.class);
        Root<PromocaoEntity> root = criteria.from(PromocaoEntity.class);

        Predicate[] predicates = criarRestricted(promocaoFiltro, builder, root);
        criteria.where(predicates);
        criteria.select(builder.count(root));
        return manager.createQuery(criteria).getSingleResult();
    }

    private void additionalRestrictedDePaginate(TypedQuery<PromocaoEntity> query, Pageable page) {
        int pagingActual = page.getPageNumber();
        int totalDeRegistryForPaging = page.getPageSize();
        int primerRegistryDaPaging = pagingActual * totalDeRegistryForPaging;

        query.setFirstResult(primerRegistryDaPaging);
        query.setMaxResults(totalDeRegistryForPaging);

    }

    private Predicate[] criarRestricted(PromocaoFiltro promocaoFiltro, CriteriaBuilder builder, Root<PromocaoEntity> root) {

        List<Predicate> predicates = new ArrayList<>();

        if (!StringUtils.isEmpty(promocaoFiltro.getDescricao())) {
            predicates.add(builder.like(
                    builder.lower(root.get("descricao")), "%"
                            + promocaoFiltro.getDescricao()
                            .toUpperCase() + "%")
            );
        }

        if (promocaoFiltro.getInicio() != null)
            predicates.add(builder.greaterThanOrEqualTo(root.get("inicio"),
                    promocaoFiltro.getInicio()));

        if (promocaoFiltro.getFim() != null)
            predicates.add(builder.lessThanOrEqualTo(root.get("fim"),
                    promocaoFiltro.getFim()));

        if (promocaoFiltro.getPromocaoStatus() != null && !promocaoFiltro.getPromocaoStatus().isEmpty())
            predicates.add(builder.equal(root.get("promocaoStatus"),
                    promocaoFiltro.getPromocaoStatus()));

        if (promocaoFiltro.getTipoDesconto() != null && !promocaoFiltro.getTipoDesconto().isEmpty())
            predicates.add(builder.equal(root.get("tipoDesconto"),
                    promocaoFiltro.getTipoDesconto()));


        return predicates.toArray(new Predicate[predicates.size()]);


    }
}
