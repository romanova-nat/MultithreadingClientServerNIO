import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

public class Main {
    public static void main(String[] args) throws IOException {
        String host = "127.0.0.1";
        int port = 23444;

        final ServerSocketChannel serverChannel = ServerSocketChannel.open();
        serverChannel.bind(new InetSocketAddress(host, port));

        while (true) {
            try (SocketChannel socketChannel = serverChannel.accept()) { // Ждем подключения клиента
                final ByteBuffer inputBuffer = ByteBuffer.allocate(2 << 10);  // Создаем буфер

                while (socketChannel.isConnected()) {
                    int bytesCount = socketChannel.read(inputBuffer); // читаем данные из канала (от клиента) в буфер
                    if (bytesCount == -1) break; // если из потока читать нельзя, перестаем работать с этим клиентом

                    final String msg = new String(inputBuffer.array(), 0, bytesCount, StandardCharsets.UTF_8); // получаем переданную от клиента строку в нужной кодировке и очищаем буфер
                    inputBuffer.clear();

                    String msg2 = textMaker(msg);
                    socketChannel.write(ByteBuffer.wrap((" ->->-> " + msg2).getBytes(StandardCharsets.UTF_8))); // отправляем ОБРАБОТАННОЕ сообщение клиента назад
                }
            } catch (IOException err) {
                System.out.println(err.getMessage());
            }
        }
    }

    public static String textMaker(String text) {
        String withoutSpaces = text.replaceAll("\\s+", "");
        return withoutSpaces;
    }
}
