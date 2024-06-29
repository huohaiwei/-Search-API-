package com.yellow.usermanager.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yellow.usermanager.common.BaseResponse;
import com.yellow.usermanager.common.ResultUtils;
import com.yellow.usermanager.design.SearchFacade;
import com.yellow.usermanager.exception.BusinessException;
import com.yellow.usermanager.exception.ThrowUtils;
import com.yellow.usermanager.model.dto.interfaceinfo.InterfaceInfoQueryRequest;
import com.yellow.usermanager.model.dto.search.SearchQuery;
import com.yellow.usermanager.model.dto.user.UserQueryRequest;
import com.yellow.usermanager.model.enums.ErrorCode;
import com.yellow.usermanager.model.enums.SearchTypeEnum;
import com.yellow.usermanager.model.vo.InterfaceInfoVO;
import com.yellow.usermanager.model.vo.SearchVO;
import com.yellow.usermanager.model.vo.UserVO;
import com.yellow.usermanager.service.InterfaceInfoService;
import com.yellow.usermanager.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * @author 陈翰垒
 */
@RestController
@RequestMapping("/search")
@Slf4j
@CrossOrigin(origins = {"http://localhost:8000"}, allowCredentials = "true")
public class SearchController {

    @Resource
    private UserService userService;

    @Resource
    private InterfaceInfoService interfaceInfoService;

    @Resource
    private SearchFacade searchFacade;

    @PostMapping("/all")
    public BaseResponse<SearchVO> searchAll(@RequestBody SearchQuery searchQuery) {
       return ResultUtils.success(searchFacade.searchAll(searchQuery));
    }

}

