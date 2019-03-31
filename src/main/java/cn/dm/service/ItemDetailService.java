package cn.dm.service;


import cn.dm.common.Dto;
import cn.dm.vo.ItemDetailVo;
import cn.dm.vo.ItemPriceVo;
import cn.dm.vo.ItemSchedulerVo;

import java.util.List;

public interface ItemDetailService {
    public Dto<ItemDetailVo> queryItemDetail(Long id)throws  Exception;

    public Dto<List<ItemSchedulerVo>> queryItemScheduler(Long itemId)throws  Exception;

    public Dto<List<ItemPriceVo>> queryItemPrice(Long id)throws Exception;

}
