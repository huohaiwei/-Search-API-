package com.yellow.usermanager.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.yellow.usermanager.common.BaseResponse;
import com.yellow.usermanager.common.ResultUtils;
import com.yellow.usermanager.constant.CommonConstant;
import com.yellow.usermanager.exception.BusinessException;
import com.yellow.usermanager.exception.ThrowUtils;
import com.yellow.usermanager.mapper.InterfaceInfoMapper;
import com.yellow.usermanager.model.dto.interfaceinfo.InterfaceInfoAddRequest;
import com.yellow.usermanager.model.dto.interfaceinfo.InterfaceInfoEsDTO;
import com.yellow.usermanager.model.dto.interfaceinfo.InterfaceInfoQueryRequest;
import com.yellow.usermanager.model.dto.search.InterfaceInfoQuery;
import com.yellow.usermanager.model.dto.search.SearchQuery;
import com.yellow.usermanager.service.UserInterfaceInfoService;
import com.yellow.yellowapicommon.model.entity.InterfaceInfo;
import com.yellow.usermanager.model.enums.ErrorCode;
import com.yellow.usermanager.model.vo.InterfaceInfoVO;

import com.yellow.usermanager.service.InterfaceInfoService;
import com.yellow.usermanager.utils.SqlUtils;
import com.yellow.yellowapicommon.model.entity.User;
import io.swagger.models.auth.In;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import javax.annotation.Resource;


/**
 * @author 陈翰垒
 * @description 针对表【interface_info(接口信息表)】的数据库操作Service实现
 * @createDate 2024-01-03 22:18:58
 */
@Service
@Slf4j
public class InterfaceInfoServiceImpl extends ServiceImpl<InterfaceInfoMapper, InterfaceInfo>
        implements InterfaceInfoService {
    @Resource
    private UserInterfaceInfoService userInterfaceInfoService;

    @Resource
    private ElasticsearchRestTemplate elasticsearchRestTemplate;

    @Override
    public void validInterfaceInfo(InterfaceInfo interfaceInfo, boolean add) {
        if (interfaceInfo == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String name = interfaceInfo.getName();
        String description = interfaceInfo.getDescription();
        String url = interfaceInfo.getUrl();
        String method = interfaceInfo.getMethod();

        // 创建时，参数不能为空
        if (add) {
            ThrowUtils.throwIf(StringUtils.isAnyBlank(name, url, method), ErrorCode.PARAMS_ERROR);
        }
        // 有参数则校验
        if (StringUtils.isNotBlank(name) && name.length() > 25) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "接口名称过长");
        }
        if (StringUtils.isNotBlank(description) && description.length() > 8192) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "接口描述过长");
        }
        if (StringUtils.isNotBlank(url) && url.length() > 8192) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "url过长");
        }
    }

    @Override
    public QueryWrapper<InterfaceInfo> getQueryWrapper(InterfaceInfoQueryRequest interfaceInfoQueryRequest) {
        QueryWrapper<InterfaceInfo> queryWrapper = new QueryWrapper<>();
        if (interfaceInfoQueryRequest == null) {
            return queryWrapper;
        }
        Long id = interfaceInfoQueryRequest.getId();
        String name = interfaceInfoQueryRequest.getName();
        String description = interfaceInfoQueryRequest.getDescription();
        String url = interfaceInfoQueryRequest.getUrl();
        String requestHeader = interfaceInfoQueryRequest.getRequestHeader();
        String responseHeader = interfaceInfoQueryRequest.getResponseHeader();
        Integer status = interfaceInfoQueryRequest.getStatus();
        String method = interfaceInfoQueryRequest.getMethod();
        Long userId = interfaceInfoQueryRequest.getUserId();
        String sortField = interfaceInfoQueryRequest.getSortField();
        String sortOrder = interfaceInfoQueryRequest.getSortOrder();

        // 拼接查询条件
        queryWrapper.like(StringUtils.isNotBlank(name), "name", name);
        queryWrapper.like(StringUtils.isNotBlank(description), "description", description);
        queryWrapper.eq(StringUtils.isNotBlank(url), "url", url);
        queryWrapper.eq(StringUtils.isNotBlank(method), "method", method);
        queryWrapper.eq(ObjectUtils.isNotEmpty(id), "id", id);
        queryWrapper.eq(ObjectUtils.isNotEmpty(userId), "userId", userId);
        queryWrapper.eq(ObjectUtils.isNotEmpty(status), "status", status);
        queryWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                sortField);
        return queryWrapper;
    }

    @Override
    public InterfaceInfoVO getInterfaceInfoVO(InterfaceInfo interfaceInfo) {
        if (interfaceInfo == null) {
            return null;
        }
        InterfaceInfoVO interfaceInfoVO = new InterfaceInfoVO();
        BeanUtils.copyProperties(interfaceInfo, interfaceInfoVO);
        return interfaceInfoVO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addInterfaceInfo(InterfaceInfo interfaceInfo, User adduser) {
        ThrowUtils.throwIf(interfaceInfo == null || adduser == null, ErrorCode.PARAMS_ERROR);
        boolean res = this.save(interfaceInfo);
        return res && userInterfaceInfoService.addDefaultInvokeCount(interfaceInfo.getId(), adduser.getId());

    }

    @Override
    public Page<InterfaceInfoVO> searchInterfaceInfoVOByPage(InterfaceInfoQueryRequest interfaceInfoQueryRequest) {
        long current = interfaceInfoQueryRequest.getCurrent();
        long pageSize = interfaceInfoQueryRequest.getPageSize();
        Page<InterfaceInfo> interfaceInfo = this.page(new Page<>(current,pageSize),
                this.getQueryWrapper(interfaceInfoQueryRequest));
        Page<InterfaceInfoVO> interfaceInfoVOPage = new Page<>(interfaceInfo.getCurrent(), interfaceInfo.getSize(),interfaceInfo.getTotal());
        List<InterfaceInfoVO> interfaceInfoVOList =  interfaceInfo.getRecords().stream().map(this::getInterfaceInfoVO).collect(Collectors.toList());
        interfaceInfoVOPage.setRecords(interfaceInfoVOList);
        return interfaceInfoVOPage;
    }

    @Override
    public Page<InterfaceInfo> searchFromEs(InterfaceInfoQuery searchQuery) {
        String searchText = searchQuery.getSearchText();
        String name = searchQuery.getName();
        Integer isDelete = searchQuery.getIsDelete();
        String description = searchQuery.getDescription();
        Long createId = searchQuery.getUserId();

        //es的起始页为0
        long current = searchQuery.getCurrent() - 1;
        long pageSize = searchQuery.getPageSize();
        //排序字段
        String sortField = searchQuery.getSortField();
        String sortOrder = searchQuery.getSortOrder();
        //构建boolquery
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        //filter 没有被删除和精确查询xx创建
        boolQueryBuilder.filter(QueryBuilders.termQuery("isDelete",0));
        if (createId!=null){
            boolQueryBuilder.filter(QueryBuilders.termQuery("userId",createId));
        }
        //should 关键词搜索
        if (StringUtils.isNotBlank(searchText)) {
            boolQueryBuilder.should(QueryBuilders.matchQuery("description", searchText));
            boolQueryBuilder.should(QueryBuilders.matchQuery("name", searchText));
            boolQueryBuilder.minimumShouldMatch(1);
        }
        //match 按接口名称检索
        if(StringUtils.isNotBlank(name)){
            boolQueryBuilder.should(QueryBuilders.matchQuery("name",name));
            boolQueryBuilder.minimumShouldMatch(1);
        }
        //match 按接口名称检索
        if(StringUtils.isNotBlank(description)){
            boolQueryBuilder.should(QueryBuilders.matchQuery("description",description));
            boolQueryBuilder.minimumShouldMatch(1);
        }
        //排序,默认按照分数排序
        SortBuilder<?> sortBuilder = SortBuilders.scoreSort();
        if (StringUtils.isNotBlank(sortField)){
            sortBuilder = SortBuilders.fieldSort(sortField);
            sortBuilder.order(CommonConstant.SORT_ORDER_ASC.equals(sortOrder)? SortOrder.ASC:SortOrder.DESC);
        }
        //分页
        PageRequest pageRequest = PageRequest.of((int) current, (int) pageSize);
        //构造查询
        NativeSearchQuery query = new NativeSearchQueryBuilder().withQuery(boolQueryBuilder)
                .withPageable(pageRequest).withSorts(sortBuilder).build();
        SearchHits<InterfaceInfoEsDTO> searchHits = elasticsearchRestTemplate.search(query,InterfaceInfoEsDTO.class);
        Page<InterfaceInfo> page = new Page<>();
        page.setTotal(searchHits.getTotalHits());
        List<InterfaceInfo> resourceList = new ArrayList<>();
        // 查出结果后，从 db 获取最新动态数据
        if (searchHits.hasSearchHits()) {
            List<SearchHit<InterfaceInfoEsDTO>> searchHitList = searchHits.getSearchHits();
            List<Long> interfaceInfoIds = searchHitList.stream().map(searchHit -> searchHit.getContent().getId())
                    .collect(Collectors.toList());
            // 从数据库中取出更完整的数据
            List<InterfaceInfo> interfaceInfoList = baseMapper.selectBatchIds(interfaceInfoIds);
            if (interfaceInfoList != null) {
                Map<Long, List<InterfaceInfo>> idInterfaceMap = interfaceInfoList.stream().collect(Collectors.groupingBy(InterfaceInfo::getId));
                interfaceInfoIds.forEach(postId -> {
                    if (idInterfaceMap.containsKey(postId)) {
                        resourceList.add(idInterfaceMap.get(postId).get(0));
                    } else {
                        // 从 es 清空 db 已物理删除的数据
                        String delete = elasticsearchRestTemplate.delete(String.valueOf(postId), InterfaceInfo.class);
                        log.info("delete post {}", delete);
                    }
                });
            }
        }
        page.setRecords(resourceList);
        return page;
    }

}




