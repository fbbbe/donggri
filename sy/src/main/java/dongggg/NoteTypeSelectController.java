package dongggg;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

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
        // 💜 노트 타입 선택 버튼 hover 효과 적용
        if (normalButton != null) {
            HoverEffects.installPurpleHover(normalButton);
        }
        if (conceptButton != null) {
            HoverEffects.installYellowHover(conceptButton);
        }
    }

    /** 🔥 일반 노트 작성 */
    @FXML
    private void onNormalNote() {
        App.showNoteEditor(null);
    }

    /** 🔥 개념 노트 작성 */
    @FXML
    private void onConceptNote() {
        App.showConceptNoteEditor(null);
    }

    /** 🔥 뒤로가기 → 대시보드 */
    @FXML
    private void onBack() {
        App.showMainView();
    }
}
