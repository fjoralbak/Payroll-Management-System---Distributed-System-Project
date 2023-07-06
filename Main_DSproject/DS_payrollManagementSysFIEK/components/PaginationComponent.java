package components;

import javafx.scene.control.Button;
import javafx.scene.layout.Pane;

import java.util.ArrayList;

public class PaginationComponent {
    private int cursor;
    private int totalItems;
    private int itemsPerPage;
    private int totalPages;

    public PaginationComponent(int totalItems, int itemsPerPage) {
        this.totalItems = totalItems;
        this.itemsPerPage = itemsPerPage;
        this.cursor = 0;
        this.totalPages = (int) (Math.ceil((double) this.totalItems / (double) this.itemsPerPage));
    }

    public int getCursor() {
        return this.cursor;
    }

    public void render(Pane paginationPane, PaginationHandler handler) {
        if (totalPages == 0) return;

        paginationPane.getChildren().clear();
        ArrayList<Button> pageButtons = new ArrayList<>();
        for (int i = 0; i < totalPages; i++) {
            final int page = i;
            Button button = new Button(Integer.toString(page + 1));
            button.setOnAction(event -> {
                handler.run(page);
                cursor = page;
                pageButtons.forEach(btn -> btn.getStyleClass().remove("active-page"));
                button.getStyleClass().add("active-page");
            });
            pageButtons.add(button);
        }
        pageButtons.get(cursor).getStyleClass().add("active-page");

        Button prevButton = new Button("◄");
        prevButton.setOnAction(event -> {
            if (cursor - 1 < 0) return;
            cursor--;
            handler.run(cursor);
            pageButtons.forEach(btn -> btn.getStyleClass().remove("active-page"));
            pageButtons.get(cursor).getStyleClass().add("active-page");
        });

        Button nextButton = new Button("►");
        nextButton.setOnAction(event -> {
            if (cursor + 1 >= totalPages) return;
            cursor++;
            handler.run(cursor);
            pageButtons.forEach(btn -> btn.getStyleClass().remove("active-page"));
            pageButtons.get(cursor).getStyleClass().add("active-page");
        });

        paginationPane.getChildren().add(prevButton);
        paginationPane.getChildren().addAll(pageButtons);
        paginationPane.getChildren().add(nextButton);
    }

    public interface PaginationHandler {
        public void run(int page);
    }
}
