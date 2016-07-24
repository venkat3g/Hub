package Test;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public class TestServer {
	public static void main(String... arg) {
		new Thread(() -> {
			try {
				ServerSocket server = new ServerSocket(1111);
				int i = 0;
				while (i < 40000000){
					System.out.println(server.accept());
					i++;
				}
					
				

			} catch (Exception e) {
				e.printStackTrace();
			}

		}).start();

		try {
			Socket client = new Socket("localhost", 1111);
			Socket client1 = new Socket("localhost", 1111);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
