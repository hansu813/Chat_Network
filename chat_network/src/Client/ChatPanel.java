package Client;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.IOException;

public class ChatPanel extends Panel {

	Panel northPanel, southPanel;
	Label nickNameL;
	TextField nickNameTF, inputTF;
	Button connectB, sendB;
	TextArea messageTA;
	List userList;
	Choice userChoice;
	
	ChatClient chatClient;
	String nickName;
	
	public ChatPanel() {
		northPanel = new Panel();
		southPanel = new Panel();
		nickNameL = new Label("NickName", Label.CENTER);
		nickNameL.setBackground(Color.white);
		nickNameTF = new TextField();
		inputTF = new TextField();
		connectB = new Button("Connect");
		connectB.setPreferredSize(new Dimension(125, 15));
		sendB = new Button("Send");
		sendB.setPreferredSize(new Dimension(125, 15));
		messageTA = new TextArea();
		userList = new List();
		userChoice = new Choice();
		userChoice.setPreferredSize(new Dimension(125, 15));
	}
	
/**
 * 	컴포넌트 배치
 */
	public void init() {
		setLayout(new BorderLayout());
		northPanel.setLayout(new BorderLayout());
		northPanel.add(nickNameL, BorderLayout.WEST);
		northPanel.add(nickNameTF, BorderLayout.CENTER);
		northPanel.add(connectB, BorderLayout.EAST);
		southPanel.setLayout(new BorderLayout());
		southPanel.add(userChoice, BorderLayout.WEST);
		southPanel.add(inputTF, BorderLayout.CENTER);
		southPanel.add(sendB, BorderLayout.EAST);
		add(northPanel, BorderLayout.NORTH);
		add(messageTA, BorderLayout.CENTER);
		add(userList, BorderLayout.EAST);
		add(southPanel, BorderLayout.SOUTH);
	}
	
/**
 * 	서버 연결	
 */
	public void connect() {
		nickName = nickNameTF.getText();
		if(isEmpty(nickName)) {
			System.out.println("Write your Nickname...");
			return;
		}
		try {
			chatClient = new ChatClient(this);
			chatClient.receiveMessage();
//			연결하자 마자 접속한 닉네임 전송
			String connectMessage = "CONNECT!#!" + nickName;
			chatClient.sendMessage(connectMessage);
		} catch(IOException e) {
			appendMessage("채팅 서버에 연결할 수 없습니다.");
		}
		nickNameTF.setEditable(false);
		nickNameTF.setEnabled(false);
		connectB.setLabel("Exit");
		inputTF.requestFocus();
	}
	
/**
 * 	서버 연결 종료	
 */
	public void disConnect() {
		String disConnectMessage = "DISCONNECT!#!" + nickName;
		chatClient.sendMessage(disConnectMessage);
		chatClient.close();
	}
	
/**
 * 	Text Field 입력 여부 검증	
 */
	private boolean isEmpty(String string) {
		return ((string == null || string.trim().length() == 0) ? true : false);
	}
	
/**
 * 	메시지 전송	
 */
	public void sendMessage() {
		String message = inputTF.getText();
		// 유효성 검증
		if(isEmpty(message)) return;
		String chatMessage = "CHAT!#!" + nickName + "!#!" + message;
		chatClient.sendMessage(chatMessage);
		inputTF.setText("");
		inputTF.requestFocus();
	}
	
/**
 * 	닉네임 선택	
 */
	public void selectUser() {
		String message = inputTF.getText();
		messageTA.append(userList.getSelectedItem() + "님을 선택하였습니다.");
		String dmMessage = "DM!#!" + nickName + "!#!" + message;
		inputTF.setText("");
		inputTF.requestFocus();
	}
	
/**
 * 	메세지 창에 메세지 추가	
 */
	public void appendMessage(String message) {
		messageTA.append(message + "\n");
	}
	
	public void appendUserItem(String csv) {
		userList.removeAll();
		userChoice.removeAll();
		userChoice.add("전체에게");
		userList.add(this.nickName);
		userChoice.add(this.nickName);
		String[] tokens = csv.split(",");
		for(String nickNameList : tokens) {
			if(nickNameList.equals(this.nickName)) {
				continue;
			}
			userList.add(nickNameList);
			userChoice.add(nickNameList);
		}
	}
	
/**
 * 	이벤트 소스에 이벤트 처리	
 */
	public void eventRegist() {
		ActionListener actionListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Object eventSource = e.getSource();
				if(eventSource == connectB || eventSource == nickNameTF) {
					if(e.getActionCommand().equalsIgnoreCase("exit")) {
						disConnect();
					} else {
						connect();
					}
				} else if(eventSource == sendB || eventSource == inputTF) {
					sendMessage();
				}
			}
		};
		connectB.addActionListener(actionListener);
		nickNameTF.addActionListener(actionListener);
		sendB.addActionListener(actionListener);
		inputTF.addActionListener(actionListener);
		userList.addItemListener(new ItemListener() {	
			@Override
			public void itemStateChanged(ItemEvent e) {
				if(e.getStateChange() == ItemEvent.SELECTED) selectUser();
			}
		});
	}
}
