package cn.dm.service.impl;

import cn.dm.client.RestDmCinemaClient;
import cn.dm.client.RestDmImageClient;
import cn.dm.client.RestDmItemClient;
import cn.dm.common.*;
import cn.dm.pojo.DmCinema;
import cn.dm.pojo.DmImage;
import cn.dm.pojo.DmItem;
import cn.dm.service.ItemDetailService;
import cn.dm.vo.ItemDetailVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.ParseException;
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
