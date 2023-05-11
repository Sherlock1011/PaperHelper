package com.example.paperhelper.utils;

import android.telephony.IccOpenLogicalChannelResponse;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Sherlock
 * 文件操作类
 * 包括文件读写操作
 */
public class FileHelper {
    private static FileHelper instance;

    private FileHelper(){

    }

    /**
     * 外部读取文件接口
     * @param filePath 文件路径
     * @return 文件内容
     */
    public String readFile(String filePath){
        String encoding = "UTF-8";
        return readFile(filePath, encoding);
    }

    /**
     * 指定编码读取文件
     * @param filePath 文件路径
     * @param encoding 文件编码
     * @return 文件内容
     */
    private String readFile(String filePath, String encoding) {
        try {
            //获取文件路径
            filePath = URLDecoder.decode(filePath, "utf-8");
            File file = new File(filePath);
            Long fileLength = file.length();
            byte[] fileContent = new byte[fileLength.intValue()];
            FileInputStream inputStream = new FileInputStream(file);
            inputStream.read(fileContent);

            inputStream.close();
            return new String(fileContent, encoding);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    public String readHtml(String path) {
        File file = new File(path);
        StringBuffer sb = new StringBuffer();
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "utf-8"));
            while(br.ready()){
                sb.append(br.readLine());
            }
            br.close();
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }
        String html = sb.toString();
        return html;
    }

    /**
     * 将字符串写入文件，可选择是否以追加的形式写入
     * @param filePath 文件路径
     * @param content 文件内容
     * @param isAppend 是否以追加的形式写入
     */
    public void writeFile(String filePath, String content, boolean isAppend){
        try {
            filePath = URLDecoder.decode(filePath, "utf-8");
            File file = new File(filePath);
            if (!file.exists()){
                file.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(filePath, true);
            fos.write(content.getBytes());
            fos.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    /**
     * 将字符串写入文件 可选择编码方式
     * @param filePath 文件路径
     * @param content 文件内容
     * @param encoding 文件编码
     */
    public void writeFile(String filePath, String content, String encoding){
        try {
            filePath = URLDecoder.decode(filePath, "utf-8");
            File file = new File(filePath);
            if (!file.exists()){
                file.createNewFile();
            }

            OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(filePath), encoding);
            writer.append(content);
            writer.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    /**
     * 获取文件的创建时间
     * @param file 要获取创建时间的文件
     * @return 该文件的创建时间
     */
    public String getFileCreateTime(File file){
        try {
            //根据文件的绝对路径获取Path
            Path path = Paths.get(file.getAbsolutePath());
            //根据path获取文件的基本属性
            BasicFileAttributes attrs = Files.readAttributes(path, BasicFileAttributes.class);
            //从基本属性中获取文件的创建时间
            FileTime fileTime = attrs.creationTime();
            //将文件时间转变为毫秒级
            long millis = fileTime.toMillis();
            SimpleDateFormat dateFormat = new SimpleDateFormat("YYYY/MM/dd");
            Date date = new Date();
            date.setTime(millis);
            String time = dateFormat.format(date);
            return time;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static synchronized FileHelper getInstance(){
        if (instance == null){
            instance = new FileHelper();
        }
        return instance;
    }
}
