package model;

import java.io.Serializable;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Oval extends Shape implements Cloneable, Serializable {

    public Oval() {
        super(null, null, 0, Color.BLACK.toString());
    }

    /**
     * The template method
     */
    @Override
    protected void drawStep(GraphicsContext gc) {
        if (isFilled()) {
            // x,y,w,h
            gc.fillOval(getP1().getX(), getP1().getY(),
                    getP2().getX() - getP1().getX(), getP2().getY() - getP1().getY());
        } else {
            gc.strokeOval(getP1().getX(), getP1().getY(),
                    getP2().getX() - getP1().getX(), getP2().getY() - getP1().getY());

        }
    }

    @Override
    public Oval clone() {
        return (Oval) super.clone();
    }

    @Override
    public String toString(){
        return "Oval";
    }
}
