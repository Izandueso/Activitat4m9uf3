import java.net.*;
import java.io.*;

public class ClientUDP2 {
	
	public static void main (String[] args) throws Exception {
		
		//FLUX PER A ENTRADA ESTÀNDARD
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		//Socket client
		DatagramSocket clientSocket = new DatagramSocket();
		byte[] enviats = new byte[1024];
		byte[] rebuts = new byte[1024];
		DatagramSocket serverSocket = new DatagramSocket(9800);
		
		//DADES DEL SERVIDOR al qual s'envia el missatge
		InetAddress IPServidor = InetAddress.getLocalHost();
		int port = 9800;
		
		//INTRODUIR DADES PEL TECLAT
		System.out.print("Introdueix missatge: ");
		String cadena = in.readLine();
		enviats = cadena.getBytes();
		
		//ENVIANT DATAGRAMA AL SERVIDOR
		System.out.println("Enviant "+enviats.length+"bytes al servidor.");
		DatagramPacket enviament = new DatagramPacket(enviats, enviats.length, IPServidor, port);
		clientSocket.send(enviament);
		
		while (true) {
			System.out.println("Esperant datagrama... ");
			//REBUT DATAGRAMA
			rebuts = new byte[1024];
			DatagramPacket paqRebuts = new DatagramPacket(rebuts, rebuts.length);
			serverSocket.receive(paqRebuts);
			serverSocket.setSoTimeout(5000);
			
			cadena = new String(paqRebuts.getData());
			
			//ADREÇA ORIGEN
			InetAddress IPOrigen = paqRebuts.getAddress();
			port = paqRebuts.getPort();
			System.out.println("\tOrigen: "+IPOrigen+":"+port);
			System.out.println("\tMissatge rebut: "+cadena.trim());
			
			//CONVERTIR CADENA A MAJÚSCULA
			String majuscula = cadena.trim().toUpperCase();
			enviats = majuscula.getBytes();
			
			//ENVIAMENT DATAGRAMA AL CLIENT
			DatagramPacket paqEnviat = new DatagramPacket(enviats, enviats.length, IPOrigen, port);
			serverSocket.send(paqEnviat);
			
			//Per acabar
			if (cadena.trim().equals("*")) break;
			
		}
		//Tanca el socket
		clientSocket.close();
		
	}

}