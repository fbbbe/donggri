package dongggg;

public class Note {

    private int id;
    private String title;
    private String content;
    private String createdAt;
    private String updatedAt;
    private String type; // "NORMAL" 또는 "CONCEPT"

    public Note(int id, String title, String content,
            String createdAt, String updatedAt, String type) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.type = type;
    }

    // 일반 노트용 생성자
    public Note(String title, String content) {
        this(0, title, content, null, null, "NORMAL");
    }

    // 타입 지정 생성자 (개념 노트 등)
    public Note(String title, String content, String type) {
        this(0, title, content, null, null, type);
    }

    // getter / setter
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}