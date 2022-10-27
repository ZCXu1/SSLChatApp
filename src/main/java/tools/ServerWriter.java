package tools;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * @Author: ZCXu1
 * @Date: 2022/10/27 0:48
 * @Version: 1.0.0
 * @Description: 写服务器聊天记录
 */
public class ServerWriter {

    private static FileWriter serverWriter;

    private ServerWriter() {
    }

    public static synchronized FileWriter getWriter() throws IOException {
        if (serverWriter == null) {
            File file = new File("src/main/resources/server/record.txt");
            if (!file.exists()) {
                boolean b = file.createNewFile();
                serverWriter = new FileWriter(file);
            } else {
                // 以追加方式写入
                serverWriter = new FileWriter(file, true);
            }

        }
        return serverWriter;
    }

    public static synchronized void close() throws IOException {
        if (serverWriter != null) {
            serverWriter.close();
        }
    }


}
