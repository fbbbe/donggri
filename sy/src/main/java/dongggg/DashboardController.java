package dongggg;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class DashboardController {

    @FXML private Button noteManageButton;
    @FXML private Button newNoteButton;
    @FXML private Button quizButton;

    private static final Duration HOVER_DURATION = Duration.millis(220);

    @FXML
    private void initialize() {
        installHoverAnimation(
                noteManageButton,
                Color.web("#7c3aed"), Color.web("#6d28d9"),
                Color.web("#8b5cf6"), Color.web("#7c3aed"),
                Color.TRANSPARENT,
                Color.web("#4c1dc5", 0.6)
        );
        installHoverAnimation(
                newNoteButton,
                Color.web("#ffffff", 0.9), Color.web("#ffffff", 0.9),
                Color.web("#e9d5ff", 0.6), Color.web("#e9d5ff", 0.6),
                Color.web("#e6dcff"),
                Color.web("#c0a6ff")
        );
        installHoverAnimation(
                quizButton,
                Color.web("#ffffff", 0.7), Color.web("#ffffff", 0.7),
                Color.web("#d3c4ff", 0.5), Color.web("#d3c4ff", 0.5),
                Color.web("#9276f9", 0.3),
                Color.web("#855aff", 0.7)
        );
    }

    /** 🔥 노트 관리 화면 이동 — Scene 방식 */
    @FXML
    private void goNoteManager() {
        switchScene("main-view.fxml");
    }

    /** 🔥 새 노트 작성 화면 이동 — Scene 방식 */
    @FXML
    private void goConceptNote() {
        switchScene("note-type-select-view.fxml");
    }

    /** 🔥 시험 시작 화면 이동 — Scene 방식 */
    @FXML
    private void goQuiz() {
        switchScene("quiz-start-view.fxml");
    }


    /** 중앙 공용: Scene 교체 함수 */
    private void switchScene(String fxml) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
            Parent root = loader.load();

            Stage stage = (Stage) noteManageButton.getScene().getWindow();
            Scene scene = new Scene(root, 1200, 720);

            scene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());
            stage.setScene(scene);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ===== 아래는 hover animation 그대로 유지 =====

    private void installHoverAnimation(Button button,
                                       Color baseBgStart, Color baseBgEnd,
                                       Color hoverBgStart, Color hoverBgEnd,
                                       Color baseBorder, Color hoverBorder) {
        if (button == null) return;

        final String baseStyle = button.getStyle() == null ? "" : button.getStyle();
        DoubleProperty progress = new SimpleDoubleProperty(0);

        progress.addListener((obs, oldVal, newVal) -> {
            double t = newVal.doubleValue();
            Color bg1 = baseBgStart.interpolate(hoverBgStart, t);
            Color bg2 = baseBgEnd.interpolate(hoverBgEnd, t);
            Color border = baseBorder.interpolate(hoverBorder, t);
            String background = toLinearGradient(bg1, bg2);

            button.setStyle(baseStyle
                    + "-fx-background-color: " + background + ";"
                    + "-fx-border-color: " + toCss(border) + ";");
        });

        button.setStyle(baseStyle
                + "-fx-background-color: " + toLinearGradient(baseBgStart, baseBgEnd) + ";"
                + "-fx-border-color: " + toCss(baseBorder) + ";");

        Timeline fadeIn = new Timeline(
                new KeyFrame(HOVER_DURATION, new KeyValue(progress, 1, Interpolator.EASE_BOTH)));
        Timeline fadeOut = new Timeline(
                new KeyFrame(HOVER_DURATION, new KeyValue(progress, 0, Interpolator.EASE_BOTH)));

        button.hoverProperty().addListener((obs, wasHover, isHover) -> {
            if (isHover) {
                fadeOut.stop();
                fadeIn.playFromStart();
            } else {
                fadeIn.stop();
                fadeOut.playFromStart();
            }
        });
    }

    private String toLinearGradient(Color start, Color end) {
        if (start.equals(end)) return toCss(start);
        return "linear-gradient(" + toCss(start) + ", " + toCss(end) + ")";
    }

    private String toCss(Color color) {
        return String.format(
                "rgba(%d,%d,%d,%.3f)",
                (int) (color.getRed() * 255),
                (int) (color.getGreen() * 255),
                (int) (color.getBlue() * 255),
                color.getOpacity()
        );
    }
}
