package application;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Pane;

import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class Mycontroller {
	
	@FXML
	public Pane pane;
	public Label check;
	public TextArea fileArea;
	public TextArea analysArea;
	public TextArea errorArea;
	public HashMap<String, Integer> stringMap;
	public HashMap<Character, Integer> charMap;
	
	public void inti()//往map添加种别码
	{
		stringMap = new HashMap<>();
		charMap = new HashMap<>();
		String[] s1 = {"begin","if","then","while","do","end","for","new","break","continue","ture","false"};

		char[] c = {'+','-','*','/','>','<','=',',','"',';','(',')','{','}','[',']','.',':','\''};
		int number = 1;
		for(int i =0;i<s1.length;i++)
		{
			stringMap.put(s1[i], number++);
		}
		for(int i=0;i<c.length;i++)
		{
			charMap.put(c[i], number++);
		}
		
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
	
	public void analyse()//分析单词
	{
		analysArea.clear();//再次分析先清空
		errorArea.clear();

		int errorCounts = 0;//错误数
		int lines = 0;//行数
		
		analysArea.appendText("-------------------token表信息...--------------------"+"\n\n");
		errorArea.appendText("-------------------词法分析错误信息...--------------------"+"\n\n");
		
		String str = fileArea.getText();
		String[] strings1 = str.split("\n|\r");//以换行符分割字符串
		boolean flag =true;//对多行注释的判断
		for(String st:strings1)
		{
			st = st.trim();
			
			lines++;
			if(st.isEmpty())//整行都是空格
				continue;
			
			char[] arrChar = st.toCharArray();//将一行的字符串划分为字符数组
			StringBuilder sb = new StringBuilder();
			
			for(int i = 0;i<arrChar.length;i++)
			{
				if(flag)
				{
					if(arrChar[i]==' ')
					{
						String s = sb.toString();//空格分隔，将之前的字符串拿出来验证并清空
						if(!s.trim().isEmpty())
						{
							if(isNumber(s))
							{
								analysArea.appendText(lines + ":    " +s +"       "+53+"\n");
							}
							else if(isIdentifier(s))
							{
								analysArea.appendText(lines + ":    " +s +"       "+58+"\n");
							}
							else 
							{
								errorCounts++;
								errorArea.appendText(lines +": 无法识别的字符:   " +s +"\n");
							}
						}
						
						sb.delete(0, sb.length());
						continue;//遇到空格跳过
					}
				}
					
				
				if(i+1<arrChar.length)
				{
					if(arrChar[i]=='/' && arrChar[i+1]=='/')//单行注释跳过该行
						break;
					if(arrChar[i]=='/' && arrChar[i+1]=='*')//遇到多行注释的开始，将flag设置为false
					{
						flag = false;
//						i+=1;会越界
					}
					if(arrChar[i]=='*' && arrChar[i+1]=='/')//多行注释的结尾，
					{
						flag = true;
					}
				}
				if(flag)
				{
					if(charMap.containsKey(arrChar[i]))//单个字符是否是关键字
					{
						String s = sb.toString();//由于中间有关键字，将之前的字符串拿出来验证并清空
						if(!s.trim().isEmpty())
						{
							if(isNumber(s))
							{
								analysArea.appendText(lines + ":    " +s +"       "+53+"\n");
							}
							else if(isIdentifier(s))
							{
								analysArea.appendText(lines + ":    " +s +"       "+58+"\n");
							}
							else 
							{
								errorCounts++;
								errorArea.appendText(lines +": 无法识别的字符:   " +s +"\n");
							}
						}
						
						analysArea.appendText(lines + ":    " +arrChar[i]+"       "+charMap.get(arrChar[i])+"\n");
	
						sb.delete(0, sb.length());
						continue;
					}
					else //将其加入字符串
					{
						sb.append(arrChar[i]);
					}
					if(stringMap.containsKey(sb))//字符串是否是关键字
					{
						analysArea.appendText(lines + ":    " +sb+"       "+stringMap.get(sb)+"\n");
						sb.delete(0, sb.length());
					}
					
				}
			}
			String s = sb.toString();//一行的末尾没有关键字，剩余的字符串就没有被判定，需要将字符串取出判定
			if(!s.trim().isEmpty())
			{
				if(isNumber(s))
				{
					analysArea.appendText(lines + ":    " +s +"       "+53+"\n");
				}
				else if(isIdentifier(s))
				{
					analysArea.appendText(lines + ":    " +s +"       "+58+"\n");
				}
				else 
				{
					errorCounts++;
					errorArea.appendText(lines +": 无法识别的字符:   " +s +"\n");
				}
			}
		}
		
		
		errorArea.appendText("\n词法分析结束   -   "+errorCounts+ "error(s)");
	}
	
	public boolean isNumber(String s)//是否是整数
	{
		
		if(s !=null && s.matches("^[0.0-9.0]+$"))
			return true;
		else 
			return false;
	}
	
	public boolean isIdentifier(String s)//是否是标识符
	{
		
		char c;
		int flag = 1;
		for(int i=0;i<s.length();i++)
		{
			c = s.charAt(i);
			if(i==0)
			{
				if(Character.isJavaIdentifierStart(c))
					flag = 1;
				else {
					flag = 0;
					break;
				}
			}
			else {
				if(Character.isJavaIdentifierPart(c))
					flag = 1;
				else {
					flag = 0;
					break;
				}
			}
				
		}
		return flag == 1? true:false;
	}
	
}
