package tools;

import javax.net.ssl.TrustManagerFactory;
import java.io.*;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.Scanner;

/**
 * @Author: ZCXu1
 * @Date: 2022/10/27 14:44
 * @Version: 1.0.0
 * @Description:
 */
public class Utils {
    private final static String password = "123456";
    private final static String password2 = "123456";
    public static void showMenu(String role) throws IOException {
        System.out.println("\033[32m" +"Welcome to SSL chat Application!");
        System.out.println("\033[33m" +"   _____ _____  __    ______ __  __ ___   ______\n" +
                "\033[33m" +"  / ___// ___/ / /   / ____// / / //   | /_  __/\n" +
                "\033[33m" +"  \\__ \\ \\__ \\ / /   / /    / /_/ // /| |  / /   \n" +
                "\033[33m" +" ___/ /___/ // /___/ /___ / __  // ___ | / /    \n" +
                "\033[33m" +"/____//____//_____/\\____//_/ /_//_/  |_|/_/     \n");

        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.println("\033[32m" +"Please input");
            System.out.println("\033[32m" +"0 to EXIT");
            System.out.println("\033[32m" +"1 to CHECK LOCAL CHAT RECORDS");
            System.out.println("\033[32m" +"2 to START CHATTING");
            int n = sc.nextInt();
            if (n == 0) System.exit(0);
            else if (n == 1) {
                System.out.println("\033[32m" +"Please input password");
                Scanner sc2 = new Scanner(System.in);
                String password = sc2.nextLine();
                // 输入口令不正确
                if (!AESTools.getEncodeRules().equals(password)){
                    System.out.println("\033[31m" +"Error! Please input again.");
                    continue;
                }
                // 打开加密的聊天记录
                File file = new File("src/main/resources/"+role+"/record.txt");
                if (file.exists()) {
                    BufferedReader br = new BufferedReader(new FileReader(file));
                    String data;
                    while ((data = br.readLine()) != null) {
                        System.out.println("\033[34m" + AESTools.AESDecode(data));
                    }
                    System.out.println("\033[32m" +"All records are loaded");
                }
            } else if (n == 2) break;
            else System.out.println("\033[31m" +"Error! Please input again.");

        }
    }

    public static void showMsg(){
        System.out.println("Connection is built. Now you can send message.");
        System.out.println("If you want to exit, please input \"exit\"");
    }

}
