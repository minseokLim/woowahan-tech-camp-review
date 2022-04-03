package com.minseoklim.woowahantechcampreview.lotto.domain.repository;

import static com.minseoklim.woowahantechcampreview.lotto.domain.QPurchase.*;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import com.querydsl.core.QueryResults;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import com.minseoklim.woowahantechcampreview.lotto.domain.Purchase;

@Repository
public class PurchaseRepositorySupport extends QuerydslRepositorySupport {
    private final JPAQueryFactory queryFactory;

    public PurchaseRepositorySupport(final JPAQueryFactory queryFactory) {
        super(Purchase.class);
        this.queryFactory = queryFactory;
    }

    public Page<Purchase> findAllByUserId(final Long userId, final Pageable pageable) {
        final JPAQuery<Purchase> query = queryFactory.selectFrom(purchase)
            .join(purchase.round)
            .fetchJoin()
            .where(purchase.userId.eq(userId));

        final JPQLQuery<Purchase> purchaseJPQLQuery = getQuerydsl().applyPagination(pageable, query);
        final QueryResults<Purchase> results = purchaseJPQLQuery.fetchResults();
        return new PageImpl(results.getResults(), pageable, results.getTotal());
    }
}
