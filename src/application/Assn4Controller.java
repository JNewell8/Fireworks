package application;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;

public class Assn4Controller {

	@FXML
	private Slider angleSlider1;

	@FXML
	private FlowPane flowPane;

	@FXML
	private Slider windSlider;

	@FXML
	private TextField windField;

	@FXML
	private Slider angleSlider2;

	@FXML
	private Button startButton;

	@FXML
	private Canvas mainCanvas;

	@FXML
	private Canvas subCanvas;

	@FXML
	private Button exitButton;

	@FXML
	private AnchorPane anchorPane;

	@FXML
	private Rectangle launcher1;

	@FXML
	private Label angleName2;

	@FXML
	private Label windLabel;

	@FXML
	private TextField angleField2;

	@FXML
	private TextField angleField1;

	@FXML
	private Label angleLabel1;

	@FXML
	private CheckBox checkBox;

	@FXML
	private Label angleLabel2;

	@FXML
	private AnchorPane mainPane;


	private double X1;
	private double Y1;
	private double X2;
	private double Y2;
	private Rectangle launcher2;
	private Integer windSpeed = 0; 
	private Integer angle1 = 0;
	private Integer angle2 = 0;
	private double num = 2;
	public static Boolean start = false;
	private GraphicsContext gc;
	private final double OPACITY = 0.4;
	private final int ANGLE_MAX = 15;
	private final int WIND_MAX = 20;
	private Rotate rot1;
	private Rotate rot2;

	@FXML
	void initialize() {
		assert subCanvas != null : "fx:id=\"subCanvas\" was not injected: check your FXML file 'Assn4FXML.fxml'.";
		assert exitButton != null : "fx:id=\"exitButton\" was not injected: check your FXML file 'Assn4FXML.fxml'.";
		assert anchorPane != null : "fx:id=\"anchorPane\" was not injected: check your FXML file 'Assn4FXML.fxml'.";
		assert windSlider != null : "fx:id=\"windSlider\" was not injected: check your FXML file 'Assn4FXML.fxml'.";
		assert windLabel != null : "fx:id=\"windLabel\" was not injected: check your FXML file 'Assn4FXML.fxml'.";
		assert startButton != null : "fx:id=\"startButton\" was not injected: check your FXML file 'Assn4FXML.fxml'.";
		assert mainCanvas != null : "fx:id=\"mainCanvas\" was not injected: check your FXML file 'Assn4FXML.fxml'.";
		assert angleLabel1 != null : "fx:id=\"angle1Label1\" was not injected: check your FXML file 'Assn4FXML.fxml'.";
		assert mainPane != null : "fx:id=\"mainPane\" was not injected: check your FXML file 'Assn4FXML.fxml'.";
		assert angleSlider1 != null : "fx:id=\"angle1Slider\" was not injected: check your FXML file 'Assn4FXML.fxml'.";
		assert launcher1 != null : "fx:id=\"launcher1\" was not injected: check your FXML file 'Assn4FXML.fxml'.";

		angleName2.setOpacity(OPACITY);
		angleLabel2.setOpacity(OPACITY);
		angleSlider1.setMax(ANGLE_MAX);
		angleSlider2.setMax(ANGLE_MAX);
		windSlider.setMax(WIND_MAX);
		angleSlider1.setMin(-ANGLE_MAX);
		angleSlider2.setMin(-ANGLE_MAX);
		windSlider.setMin(-WIND_MAX);
		gc = subCanvas.getGraphicsContext2D();
		launcher2 = new Rectangle(0,0,launcher1.getWidth(),launcher1.getHeight());
		launcher2.setFill(Color.GOLDENROD);
		launcher2.setStroke(Color.BLACK);
		resize();
		// launcher rotation for when angle is changed
		rot1 = new Rotate(angle1, launcher1.getWidth() / 2, launcher1.getHeight(), 0, Rotate.Z_AXIS);
		launcher1.getTransforms().add(rot1);
		rot2 = new Rotate(angle2, launcher1.getWidth() / 2, launcher2.getHeight(), 0, Rotate.Z_AXIS);
		launcher2.getTransforms().add(rot2);
		// When the window's size changes the size of the canvas changes and thus
		// the background must be updated
		anchorPane.widthProperty().addListener(observable -> resize());
		anchorPane.heightProperty().addListener(observable -> resize());
		// Check box used to see if the user wants one or two launch tubes
		checkBox.setOnAction(event -> {
			if (checkBox.isSelected()){
				flowPane.getChildren().add(launcher2);
				// Ratio of displacements for the two launchers
				num = 3;
				// Disable controls when unchecked
				angleName2.setOpacity(1);
				angleLabel2.setOpacity(1);
				angleSlider2.setDisable(false);
				angleField2.setDisable(false);
				Main.checked = true;
				resize();
			}
			else {
				flowPane.getChildren().remove(1);
				// Ratio of displacements for the one launcher
				num = 2;
				// Enable controls when checked
				angleName2.setOpacity(OPACITY);
				angleLabel2.setOpacity(OPACITY);
				angleSlider2.setDisable(true);
				angleField2.setDisable(true);
				Main.reset2();
				start = false;
				Main.checked = false;
				resize();
			}
		});

		// If the wind speed is changed the text value and the manager wind speed is changed
		windSlider.valueProperty().addListener((observableValue, oldVal, newVal) -> {
			windSpeed = newVal.intValue();
			Main.setWind(windSpeed);
			windField.setText(windSpeed.toString());
		});
		// If the angle is changed the text value and the manager angle is changed
		angleSlider1.valueProperty().addListener((observableValue, oldVal, newVal) -> {
			angle1 = newVal.intValue();
			angleField1.setText(angle1.toString());
			rot1.setAngle(angle1);
			setLaunch();
		});
		angleSlider2.valueProperty().addListener((observableValue, oldVal, newVal) -> {
			angle2 = newVal.intValue();
			angleField2.setText(angle2.toString());
			rot2.setAngle(angle2);
			setLaunch();
		});
		// When any of the text boxes are changed the value is changed (either by de-selecting
		// the box or pressing enter)
		angleField1.focusedProperty().addListener((obsVal, oldVal, newVal) ->{
			if (!newVal)
				angle1Change();
		});
		angleField1.setOnKeyPressed(event -> {
			if (event.getCode() == KeyCode.ENTER)
				angle1Change();
		});
		angleField2.focusedProperty().addListener((obsVal, oldVal, newVal) ->{
			if (!newVal)
				angle2Change();
		});
		angleField2.setOnKeyPressed(event -> {
			if (event.getCode() == KeyCode.ENTER)
				angle2Change();
		});
		windField.setOnKeyPressed(event -> {
			if (event.getCode() == KeyCode.ENTER)
				windChange();
		});	
		windField.focusedProperty().addListener((obsVal, oldVal, newVal) ->{
			if (!newVal)
				windChange();
		});

		// Starts annimation on S, exits on E
		mainPane.setOnKeyPressed(event -> {
			if (event.getCode() == KeyCode.E)
				exit();
			else if (event.getCode() == KeyCode.S)
				try {
					start();
				} catch (Exception e) {
					e.printStackTrace();
				}
		});

		startButton.setOnAction(event -> start());
		exitButton.setOnAction(event -> exit());
	}
	// Checks to see if the input is valid and changes the angle value accordingly
	private void angle1Change(){
		double angle;
		String newVal = angleField1.getText();
		try {
			angle = Math.round(Float.parseFloat(newVal));
		} catch( Exception e){
			angle = 0;
		}
		if (angle > ANGLE_MAX)
			angle1 = ANGLE_MAX;
		else if (angle < -ANGLE_MAX)
			angle1 = -ANGLE_MAX;
		else
			angle1 = (int) angle;
		angleSlider1.setValue(angle1);
		angleField1.setText(angle1.toString());
		rot1.setAngle(angle1);
		setLaunch();
	}
	private void angle2Change(){
		double angle;
		String newVal = angleField2.getText();
		try {
			angle = Math.round(Float.parseFloat(newVal));
		} catch( Exception e){
			angle = 0;
		}
		if (angle > ANGLE_MAX)
			angle2 = ANGLE_MAX;
		else if (angle < -ANGLE_MAX)
			angle2 = -ANGLE_MAX;
		else
			angle2 = (int) angle;
		angleSlider2.setValue(angle2);
		angleField2.setText(angle2.toString());
		rot2.setAngle(angle2);
		setLaunch();
	}
	// Checks to see if the input is valid and changes the wind value accordingly
	private void windChange(){
		double wind;
		String newVal = windField.getText();
		try {
			wind = Math.round(Float.parseFloat(newVal));
		} catch( Exception e){
			wind = 0;
		}
		if (wind > WIND_MAX)
			windSpeed = WIND_MAX;
		else if (wind < -WIND_MAX)
			windSpeed = -WIND_MAX;
		else
			windSpeed = (int) wind;
		windSlider.setValue(windSpeed);
		windField.setText(windSpeed.toString());
		setLaunch();
	}
	// On start resets the tip position and the animation 
	private void start() {
		if (Main.checked)
			start = true;			
		startButton.setText("Restart [S]");
		Main.reset();
		setLaunch();
	}
	// On exit, closes window and terminates program
	private void exit(){
		Stage stage = (Stage) exitButton.getScene().getWindow();
		stage.close();
		System.exit(0);
	}
	// Resizes the canvas and the sub-canvas. Fills the background canvas so as to not 
	// disturb the animation
	private void resize(){
		mainCanvas.setWidth(anchorPane.getWidth());
		mainCanvas.setHeight(anchorPane.getHeight());
		subCanvas.setWidth(anchorPane.getWidth());
		subCanvas.setHeight(anchorPane.getHeight());
		gc.setFill(Main.BACKGROUND_COLOUR);		
		gc.fillRect(0, 0, mainCanvas.getWidth(), mainCanvas.getWidth());
		Main.canvas = mainCanvas;
		flowPane.setHgap(mainCanvas.getWidth()/3.0);
		setLaunch();
	}
	// Uses launcher position to set the tip position
	private void setLaunch(){
		Main.setAngle1(angle1);
		X1 = launcher1.getHeight()*Math.sin(Math.toRadians(angle1));
		Y1 = launcher1.getHeight()*Math.cos(Math.toRadians(angle1));
		if (!checkBox.isDisabled()){
			Main.setAngle2(angle2);
			X2 = launcher2.getHeight()*Math.sin(Math.toRadians(angle2));
			Y2 = launcher2.getHeight()*Math.cos(Math.toRadians(angle2));
		}
		Main.setTip(X1,Y1,X2,Y2,num);
	}
}

