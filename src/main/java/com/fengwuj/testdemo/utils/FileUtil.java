package com.fengwuj.testdemo.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.util.IOUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j
public class FileUtil {

    private static final Lock lock = new ReentrantLock();

    public static void initSaveTempDir(String dirPath) {
        lock.lock();
        try {
            File tempDir = new File(dirPath);
            if (!tempDir.exists()) {
                tempDir.mkdirs();
            }
        } finally {
            lock.unlock();
        }
    }

    /**
     * 建立文件(可以同时建立文件需要的文件夹)
     * <功能详细描述>
     *
     * @param path
     * @return File [返回类型说明]
     * @throws throws [异常类型] [异常说明]
     * @see [类、类#方法、类#成员]
     */
    public static File createFile(String path) {
        // 先格式化路径
        if (path == null || "".equals(path)) {
            return null;
        }

        if (path.contains("/")){
            path = getPath(path);
            String mdr = path.substring(0, path.lastIndexOf("/"));

            File file = new File(mdr);

            if (!file.exists()) {
                try {
                    // 建立路径
                    file.mkdirs();
                } catch (Exception e) {
                    return null;
                }

            }
        }
        return new File(path);
    }

    /**
     * 获得本地认识的path(屏蔽平台之间的字符集设置)
     * <功能详细描述>
     *
     * @param path
     * @return String [返回类型说明]
     * @throws throws [异常类型] [异常说明]
     * @see [类、类#方法、类#成员]
     */
    public static String getPath(String path) {
        path = path.replace("\\\\", "/");
        return new String(path.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * 复制文件
     * <功能详细描述>
     *
     * @param is
     * @param dirFile
     * @return boolean [返回类型说明]
     * @throws IOException [参数说明]
     * @throws throws      [异常类型] [异常说明]
     * @see [类、类#方法、类#成员]
     */
    public static boolean copyFile(InputStream is, File dirFile)
            throws IOException {
        try (FileOutputStream out = new FileOutputStream(dirFile)){
            copyFile(is, out);
        }
        return true;
    }

    /**
     * 拷贝文件
     * <功能详细描述>
     *
     * @param in
     * @param out
     * @return boolean [返回类型说明]
     * @throws IOException [参数说明]
     * @throws throws      [异常类型] [异常说明]
     * @see [类、类#方法、类#成员]
     */
    public static boolean copyFile(InputStream in, OutputStream out) throws IOException {
        if (in == null || out == null) {
            return false;
        }
        IOUtils.copy(in,out);
        return true;
    }

    /**
     * 复制文件
     * <功能详细描述>
     *
     * @param srcFile 来源
     * @param dirFile 目标
     * @return boolean [返回类型说明]
     * @throws IOException [参数说明]
     * @throws throws      [异常类型] [异常说明]
     * @see [类、类#方法、类#成员]
     */
    public static boolean copyFile(String srcFile, String dirFile) {
        try (
                FileOutputStream out = new FileOutputStream(getPath(dirFile));
                FileInputStream in = new FileInputStream(getPath(srcFile))
        ) {
            copyFile(in, out);
        }catch (IOException e){
            throw new RuntimeException(e);
        }
        return true;
    }
}
