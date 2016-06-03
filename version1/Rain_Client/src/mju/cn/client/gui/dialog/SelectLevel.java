package mju.cn.client.gui.dialog;

//방 생성시 상 중 하 채크박스
public enum SelectLevel {
	low("하",1),middle("중",2),high("상",3);

	public String name;
	public int value;
	SelectLevel(String name,int value){
		this.name = name;
		this.value = value;
	}
}
