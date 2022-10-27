package tools;

import java.io.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


/**
 * @Author: ZCXu1
 * @Date: 2022/10/26 19:56
 * @Version: 1.0.0
 * @Description: 从本地标准IO接收数据并通过socket发送到对方的线程
 */

public class LocalIOThread implements Runnable{
    private final PrintWriter out;

    public LocalIOThread(PrintWriter out) {
        this.out = out;


    }

    @Override
    public void run(){
        LocalDateTime localDateTime;
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        try (BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in))) {
            while (true) {
                String inputMsg = stdIn.readLine();
                if (inputMsg != null) {
                    localDateTime = LocalDateTime.now();
                    System.out.println("\033[32m" +"You "+format.format(localDateTime));
                    System.out.println("\033[33m" +inputMsg);
                    String role = Thread.currentThread().getName().substring(0,6);
                    FileWriter fw;
                    if (role.equals("server")){
                        fw = ServerWriter.getWriter();
                    }else{
                        fw = ClientWriter.getWriter();
                    }
                    // 本地聊天记录加密存储
                    fw.write(AESTools.AESEncode("You "+format.format(localDateTime))+"\n");
                    fw.write(AESTools.AESEncode(inputMsg)+"\n");
                    fw.flush();
                    out.println(inputMsg);
                    // 两个线程都判断是否有exit
                    // 客户端和服务器不管是收到exit还是发出exit都结束进程
                    if ("exit".equals(inputMsg)) {
                        System.exit(0);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
