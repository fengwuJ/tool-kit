package com.fengwuj.testdemo.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

@Slf4j
public class JsonUtil {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    //字符串转Class
    public static Class<?> convertToClass(String fullClassName) {
        Class<?> clazz = null;
        try {
            clazz = Class.forName(fullClassName);
        } catch (ClassNotFoundException e) {
            log.error("找不到类:{}", fullClassName, e);
            throw new RuntimeException(e);
        }
        return clazz;
    }

    //类数据转换
    public static <T> T convertToObject(Object data, Class<T> clazz) {
        T t = null;
        try {
            t = clazz.newInstance();
        } catch (InstantiationException e) {
            log.error("实例化类异常");
        } catch (IllegalAccessException e) {
            log.error("访问权限异常");
        }

        if (Objects.isNull(data)) {
            return t;
        }

        String s = null;
        String name = clazz.getSimpleName();
        JSONArray jsonArray = JSON.parseArray(JSON.toJSONString(data));
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            if (jsonObject.containsKey(name)) {
                s = jsonObject.getString(name);
                break;
            }
        }

        if (null == s || s.length() == 0) {
            return t;
        }

        t = JSON.parseObject(s, clazz);
        return t;
    }

    //List数据转换
    public static <T> List<T> convertToList(Object data, Class<T> clazz) {
        List<T> resultList = new ArrayList<>();
        if (Objects.isNull(data)) {
            return resultList;
        }
        String name = clazz.getSimpleName();
        String s = null;
        JSONArray jsonArray = JSON.parseArray(JSON.toJSONString(data));
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            if (jsonObject.containsKey(name)) {
                s = jsonObject.getString(name);
                break;
            }
        }

        if (s == null || s.length() == 0) {
            return resultList;
        }

        JSONArray dataArray = JSON.parseArray(s);
        for (int i = 0; i < dataArray.size(); i++) {
            JSONObject o = dataArray.getJSONObject(i);
            resultList.add(JSON.parseObject(o.toJSONString(), clazz));
        }

        return resultList;
    }

    public static String writeAsJsonString(Object data){
        if (Objects.isNull(data)){
            return null;
        }
        String s = null;
        try {
            s = objectMapper.writeValueAsString(data);
        } catch (JsonProcessingException e) {
            log.error("无法序列化为json数据");
            throw new RuntimeException(e);
        }
        return s;
    }

    public static <T> T readJsonStringAsObject(String origin, Class<T> clazz) {
        T t = null;
        if (null == origin) {
            return t;
        }
        try {
            t = objectMapper.readValue(origin, clazz);
        } catch (JsonProcessingException e) {
            log.error("json反序列化失败");
            throw new RuntimeException(e);
        }
        return t;
    }

    public static <T> T readJsonStringAsObject(String origin, TypeReference<T> typeReference) throws IOException {
        T t = null;
        if (null == origin) {
            return t;
        }
        try {
            t = objectMapper.readValue(origin, typeReference);
        } catch (JsonProcessingException e) {
            log.error("json反序列化失败");
            throw new RuntimeException(e);
        }
        return t;
    }

    //JSONArray转字段Map
    public static Map<String, JSONObject> jsonArrayToMap(JSONArray srcArray, String jsonKey) {
        Map<String, JSONObject> map = new HashMap<>();
        for (int i = 0; i < srcArray.size(); i++) {
            JSONObject jsonObject = srcArray.getJSONObject(i);
            map.put(jsonObject.getString(jsonKey), jsonObject);
        }
        return map;
    }

    public static String inputStreamToString(InputStream is){

        StringBuilder sb = new StringBuilder();
        String line;

        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        while (true) {
            try {
                if ((line = br.readLine()) == null) break;
            } catch (IOException e) {
                log.error("读取流文件异常");
                throw new RuntimeException(e);
            }
            sb.append(line);
        }
        return sb.toString();
    }

    public static JSONObject parseJsonFile(String filePath) {
        String jsonStr = null;
        try(InputStream is = FileFillConfigUtil.class.getClassLoader().getResourceAsStream(filePath)){
            jsonStr = JsonUtil.inputStreamToString(is);
        } catch (Exception e) {
            log.error("读取流数据失败", e);
            throw new RuntimeException(e);
        }
        if (null == jsonStr || jsonStr.length() == 0){
            return new JSONObject();
        }
        return JSON.parseObject(jsonStr);
    }
}
