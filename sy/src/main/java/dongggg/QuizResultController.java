package dongggg;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.List;

public class QuizResultController {

    // 점수 요약 영역
    @FXML private Label scorePercentLabel;
    @FXML private Label scoreSummaryLabel;
    @FXML private ProgressBar scoreBar;

    // 문제별 카드들이 추가될 박스
    @FXML private VBox resultListBox;

    private Scene previousScene;

    /** 🔥 시험 결과 표시 */
    public void showResult(List<ConceptPair> quizList, List<String> userAnswers) {

        int total = quizList.size();
        int correctCount = 0;

        // 리스트 영역 초기화
        resultListBox.getChildren().clear();

        for (int i = 0; i < total; i++) {
            ConceptPair pair = quizList.get(i);
            String correct = pair.getExplanation();
            String user = userAnswers.get(i);

            boolean isCorrect = user.equalsIgnoreCase(correct);
            if (isCorrect) correctCount++;

            // 🔥 문제별 UI카드를 동적으로 생성
            resultListBox.getChildren().add(createResultCard(pair.getTerm(), correct, user, isCorrect));
        }

        // 🔥 점수 계산
        int scorePercent = (int) Math.round((correctCount * 100.0) / total);

        scorePercentLabel.setText(scorePercent + "%");
        scoreSummaryLabel.setText(correctCount + " / " + total + "개 정답");
        scoreBar.setProgress(scorePercent / 100.0);
    }

    /** 🔥 React 디자인 카드 하나 생성 */
    private VBox createResultCard(String concept, String correct, String user, boolean isCorrect) {

        VBox card = new VBox(8);
        card.setStyle(
            "-fx-background-color: white;" +
            "-fx-padding: 18;" +
            "-fx-background-radius: 22;" +
            "-fx-border-color: #e8dff5;" +
            "-fx-border-radius: 22;" +
            "-fx-border-width: 2;" +
            "-fx-effect: dropshadow(gaussian, rgba(168,85,221,0.12), 18, 0.25, 0, 4);"
        );

        Label conceptLabel = new Label("개념: " + concept);
        conceptLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: 600; -fx-text-fill: #a855dd;");

        Label correctLabel = new Label("정답: " + correct);
        correctLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #d9b5ff;");

        VBox userBox = new VBox();
        userBox.setStyle(
            "-fx-background-color: #f5f0fb;" +
            "-fx-padding: 12;" +
            "-fx-background-radius: 12;" +
            "-fx-border-color: #e8dff5;" +
            "-fx-border-radius: 12;"
        );
        Label userTitle = new Label("당신의 답변");
        userTitle.setStyle("-fx-font-size: 10px; -fx-text-fill: #d9b5ff;");
        Label userValue = new Label(user.isEmpty() ? "(답변 없음)" : user);
        userValue.setStyle("-fx-font-size: 13px; -fx-text-fill: #3b275b;");
        userBox.getChildren().addAll(userTitle, userValue);

        // 정오표시
        Label resultTag = new Label(isCorrect ? "✓ 정답" : "✗ 오답");
        resultTag.setStyle(
            isCorrect
                ? "-fx-background-color: #f5f0fb; -fx-text-fill: #a855dd; -fx-font-weight: 600; -fx-padding: 6 12; -fx-background-radius: 12;"
                : "-fx-background-color: #fee2e2; -fx-text-fill: #dc2626; -fx-font-weight: 600; -fx-padding: 6 12; -fx-background-radius: 12;"
        );

        card.getChildren().addAll(conceptLabel, correctLabel, userBox, resultTag);
        return card;
    }

    /** 🔥 이전 화면 저장 */
    public void setPreviousScene(Scene scene) {
        this.previousScene = scene;
    }

    /** 뒤로가기 버튼 */
    @FXML
    private void goBack() {
        try {
            Stage stage = (Stage) resultListBox.getScene().getWindow();
            if (previousScene != null) {
                stage.setScene(previousScene);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /** 대시보드로 이동 */
    @FXML
    private void goDashboard() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("dashboard-view.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) resultListBox.getScene().getWindow();

            Scene scene = new Scene(root, 1200, 720);
            scene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());

            stage.setScene(scene);
            stage.sizeToScene(); // 화면 재조정

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
