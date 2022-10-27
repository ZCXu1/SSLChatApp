package client;


import tools.AESTools;
import tools.SocketIOThread;
import tools.LocalIOThread;

import javax.net.ssl.*;
import java.io.*;
import java.security.*;
import java.util.Scanner;


public class SSLClient {


    public static void main(String[] args) {
        try {
            System.out.println("\033[32m" +"Welcome to SSL chat Application!");
            System.out.println("\033[33m" +"   _____ _____  __    ______ __  __ ___   ______\n" +
                    "\033[33m" +"  / ___// ___/ / /   / ____// / / //   | /_  __/\n" +
                    "\033[33m" +"  \\__ \\ \\__ \\ / /   / /    / /_/ // /| |  / /   \n" +
                    "\033[33m" +" ___/ /___/ // /___/ /___ / __  // ___ | / /    \n" +
                    "\033[33m" +"/____//____//_____/\\____//_/ /_//_/  |_|/_/     \n");

            Scanner sc = new Scanner(System.in);
            while (true){

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
                    File file = new File("src/main/resources/client/record.txt");
                    if (file.exists()) {
                        BufferedReader br = new BufferedReader(new FileReader(file));
                        String data;
                        while ((data = br.readLine()) != null) {
                            System.out.println("\033[34m" +AESTools.AESDecode(data));

                        }
                        System.out.println("\033[32m" +"All records are loaded");
                    }
                }
                else if (n == 2) break;
                else {
                    System.out.println("\033[31m" +"Error! Please input again.");
                }
            }
            // 打开终端debug调试
            // System.setProperty("javax.net.debug", "all");
            System.out.println("Connecting to Server...");
            KeyStore keyStore = KeyStore.getInstance("PKCS12");
            final String password = "123456";
            InputStream inputStream = ClassLoader.getSystemClassLoader().getResourceAsStream("client/client-certificate.p12");
            keyStore.load(inputStream, password.toCharArray());


            KeyStore trustStore = KeyStore.getInstance("PKCS12");
            final String password2 = "123456";
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance("PKIX", "SunJSSE");
            InputStream inputStream1 = ClassLoader.getSystemClassLoader().getResourceAsStream("server/server-certificate.p12");
            trustStore.load(inputStream1, password2.toCharArray());
            trustManagerFactory.init(trustStore);
            X509TrustManager x509TrustManager = null;
            for (TrustManager trustManager : trustManagerFactory.getTrustManagers()) {
                if (trustManager instanceof X509TrustManager) {
                    x509TrustManager = (X509TrustManager) trustManager;
                    break;
                }
            }

            if (x509TrustManager == null) throw new NullPointerException();


            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("SunX509", "SunJSSE");
            keyManagerFactory.init(keyStore, password.toCharArray());
            X509KeyManager x509KeyManager = null;
            for (KeyManager keyManager : keyManagerFactory.getKeyManagers()) {
                if (keyManager instanceof X509KeyManager) {
                    x509KeyManager = (X509KeyManager) keyManager;
                    break;
                }
            }
            if (x509KeyManager == null) throw new NullPointerException();


            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(new KeyManager[]{x509KeyManager}, new TrustManager[]{x509TrustManager}, null);

            SSLSocketFactory socketFactory = sslContext.getSocketFactory();
            SSLSocket sslSocket = (SSLSocket) socketFactory.createSocket("127.0.0.1", 8333);
            sslSocket.setEnabledProtocols(new String[]{"TLSv1.2"});

            PrintWriter out = new PrintWriter(sslSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(sslSocket.getInputStream()));
            System.out.println("Connection is built. Now you can send message.");
            System.out.println("If you want to exit, please input \"exit\"");

            new Thread(new LocalIOThread(out), "clientLocal").start();
            new Thread(new SocketIOThread(in), "clientSocket").start();


        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
