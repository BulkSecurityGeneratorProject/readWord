package com.qigu.readword.repository;

import com.qigu.readword.domain.VipOrder;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import java.util.List;

/**
 * Spring Data JPA repository for the VipOrder entity.
 */
@SuppressWarnings("unused")
@Repository
public interface VipOrderRepository extends JpaRepository<VipOrder, Long>, JpaSpecificationExecutor<VipOrder> {

    @Query("select vip_order from VipOrder vip_order where vip_order.user.login = ?#{principal.username}")
    List<VipOrder> findByUserIsCurrentUser();

    VipOrder findByOutTradeNoEquals(String outTradeNo);

}
