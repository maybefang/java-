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
			//��Socket����õ�����������������Ӧ��BufferedReader����
			BufferedReader is1=new BufferedReader(new InputStreamReader(socket1.getInputStream()));
			//�ڱ�׼����ϴ�ӡ�ӿͻ��˶�����ַ���
			line = is1.readLine();
			System.out.println(othername + line);
			//�ӱ�׼�������һ�ַ���
			while(!line.equals("bye")){//������ַ���Ϊ "bye"����ֹͣѭ��
				System.out.println(othername + ":" + line);
			   line=is1.readLine();//��ϵͳ��׼�������һ�ַ���
			}//����ѭ��
			is1.close(); //�ر�Socket������		
		}catch(Exception e){
			System.out.println("Error:"+e);//������ӡ������Ϣ
		}
    }
}
