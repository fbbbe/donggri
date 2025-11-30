package dongggg;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.Effect;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.SVGPath;
import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.util.Duration;
import javafx.scene.layout.Pane;
import java.util.Random;

import java.util.ArrayList;
import java.util.List;

public class WardrobeController {

    @FXML
    private Label currentLevelLabel;
    @FXML
    private ImageView currentSkinImage;
    @FXML
    private Label currentSkinNameLabel;
    @FXML
    private Label currentSkinDescLabel;
    @FXML
    private HBox skinListBox;
    @FXML
    private Button confirmButton;
    @FXML
    private Button back;
    @FXML
    private Pane effectLayer;

    private final Random random = new Random();
    // lucide heart 아이콘 path
    private static final String HEART_PATH = "M2 9.5a5.5 5.5 0 0 1 9.591-3.676.56.56 0 0 0 .818 0 " +
            "A5.49 5.49 0 0 1 22 9.5c0 2.29-1.5 4-3 5.5 " +
            "l-5.492 5.313a2 2 0 0 1-3 .019L5 15 " +
            "c-1.5-1.5-3-3.2-3-5.5";
    private static final String[] HEART_GRADIENTS = new String[] {
            "linear-gradient(from 0% 0% to 0% 100%, #ff7aa2 0%, #ffd07f 100%)",
            "linear-gradient(from 0% 0% to 0% 100%, #ff9ec7 0%, #ffe780 100%)",
            "linear-gradient(from 0% 0% to 0% 100%, #ff84a5 0%, #ffc6a8 100%)",
            "linear-gradient(from 0% 0% to 0% 100%, #ff7e80 0%, #ffd166 100%)"
    };

    private int currentLevel = 1;
    private static final List<String> SKIN_NAMES = List.of(
            "동그리",
            "외계인 동그리",
            "토마토 동그리",
            "일상복 동그리",
            "곰돌이 동그리",
            "신사 동그리",
            "공대생 동그리",
            "감기 동그리",
            "해파리 동그리",
            "학생 동그리",
            "잠자는 동그리",
            "유치원생 동그리",
            "농구 동그리",
            "도둑 동그리",
            "짱구 동그리",
            "하츄핑 동그리",
            "메타몽 동그리",
            "경찰 동그리",
            "코난 동그리",
            "음식 동그리",
            "졸업 동그리");

    @FXML
    private void initialize() {
        if (back != null) {
            HoverEffects.installPurpleHover(back);
        }
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
        int activeIndex = findIndexByThreshold(skins, active.getLevelThreshold());
        String activeName = skinNameForIndex(activeIndex);

        if (currentSkinImage != null) {
            currentSkinImage.setImage(loadImage(active.getImagePath()));
        }
        if (currentSkinNameLabel != null) {
            currentSkinNameLabel.setText(activeName);
        }
        if (currentSkinDescLabel != null) {
            currentSkinDescLabel.setText("동그리 스타일");
        }

        if (skinListBox != null) {
            skinListBox.getChildren().clear();
            for (int i = 0; i < skins.size(); i++) {
                MascotSkin skin = skins.get(i);
                skinListBox.getChildren()
                        .add(buildSkinCard(skin, skin.getLevelThreshold() == active.getLevelThreshold(), i));
            }
        }
    }

    private void playHearts() {
        // 한 번 누를 때 여러 개 뿜고 싶으면 개수만 바꾸면 됨
        for (int i = 0; i < 4; i++) {
            spawnHeart();
        }
    }

    private void spawnHeart() {
        if (effectLayer == null || effectLayer.getScene() == null) {
            return;
        }

        HeartNode heart = createHeartNode();
        StackPane heartNode = heart.node();

        double layerWidth = effectLayer.getWidth() > 0
                ? effectLayer.getWidth()
                : effectLayer.getScene().getWidth();
        double layerHeight = effectLayer.getHeight() > 0
                ? effectLayer.getHeight()
                : effectLayer.getScene().getHeight();

        // 시작 위치: 화면 아래쪽 (아래에서 랜덤 X 위치로 올라오게)
        double startX = 40 + random.nextDouble() * (layerWidth - 80);
        double startY = layerHeight + 40;

        heartNode.setLayoutX(startX);
        heartNode.setLayoutY(startY);
        effectLayer.getChildren().add(heartNode);

        // 랜덤 방향: 직선/대각선 모두 포함 (X는 좌우로 퍼뜨리기)
        double targetY = -80 - random.nextDouble() * 80;
        double maxHorizontalDrift = Math.max(120, layerWidth * 0.25);
        double targetX = (random.nextDouble() * 2 - 1) * maxHorizontalDrift; // -max ~ +max

        Duration duration = Duration.seconds(2 + random.nextDouble());

        TranslateTransition move = new TranslateTransition(duration, heartNode);
        move.setFromY(0);
        move.setToY(targetY - startY);
        move.setFromX(0);
        move.setToX(targetX);

        FadeTransition fade = new FadeTransition(duration, heartNode);
        fade.setFromValue(1.0);
        fade.setToValue(0.0);

        double baseScale = heart.baseScale();
        ScaleTransition scale = new ScaleTransition(duration, heartNode);
        scale.setFromX(baseScale * 0.9);
        scale.setFromY(baseScale * 0.9);
        scale.setToX(baseScale * 1.1);
        scale.setToY(baseScale * 1.1);

        ParallelTransition combo = new ParallelTransition(move, fade, scale);
        combo.setOnFinished(e -> effectLayer.getChildren().remove(heartNode));
        combo.play();
    }

    private StackPane buildSkinCard(MascotSkin skin, boolean selected, int index) {
        boolean unlocked = skin.isUnlocked();

        StackPane card = new StackPane();
        card.getStyleClass().add("wardrobe-skin-card");
        if (selected && unlocked)
            card.getStyleClass().add("selected");
        if (!unlocked)
            card.getStyleClass().add("locked");
        if (unlocked)
            card.getStyleClass().add("unlocked");

        VBox box = new VBox(6);
        box.setAlignment(javafx.geometry.Pos.CENTER);

        ImageView img = new ImageView(loadImage(skin.getImagePath()));
        img.setFitWidth(80);
        img.setFitHeight(80);
        img.setPreserveRatio(true);
        img.getStyleClass().add("wardrobe-skin-thumb");
        if (!unlocked) {
            img.setOpacity(0.08); // 잠금 상태에서는 거의 보이지 않게
        }

        Label name = new Label();
        name.getStyleClass().add("wardrobe-skin-name");
        if (unlocked) {
            name.setText(skinNameForIndex(index));
        } else {
            // 잠긴 카드에는 아래 오버레이에만 레벨 표기 → 텍스트 숨김
            name.setVisible(false);
            name.setManaged(false);
        }

        box.getChildren().addAll(img, name);
        card.getChildren().add(box);

        if (!unlocked) {
            VBox lockOverlay = new VBox(4);
            lockOverlay.setAlignment(javafx.geometry.Pos.CENTER);
            lockOverlay.getStyleClass().add("wardrobe-lock-overlay");
            // SVG 기반 잠금 아이콘
            SVGPath lockShape = new SVGPath();
            lockShape.setContent(
                    "M3 11 " + // 시작점: 자물쇠 바디 왼쪽 위
                            "h18 " + // 가로로 18px (바디 상단)
                            "a2 2 0 0 1 2 2 " + // 오른쪽 위 모서리 라운드
                            "v7 " + // 아래로 7px
                            "a2 2 0 0 1 -2 2 " + // 오른쪽 아래 모서리 라운드
                            "h-18 " + // 왼쪽으로 18px
                            "a2 2 0 0 1 -2 -2 " + // 왼쪽 아래 모서리 라운드
                            "v-7 " + // 위로 7px
                            "a2 2 0 0 1 2 -2 " + // 왼쪽 위 모서리 라운드
                            "z " + // 바디 닫기
                            // 자물쇠 고리
                            "M7 11 " +
                            "V7 " +
                            "a5 5 0 0 1 10 0 " +
                            "v4");
            lockShape.setFill(Color.TRANSPARENT);
            lockShape.setStroke(Color.web("#D9B5FF"));
            lockShape.setStrokeWidth(2.0);

            StackPane lockIcon = new StackPane(lockShape);
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
        playHearts();
    }

    private HeartNode createHeartNode() {
        SVGPath shape = new SVGPath();
        shape.setContent(HEART_PATH);
        shape.getStyleClass().add("floating-heart-shape");

        String fill = HEART_GRADIENTS[random.nextInt(HEART_GRADIENTS.length)];
        shape.setStyle(
                "-fx-fill: " + fill +
                        "; -fx-stroke: rgba(255,255,255,0.72);" +
                        "; -fx-stroke-width: 1.6;");

        StackPane node = new StackPane(shape);
        node.setPickOnBounds(false);
        node.getStyleClass().add("floating-heart");

        // ✅ 크게 키운 뒤 (기본 대비 10배 이상), 부모 노드 스케일에 적용
        double baseScale = randomRange(12.0, 18.0);
        node.setScaleX(baseScale);
        node.setScaleY(baseScale);

        // 회전만 부모에 적용
        node.setRotate(randomRange(-18, 18));

        return new HeartNode(node, baseScale);
    }

    private double randomRange(double min, double max) {
        return min + random.nextDouble() * (max - min);
    }

    private record HeartNode(StackPane node, double baseScale) {}

    private String skinNameForIndex(int index) {
        if (index >= 0 && index < SKIN_NAMES.size()) {
            return SKIN_NAMES.get(index);
        }
        return "동그리";
    }

    private int findIndexByThreshold(List<MascotSkin> skins, int threshold) {
        for (int i = 0; i < skins.size(); i++) {
            if (skins.get(i).getLevelThreshold() == threshold) {
                return i;
            }
        }
        return 0;
    }
}
