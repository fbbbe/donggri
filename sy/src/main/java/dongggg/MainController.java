package dongggg;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.List;

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

        // 샘플 노트 보장
        NoteRepository.ensureSampleData();

        // 최근 노트 목록 로드
        loadRecentNotes();
    }

    private void loadRecentNotes() {
        recentNotesBox.getChildren().clear();

        List<Note> notes = NoteRepository.findRecent(10);

        for (Note note : notes) {
            HBox card = createNoteCard(note);
            recentNotesBox.getChildren().add(card);
        }
    }

    private HBox createNoteCard(Note note) {
        HBox card = new HBox();
        card.setAlignment(Pos.CENTER_LEFT);
        card.setSpacing(8);
        card.getStyleClass().add("note-card");

        VBox textBox = new VBox();
        textBox.setSpacing(4);

        Label titleLabel = new Label(note.getTitle());
        titleLabel.getStyleClass().add("note-title");

        // 타입에 따라 태그 텍스트 변경
        String tagText = "일반 노트";
        if ("CONCEPT".equalsIgnoreCase(note.getType())) {
            tagText = "개념 노트";
        }

        Label tagLabel = new Label(tagText);
        tagLabel.getStyleClass().add("note-tag");

        String dateText = note.getUpdatedAt() != null ? note.getUpdatedAt() : "";
        Label dateLabel = new Label(dateText);
        dateLabel.getStyleClass().add("note-date");

        HBox metaBox = new HBox(8, tagLabel, dateLabel);
        textBox.getChildren().addAll(titleLabel, metaBox);

        card.getChildren().add(textBox);

        // 노트 타입에 따라 적절한 편집 화면 열기
        card.setOnMouseClicked(event -> {
            if ("CONCEPT".equalsIgnoreCase(note.getType())) {
                openConceptNoteEditor(note);
            } else {
                openNoteDetail(note);
            }
        });

        return card;
    }

    @FXML
    private void onNewFolder() {
        System.out.println("새 폴더 버튼 클릭 (추후 구현)");
    }

    // + 버튼 클릭 시: 일반 노트 / 개념 노트 선택
    @FXML
    private void onNewNote() {
        // 1) 버튼 타입 정의
        ButtonType normalBtn = new ButtonType("일반 노트", ButtonBar.ButtonData.OK_DONE);
        ButtonType conceptBtn = new ButtonType("개념 노트", ButtonBar.ButtonData.OTHER);
        ButtonType cancelBtn = new ButtonType("취소", ButtonBar.ButtonData.CANCEL_CLOSE);

        // 2) Alert 생성 (기본은 내용 없는 CONFIRMATION 다이얼로그)
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(null); // 상단 제목은 안 씀
        alert.setHeaderText(null); // 굵은 헤더 텍스트 제거
        alert.setContentText(null); // 기본 contentText도 안 씀
        alert.getButtonTypes().setAll(normalBtn, conceptBtn, cancelBtn);

        // 3) DialogPane에 CSS 적용
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(
                App.class.getResource("styles.css").toExternalForm());
        dialogPane.getStyleClass().add("note-type-dialog");

        // 4) 다이얼로그 안에 들어갈 제목/설명 Label 구성
        Label titleLabel = new Label("어떤 노트를 만들까요?");
        titleLabel.getStyleClass().add("note-type-title");

        Label subLabel = new Label("나중에 언제든지 편집할 수 있어요.");
        subLabel.getStyleClass().add("note-type-subtitle");

        VBox contentBox = new VBox(6, titleLabel, subLabel);
        dialogPane.setContent(contentBox);

        // 5) 버튼들에 CSS 클래스 부여 (lookupButton으로 실제 Button 얻기)
        Button normalButton = (Button) dialogPane.lookupButton(normalBtn);
        normalButton.setText("✏️ 일반 노트");
        normalButton.getStyleClass().addAll("note-type-choice", "note-type-normal");

        Button conceptButton = (Button) dialogPane.lookupButton(conceptBtn);
        conceptButton.setText("📚 개념 노트");
        conceptButton.getStyleClass().addAll("note-type-choice", "note-type-concept");

        Button cancelButton = (Button) dialogPane.lookupButton(cancelBtn);
        cancelButton.getStyleClass().add("note-type-cancel");

        // 6) 모달로 보여주고 결과에 따라 분기
        alert.initOwner(searchField.getScene().getWindow());
        alert.showAndWait().ifPresent(buttonType -> {
            if (buttonType == normalBtn) {
                // 👉 새 "일반 노트" 편집창 열기
                openNoteDetail(null);
            } else if (buttonType == conceptBtn) {
                // 👉 새 "개념 노트" 편집창 열기
                openConceptNoteEditor(null);
            } else {
                // 취소 눌렀을 때는 아무 것도 안 함
            }
        });
    }

    private void openNoteDetail(Note note) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    App.class.getResource("note-detail-view.fxml"));

            Parent root = loader.load();

            NoteDetailController controller = loader.getController();
            controller.setNote(note); // null이면 새 노트 모드

            Scene scene = new Scene(root, 600, 500);
            scene.getStylesheets().add(
                    App.class.getResource("styles.css").toExternalForm());

            Stage stage = new Stage();
            stage.setTitle("노트 편집");
            stage.setScene(scene);
            stage.initOwner(searchField.getScene().getWindow());

            stage.showAndWait();

            loadRecentNotes();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 개념 노트 편집 화면 열기
    private void openConceptNoteEditor(Note note) {
        try {
            // 1) FXML 경로 확인
            var url = App.class.getResource("concept-note-view.fxml");
            if (url == null) {
                // FXML을 못 찾으면 바로 알림
                showErrorDialog("concept-note-view.fxml 파일을 찾을 수 없습니다.\n" +
                        "경로: src/main/resources/dongggg/concept-note-view.fxml 을 확인해주세요.");
                return;
            }

            // 2) FXML 로더 생성
            FXMLLoader loader = new FXMLLoader(url);

            // 3) 루트 노드 로드
            Parent root = loader.load();

            // 4) 컨트롤러 가져와서 편집할 노트 주입
            ConceptNoteController controller = loader.getController();
            controller.setNote(note); // null이면 새 개념 노트 모드

            // 5) Scene / CSS 설정
            Scene scene = new Scene(root, 900, 600);
            scene.getStylesheets().add(
                    App.class.getResource("styles.css").toExternalForm());

            // 6) 새 Stage 생성해서 모달로 띄우기
            Stage stage = new Stage();
            stage.setTitle("개념 노트 편집");
            stage.setScene(scene);
            stage.initOwner(searchField.getScene().getWindow());

            stage.showAndWait();

            // 7) 창이 닫힌 후 목록을 새로고침
            loadRecentNotes();

        } catch (Exception e) {
            e.printStackTrace(); // 터미널에도 이유 출력

            // 사용자에게도 간단히 이유를 보여줌
            showErrorDialog("개념 노트 화면을 여는 중 오류가 발생했습니다.\n\n"
                    + e.getClass().getSimpleName() + ": " + e.getMessage());
        }
    }

    // 공통 에러 알림창
    private void showErrorDialog(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("오류");
        alert.setHeaderText("화면을 여는 중 오류가 발생했습니다.");
        alert.setContentText(message);

        alert.getDialogPane().getStylesheets().add(
                App.class.getResource("styles.css").toExternalForm());
        alert.getDialogPane().getStyleClass().add("note-type-dialog"); // 배경만 재활용

        alert.initOwner(searchField.getScene().getWindow());
        alert.showAndWait();
    }
}