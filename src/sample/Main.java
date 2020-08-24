package sample;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ToolBar;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Paint Program");

        double width = 600, height = 400;

        BorderPane bp = new BorderPane();
        //bp.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));

        MenuItem save = new MenuItem("Save");
        MenuItem exit = new MenuItem("Exit");

        Menu file = new Menu("File");
        file.getItems().add(save);
        file.getItems().add(exit);
        Menu edit = new Menu("Edit");

        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().add(file);
        menuBar.getMenus().add(edit);

        TextField tfPenSize = new TextField();
        tfPenSize.setText("10");
        tfPenSize.setPrefColumnCount(2);

        ColorPicker colorPicker = new ColorPicker();
        CheckBox eraser = new CheckBox("Eraser");

        ToolBar toolBar = new ToolBar();
        toolBar.getItems().add(tfPenSize);
        toolBar.getItems().add(colorPicker);
        toolBar.getItems().add(eraser);

        VBox vBox = new VBox();
        vBox.getChildren().add(menuBar);
        vBox.getChildren().add(toolBar);
        bp.setTop(vBox);

        Canvas canvas = new Canvas(width, height);
        GraphicsContext graphicsContext = canvas.getGraphicsContext2D();
        bp.setCenter(canvas);
        graphicsContext.setLineCap(StrokeLineCap.ROUND);

        Paint bg = Color.WHITE;
        graphicsContext.setFill(bg);
        graphicsContext.fillRect(0,0, width,height);

        Label instruction = new Label("To draw, just drag the mouse on the canvas");
        instruction.setFont(new Font("Arial", 15));
        instruction.setPadding(new Insets(0, 0, 5, 5));
        bp.setBottom(instruction);

        canvas.setOnMousePressed(e->{
            double x = e.getX(), y=e.getY();
            graphicsContext.beginPath();
            graphicsContext.lineTo(x, y);
            graphicsContext.stroke();
        });

        canvas.setOnMouseDragged(event -> {
            double x = event.getX(), y = event.getY();
            graphicsContext.lineTo(x, y);
            graphicsContext.stroke();
        });

        colorPicker.setOnAction(event -> {
            graphicsContext.setStroke(colorPicker.getValue());
        });

        tfPenSize.setOnKeyReleased(event -> {
            String penSize = tfPenSize.getText();
            double size = Double.parseDouble(penSize);
            graphicsContext.setLineWidth(size);
        });

        eraser.setOnAction(event -> {
            if(eraser.isSelected()){
                graphicsContext.setStroke(bg);
            }else{
                graphicsContext.setStroke(colorPicker.getValue());
            }
        });

        save.setOnAction(event -> {
            Image snapshot = canvas.snapshot(null, null);
            try{
                File afile = new File("paint_image.png");
                ImageIO.write(SwingFXUtils.fromFXImage(snapshot, null),"png", afile);

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Save File");
                alert.setHeaderText("Success!");
                String s = "Your file is saved in:\n" + afile.getAbsolutePath();
                alert.setContentText(s);
                alert.show();
            }catch (IOException e){
                e.printStackTrace();
            }
        });

        exit.setOnAction(event -> {
            Platform.exit();
        });

//        canvas.heightProperty().bind(bp.heightProperty());
//        canvas.widthProperty().bind(bp.widthProperty());

        Scene scene = new Scene(bp, width, height);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    public static void main(String[] args) {
        Application.launch(args);
    }
}
