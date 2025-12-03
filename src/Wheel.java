//Joey Barton

//This class holds all logic for the animated Wheel

import java.util.Random;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.geometry.VPos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;

public class Wheel extends StackPane{

    private Canvas canvas;
    private Canvas pointerCanvas;
    private Rotate rotation;
    private Random rand;

    private static final String[] SEGMENT_VALUES = {
        "$200", "Lose Turn", "$100", "$200", "$500", "Bankrupt", "$500", "$100", 
    };

    private static final Color[] SEGMENT_COLORS = {
        Color.rgb(255, 193, 7), //Gold (500)
        Color.rgb(76, 175, 80), //Green (200)
        Color.rgb(33, 150, 243), // Blue (100)
        Color.rgb(244, 67, 54), //Red(Lost Turn)
        Color.rgb(76, 175, 80), //Green (200)
        Color.rgb(33, 150, 243), // Blue (100)
        Color.rgb(255, 193, 7), //Gold (500)
        Color.rgb(0, 0, 0) // Black(Bankrupt )
    };

    private SpinCompleteListener spinListener;
    private boolean isSpinning = false;

    

    public Wheel(double size) {
        rand = new Random();

        //Creates the canvas for the wheel
        canvas = new Canvas(size, size);
        rotation = new Rotate(0, size / 2, size / 2);
        canvas.getTransforms().add(rotation);

        //Creates the pointer for the wheel segments
        pointerCanvas = new Canvas(size, size + 50);
        drawPointer(pointerCanvas, size);

        getChildren().addAll(canvas, pointerCanvas);

        drawWheel();
    }

    private void drawPointer(Canvas pointerCanvas, double size) {
        GraphicsContext gc = pointerCanvas.getGraphicsContext2D();

        double centerX = size / 2;
        double pointerWidth = 15;
        double pointerHeight = 30;

        gc.setFill(Color.RED);
        gc.setStroke(Color.DARKRED);
        gc.setLineWidth(3);

        gc.beginPath();
        gc.moveTo(centerX, pointerHeight + 20);
        gc.lineTo(centerX - pointerWidth, 10);
        gc.lineTo(centerX + pointerWidth, 10);
        gc.closePath();
        gc.fill();
        gc.stroke();
    }

    private void drawWheel() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        double centerX = canvas.getWidth() / 2;
        double centerY = canvas.getHeight() / 2;
        double radius = canvas.getWidth() / 2 - 10;

        //Clear the canvas
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

        double anglePerSegment = 360.0 / SEGMENT_VALUES.length;

        //Draw each segment
        for(int i = 0; i < SEGMENT_VALUES.length; i++) {

            //Calculate angle offset by half segment to center first segment at top
            double startAngle = 90 + (i * anglePerSegment) - (anglePerSegment / 2);

            //Fill in the colors for each segment
            gc.setFill(SEGMENT_COLORS[i]);
            gc.fillArc(centerX - radius, centerY - radius, radius * 2, radius * 2, startAngle, anglePerSegment, javafx.scene.shape.ArcType.ROUND);

            //Border for each segment
            gc.setStroke(Color.WHITE);
            gc.setLineWidth(3);
            gc.strokeArc(centerX - radius, centerY - radius, radius * 2, radius * 2, startAngle, anglePerSegment, javafx.scene.shape.ArcType.ROUND);
            
        }
        
        //Prints the text on each segment
        for(int i = 0; i < SEGMENT_VALUES.length; i++) {
            double angle = 90 + (i * anglePerSegment);

            gc.save();

            gc.translate(centerX, centerY);
            gc.rotate(angle);

            gc.setFill(Color.WHITE);
            gc.setStroke(Color.BLACK);
            gc.setLineWidth(1);
            gc.setFont(Font.font("Times New Roman", FontWeight.BOLD, 16));
            gc.setTextAlign(TextAlignment.CENTER);
            gc.setTextBaseline(VPos.CENTER);

            //Print text in the middle of each segment
            double textRadius = radius * 0.70;

            //Text with outline which points outward from center
            gc.strokeText(SEGMENT_VALUES[i], textRadius, 0);
            gc.fillText(SEGMENT_VALUES[i], textRadius, 0);

            gc.restore();
        }

        //Center Circle
        double centerCircleRadius = radius * 0.15;
        gc.setFill(Color.rgb(200, 200, 200));
        gc.fillOval(centerX - centerCircleRadius, centerY - centerCircleRadius, centerCircleRadius * 2, centerCircleRadius *2);
        gc.setStroke(Color.DARKGRAY);
        gc.setLineWidth(2);
        gc.strokeOval(centerX - centerCircleRadius, centerY - centerCircleRadius, centerCircleRadius * 2, centerCircleRadius *2);
    }

    public void spin() {
        if (isSpinning) return;

        isSpinning = true;

        //3-6 full rotations, random. Plus random final position.
        double spins  = 3 + rand.nextInt(4);
        double extraRotation = rand.nextDouble() * 360;
        double totalRotation = rotation.getAngle() + (spins * 360) + extraRotation;

        //Spinning Annimation!!
        Timeline timeline = new Timeline(new KeyFrame(Duration.ZERO, new KeyValue(rotation.angleProperty(), rotation.getAngle())), new KeyFrame(Duration.seconds(4), new KeyValue(rotation.angleProperty(), totalRotation)));

        timeline.setOnFinished(event -> {
            isSpinning = false;
            String result = getSegmentAtPointer();
            if(spinListener != null) {
                spinListener.onSpinComplete(result);
            }
        });

        timeline.play();
            
    
    }

    private String getSegmentAtPointer() {
        //Normalize the angle from 0-360
        double normalizedAngle = rotation.getAngle() % 360;
        if(normalizedAngle < 0) {
            normalizedAngle += 360;
        }

        double anglePerSegment = 360.0 / SEGMENT_VALUES.length;

        //Pointer is at top (90 degrees)
        //Subtract 90 to shift from  3 o'clock to 12 o'clock
        //Then, add half segment to account for offset
        double pointerAngle = 180;
        double relativeAngle = (pointerAngle - normalizedAngle) % 360;
        if(relativeAngle < 0) {
            relativeAngle += 360;
        }

        //Adjust for the segment offset(segments are centered)
        double adjustedAngle = (relativeAngle + (anglePerSegment / 2)) % 360;

        int segmentIndex = (int) (adjustedAngle / anglePerSegment);

        //Simple error checking
        if(segmentIndex >= SEGMENT_VALUES.length) {
            segmentIndex = 0;
        }

        return SEGMENT_VALUES[segmentIndex];
    }

    public void setOnSpinComplete(SpinCompleteListener listener) {
        this.spinListener = listener;
    }

    public boolean isSpinning() {
        return isSpinning;
    }

    @FunctionalInterface
    public interface SpinCompleteListener {
        void onSpinComplete(String result);
    }
    
}

