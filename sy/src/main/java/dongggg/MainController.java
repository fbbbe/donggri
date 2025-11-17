package dongggg;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

public class MainController {

    @FXML
    private TextField searchField;

    @FXML
    private Button newFolderButton;

    @FXML
    private Button newNoteButton;

    @FXML
    private GridPane folderGrid;

    @FXML
    private VBox recentNotesBox;

    @FXML
    public void initialize() {
        System.out.println("MainController initialize");
        // TODO: 여기서 나중에 폴더/최근 노트 DB에서 불러와서
        // folderGrid / recentNotesBox 에 카드 추가하면 됨.
    }

    @FXML
    private void onNewFolder() {
        System.out.println("새 폴더 버튼 클릭");
    }

    @FXML
    private void onNewNote() {
        System.out.println("새 노트 버튼 클릭");
    }
}