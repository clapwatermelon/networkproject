package mju.cn.client.gui.panel;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

import mju.cn.client.gui.SCMainFrame;


public class SCGameResultPanel extends JPanel {
	private String m_userId; 
	private String m_HostId; 
	private Vector<Object[]> playerList ; 
	private JPanel slotPanel;
	private Image bg_lose;
	private Image bg_win;
	private Image bg_current; //win or lose. 
	private boolean isWinner;
	private JButton exitButton;
	private SCMainFrame m_owner;
	
	
	public SCGameResultPanel(SCMainFrame m_owner,String m_userId,String m_HostId,Vector<Object[]> playerList,boolean isWinner){
		this.m_userId = m_userId;
		this.m_HostId = m_HostId;
		this.playerList = playerList;
		this.slotPanel = new JPanel();
		this.isWinner = isWinner;
		this.exitButton = new JButton();
		this.m_owner = m_owner;
		this.init();
		this.initEventHandler();
	}
	
	 // Initialization
	public void init(){
		this.setOpaque(false);
	    this.setPreferredSize(new Dimension(500, 500));
	    this.setLayout(null);
	    
	    bg_lose = Toolkit.getDefaultToolkit().getImage(
				"images/bg_gameresult_lose.png");
	    bg_win = Toolkit.getDefaultToolkit().getImage(
				"images/bg_gameresult_win.png");
	    exitButton.setContentAreaFilled(false);
	    exitButton.setBorder(null);
	    exitButton.setIcon(new ImageIcon("images/btn_gameresult_exit.png"));
	    exitButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
	    exitButton.setSize(25, 25);
	    exitButton.setOpaque(false);
	    
	    exitButton.setBounds(465, 3, 25, 25);
	   this.add(exitButton);
	   
	    
	    slotPanel.setLayout(null);
	    
	    
	    for(int i=0; i< playerList.size(); i++){
	    	
	    	String id = (String)playerList.get(i)[0];
	    	String avatar = (String)playerList.get(i)[1];
	    	int score = (Integer)playerList.get(i)[2];
	    	boolean isWinner = (Boolean)playerList.get(i)[3];
	    	
	    	SCGameResultSlotPanel panel = new SCGameResultSlotPanel(id,avatar,score, this);
	    	panel.setBounds(0, 0+(i*43), 492, 43);
	    	slotPanel.add(panel);
	    }
	    
	    slotPanel.setBounds(0, 200, 492, 260);
	    this.add(slotPanel);
	}
	
	private void initEventHandler() {
		
		exitButton.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
             m_owner.getGlassPane().setVisible(false);
         }
		});
	}

	public String getUserId(){
		return m_userId;
	}
	public String getHostId(){
		return m_HostId;
	}

	@Override
	protected void paintComponent(Graphics g) {
		if(isWinner){
			g.drawImage(bg_win, 0, 0, this.getWidth(), this.getHeight(), this);
		}else{
			g.drawImage(bg_lose, 0, 0, this.getWidth(), this.getHeight(), this);
		}
		super.paintComponent(g);
	}
	

}
