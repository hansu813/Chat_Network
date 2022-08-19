package Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class ChatService {

	public static final int PORT = 2022;
	private boolean running;
	private ServerSocket serverSocket;
	
	private Map<String, ChatThread> clients;
	
	public ChatService() throws IOException {
		serverSocket = new ServerSocket(PORT);
		running = true;
		clients = new LinkedHashMap<>();
	}
	
	public void connectListening() throws IOException {
		while(running) {
			Socket socket = serverSocket.accept();
			System.out.println("ChatClient [ " + socket.getInetAddress().getHostAddress() + " ] Connected...");
			ChatThread thread = new ChatThread(socket, this);
			thread.start();
		}
	}
	
//	접속자 리스트에 접속 클라이언트 추가
	public void addClient(String nickName, ChatThread thread) {
		clients.put(nickName, thread);
	}
	
//	접속자 리스트에서 클라이언트 제거
	public void removeClient(String nickName) {
		clients.remove(nickName);
	}
	
//	모든 접속자에게 메시지를 전송
	public void sendAllMessage(String message) {
		Collection<ChatThread> list = clients.values();
		Iterator<ChatThread> iter = list.iterator();
		while(iter.hasNext()) {
			ChatThread chatThread = (ChatThread) iter.next();
			chatThread.sendMessage(message);
		}
	}
	
//	접속자 닉네임 리스트 반환
	public String getNickNameList() {
		Set<String> keys = clients.keySet();
		if(keys.isEmpty()) return null;
		StringBuilder sb = new StringBuilder();
		Iterator<String> iter = keys.iterator();
		while(iter.hasNext()) {
			String nickName = iter.next();
			sb.append(nickName).append(",");
		}
		return sb.toString();
	}
}
