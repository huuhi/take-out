package com.sky.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sky.dto.OrdersCancelDTO;
import com.sky.dto.OrdersRejectionDTO;
import com.sky.entity.Orders;
import com.sky.service.OrdersService;
import com.sky.mapper.OrdersMapper;
import com.sky.vo.OrderStatisticsVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
* @author windows
* @description 针对表【orders(订单表)】的数据库操作Service实现
* @createDate 2025-02-12 14:16:36
*/
@Service
@Slf4j
public class OrdersServiceImpl extends ServiceImpl<OrdersMapper, Orders>
    implements OrdersService{

    @Override
    public boolean updateStatus(Integer id, int i) {
        Orders order = getById(id);
        order.setStatus(i);
        return updateById(order);
    }

    @Override
    public boolean rejection(OrdersRejectionDTO ordersRejectionDTO, int i) {
        Long id = ordersRejectionDTO.getId();
        String rejectionReason = ordersRejectionDTO.getRejectionReason();
        Orders order = getById(id);
        order.setStatus(i);
        order.setRejectionReason(rejectionReason);
        return updateById(order);
    }

    @Override
    public boolean cancel(OrdersCancelDTO ordersCancelDTO, Integer cancelled) {
        Long id = ordersCancelDTO.getId();
        String cancelReason = ordersCancelDTO.getCancelReason();
        Orders order = getById(id);
        order.setCancelReason(cancelReason);
        order.setStatus(cancelled);
        return updateById(order);
    }

    @Override
    public OrderStatisticsVO getStatistics() {
        // TODO: 2025-02-12 待实现
        QueryWrapper<Orders> wrapper = new QueryWrapper<>();
        wrapper.select("count(*) as count","status")
                .in("status",2,3,4)
                .groupBy("status");
        List<Map<String, Object>> maps = baseMapper.selectMaps(wrapper);
        log.info("maps: {}", maps);
        //maps: [{count=1, status=2}, {count=1, status=3}]
        OrderStatisticsVO orderStatisticsVO = new OrderStatisticsVO();
        for(Map<String, Object> map : maps){
            Long o2 = (Long) map.get("count");
            Integer o1 = (Integer) map.get("status");
            map.put("count", o2.intValue());
            Integer o = (Integer) map.get("count");
            switch (o1){
                case 2:
                    orderStatisticsVO.setToBeConfirmed(o);
                    break;
                case 3:
                    orderStatisticsVO.setConfirmed(o);
                    break;
                case 4:
                    orderStatisticsVO.setDeliveryInProgress(o);
                    break;
                default:
                    break;
            }
        }
        return orderStatisticsVO;
    }
}




