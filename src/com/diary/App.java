package com.diary;

import com.diary.ui.MainFrame;

public class App {
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            // 这里目前会报错，因为我们还没写 MainFrame 类
            // 等明天完成 Day 2 的任务，主界面就能跑起来了
            // new MainFrame().setVisible(true);
            MainFrame mainFrame = new MainFrame();
            mainFrame.setVisible(true); // 设置窗口为可见 (Make it visible)
        });

    }
}
