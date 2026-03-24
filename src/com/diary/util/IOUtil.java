package com.diary.util;

import com.diary.entity.Diary;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * 文件读写工具类
 */
public class IOUtil  {

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
    /**
     * Export (导出)：将日记数据打包为 ZIP 压缩包
     * @param diaryList 当前表格中的日记数据
     * @param zipFilePath 压缩包要保存的绝对路径 (Absolute Path)
     */
    public static boolean exportToZip(List<Diary> diaryList, String zipFilePath) {
        // 使用 Try-with-resources 自动管理流的关闭
        try (FileOutputStream fos = new FileOutputStream(zipFilePath);
             ZipOutputStream zos = new ZipOutputStream(fos)) {

            // ZipEntry 代表压缩包里的一个文件条目
            ZipEntry entry = new ZipEntry("diary_data.dat");
            zos.putNextEntry(entry);

            // 将集合对象写入压缩包内的文件中
            // 注意：这里不能把 ObjectOutputStream 放进 try() 的括号里，
            // 因为它一旦 close()，就会把外层的 ZipOutputStream 也关掉，导致压缩文件损坏
            ObjectOutputStream oos = new ObjectOutputStream(zos);
            oos.writeObject(diaryList);
            oos.flush(); // 刷新缓冲区 (Flush buffer)

            zos.closeEntry(); // 完成当前条目的写入
            return true;
        } catch (IOException e) {
            System.err.println("导出 ZIP 失败：" + e.getMessage());
            return false;
        }
    }
    /**
     * Import (导入)：从 ZIP 压缩包中读取日记数据
     * @param zipFilePath 压缩包的绝对路径
     * @return 读取到的日记列表，如果失败或文件不存在则返回 null
     */
    @SuppressWarnings("unchecked")
    public static List<Diary> importFromZip(String zipFilePath) {
        File zipFile = new File(zipFilePath);

        // Validation: 检查桌面上到底有没有这个压缩包
        if (!zipFile.exists()) {
            return null;
        }

        try (FileInputStream fis = new FileInputStream(zipFile);
             ZipInputStream zis = new ZipInputStream(fis)) {

            // 获取压缩包内的第一个文件条目 (我们昨天存进去的 diary_data.dat)
            ZipEntry entry = zis.getNextEntry();

            if (entry != null && entry.getName().equals("diary_data.dat")) {
                // 读取对象
                ObjectInputStream ois = new ObjectInputStream(zis);
                List<Diary> importedList = (List<Diary>) ois.readObject();

                zis.closeEntry(); // 关闭当前条目
                return importedList;
            }
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("导入 ZIP 失败：" + e.getMessage());
        }
        return null;
    }
}