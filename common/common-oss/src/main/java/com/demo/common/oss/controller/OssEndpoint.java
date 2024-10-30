package com.demo.common.oss.controller;

import com.demo.common.core.result.Result;
import com.demo.common.exception.BizException;
import com.demo.common.oss.entity.Bucket;
import com.demo.common.oss.entity.ObjectInfo;
import com.demo.common.oss.service.OssTemplate;
import io.minio.messages.Item;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping({"/oss"})
@Slf4j
public class OssEndpoint {

    private OssTemplate ossTemplate;

    public OssEndpoint(OssTemplate ossTemplate) {
        this.ossTemplate = ossTemplate;
    }

    @PostMapping({"/bucket/{bucketName}"})
    public Result<Bucket> createBucket(@PathVariable String bucketName) throws Exception {
        this.ossTemplate.createBucket(bucketName);
        return Result.success(Bucket.convertBucket(this.ossTemplate.getBucket(bucketName).get()));
    }

    @GetMapping({"/bucket"})
    @ResponseBody
    public Result<List<Bucket>> getBuckets() {
        try {
            return Result.success(this.ossTemplate.getAllBuckets().stream().map(Bucket::convertBucket).collect(Collectors.toList()));
        } catch (Exception exception) {
            log.error("获取桶失败:", exception.getMessage());
            throw new BizException("获取桶失败", exception);
        }

    }

    @GetMapping({"/bucket/{bucketName}"})
    public Result<Bucket> getBucket(@PathVariable String bucketName) {
        try {
            return Result.success(Bucket.convertBucket(this.ossTemplate.getBucket(bucketName).get()));
        } catch (Exception exception) {
            log.error("创建桶失败:", exception.getMessage());
            throw new BizException("创建桶失败", exception);
        }

    }

    @DeleteMapping({"/bucket/{bucketName}"})
    @ResponseStatus(HttpStatus.ACCEPTED)
    public Result<String> deleteBucket(@PathVariable String bucketName) {
        try {
            this.ossTemplate.removeBucket(bucketName);
            return Result.success();
        } catch (Exception exception) {
            log.error("删除桶失败:", exception.getMessage());
            throw new BizException("删除桶失败", exception);
        }
    }

    @PostMapping({"/object/{bucketName}"})
    public Result<ObjectInfo> createObject(@RequestBody MultipartFile object, @PathVariable String bucketName) {
        try {
            String name = object.getOriginalFilename();
            this.ossTemplate.putObject(bucketName, name, object.getInputStream(), object.getSize(), object.getContentType());
            return Result.success(this.ossTemplate.getObjectInfo(bucketName, name));
        } catch (Exception exception) {
            log.error("创建对象失败:", exception.getMessage());
            throw new BizException("创建对象失败", exception);
        }
    }

    @PostMapping({"/object/{bucketName}/{objectName}"})
    public Result<ObjectInfo> createObject(@RequestBody MultipartFile object, @PathVariable String bucketName, @PathVariable String objectName) {
        try {
            this.ossTemplate.putObject(bucketName, objectName, object.getInputStream(), object.getSize(), object.getContentType());
            return Result.success(this.ossTemplate.getObjectInfo(bucketName, objectName));
        } catch (Exception exception) {
            log.error("创建对象失败:", exception.getMessage());
            throw new BizException("创建对象失败", exception);
        }
    }

    @GetMapping({"/object/{bucketName}/{objectName}"})
    public Result<List<Item>> filterObject(@PathVariable String bucketName, @PathVariable String objectName) {
        try {
            return Result.success(this.ossTemplate.getAllObjectsByPrefix(bucketName, objectName, true));
        } catch (Exception exception) {
            log.error("获取对象失败:", exception.getMessage());
            throw new BizException("获取对象失败", exception);
        }
    }

    @GetMapping({"/object/{bucketName}/{objectName}/{expires}"})
    public Result<Map<String, Object>> getObject(@PathVariable String bucketName, @PathVariable String objectName, @PathVariable Integer expires) {
        try {
            Map<String, Object> responseBody = new HashMap(8);
            responseBody.put("bucket", bucketName);
            responseBody.put("object", objectName);
            responseBody.put("url", this.ossTemplate.getObjectURL(bucketName, objectName, expires));
            responseBody.put("expires", expires);
            return Result.success(responseBody);
        } catch (Exception exception) {
            log.error("获取对象URL失败:", exception.getMessage());
            throw new BizException("获取对象URL失败", exception);
        }
    }

    @ResponseStatus(HttpStatus.ACCEPTED)
    @DeleteMapping({"/object/{bucketName}/{objectName}/"})
    public void deleteObject(@PathVariable String bucketName, @PathVariable String objectName) {
        try {
            this.ossTemplate.removeObject(bucketName, objectName);
        } catch (Exception var4) {
            this.log.error("删除对象失败:", var4.getMessage());
            throw new RuntimeException("删除对象失败");
        }
    }

}
