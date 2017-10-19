package com.shaoming.core.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.collections4.list.TreeList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

/**
 * Created by Jerry on 2017/6/23.
 */
@Configuration
@EnableCaching
public class RedisConfig extends CachingConfigurerSupport{
    private final static Logger logger = LoggerFactory.getLogger(RedisConfig.class);

    @Bean
    public KeyGenerator tomcartKeyGenerator(){
        return new KeyGenerator() {
            @Override
            public Object generate(Object target, Method method, Object... params) {
            StringBuilder sb = new StringBuilder();
            sb.append(target.getClass().getName());
            sb.append(method.getName());
            for (Object obj : params) {
                if(obj == null) continue;

                if (isBasicType(obj)) {
                    sb.append(obj.toString());
                } else if(obj instanceof List || obj instanceof ArrayList || obj instanceof TreeList){
                    List list = (List) obj;
                    for (Object o : list){
                        if (isBasicType(o)) {
                            sb.append(o.toString());
                        }else{
                            sb.append(getParams(o));
                        }
                    }
                } else if(obj instanceof Set || obj instanceof HashSet){
                    Set set = (Set) obj;
                    for (Object o : set){
                        if (isBasicType(o)) {
                            sb.append(o.toString());
                        }else{
                            sb.append(getParams(o));
                        }
                    }
                } else if (obj.getClass().isArray()) {
                    Object[] objArray = (Object[]) obj;
                    for (Object o : objArray) {
                        if (isBasicType(o)) {
                            sb.append(o.toString());
                        }else{
                            sb.append(getParams(o));
                        }
                    }
                } else {
                    sb.append(getParams(obj));
                }
            }
            return sb.toString();
            }
        };
    }

    //判断是否为基本类型
    private boolean isBasicType(Object obj){
        boolean result = false;
        if (obj instanceof Integer || obj instanceof String || obj instanceof Double || obj.getClass().isEnum() ||
                obj instanceof Float || obj instanceof Long || obj instanceof Boolean || obj instanceof Date) {
            result = true;
        }
        return result;
    }

    @Bean
    public CacheManager cacheManager(RedisTemplate redisTemplate) {
        RedisCacheManager cacheManager = new RedisCacheManager(redisTemplate);
        //cacheManager.setDefaultExpiration(10); //设置key-value超时时间
        Map<String, Long> expireMap = new HashMap<String, Long>();
        expireMap.put("showIndexCategoryGoods", 60 * 60L); //单位：秒，缓存一个小时60*60
        cacheManager.setExpires(expireMap);
        return cacheManager;

        //return new RedisCacheManager(redisTemplate);
    }

    @Bean
    public RedisTemplate<String, String> redisTemplate(RedisConnectionFactory factory) {
        StringRedisTemplate template = new StringRedisTemplate(factory);
        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(om);
        template.setValueSerializer(jackson2JsonRedisSerializer);
        template.afterPropertiesSet();
        return template;
    }

    /**
     * 拼接参数
     */
    public static String getParams(Object obj) {
        StringBuilder sb = new StringBuilder();

        // 获取实体类的所有属性，返回Field数组
        Field[] field = obj.getClass().getDeclaredFields();
        //获取实体类名称
        String simpleName = obj.getClass().getSimpleName();
        //将实体类名称首字符小写
        String className = simpleName.substring(0, 1).toLowerCase() + simpleName.substring(1);

        // 遍历所有属性
        for (int j = 0; j < field.length; j++) {
            try {
                // 获取属性的名字
                String fieldName = field[j].getName();
                // 将属性的首字符大写，方便构造get，set方法
                String fieldNameStr = fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
                // 获取属性的类型
                String fieldType = field[j].getType().getSimpleName();
                Method m = obj.getClass().getMethod("get" + fieldNameStr);
                // 调用getter方法获取属性值
                Object value =  m.invoke(obj);

                if(value == null) continue;

                if ("String".equals(fieldType)){
                    if(!"".equals(value)) sb.append(fieldName+value);
                }else if ("Date".equals(fieldType)){
                    sb.append(fieldName+value.toString());
                }else if ("Integer".equals(fieldType) || "int".equals(fieldType)){
                    sb.append(fieldName+value.toString());
                }else if ("Long".equalsIgnoreCase(fieldType) || "long".equals(fieldType)) {
                    sb.append(fieldName+value.toString());
                }else if ("Double".equalsIgnoreCase(fieldType) || "double".equals(fieldType)){
                    sb.append(fieldName+value.toString());
                }else if ("Float".equalsIgnoreCase(fieldType) || "float".equals(fieldType)){
                    sb.append(fieldName+value.toString());
                }else if ("Boolean".equalsIgnoreCase(fieldType) || "boolean".equals(fieldType)){
                    sb.append(fieldName+value.toString());
                } else {
                    sb.append(getParams(value));
                }
            } catch (Exception exception) {
                //属性没有getField方法，不处理报错问题，直接检查下一个属性
                continue;
            }
        }

        return sb.toString();
    }
}
