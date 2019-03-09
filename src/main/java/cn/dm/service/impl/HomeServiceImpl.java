package cn.dm.service.impl;

import cn.dm.client.RestDmImageClient;
import cn.dm.client.RestDmItemClient;
import cn.dm.common.Constants;
import cn.dm.common.EmptyUtils;
import cn.dm.pojo.DmImage;
import cn.dm.pojo.DmItem;
import cn.dm.service.HomeService;
import cn.dm.vo.HotItemVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class HomeServiceImpl implements HomeService {
    @Autowired
    private RestDmItemClient restDmItemClient;
    @Autowired
    private RestDmImageClient restDmImageClient;

    @Override
    public List<HotItemVo> queryBanner() throws Exception {
        //查询轮播图前5个
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("isBanner", 1);
        param.put("beginPos", 0);
        param.put("pageSize", 5);
        List<DmItem> dmItemList = restDmItemClient.getDmItemListByMap(param);
        //组装接口返回数据
        List<HotItemVo> hotItemVoList = new ArrayList<HotItemVo>();
        if (EmptyUtils.isEmpty(dmItemList)) {
            return null;
        }
        for (DmItem dmItem : dmItemList) {
            HotItemVo hotItemVo = new HotItemVo();
            BeanUtils.copyProperties(dmItem, hotItemVo);
            //查询图片信息
            List<DmImage> dmImageList = restDmImageClient.queryDmImageList(dmItem.getId(),
                    Constants.Image.ImageType.carousel,
                    Constants.Image.ImageCategory.item);
            //组装图片信息
            hotItemVo.setImgUrl(EmptyUtils.isEmpty(dmImageList) ? null : dmImageList.get(0).getImgUrl());
            hotItemVoList.add(hotItemVo);
        }
        return hotItemVoList;
    }
}
