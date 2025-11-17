package dongggg;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {

    // 프로젝트 루트에 donggri.db 파일 생성
    private static final String URL = "jdbc:sqlite:donggri.db";

    // DB 연결용 메서드
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL);
    }

    // 앱 시작 시 호출: notes 테이블 없으면 생성
    public static void init() {
        String sql = "CREATE TABLE IF NOT EXISTS notes (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "title TEXT NOT NULL," +
                "content TEXT NOT NULL," +
                "created_at TEXT DEFAULT CURRENT_TIMESTAMP," +
                "updated_at TEXT DEFAULT CURRENT_TIMESTAMP" +
                ");";

        /*
         * • id : 기본키
         * • title : 노트 제목
         * • content : 노트 내용
         * • created_at, updated_at : 생성/수정 시간 (일단 TEXT로 둠)
         */

        // 오류 확인 try- catch
        try (Connection conn = getConnection();
                Statement stmt = conn.createStatement()) {

            stmt.execute(sql);
            System.out.println("[DB] notes 테이블 초기화 완료");

        } catch (SQLException e) {
            System.out.println("[DB] 초기화 중 오류 발생");
            e.printStackTrace();
        }
    }
}