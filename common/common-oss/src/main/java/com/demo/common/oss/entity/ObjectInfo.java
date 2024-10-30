package com.demo.common.oss.entity;

import io.minio.GetObjectResponse;
import lombok.Data;
import okhttp3.Headers;

@Data
public class ObjectInfo {

    private Headers headers;
    private String bucket;
    private String region;
    private String object;

    public static ObjectInfo convertObject(GetObjectResponse response){
        ObjectInfo objectInfo = new ObjectInfo();
        if(response==null){
            return objectInfo;
        }
        objectInfo.setBucket(response.bucket());
        objectInfo.setObject(response.object());
        objectInfo.setRegion(response.region());
        objectInfo.setHeaders(response.headers());
        return objectInfo;
    }
}
