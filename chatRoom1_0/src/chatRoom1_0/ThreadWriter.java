package chatRoom1_0;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Scanner;

/**
 * ���������߳�
 * @author Administrator
 *
 */
public class ThreadWriter implements Runnable{

    
    private OutputStream os;
    public ThreadWriter(OutputStream os) {
        this.os = os;
    }

    @Override
    public void run() {

        try {
            Scanner sc = new Scanner(System.in);
            while(true){
            System.out.println(Thread.currentThread().getName()+"˵��");
            String message = sc.next();
            os.write(message.getBytes());
            os.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }

}