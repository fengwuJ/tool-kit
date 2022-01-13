package com.fengwuj.testdemo.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.web.util.UriUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author amzheng
 * 处理流相关的工具
 */
@Slf4j
public class StreamUtil {

    private StreamUtil(){}

    public static void writePng(HttpServletResponse response, InputStream inputStream){
        response.setContentType("image/png");
        write(response,inputStream);
    }

    public static void download(HttpServletResponse response, InputStream inputStream, String fileName){
        response.setHeader("content-type", "application/octet-stream;charset=UTF-8");
        response.setContentType("application/octet-stream");
        try {
            response.setHeader("Content-Disposition", "attachment; filename*=utf-8''"+ UriUtils.encode(fileName, "UTF-8"));
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        write(response,inputStream);
    }

    //header manipulation 问题解决
    public static boolean filterSpecialChar(String str){
        if(null == str || str.length() == 0){
            return true;
        }
        Pattern pa = Pattern.compile("[`~!@#$%^&*\\+\\=\\{}|:\"?><\\/r\\/n]");
        Matcher ma = pa.matcher(str);
        return ma.find();
    }
    public static String filterSpecialUrl(String url) {
        if(null == url || url.length() == 0){
            return "";
        }
        try {
            URL url2 = new URL(url);
            return new StringBuilder(url2.getProtocol())
                    .append("://")
                    .append(url2.getHost())
                    .append(url2.getPort()== -1 ? "" : ":"+url2.getPort())
                    .toString();
        }catch (MalformedURLException e){
            //ignore
        }
        if(url.indexOf(".com") != -1){
            return url.substring(0,url.indexOf(".com")+4);
        }
        if(url.indexOf(".cn") != -1){
            return url.substring(0,url.indexOf(".cn")+3);
        }
        return "";
    }

    public static void preview(HttpServletResponse response, InputStream inputStream){
        response.setContentType("application/pdf");
        write(response,inputStream);
    }

    public static void write(HttpServletResponse response, InputStream inputStream){
        if( response == null  ){
            log.error("response is null");
            return;
        }
        if(  inputStream == null ){
            log.error("inputStream is null");
            return;
        }
        OutputStream out = null;
        try {
            out = response.getOutputStream();
            IOUtils.copy(inputStream,out);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }finally {
            IOUtils.closeQuietly(inputStream);
            IOUtils.closeQuietly(out);
        }
    }

    public static void write(HttpServletResponse response, File file){
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            log.error("文件未找到",e);
            throw new RuntimeException(e);
        }
        write(response,inputStream);
    }

}
