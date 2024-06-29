package com.yellow.usermanager.design;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yellow.usermanager.common.BaseResponse;
import com.yellow.usermanager.common.ResultUtils;
import com.yellow.usermanager.exception.BusinessException;
import com.yellow.usermanager.exception.ThrowUtils;
import com.yellow.usermanager.model.dto.interfaceinfo.InterfaceInfoQueryRequest;
import com.yellow.usermanager.model.dto.search.InterfaceInfoQuery;
import com.yellow.usermanager.model.dto.search.SearchQuery;
import com.yellow.usermanager.model.dto.user.UserQueryRequest;
import com.yellow.usermanager.model.entity.Picture;
import com.yellow.usermanager.model.enums.ErrorCode;
import com.yellow.usermanager.model.enums.SearchTypeEnum;
import com.yellow.usermanager.model.vo.InterfaceInfoVO;
import com.yellow.usermanager.model.vo.SearchVO;
import com.yellow.usermanager.model.vo.UserVO;
import com.yellow.usermanager.service.InterfaceInfoService;
import com.yellow.usermanager.service.UserService;
import com.yellow.yellowapicommon.model.entity.InterfaceInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * 搜索门面
 * @author 陈翰垒
 */
@Component
@Slf4j
public class SearchFacade {
    @Resource
    private UserService userService;

    @Resource
    private InterfaceInfoService interfaceInfoService;

    public SearchVO searchAll(SearchQuery searchQuery) {
        String type = searchQuery.getType();
        SearchTypeEnum searchTypeEnum = SearchTypeEnum.getEnumByValue(type);
//        ThrowUtils.throwIf(StringUtils.isBlank(type), ErrorCode.PARAMS_ERROR, "参数有误");
        String searchText = searchQuery.getSearchText();
        int current = searchQuery.getCurrent();
        int pageSize = searchQuery.getPageSize();
        if (searchTypeEnum == null) {
            CompletableFuture<Page<UserVO>> userTask = CompletableFuture.supplyAsync(() -> {
                UserQueryRequest userQueryRequest = new UserQueryRequest();
                userQueryRequest.setUserName(searchText);
                Page<UserVO> userVOPage = userService.searchUserVOByPage(userQueryRequest);
                return userVOPage;
            });

            CompletableFuture<Page<InterfaceInfo>> interfaceTask = CompletableFuture.supplyAsync(() -> {
                InterfaceInfoQueryRequest interfaceInfoQueryRequest = new InterfaceInfoQueryRequest();
                interfaceInfoQueryRequest.setName(searchText);
                Page<InterfaceInfo> interfaceInfoVOPage = doSearch(searchText,current,pageSize);
                return interfaceInfoVOPage;
            });

            CompletableFuture<Page<Picture>> pictureTask = CompletableFuture.supplyAsync(() -> {
                Page<Picture> picturePage= doSearchPicture(searchText,1,10);
                return picturePage;
            });

            CompletableFuture.allOf(userTask, interfaceTask,pictureTask).join();
            try {
                Page<UserVO> userVOPage = userTask.get();
                Page<InterfaceInfo> interfaceInfoPage = interfaceTask.get();
                Page<Picture> picturePage = pictureTask.get();
                SearchVO searchVO = new SearchVO();
                searchVO.setUserVOList(userVOPage.getRecords());
                searchVO.setInterfaceInfoList(interfaceInfoPage.getRecords());
                searchVO.setPictureList(picturePage.getRecords());
                return searchVO;
            } catch (Exception e) {
                log.error("查询失败", e);
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数有误");
            }
        } else {
            SearchVO searchVO = new SearchVO();
            switch (searchTypeEnum) {
                case USER:
                    UserQueryRequest userQueryRequest = new UserQueryRequest();
                    userQueryRequest.setUserName(searchText);
                    Page<UserVO> userVOPage = userService.searchUserVOByPage(userQueryRequest);
                    searchVO.setUserVOList(userVOPage.getRecords());
                    break;
                case INTERFACE:
                    Page<InterfaceInfo> interfaceInfoVOPage = doSearch(searchText,current,pageSize);
                    searchVO.setInterfaceInfoList(interfaceInfoVOPage.getRecords());
                    break;
                case PICTURE:
                    Page<Picture> picturePage= doSearchPicture(searchText,1,10);
                    searchVO.setPictureList(picturePage.getRecords());
                    break;
            }
            return searchVO;
        }
    }

    public Page<InterfaceInfo> doSearch(String searchText,int pageNum,int pageSize){
        InterfaceInfoQuery interfaceInfoQuery = new InterfaceInfoQuery();
        interfaceInfoQuery.setSearchText(searchText);
        interfaceInfoQuery.setCurrent(pageNum);
        interfaceInfoQuery.setPageSize(pageSize);
        Page<InterfaceInfo> page = interfaceInfoService.searchFromEs(interfaceInfoQuery);
        return page;

    }

    public Page<Picture> doSearchPicture(String searchText, int pageNum, int pageSize){
        int current = (pageNum - 1) * pageSize;
        String url = String.format("https://cn.bing.com/images/search?q=%s&first=%s", searchText, current);
        Document document = null;
        try{
            document = Jsoup.connect(url).get();
        }catch (IOException e){
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "数据获取异常");
        }
        Elements elements = document.select(".iuscp.isv");
        List<Picture> pictures = new ArrayList<>();
        for (Element element : elements) {
            // 取图片地址（murl）
            String m = element.select(".iusc").get(0).attr("m");
            Map<String, Object> map = JSONUtil.toBean(m, Map.class);
            String murl = (String) map.get("murl");
//            System.out.println(murl);
            // 取标题
            String title = element.select(".inflnk").get(0).attr("aria-label");
//            System.out.println(title);
            Picture picture = new Picture();
            picture.setTitle(title);
            picture.setUrl(murl);
            pictures.add(picture);
            if (pictures.size() >= pageSize) {
                break;
            }
        }
        Page<Picture> picturePage = new Page<>(pageNum, pageSize);
        picturePage.setRecords(pictures);
        return picturePage;
    }
}
