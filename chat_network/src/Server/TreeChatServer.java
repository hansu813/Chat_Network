package Server;

import java.io.IOException;

public class TreeChatServer {
	public static void main(String[] args) {
		try {
			ChatService chatService = new ChatService();
			System.out.println("==== ChatServer ( " + ChatService.PORT + " ) Start ====");
			chatService.connectListening();
		} catch (IOException e) {
			System.err.println("PORT Error : ( " + ChatService.PORT + " ) Crash");
		}
	}
}
