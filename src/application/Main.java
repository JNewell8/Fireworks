package application;

import javafx.util.Duration;

import java.util.ArrayList;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;

public class Main extends Application {
	private static int wind = 0;
	private static int launchAngle1 = 0;
	private static int launchAngle2 = 0;
	private static Timeline timeline;
	private static ParticleManager manager1;
	private static ParticleManager manager2;

	private final double TIME_INTERVAL = 0.02;
	private AnchorPane root;
	private static double time = 0;
	private static double time2 = 0;
	private static ArrayList<Particle> fireworks;
	private static ArrayList<Particle> fireworks2;
	protected static Canvas canvas;
	private static GraphicsContext gc;
	protected static Boolean checked = false;
	private BurningParticle Star1;
	public static final Color BACKGROUND_COLOUR = Color.gray(.35);
	@Override
	public void start(Stage primaryStage) {
		try {
			try {
				manager1 = new ParticleManager(wind, launchAngle1);
				manager1.start(time);
				manager2 = new ParticleManager(wind, launchAngle1);
				manager2.start(time);
			} catch (EnvironmentException except) {
				System.out.println(except.getMessage());
				return;
			} catch (EmitterException except) {
				System.out.println(except.getMessage());			
				return;
			}

			root = (AnchorPane)FXMLLoader.load(getClass().getResource("../Assn4FXML.fxml"));
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.setHeight(root.getPrefHeight()+80);
			primaryStage.setWidth(root.getPrefWidth());
			primaryStage.setMinHeight(root.getPrefHeight()+80);
			primaryStage.setMinWidth(root.getPrefWidth());

			primaryStage.setTitle("Assignment 4: Fireworks");
			timeline = new Timeline(
					new KeyFrame(Duration.ZERO, actionEvent -> {
						drawFireworks();
						if (checked && Assn4Controller.start) drawFireworks2();
					}),
					// Set the frame rate to match the time interval
					new KeyFrame(Duration.millis(1000*TIME_INTERVAL)));
			timeline.setCycleCount(Timeline.INDEFINITE);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	private void drawFireworks() {
		// wipe the canvas using the BACKGROUND_COLOUR color
		gc.setFill(BACKGROUND_COLOUR);
		gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
		if (fireworks.size() > 0){
			for (Particle firework : fireworks) {
				// Get the position for the Particle
				double[] pos = firework.getPosition();
				if (firework instanceof Streak){
					// Streaks are lines from the firework to the origin
					double[] origin = ((Streak) firework).getOrigin();
					gc.setFill(spreadColor(firework.getColour()));
					gc.strokeLine(xPos(origin[0]),yPos(origin[1]),xPos(pos[0]),yPos(pos[1]));
				}
				else if (firework instanceof BurningParticle){
					// Burning Particles are the stars themselves
					// Saves the star object to test for a collision later
					Star1 = (BurningParticle) firework.clone();
					gc.setFill(firework.getColour());
					gc.fillOval(xPos(pos[0]),yPos(pos[1]), 6, 6);
				}
				else {
					// Everything else is a spark and can have a random color / transparency 
					gc.setFill(spreadColor(firework.getColour()));
					gc.fillOval(xPos(pos[0]),yPos(pos[1]), 1 + 3*Math.random(), 1 + 3*Math.random());
				}
			}
			// update the current time and the fireworks attribute
			time = (time + TIME_INTERVAL);
			fireworks = manager1.getFireworks(time);
		}
	}
	private void drawFireworks2() {
		if (fireworks2.size() > 0){
			Boolean check = false;
			for (Particle firework : fireworks2) {
				// Get the position for the Particle
				double[] pos = firework.getPosition();
				if (firework instanceof Streak){
					// Streaks are lines from the firework to the origin
					double[] origin = ((Streak) firework).getOrigin();
					gc.setFill(spreadColor(firework.getColour()));
					gc.strokeLine(xPos(origin[0]),yPos(origin[1]),xPos(pos[0]),yPos(pos[1]));
				}
				else if (firework instanceof BurningParticle){
					// Burning Particles are the stars themselves
					double[] firework1 = Star1.getPosition();
					// if the two stars are too close, they will crash
					if ((Math.abs(pos[0] - firework1[0]) < 1.5) && (Math.abs(pos[1] - firework1[1]) < 1.5)){
						check = true;
					}
					gc.setFill(firework.getColour());
					gc.fillOval(xPos(pos[0]),yPos(pos[1]), 6, 6);
				}
				else {
					// Everything else is a spark and can have a random color / transparency 
					gc.setFill(spreadColor(firework.getColour()));
					gc.fillOval(xPos(pos[0]),yPos(pos[1]), 1 + 3*Math.random(), 1 + 3*Math.random());
				}
			}
			if (check){
				manager1.collision(time);
				manager2.collision(time2);
			}
			// update the current time and the fireworks attribute
			time2 = (time2 + TIME_INTERVAL);
			fireworks2 = manager2.getFireworks(time2);
		}
	}

	// Gives a random colour within a certain spread of the original colour with a 
	// small amount of transparency 
	private Color spreadColor(Color oldColor){
		double spread = 0.8;
		Color newColor = new Color(oldColor.getRed()*(spread)*Math.random(),oldColor.getGreen()*
				(spread)*Math.random(),oldColor.getBlue()*(spread)*Math.random(),0.4*Math.random());
		return newColor.brighter().brighter();
	}
	// Setter for the wind (affects the manager1 object)
	public static void setWind(int windNEW){
		wind = windNEW;
		try {
			manager1.setWindVelocity(windNEW);
			manager2.setWindVelocity(windNEW);
		} catch (EnvironmentException e) {
			e.printStackTrace();
		}

	}
	// Setter for the angle (affects the manager1 object)
	public static void setAngle1(int angle){
		launchAngle1 = angle;
		try {
			manager1.setLaunchAngle(angle);
		} catch (EmitterException e) {
			e.printStackTrace();
		}
	}
	public static void setAngle2(int angle){
		launchAngle2 = angle;
		try {
			manager2.setLaunchAngle(angle);
		} catch (EmitterException e) {
			e.printStackTrace();
		}
	}
	// Converts the X position in meters to pixels
	private double xPos(double xMetres){
		return xMetres*14+canvas.getWidth()/2;
	}
	// Converts the Y position in meters to pixels
	private double yPos(double yMetres){
		return -yMetres*14+canvas.getHeight();
	}
	//Sets the tip of the launch tube
	public static void setTip(double X1, double Y1, double X2, double Y2,double num) {
		double[] pos1 = new double[2];
		pos1[0] = X1/(14) - canvas.getWidth()/3*(num-2)/26.5;
		pos1[1] = Y1/14;
		manager1.setTipPosition(pos1);
		double[] pos2 = new double[2];
		pos2[0] = X2/(14) + canvas.getWidth()/3*(num-2)/26.5;
		pos2[1] = Y2/14;
		manager2.setTipPosition(pos2);
	}
	// Restarts the animation 
	public static void reset(){
		gc = canvas.getGraphicsContext2D();
		gc.setLineWidth(1);
		time = 0;
		try {
			double[] tip = manager1.getTipPosition();
			manager1 = new ParticleManager(wind, launchAngle1);
			manager1.setTipPosition(tip);
			manager1.start(time);
		} catch (EnvironmentException except) {
			System.out.println(except.getMessage());
			return;
		} catch (EmitterException except) {
			System.out.println(except.getMessage());			
			return;
		}
		fireworks = manager1.getFireworks(time);
		reset2();
		timeline.playFromStart();
	}

	public static void isStart(){
		if (Main.checked){
			//Assn4Controller.start = true;
			checked = true;
		}
		else
			//Assn4Controller.start = false;
			checked = false;
	}
	public static void reset2() {
		time2 = 0;
		try {
			double[] tip = new double[2];
			tip[0] = canvas.getWidth()/(3*26.5);
			tip[1] = 43.0/14;
			manager2 = new ParticleManager(wind, launchAngle2);
			manager2.setTipPosition(tip);
			manager2.start(time2);
		} catch (EnvironmentException except) {
			System.out.println(except.getMessage());
			return;
		} catch (EmitterException except) {
			System.out.println(except.getMessage());			
			return;
		}
		fireworks2 = manager2.getFireworks(time2);
	}
	public static void main(String[] args) {
		launch(args);
	}
}