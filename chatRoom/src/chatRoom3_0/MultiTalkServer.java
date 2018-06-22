package chatRoom3_0;

import java.io.*;
import java.net.*;
import java.util.*;

public class MultiTalkServer{
	static int clientnum=0;
	static Hashtable<String,Socket> talk = new Hashtable<String,Socket>(); //静态成员变量，记录当前客户的个数
	public static void main(String args[]) throws IOException {
		ServerSocket serverSocket=null;
		boolean listening=true;
		try{
			//创建一个ServerSocket在端口4700监听客户请求
			serverSocket=new ServerSocket(4008); 			
		}catch(IOException e) {
			System.out.println("Could not listen on port:4008.");
			//出错，打印出错信息
			System.exit(-1); //退出
		}
		while(listening){ //循环监听
			if(clientnum != 0) {
				//监听到客户请求，根据得到的Socket对象和客户计数创建服务线程，并启动之
				Socket socket1 = serverSocket.accept();
				String line1,line2,line3,name,othername;
				//由Socket对象得到输入流，并构造相应的BufferedReader对象
				BufferedReader is=new BufferedReader(new 
						InputStreamReader(socket1.getInputStream()));
				//由Socket对象得到输出流，并构造PrintWriter对象
				PrintWriter os=new PrintWriter(socket1.getOutputStream());
				line1 = new String("你的名字：");
				line2 = new String("请输入聊天对象：");
				line3 = new String("没有此用户");
				//由系统标准输入设备构造BufferedReader对象
				BufferedReader sin=new BufferedReader(new InputStreamReader(System.in));
				//在标准输出上打印从客户端读入的字符串
				os.println(line1);
				os.flush();
				name=is.readLine();
				talk.put(name, socket1);
				while(true) {
					os.println(line2);
					os.flush();
					othername = is.readLine();
					if(talk.containsKey(othername))
					{
						new WriteThread(name,talk.get(name),othername,talk.get(othername)).start();
						new ReadThread(name,talk.get(name),othername,talk.get(othername)).start();
						clientnum++; //增加客户计数
						break;
					}
					else {
						os.println(line3);
						os.flush();
					}
				}
			}
			else
			{
				Socket socket1 = serverSocket.accept();
				String line1,line2,name;
				//由Socket对象得到输入流，并构造相应的BufferedReader对象
				BufferedReader is=new BufferedReader(new 
						InputStreamReader(socket1.getInputStream()));
				//由Socket对象得到输出流，并构造PrintWriter对象
				PrintWriter os=new PrintWriter(socket1.getOutputStream());
				line1 = new String("你的名字：");
				line2 = new String("本聊天器内只有您一个用户，无法与其他用户聊天，请等待。。。");
				//由系统标准输入设备构造BufferedReader对象
				os.println(line1);
				os.flush();
				name=is.readLine();
				talk.put(name, socket1);
				os.println(line2);
				os.flush();
				clientnum++; //增加客户计数
			}
		}
		serverSocket.close(); //关闭ServerSocket
	}
}
