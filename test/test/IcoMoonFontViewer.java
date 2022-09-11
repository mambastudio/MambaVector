/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

/**
 *
 * @author jmburu
 */
import javafx.application.Application;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.SVGPath;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;
import java.util.stream.Collectors;

public class IcoMoonFontViewer extends Application {
    private static final String SVG_FONT_DIRECTORY_RESOURCE_PATH =
            "/icomoonfontviewer/icon-directory.txt";

    private static final String SVG_FONT_RESOURCE_PATH =
            "/icomoonfontviewer/SVG/";

    private IcoMoonSVGGlyphLoader glyphLoader = new IcoMoonSVGGlyphLoader();

    private GlyphDetailViewer glyphDetailViewer = new GlyphDetailViewer();

    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("IcoMoon SVG Font Viewer");

        ScrollPane scrollableGlyphs = allGlyphs();

        HBox layout = new HBox(
                scrollableGlyphs,
                glyphDetailViewer
        );
        HBox.setHgrow(scrollableGlyphs, Priority.ALWAYS);

        // display the app.
        stage.setScene(new Scene(layout));
        stage.show();
    }

    private ScrollPane allGlyphs() throws IOException {
        ObservableList<SVGGlyph> glyphs =
                new IcoMoonSVGGlyphLoader().readIcoMoonGlyphsFromDirectory(
                        IcoMoonFontViewer.class.getResource(
                                SVG_FONT_DIRECTORY_RESOURCE_PATH
                        ),
                        IcoMoonFontViewer.class.getResource(
                                SVG_FONT_RESOURCE_PATH
                        )
                );

        glyphs.forEach(glyph -> glyph.setPrefSize(16, 16));

        List<Button> iconButtons = glyphs.stream()
                .map(this::createIconButton)
                .collect(Collectors.toList());

        iconButtons.get(0).fire();

        FlowPane glyphLayout = new FlowPane();
        glyphLayout.setHgap(10);
        glyphLayout.setVgap(10);
        glyphLayout.setPadding(new Insets(10));
        glyphLayout.getChildren().setAll(iconButtons);
        glyphLayout.setPrefSize(400, 300);

        ScrollPane scrollableGlyphs = new ScrollPane(glyphLayout);
        scrollableGlyphs.setFitToWidth(true);

        return scrollableGlyphs;
    }

    private Button createIconButton(SVGGlyph glyph) {
        Button button = new Button(null, glyph);
        button.setOnAction(event -> viewGlyphDetail(glyph));
        Tooltip.install(
                button,
                new Tooltip(
                        glyph.getName()
                )
        );

        return button;
    }

    private void viewGlyphDetail(SVGGlyph glyph) {
        try {
            glyphDetailViewer.setGlyph(
                    glyphLoader.loadGlyph(
                            IcoMoonFontViewer.class.getResource(
                                    SVG_FONT_RESOURCE_PATH + glyphLoader.getFileName(glyph)
                            )
                    )
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}

class GlyphDetailViewer extends VBox {
    private final int MIN_ICON_SIZE     = 8;
    private final int DEFAULT_ICON_SIZE = 128;
    private final int MAX_ICON_SIZE     = 256;

    private final ObjectProperty<SVGGlyph> glyph = new SimpleObjectProperty<>();
    private final Label idLabel = new Label();
    private final Label nameLabel = new Label();
    private final ColorPicker colorPicker = new ColorPicker(Color.BLACK);
    private final Slider sizeSlider = new Slider(MIN_ICON_SIZE, MAX_ICON_SIZE, DEFAULT_ICON_SIZE);
    private final Label sizeLabel = new Label();
    private StackPane centeredGlyph = new StackPane();

    public GlyphDetailViewer() {
        GridPane details = new GridPane();
        details.setHgap(10);
        details.setVgap(10);
        details.setPadding(new Insets(10));
        details.setMinSize(GridPane.USE_PREF_SIZE, GridPane.USE_PREF_SIZE);

        Label sizeCalculator = new Label("999");
        Group sizingRoot = new Group(sizeCalculator);
        new Scene(sizingRoot);
        sizingRoot.applyCss();
        sizingRoot.layout();
        sizeLabel.setMinWidth(Label.USE_PREF_SIZE);
        sizeLabel.setPrefWidth(sizeCalculator.getWidth());
        sizeLabel.setAlignment(Pos.BASELINE_RIGHT);

        HBox sizeControl = new HBox(
                5,
                sizeLabel,
                sizeSlider
        );
        sizeControl.prefWidthProperty().bind(
                colorPicker.widthProperty()
        );

        details.addRow(
                0,
                new Label("Id"),
                idLabel
        );
        details.addRow(
                1,
                new Label("Name"),
                nameLabel
        );

        details.addRow(
                2,
                new Label("Color"),
                colorPicker
        );
        details.addRow(
                3,
                new Label("Size"),
                sizeControl
        );

        sizeLabel.textProperty().bind(
                sizeSlider.valueProperty().asString("%.0f")
        );

        VBox.setVgrow(centeredGlyph, Priority.ALWAYS);
        StackPane.setMargin(centeredGlyph, new Insets(10));

        centeredGlyph.setPrefSize(MAX_ICON_SIZE + 10 * 2, MAX_ICON_SIZE + 10 * 2);

        glyphProperty().addListener((observable, oldValue, newValue) -> {
            if (oldValue != null) {
                oldValue.fillProperty().unbind();
                oldValue.prefWidthProperty().unbind();
                oldValue.prefHeightProperty().unbind();
            }

            refreshView();
        });


        getChildren().setAll(
                details,
                centeredGlyph
        );
    }

    private void refreshView() {
        if (glyph.getValue() == null) {
            idLabel.setText("");
            nameLabel.setText("");

            return;
        }

        glyph.get().setMinSize(
                StackPane.USE_PREF_SIZE,
                StackPane.USE_PREF_SIZE
        );
        glyph.get().setPrefSize(
                sizeSlider.getValue(),
                sizeSlider.getValue()
        );
        glyph.get().setMaxSize(
                StackPane.USE_PREF_SIZE,
                StackPane.USE_PREF_SIZE
        );
        glyph.get().prefWidthProperty().bind(
                sizeSlider.valueProperty()
        );
        glyph.get().prefHeightProperty().bind(
                sizeSlider.valueProperty()
        );

        idLabel.setText(
                String.format(
                        "%04d",
                        glyph.get().getGlyphId()
                )
        );

        nameLabel.setText(
                glyph.get().getName()
        );

        glyph.get().setFill(
                colorPicker.getValue()
        );
        glyph.get().fillProperty().bind(
                colorPicker.valueProperty()
        );

        centeredGlyph.getChildren().setAll(
                glyph.get()
        );
    }

    public SVGGlyph getGlyph() {
        return glyph.get();
    }

    public ObjectProperty<SVGGlyph> glyphProperty() {
        return glyph;
    }

    public void setGlyph(SVGGlyph glyph) {
        this.glyph.set(glyph);
    }
}

class SVGGlyph extends Pane {
    private final int    glyphId;
    private final String name;

    private static final int DEFAULT_PREF_SIZE = 64;

    private ObjectProperty<Paint> fill = new SimpleObjectProperty<>();

    public SVGGlyph(
            int glyphId,
            String name,
            String svgPathContent,
            Paint fill
    ) {
        this.glyphId = glyphId;
        this.name = name;

        SVGPath shape = new SVGPath();
        shape.setContent(svgPathContent);

        this.fill.addListener((observable, oldValue, newValue) ->
            setBackground(
                    new Background(
                            new BackgroundFill(
                                    newValue, null, null
                            )
                    )
            )
        );
        setShape(shape);
        setFill(fill);

        setPrefSize(DEFAULT_PREF_SIZE, DEFAULT_PREF_SIZE);
    }

    public int getGlyphId() {
        return glyphId;
    }

    public String getName() {
        return name;
    }

    public void setFill(Paint fill) {
        this.fill.setValue(fill);
    }

    public ObjectProperty<Paint> fillProperty() {
        return fill;
    }

    public Paint getFill() {
        return fill.getValue();
    }
}

class IcoMoonSVGGlyphLoader {

    public ObservableList<SVGGlyph> readIcoMoonGlyphsFromDirectory(
            URL fontDirectoryLocation,
            URL fontResourcePath
    ) throws IOException {
        ObservableList<SVGGlyph> glyphs = FXCollections.observableArrayList();

        try (
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(
                                fontDirectoryLocation.openStream()
                        )
                )
        ) {
            for (;;) {
                String line = reader.readLine();
                if (line == null) {
                    break;
                }

                if (line.startsWith("#")) {
                    continue;
                }

                line = line.trim();

                if (line.isEmpty()) {
                    continue;
                }

                glyphs.add(
                        loadGlyph(
                                new URL(fontResourcePath + line)
                        )
                );
            }
        }
        return glyphs;
    }

    public SVGGlyph loadGlyph(URL url) throws IOException {
        String urlString = url.toString();
        String filename = urlString.substring(
                urlString.lastIndexOf('/') + 1
        );

        int startPos = 0;
        int endPos   = 0;
        while (endPos < filename.length() && filename.charAt(endPos) != '-') { endPos++; }
        int id = Integer.parseInt(filename.substring(startPos, endPos));
        startPos = endPos + 1;

        while (endPos < filename.length() && filename.charAt(endPos) != '.') { endPos++; }
        String name = filename.substring(startPos, endPos);

        return new SVGGlyph(
                id,
                name,
                extractSvgPath(
                        getStringFromInputStream(
                                url.openStream()
                        )
                ),
                Color.BLACK
        );
    }

    private String extractSvgPath(String svgString) {
        return svgString
                .replaceFirst(".*d=\"", "")
                .replaceFirst("\".*", "");
    }

    // convert InputStream to String
    // copied from: http://www.mkyong.com/java/how-to-convert-inputstream-to-string-in-java/
    private String getStringFromInputStream(InputStream is) {
        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();

        String line;
        try {

            br = new BufferedReader(new InputStreamReader(is));
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return sb.toString();
    }

    public String getFileName(SVGGlyph glyph) {
        return
                String.format("%04d", glyph.getGlyphId())
                        + "-"
                        + glyph.getName()
                        + ".svg";
    }

}