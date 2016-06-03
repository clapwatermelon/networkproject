package mju.cn.client.gui.panel;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.JPanel;
import javax.swing.JScrollPane;


public class SCPlayerListPanel extends JPanel{
	private static final long serialVersionUID = 1L;
	
	// Attributes
	private Image m_background; // 배경
	
	// Components
	private JScrollPane m_scrollPane; // 스크롤
	private JPanel m_mainPanel; // 메인패널
	private SCLobbyPanel m_parent; // 상위패널(로비)
	
	// Constructor
	public SCPlayerListPanel(SCLobbyPanel parent){
		m_parent = parent;
		m_background = Toolkit.getDefaultToolkit().getImage("images/Player_List.png");
		m_mainPanel = new JPanel();
		m_scrollPane = new JScrollPane(m_mainPanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		this.init();
	}
	
	// Initialization
	private void init(){
		m_mainPanel.setPreferredSize(new Dimension(230, 270));
		m_mainPanel.setOpaque(false);
		m_mainPanel.setBorder(null);
		
		m_scrollPane.setPreferredSize(new Dimension(230, 270));
		m_scrollPane.setOpaque(false);
		m_scrollPane.getViewport().setOpaque(false);
		m_scrollPane.setBorder(null);
		
		this.setOpaque(false);
		this.setPreferredSize(new Dimension(245, 355));
		this.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 100));
		this.add(m_scrollPane);
		this.initMainPanel();
	}
	
	// 메인패널 초기화 함수
	public void initMainPanel(){
		m_mainPanel.removeAll();
		m_mainPanel.setOpaque(false);
		m_mainPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
		
		int height = 0;
		for(int i=0;i<m_parent.getPlayerList().size();i++){
			m_mainPanel.add(m_parent.getPlayerList().get(i));
			height += 40;
		}
		
		m_mainPanel.setPreferredSize(new Dimension(230, height));
		m_scrollPane.setViewportView(m_mainPanel);
		this.updateUI();
	}
	
	// 컴포넌트 그리기 함수
	@Override
	protected void paintComponent(Graphics g) {
		g.drawImage(m_background, 0, 40, this.getWidth(), 300, this);
		super.paintComponent(g);
	}
	
}
