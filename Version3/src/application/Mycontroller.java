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
								analysArea.appendText(lines + ":\t" +s +"\t\t\t"+53+"\n");
								
							}
							else if(isIdentifier(s))
							{
								analysArea.appendText(lines + ":\t" +s +"\t\t\t"+58+"\n");
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
								analysArea.appendText(lines + ":\t" +s +"\t\t\t"+53+"\n");
							}
							else if(isIdentifier(s))
							{
								analysArea.appendText(lines + ":\t" +s +"\t\t\t"+58+"\n");
							}
							else 
							{
								errorCounts++;
								errorArea.appendText(lines +": 无法识别的字符:   " +s +"\n");
							}
						}
						
						analysArea.appendText(lines + ":\t" +arrChar[i]+"\t\t\t"+charMap.get(arrChar[i])+"\n");
	
						sb.delete(0, sb.length());
						continue;
					}
					else //将其加入字符串
					{
						sb.append(arrChar[i]);
					}
					if(stringMap.containsKey(sb))//字符串是否是关键字
					{
						analysArea.appendText(lines + ":\t" +sb+"\t\t\t"+stringMap.get(sb)+"\n");
						sb.delete(0, sb.length());
					}
					
				}
			}
			String s = sb.toString();//一行的末尾没有关键字，剩余的字符串就没有被判定，需要将字符串取出判定
			if(!s.trim().isEmpty())
			{
				if(isNumber(s))
				{
					analysArea.appendText(lines + ":\t" +s +"\t\t\t"+53+"\n");
				}
				else if(isIdentifier(s))
				{
					analysArea.appendText(lines + ":\t" +s +"\t\t\t"+58+"\n");
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
	
	char curChar;//算术分析所需全局变量
	int index = 0;
	String string;
	int flag=0;//记录调用方法路径，方便调试用的
	
	///递归下降算术分析方法
	public void mathMatch()
	{
		analysArea.clear();
		errorArea.clear();
		index = 0;flag = 0;//多次点击需要重置下标
		
		string = fileArea.getText();
		string.trim();//清空前后空白
		curChar = string.charAt(index);

		E();
		
		if(errorArea.getText().equals(""))
		{
			if(curChar==';')
				analysArea.appendText("Success!");
			else 
				analysArea.appendText("Fail!");
		}
		else 
		{
			analysArea.appendText("Fail!");
		}
		///
	}
	public char nextChar(String string)
	{
		return string.charAt(index);
	}
	public void E()//功能函数
	{
//		System.out.println("E()"+ ++flag);
		T();
		E_();
	}
	public void T()//功能函数
	{
//		System.out.println("T()"+ ++flag);
		F();
		T_();
	}
	public void E_()//功能函数
	{
//		System.out.println("E_()"+ ++flag);
		if(curChar=='+')
		{
			index++;
			curChar = nextChar(string);
			
			T();
			E_();
		}
		else if(curChar!=')' && curChar!=';'&& curChar!=' ')
		{
			errorArea.appendText("\n算术分析结束   -E_()   "+curChar+"  不在此文法之内or不是算法表达符");
			return ;
		}
	}
	public void T_()//功能函数
	{
//		System.out.println("T_()"+ ++flag);
		if(curChar=='*')
		{
			index++;
			curChar =nextChar(string);
			F();
			T_();
		}
		else if(curChar!='+' && curChar!=')' && curChar!=';'&& curChar!=' ')
		{
			errorArea.appendText("\n算术分析结束   -T_()   "+curChar+"  不在此文法之内or不是算法表达符");
			return ;
		}
	}
	public void F()//功能函数
	{
//		System.out.println("F()"+ ++flag);
		if(curChar=='(')
		{
			index++;
			curChar =nextChar(string);
			
			E();
			if(curChar==')')
			{
				index++;
				curChar =nextChar(string);
				
			}
			else 
			{
				errorArea.appendText("\n算术分析结束   -F()1   "+curChar+" 前可能缺少 )");
				return ;
			}
		}
		else if(curChar=='i')
		{
			index++;
			curChar =nextChar(string);
		}
		else 
		{
			if(curChar!='+' && curChar!='-' && curChar!='*' && curChar!='/')
				errorArea.appendText("\n算术分析结束   -F()2   "+curChar+"非算术表达符");
			else 
				errorArea.appendText("\n算术分析结束   -F()2   "+curChar+"前已有运算符，不支持连续运算符");
			return ;
		}
	}
	public String[] strs;
	///布尔表达式分析
	public void buerMatch()
	{
		analysArea.clear();
		errorArea.clear();
		index = 0;flag = 0;//多次点击需要重置下标
		
		string = fileArea.getText();
		string.trim();//清空前后空白
		
		strs = string.split(" ");
		E2();
		
		if(errorArea.getText().equals(""))
		{
			if(strs[index].equals(";"))
				analysArea.appendText("Success!");
			else 
			{
				analysArea.appendText("Fail!");
				errorArea.appendText("\n算术分析结束   -   "+"结尾缺少终结符，如（；）");
			}
				
		}
		else 
		{
			analysArea.appendText("Fail!");
		}
	}
	public void E2()
	{
		T2();
		E2_();
	}
	public void E2_()
	{
		if(strs[index].equals("or"))
		{
			index++;
			T2();
			E2_();
		}
		else if(!strs[index].equals(")") && !strs[index].equals(";") && !strs[index].equals(" "))
		{
			errorArea.appendText("\n算术分析结束   -   "+strs[index]+"  不在此文法之内or不是算法表达符");
			return ;
		}
	}
	public void T2()
	{
		F2();
		T2_();
	}
	public void T2_()
	{
		if(strs[index].equals("and"))
		{
			index++;
			T2();
			E2_();
		}
		else if(!strs[index].equals(")") && !strs[index].equals(";") && !strs[index].equals(" "))
		{
			errorArea.appendText("\n算术分析结束   -   "+strs[index]+"  不在此文法之内or不是算法表达符");
			return ;
		}
	}
	public void F2()
	{
		if(strs[index].equals("not"))
		{
			index++;
			F2();
		}
		else if(strs[index].equals("true"))
		{
			index++;
			return ;
		}
		else if(strs[index].equals("false"))
		{
			index++;
			return ;
		}
		else if(strs[index].equals("("))
		{
			index++;
			E2();
			if(strs[index].equals(")"))
			{
				index++;
				return ;
			}
			else 
			{
				errorArea.appendText("\n算术分析结束   -   "+strs[index]+" 前可能缺少 )");
				return ;
			}
		}
		else 
		{
			if(strs[index].equals("i"))
			{
				index++;
				if(strs[index].equals(">") || strs[index].equals("<") || strs[index].equals("=="))
				{
					index++;
					if(strs[index].equals("i"))
					{
						index++;
						return ;
					}
					else 
					{
						errorArea.appendText("\n算术分析结束   -   "+strs[index]+"应该为i，关系运算符后应该为数字");
					}
				}
				else 
				{
					errorArea.appendText("\n算术分析结束   -   "+strs[index]+"应该为关系运算符");
				}
			}
			else if(!strs[index].equals(" "))
			{
				errorArea.appendText("\n算术分析结束   -   "+strs[index]+"不在布尔表达式文法内or连续使用布尔运算符");
				return ;
			}
		}
	}
}
