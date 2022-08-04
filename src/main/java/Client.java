import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) throws IOException {
        String host = "127.0.0.1";
        int port = 23444;

        InetSocketAddress socketAddress = new InetSocketAddress(host, port);
        final SocketChannel socketChannel = SocketChannel.open(); // определяем сокет сервера
        socketChannel.connect(socketAddress); // подключаемся к серверу

        try (Scanner scanner = new Scanner(System.in)) {
            final ByteBuffer inputBuffer = ByteBuffer.allocate(2 << 10); // Определяем буфер для получения данных
            String msg;

            while (true) {
                System.out.println("Введите предложение");
                msg = scanner.nextLine();
                if ("end".equals(msg) || " ".equals(msg)) break;

                socketChannel.write(ByteBuffer.wrap(msg.getBytes(StandardCharsets.UTF_8))); // отправляем сообщение
                Thread.sleep(2000);

                int bytesCount = socketChannel.read(inputBuffer);     // читаем данные из канала в буфер

                System.out.println(new String(inputBuffer.array(), 0, bytesCount, StandardCharsets.UTF_8).trim()); // получаем переданную от клиента строку в нужной кодировке и очищаем буфер
                inputBuffer.clear();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            socketChannel.close();
        }
    }
}
