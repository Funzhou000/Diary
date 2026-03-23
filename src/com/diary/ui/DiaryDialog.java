package com.diary.ui;

import com.diary.entity.Diary;

import javax.swing.*;
import java.awt.*;

/**
 * 日记添加/修改弹窗
 * 继承自 JDialog (对话框)
 */
public class DiaryDialog extends JDialog {

    private JTextField titleField;  // 单行文本输入框 (用于标题)
    private JTextArea contentArea;  // 多行文本区域 (用于正文)
    private JButton btnSave;
    private JButton btnCancel;
    /**
     * 将已有数据填充到文本框中，用于修改模式的数据回显
     */
    public void setDiaryData(String title, String content) {
        titleField.setText(title);
        contentArea.setText(content);
    }
    public boolean isSaved() {
        return isSaved;
    }

    public Diary getDiary() {
        return diary;
    }

    private boolean isSaved = false;
    private Diary diary; // 用于存储生成的数据对象
    // Constructor (构造方法)
    public DiaryDialog(JFrame parent, String dialogTitle) {
        // super 调用父类 JDialog 的构造方法
        // 参数3：true 表示这是一个 Modal Dialog (模态对话框)
        // 模态的含义是：如果不关闭这个弹窗，用户就无法点击背后的主界面
        super(parent, dialogTitle, true);
        initUI();
    }

    private void initUI() {
        setSize(400, 300);
        // setLocationRelativeTo(getParent()) 会让弹窗恰好出现在主窗口的正中央
        setLocationRelativeTo(getParent());

        // 1. 标题输入区 (North)
        JPanel titlePanel = new JPanel(new BorderLayout(5, 5));
        titlePanel.add(new JLabel(" 标题："), BorderLayout.WEST);
        titleField = new JTextField();
        titlePanel.add(titleField, BorderLayout.CENTER);

        // 2. 正文输入区 (Center)
        JPanel contentPanel = new JPanel(new BorderLayout(5, 5));
        contentPanel.add(new JLabel(" 内容："), BorderLayout.NORTH);
        contentArea = new JTextArea();
        contentArea.setLineWrap(true); // 开启自动换行 (Word wrap)

        // 正文可能很长，必须放进 JScrollPane 里才会有滚动条
        JScrollPane scrollPane = new JScrollPane(contentArea);
        contentPanel.add(scrollPane, BorderLayout.CENTER);

        // 3. 按钮区 (South)
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnSave = new JButton("保存");
        btnCancel = new JButton("取消");
        buttonPanel.add(btnSave);
        buttonPanel.add(btnCancel);

        // 4. 将以上三个部分拼装到 Dialog 的 Container (容器) 中
        Container cp = getContentPane();
        // 给 BorderLayout 增加一点间距 (Padding) 让界面不那么拥挤
        cp.setLayout(new BorderLayout(10, 10));
        cp.add(titlePanel, BorderLayout.NORTH);
        cp.add(contentPanel, BorderLayout.CENTER);
        cp.add(buttonPanel, BorderLayout.SOUTH);
        // btnSave 的点击逻辑
        btnSave.addActionListener(e -> {
            // 获取输入内容并去除首尾空格
            String title = titleField.getText().trim();
            String content = contentArea.getText().trim();

            // Data Validation (数据校验)：防止用户输入空数据
            if (title.isEmpty() || content.isEmpty()) {
                JOptionPane.showMessageDialog(this, "标题和内容不能为空！", "Warning", JOptionPane.WARNING_MESSAGE);
                return; // 终止执行，不关闭窗口
            }

            // 实例化 Entity Object (实体对象)
            // 编号暂时放空字符串，由主界面统一分配
             diary = new Diary(title, content,"" );
            isSaved = true; // 标记为已保存
            dispose(); // 销毁并关闭当前弹窗
        });

        // btnCancel 的点击逻辑
        btnCancel.addActionListener(e -> {
            isSaved = false;
            dispose(); // 直接关闭弹窗
        });

    }
}