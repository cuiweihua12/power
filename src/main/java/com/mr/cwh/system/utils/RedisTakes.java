package com.mr.cwh.system.utils;

import java.util.List;
import java.util.zip.ZipEntry;

/**
 * @program: power
 * @description: Redis操作基本接口
 * @author: cuiweihua
 * @create: 2020-06-17 09:29
 */
public interface RedisTakes<H,K,V> {

    /**
    *@Description: //增加
    *@Param: 
    *@return: 
    *@Author: cuiweihua
    *@date: 2020/6/17
    */
    public void add(K key,V value);
    public void addObj(H objectKey,K key,V object);

    /**
    *@Description: 删除
    *@Param: 
    *@return: 
    *@Author: cuiweihua
    *@date: 2020/6/17
    */
    public void delete(K key);
    public void delete(List<K> listKeys);
    public void deletObj(H objecyKey,K key);
    
    /**
    *@Description: 修改
    *@Param: 
    *@return: 
    *@Author: cuiweihua
    *@date: 2020/6/17
    */
    public void update(K key,String value);
    public void updateObj(H objectKey,K key,V object);
    
    /**
    *@Description: 查询
    *@Param: 
    *@return: 
    *@Author: cuiweihua
    *@date: 2020/6/17
    */
    public String get(K key);
    public V getObj(H objectKey,K key);
}
