package mju.cn.client.gui;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseMotionAdapter;

import javax.swing.JComponent;
import javax.swing.JPanel;


public class SCGlassPane extends JPanel {
	private static final long serialVersionUID = 1L;
	
	// Components
	private JComponent m_component; // 그리고자하는 컴포넌트

	// Constructor
	public SCGlassPane() {
	}

	// Initialization
	public void init() {
		this.setPreferredSize(new Dimension(SCMainFrame.FRAME_WIDTH,
				SCMainFrame.FRAME_HEIGHT));
		this.setOpaque(false);
		this.initEventHandler();
	}

	// 이벤트핸들러 생성 함수
	private void initEventHandler() {
		this.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentShown(ComponentEvent e) {
				requestFocusInWindow();
			}
		});

		this.addMouseListener(new MouseAdapter() {
		});
		this.addMouseMotionListener(new MouseMotionAdapter() {
		});
		this.addKeyListener(new KeyAdapter() {
		});
		this.setFocusTraversalKeysEnabled(false);
	}

	// 컴포넌트 설정 함수
	public void setComponent(JComponent comp) {
		if (m_component != null) {
			this.remove(m_component);
		}
		m_component = comp;

		int vgap = (this.getHeight() / 2 - (int) (m_component
				.getPreferredSize().getHeight()) / 2);
		this.setLayout(new FlowLayout(FlowLayout.CENTER, 0, vgap));
		this.add(m_component);
	}

	// 컴포넌트 그리기 함수
	@Override
	protected void paintComponent(Graphics g) {
		if (this.isVisible()) {
			Graphics2D g2d = (Graphics2D) g;
			Composite oldComposite = g2d.getComposite();
			g2d.setComposite(AlphaComposite.getInstance(
					AlphaComposite.SRC_OVER, 0.3f));
			g2d.setColor(Color.white);
			g2d.fillRect(0, 0, this.getWidth(), this.getHeight());
			g2d.setComposite(oldComposite);
		}
		super.paintComponent(g);
	}

}
