package com.example.dealcontracts.repository

import com.example.dealcontracts.repository.entity.Order
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository


@Repository
interface OrderRepository : CrudRepository<Order, Long> {

    @Query(
        """
                select * from orders
                where account_id = ?1
                order by updated_at desc
                limit 1
            """,
        nativeQuery = true
    )
    fun findUserLastOrder(accountId: String): Order?

}
