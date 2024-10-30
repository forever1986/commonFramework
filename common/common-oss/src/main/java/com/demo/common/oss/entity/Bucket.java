package com.demo.common.oss.entity;

import lombok.Data;

@Data
public class Bucket {
    private String name;
    private Long createDate;


    public static Bucket convertBucket(io.minio.messages.Bucket bucket){
        Bucket newBucket = new Bucket();
        if(bucket==null){
            return newBucket;
        }
        newBucket.setName(bucket.name());
        if(bucket.creationDate()!=null){
            newBucket.setCreateDate(bucket.creationDate().toInstant().toEpochMilli());
        }
        return newBucket;
    }
}
