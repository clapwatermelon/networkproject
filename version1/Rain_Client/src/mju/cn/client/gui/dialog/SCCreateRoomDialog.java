package mju.cn.client.gui.dialog;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.ButtonModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.JCheckBox;

import mju.cn.client.gui.SCMainFrame;
import mju.cn.client.gui.item.SCSound;
import mju.cn.client.gui.panel.SCLobbyPanel;

public class SCCreateRoomDialog extends JPanel  {
   private static final long serialVersionUID = 1L;

   // Components
   private Image m_background; // 배경이미지
   private JTextField m_roomName; // 방이름 필드
   private JSlider m_gameSlider; // 게임 횟수 슬라이더
   private JSlider m_timeSlider; // 시간 조절 슬라이더
   /*
   private JCheckBox timerCheck1 = new JCheckBox("하");
   private JCheckBox timerCheck2 = new JCheckBox("중");
   private JCheckBox timerCheck3 = new JCheckBox("상");
   */
   private ButtonGroup m_buttonGroup;//*난이도 라디오 버튼 그룹
   private JRadioButton m_radioBtn;//*난이도 라디오버튼

   private String m_selectedLevel; //*선택된 난이도
   private JButton m_yes; // 만들기 버튼
   private JButton m_no; // 취소 버튼
   private SCMainFrame m_owner; // 상위 프레임
   private SCSound m_sound;
   ActionListener levelListener; //*난이도 액션
   
   // Constructor
   public SCCreateRoomDialog(SCMainFrame owner) {
      m_roomName = new JTextField();
      m_gameSlider = new JSlider(JSlider.HORIZONTAL, 5, 20, 5);
      m_timeSlider = new JSlider(JSlider.HORIZONTAL, 1, 3, 1);
      m_yes = new JButton();
      m_no = new JButton();
      m_owner = owner;
      m_radioBtn = null;
      m_buttonGroup = new ButtonGroup();
      m_sound = new SCSound();
      this.init();
      this.initEventHandler();
      
   }

   // Initialization
   private void init() {
      initInputField(m_roomName);

      m_background = Toolkit.getDefaultToolkit().getImage(
            "images/create_dialog.png");

      m_yes.setContentAreaFilled(false);
      m_yes.setBorder(null);
      m_yes.setIcon(new ImageIcon("images/exit_yes.png"));
      m_yes.setCursor(new Cursor(Cursor.HAND_CURSOR));
      m_yes.setSize(126, 27);
      m_yes.setOpaque(false);

      m_no.setContentAreaFilled(false);
      m_no.setBorder(null);
      m_no.setIcon(new ImageIcon("images/exit_no.png"));
      m_no.setCursor(new Cursor(Cursor.HAND_CURSOR));
      m_no.setSize(126, 100); //버튼 크기인가
      m_no.setOpaque(false);

      this.setOpaque(false);
      this.setPreferredSize(new Dimension(340, 230));
      this.setLayout(new BorderLayout());
      this.add(this.createTitle(), BorderLayout.NORTH);
      this.add(this.createBottomPanel(), BorderLayout.SOUTH);
          
   }   
   

   // 이벤트핸들러 초기화 함수
   private void initEventHandler() {
      m_yes.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {


            if (m_roomName.getText().trim().equals("")) {
               m_owner.getChild().hideDialog();
               m_owner.getChild().showMessageDialog("방 제목을 입력하세요.");
            }

            try {
               String roomName = m_roomName.getText().trim();

               m_selectedLevel =  m_buttonGroup.getSelection().getActionCommand();
               System.out.println(m_selectedLevel);

               SCLobbyPanel lobbyPanel = m_owner.getChild().getLobbyPanel();
          
               lobbyPanel.getRoomListController().createRoom(lobbyPanel.getUserId(), roomName, m_selectedLevel);
               m_owner.getChild().hideDialog();
            } catch (Exception ex) {
            	ex.printStackTrace();
               m_owner.getChild().hideDialog();
            }
         }
         
      });

      m_no.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            m_owner.getGlassPane().setVisible(false);
         }
      });
   }

   // 입력필드 초기화 함수
   private void initInputField(JTextField input) {
      input.setBorder(null);
      input.setFont(new Font("돋움", Font.BOLD, 15));
      input.setForeground(Color.DARK_GRAY);
      input.setPreferredSize(new Dimension(180, 20));
   }

   // 타이틀생성 함수
   private JPanel createTitle() {
      JPanel panel = new JPanel() {
         private static final long serialVersionUID = 1L;

         @Override
         protected void paintComponent(Graphics g) {
            /*
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                  RenderingHints.VALUE_ANTIALIAS_ON);
            Font oldFont = g2d.getFont();
            g2d.setFont(new Font("돋움", Font.BOLD, 16));
            g2d.setColor(Color.BLACK);
            g2d.drawString("방 만들기", 20, 30);
            g2d.setFont(oldFont);
            */
            super.paintComponent(g);
         }

      };
      panel.setOpaque(false);
      panel.setPreferredSize(new Dimension(0, 40));
      return panel;
   }

   // 아래쪽패널 생성 함수
   private Box createBottomPanel() {
      Box box = Box.createVerticalBox();
      box.add(this.createInputPanel("방 제목", m_roomName, m_gameSlider,
            m_timeSlider));
      box.add(this.createButtonPanel());

      return box;
   }

   // 입력패널 생성 함수
   private JPanel createInputPanel(String text, JTextField input,
         JSlider gameSlider, JSlider timeSlider) {
      JPanel panel = new JPanel();
      panel.setPreferredSize(new Dimension(0, 120));
      panel.setOpaque(false);
      panel.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 8)); // 방 만들기 밑에 얼마나 띄울것인
      panel.add(this.createLabel(text));
      panel.add(input);
      panel.add(this.createLabel("난이도"));
      
     
      /*난이도 라디오버튼 생성*/
      m_radioBtn = new JRadioButton();
    
      
      m_radioBtn.setText("하");
      m_radioBtn.setSelected(true);
      m_radioBtn.setActionCommand("하");
      m_buttonGroup.add(m_radioBtn);
      panel.add(m_radioBtn);
     
      m_radioBtn = new JRadioButton();
      m_radioBtn.setText("중");
      m_radioBtn.setActionCommand("중");

     // m_radioBtn.addActionListener(levelListener);
      m_buttonGroup.add(m_radioBtn);
      panel.add(m_radioBtn);
      
      m_radioBtn = new JRadioButton();
      m_radioBtn.setText("상");
      m_radioBtn.setActionCommand("상");
      //m_radioBtn.addActionListener(levelListener);
      m_buttonGroup.add(m_radioBtn);
      panel.add(m_radioBtn);
      return panel;
   }

   // 버튼패널 생성 함수
   private JPanel createButtonPanel() {
      JPanel panel = new JPanel();
      panel.setPreferredSize(new Dimension(0, 50));
      panel.setOpaque(false);
      panel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, -4)); // 버튼 위치
      panel.add(m_yes);
      panel.add(m_no);

      return panel;
   }

   // 라벨생성 함수
   private JLabel createLabel(String text) {
      JLabel label = new JLabel(text);
      label.setOpaque(false);
      label.setFont(new Font("돋움", Font.BOLD, 15));
      label.setForeground(Color.BLACK);
      label.setPreferredSize(new Dimension(70, 25));
      return label;
   }

   // 그리기 함수
   protected void paintComponent(Graphics g) {
      // TODO Auto-generated method stub
      g.drawImage(m_background, 0, 0, m_background.getWidth(this) + 20,
            m_background.getHeight(this), this);
      super.paintComponent(g);
   }
}