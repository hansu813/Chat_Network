package Client;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class TreeTalk {
	public static void main(String[] args) {
		Frame frame = new Frame("Tree Talk");
		ChatPanel panel = new ChatPanel();
		panel.init();
		panel.eventRegist();
		frame.add(panel, BorderLayout.CENTER);
		frame.setSize(400, 500);
		frame.setVisible(true);
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
	}
}
