package dongggg;

public class MascotSkin {
    private final int levelThreshold;
    private final String imagePath;
    private final boolean unlocked;
    private final boolean selected;

    public MascotSkin(int levelThreshold, String imagePath, boolean unlocked, boolean selected) {
        this.levelThreshold = levelThreshold;
        this.imagePath = imagePath;
        this.unlocked = unlocked;
        this.selected = selected;
    }

    public int getLevelThreshold() {
        return levelThreshold;
    }

    public String getImagePath() {
        return imagePath;
    }

    public boolean isUnlocked() {
        return unlocked;
    }

    public boolean isSelected() {
        return selected;
    }
}
