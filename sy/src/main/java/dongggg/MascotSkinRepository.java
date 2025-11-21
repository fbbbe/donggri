package dongggg;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MascotSkinRepository {

    public static int getSelectedSkinThreshold() {
        String sql = "SELECT selected_skin FROM donggri_status WHERE id = 1";
        try (Connection conn = Database.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql);
                ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) {
                return rs.getInt("selected_skin");
            }
        } catch (SQLException e) {
            System.out.println("[DB] 선택된 스킨 조회 실패");
            e.printStackTrace();
        }
        return 1;
    }

    public static void selectSkin(int threshold, int currentLevel) {
        String sql = "UPDATE donggri_status SET selected_skin = ? WHERE id = 1";
        try (Connection conn = Database.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, threshold);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("[DB] 선택 스킨 저장 실패");
            e.printStackTrace();
        }
    }

    public static List<MascotSkin> getSkinsWithState(int currentLevel) {
        int selected = getSelectedSkinThreshold();
        List<MascotSkin> list = new ArrayList<>();

        String sql = """
                SELECT level_threshold, image_path
                FROM mascot_skins
                ORDER BY level_threshold ASC
                """;

        try (Connection conn = Database.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql);
                ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                int threshold = rs.getInt("level_threshold");
                String path = rs.getString("image_path");
                boolean unlocked = currentLevel >= threshold;
                boolean isSelected = unlocked && (threshold == selected);
                list.add(new MascotSkin(threshold, path, unlocked, isSelected));
            }
        } catch (SQLException e) {
            System.out.println("[DB] 마스코트 스킨 목록 조회 실패");
            e.printStackTrace();
        }

        return list;
    }

    public static MascotSkin resolveSkinForLevel(int currentLevel) {
        List<MascotSkin> skins = getSkinsWithState(currentLevel);
        // 우선 선택된 스킨이 있고 해금된 경우 우선
        for (MascotSkin skin : skins) {
            if (skin.isSelected() && skin.isUnlocked()) {
                return skin;
            }
        }
        // 선택 스킨이 잠금이면 가장 높은 해금 스킨
        MascotSkin fallback = null;
        for (MascotSkin skin : skins) {
            if (skin.isUnlocked()) {
                fallback = skin;
            } else {
                break;
            }
        }
        if (fallback != null) return fallback;
        return new MascotSkin(1, "dongggg/images/dong1.png", true, true);
    }
}
