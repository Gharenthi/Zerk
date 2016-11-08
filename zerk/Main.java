package zerk;

import javafx.application.Application;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Main extends Application{
	private final int MID_WIDTH = 400;
	private final int LEFT_WIDTH = 250;
	private final int RIGHT_WIDTH = 250;
	private final int WIDTH = MID_WIDTH + LEFT_WIDTH + RIGHT_WIDTH;
	private final int HEIGHT = 450;
	
	Text textField;
	public Text itemDisplay;
	public Text actionDisplay;
	public Text playerDisplay;
	public Text enemyDisplay;
	GameData game;
	
	public static void main(String[] args) {
		launch(args);
	}
	
	public void start(Stage stage){
		stage.setOnCloseRequest((event) -> {
			System.exit(0);
		});
		
		stage.addEventHandler(KeyEvent.ANY, (event) -> {
			if (event.getCode() == KeyCode.ESCAPE) System.exit(0);
		});
		
		SplitPane root = new SplitPane();
		root.setOrientation(Orientation.HORIZONTAL);
		SplitPane left = new SplitPane();
		left.setMinWidth(LEFT_WIDTH);
		left.setOrientation(Orientation.VERTICAL);
		SplitPane right = new SplitPane();
		right.setMinWidth(RIGHT_WIDTH);
		right.setOrientation(Orientation.VERTICAL);
		BorderPane mid = new BorderPane();
		
		root.getItems().addAll(left, mid, right);
		
		textField = new Text();
		textField.getStyleClass().add("text");
		TextField inputBox = new TextField();
		mid.setTop(textField);
		textField.setWrappingWidth(MID_WIDTH);
		mid.setBottom(inputBox);
		inputBox.addEventHandler(KeyEvent.ANY, (event) -> {
			if (event.getCode() == KeyCode.ENTER){
				String str = inputBox.getText();
				if (str != null){
					if (! str.equals("")){
						inputBox.setText(null);
						submitText(str);
					}
				}
			}
		});
		
		itemDisplay = new Text();
		itemDisplay.setWrappingWidth(LEFT_WIDTH);
		ScrollPane itemBox = new ScrollPane();
		itemBox.setContent(itemDisplay);
		actionDisplay = new Text();
		actionDisplay.setWrappingWidth(LEFT_WIDTH);
		ScrollPane actionBox = new ScrollPane();
		actionBox.setContent(actionDisplay);
		
		left.getItems().addAll(itemBox, actionBox);
		
		playerDisplay = new Text();
		playerDisplay.setWrappingWidth(RIGHT_WIDTH);
		ScrollPane playerBox = new ScrollPane();
		playerBox.setContent(playerDisplay);
		enemyDisplay = new Text();
		enemyDisplay.setWrappingWidth(RIGHT_WIDTH);
		ScrollPane enemyBox = new ScrollPane();
		enemyBox.setContent(enemyDisplay);
		
		right.getItems().addAll(playerBox, enemyBox);
		
		
		itemDisplay.getStyleClass().add("text");
		actionDisplay.getStyleClass().add("text");
		playerDisplay.getStyleClass().add("text");
		enemyDisplay.getStyleClass().add("text");
		
		itemBox.setHbarPolicy(ScrollBarPolicy.NEVER);
		actionBox.setHbarPolicy(ScrollBarPolicy.NEVER);
		playerBox.setHbarPolicy(ScrollBarPolicy.NEVER);
		enemyBox.setHbarPolicy(ScrollBarPolicy.NEVER);
		
		Scene scene = new Scene(root, WIDTH, HEIGHT);
		scene.getStylesheets().add("resources/stylesheet.css");
		
		stage.setResizable(false);
		stage.setTitle("Ledjndz uv Zerk");
		stage.setScene(scene);
		
		game = new GameData(this);
		
		stage.show();
	}

	private void submitText(String str){
		game.send(str);
	}
	
	public void write(String str){
		textField.setText(textField.getText() + str);
		while (textField.getBoundsInParent().getHeight() > HEIGHT - 10){
			textField.setText(textField.getText().substring(1));
		}
	}
}
