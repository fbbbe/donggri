package dongggg;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DonggriRepository {

    public static DonggriStatus getStatus() {
        String sql = "SELECT cumulative_score, cumulative_correct FROM donggri_status WHERE id = 1";

        try (Connection conn = Database.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql);
                ResultSet rs = pstmt.executeQuery()) {

            if (rs.next()) {
                return new DonggriStatus(rs.getInt("cumulative_score"), rs.getInt("cumulative_correct"));
            }

        } catch (SQLException e) {
            System.out.println("[DB] donggri_status 조회 중 오류 발생");
            e.printStackTrace();
        }

        // 조회 실패 시 기본값
        return new DonggriStatus(0, 0);
    }

    public static void updateStatus(DonggriStatus status) {
        String sql = "UPDATE donggri_status SET cumulative_score = ?, cumulative_correct = ? WHERE id = 1";

        try (Connection conn = Database.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, status.getCumulativeScore());
            pstmt.setInt(2, status.getCumulativeCorrect());
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("[DB] donggri_status 업데이트 중 오류 발생");
            e.printStackTrace();
        }
    }

    public static DonggriLevelInfo getLevelInfo() {
        DonggriStatus status = getStatus();
        List<LevelRequirement> requirements = fetchLevelRequirements();

        int currentLevel = 1;
        Integer nextLevel = null;
        int nextScoreRequirement = 0;
        int nextCorrectRequirement = 0;

        for (LevelRequirement req : requirements) {
            if (status.getCumulativeScore() >= req.requiredScore
                    && status.getCumulativeCorrect() >= req.requiredCorrect) {
                currentLevel = req.level;
            } else {
                nextLevel = req.level;
                nextScoreRequirement = req.requiredScore;
                nextCorrectRequirement = req.requiredCorrect;
                break;
            }
        }

        double progressRatio;
        int remainingScore;
        int remainingCorrect;

        if (nextLevel == null) {
            progressRatio = 1.0;
            remainingScore = 0;
            remainingCorrect = 0;
        } else {
            double scoreRatio = nextScoreRequirement == 0
                    ? 1.0
                    : Math.min(1.0, status.getCumulativeScore() / (double) nextScoreRequirement);

            double correctRatio = nextCorrectRequirement == 0
                    ? 1.0
                    : Math.min(1.0, status.getCumulativeCorrect() / (double) nextCorrectRequirement);

            progressRatio = Math.max(0.0, Math.min(1.0, (scoreRatio + correctRatio) / 2.0));

            remainingScore = Math.max(0, nextScoreRequirement - status.getCumulativeScore());
            remainingCorrect = Math.max(0, nextCorrectRequirement - status.getCumulativeCorrect());
        }

        return new DonggriLevelInfo(
                currentLevel,
                nextLevel,
                nextScoreRequirement,
                nextCorrectRequirement,
                remainingScore,
                remainingCorrect,
                progressRatio,
                status.getCumulativeScore(),
                status.getCumulativeCorrect());
    }

    private static List<LevelRequirement> fetchLevelRequirements() {
        List<LevelRequirement> list = new ArrayList<>();

        String sql = """
                SELECT level, required_cumulative_score, required_cumulative_correct
                FROM donggri_levels
                ORDER BY level ASC
                """;

        try (Connection conn = Database.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql);
                ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                list.add(new LevelRequirement(
                        rs.getInt("level"),
                        rs.getInt("required_cumulative_score"),
                        rs.getInt("required_cumulative_correct")));
            }

        } catch (SQLException e) {
            System.out.println("[DB] donggri_levels 조회 중 오류 발생");
            e.printStackTrace();
        }

        return list;
    }

    private record LevelRequirement(int level, int requiredScore, int requiredCorrect) {
    }
}
