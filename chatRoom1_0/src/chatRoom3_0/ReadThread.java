package chatRoom3_0;

import java.io.*;
import java.net.*;

public class ReadThread extends Thread{
    public Socket socket1 = null,socket2 = null;
    public String name,othername;
    public ReadThread(String name1,Socket socket1,String name2,Socket socket2) {
    	this.socket1 = socket1;
    	this.socket2 = socket2;
    	this.name = name1;
    	this.othername = name2;
    	
    }
    public void run() {
    	try{
			String line;
			//由Socket对象得到输入流，并构造相应的BufferedReader对象
			BufferedReader is1=new BufferedReader(new InputStreamReader(socket1.getInputStream()));
			//在标准输出上打印从客户端读入的字符串
			line = is1.readLine();
			System.out.println(othername + line);
			//从标准输入读入一字符串
			while(!line.equals("bye")){//如果该字符串为 "bye"，则停止循环
				System.out.println(othername + ":" + line);
			   line=is1.readLine();//从系统标准输入读入一字符串
			}//继续循环
			is1.close(); //关闭Socket输入流		
		}catch(Exception e){
			System.out.println("Error:"+e);//出错，打印出错信息
		}
    }
}
