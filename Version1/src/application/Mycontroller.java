package application;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class Mycontroller {
	
	@FXML
	public Pane pane;
	public Label check;
	
	public TextArea fileArea;
	public TextArea analysArea;
	public TextArea errorArea;
	
	public void change()
	{
		check.setTextFill(Color.RED);
	}
	
	
	public void help() throws IOException
	{
		//Runtime.getRuntime().exec("四月计划.chm");
	}
	
	public void openfile() throws IOException//打开文件
	{
		FileChooser fileChooser = new FileChooser();
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(extFilter);
        fileChooser.setTitle("选择文件");       
        File file = fileChooser.showOpenDialog(new Stage());
        
        if(file!=null)
        {
        	 FileInputStream inputStream = new FileInputStream(file);
        	 BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        	 String string = null;
        	 while((string = bufferedReader.readLine()) != null) 
        	 {
        		 fileArea.appendText(string+"\n");
        	 }
        
        	 inputStream.close();
        	 bufferedReader.close();
        	 System.out.println(file);
        }
        
	}
	
	public void savefile() throws IOException//保存文件
	{
		FileChooser fileChooser = new FileChooser();
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(extFilter);
        fileChooser.setTitle("保存文件");       
        File file = fileChooser.showSaveDialog(new Stage());
        
        if(file == null) return ;
        
        if(file.exists())//文件已存在，则删除覆盖文件
        	file.delete();
        else {
			file.createNewFile();
		}
       
        System.out.println(file);
		String string = fileArea.getText();
		
		FileWriter fileWriter = new FileWriter(file);
		BufferedWriter bw = new BufferedWriter(fileWriter);
		
		bw.write(string);
		bw.close();
		
		
	}
	
	
}
