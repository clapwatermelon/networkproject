package mju.cn.common;


//�ܾ� Ŭ����
public class Word {
    private int x;
    private int y;
    private String str;
    
    public Word(String str, int x, int y) {			//������ ��ġ�� ���鋚 (Client)
        this.str = str;
    	this.x = x;
        this.y = y;
   }
    
    public Word(String str, int level, int location, String empty) {		//������ ó�� �ܾ� �����Ҷ�
    	switch(level){
    	case 1:
    		this.x = (int) (Math.random() * (460 - 40 + 1)) + 40;
    		break;
    	case 2:
    		if(location == 0) {
    			this.x = (int) (Math.random() * (230 - 40 + 1)) + 40;
    		}
    		else {
    			this.x = (int) (Math.random() * (230 - 40 + 1)) + 270;
    		}
    		break;
    	case 3:
    		if(location == 0) {
    			this.x = (int) (Math.random() * (153 - 40 + 1)) + 40;
    		}
    		else if(location == 1){
    			this.x = (int) (Math.random() * (153 - 40 + 1)) + 193;
    		}
    		else {
    			this.x = (int) (Math.random() * (153 - 40 + 1)) + 346;
    		}
    		break;
    	}
    	this.y=0;
    	this.str= str;
    }
    
    public void setX(int x) {
    	this.x = x;
    }
    
    public int getX() {
    	return this.x;
    }
    
    public void setY(int y) {
    	this.y = y;
    }
    
    public int getY() {
    	return this.y;
    }
    
    public void setString(String str) {
    	this.str = str;
    }
    
    public String getString() {
    	return str;
    }
    
    public void addY (boolean isStop) {
    	if(isStop) {
//    		y = y; 			//�ƹ��������Ѵ�
    	}
    	else {
    		y+=20;
    	}
    }
 }
