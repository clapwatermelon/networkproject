package mju.cn.client.gui.item;

import java.applet.Applet;
import java.applet.AudioClip;
import java.net.URL;


public class SCSound {
	// Attributes
	AudioClip ac;
	URL url;
	
	/* 게임 배경음악 */
	public void gameSound() {
		try {
			URL url = new URL("file:./sounds/patrit.wav");
			ac = Applet.newAudioClip(url);
			ac.loop();
			
			
		} catch (Exception e) {
			System.out.println(e);
		}
	}
	
	/* 배경음악 종료 */
	public void stopGameSound(){
		try {
			ac.stop();
			
		} catch (Exception e) {
			System.out.println(e);
		}
	
	}
	/* 게임 배경음악 */
	public void lobbySound() {
		try {
			URL url = new URL("file:./sounds/lobbysound.wav");
			ac = Applet.newAudioClip(url);
			ac.loop();
			
			
		} catch (Exception e) {
			System.out.println(e);
		}
	}
	
	/* 배경음악 종료 */
	public void stoplobbySound(){
		try {
			ac.stop();	
		} catch (Exception e) {
			System.out.println(e);
		}
	
	}

	/* 게임 배경음악 */
	public void loginSound() {
		try {
			URL url = new URL("file:./sounds/login.wav");
			ac = Applet.newAudioClip(url);
			ac.loop();
			
			
		} catch (Exception e) {
			System.out.println(e);
		}
	}
	
	/* 배경음악 종료 */
	public void stoploginSound(){
		try {
			ac.stop();	
		} catch (Exception e) {
			System.out.println(e);
		}
	
	}

	/* 게임 배경음악 */
	public void gameroomSound() {
		try {
			URL url = new URL("file:./sounds/gameroom.wav");
			ac = Applet.newAudioClip(url);
			ac.loop();
			
			
		} catch (Exception e) {
			System.out.println(e);
		}
	}
	
	/* 배경음악 종료 */
	public void stopgameroomSound(){
		try {
			ac.stop();	
		} catch (Exception e) {
			System.out.println(e);
		}
	
	}


	public void clickSound() {
		try {
			URL url = new URL("file:./sounds/click.wav");
			AudioClip ac = Applet.newAudioClip(url);
			ac.play();
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	public void alertSound() {
		try {
			URL url = new URL("file:./sounds/alert.wav");
			AudioClip ac = Applet.newAudioClip(url);
			ac.play();
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	public void enterSound() {
		try {
			URL url = new URL("file:./sounds/enter.wav");
			AudioClip ac = Applet.newAudioClip(url);
			ac.play();
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	public void joinSound() {
		try {
			URL url = new URL("file:./sounds/join_sound.wav");
			AudioClip ac = Applet.newAudioClip(url);
			ac.play();
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	public void correctSound() {
		try {
			URL url = new URL("file:./sounds/correct.wav");
			AudioClip ac = Applet.newAudioClip(url);
			ac.play();
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	public void slotJoin() {
		try {
			URL url = new URL("file:./sounds/slot_join.wav");
			AudioClip ac = Applet.newAudioClip(url);
			ac.play();
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	public void slotLeave() {
		try {
			URL url = new URL("file:./sounds/slot_leave.wav");
			AudioClip ac = Applet.newAudioClip(url);
			ac.play();
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	public void sliderSound() {
		try {
			URL url = new URL("file:./sounds/slide.wav");
			AudioClip ac = Applet.newAudioClip(url);
			ac.play();
		} catch (Exception e) {
			System.out.println(e);
		}
	}
	public void endSound() {
		try {
			URL url = new URL("file:./sounds/endsound.wav");
			AudioClip ac = Applet.newAudioClip(url);
			ac.play();
		} catch (Exception e) {
			System.out.println(e);
		}
	}
	public void scoreSound(){
	      try{
	         URL url = new URL("file:./sounds/score.wav");
	         AudioClip ac = Applet.newAudioClip(url);
	         ac.play();
	      } catch(Exception e){
	         System.out.println(e);
	      }
	   }


}