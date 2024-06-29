package com.yellow.usermanager.job;

import com.yellow.usermanager.esdao.InterfaceInfoEsDao;
import com.yellow.usermanager.mapper.InterfaceInfoMapper;
import com.yellow.usermanager.model.dto.interfaceinfo.InterfaceInfoEsDTO;
import com.yellow.yellowapicommon.model.entity.InterfaceInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 增量同步数据到ES(每 分钟去找近五分钟修改或添加的数据)
 *
 * @author 陈翰垒
 */
//@Component
@Slf4j
public class IncSyncInterfaceToEs {
    @Resource
    private InterfaceInfoMapper interfaceInfoMapper;

    @Resource
    private InterfaceInfoEsDao interfaceInfoEsDao;

    /**
     * 定时任务，每分钟执行一次
     */
    @Scheduled(fixedRate = 60 * 1000)
    public void run() {
        //查询最近5分钟的数据
        Date fiveMinutesAgoDate = new Date(System.currentTimeMillis() - 5 * 60 * 1000L);
        List<InterfaceInfo> interfaceInfoList = interfaceInfoMapper.listInterfaceInfoWithDelete(fiveMinutesAgoDate);
        if (CollectionUtils.isEmpty(interfaceInfoList)) {
            log.info("no inc InterfaceInfo");
            return;
        }
        List<InterfaceInfoEsDTO> interfaceInfoEsDTOList = interfaceInfoList.stream()
                .map(InterfaceInfoEsDTO::objToDto)
                .collect(Collectors.toList());
        final int pageSize = 500;
        int total =interfaceInfoList.size();
        log.info("IncSyncInterfaceToEs start, total {}", total);
        for (int i =0;i<total;i+=pageSize){
            int min = Math.min(total, i+pageSize);
            interfaceInfoEsDao.saveAll(interfaceInfoEsDTOList.subList(i,min));
        }
        log.info("IncSyncPostToEs end, total {}", total);
    }

}
