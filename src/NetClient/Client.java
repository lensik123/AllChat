package NetClient;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

public class Client implements Runnable {
	Socket socket;
	Thread thread;
	FriendsChat friendsChat;
	Scanner in;
	PrintStream out;

	private static int clientCounter = 0; // делаем счетчик клиентов
	private int clientNum; //номер клиента который отправляет сообщения


	public Client(Socket socket, FriendsChat friendsChat) throws IOException {
		this.friendsChat = friendsChat;
		this.socket = socket;
		new Thread(this).start();
		this.clientNum  = ++clientCounter;

	}

	public void printMessageToAllChat(String message) {
		out.println(message);
	}

	public void run() {
		try {
			// получаем потоки ввода и вывода
			InputStream is = socket.getInputStream();
			OutputStream os = socket.getOutputStream();

			// создаем удобные средства ввода и вывода
			in = new Scanner(is);
			out = new PrintStream(os);

			// читаем из сети и пишем в сеть
			out.println("Welcome to FriendsChat!");
			String input = in.nextLine();
			while (!input.equals("bye")) {
				friendsChat.sendMessageForAllClients("client" + String.valueOf(clientNum) + ": " + input);// собираем сообщения клиент *номер*: + сообщение
				input = in.nextLine();
			}
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}