package com.group.libraryapp.domain.user

import com.group.libraryapp.domain.user.QUser.*
import com.group.libraryapp.domain.user.loanhistory.QUserLoanHistory.*
import com.querydsl.jpa.impl.JPAQueryFactory


class UserRepositoryCustomImpl (
    private val queryFactory: JPAQueryFactory
) : UserRepositoryCustom {

    override fun findAllWithHistories(): List<User> {
        return queryFactory
            .select(user)
            .distinct()
            .from(user)
            .leftJoin(user.userLoanHistories, userLoanHistory).fetchJoin()
            .fetch()
    }

}