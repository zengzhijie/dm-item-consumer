package cn.dm.controller;

import cn.dm.common.Constants;
import cn.dm.common.Dto;
import cn.dm.common.LogUtils;
import cn.dm.service.ItemDetailService;
import cn.dm.vo.ItemDetailVo;
import org.springframework.web.bind.annotation.*;
import sun.rmi.runtime.Log;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 商品详情界面contorller
 */
@RestController
@RequestMapping("/api")
public class ItemDetailController {
    @Resource
    private ItemDetailService itemDetailService;

    @Resource
    private LogUtils logUtils;

    @ResponseBody
    @RequestMapping(value = "/p/queryItemDetail", method = RequestMethod.POST)
    public Dto<ItemDetailVo> queryItemDetail(@RequestBody Map<String, Object> param) throws Exception {
        Integer id = Integer.parseInt(param.get("id").toString());
        logUtils.i(Constants.TOPIC.ITEM_CONSUMER, "查询商品id为" + id + "信息");
        return itemDetailService.queryItemDetail((long) id);
    }

}
