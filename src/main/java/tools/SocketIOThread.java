package tools;

import java.io.*;
import java.lang.management.ManagementFactory;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @Author: ZCXu1
 * @Date: 2022/10/26 20:02
 * @Version: 1.0.0
 * @Description: 从Socket接收对方的数据并通过本地IO回显的线程
 */
public class SocketIOThread implements Runnable {
    private final BufferedReader in;


    public SocketIOThread(BufferedReader in) {
        this.in = in;

    }

    @Override
    public void run() {
        String inputMsg;
        LocalDateTime localDateTime;
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        try {
            while ((inputMsg = in.readLine()) != null) {
                localDateTime = LocalDateTime.now();
                System.out.println("\033[32m" + "The other side " + format.format(localDateTime));
                System.out.println("\033[33m" + inputMsg);
                String role = Thread.currentThread().getName().substring(0,6);
                FileWriter fw;
                if (role.equals("server")){
                    fw = ServerWriter.getWriter();
                }else{
                    fw = ClientWriter.getWriter();
                }
                // 本地聊天记录加密存储

                fw.write(AESTools.AESEncode("The other side " + format.format(localDateTime))+"\n");
                fw.write(AESTools.AESEncode(inputMsg)+"\n");
                fw.flush();
                // 两个线程都判断是否有exit
                // 客户端和服务器不管是收到exit还是发出exit都结束进程
                if ("exit".equals(inputMsg)) {
                    System.exit(0);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
