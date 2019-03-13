package ui;

import entity.DeviceInfo;
import entity.TaskListItem;
import sd.SDClient;
import sd.SDClientCallback;
import utils.Util;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Vector;

public class MainWindow {

    private JFrame frame;
    private JList listDeviceFound;
    private JButton btnScan;
    private JButton btnSendFile;
    private JTextArea taConsole;
    private JScrollPane scrollPane_1;
    private JTable table;
    private PromptDialog dialogPrompt;

    private ActionListener btnScanClickListener = new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent e) {
            // TODO Auto-generated method stub
            printConsole("click button scan");
            SDClient sdClient = new SDClient(new SDClientCallback() {

                @Override
                public void discoverDevices(List<DeviceInfo> deviceInfoList) {
                    // TODO Auto-generated method stub
                    DeviceInfo[] deviceInfos = new DeviceInfo[deviceInfoList.size()];
                    deviceInfoList.toArray(deviceInfos);
                    listDeviceFound.setListData(deviceInfos);
                    dialogPrompt.setVisible(false);

                }

            });
            sdClient.start();
            PromptDialog dialogPrompt = getDialogPrompt();
            dialogPrompt.setMessage("正在扫描...");
            dialogPrompt.setVisible(true);
        }

    };

    private ActionListener btnSendFileClickListener = new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent e) {
            // TODO Auto-generated method stub


        }

    };

    /**
     * Create the application.
     */
    public MainWindow() {
        initialize();
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {
        frame = new JFrame();
        frame.setResizable(false);
        frame.setTitle("\u5C40\u57DF\u7F51\u6587\u4EF6\u4F20\u8F93\u52A9\u624B");
        frame.setBounds(100, 100, 527, 554);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);

        JLabel label = new JLabel("\u5DF2\u53D1\u73B0\u8BBE\u5907");
        label.setBounds(14, 13, 95, 18);
        frame.getContentPane().add(label);

        listDeviceFound = new JList();
        listDeviceFound.setBounds(14, 44, 166, 246);
        frame.getContentPane().add(listDeviceFound);

        JLabel label_1 = new JLabel("\u65E5\u5FD7\u8F93\u51FA");
        label_1.setBounds(219, 13, 72, 18);
        frame.getContentPane().add(label_1);

        btnScan = new JButton("\u626B\u63CF");
        btnScan.setBounds(14, 303, 113, 27);
        frame.getContentPane().add(btnScan);
        btnScan.addActionListener(btnScanClickListener);

        btnSendFile = new JButton("\u53D1\u9001\u6587\u4EF6");
        btnSendFile.setBounds(141, 303, 113, 27);
        frame.getContentPane().add(btnSendFile);
        btnSendFile.addActionListener(btnSendFileClickListener);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(219, 44, 273, 246);
        frame.getContentPane().add(scrollPane);

        taConsole = new JTextArea();
        taConsole.setEditable(false);
        scrollPane.setViewportView(taConsole);

        table = new JTable();
        table.setModel(new DefaultTableModel(
                new Object[][] {
                },
                new String[] {
                        "\u7C7B\u578B", "\u5BF9\u7AEFIP", "\u6587\u4EF6\u8DEF\u5F84", "\u8FDB\u5EA6"
                }
        ) {
            boolean[] columnEditables = new boolean[] {
                    false, false, false, false
            };
            public boolean isCellEditable(int row, int column) {
                return columnEditables[column];
            }
        });

        scrollPane_1 = new JScrollPane(table);
        scrollPane_1.setBounds(14, 372, 481, 122);
        frame.getContentPane().add(scrollPane_1);

        JLabel tblTask = new JLabel("\u4EFB\u52A1\u5217\u8868");
        tblTask.setBounds(14, 343, 72, 18);
        frame.getContentPane().add(tblTask);

    }

    public void printConsole(String text) {
        taConsole.append(Util.getTimeString() + " " + text + "\n");
    }

    public void setVisible(boolean visible) {
        frame.setVisible(visible);
    }

    private PromptDialog getDialogPrompt() {
        if(dialogPrompt == null) {
            dialogPrompt = new PromptDialog();
        }
        return dialogPrompt;
    }

    public void addTask(TaskListItem item) {
        Vector<String> v = new Vector<String>(4);
        v.add(item.getTypeString());
        v.add(item.getPeerIp());
        v.add(item.getPath());
        v.add(item.getProgressString());
        DefaultTableModel model = (DefaultTableModel)table.getModel();
        item.setId(model.getRowCount());
        model.addRow(v);
    }

    public void updateProgress(TaskListItem item) {
        table.setValueAt(item.getProgressString(), item.getId(), 3);
    }

    public void removeTask(TaskListItem item) {
        DefaultTableModel model = (DefaultTableModel)table.getModel();
        model.removeRow(item.getId());
    }
}
