package com.sky.dto;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author 胡志坚
 * @version 1.0
 * 创造日期 2025/2/9
 * 说明:分页查询结果
 */
@Data
public class PageDTO<T> {
    private Long total;
    private List<T> records;

    public  static <PO,VO> PageDTO<VO> of(Page<PO> result, Class<VO> voClass){
        PageDTO<VO> voPageDTO = new PageDTO<>();
        voPageDTO.setTotal(result.getTotal());
        if(CollUtil.isEmpty(result.getRecords())){
            voPageDTO.setRecords(Collections.emptyList());
            return voPageDTO;
        }
        List<VO> data = BeanUtil.copyToList(result.getRecords(), voClass);
        voPageDTO.setRecords(data);
        return voPageDTO;
    }

    public  static <PO,VO> PageDTO<VO> of(Page<PO> result, Function<PO,VO> converter){
        PageDTO<VO> voPageDTO = new PageDTO<>();
        voPageDTO.setTotal(result.getTotal());
        List<PO> records = result.getRecords();
        if(CollUtil.isEmpty(records)){
            voPageDTO.setRecords(Collections.emptyList());
            return voPageDTO;
        }
        voPageDTO.setRecords(records.stream().map(converter).collect(Collectors.toList()));
        return voPageDTO;
    }
}
