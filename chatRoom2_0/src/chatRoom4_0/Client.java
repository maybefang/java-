package chatRoom4_0;

import java.io.*;
import java.net.*;

public class Client {
	
	
	private Socket socket = null;  
    // ���������  
    private PrintWriter dataOutputStream = null;  
    // ����������  
    private BufferedReader dataInputStream = null; 
    
    private FileReader fis = null;//�ļ�������
    
    private BufferedReader dis = null;  
    
    private FileWriter fos = null;  
    
    private String textfp;//�ļ�·��
    
    private String othername;
    
    private boolean isConnect = false;  
    Thread tReceive = new Thread(new ReceiveThread());  
    Thread tSend = new Thread(new sendThread());
    String name;
    int flag = 0;
    
    public static void main(String[] args) throws IOException {  
        Client chatClient = new Client();  
        chatClient.connect();
        while(!chatClient.GetIsConnect()) {
        	if(chatClient.getf() == 2) {
        		chatClient.connect();
        		chatClient.CloseSocket();
        		chatClient.BreakConnect();
            }	
        }
        
	}
    
    //���ӷ�����
    public void connect() {  
        try {  
            // �½����������  
            socket = new Socket("192.168.1.102", 4008);  
            // ��ȡ�ͻ��������  
            dataOutputStream = new PrintWriter(socket.getOutputStream()); 
            dataInputStream = new BufferedReader(new InputStreamReader(socket.getInputStream()));  
            System.out.println("���Ϸ����");  
            isConnect = true;  
            tReceive.start();
            tSend.start();
        } catch (UnknownHostException e) {  
            e.printStackTrace();  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
    }  
    
    public int getf() {
    	return flag;
    }
    
    public void BreakConnect() {
    	isConnect = false;
    }
    
    public void CloseSocket() throws IOException {
    	socket.close();
    }

    
    public boolean GetIsConnect() {
    	return isConnect;
    }
    
    
    //����Ϣ�߳�
    private class sendThread extends Thread {
    	public void run() {
    		try {  
    			
    			if(isConnect) {
    				String myname = new BufferedReader(new InputStreamReader(System.in)).readLine();
    				dataOutputStream.println(myname);
    				dataOutputStream.flush();
    			}
    			
    			if(isConnect) {
    				othername = new BufferedReader(new InputStreamReader(System.in)).readLine();
    				dataOutputStream.println(othername);
    				dataOutputStream.flush();
    			}
    			while (isConnect) {
    				String text = new BufferedReader(new InputStreamReader(System.in)).readLine();
    				
    				dataOutputStream.println(text);  
    				dataOutputStream.flush(); 
                    if (text.equals("bye")) {  
                    	//dataOutputStream.close();
                    	socket.shutdownOutput();
                    	flag++;
                    	break;
                    }
                    if(text.equals("!@#")) {
                    	System.out.println("�������ļ�·����");
                        textfp = new BufferedReader(new InputStreamReader(System.in)).readLine();
                    	try {
							sendFile sf = new sendFile();
							sf.start();
                    		//sendFile();
						} catch (Exception e) {
							e.printStackTrace();
						}
                    }
    			}
            } catch (IOException e1) {  
                e1.printStackTrace(); 
            }  
    	}
    }
    
  //����Ϣ�߳�
    private class ReceiveThread extends Thread {  
    	
        public void run() {  
            try {  
                while (isConnect) {  
                    String message = dataInputStream.readLine();  
                    if (message.equals("bye")) {  
                        System.out.println(othername + ":" + message);
                        System.out.println("other person says bye");
                        //dataInputStream.close();
                        //socket.shutdownInput();
                   	    //flag++;
                   	    //break;
                      }
                    else if (message.equals("!@#")) {
                    	System.out.println("�����ļ�");
                    	//receiveFile();
                    	receiveFile rf = new receiveFile();
                    	rf.start();
                    	sendFile sf = new sendFile();
						sf.start();
                    	System.out.println("reveive is finish");
                    }
                    else {
                    	System.out.println(message);
                    }
                }  
            } catch (IOException e) {  
                //System.out.println("You close the client!");
            	e.printStackTrace(); 
            }  
        }  
  
    }  
    
    
    /** 
     * �����˴����ļ� 
     * @throws Exception 
     */  
    public class sendFile extends Thread {  
    	public void run() {
        
    //public void sendFile() {
    	
         try {  
            File file = new File(textfp);
            System.out.println(file.getAbsolutePath());
            if(file.exists()) {  
                fis = new FileReader(file);   
  
                // �ļ���
                dataOutputStream.println(file.getName());  
                dataOutputStream.flush();  
                // ��ʼ�����ļ�  
                System.out.println("======== ��ʼ�����ļ� ========");  
                BufferedReader rf = new BufferedReader(fis); 
                String readfile ;
                while( (readfile = rf.readLine()) != null) {
                	System.out.println("���δ�������Ϊ��"+ readfile);
                	dataOutputStream.println(readfile);
                	dataOutputStream.flush();
                }
                dataOutputStream.println("!@#$");
                dataOutputStream.flush();
                System.out.println();  
                System.out.println("======== �ļ�����ɹ� ========");   
            }
        } catch (Exception e) {  
            e.printStackTrace();  
            } finally {
            	try {  
                    if(fis != null)  
                        fis.close(); 
                } catch (Exception e) {}  
            }
    	}
    }
    	
  /**
   * �����ļ�
   */
    
    public class receiveFile extends Thread {
    //public void receiveFile () {
        public void run() { 
             try {
                dis = new BufferedReader(new InputStreamReader(socket.getInputStream()));  
                   // �ļ����ͳ���  
                String fileName = dis.readLine(); 
                System.out.println(fileName);
                String path = "D:\\FT";        
                File directory = new File(path);  
                if(!directory.exists()) {  
                    directory.mkdir();  
                    }   
                File file = new File(directory , fileName); System.out.println("��ִ�У�File file = new File(directory , fileName);");  
                fos = new FileWriter(file,true);  System.out.println("��ִ�У�fos = new FileWriter(file,true); ");
      
                // ��ʼ�����ļ�  
                String writefile = dis.readLine();
                while(!writefile.equals("!@#$")) {System.out.println("��ִ�У�while((writefile = dis.readLine()) != !@#$) {    "+writefile);
                	fos.write(writefile);System.out.println("��ִ�У�fos.write(writefile);");
                	fos.flush();System.out.println("��ִ�У�fos.flush();");
                	writefile = dis.readLine();
                }
                System.out.println("======== �ļ����ճɹ� [File Path��" + file.getAbsolutePath() + "] ========");
            } catch (Exception e) {  
                e.printStackTrace();  
            } finally {  
                try {  
                    if(fos != null)  
                        fos.close();  
                } catch (Exception e) {}  
            }  
        }
    }  
}
