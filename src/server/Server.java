package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDate;
import java.time.LocalTime;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Server {

	// ----------- Variablen -----------

	private static Logger log = LogManager.getLogger();

	// ----------- Konstruktor -----------

	public Server() {
		// TODO Server / Client Funktionalität implementieren

		try (ServerSocket serverSocket = new ServerSocket(1111)) {
			// Server starten Port wird gebunden an Programm
			log.info("Server wartet");
			while (true) {
				Socket cSocket = serverSocket.accept();
				log.debug("vom Client: " + cSocket.getInetAddress());
				returnMessage(cSocket);
			}

		} catch (IOException | ClassNotFoundException e) {
			log.error(e);
		}
	}

	// ----------- Methoden -----------

	private void returnMessage(Socket cSocket) throws IOException, ClassNotFoundException {

		new Thread(() -> {
			try {
				/*
				 * ACHTUNG!: - Objekte müssen Serializable implementieren - Reihenfolge der
				 * Stream erzeugung muss beim Client umgekehrt sein
				 */
				log.info("Server antwor an Client");

				ObjectInputStream in = new ObjectInputStream(cSocket.getInputStream());
				ObjectOutputStream out = new ObjectOutputStream(cSocket.getOutputStream());

				String a = String.valueOf(in.readObject());
				a += LocalDate.now() + " " + LocalTime.now();
				out.writeObject(a.toUpperCase());

			} catch (Exception e) {
				log.error(e);
			}
		}).start();

	}

}
