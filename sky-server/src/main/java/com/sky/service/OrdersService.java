package com.sky.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.sky.dto.OrdersCancelDTO;
import com.sky.dto.OrdersRejectionDTO;
import com.sky.entity.Orders;
import com.sky.vo.OrderStatisticsVO;

/**
* @author windows
* @description 针对表【orders(订单表)】的数据库操作Service
* @createDate 2025-02-12 14:16:36
*/
public interface OrdersService extends IService<Orders> {

    boolean updateStatus(Integer id, int i);

    boolean rejection(OrdersRejectionDTO ordersRejectionDTO, int i);

    boolean cancel(OrdersCancelDTO ordersCancelDTO, Integer cancelled);

    OrderStatisticsVO getStatistics();

}
