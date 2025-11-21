package dongggg;

import javafx.scene.image.Image;

public class MascotProvider {

    /**
     * 현재 선택된 스킨(잠금 시 해금된 범위 내 가장 높은 스킨)을 반환.
     */
    public static Image loadForLevel(int level) {
        MascotSkin skin = MascotSkinRepository.resolveSkinForLevel(level);
        String path = skin.getImagePath();
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
}
