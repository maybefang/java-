package chatRoom4_0;

import java.io.*;  
import java.net.*;  
import java.util.*;  

public class Server {
	
	private boolean isStart = false;  
    // 服务端socket  
    private ServerSocket ss = null;  
    // 客户端socket  
    private Socket socket = null;  
    // 保存客户端集合  
    Hashtable <String,ClientThread> chat = new Hashtable<String,ClientThread>();  
    
    
    public static void main(String[] args) {  
        new Server().start();  
    }  
  
    public void start() {  
        try {  
            // 启动服务器  
            ss = new ServerSocket(4008);  
        } catch (BindException e) {  
            System.out.println("端口已在使用中");  
            // 关闭程序  
            System.exit(0);  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
  
        try {  
            isStart = true;  
            while (isStart) {  
                // 启动监听  
                socket = ss.accept();  
                System.out.println("one client connect");  
                // 启动客户端线程  
                ClientThread client = new ClientThread(socket);
                new Thread(client).start();  
                 
            }  
        } catch (Exception e) {  
            e.printStackTrace();  
        } finally {  
            // 关闭服务  
            try {  
                ss.close();  
                isStart = false;
            } catch (IOException e) {  
                e.printStackTrace();  
            }  
        }  
  
    }  
  
  
 
    private class ClientThread extends Thread {  
        // 客户端socket  
        private Socket socket = null;  
        // 客户端输入流  
        private BufferedReader dataInputStream = null;  
        // 客户端输出流  
        private PrintWriter dataOutputStream = null;  
        private boolean isConnect = false;  
        private String myname;
  
        public ClientThread(Socket socket) {  
            this.socket = socket;  
            try {  
                isConnect = true;  
                // 获取客户端输入流  
                dataInputStream = new BufferedReader(new InputStreamReader(socket.getInputStream()));  
                // 获取客户端输出流  
                dataOutputStream = new PrintWriter(socket.getOutputStream());  
            } catch (IOException e) {  
                e.printStackTrace();  
            }  
        }  
  
        // 向客户端群发（转发）数据 
         
        public void sendMessageToClients(String message) {  
            dataOutputStream.println(message);
			dataOutputStream.flush();  
        }  
        
        
        public void setname()  {
        	String line = new String("请输入你的名字：");
        	
        	sendMessageToClients(line);
        	
        	try {
				myname = dataInputStream.readLine();
				System.out.println(myname);
			} catch (IOException e) {
				e.printStackTrace();
			}
        }
        
        public String getname() {
        	return myname;
        }
  
          
        public void run() {  
            isConnect = true;  
            ClientThread c = null;
            String othername = null;
            setname();
            String name = getname();
            chat.put(name,this); 
            if(isConnect) {
            	while(true) {
            	    String line = new String("请输入你想聊天的对象：");
            	    sendMessageToClients(line);
            	    try {
            	    	othername =  dataInputStream.readLine();
            	    	if(chat.containsKey(othername)) {
            	    		String l = "已确定对象！";
            	    		sendMessageToClients(l);
            	    		break;
            	    		}
            	    	else {
            	    		String l = "输入用户不存在，重新输入：";
            	    		sendMessageToClients(l);
            	    	}
            	    } catch (IOException e) {
            	    	e.printStackTrace();
            	    }
            	}
            }
            try {  
            	c = chat.get(othername);  
                while (isConnect) {  
                    // 读取客户端传递的数据  
                    String message = dataInputStream.readLine();  
                    c.sendMessageToClients(message); 
                }  
            } catch (EOFException e) {  
                System.out.println("client closed!"); 
                sendMessageToClients("client closed!");
            } catch (SocketException e) {  
                if (c != null) {  
                    chat.remove(c);  
                }  
                System.out.println("Client is Closed!!!!");  
                sendMessageToClients("client closed!");
            } catch (Exception e) {  
                e.printStackTrace();  
            } finally {  
                // 关闭相关资源  
                try {  
                    if (dataInputStream != null) {  
                        dataInputStream.close();  
                    }  
                    if (socket != null) {  
                        socket.close();  
                        socket = null;  
                    }  
                } catch (IOException e) {  
                    e.printStackTrace();  
                }  
            }  
        }  
    }  
 
}
