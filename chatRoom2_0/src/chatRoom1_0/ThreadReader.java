package chatRoom1_0;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * ��ȡ�������߳�
 * @author Administrator
 *
 */
public class ThreadReader implements Runnable{

    //private static int HEAD_SIZE=5;//��������ֽڳ���
    //private static int BUFFER_SIZE=10;//ÿ�ζ�ȡ10���ֽ�
    private InputStream is;
    public ThreadReader(InputStream is) {
        this.is = is;
    }

    @Override
    public void run() {

        try {
            while(true){
                byte[] b = new byte[1024];
                int length = is.read(b);
                String message = new String(b,0,length);
                System.out.println(Thread.currentThread().getName()+":"+message);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }

}