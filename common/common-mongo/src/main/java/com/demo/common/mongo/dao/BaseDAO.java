package com.demo.common.mongo.dao;

import com.demo.common.mongo.entity.Base;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.util.List;
import java.util.Map;

public abstract class BaseDAO<T> {

    protected String ID = "id";

    protected String DEL_FLAG = "del_flag";

    protected Class<T> clazz;


    public abstract MongoTemplate getMongoTemplate();

    public <T> T findById(Long id){
        Query query = new Query(Criteria.where(ID).is(id).and(DEL_FLAG).is(false));
        List<T> list = (List<T>) getMongoTemplate().find(query, clazz);
        if(list!=null&&list.size()>0){
            if(list.size()>1)
                throw new IllegalArgumentException("id="+id+" is duplicate!");
            return list.get(0);
        }
        return null;
    }

    public T insertDocument(T base){
        if(((Base)base).getID()==null)
            throw new IllegalArgumentException("id is null!");
        ((Base) base).setDel_flag(false);
        return getMongoTemplate().insert(base);
    }

    public T updateDocument(Map<String,Object> updateMap){
        if(updateMap.get(ID)==null)
            throw new IllegalArgumentException("id is null!");
        Query query = new Query(Criteria.where(ID).is(updateMap.get(ID)).and(DEL_FLAG).is(false));
        updateMap.remove(ID);
        Update update = new Update();
        updateMap.keySet().stream().forEach(
                key -> {
                    update.set(key,updateMap.get(key));
                }
        );
        getMongoTemplate().findAndModify(query, update, clazz);
        List<T> list = getMongoTemplate().find(query, clazz);
        return list.get(0);
    }

    public T deleteDocument(Long id){
        if(id==null)
            throw new IllegalArgumentException("id is null!");
        Query query = new Query(Criteria.where(ID).is(id).and(DEL_FLAG).is(false));
        Update update = new Update().set("del_flag", true);
        getMongoTemplate().findAndModify(query, update, clazz);
        List<T> list = getMongoTemplate().find(query, clazz);
        return list.get(0);
    }

    public List<Map> findDocument(List<String> fieldList, Criteria criteria){
        Query query = new Query(criteria.and(DEL_FLAG).is(false));
        for(String fieldName : fieldList){
            query.fields().include(fieldName);
        }
        query.fields().include(ID);
        query.fields().include(DEL_FLAG);
        Document documentAnnotation = clazz.getAnnotation(Document.class);
        return getMongoTemplate().find(query,Map.class,documentAnnotation.collection());
    }

}
