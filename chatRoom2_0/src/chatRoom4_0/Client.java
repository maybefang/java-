package chatRoom4_0;

import java.io.*;
import java.net.*;

public class Client {
	
	
	private Socket socket = null;  
    // 数据输出流  
    private PrintWriter dataOutputStream = null;  
    // 数据输入流  
    private BufferedReader dataInputStream = null; 
    
    private FileReader fis = null;//文件输入流
    
    private BufferedReader dis = null;  
    
    private FileWriter fos = null;  
    
    private String textfp;//文件路径
    
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
    
    //连接服务器
    public void connect() {  
        try {  
            // 新建服务端连接  
            socket = new Socket("192.168.1.102", 4008);  
            // 获取客户端输出流  
            dataOutputStream = new PrintWriter(socket.getOutputStream()); 
            dataInputStream = new BufferedReader(new InputStreamReader(socket.getInputStream()));  
            System.out.println("连上服务端");  
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
    
    
    //发消息线程
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
                    	System.out.println("请输入文件路径：");
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
    
  //收消息线程
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
                    	System.out.println("接收文件");
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
     * 向服务端传输文件 
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
  
                // 文件名
                dataOutputStream.println(file.getName());  
                dataOutputStream.flush();  
                // 开始传输文件  
                System.out.println("======== 开始传输文件 ========");  
                BufferedReader rf = new BufferedReader(fis); 
                String readfile ;
                while( (readfile = rf.readLine()) != null) {
                	System.out.println("本次传输内容为："+ readfile);
                	dataOutputStream.println(readfile);
                	dataOutputStream.flush();
                }
                dataOutputStream.println("!@#$");
                dataOutputStream.flush();
                System.out.println();  
                System.out.println("======== 文件传输成功 ========");   
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
   * 接受文件
   */
    
    public class receiveFile extends Thread {
    //public void receiveFile () {
        public void run() { 
             try {
                dis = new BufferedReader(new InputStreamReader(socket.getInputStream()));  
                   // 文件名和长度  
                String fileName = dis.readLine(); 
                System.out.println(fileName);
                String path = "D:\\FT";        
                File directory = new File(path);  
                if(!directory.exists()) {  
                    directory.mkdir();  
                    }   
                File file = new File(directory , fileName); System.out.println("已执行：File file = new File(directory , fileName);");  
                fos = new FileWriter(file,true);  System.out.println("已执行：fos = new FileWriter(file,true); ");
      
                // 开始接收文件  
                String writefile = dis.readLine();
                while(!writefile.equals("!@#$")) {System.out.println("已执行：while((writefile = dis.readLine()) != !@#$) {    "+writefile);
                	fos.write(writefile);System.out.println("已执行：fos.write(writefile);");
                	fos.flush();System.out.println("已执行：fos.flush();");
                	writefile = dis.readLine();
                }
                System.out.println("======== 文件接收成功 [File Path：" + file.getAbsolutePath() + "] ========");
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
