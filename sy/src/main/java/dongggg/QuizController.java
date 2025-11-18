package dongggg;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.Alert;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Parent;

import java.util.List;
import java.util.ArrayList;

public class QuizController {

    @FXML private Label conceptLabel;
    @FXML private TextArea answerArea;
    @FXML private Label progressLabel;

    private final QuizService quizService = new QuizServiceImpl();
    private List<ConceptPair> quizList = new ArrayList<>();
    private List<String> userAnswers = new ArrayList<>();

    private int currentIndex = 0;

    public void initQuiz(int noteId) {

        // 🔥 실제 DB 문제 생성
        quizList = quizService.generateQuiz(noteId, 5);

        // 🔥 문제 없으면 샘플 3문제 자동 추가
        // 🔥 문제 없으면 샘플 3문제 자동 추가
        if (quizList == null || quizList.isEmpty()) {

            quizList = new ArrayList<>();

            quizList.add(new ConceptPair(0, noteId, "클래스(Class)", "객체를 만들기 위한 설계도", 0));
            quizList.add(new ConceptPair(0, noteId, "객체(Object)", "클래스로부터 생성된 실체", 0));
            quizList.add(new ConceptPair(0, noteId, "상속(Inheritance)", "부모 클래스 기능을 자식이 물려받는 것", 0));
        }


        currentIndex = 0;
        loadQuestion(currentIndex);
    }


    private void loadQuestion(int index) {
        ConceptPair cp = quizList.get(index);
        conceptLabel.setText(cp.getTerm());
        updateProgress();
    }

    private void updateProgress() {
        progressLabel.setText((currentIndex + 1) + " / " + quizList.size() + " 문제");
    }

    @FXML
    public void nextQuestion() {
        userAnswers.add(answerArea.getText());
        answerArea.clear();

        currentIndex++;

        if (currentIndex >= quizList.size()) {
            goToResult();
            return;
        }

        loadQuestion(currentIndex);
    }

    private void goToResult() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("quiz-result-view.fxml"));
            Parent root = loader.load();

            QuizResultController controller = loader.getController();
            controller.showResult(quizList, userAnswers);

            Stage stage = (Stage) conceptLabel.getScene().getWindow();
            stage.setScene(new Scene(root));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
