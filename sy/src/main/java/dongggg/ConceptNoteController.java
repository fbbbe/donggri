package dongggg;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.util.List;

public class ConceptNoteController {

    @FXML
    private BorderPane root;

    @FXML
    private TextField titleField;

    @FXML
    private ListView<ConceptPair> pairListView;

    @FXML
    private TextField termField;

    @FXML
    private TextArea explanationArea;

    @FXML
    private Label statusLabel;

    private final ObservableList<ConceptPair> pairs = FXCollections.observableArrayList();

    private Note note;
    private boolean isNewNote = true;
    private boolean suppressSelectionEvent = false;

    @FXML
    public void initialize() {
        pairListView.setItems(pairs);

        // 리스트에서 선택이 바뀌면 오른쪽 입력 칸에 반영
        pairListView.getSelectionModel().selectedItemProperty().addListener((obs, oldPair, newPair) -> {
            if (suppressSelectionEvent)
                return;
            showPair(newPair);
        });
    }

    // MainController에서 호출
    public void setNote(Note note) {
        if (note == null) {
            // 새 개념 노트
            this.note = new Note("", "", "CONCEPT");
            isNewNote = true;
            titleField.clear();
            pairs.clear();
            statusLabel.setText("새 개념 노트");
        } else {
            this.note = note;
            isNewNote = false;
            titleField.setText(note.getTitle());

            // 기존 개념 페어 로드
            List<ConceptPair> loaded = ConceptPairRepository.findByNoteId(note.getId());
            pairs.setAll(loaded);
            statusLabel.setText("개념 노트 편집");
        }

        if (pairs.isEmpty()) {
            onAddPair(); // 기본 항목 하나 만들어두기
        } else {
            pairListView.getSelectionModel().selectFirst();
        }
    }

    private void showPair(ConceptPair pair) {
        if (pair == null) {
            termField.clear();
            explanationArea.clear();
            return;
        }
        termField.setText(pair.getTerm());
        explanationArea.setText(pair.getExplanation());
    }

    @FXML
    private void onAddPair() {
        ConceptPair newPair = new ConceptPair("", "");
        pairs.add(newPair);

        suppressSelectionEvent = true;
        pairListView.getSelectionModel().select(newPair);
        suppressSelectionEvent = false;

        showPair(newPair);
    }

    @FXML
    private void onDeletePair() {
        ConceptPair selected = pairListView.getSelectionModel().getSelectedItem();
        if (selected == null)
            return;

        pairs.remove(selected);

        if (pairs.isEmpty()) {
            onAddPair();
        } else {
            pairListView.getSelectionModel().selectFirst();
        }
    }

    // 현재 오른쪽 입력 내용을 선택된 ConceptPair 객체에 반영
    private void syncCurrentFieldsToSelectedPair() {
        ConceptPair selected = pairListView.getSelectionModel().getSelectedItem();
        if (selected == null)
            return;

        selected.setTerm(termField.getText().trim());
        selected.setExplanation(explanationArea.getText().trim());
    }

    @FXML
    private void onSaveAll() {
        String title = titleField.getText().trim();
        if (title.isEmpty()) {
            showAlert("제목을 입력해주세요.");
            return;
        }

        // 입력 중인 항목 동기화
        syncCurrentFieldsToSelectedPair();

        // 모든 항목이 완전 빈 값이면 저장하지 않도록 필터링
        pairs.removeIf(p -> (p.getTerm() == null || p.getTerm().isBlank())
                && (p.getExplanation() == null || p.getExplanation().isBlank()));

        if (pairs.isEmpty()) {
            showAlert("최소 한 개 이상의 개념/설명을 입력해주세요.");
            return;
        }

        note.setTitle(title);

        if (isNewNote) {
            NoteRepository.insert(note); // id 세팅됨
            isNewNote = false;
        } else {
            // 개념 노트도 제목 수정 가능하도록
            NoteRepository.update(note);
        }

        // note_id 기준으로 기존 페어 싹 지우고 다시 저장
        ConceptPairRepository.replaceAllForNote(note.getId(), pairs);

        closeWindow();
    }

    @FXML
    private void onCancel() {
        closeWindow();
    }

    private void closeWindow() {
        Stage stage = (Stage) root.getScene().getWindow();
        stage.close();
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("알림");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}