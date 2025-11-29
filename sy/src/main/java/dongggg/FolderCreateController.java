package dongggg;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import dongggg.HoverEffects;

public class FolderCreateController {

    @FXML
    private TextField folderNameField;

    @FXML
    private Label statusLabel;

    @FXML
    private Button cancelButton;

    @FXML
    private Button createButton;

    // 🔥 FXML 로딩이 끝난 뒤 자동으로 호출되는 메소드
    @FXML
    public void initialize() {
        // 여기까지 안 들어오면 hover는 절대 안 먹음
        if (cancelButton != null) {
            HoverEffects.installPurpleHover(cancelButton);
        }
        if (createButton != null) {
            HoverEffects.installPurpleHover(createButton);
        }
    }

    @FXML
    private void onBack() {
        App.showMainView();
    }

    @FXML
    private void onCreate() {
        String name = folderNameField.getText() != null ? folderNameField.getText().trim() : "";

        if (name.isEmpty()) {
            showStatus("폴더 이름을 입력해주세요.");
            return;
        }

        Folder folder = new Folder(name);
        FolderRepository.insert(folder);
        App.showMainView();
    }

    private void showStatus(String msg) {
        if (statusLabel != null) {
            statusLabel.setText(msg);
        }
        System.out.println("[FolderCreate] " + msg);
    }
}