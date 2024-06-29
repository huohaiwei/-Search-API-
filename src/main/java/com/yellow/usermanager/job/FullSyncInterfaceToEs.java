package com.yellow.usermanager.job;



import com.yellow.usermanager.esdao.InterfaceInfoEsDao;
import com.yellow.usermanager.mapper.InterfaceInfoMapper;
import com.yellow.usermanager.model.dto.interfaceinfo.InterfaceInfoEsDTO;
import com.yellow.usermanager.service.InterfaceInfoService;
import com.yellow.yellowapicommon.model.entity.InterfaceInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.boot.CommandLineRunner;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 全量同步数据到ES
 *
 * @author 陈翰垒
 */
//@Component
@Slf4j
public class FullSyncInterfaceToEs implements CommandLineRunner {
    @Resource
    private InterfaceInfoService interfaceInfoService;

    @Resource
    private InterfaceInfoEsDao interfaceInfoEsDao;

    @Override
    public void run(String... args) {

        List<InterfaceInfo> interfaceInfoList = interfaceInfoService.list();
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
