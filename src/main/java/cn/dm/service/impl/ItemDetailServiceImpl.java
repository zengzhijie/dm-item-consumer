package cn.dm.service.impl;

import cn.dm.client.*;
import cn.dm.common.*;
import cn.dm.exception.ItemErrorCode;
import cn.dm.pojo.*;
import cn.dm.service.ItemDetailService;
import cn.dm.vo.ItemDetailVo;
import cn.dm.vo.ItemPriceVo;
import cn.dm.vo.ItemSchedulerVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ItemDetailServiceImpl implements ItemDetailService {

    @Autowired
    private RestDmItemClient dmItemClient;
    @Autowired
    private RestDmImageClient dmImageClient;
    @Autowired
    private RestDmCinemaClient restDmCinemaClient;

    @Autowired
    private RestDmSchedulerClient restDmSchedulerClient;
    @Autowired
    RestDmSchedulerSeatClient restDmSchedulerSeatClient;
    @Autowired
    RestDmSchedulerSeatPriceClient restDmSchedulerSeatPriceClient;

    @Override
    public Dto<ItemDetailVo> queryItemDetail(Long id) throws Exception {
        DmItem dmItem = dmItemClient.getDmItemById(id);
        if (EmptyUtils.isEmpty(dmItem)) {
            return null;
        }
        //查询图片
        List<DmImage> dmImageList = getImageList(dmItem.getId(), Constants.Image.ImageType.carousel, Constants.Image.ImageCategory.item);
        //查询剧院
        DmCinema dmCinema = restDmCinemaClient.getDmCinemaById(dmItem.getCinemaId());
        //组装返回数据
        ItemDetailVo itemDetailVo = copyData(dmItem, dmCinema, dmImageList);
        return DtoUtil.returnDataSuccess(itemDetailVo);
    }

    @Override
    public Dto<List<ItemSchedulerVo>> queryItemScheduler(Long itemId) throws Exception {
        //根据商品主键查询商品信息
        DmImage dmItem = dmImageClient.getDmImageById(itemId);
        if (EmptyUtils.isEmpty(dmItem)) {
            throw new BaseException(ItemErrorCode.ITEM_NO_DATA);
        }
        HashMap<String, Object> param = new HashMap<>();
        param.put("itemId", dmItem.getId());
        List<DmScheduler> dmSchedulerList = restDmSchedulerClient.getDmSchedulerListByMap(param);
        if (EmptyUtils.isEmpty(dmSchedulerList)) {
            throw new BaseException(ItemErrorCode.ITEM_NO_DATA);
        }
        List<ItemSchedulerVo> itemSchedulerVoList = new ArrayList<ItemSchedulerVo>();
        for (DmScheduler dmScheduler : dmSchedulerList) {
            ItemSchedulerVo itemSchedulerVo = new ItemSchedulerVo();
            BeanUtils.copyProperties(dmItem, itemSchedulerVo);
            BeanUtils.copyProperties(dmScheduler, itemSchedulerVo);
            //更新显示字段-时间字段
            itemSchedulerVo.setStartTime(DateUtil.format(dmScheduler.getStartTime()));
            itemSchedulerVo.setEndTime(DateUtil.format(dmScheduler.getEndTime()));
            itemSchedulerVoList.add(itemSchedulerVo);
        }
        return DtoUtil.returnDataSuccess(itemSchedulerVoList);
    }

    @Override
    public Dto<List<ItemPriceVo>> queryItemPrice(Long id) throws Exception {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("scheduleId", id);
        //根据排期ID查询排期价格信息
        List<DmSchedulerSeatPrice> dmSchedulerSeatPriceList = restDmSchedulerSeatPriceClient.getDmSchedulerSeatPriceListByMap(param);
        if (EmptyUtils.isEmpty(dmSchedulerSeatPriceList)) {
            throw new BaseException(ItemErrorCode.ITEM_NO_DATA);
        }
        //组装返回显示信息
        List<ItemPriceVo> itemPriceVoList = new ArrayList<ItemPriceVo>();
        for (DmSchedulerSeatPrice dmSchedulerSeatPrice : dmSchedulerSeatPriceList) {
            ItemPriceVo itemPriceVo = new ItemPriceVo();
            BeanUtils.copyProperties(dmSchedulerSeatPrice, itemPriceVo);
            //查询所有是有效的并且没有被锁定的座位
            Map<String, Object> lockMap = new HashMap<String, Object>();
            lockMap.put("status", 1);
            lockMap.put("scheduleId", itemPriceVo.getScheduleId());
            int num = restDmSchedulerSeatClient.getDmSchedulerSeatCountByMap(lockMap);
            int isHaveSeat = num > 0 ? 1 : 0;
            itemPriceVo.setIsHaveSeat(isHaveSeat);
            itemPriceVoList.add(itemPriceVo);
        }
        return DtoUtil.returnDataSuccess(itemPriceVoList);
    }


    private ItemDetailVo copyData(DmItem dmItem, DmCinema dmCinema, List<DmImage> dmImageList) throws ParseException {
        ItemDetailVo itemDetailVo = new ItemDetailVo();
        BeanUtils.copyProperties(dmItem, itemDetailVo);
        itemDetailVo.setStartTime(DateUtil.format(dmItem.getStartTime()));
        itemDetailVo.setEndTime(DateUtil.format(dmItem.getEndTime()));
        itemDetailVo.setState(dmItem.getState() + "");
        itemDetailVo.setImgUrl(dmImageList.get(0).getImgUrl());
        if (EmptyUtils.isEmpty(dmCinema)) {
            //封装剧院信息
            BeanUtils.copyProperties(dmCinema, itemDetailVo);
        }
        itemDetailVo.setId(dmItem.getId());
        return itemDetailVo;
    }

    /**
     * 查询图片
     *
     * @param id
     * @param type
     * @param category
     * @return
     */
    public List<DmImage> getImageList(Long id, Integer type, Integer category) throws Exception {
        Map<String, Object> paramImage = new HashMap<String, Object>();
        paramImage.put("targetId", id);
        paramImage.put("type", type);
        paramImage.put("category", category);
        return dmImageClient.queryDmImageList(id, type, category);
    }
}
