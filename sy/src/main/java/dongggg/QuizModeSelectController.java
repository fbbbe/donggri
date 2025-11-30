package dongggg;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import java.util.ArrayList;
import java.util.List;

public class QuizModeSelectController {

    @FXML
    private Label totalCountLabel;
    @FXML
    private Label totalTimeLabel;
    @FXML
    private Slider worstCountSlider;
    @FXML
    private Label worstCountValueLabel;
    @FXML
    private Label worstSelectedCountLabel;
    @FXML
    private Label worstTimeLabel;
    @FXML
    private Button btn5;
    @FXML
    private Button btn10;
    @FXML
    private Button btn15;
    @FXML
    private Button btn20;
    @FXML
    private Button starta;
    @FXML
    private Button startb;

    private List<Note> selectedNotes = new ArrayList<>();
    private int totalQuestions = 0;
    private int maxSelectableQuestions = 1;
    private boolean singleQuestionMode = false;

    // QuizStart 화면의 Root 저장
    private Parent previousRoot;

    public void setPreviousRoot(Parent root) {
        this.previousRoot = root;
    }

    public void setSelectedNotes(List<Note> notes) {
        this.selectedNotes = (notes != null) ? notes : new ArrayList<>();
        updateCounts();
    }

    @FXML
    private void initialize() {
        worstCountSlider.setMin(1);
        worstCountSlider.setMajorTickUnit(1);
        worstCountSlider.setMinorTickCount(0);
        worstCountSlider.setBlockIncrement(1);
        worstCountSlider.setSnapToTicks(true);
        worstCountSlider.valueProperty().addListener((obs, o, n) -> handleWorstSliderChange(n.doubleValue()));
    }

    private void updateCounts() {
        List<Integer> ids = selectedNotes.stream().map(Note::getId).toList();
        totalQuestions = ConceptPairRepository.countByNoteIds(ids);

        totalCountLabel.setText(totalQuestions + "문제");
        totalTimeLabel.setText(estimateMinutes(totalQuestions) + "분");

        maxSelectableQuestions = Math.max(1, totalQuestions);
        singleQuestionMode = totalQuestions == 1;

        double sliderMax = singleQuestionMode ? 2.0 : maxSelectableQuestions;
        int defaultWorst = Math.min(10, maxSelectableQuestions);

        worstCountSlider.setMax(sliderMax);
        setSliderToCount(defaultWorst);
        worstCountSlider.setDisable(totalQuestions <= 0);

        updateWorstCountDisplay();
    }

    private void updateWorstCountDisplay() {
        updateWorstCountDisplay(getSelectedWorstCount());
    }

    private void updateWorstCountDisplay(int count) {
        int clamped = clampToQuestionLimit(count);
        worstCountValueLabel.setText(clamped + "");
        worstSelectedCountLabel.setText(clamped + "문제");
        worstTimeLabel.setText(estimateMinutes(clamped) + "분");
    }

    private void handleWorstSliderChange(double newValue) {
        if (singleQuestionMode) {
            double pinnedValue = worstCountSlider.getMax();
            if (Math.abs(newValue - pinnedValue) > 0.0001) {
                worstCountSlider.setValue(pinnedValue);
                return;
            }
            updateWorstCountDisplay(1);
            return;
        }

        double snapped = snapSliderValue(newValue);
        if (Math.abs(snapped - newValue) > 0.0001) {
            worstCountSlider.setValue(snapped);
            return;
        }

        updateWorstCountDisplay((int) snapped);
    }

    private double snapSliderValue(double value) {
        double min = worstCountSlider.getMin();
        double max = worstCountSlider.getMax();
        double snapped = Math.round(value);
        if (snapped < min)
            snapped = min;
        if (snapped > max)
            snapped = max;
        return snapped;
    }

    private void setSliderToCount(int desiredCount) {
        if (singleQuestionMode) {
            worstCountSlider.setValue(worstCountSlider.getMax());
            return;
        }

        int clamped = clampToQuestionLimit(desiredCount);
        worstCountSlider.setValue(clamped);
    }

    private int getSelectedWorstCount() {
        int count = (int) Math.round(worstCountSlider.getValue());
        return clampToQuestionLimit(count);
    }

    private int clampToQuestionLimit(int value) {
        if (value < 1)
            return 1;
        if (value > maxSelectableQuestions)
            return maxSelectableQuestions;
        return value;
    }

    private int estimateMinutes(int questions) {
        return Math.max(1, (int) Math.round(questions * 1.5));
    }

    @FXML
    private void onQuickSelect5() {
        quickSelect(5, btn5);
    }

    @FXML
    private void onQuickSelect10() {
        quickSelect(10, btn10);
    }

    @FXML
    private void onQuickSelect15() {
        quickSelect(15, btn15);
    }

    @FXML
    private void onQuickSelect20() {
        quickSelect(20, btn20);
    }

    private void quickSelect(int count, Button button) {
        setSliderToCount(count);
        updateQuickSelectStyles(button);
    }

    @FXML
    private void onStartAll() {
        if (totalQuestions <= 0)
            return;
        startQuiz(QuizService.QuizMode.ALL, totalQuestions);
    }

    @FXML
    private void onStartWorst() {
        if (totalQuestions <= 0)
            return;
        int count = getSelectedWorstCount();
        startQuiz(QuizService.QuizMode.WORST, count);
    }

    // 뒤로가기 → QuizStart 화면으로 이동
    @FXML
    private void goBack() {
        try {
            if (previousRoot != null) {
                App.swapRootKeepingState(previousRoot);
            } else {
                App.showQuizStartView();
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
            controller.setPreviousRoot(App.getScene().getRoot());

            App.swapRootKeepingState(root);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateQuickSelectStyles(Button activeButton) {
        Button[] buttons = { btn5, btn10, btn15, btn20 };

        for (Button b : buttons) {
            b.getStyleClass().removeAll("quiz-pill-primary", "quiz-pill-outline");

            if (b == activeButton)
                b.getStyleClass().add("quiz-pill-primary");
            else
                b.getStyleClass().add("quiz-pill-outline");
        }
    }

}
