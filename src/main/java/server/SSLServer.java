package server;


import tools.*;

import javax.net.ssl.*;
import java.io.*;
import java.security.*;


public class SSLServer {
    public static void main(String[] args) {
        try {
            Utils.showMenu("server");

            // 打开终端debug调试
            // System.setProperty("javax.net.debug", "all");
            System.out.println("Connecting to Client...");
            KeyStore keyStore = KeyStore.getInstance("PKCS12");
            final String password = "123456";
            InputStream inputStream = ClassLoader.getSystemClassLoader().getResourceAsStream("server/server-certificate.p12");
            keyStore.load(inputStream, password.toCharArray());

            KeyStore trustStore = KeyStore.getInstance("PKCS12");
            final String password2 = "123456";
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance("PKIX", "SunJSSE");
            InputStream inputStream1 = ClassLoader.getSystemClassLoader().getResourceAsStream("client/client-certificate.p12");
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

            SSLServerSocketFactory serverSocketFactory = sslContext.getServerSocketFactory();
            SSLServerSocket serverSocket = (SSLServerSocket) serverSocketFactory.createServerSocket(8333);
            serverSocket.setNeedClientAuth(true);
            serverSocket.setEnabledProtocols(new String[]{"TLSv1.2"});
            SSLSocket socket = (SSLSocket) serverSocket.accept();
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            Utils.showMsg();

            new Thread(new LocalIOThread(out), "serverLocal").start();
            new Thread(new SocketIOThread(in), "serverSocket").start();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
