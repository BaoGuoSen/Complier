package application;
	
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;



public class Main extends Application {
	Mycontroller controller=new Mycontroller();
	@Override
	public void start(Stage primaryStage) {
		try {
			FXMLLoader loader = new FXMLLoader();
	        loader.setLocation(Main.class.getResource("scenebuilder.fxml"));
	        
	        
	        Pane t =loader.load();
	        controller=loader.getController();
	       // controller.start();
			
	        Scene scene = new Scene(t,1115,679);
	        
			primaryStage.setScene(scene);
			primaryStage.setResizable(false);
			primaryStage.setTitle("11703080108-包国森");
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
