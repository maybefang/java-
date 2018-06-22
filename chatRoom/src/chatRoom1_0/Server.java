package chatRoom1_0;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

/**
 * ������
 * @author Administrator
 *
 */
public class Server {
	    private int duankou = 9000;//�˿ں�
	    private ServerSocket server;//����������
	    private static Socket socket;//�����ͻ���
	    private String serName;
	    public Server(){
	        try {
	            init();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }
	    
	    /*
	     * ��������������ʼ����
	     */
	    private void init() throws IOException{
	        server = new ServerSocket(duankou);
	        System.out.println("------�������ѿ���--------");
	        System.out.println("��������������֣�");
	        Scanner sc = new Scanner(System.in);
	        serName = sc.next();
	        while(true){
	            socket = server.accept();
	            hands(socket);
	        }
	    }


	    private void hands(Socket socket) {
	        String key = socket.getInetAddress().getHostAddress()+":"+socket.getPort();
	        System.out.println("�������Ŀͻ��ˣ�"+key);
	        Thread thread = new Thread(new ThreadSocket(socket));
	        thread.setName(serName);
	        thread.start();
	    }
	    
	    public static void main(String[] args) {
	        Server server = new Server();
	    }
	    
	}

