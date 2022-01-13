package com.fengwuj.testdemo.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fengwuj.testdemo.excel.support.ExcelDef;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.util.IOUtils;

import java.io.*;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j
public class FileFillConfigUtil {

    private static Lock lock = new ReentrantLock();

    private static volatile ConcurrentHashMap<String, Map<String, JSONObject>> hashedPropertyMappingCache = new ConcurrentHashMap<>();

    private static volatile ConcurrentHashMap<String, JSONArray> configListMappingCache = new ConcurrentHashMap<>();

    private void initConfigData(String configJsonPath, String keyPrefix) {
        lock.lock();
        try {
            if (hashedPropertyMappingCache.isEmpty() || configListMappingCache.isEmpty()) {
                JSONObject configJsonObject = JsonUtil.parseJsonFile(configJsonPath);
                //hashedPropertyMapping
                Map<String, JSONObject> hashedPropertyMap = JsonUtil.jsonArrayToMap(configJsonObject.getJSONArray("hashedProperty"), "propertyName");
                //configListMapping
                JSONArray configListMapping = configJsonObject.getJSONArray("configList");
                hashedPropertyMappingCache.put(keyPrefix + "hashedProperty", hashedPropertyMap);
                configListMappingCache.put(keyPrefix + "configList", configListMapping);
            }
        } finally {
            lock.unlock();
        }
    }

    private File fillIntoExcel(List<ExcelDef> cellDataList, InputStream is,String suffix) {
        String tempDir = "tempDir";
        FileUtil.initSaveTempDir("tempDir");
        File resultFile = FileUtil.createFile(tempDir + "/" + UUID.randomUUID() + "." + suffix);
        try {
            FileUtil.copyFile(is, resultFile);
        } catch (IOException e) {
            log.error("文件复制失败", e);
            throw new RuntimeException(e);
        }
        FileInputStream in = null;
        FileOutputStream fileOutputStream = null;
        try (
                Workbook workbook = WorkbookFactory.create(in = new FileInputStream(resultFile))
        ) {
            Sheet sheet = workbook.getSheetAt(0);
            for (ExcelDef cellDef : cellDataList) {
                sheet.getRow(cellDef.getXIndex()).getCell(cellDef.getYIndex()).setCellValue(cellDef.getValue());
            }
            //输出流
            fileOutputStream = new FileOutputStream(resultFile);
            //协出
            workbook.write(fileOutputStream);
        } catch (Exception e) {
            log.error("异常", e);
            throw new RuntimeException(e);
        } finally {
            IOUtils.closeQuietly(in);
            IOUtils.closeQuietly(fileOutputStream);
        }
        return resultFile;
    }
}
