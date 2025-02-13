package com.sky.controller.admin;

import com.sky.dto.OrdersCancelDTO;
import com.sky.dto.OrdersRejectionDTO;
import com.sky.entity.Orders;
import com.sky.result.Result;
import com.sky.service.OrdersService;
import com.sky.vo.OrderStatisticsVO;
import com.sky.vo.OrderVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author 胡志坚
 * @version 1.0
 * 创造日期 2025/2/12
 * 说明:
 */
@RestController
@RequestMapping("/admin/order")
public class OrderController {
    @Autowired
    private OrdersService orderService;

//    接单
    @PutMapping("/confirm")
    public Result confirm(@RequestBody Integer id) {
//        将status设置为3
        boolean b = orderService.updateStatus(id, Orders.CONFIRMED);
        return b? Result.success() : Result.error("接单失败");
    }
//    拒单
    @PutMapping("/rejection")
    public Result rejection(@RequestBody OrdersRejectionDTO ordersRejectionDTO) {
        boolean b = orderService.rejection(ordersRejectionDTO, Orders.CANCELLED);
        return b? Result.success() : Result.error("拒单失败");
    }
//    取消订单
    @PutMapping("/cancel")
    public Result cancel(@RequestBody OrdersCancelDTO ordersCancelDTO) {
        boolean b= orderService.cancel(ordersCancelDTO, Orders.CANCELLED);
        return b? Result.success() : Result.error("取消订单失败");
    }
//    订单统计
    @GetMapping("/statistics")
    public Result<OrderStatisticsVO> statistics() {
//        2,3,4
        OrderStatisticsVO orderStatisticsVO = orderService.getStatistics();
        return Result.success(orderStatisticsVO);
    }
//    完成订单
    @PutMapping("/complete/{id}")
    public Result complete(@PathVariable("id") Integer id) {
        boolean b = orderService.updateStatus(id, Orders.COMPLETED);
        return b? Result.success() : Result.error("完成订单失败");
    }
//    查询订单详情
//    @GetMapping("/details/{id}")
//    public Result<OrderVO> getDetails(@PathVariable("id") Integer id) {
//
//    }
}
