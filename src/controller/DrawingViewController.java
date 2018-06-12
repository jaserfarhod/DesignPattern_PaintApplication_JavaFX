package controller;

import model.Shape;
import model.factory.ShapeFactory;
import javafx.scene.control.*;
import model.ModelFacade;
import model.Point;

import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import model.util.FileUtil;
import model.util.PaintLogging;

public class DrawingViewController implements Initializable, Observer {


    private Point p1 = new Point();
    private Point p2 = new Point();
    private Point startPoint = new Point();
    private ModelFacade modelFacade;
    private int nrOfComposites;
    private ArrayList<String> viewList = new ArrayList<>();

    @FXML
    private Button undo;
    @FXML
    private Button redo;
    @FXML
    private ColorPicker colorPicker;
    @FXML
    private CheckBox filledShape;
    @FXML
    private ComboBox<String> shapePicker;
    @FXML
    private Canvas canvas;
    @FXML
    private ComboBox<Double> thicknessPicker;
    @FXML
    private ListView<String> listView;
    @FXML
    private CheckBox editSelectedItem;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        Method[] methods = ShapeFactory.class.getDeclaredMethods();
        for (Method m : methods) {
            if (m.toString().contains("create")) {
                shapePicker.getItems().add(m.getName().replace("create", ""));
            }
        }
        shapePicker.getSelectionModel().select(0);
        shapePicker.setTooltip(new Tooltip("Pick a shape"));

        thicknessPicker.getItems().add(1.0);
        thicknessPicker.getItems().add(2.0);
        thicknessPicker.getItems().add(4.0);
        thicknessPicker.getItems().add(8.0);
        thicknessPicker.getItems().add(16.0);
        thicknessPicker.getSelectionModel().select(1);
        thicknessPicker.setTooltip(new Tooltip("Pick thickness"));

        colorPicker.setValue(Color.BLACK);

        modelFacade = new ModelFacade(canvas.getWidth(), canvas.getHeight());
        modelFacade.addObserver(this);  // adds controller as observer to modelFacade
    }

    /**
     * Method that gets called when the Undo button is clicked.
     */
    @FXML
    private void onActinoUndo(ActionEvent event) {
        event.consume();
        modelFacade.undo(canvas.getGraphicsContext2D());
    }

    /**
     * Method that gets called when the Redo button is clicked.
     */
    @FXML
    private void onActionRedo(ActionEvent event) {
        event.consume();
        modelFacade.redo(canvas.getGraphicsContext2D());
    }

    /**
     * If edit is toggled, it marks an object on the canvas,
     * else it created a shape when mouse click is released.
     */
    @FXML
    private void onMouseReleasedCanvas(MouseEvent event) {
        p2.setX(event.getX());
        p2.setY(event.getY());

        if (editSelectedItem.isSelected()) {
            modelFacade.markItems(p1, canvas.getGraphicsContext2D());
        } else {
            createSelectedShape(event);
        }
    }

    /**
     * Creates a select shape.
     */
    private void createSelectedShape(MouseEvent event) {
        PaintLogging.logInfo("DrawingViewController | createSelectedShape - method");

        if (shapePicker.getValue().toLowerCase().compareTo("line") != 0) {
            p1.setX(Math.min(event.getX(), startPoint.getX()));
            p1.setY(Math.min(event.getY(), startPoint.getY()));
            p2.setX(Math.max(event.getX(), startPoint.getX()));
            p2.setY(Math.max(event.getY(), startPoint.getY()));
        }

        if (p1.getX() != p2.getX() && p1.getY() != p2.getY()) {

            // if the selected shape is NOT a composite shape
            if (!shapePicker.getValue().toLowerCase().contains("composite")) {

                modelFacade.drawShape(shapePicker.getValue(), new Point(p1.getX(), p1.getY()), new Point(p2.getX(), p2.getY()),
                        colorPicker.getValue(), thicknessPicker.getValue(), filledShape.isSelected(), canvas.getGraphicsContext2D());

            } else {

                // if the selected shape is a composite shape
                // compositeNr -1, because array list is indexed 0...n, and the shapePicker has them indexed 1...n
                int compositeNo = Integer.parseInt(shapePicker.getValue().replaceAll("\\D", "")) - 1;

                modelFacade.drawComposite(compositeNo,
                        new Point(p1.getX(), p1.getY()), new Point(p2.getX(), p2.getY()),
                        colorPicker.getValue(), thicknessPicker.getValue(), filledShape.isSelected(), canvas.getGraphicsContext2D());

            }
        }
    }

    /**
     * Method that gets called when the composite button is clicked.
     */
    public void onActionComposite(ActionEvent actionEvent) {
        actionEvent.consume();
        if (editSelectedItem.isSelected()) {
            modelFacade.createComposite();
            shapePicker.getItems().add("Composite" + (++nrOfComposites));
        }
    }

    /**
     * update the releasepoint while dragging.
     *
     * @param event fxevent
     */
    @FXML
    private void onMouseDraggedCanvas(MouseEvent event) {
        event.consume();
        // Only needed if we want viual dragging effect
    }

    /**
     * set initial click.
     *
     * @param event fxevent
     */
    @FXML
    private void onMousePressedCanvas(MouseEvent event) {
        startPoint.setX(event.getX());
        startPoint.setY(event.getY());
        p1.setX(event.getX());
        p1.setY(event.getY());
    }

    /**
     * Method that gets called when the thickness box / button is clicked.
     */
    @FXML
    private void onActionThicknessPicker(ActionEvent event) {
        event.consume();
        if (editSelectedItem.isSelected()) {
            modelFacade.editMarkedThickness(canvas.getGraphicsContext2D(), thicknessPicker.getValue());
        }
    }

    /**
     * Method that gets called when the color choice button is clicked.
     */
    @FXML
    private void onActionColorPicker(ActionEvent event) {
        event.consume();
        if (editSelectedItem.isSelected()) {
            modelFacade.editMarkedColor(canvas.getGraphicsContext2D(), colorPicker.getValue());
        }
    }

    /**
     * Method that gets called when the fill shape button is clicked.
     */
    @FXML
    private void onActionFilledShape(ActionEvent event) {
        event.consume();
        if (editSelectedItem.isSelected()) {
            modelFacade.editMarkedFilled(canvas.getGraphicsContext2D(), filledShape.isSelected());
        }
    }

    /**
     * delete an item from the list
     *
     * @param event fxevent
     */
    @FXML
    private void onActionDelete(ActionEvent event) {
        event.consume();
        if (editSelectedItem.isSelected()) {
            modelFacade.delete(canvas.getGraphicsContext2D());
        }
    }

    /**
     * Toggles edit mode, and disables undo / redo buttons.
     */
    public void onActionEditSelectedItem(ActionEvent actionEvent) {
        actionEvent.consume();

        if (!editSelectedItem.isSelected()) {
            modelFacade.clearMarkedShapes();
            modelFacade.reRender(canvas.getGraphicsContext2D());

            redo.setDisable(false);
            undo.setDisable(false);
        } else {
            redo.setDisable(true);
            undo.setDisable(true);
        }
    }

    /**
     * Updates the view, used by model to tell view something has changed in the model.
     *
     * @param observable observable object
     * @param arg        the argument object
     */

    public void update(Observable observable, Object arg) {
        PaintLogging.logInfo("DrawingViewController | update - method \n" +
                "observable: " + observable.toString() + "\n" +
                "arg: " + arg.toString());

        // observer observable from:
        // https://www.javaworld.com/article/2077258/learn-java/observer-and-observable.html
        if (observable == modelFacade) {

            // https://stackoverflow.com/questions/14346219/unsafe-parameterized-arraylist-cast-after-notify-from-observable
            viewList.clear();
            if (arg instanceof ArrayList<?>) {
                for (Object o : (ArrayList<?>) arg) {
                    if (o instanceof Shape) {
                        viewList.add(o.toString());
                    }
                }
                listView.getItems().clear();
                listView.getItems().addAll(viewList);
            }
        }

        modelFacade.reRender(canvas.getGraphicsContext2D());
    }

    /**
     * Menu button 'Open Project'
     *
     * @param event fxevent
     */
    @FXML
    private void onActionOpenProject(ActionEvent event) {
        event.consume();
        FileUtil fh = new FileUtil();

        modelFacade.setShapes(fh.loadProject());
        modelFacade.addObserver(this);
        modelFacade.reRender(canvas.getGraphicsContext2D());
    }


    /**
     * Menu button 'Save Project'
     */
    @FXML
    private void onActionSaveProject(ActionEvent event) {
        event.consume();
        FileUtil fileUtil = new FileUtil();
        fileUtil.saveProject(modelFacade.getShapes());
    }

    /**
     * Menu button 'SaveImage'
     *
     * @param event fxevent
     */
    @FXML
    private void onActionSaveImage(ActionEvent event) {
        event.consume();
        FileUtil fileUtil = new FileUtil();
        fileUtil.saveCanvasToPNG(this.canvas);
    }

    /**
     * Menu button 'Close'
     *
     * @param event fxevent
     */
    @FXML
    private void onActionCloseProgram(ActionEvent event) {
        event.consume();
        System.exit(0);
    }

}
