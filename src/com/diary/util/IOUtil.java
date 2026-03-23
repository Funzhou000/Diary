package com.diary.util;

import com.diary.entity.Diary;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 文件读写工具类
 */
public class IOUtil {
    private static final long serialVersionUID = 1L;
    // 定义数据保存的相对路径，文件会直接生成在你 IDEA 项目的根目录下
    private static final String FILE_PATH = "diary_data.dat";

    /**
     * Serialization: 将日记列表保存到硬盘
     */
    public static void saveData(List<Diary> diaryList) {
        // 使用 ObjectOutputStream 进行对象序列化
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            oos.writeObject(diaryList);
        } catch (IOException e) {
            System.err.println("保存数据失败：" + e.getMessage());
        }
    }

    /**
     * Deserialization: 从硬盘读取日记列表
     */
    @SuppressWarnings("unchecked")
    public static List<Diary> loadData() {
        File file = new File(FILE_PATH);
        // 如果文件不存在（比如第一次运行程序），直接返回一个空的 ArrayList
        if (!file.exists()) {
            return new ArrayList<>();
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (List<Diary>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("读取数据失败：" + e.getMessage());
            return new ArrayList<>();
        }
    }
}