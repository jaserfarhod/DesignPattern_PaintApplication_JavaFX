package model.util;

import controller.DrawingViewController;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.WritableImage;
import javafx.stage.FileChooser;
import model.Shape;

import javax.imageio.ImageIO;
import java.awt.image.RenderedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FileUtil {

    // the shapes ("project")
    private List<Shape> loadedShapes;

    // input streams
    private FileInputStream fis;
    private ObjectInputStream ois;

    // output streams
    private FileOutputStream fos;
    private ObjectOutputStream oos;

    // Constructor, initializes all class members as null
    public FileUtil() {
        loadedShapes = new ArrayList<>();

        fis = null;
        ois = null;
        fos = null;
        oos = null;
    }

    /**
     * Code from: http://java-buddy.blogspot.se/2013/04/save-canvas-to-png-file.html
     * <p>
     * Saves Project/Canvas to a picture file, PNG file.
     *
     * @param c the canvas of which to create the PNG.
     */
    public void saveCanvasToPNG(Canvas c) {
        FileChooser fileChooser = getFileChooser("Save Image", "png files(*.png)", "*.png");

        File file = fileChooser.showSaveDialog(null);
        if (file != null) {
            try {
                WritableImage writableImage = new WritableImage((int) c.getWidth(), (int) c.getHeight());
                c.snapshot(null, writableImage);
                RenderedImage renderedImage = SwingFXUtils.fromFXImage(writableImage, null);
                ImageIO.write(renderedImage, "png", file);
            } catch (IOException ex) {
                System.out.println("saveCanvasToPNG exception : " + ex.getMessage());
            }
        }

    }

    /**
     * Loads the project (picture/canvas)
     *
     * @return loadShapes a List of Shape objects.
     */
    public List<Shape> loadProject() {
        FileChooser fileChooser = getFileChooser("Open Project", "mydat", "*.mydat");
        File file = fileChooser.showOpenDialog(null);

        try {
            if (file != null) {
                fis = new FileInputStream(file);
                ois = new ObjectInputStream(fis);
                loadedShapes = (ArrayList<Shape>) ois.readObject();
            }

        } catch (ClassNotFoundException | IOException e) {
            System.out.println("loadProject exception: " + e.getLocalizedMessage());
        } finally {
            closeFileInputStream();
            closeObjectInputStream();
        }

        return loadedShapes;
    }

    /**
     * Saves the Project to file
     *
     * @param shapesToSave List of Shape objects to be saved
     */
    public void saveProject(List<Shape> shapesToSave) {
        FileChooser fileChooser = getFileChooser("Save Project", "mydat files(*.mydat)", "*.mydat");
        File file = fileChooser.showSaveDialog(null);

        try {
            if (file != null) {
                fos = new FileOutputStream(file);
                oos = new ObjectOutputStream(fos);
                oos.writeObject(shapesToSave);
            }
        } catch (IOException ex) {
            Logger.getLogger(DrawingViewController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            closeFileOutputStream();
            closeObjectOutputStream();
        }
    }

    // closes file output stream - helper
    private void closeFileOutputStream() {
        if (fos != null) {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // closes the object output stream  - helper
    private void closeObjectOutputStream() {
        if (oos != null) {
            try {
                oos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Closes the FileInputStream  - helper
     */
    private void closeFileInputStream() {
        if (fis != null) {
            try {
                fis.close();
            } catch (IOException e) {
                System.out.println("closeFileInputStream exception: " + e.getLocalizedMessage());
                e.printStackTrace();
            }
        }
    }

    /**
     * Closes the ObjectInputStream  - helper
     */
    private void closeObjectInputStream() {
        if (ois != null) {
            try {
                ois.close();
            } catch (IOException e) {
                System.out.println("closeObjectInputStream exception: " + e.getLocalizedMessage());
            }
        }
    }

    /**
     * Prepares the FileChooser  - helper
     */
    private FileChooser getFileChooser(String title, String description, String extensions) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(title);

        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter(description, extensions)
        );

        return fileChooser;
    }
}
