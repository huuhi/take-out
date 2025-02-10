package com.sky.query;


import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.xiaoymin.knife4j.core.util.StrUtil;
import lombok.Data;

/**
 * @author 胡志坚
 * @version 1.0
 * 创造日期 2025/2/9
 * 说明:
 */

@Data
public class PageQuery {
     private Integer page = 1;
     private Integer pageSize = 10;
     private String name;
     private String sortBy;
     private Boolean isAsc = true;

     //是否升序
     public <T> Page<T> toMpPage(OrderItem... orderItem){
        Page<T> page1 = Page.of(page, pageSize);

        if (StrUtil.isNotBlank(sortBy)) {
            if(isAsc){
                page1.addOrder(OrderItem.asc(sortBy));
            }else{
                page1.addOrder(OrderItem.desc(sortBy));
            }
        }else if(orderItem!= null){
//            默认按照更新时间排序
            page1.addOrder(orderItem);
        }
        return page1;
     }
     public <T> Page<T> toMpPageDefaultSortByUpdateTime() {
         return toMpPage(OrderItem.desc("update_time"));
     }
     public <T> Page<T> toMpPageDefaultSortByCreateTime() {
          return toMpPage(OrderItem.desc("create_time"));
     }

}
