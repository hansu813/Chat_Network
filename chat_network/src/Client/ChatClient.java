package Client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class ChatClient {
	
	public static final String SERVER_IP = "localhost";
	public static final int SERVER_PORT = 2022;
	private Socket socket;
	private PrintWriter out;
	private BufferedReader in;
	
	private ChatPanel chatPanel;
	
	public ChatClient(ChatPanel chatPanel) throws UnknownHostException, IOException {
		this.chatPanel = chatPanel;
		socket = new Socket(SERVER_IP, SERVER_PORT);
		out = new PrintWriter(socket.getOutputStream());
		in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	}
	
//	채팅 메시지 서버에 전송
	public void sendMessage(String message) {
		out.println(message);
		out.flush();
	}
	
//	채팅 메시지 서버에서 수신
	public void receiveMessage() {
		new Thread() {
			@Override
			public void run() {
				String message = "";
				try {
					while((message = in.readLine()) != null) {
						System.out.println("Server Message : " + message);
						String[] tokens = message.split("!#!");
						String messageType = tokens[0];
						String senderNickName = tokens[1];
						
						switch (messageType) {
						case "CONNECT" : // 최초 입장
							chatPanel.appendMessage("\"[" + senderNickName + "] 님이 입장하였습니다.\"");
							break;
						case "USERLIST" : // 접속자 리스트
							String userList = tokens[2];
							chatPanel.appendUserItem(userList);
							break;
						case "CHAT" :
							String chatMessage = tokens[2];
							chatPanel.appendMessage("[" + senderNickName + "] : " + chatMessage);
							break;
						case "DISCONNECT" : // 퇴장
							chatPanel.appendMessage("\"[" + senderNickName + "] 님이 퇴장했습니다.\"");
							break;
						}
					}
				} catch (IOException e) {}
			}
		}.start();
	}
	
	public void close() {
		if(socket != null) 
			try {
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
	}
}
