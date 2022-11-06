package bbs;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

 
public class BbsDAO {
	  private Connection conn;
	  //private PreparedStatement pstmt; ->데이터 접근에 있어서 마찰이 생기지 않게 없애야함, 직접넣음
	  private ResultSet rs;

	 

	    public BbsDAO() {

	        try {

	            String dbURL = "jdbc:mariadb://localhost/BBS";

	            String dbID = "root";

	            String dbPassword = "password";

	            Class.forName("org.mariadb.jdbc.Driver");

	            conn = DriverManager.getConnection(dbURL, dbID, dbPassword);

	        } catch (Exception e) {
	            e.printStackTrace();

	        }
	    }

	 

	    public String getDate() {
	        String SQL = "SELECT NOW()";
	        try {
	            PreparedStatement pstmt = conn.prepareStatement(SQL); //conn객체를 이용 SQL문장을 실행준비로 만듬
	            rs = pstmt.executeQuery();
	            if (rs.next()) {
	                return rs.getString(1); //1을해서 현재날짜 그대로 반환
	            }
	        } catch(Exception e) {
	            e.printStackTrace();
	        }
	        return ""; //데이터베이스오류
	    }

	 

	    public int getNext() {
	        String SQL = "SELECT bbsID FROM BBS ORDER BY bbsID DESC"; // 내림차순하여 가장 마지막에쓰인 글번호              를 가져올 수 있도록함
	        try {
	            PreparedStatement pstmt = conn.prepareStatement(SQL); // conn객체를 이용 SQL문장을 실행준비로 만듬
	            rs = pstmt.executeQuery();
	            if (rs.next()) {
	               return rs.getInt(1) +1; //1을 더해서 그다음 게시글이 들어갈 수 있도록한다.
	            }
	            return 1; // 현재가 첫번째 게시물인 경우
	        } catch(Exception e) {
	            e.printStackTrace();
	        }
	        return -1; //데이터베이스오류
	    }

	 

	    public int write(String bbsTitle, String userID, String bbsContent) {
	        String SQL = "INSERT INTO BBS VALUES (?, ?, ?, ?, ?, ?)";// 데이터베이스 코드
	        try {
	            PreparedStatement pstmt = conn.prepareStatement(SQL); //conn객체를 이용, SQL문장을 실행준비로 만듬
	            pstmt.setInt(1, getNext());//getNext 다음번에 쓰일 게시글번호
	            pstmt.setString(2, bbsTitle);
	            pstmt.setString(3, userID);
	            pstmt.setString(4, getDate());
	            pstmt.setString(5, bbsContent);
	            pstmt.setInt(6, 1);//허용상태 글이 있는상태이기 때문에 1
	            return pstmt.executeUpdate(); // 성공적으로 수행시 0이상의 값을 반환
	            //INSERT는 executeUpdate()로 작동됨
	        } catch(Exception e) {
	            e.printStackTrace();
	        }
	        return -1; //데이터베이스오류
	    }
	}
