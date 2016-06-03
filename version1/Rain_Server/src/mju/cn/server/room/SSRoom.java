package mju.cn.server.room;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Random;
import java.util.Vector;

import javax.swing.Timer;

import mju.cn.common.Word;
import mju.cn.server.controller.SSGameController;
import mju.cn.server.db.SQLiteManager;
import mju.cn.server.network.SSSocketManager;
import mju.cn.server.player.SSPlayer;

public class SSRoom extends Thread{
	// Attributes
	private int m_roomNum; // 방 번호
	private int m_playTime; // 플레이 시간
	private int m_maxPlayer; //최대인원
	private int m_level;
	private Vector<String> m_dbWords;
	
	private HashMap<String, Integer> m_playersHP;	//채력
	private HashMap<String, Integer> m_playersScore; //점수
	private int m_threadTime;						//쓰래드 쉬는 시간
	private int m_threadTimeBefore;					//blue아이템
	private int m_timerTime;
	private int m_surviveCnt;
	private Vector<Word> m_viewingWords;
	private Vector<String> m_blueWords;
	private Vector<String> m_redWords;
	private Vector<String> m_blindWords;
	private Vector<String> m_stopWords;
	private boolean m_stopped;
	
	private boolean m_blueOn;
	private int m_blueTime;
	private boolean m_stopOn;
	private int m_stopTime;
	private boolean m_blindOn;
	private int m_blindTime;
	
	// Components
	private Vector<SSPlayer> m_players; //플레이어 목록	
	private boolean m_waiting; //방 상태
	private String m_roomSubject; //방제목
	private SSLobby m_parent; // 부모 객체
	private SSGameController m_gameController; //게임컨트롤러
	private Thread m_thread; // 게임 쓰래드
	private ActionListener actionListener;
	private Timer m_timer;
	
	

	
	// Constructor
	public SSRoom(int roomNum, int maxPlayer, String subject, SSLobby lobby, String level){
		m_roomNum = roomNum;
		m_maxPlayer = maxPlayer;
		m_players = new Vector<SSPlayer>();
		m_roomSubject = subject;
		m_parent = lobby;
		m_waiting = true;
		if(level.equals("상")) {
			m_level = 3;
		}
		else if(level.equals("중")) {
			m_level = 2;
		}
		else {
			m_level = 1;
		}
		m_playersHP = new HashMap<String, Integer>();
		m_surviveCnt = 0;
		m_threadTime = 1000;
		m_timerTime = 0;
		m_stopped = false;
		m_stopOn = false;
		actionListener = new ActionListener() {
	        public void actionPerformed(ActionEvent e) {
	        	boolean isEnd = checkTime();
	        	if(isEnd) {
	        		finishGame();
					try {
						m_thread.sleep(5000); 
					}catch(InterruptedException ie) {}
					resultGame();
	        	}
	        	
	        }
		};
		m_gameController = new SSGameController();
	}	
	
	// Initialization
	// 모든 플레이어의 게임정보를 초기화 한다.
	public void initGame(){
		SQLiteManager sql=new SQLiteManager();
        m_dbWords=sql.getRecord();
		m_waiting = false;									//방상태 게임중
		m_playersHP = new HashMap<String, Integer>();		//매 새로시작마다 초기화
		m_playersScore = new HashMap<String, Integer>();
		for (SSPlayer player : getPlayerList()) {
			m_playersHP.put(player.getId(), 3);			//채력표만들기
			m_playersScore.put(player.getId(), 0);			//점수표만들기
		}
		m_surviveCnt = getPlayerList().size();				//살아있는 수 만들기
		for (int index = 0; index < m_players.size(); index++) {	//사람초기화
			SSPlayer player = m_players.get(index);
			player.initInfo();
		}
		m_viewingWords = new Vector<Word>();
		m_blueWords = new Vector<String>();
		m_redWords = new Vector<String>();
		m_blindWords = new Vector<String>();
		m_stopWords = new Vector<String>();
		m_thread = new Thread(this);					//게임쓰래드 생성
		m_stopped = false;
		m_blindOn = false;
		m_blueOn = false;
		m_blueTime = 0;
		m_threadTime = 1000;
	    m_threadTimeBefore = m_threadTime;
		
		m_timer = new Timer(1000, actionListener);
		m_thread.start();									//게임시작
		m_timer.start();
	}
	
	/* get Player의 HP*/
	public int getPlayerHP(String id){
		return m_playersHP.get(id);
	}
	
	public int getPlayerScore(String id){
		return m_playersScore.get(id);
	}
	

	
	
	// 게임종료 함수
	// 방을 대기룸 상태로 놓는다.
	public void endGame (){
		m_waiting = true;
	}
	
	
	
	// 입장 함수
	// 최대 인원수 내에 입장이 가능하다.
	public boolean enterRoom(SSPlayer player) {
		if (m_players.size()>=m_maxPlayer) {
			return false;
		}else{
			m_players.add(player);
			m_parent.getWaitPlayerList().remove(player);	
			return true;
		}		
	}
	
	// 퇴장함수
	// 방에서 플레이어를  삭제하고 대기방에 입력한다.
	// 마지막 플레이어가 나가면 방을 삭제한다.
	public boolean exitRoom(SSPlayer player){
		m_players.remove(player);
		m_parent.getWaitPlayerList().add(player);
		
		if(m_players.size() == 0){
			SSLobby lobby = SSLobby.getLobby();
			lobby.deleteRoom(this);
			return false;
		}
		return true;
	}
	
	//채력깍는 함수
	public void minusHP(){			
		for(SSPlayer player : m_players) {
			int hp = m_playersHP.get(player.getId());
			hp--;
			if(hp == 0) {
				m_surviveCnt--;
			}
			m_playersHP.put(player.getId(), hp);
		}
		m_gameController.minusHP(m_roomNum);
	}
	
	public void finishGame() {
		m_timer.stop();
		m_gameController.finishGame(m_roomNum);
	}
	
	public void resultGame() {
		m_stopped = true;
		m_waiting = true;
		m_gameController.resultGame(m_roomNum);
		
	}
	
	public boolean checkTime() {
		m_timerTime++;
		if(m_timerTime%10 == 0 && m_threadTime > 400) {		//시간이 갈수록 소리줄어듬
			m_threadTime-=100;
		}
		
		if(m_blueOn) {										//파란색 아이템(빨라지는 아이템)
    		if(m_timerTime-m_blueTime >= 3) {
    			m_threadTime =  m_threadTimeBefore;
    			m_blueOn = false;
    		}
    	}
		
		if(m_stopOn) {										//녹색아이템(멈추는 아이템)
			if(m_timerTime-m_stopTime >= 3) {
    			m_stopOn = false;
    		}
		}
		
//		if(m_blindOn){
//			if(m_timerTime-m_blindTime >= 3) {
//    			m_blindOn = false;
//    			m_gameController.blindOff(m_roomNum);
//    		}
//		}
		
		if(m_timerTime >= 300) {								//5분이 되면 게임종료
			return true;
		}
		else {
			return false;
		}
	}
	
	public void run() {
		try {
			m_thread.sleep(3000); 								//3초쉰다 게임시작알림중
		} catch (InterruptedException e) {}
		m_gameController.startGame(this);
		int wordCnt = 0;										//생성한 단어수
		SSLobby lobby = SSLobby.getLobby();						
		
		while(!m_stopped) {
			try {
				m_thread.sleep(m_threadTime); 					//쓰래드 초를 표현하기위한 일시적 슬립
			}catch(InterruptedException e) {}
			 
			for(Word w : m_viewingWords) {						//현재 생성한 단어들의 Y자표값을 바꾼다
				w.addY(m_stopOn);
			}
         
			if(!m_viewingWords.isEmpty()) {
				if(m_viewingWords.get(0).getY() > 510) {      	  //일정Y좌표이상갈시 모두 채력감소
					m_viewingWords.remove(0);
            		//전채채력 감소
					minusHP();
				}            
			}
			
			for(int level=0; level<m_level; level++) {		//레벨에 따라 한회에 생성하는 단어수 조절
				wordCnt++;
				Random rand = new Random();
				int randomNum = rand.nextInt(m_dbWords.size());
				String thisWord = m_dbWords.remove(randomNum);
				if(wordCnt%5 == 0) {	
//					if(wordCnt%10 == 0) {
//						m_blindWords.add(thisWord);
//					}
					if(wordCnt%20 == 0) {
						m_stopWords.add(thisWord);
					}
					else if(wordCnt%10 == 0) {
						m_blueWords.add(thisWord);
					}
					else {
						m_redWords.add(thisWord);
					}
				}
				if(!m_stopOn) {
					m_viewingWords.add(new Word(thisWord,m_level,level,""));
				}
				
			}
			m_gameController.sendGameData(getRoomNum());
			
			if(m_surviveCnt <= 1) {
				finishGame();
				try {
					m_thread.sleep(5000); 
				}catch(InterruptedException e) {}
				resultGame();
			}
		}
	}
	
	public boolean scoringWord(String userId, String inputText) {
		for(Word word : m_viewingWords) {
			if(word.getString().equals(inputText)) {
				m_viewingWords.remove(word);
				System.out.println(inputText);
				if(m_blueWords.contains(inputText)) {
					m_blueWords.remove(inputText);
					if(!m_blueOn) {
						m_threadTimeBefore = m_threadTime;						// 돌아갈 시간을 저장
						m_threadTime = 300;										// 임의로 빠르게 할 시간을 입력
						m_blueOn = true;										// 빨리시작 설정
						m_blueTime = m_timerTime;								// 빨리시작한 시간을 저장(5초후에 되돌리기위하여)
					}
				}
				else if(m_redWords.contains(inputText)) {						// 친아이템이 
					m_redWords.remove(inputText);
					int userHP = m_playersHP.get(userId);
					if(userHP < 3) {
						userHP++;
						m_playersHP.put(userId, userHP);
						m_gameController.updateHP(m_roomNum, userId, userHP);
					}
				}
				else if(m_stopWords.contains(inputText)){						//만약 친 단어가 스탑아이템일경우
					m_stopOn = true;
					m_stopTime = m_timerTime;
				}
//				else if(m_blindWords.contains(inputText)){
//					m_blindOn = true;
//					m_blindTime = m_timerTime;
//					m_gameController.blindOn(m_roomNum, userId);				// 사용자 제외하고 보냄
//				}
				int score = m_playersScore.get(userId);
				score+=10;
				m_playersScore.put(userId, score);
				return true;
			}
		}
		return false;
	}

	
	
	
	
	// Getters and Setters
	public Vector<SSPlayer> getPlayerList() {
		return m_players;
	}

	public SSPlayer getGameHost() {
		return m_players.get(0);
	}
	public boolean isStart() {
		return !m_waiting;
	}

	public int getMaxPlayer() {
		return m_maxPlayer;
	}

	public void setMaxPlayer(int m_maxPlayer) {
		this.m_maxPlayer = m_maxPlayer;
	}

	public boolean isWaiting() {
		return m_waiting;
	}

	public void setWaiting(boolean m_waiting) {
		this.m_waiting = m_waiting;
	}

	public String getRoomSubject() {
		return m_roomSubject;
	}

	public void setRoomSubject(String m_roomSubject) {
		this.m_roomSubject = m_roomSubject;
	}

	public int getRoomNum() {
		return m_roomNum;
	}

	public int getPlayTime() {
		return m_playTime;
	}

	public void setPlayTime(int playTime) {
		this.m_playTime = playTime;
	}
	
	public int getSurviveCnt() {
		return m_surviveCnt;
	}

	public void setSurviveCnt(int surviveCnt) {
		this.m_surviveCnt = surviveCnt;
	}

	public int getThreadTime() {
		return m_threadTime;
	}

	public void setThreadTime(int m_threadTime) {
		this.m_threadTime = m_threadTime;
	}
	
	public Vector<Word> getViewingWord() {
		return m_viewingWords;
	}
	
	public void setViewingWord(Vector<Word> viewingWord) {
		this.m_viewingWords = viewingWord;
	}
	
	public HashMap<String, Integer> getPlayersHP() {
		return m_playersHP;
	}
	
	public HashMap<String, Integer> getPlayersScore() {
		return m_playersScore;
	}
	
	public void setViewingWord(HashMap<String, Integer> playersHP) {
		this.m_playersHP = playersHP;
	}
	
	public Vector<String> getBlueWords() {
		return m_blueWords;
	}
	
	public void setBlueWords(Vector<String> bluewords) {
		this.m_blueWords = bluewords;
	}
	
	public Vector<String> getRedWords() {
		return m_redWords;
	}
	
	public void setRedWords(Vector<String> redwords) {
		this.m_redWords = redwords;
	}
	
	public Vector<String> getStopWords() {
		return m_stopWords;
	}
	
	public void setStopWords(Vector<String> stopwords) {
		this.m_stopWords = stopwords;
	}
	
	public Vector<String> getBlindWords() {
		return m_blindWords;
	}
	
	public void setBlindWords(Vector<String> blindwords) {
		this.m_blindWords = blindwords;
	}

}
