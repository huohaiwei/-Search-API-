package com.yellow.usermanager.controller;

import cn.hutool.http.server.HttpServerResponse;
import com.aliyun.oss.OSS;
import com.aliyun.oss.internal.OSSObjectOperation;
import com.aliyun.oss.internal.OSSUtils;
import com.aliyun.oss.model.OSSObject;
import com.yellow.usermanager.common.BaseResponse;
import com.yellow.usermanager.common.ResultUtils;
import com.yellow.usermanager.config.OssClientConfig;
import com.yellow.usermanager.exception.BusinessException;
import com.yellow.usermanager.model.enums.ErrorCode;
import com.yellow.usermanager.utils.OssUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * 资源管理控制层
 * @author 陈翰垒
 */
@Slf4j
@RestController
@RequestMapping("/resource")
public class ResourceController {

    @Resource
    OssUtils ossUtils;

    @PostMapping("/upload")
    public BaseResponse<String> uploadFile(@RequestBody MultipartFile multipartFile){
        // 填写Object完整路径，例如exampledir/exampleobject.txt。Object完整路径中不能包含Bucket名称。
        String objectName = multipartFile.getOriginalFilename();
        // 去除后缀名
        // if(StringUtils.isNotBlank(objectName)){
        //    objectName = objectName.substring(0, objectName.lastIndexOf('.'));
        // }
        String filePath = String.format("test/%s",objectName);
        File file = null;
        try {
            //临时空文件
            file = File.createTempFile(filePath,null);
            multipartFile.transferTo(file);
            ossUtils.putObject(filePath,file);
            // 返回可访问地址
            return ResultUtils.success(filePath);
        } catch (Exception oe) {
            log.error("file upload error, filepath = " + filePath, oe);
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "上传失败");
        }
         finally {
            //删除临时文件
            if(file!=null){
                boolean delete = file.delete();
                if (!delete){
                    log.error("file delete error, filepath = {}", filePath);
                }
            }
//            ossUtils.shutdownOssClient();
        }
    }

    @GetMapping("/download")
    public void downloadFile(String fileName, HttpServletResponse httpServerResponse) throws IOException {
        OSSObject object = ossUtils.getObject(fileName);
        try {
            //OSS对象的内容
            InputStream objectContent = object.getObjectContent();
            byte[] bytes = IOUtils.toByteArray(objectContent);
            //设置响应头
            //指定返回内容类型为二进制，浏览器会开启下载
            httpServerResponse.setContentType("application/octet-stream;charset=UTF-8");
            //指定内容为附件，指定下载的名字
            httpServerResponse.setHeader("Content-Disposition", "attachment; filename="
                    + new String(fileName.getBytes("UTF-8"), "ISO8859-1"));
            // 写入响应
            httpServerResponse.getOutputStream().write(bytes);
            httpServerResponse.getOutputStream().flush();
        }catch (Exception oe){
            log.error("file download error, fileName = " + fileName, oe);
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "下载失败");
        }finally {
            if (object!=null) {
                object.close();
            }
//            ossUtils.shutdownOssClient();
        }
    }
}
