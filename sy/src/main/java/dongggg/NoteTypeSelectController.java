package dongggg;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;

/**
 * "+ 버튼"을 눌렀을 때 뜨는
 * "일반 노트 / 개념 노트 선택 화면" 담당 컨트롤러.
 */
public class NoteTypeSelectController {

    @FXML
    private Button normalButton;

    @FXML
    private Button conceptButton;

    @FXML
    public void initialize() {
        // 필요하면 여기서 버튼 상태 초기화 가능
    }

    /** 🔥 공용 Scene 전환 메서드 — App.scene이 아니라 현재 Stage 기준으로 root만 변경 */
    private void switchTo(String fxml) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
            Parent root = loader.load();

            // 현재 화면(Stage) 기준으로 Scene 재사용
            Stage stage = (Stage) normalButton.getScene().getWindow();
            Scene scene = stage.getScene();   // 기존 Scene 그대로

            scene.setRoot(root);              // root만 교체
            scene.getStylesheets().clear();
            scene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /** 🔥 일반 노트 작성 */
    @FXML
    private void onNormalNote() {
        switchTo("note-detail-view.fxml");
    }

    /** 🔥 개념 노트 작성 */
    @FXML
    private void onConceptNote() {
        switchTo("concept-note-view.fxml");
    }

    /** 🔥 뒤로가기 → 대시보드 */
    @FXML
    private void onBack() {
        switchTo("dashboard-view.fxml");
    }
}
