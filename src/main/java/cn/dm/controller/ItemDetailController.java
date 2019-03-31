package cn.dm.controller;

import cn.dm.common.Constants;
import cn.dm.common.Dto;
import cn.dm.common.LogUtils;
import cn.dm.service.ItemDetailService;
import cn.dm.vo.ItemDetailVo;
import cn.dm.vo.ItemPriceVo;
import cn.dm.vo.ItemSchedulerVo;
import org.springframework.web.bind.annotation.*;
import sun.rmi.runtime.Log;

import javax.annotation.Resource;
import java.util.List;
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

    @ResponseBody
    @RequestMapping(value = "/p/queryItemScheduler",method = RequestMethod.POST)
    public Dto<List<ItemSchedulerVo>> queryItemScheduler(@RequestBody Map<String, Object> param) throws Exception {
        Integer id = Integer.parseInt(param.get("itemId").toString());
        return itemDetailService.queryItemScheduler((long)id);
    }
    @ResponseBody
    @RequestMapping(value = "/p/queryItemPrice",method = RequestMethod.POST)
    public Dto<List<ItemPriceVo>> queryItemPrice(@RequestBody Map<String, Object> param) throws Exception {
        Integer id = Integer.parseInt(param.get("scheduleId").toString());
        return itemDetailService.queryItemPrice((long)id);
    }

}
