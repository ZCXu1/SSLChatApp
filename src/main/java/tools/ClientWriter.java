package tools;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * @Author: ZCXu1
 * @Date: 2022/10/27 0:52
 * @Version: 1.0.0
 * @Description: 写客户端聊天记录
 */
public class ClientWriter {

    private static FileWriter clientWriter;

    private ClientWriter() {
    }

    public static synchronized FileWriter getWriter() throws IOException {
        if (clientWriter == null) {
            File file = new File("src/main/resources/client/record.txt");
            if (!file.exists()) {
                boolean b = file.createNewFile();
                clientWriter = new FileWriter(file);
            } else {
                // 以追加方式写入
                clientWriter = new FileWriter(file, true);
            }

        }
        return clientWriter;
    }

    public static synchronized void close() throws IOException {
        if (clientWriter != null) {
            clientWriter.close();
        }
    }
}
