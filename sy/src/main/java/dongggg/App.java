package dongggg;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {

    private static Scene scene;
    private static final String MAIN_STYLESHEET =
            App.class.getResource("styles.css").toExternalForm();
    private static final Color ROOT_BACKGROUND = Color.web("#fdf4ff");

    @Override
    public void start(Stage stage) throws IOException {
        Database.init();
        NoteRepository.ensureSampleData();

        FXMLLoader loader = new FXMLLoader(App.class.getResource("main-view.fxml"));
        Parent root = loader.load();

        scene = new Scene(root, 1200, 720);
        reloadStylesheet();

        stage.setTitle("동그리 노트");
        stage.setScene(scene);
        stage.show();
    }

    public static Scene getScene() {
        return scene;
    }

    public static void showMainView() {
        try {
            FXMLLoader loader = new FXMLLoader(App.class.getResource("main-view.fxml"));
            Parent root = loader.load();
            scene.setRoot(root);
            reloadStylesheet();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void showNoteEditor(Note note) {
        try {
            FXMLLoader loader = new FXMLLoader(App.class.getResource("note-detail-view.fxml"));
            Parent root = loader.load();

            NoteDetailController controller = loader.getController();
            controller.setNote(note);

            scene.setRoot(root);
            reloadStylesheet();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void showConceptNoteEditor(Note note) {
        try {
            FXMLLoader loader = new FXMLLoader(App.class.getResource("concept-note-view.fxml"));
            Parent root = loader.load();

            ConceptNoteController controller = loader.getController();
            controller.setNote(note);

            scene.setRoot(root);
            reloadStylesheet();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void showNoteTypeSelect() {
        try {
            FXMLLoader loader = new FXMLLoader(App.class.getResource("note-type-select-view.fxml"));
            Parent root = loader.load();
            scene.setRoot(root);
            reloadStylesheet();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 🔥🔥 여기! 대시보드 전환 기능
    public static void showDashboardView() {
        try {
            System.out.println("🔥 App.showDashboardView() called");

            FXMLLoader loader = new FXMLLoader(App.class.getResource("dashboard-view.fxml"));
            Parent newRoot = loader.load();

            if (scene == null) {
                System.out.println("❌ Scene is NULL!");
                return;
            }

            // ⭐ 현재 root를 완전히 교체 (문제되는 setRoot 중복 버그 제거)
            scene.setRoot(newRoot);

            // ⭐ CSS 재적용
            scene.getStylesheets().clear();
            scene.getStylesheets().add(MAIN_STYLESHEET);

            System.out.println("🔥 Dashboard root set successfully!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    // 🔥 시험 시작 화면 전환 기능
    public static void showQuizStartView() {
        try {
            FXMLLoader loader = new FXMLLoader(App.class.getResource("quiz-start-view.fxml"));
            Parent root = loader.load();
            scene.setRoot(root);
            reloadStylesheet();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void reloadStylesheet() {
        if (scene != null) {
            scene.getStylesheets().clear();
            scene.getStylesheets().add(MAIN_STYLESHEET);
            scene.setFill(ROOT_BACKGROUND);
        }
    }


    public static void main(String[] args) {
        launch();
    }
}
