package com.yellow.usermanager.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yellow.usermanager.annotation.AuthChecker;
import com.yellow.usermanager.common.BaseResponse;
import com.yellow.usermanager.common.ResultUtils;
import com.yellow.usermanager.constant.UserConstant;
import com.yellow.usermanager.mapper.UserInterfaceInfoMapper;
import com.yellow.usermanager.service.InterfaceInfoService;
import com.yellow.yellowapicommon.model.entity.InterfaceInfo;
import com.yellow.yellowapicommon.model.entity.UserInterfaceInfo;
import com.yellow.yellowapicommon.model.vo.StatisticsVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author 陈翰垒
 */
@RestController
@Slf4j
@RequestMapping("/analysis")
@CrossOrigin(origins = {"http://localhost:8000","https://poorly-desired-macaw.ngrok-free.app"}, allowCredentials = "true")
public class AnalysisController {

    @Resource
    private UserInterfaceInfoMapper userInterfaceInfoMapper;

    @Resource
    private InterfaceInfoService interfaceInfoService;

    @GetMapping("/top/interface/invoke")
    @AuthChecker(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<List<StatisticsVO>> getTopInterfaceInvoke() {
        List<UserInterfaceInfo> userInterfaceInfoList = userInterfaceInfoMapper.getTopInvokeInterfaceInfo(3);
        Map<Long, List<UserInterfaceInfo>> interfaceInfoIdObjMap =
                userInterfaceInfoList.stream()
                        .collect(Collectors.groupingBy(UserInterfaceInfo::getInterfaceInfoId));
        QueryWrapper<InterfaceInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("id", interfaceInfoIdObjMap.keySet());
        List<InterfaceInfo> interfaceInfoList = interfaceInfoService.list(queryWrapper);
        List<StatisticsVO> res = interfaceInfoList.stream().map(interfaceInfo -> {
            StatisticsVO statisticsVO = new StatisticsVO();
            BeanUtils.copyProperties(interfaceInfo, statisticsVO);
            statisticsVO.setTotalNum(interfaceInfoIdObjMap.get(interfaceInfo.getId()).get(0).getTotalNum());
            return statisticsVO;
        }).collect(Collectors.toList());
        return ResultUtils.success(res);
    }
}
