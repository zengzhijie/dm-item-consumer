package cn.dm.service;


import cn.dm.common.Dto;
import cn.dm.vo.ItemDetailVo;

public interface ItemDetailService {
    public Dto<ItemDetailVo> queryItemDetail(Long id)throws  Exception;
}
