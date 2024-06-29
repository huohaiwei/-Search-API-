package com.yellow.usermanager.esdao;

import com.yellow.usermanager.model.dto.interfaceinfo.InterfaceInfoEsDTO;
import com.yellow.yellowapicommon.model.entity.InterfaceInfo;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;


public interface InterfaceInfoEsDao extends ElasticsearchRepository<InterfaceInfoEsDTO, Long> {
    List<InterfaceInfoEsDTO> findByName(String interfaceName);
}
