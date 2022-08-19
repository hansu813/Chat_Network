package Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ChatThread extends Thread {

	private Socket socket;
	private BufferedReader in;
	private PrintWriter out;
	private ChatService chatService;

	public ChatThread(Socket socket, ChatService chatService) throws IOException {
		this.socket = socket;
		this.chatService = chatService;
		in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		out = new PrintWriter(socket.getOutputStream());
	}

	public void messageProcess() throws IOException {
		String message = "";
		while ((message = in.readLine()) != null) {
			// 디버깅용
			System.out.println("Client Send Message : " + message);
			String[] tokens = message.split("!#!");
			String messageType = tokens[0];
			String senderNickName = tokens[1];
			switch (messageType) {
			case "CONNECT": // 최초 입장
				chatService.addClient(senderNickName, this);
				chatService.sendAllMessage(message);
				// 현재 참여한 모든 클라이언트 리스트 전송
				chatService.sendAllMessage("USERLIST!#!" + senderNickName + "!#!" + chatService.getNickNameList());
				break;
			case "CHAT": // 채팅 메세지
				chatService.sendAllMessage(message);
				break;
			case "DISCONNECT": // 퇴장
				chatService.removeClient(senderNickName);
				chatService.sendAllMessage(message);
				break;
			case "DM" :
				chatService.sendAllMessage(message);
			}
		}
	}

	public void sendMessage(String message) {
		out.println(message);
		out.flush();
	}

	@Override
	public void run() {
		try {
			messageProcess();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (socket != null)
				try {
					socket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
	}
}
