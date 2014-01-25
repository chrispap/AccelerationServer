import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class NetworkBuffer extends Thread {
	private int PORT;
	private float[] entryBuffer = new float[3];

	public NetworkBuffer(int PORT) {
		super("Network-Thread");
		this.PORT = PORT;
	}

	public void start() {
		super.start();
		this.setPriority(Thread.MIN_PRIORITY);
	}

	/** @return Reference to the entryBuffer */
	public synchronized float[] getEntryBuffer() {
		return entryBuffer;
	}

	/**
	 * Writes a new data in the Buffer<br>
	 * If the previous data has not been read yes, it is overwritten
	 */
	private synchronized void putData(String data) {
		try {
			String G[] = data.split(":", 3);
			entryBuffer[0] = Float.valueOf(G[0]);
			entryBuffer[1] = Float.valueOf(G[1]);
			entryBuffer[2] = Float.valueOf(G[2]);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Reads the UDP Packets from the socket <br>
	 * and stores them to the buffer
	 */
	public void run() {
		DatagramSocket dsocket = null;

		try {
			dsocket = new DatagramSocket(PORT);
			byte[] buffer = new byte[2048];
			DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

			while (true) {
				dsocket.receive(packet);
				String receivedData = new String(buffer, 0, packet.getLength());
				System.out.println(receivedData);
				putData(receivedData);
				packet.setLength(buffer.length);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		if (dsocket != null)
			dsocket.close();
	}

}
