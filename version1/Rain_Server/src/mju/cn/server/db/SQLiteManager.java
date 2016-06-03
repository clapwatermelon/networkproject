package mju.cn.server.db;

import java.util.Vector;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;

public class SQLiteManager {
   Connection conn;
   Vector<String> word= new Vector();
   
   public SQLiteManager(){
      
   }
   //DB연동 메서드
   public void openConnection(){
      try{
         Class.forName("org.sqlite.JDBC");
         conn=DriverManager.getConnection("jdbc:sqlite:AcidRain.db");
         
   
      }catch(SQLException e){
         e.printStackTrace();
      }catch(ClassNotFoundException e){
         e.printStackTrace();
      }
   }
   //테이블 생성 메서드
   public void createTable(){
      this.openConnection();
      Statement stmt;
      String expression="CREATE TABLE word (id,word);";
      try {
         stmt=conn.createStatement();
         stmt.executeUpdate(expression);
         conn.close();
      } catch (SQLException e) {
         e.printStackTrace();
      }
      
      
   }
   //text에 있는 단어 Vector에 넣는 작업
   public void InsertWord() {
      try{
         BufferedReader in=new BufferedReader(new FileReader("words.txt"));
         String s;
         
         while((s=in.readLine())!=null){
            String[] split =s.split("\n");
            word.add(split[0]);
         }
         
      }catch(IOException e){
         e.printStackTrace();
      }
   }
   //Vector에 있는 단어 DB에 넣기
   public void InsertDB(){
      try {
         this.openConnection();
         for(int i=0;i<word.size();i++){
            PreparedStatement prep=conn.prepareStatement("insert into word values(?,?);");
            prep.setInt(1, i);
            prep.setString(2, word.get(i));
            prep.executeUpdate();
         }
      
      } catch (SQLException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
      
   }

   public Vector<String> getRecord(){
      this.openConnection();
      Statement st=null;
      ResultSet rs;
      Vector<String> returnValue=new Vector();
      String sql="SELECT * FROM word";
      
      try {
         st=conn.createStatement();
         rs=st.executeQuery(sql);
         while(rs.next()){
            returnValue.add(rs.getString("word"));
         }
         rs.close();
      } catch (SQLException e) {
         // TODO Auto-generated catch block
//         e.printStackTrace();
      }
      return returnValue;
   }
}