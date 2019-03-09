package cn.dm.controller;

import cn.dm.common.Dto;
import cn.dm.common.DtoUtil;
import cn.dm.service.HomeService;
import cn.dm.vo.HotItemVo;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 首页contorller
 */
@RestController
@RequestMapping("api/p/index")
public class HomeController {

    @Resource
    private HomeService homeService;

    @RequestMapping(value = "/queryBanner", method = RequestMethod.POST)
    public Dto<HotItemVo> queryBanner() throws Exception {
        List<HotItemVo> hotItemVoList = homeService.queryBanner();
        return DtoUtil.returnDataSuccess(hotItemVoList);
    }

}
