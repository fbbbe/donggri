package dongggg;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {

    private static final String DB_URL = "jdbc:sqlite:donggri.db";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }

    public static void init() {
        try (Connection conn = getConnection();
                Statement stmt = conn.createStatement()) {

            // notes 테이블: type 추가 (NORMAL / CONCEPT)
            String createNotesTable = """
                    CREATE TABLE IF NOT EXISTS notes (
                        id INTEGER PRIMARY KEY AUTOINCREMENT,
                        title TEXT NOT NULL,
                        content TEXT,
                        type TEXT NOT NULL DEFAULT 'NORMAL',
                        created_at TEXT DEFAULT (datetime('now')),
                        updated_at TEXT DEFAULT (datetime('now'))
                    );
                    """;

            /*
             * • id : 기본키
             * • title : 노트 제목
             * • content : 노트 내용
             * • created_at, updated_at : 생성/수정 시간 (일단 TEXT로 둠)
             */

            stmt.execute(createNotesTable);

            // 개념-설명 페어 테이블
            String createConceptPairs = """
                    CREATE TABLE IF NOT EXISTS concept_pairs (
                        id INTEGER PRIMARY KEY AUTOINCREMENT,
                        note_id INTEGER NOT NULL,
                        term TEXT NOT NULL,
                        explanation TEXT,
                        sort_order INTEGER DEFAULT 0
                    );
                    """;

            /*
             * • id : 기본키
             * • title : 노트 제목
             * • term : 개념 노트의 개념 부분
             * • explanation : 개념 노트의 설명 부분
             */
            stmt.execute(createConceptPairs);

            System.out.println("[DB] notes / concept_pairs 테이블 초기화 완료");
        } catch (SQLException e) {
            System.out.println("[DB] 초기화 중 오류 발생");
            e.printStackTrace();
        }
    }
}