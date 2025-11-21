package dongggg;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.StackPane;

import java.util.List;

public class WardrobeController {

    @FXML private Label currentLevelLabel;
    @FXML private ImageView currentSkinImage;
    @FXML private Label currentSkinNameLabel;
    @FXML private Label currentSkinDescLabel;
    @FXML private HBox skinListBox;
    @FXML private Button confirmButton;

    private int currentLevel = 1;

    @FXML
    private void initialize() {
        DonggriLevelInfo info = DonggriRepository.getLevelInfo();
        currentLevel = info.getCurrentLevel();
        if (currentLevelLabel != null) {
            currentLevelLabel.setText("현재 레벨: " + currentLevel);
        }
        refill();
    }

    private void refill() {
        List<MascotSkin> skins = MascotSkinRepository.getSkinsWithState(currentLevel);
        MascotSkin active = MascotSkinRepository.resolveSkinForLevel(currentLevel);

        if (currentSkinImage != null) {
            currentSkinImage.setImage(loadImage(active.getImagePath()));
        }
        if (currentSkinNameLabel != null) {
            currentSkinNameLabel.setText("Lv." + active.getLevelThreshold() + " 동그리");
        }
        if (currentSkinDescLabel != null) {
            currentSkinDescLabel.setText("동그리 스타일");
        }

        if (skinListBox != null) {
            skinListBox.getChildren().clear();
            for (MascotSkin skin : skins) {
                skinListBox.getChildren().add(buildSkinCard(skin, skin.getLevelThreshold() == active.getLevelThreshold()));
            }
        }
    }

    private StackPane buildSkinCard(MascotSkin skin, boolean selected) {
        boolean unlocked = skin.isUnlocked();

        StackPane card = new StackPane();
        card.getStyleClass().add("wardrobe-skin-card");
        if (selected && unlocked) card.getStyleClass().add("selected");
        if (!unlocked) card.getStyleClass().add("locked");

        VBox box = new VBox(6);
        box.setAlignment(javafx.geometry.Pos.CENTER);

        ImageView img = new ImageView(loadImage(skin.getImagePath()));
        img.setFitWidth(80);
        img.setFitHeight(80);
        img.setPreserveRatio(true);
        img.getStyleClass().add("wardrobe-skin-thumb");

        Label name = new Label("Lv." + skin.getLevelThreshold());
        name.getStyleClass().add("wardrobe-skin-name");

        box.getChildren().addAll(img, name);
        card.getChildren().add(box);

        if (!unlocked) {
            VBox lockOverlay = new VBox(4);
            lockOverlay.setAlignment(javafx.geometry.Pos.CENTER);
            Label lockIcon = new Label("🔒");
            lockIcon.getStyleClass().add("wardrobe-lock-icon");
            Label lockText = new Label("Lv." + skin.getLevelThreshold());
            lockText.getStyleClass().add("wardrobe-lock-text");
            lockOverlay.getChildren().addAll(lockIcon, lockText);
            card.getChildren().add(lockOverlay);
        } else {
            card.setOnMouseClicked(e -> {
                MascotSkinRepository.selectSkin(skin.getLevelThreshold(), currentLevel);
                refill();
            });
        }

        return card;
    }

    private Image loadImage(String path) {
        try {
            Image img = new Image(path, true);
            if (img.isError()) {
                return new Image("dongggg/images/dong1.png");
            }
            return img;
        } catch (Exception e) {
            return new Image("dongggg/images/dong1.png");
        }
    }

    @FXML
    private void onBack() {
        App.showDashboardView();
    }

    @FXML
    private void onConfirm() {
        App.showDashboardView();
    }
}
