package com.diary.ui;

import com.diary.entity.Diary;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;


/**
 * 主界面窗口
 * 继承自 JFrame (Java 的基础窗口类)
 */
public class MainFrame extends JFrame {



    // 声明 UI Components (UI 组件)
    private JTable diaryTable;
    private DefaultTableModel tableModel;
    private JButton btnAdd;
    private JButton btnEdit;
    private JButton btnDelete;
    /**
     * 启动时：从本地文件读取数据并渲染到 Table
     */
    private void loadDataToTable() {
        List<Diary> list = com.diary.util.IOUtil.loadData();
        for (Diary d : list) {
            tableModel.addRow(new Object[]{d.getId(), d.getTitle(), d.getContent()});
        }
    }

    /**
     * 数据变动时：提取 Table 中的所有数据，同步保存到本地文件
     */
    private void syncDataToFile() {
        List<Diary> currentList = new ArrayList<>();
        // 遍历整个表格模型，把每一行重新组装成 Diary 对象
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            String id = (String) tableModel.getValueAt(i, 0);
            String title = (String) tableModel.getValueAt(i, 1);
            String content = (String) tableModel.getValueAt(i, 2);
            // 这里注意参数顺序：id, title, content
            currentList.add(new Diary(title, content, id));
        }
        // 调用工具类保存到硬盘
        com.diary.util.IOUtil.saveData(currentList);
    }

    // Constructor (构造方法)：在创建窗口时进行初始化
    public MainFrame() {
        initUI();
    }

    /**
     * 初始化 User Interface (用户界面)
     */
    private void initUI() {
        // 1. 设置窗口的基础 Properties (属性)
        setTitle("每日一记");
        setSize(600, 500); // 宽 600，高 500
        setLocationRelativeTo(null); // 让窗口在屏幕中央显示 (Center on screen)
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // 点击关闭按钮时退出整个程序

        // 2. 设置 Layout (布局)
        // BorderLayout (边界布局) 将窗口分为东南西北中五个区域
        setLayout(new BorderLayout());

        // 3. 初始化 Table (表格)
        // DefaultTableModel (默认表格模型) 用于管理表格的数据和列名
        String[] columnNames = {"编号", "标题", "正文"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            // Override (重写) 方法，让表格单元格默认不可直接在界面上编辑
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        diaryTable = new JTable(tableModel);

        // 将表格放入 JScrollPane (滚动面板) 中，这样数据多的时候会出现滚动条
        JScrollPane scrollPane = new JScrollPane(diaryTable);
        // 将滚动面板放到窗口的 Center (中间区域)
        add(scrollPane, BorderLayout.CENTER);

        // 4. 初始化 Buttons (按钮) 区域
        // JPanel 是一个容器，FlowLayout (流式布局) 会让里面的组件从左到右像流水一样排列
        JPanel bottomPanel = new JPanel(new FlowLayout());

        btnAdd = new JButton("添加");
        btnEdit = new JButton("修改");
        btnDelete = new JButton("删除");

        bottomPanel.add(btnAdd);
        bottomPanel.add(btnEdit);
        bottomPanel.add(btnDelete);

        // 将按钮面板放到窗口的 South (底部区域)
        add(bottomPanel, BorderLayout.SOUTH);
        // 为“添加”按钮绑定 Action Listener (动作监听器)
        // 当用户点击 (Click) 按钮时，就会执行 Lambda 表达式 {...} 里的代码
        btnAdd.addActionListener(e -> {
            // 创建并显示弹窗
            // MainFrame.this 指代当前的主窗口，作为弹窗的 parent
            DiaryDialog dialog = new DiaryDialog(MainFrame.this, "添加日记");
            dialog.setVisible(true);
        });
        // 为“添加”按钮绑定 Action Listener
        btnAdd.addActionListener(e -> {
            DiaryDialog dialog = new DiaryDialog(MainFrame.this, "添加日记");

            // 程序运行到这里会 Block (阻塞)，直到 dialog 被 dispose()
            dialog.setVisible(true);

            // 弹窗关闭后，代码继续往下执行。我们检查是否是通过“保存”按钮关闭的
            if (dialog.isSaved()) {
                // 获取弹窗里打包好的数据
                Diary newDiary = dialog.getDiary();

                // 动态生成一个简易编号 (当前表格行数 + 1)
                String newId = String.valueOf(tableModel.getRowCount() + 1);
                newDiary.setId(newId);

                // 将数据以 Object 数组的形式，追加到表格的 Model 中
                tableModel.addRow(new Object[]{
                        newDiary.getId(),
                        newDiary.getTitle(),
                        newDiary.getContent()
                });
            }
            syncDataToFile();
        });
        // 为“删除”按钮绑定 Action Listener
        btnDelete.addActionListener(e -> {
            // 获取用户选中的行索引 (Index 默认从 0 开始，如果没有选中则返回 -1)
            int selectedRow = diaryTable.getSelectedRow();

            // Validation: 检查是否选中了数据
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(MainFrame.this, "未选中要删除的行，请重试", "提示信息", JOptionPane.WARNING_MESSAGE);
                return; // 终止后续操作
            }

            // 弹出 Confirmation Dialog (确认对话框)
            int option = JOptionPane.showConfirmDialog(MainFrame.this, "是否删除选中数据？", "删除信息确认", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

            // 如果用户点击了“是 (YES)”
            if (option == JOptionPane.YES_OPTION) {
                // 直接从 Data Model (数据模型) 中移除这一行，界面会自动刷新
                tableModel.removeRow(selectedRow);
            }
            syncDataToFile();
        });
        // 为“修改”按钮绑定 Action Listener
        btnEdit.addActionListener(e -> {
            int selectedRow = diaryTable.getSelectedRow();

            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(MainFrame.this, "未选中要修改的行，请重试", "提示信息", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // 1. 从表格模型中提取当前选中的数据
            // getValueAt(row, column) 获取指定单元格的值，列索引：0是编号，1是标题，2是正文
            String oldTitle = (String) tableModel.getValueAt(selectedRow, 1);
            String oldContent = (String) tableModel.getValueAt(selectedRow, 2);

            // 2. 召唤修改弹窗
            DiaryDialog dialog = new DiaryDialog(MainFrame.this, "修改日记");
            // 在窗口显示之前，先把旧数据填进去
            dialog.setDiaryData(oldTitle, oldContent);
            // 窗口阻塞，等待用户操作
            dialog.setVisible(true);

            // 3. 弹窗关闭后，检查用户是否点击了“保存”
            if (dialog.isSaved()) {
                // 获取修改后的新数据
                Diary modifiedDiary = dialog.getDiary();

                // 逐个更新表格中对应行的列数据 (Update specific cells)
                tableModel.setValueAt(modifiedDiary.getTitle(), selectedRow, 1);
                tableModel.setValueAt(modifiedDiary.getContent(), selectedRow, 2);
            }
            syncDataToFile();
        });
        loadDataToTable();
    }
}