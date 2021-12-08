package com.arcane22.dbdbdeep.v1;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Stack;


public class MainPanel extends JPanel {

    // Private inner class (button action listener)
    private class ButtonActionListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            JButton btn = (JButton) e.getSource();
            int hash = btn.hashCode();

            if(hash == menuBtn.hashCode()) {
                String queryStr = String.format("Select sName, changeDiaper, sum(mNum), sum(wNum)\n" +
                        "from Tourist, Toilet\n" +
                        "where address1 = sAddress or address2 = sAddress\n" +
                        "group by rollup(sName, changeDiaper)\n" +
                        "order by sName, changeDiaper;");

                String queryStr2 = String.format("Select sName, CCTV, avg(TS.starRate)\n" +
                        "from Tourist, Toilet, (select address1, address2, starRate\n" +
                        "from Toilet, Review\n" +
                        "where Toilet.tName = Review.tName) as TS\n" +
                        "where TS.address1 = sAddress or TS.address2 = sAddress\n" +
                        "group by rollup(sName, CCTV)\n" +
                        "order by sName,CCTV;");

                rs = DBManager.getInstance().execQuery(queryStr, 1);
                rs2 = DBManager.getInstance().execQuery(queryStr2, 2);
                sceneHistory.push(6);
                appSceneNum = 6;
            }
            else if(hash == homeBtn.hashCode()) {
                sceneHistory.push(0);
                appSceneNum = 0;
            }
            else if(hash == backBtn.hashCode()){
                if(sceneHistory.size() > 1) {
                    sceneHistory.pop();
                    appSceneNum = sceneHistory.get(sceneHistory.size() - 1);
                }
                else {
                    // Exit App
                }
            }
            else if(hash == scene0SelectBtn.hashCode()) {
                rs = DBManager.getInstance().execQuery("select distinct sName from Tourist;");
                sceneHistory.push(1);
                appSceneNum = 1;
            }
            else if(hash == scene0AllBtn.hashCode()) {
                scrollPane.removeAllData();
                rs = DBManager.getInstance().execQuery("select * from Toilet;");
                sceneHistory.push(2);
                appSceneNum = 2;
            }
            else if(hash == scene1SelectBtn.hashCode()) {
                scrollPane.removeAllData();
                String queryStr = String.format("select * from Tourist where sName='%s';", currTourist);
                rs = DBManager.getInstance().execQuery(queryStr);
                sceneHistory.push(4);
                appSceneNum = 4;
            }
            else if(hash == scene3ReviewBtn.hashCode()) {
                congestion = new Congestion();
                reviewList = new ArrayList<>();
                scrollPane2.removeAllData();

                String queryStr = String.format("Select Distinct Congestion.tName, mUsing , wUsing\n" +
                        "from  Congestion, Toilet\n" +
                        "where Congestion.tName = '%s';", toilet.tName);
                String queryStr2 = String.format("select * from Review where tName='%s';", toilet.tName);

                rs = DBManager.getInstance().execQuery(queryStr, 1);
                rs2 = DBManager.getInstance().execQuery(queryStr2, 2);
                sceneHistory.push(5);
                appSceneNum = 5;
            }
        }
    }

    // private constants, instance variables
    private Stack<Integer> sceneHistory;
    private int appSceneNum;

    private Toilet toilet;
    private Congestion congestion;
    private ArrayList<Review> reviewList;
    private String currTourist;

    private ScrollPane scrollPane, scrollPane2, scrollPane3, scrollPane4;
    private JComboBox<String> touristCBox;
    private ImageSource pToiletImgSrc, pToilet2ImgSrc, jejuImgSrc, touristImgSrc, travelerImgSrc;
    private ResultSet rs, rs2;

    private JButton menuBtn, homeBtn, backBtn;
    private JButton scene0SelectBtn, scene0AllBtn;
    private JButton scene1SelectBtn;
    private JButton scene3ReviewBtn;

    // Constructor
    public MainPanel(int w, int h) {

        setLayout(null);
        setBounds(0, 0, w, h);

        scrollPane = new ScrollPane(20, 150, 500, 600);
        scrollPane2 = new ScrollPane(20, 400, 500, 400);
        scrollPane3 = new ScrollPane(20, 100, 500, 300);
        scrollPane4 = new ScrollPane(20, 500, 500, 300);

        scrollPane.setMainPanel(this);
        scrollPane2.setMainPanel(this);
        scrollPane3.setMainPanel(this);
        scrollPane4.setMainPanel(this);
        add(scrollPane);
        add(scrollPane2);
        add(scrollPane3);
        add(scrollPane4);

        touristCBox = new JComboBox<>();
        touristCBox.setBounds(110, 500, 320, 40);
        touristCBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                currTourist = e.getItem().toString();
            }
        });
        add(touristCBox);

        sceneHistory = new Stack<>();
        sceneHistory.push(0);
        pToiletImgSrc = new ImageSource("./img/public_toilet.png", 0, 50);
        pToilet2ImgSrc = new ImageSource("./img/public_toilet2.png", 0, 100);
        pToilet2ImgSrc.setXToCenter(this.getWidth());
        jejuImgSrc = new ImageSource("./img/jeju2.png", 0, 620);
        touristImgSrc = new ImageSource("./img/jeju.png", 0, 150);
        touristImgSrc.setXToCenter(this.getWidth());
        travelerImgSrc = new ImageSource("./img/traveler.png", 0, 100);
        travelerImgSrc.setXToCenter(this.getWidth());

        menuBtn = new JButton(new ImageIcon("./img/menuBtnImage.png"));
        homeBtn = new JButton(new ImageIcon("./img/homeBtnImage.png"));
        backBtn = new JButton(new ImageIcon("./img/backBtnImage.png"));
        scene0SelectBtn = new JButton(new ImageIcon("./img/scene0SelectBtn.png"));
        scene0AllBtn = new JButton(new ImageIcon("./img/scene0AllBtn.png"));
        scene1SelectBtn = new JButton(new ImageIcon("./img/scene1SelectBtn.png"));
        scene3ReviewBtn = new JButton(new ImageIcon("./img/scene3ReviewBtn.png"));

        ButtonActionListener btnActionListener = new ButtonActionListener();
        setButton(menuBtn, 0, 850, 180, 80, btnActionListener);
        setButton(homeBtn,180, 850, 180, 80, btnActionListener);
        setButton(backBtn,360, 850, 180, 80, btnActionListener);
        setButton(scene0SelectBtn, 120, 350, 300, 100, btnActionListener);
        setButton(scene0AllBtn, 120, 500, 300, 100, btnActionListener);
        setButton(scene1SelectBtn, 170, 650, 200, 100, btnActionListener);
        setButton(scene3ReviewBtn, 350, 750, 150, 50, btnActionListener);
    }



    /*
     * Method for initialize buttons
     * @param {JButton} button, button for initializing
     * @param {int} x, y, w, h, x pos / y pos / width / height
     * @param {ButtonActionListener} listener, Button Action listener
     * @return {void}
     */
    private void setButton(JButton button, int x, int y, int w, int h, ButtonActionListener listener) {

        button.setBounds(x, y, w, h);
        button.setBackground(Color.BLACK);
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.addActionListener(listener);
        button.setVisible(true);
        add(button);
    }



    /*
     * Method for drawing Scene0
     * @Param {Graphics2D} g2D, graphics2D object for drawing application components
     * @Return {void}
     */
    private void drawScene0(Graphics2D g2D) {
        pToiletImgSrc.setXToCenter(this.getWidth());
        pToiletImgSrc.drawImage(g2D);
        jejuImgSrc.setXToCenter(this.getWidth());
        jejuImgSrc.drawImage(g2D);

        g2D.setFont(new Font("맑은고딕", Font.BOLD, 30));
        g2D.drawString("관광객을 위한", 160, 250);
        g2D.drawString("제주 공공화장실 정보", 110, 300);

        scrollPane.setVisible(false);
        scrollPane2.setVisible(false);
        scrollPane3.setVisible(false);
        scrollPane4.setVisible(false);
        touristCBox.setVisible(false);
        scene1SelectBtn.setVisible(false);
        scene3ReviewBtn.setVisible(false);

        scene0SelectBtn.setVisible(true);
        scene0AllBtn.setVisible(true);
    }



    /*
     * Method for drawing Scene1 (Select
     * @Param {Graphics2D} g2D, graphics2D object for drawing application components
     * @Return {void}
     */
    private void drawScene1(Graphics2D g2D) {
        g2D.setFont(new Font("맑은고딕", Font.BOLD, 30));
        g2D.drawString("관광지 목록 선택", 150, 150);
        touristImgSrc.drawImage(g2D);


        try {
            if(rs != null && !rs.isClosed()) {
                while(rs.next()) {
                    touristCBox.addItem(rs.getString(1));
                }
            }
        }
        catch(SQLException e) {
            e.printStackTrace();
        }

        scrollPane.setVisible(false);
        scrollPane2.setVisible(false);
        scrollPane3.setVisible(false);
        scrollPane4.setVisible(false);
        scene3ReviewBtn.setVisible(false);
        scene0SelectBtn.setVisible(false);
        scene0AllBtn.setVisible(false);

        scene1SelectBtn.setVisible(true);
        touristCBox.setVisible(true);
    }



    /*
     * Method for drawing Scene2 (Show all toilet list)
     * @Param {Graphics2D} g2D, graphics2D object for drawing application components
     * @Return {void}
     */
    private void drawScene2(Graphics2D g2D) {
        g2D.setFont(new Font("맑은고딕", Font.BOLD, 30));
        g2D.drawString("전체 공중화장실 정보 조회", 75, 100);

        StringBuffer sb = new StringBuffer();
        String tName, address1, address2, diaper, pNumber, openTime;
        Boolean unisex, cctv;
        int mNum, wNum;
        tName = address1 = address2 = diaper = pNumber = openTime = "";
        unisex = cctv = false;
        mNum = wNum = 0;

        try {
            if(rs != null && !rs.isClosed()) {
                while(rs.next()) {
                    tName = rs.getString(1);
                    address1 = rs.getString(2);
                    address2 = rs.getString(3);
                    sb.append(tName);
                    sb.append(", ");
                    sb.append(address1);
                    sb.append(", ");
                    sb.append(address2);
                    scrollPane.appendData(sb.toString());
                    sb.delete(0, sb.length());
                }
            }
        }
        catch(SQLException e) {
            e.printStackTrace();
        }

        scene0SelectBtn.setVisible(false);
        scene0AllBtn.setVisible(false);
        scene1SelectBtn.setVisible(false);
        scene3ReviewBtn.setVisible(false);
        touristCBox.setVisible(false);
        scrollPane2.setVisible(false);
        scrollPane3.setVisible(false);
        scrollPane4.setVisible(false);

        scrollPane.setVisible(true);
    }



    /*
     * Method for drawing Scene3 (Show Toilet information)
     * @Param {Graphics2D} g2D, graphics2D object for drawing application components
     * @Return {void}
     */
    private void drawScene3(Graphics2D g2D) {

        pToilet2ImgSrc.drawImage(g2D);
        if(toilet != null) {
            g2D.setFont(new Font("맑은고딕", Font.BOLD, 22));
            g2D.drawString("화장실명: " + toilet.tName, 50, 300);

            g2D.setFont(new Font("맑은고딕", Font.BOLD, 15));
            g2D.drawString("주소1: " + (toilet.address1.equals("") ? "정보 없음" : toilet.address1), 50, 350);
            g2D.drawString("주소2: " + (toilet.address2.equals("") ? "정보 없음" : toilet.address2), 50, 400);

            g2D.setFont(new Font("맑은고딕", Font.BOLD, 20));
            g2D.drawString("남녀 공용: " + (toilet.unisex ? 'Y' : 'N'), 50, 450);
            g2D.drawString("남자화장실 칸 수: " + toilet.mNum, 50, 500);
            g2D.drawString("여자화장실 칸 수: " +toilet.wNum, 50, 550);
            g2D.drawString("cctv 설치: " + (toilet.cctv ? 'Y' : 'N'), 50, 600);
            g2D.drawString("기저귀교환대: " + toilet.diaper, 50, 650);
            g2D.drawString("관리자번호: " + toilet.pNumber, 50, 700);
            g2D.drawString("개방시간: " +toilet.openTime, 50, 750);
        }

        scene0SelectBtn.setVisible(false);
        scene0AllBtn.setVisible(false);
        scene1SelectBtn.setVisible(false);
        touristCBox.setVisible(false);
        scrollPane.setVisible(false);
        scrollPane2.setVisible(false);
        scrollPane3.setVisible(false);
        scrollPane4.setVisible(false);

        scene3ReviewBtn.setVisible(true);
    }



    /*
     * Method for drawing Scene4
     * @Param {Graphics2D} g2D, graphics2D object for drawing application components
     * @Return {void}
     */
    private void drawScene4(Graphics2D g2D) {

        g2D.setFont(new Font("맑은고딕", Font.BOLD, 30));
        g2D.drawString("관광지 정보 & 화장실 정보", 75, 100);

        String sName, tType, bName, sAddress;
        StringBuffer sb = new StringBuffer();

        try {
            if(rs != null && !rs.isClosed()) {
                while(rs.next()) {
                    sName = rs.getString(1);
                    tType = rs.getString(2);
                    switch(rs.getString(2)) {
                        case "f":
                            tType = "음식점";
                            break;
                        case "s":
                            tType = "관광지";
                            break;
                        case "h":
                            tType = "숙박시설";
                            break;
                        case "t":
                            tType = "화장실";
                            break;
                    }
                    bName = rs.getString(3);
                    sAddress = rs.getString(4);

                    sb.append(sName);
                    sb.append(", ");
                    sb.append(tType);
                    sb.append(", ");
                    sb.append(bName);
                    sb.append(", ");
                    sb.append(sAddress);
                    scrollPane.appendData(sb.toString());
                    sb.delete(0, sb.length());
                }
            }
        }
        catch(SQLException e) {
            e.printStackTrace();
        }

        scene0SelectBtn.setVisible(false);
        scene0AllBtn.setVisible(false);
        scene3ReviewBtn.setVisible(false);
        scene1SelectBtn.setVisible(false);
        touristCBox.setVisible(false);
        scrollPane2.setVisible(false);
        scrollPane3.setVisible(false);
        scrollPane4.setVisible(false);

        scrollPane.setVisible(true);
    }



    /*
     * Method for drawing Scene5 (Show Toilet Congestion, Review)
     * @Param {Graphics2D} g2D, graphics2D object for drawing application components
     * @Return {void}
     */
    private void drawScene5(Graphics2D g2D) {

        pToilet2ImgSrc.drawImage(g2D);
        try {
            if(rs != null && !rs.isClosed()) {
                while(rs.next()) {
                    congestion.tName = rs.getString(1);
                    congestion.mUsing = rs.getInt(2);
                    congestion.wUsing = rs.getInt(3);
                    congestion.mNum = toilet.mNum;
                    congestion.wNum = toilet.wNum;
                }
            }

            StringBuffer sb = new StringBuffer();
            if(rs2 != null && !rs2.isClosed()) {
                while (rs2.next()) {
                    sb.append("화장실 명: " + rs2.getString(1));
                    sb.append(",  ");
                    sb.append("의견: " + rs2.getString(2));
                    sb.append(",  ");
                    sb.append("별점: " + rs2.getString(3) + " / 5");
                    scrollPane2.appendData(sb.toString());
                    sb.delete(0, sb.length());
                }

                if(scrollPane2.getElementCount() == 0) {
                    scrollPane2.appendData("  데이터가 없습니다.");
                }
            }
        }
        catch(SQLException ex) {
            ex.printStackTrace();
        }

        if(congestion != null) {
            g2D.setFont(new Font("맑은고딕", Font.BOLD, 22));
            g2D.drawString("화장실명: " + congestion.tName, 50, 250);

            g2D.setFont(new Font("맑은고딕", Font.BOLD, 15));
            g2D.drawString(String.format("남자화장실 [사용수, 칸 수]: [%d, %d],  혼잡도: %.1f ", congestion.mUsing, congestion.mNum,
                                        (congestion.mNum == 0f ? 0f : 100 * congestion.mUsing / (float) congestion.mNum)) + "(%)", 50, 280);
            g2D.drawString(String.format("여자화장실 [사용수, 칸 수]: [%d, %d],  혼잡도: %.1f ", congestion.wUsing, congestion.wNum,
                                        (congestion.wNum == 0f ? 0f : 100 * congestion.wUsing / (float) congestion.wNum)) + "(%)", 50, 310);
        }

        scene0SelectBtn.setVisible(false);
        scene0AllBtn.setVisible(false);
        scene1SelectBtn.setVisible(false);
        scene3ReviewBtn.setVisible(false);
        touristCBox.setVisible(false);
        scrollPane.setVisible(false);
        scrollPane3.setVisible(false);
        scrollPane4.setVisible(false);

        scrollPane2.setVisible(true);
    }


    /*
     * Method for drawing Scene6 (Show Toilet Congestion, Review)
     * @Param {Graphics2D} g2D, graphics2D object for drawing application components
     * @Return {void}
     */
    private void drawScene6(Graphics2D g2D) {

        g2D.setFont(new Font("맑은고딕", Font.BOLD, 20));
        g2D.drawString("화장실의 기저기 교환대 유무에 따른 변기 수 통계", 40, 70);
        g2D.drawString("CCTV 설치 유무에 따른 평균 벌점 통계", 75, 470);

        try {
            StringBuffer sb = new StringBuffer();
            if(rs != null && !rs.isClosed()) {
                while(rs.next()) {
                    sb.append("관광지명: " + rs.getString(1));
                    sb.append(",  ");
                    sb.append("기저귀 교환대 유무: " +  rs.getString(2));
                    sb.append(",  ");
                    sb.append("남자화장실 변기 총합: " + rs.getInt(3));
                    sb.append(",  ");
                    sb.append("여자화장실 변기 총합: " + rs.getInt(4));
                    scrollPane3.appendData(sb.toString());
                    sb.delete(0, sb.length());
                }

                if(scrollPane3.getElementCount() == 0) {
                    scrollPane3.appendData("  데이터가 없습니다.");
                }
            }

            sb = new StringBuffer();
            if(rs2 != null && !rs2.isClosed()) {
                while (rs2.next()) {
                    sb.append("관광지명: " + rs2.getString(1));
                    sb.append(",  ");
                    sb.append("CCTV 설치유무: " + (rs2.getBoolean(2) ? "유" : "무"));
                    sb.append(",  ");
                    sb.append(String.format("평균별점: %.2f", rs2.getFloat(3)));
                    scrollPane4.appendData(sb.toString());
                    sb.delete(0, sb.length());
                }

                if(scrollPane4.getElementCount() == 0) {
                    scrollPane4.appendData("  데이터가 없습니다.");
                }
            }
        }
        catch(SQLException ex) {
            ex.printStackTrace();
        }

        scene0SelectBtn.setVisible(false);
        scene0AllBtn.setVisible(false);
        scene1SelectBtn.setVisible(false);
        scene3ReviewBtn.setVisible(false);
        touristCBox.setVisible(false);
        scrollPane.setVisible(false);
        scrollPane2.setVisible(false);

        scrollPane3.setVisible(true);
        scrollPane4.setVisible(true);
    }



    /*
     * Method for changing current scene in another class
     * @param {int} num, scene number
     * @param {String} data, data to send panel
     * @return {void}
     */
    public void setSceneNum(int fromScene, int toScene, String... data){
        appSceneNum = toScene;
        sceneHistory.push(toScene);

        if(toScene == 3)
        {
            if(fromScene == 2) {
                String queryStr = String.format("select * from Toilet where tName='%s';", data[0]);
                rs = DBManager.getInstance().execQuery(queryStr);
                toilet = new Toilet();

                try {
                    while(rs.next()) {
                        toilet.tName = rs.getString(1);
                        toilet.address1 = rs.getString(2);
                        toilet.address2 = rs.getString(3);
                        toilet.unisex = rs.getBoolean(4);
                        toilet.mNum = rs.getInt(5);
                        toilet.wNum = rs.getInt(6);
                        toilet.cctv = rs.getBoolean(7);
                        toilet.diaper = rs.getString(8);
                        toilet.pNumber = rs.getString(9);
                        toilet.openTime = rs.getString(10);
                    }
                }
                catch(SQLException e) {
                    e.printStackTrace();
                }
            }
            else if(fromScene == 4) {
                String queryStr = String.format("select * from " +
                        "(Select Distinct * from  Toilet where address1 = any(select sAddress from Tourist where sName = '%s') " +
                        "or address2 = any(select sAddress from Tourist where sName = '%s')) as ST\n" +
                        "where ST.address1 = '%s' or ST.address2 = '%s';", data[0], data[1], data[2], data[3]);
                rs = DBManager.getInstance().execQuery(queryStr);
                toilet = new Toilet();

                try {
                    while(rs.next()) {
                        toilet.tName = rs.getString(1);
                        toilet.address1 = rs.getString(2);
                        toilet.address2 = rs.getString(3);
                        toilet.unisex = rs.getBoolean(4);
                        toilet.mNum = rs.getInt(5);
                        toilet.wNum = rs.getInt(6);
                        toilet.cctv = rs.getBoolean(7);
                        toilet.diaper = rs.getString(8);
                        toilet.pNumber = rs.getString(9);
                        toilet.openTime = rs.getString(10);
                    }
                }
                catch(SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public int getSceneNum() {
        return appSceneNum;
    }



    /*
     * Method for drawing graphics object (rect, circle, image etc..)
     * @Param {Graphics} g, graphics object for drawing application components
     * @Return {void}
     */
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2D = (Graphics2D) g;

        // status bar
        g2D.setColor(Color.GRAY);
        g2D.fillRect(0, 0, 540, 30);

        g2D.setColor(Color.WHITE);
        g2D.setFont(new Font("맑은고딕", Font.BOLD, 12));
        String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        g2D.drawString(date, 20, 20);
        g2D.fillRect(480, 10, 20, 10);
        g2D.fillRect(500, 12, 4, 6);

        g2D.setColor(Color.DARK_GRAY);
        g2D.fillRect(480, 10, 15, 10);


        g2D.setColor(Color.DARK_GRAY);
        // draw scene
        switch(appSceneNum) {
            case 0:
                drawScene0(g2D);
                break;
            case 1:
                drawScene1(g2D);
                break;
            case 2:
                drawScene2(g2D);
                break;
            case 3:
                drawScene3(g2D);
                break;
            case 4:
                drawScene4(g2D);
                break;
            case 5:
                drawScene5(g2D);
                break;
            case 6:
                drawScene6(g2D);
                break;
        }
    }
}
