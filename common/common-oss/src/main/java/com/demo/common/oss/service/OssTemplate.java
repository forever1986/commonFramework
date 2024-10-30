package com.demo.common.oss.service;

import com.demo.common.oss.config.OssProperties;
import com.demo.common.oss.entity.ObjectInfo;
import io.minio.*;
import io.minio.http.Method;
import io.minio.messages.Bucket;
import io.minio.messages.Item;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

public class OssTemplate {

    private OssProperties ossProperties;

    private MinioClient minioClient;

    public OssTemplate(OssProperties ossProperties, MinioClient minioClient) {
        this.ossProperties = ossProperties;
        this.minioClient = minioClient;
    }

    public void createBucket(String bucketName) throws Exception{
        if (!this.minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build())) {
            this.minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
        }
    }

    public Optional<Bucket> getBucket(String bucketName) throws Exception{
        return this.minioClient.listBuckets().stream().filter((bucket) -> {
            return bucket.name().equals(bucketName);
        }).findFirst();
    }

    public List<Bucket> getAllBuckets() throws Exception{
        return this.minioClient.listBuckets();
    }

    public void removeBucket(String bucketName) throws Exception{
        this.minioClient.removeBucket(RemoveBucketArgs.builder().bucket(bucketName).build());
    }

    public void putObject(String bucketName, String objectName, InputStream stream) throws Exception {
        this.putObject(bucketName, objectName, stream, (long)stream.available(), "application/octet-stream");
    }

    public void putObject(String bucketName, String objectName, InputStream stream, long size, String contextType) throws Exception {
        ObjectWriteResponse response = this.minioClient.putObject(PutObjectArgs.builder().bucket(bucketName).object(objectName).stream(stream, size, -1).contentType(contextType).build());
        System.out.println(response.headers());
    }

    public ObjectInfo getObjectInfo(String bucketName, String objectName) throws Exception {
        GetObjectResponse response = this.minioClient.getObject(GetObjectArgs.builder().bucket(bucketName).object(objectName).build());
        return ObjectInfo.convertObject(response);
    }

    public List<Item> getAllObjectsByPrefix(String bucketName, String prefix, boolean recursive) throws Exception {
        Iterable<Result<Item>> it = this.minioClient.listObjects(ListObjectsArgs.builder().bucket(bucketName).prefix(prefix).recursive(recursive).build());
        List<Item> list = new ArrayList<>();
        if(it!=null){
            Iterator<Result<Item>> iterator = it.iterator();
            while (iterator.hasNext()){
                Result<Item> item = iterator.next();
                list.add(item.get());
            }
        }
        return list;
    }

    public String getObjectURL(String bucketName, String objectName, Integer expires) throws Exception {
        return this.minioClient.getPresignedObjectUrl(GetPresignedObjectUrlArgs.builder().bucket(bucketName).object(objectName).expiry(expires, TimeUnit.SECONDS).method(Method.GET).build());
    }

    public void removeObject(String bucketName, String objectName) throws Exception {
        this.minioClient.removeObject(RemoveObjectArgs.builder().bucket(bucketName).object(objectName).build());
    }

}
