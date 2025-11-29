package dongggg;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.CustomMenuItem;
import javafx.scene.input.MouseEvent;
import javafx.geometry.Side;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.shape.SVGPath;
import javafx.scene.paint.Color;
import dongggg.FolderRepository;
import dongggg.Folder;
import dongggg.NoteRepository;
import dongggg.NoteFolderRepository;
import dongggg.App;

public class NoteCardController {

    @FXML
    private Label titleLabel;
    @FXML
    private Label dateLabel;
    @FXML
    private CheckBox checkBox;
    @FXML
    private Button moreButton;

    private Note note;

    public void setData(Note note) {
        this.note = note;
        titleLabel.setText(note.getTitle());
        dateLabel.setText(note.getUpdatedAt() != null ? note.getUpdatedAt() : note.getCreatedAt());
    }

    public boolean isSelected() {
        return checkBox.isSelected();
    }

    public Note getNote() {
        return note;
    }

    public CheckBox getCheckBox() {
        return checkBox;
    }

    public void setMoreVisible(boolean visible) {
        if (moreButton != null) {
            moreButton.setVisible(visible);
            moreButton.setManaged(visible);
        }
    }

    @FXML
    private void onMore(MouseEvent event) {
        if (note == null)
            return;

        ContextMenu menu = new ContextMenu();
        menu.getStyleClass().add("note-context-menu");

        CustomMenuItem moveItem = new CustomMenuItem(buildMenuRow("→", "이동", false));
        moveItem.setHideOnClick(false);
        moveItem.setOnAction(e -> {
            menu.hide();
            showFolderMenu();
        });

        CustomMenuItem deleteItem = new CustomMenuItem(buildDeleteRow());
        deleteItem.setOnAction(e -> {
            NoteRepository.delete(note.getId());
            App.showMainView();
        });

        menu.getItems().add(moveItem);
        menu.getItems().add(new SeparatorMenuItem());
        menu.getItems().add(deleteItem);

        menu.show(moreButton, Side.LEFT, 0, 0);
        event.consume();
    }

    private void showFolderMenu() {
        ContextMenu submenu = new ContextMenu();
        submenu.getStyleClass().add("note-folder-menu");
        var folders = FolderRepository.findAll();

        for (Folder folder : folders) {
            CustomMenuItem item = new CustomMenuItem(buildFolderRow(folder.getName()));
            item.setHideOnClick(true);
            item.setOnAction(e -> {
                NoteFolderRepository.setNoteFolder(note.getId(), folder.getId());
                App.showMainView();
            });
            submenu.getItems().add(item);
        }

        if (submenu.getItems().isEmpty()) {
            MenuItem empty = new MenuItem("폴더가 없습니다");
            empty.setDisable(true);
            submenu.getItems().add(empty);
        }

        submenu.show(moreButton, Side.LEFT, -6, 0);
    }

    private HBox buildMenuRow(String iconText, String labelText, boolean danger) {
        HBox row = new HBox(10);
        row.setAlignment(Pos.CENTER_LEFT);
        row.getStyleClass().add("note-menu-item");

        Label icon = new Label(iconText);
        icon.getStyleClass().add(danger ? "note-menu-icon-danger" : "note-menu-icon");

        Label label = new Label(labelText);
        label.getStyleClass().add(danger ? "note-menu-label-danger" : "note-menu-label");

        row.getChildren().addAll(icon, label);
        return row;
    }

    private HBox buildDeleteRow() {
        SVGPath trash = new SVGPath();
        trash.setContent(
                "M10 11 v6 " +                                     // 왼쪽 막대
                "M14 11 v6 " +                                     // 오른쪽 막대
                "M19 6 v14 a 2 2 0 0 1 -2 2 H7 " +                 // 오른쪽 위에서 시작해 아래로 + 둥근 모서리 + 아래 가로
                "a 2 2 0 0 1 -2 -2 V6 " +                          // 왼쪽 아래 모서리 + 위로
                "M3 6 h18 " +                                      // 상단 전체 가로선
                "M8 6 V4 a 2 2 0 0 1 2 -2 h4 " +                   // 뚜껑 양쪽 기둥 + 둥근 상단
                "a 2 2 0 0 1 2 2 v2"                               // 오른쪽 위 모서리 + 아래로
        );
        trash.setStroke(Color.web("#9CA3AF"));
        trash.setFill(Color.TRANSPARENT);
        trash.setStrokeWidth(1.8);
        trash.setScaleX(0.8);
        trash.setScaleY(0.8);

        HBox row = new HBox(8);
        row.setAlignment(Pos.CENTER_LEFT);
        row.getStyleClass().addAll("note-menu-item", "note-menu-delete");

        Label label = new Label("삭제");
        label.getStyleClass().add("note-menu-label-danger");

        row.getChildren().addAll(trash, label);
        return row;
    }

    private HBox buildFolderRow(String name) {
        HBox row = new HBox(10);
        row.setAlignment(Pos.CENTER_LEFT);
        row.getStyleClass().add("note-folder-item");

        SVGPath icon = new SVGPath();
        icon.setContent(
                "M20 20a2 2 0 0 0 2-2V8a2 2 0 0 0-2-2h-7.9a2 2 0 0 1-1.69-.9L9.6 3.9A2 2 0 0 0 7.93 3H4a2 2 0 0 0-2 2v13a2 2 0 0 0 2 2Z");
        icon.setStroke(Color.web("#F4B400"));
        icon.setFill(Color.TRANSPARENT);
        icon.setStrokeWidth(1.6);
        icon.setScaleX(0.8);
        icon.setScaleY(0.8);

        Label label = new Label(name);
        label.getStyleClass().add("note-folder-label");

        row.getChildren().addAll(icon, label);
        return row;
    }
}
