package dongggg;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.util.List;

public class QuizModeSelectController {

    @FXML private Label totalCountLabel;
    @FXML private Label totalTimeLabel;
    @FXML private Slider worstCountSlider;
    @FXML private Label worstCountValueLabel;
    @FXML private Label worstSelectedCountLabel;
    @FXML private Label worstTimeLabel;

    private List<Note> selectedNotes = new ArrayList<>();
    private int totalQuestions = 0;

    // QuizStart 화면의 Scene 저장
    private Scene previousScene;

    public void setPreviousScene(Scene scene) {
        this.previousScene = scene;
    }

    public void setSelectedNotes(List<Note> notes) {
        this.selectedNotes = (notes != null) ? notes : new ArrayList<>();
        updateCounts();
    }

    @FXML
    private void initialize() {
        worstCountSlider.valueProperty().addListener((obs, o, n) -> updateWorstCountDisplay());
    }

    private void updateCounts() {
        List<Integer> ids = selectedNotes.stream().map(Note::getId).toList();
        totalQuestions = ConceptPairRepository.countByNoteIds(ids);

        totalCountLabel.setText(totalQuestions + "문제");
        totalTimeLabel.setText(estimateMinutes(totalQuestions) + "분");

        int max = Math.max(1, totalQuestions);
        int defaultWorst = Math.min(10, max);
        worstCountSlider.setMax(max);
        worstCountSlider.setValue(defaultWorst);

        updateWorstCountDisplay();
    }

    private void updateWorstCountDisplay() {
        int count = (int) Math.round(worstCountSlider.getValue());
        worstCountValueLabel.setText(count + "");
        worstSelectedCountLabel.setText(count + "문제");
        worstTimeLabel.setText(estimateMinutes(count) + "분");
    }

    private int estimateMinutes(int questions) {
        return Math.max(1, (int) Math.round(questions * 1.5));
    }

    @FXML private void onQuickSelect5() { worstCountSlider.setValue(Math.min(5, worstCountSlider.getMax())); }
    @FXML private void onQuickSelect10() { worstCountSlider.setValue(Math.min(10, worstCountSlider.getMax())); }
    @FXML private void onQuickSelect15() { worstCountSlider.setValue(Math.min(15, worstCountSlider.getMax())); }
    @FXML private void onQuickSelect20() { worstCountSlider.setValue(Math.min(20, worstCountSlider.getMax())); }

    @FXML
    private void onStartAll() {
        if (totalQuestions <= 0) return;
        startQuiz(QuizService.QuizMode.ALL, totalQuestions);
    }

    @FXML
    private void onStartWorst() {
        if (totalQuestions <= 0) return;
        int count = (int) worstCountSlider.getValue();
        startQuiz(QuizService.QuizMode.WORST, count);
    }

    // 뒤로가기 → QuizStart 화면으로 이동
    @FXML
    private void goBack() {
        try {
            Stage stage = (Stage) totalCountLabel.getScene().getWindow();
            if (previousScene != null) {
                stage.setScene(previousScene);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void startQuiz(QuizService.QuizMode mode, int limit) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("quiz-view.fxml"));
            Parent root = loader.load();

            QuizController controller = loader.getController();
            controller.initQuiz(selectedNotes, mode, limit);
            controller.setPreviousScene(worstCountSlider.getScene());

            Stage stage = App.getStage();
            stage.setScene(new Scene(root, 1200, 720));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
