package com.arcane22.dbdbdeep.v1;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ScrollPane extends JScrollPane {

    private class MyListCellRenderer implements ListCellRenderer {

        JLabel label;
        public MyListCellRenderer() {
            label = new JLabel();
            label.setOpaque(true);
            label.setBackground(new Color(200, 200, 200, 128));
            label.setBounds(0, 0, 30, 500);
        }
        @Override
        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            label.setText(value.toString());
            return label;
        }
    }

    private DefaultListModel<String> listModel;
    private JList jList;
    private MyListCellRenderer renderer;
    private MainPanel mainPanel;

    public ScrollPane(){
    }

    public ScrollPane(int x, int y, int w, int h) {
        setBounds(x, y, w, h);
        listModel = new DefaultListModel<>();
        jList = new JList();
        renderer = new MyListCellRenderer();

        jList.setFixedCellHeight(50);
        jList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        jList.setCellRenderer(renderer);
        jList.setModel(listModel);
        jList.setVisible(true);

        jList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                String s = (String) ((JList)e.getSource()).getSelectedValue();
                if(mainPanel.getSceneNum() == 2) {
                    mainPanel.setSceneNum(2, 3, s.split(", ")[0]);
                }
                else if(mainPanel.getSceneNum() == 4) {
                    if(s.split(", ")[1].equals("화장실")) {
                        mainPanel.setSceneNum(4,3, s.split(", ")[0], s.split(", ")[0], s.split(", ")[3], s.split(", ")[3]);
                    }
                    else {
                        JOptionPane.showMessageDialog(null, "화장실 정보를 선택해주세요!");
                    }
                }
            }
        });

       this.setViewportView(jList);
    }


    // set, remove data
    public void appendData(String data) {
        listModel.addElement(data);
    }

    public String popData() {
        return listModel.remove(listModel.size() - 1);
    }

    public void removeData(int idx) {
        listModel.remove(idx);
    }

    public void removeAllData() {
        listModel.clear();
    }

    public void setMainPanel(MainPanel panel) {
        this.mainPanel = panel;
    }

    public int getElementCount() {
        return listModel.size();
    }
}
