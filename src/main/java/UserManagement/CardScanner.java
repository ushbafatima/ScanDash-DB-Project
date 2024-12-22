package UserManagement;

import com.fazecast.jSerialComm.SerialPort;

public class CardScanner {
    // Declare the SerialPort variable as static for easier access
    private static SerialPort comPort;

    public static String getCardUID() throws Exception {
        // Check if the port is initialized
        if (comPort == null) {
            // Open the port if it's not already opened
            comPort = SerialPort.getCommPort("COM6"); // Specify the correct COM port
            if (!comPort.openPort()) {
                throw new Exception("Failed to open the port.");
            }
            comPort.setBaudRate(9600); // Match Arduino baud rate
            comPort.setNumDataBits(8);
            comPort.setNumStopBits(SerialPort.ONE_STOP_BIT);
            comPort.setParity(SerialPort.NO_PARITY);
        }

        StringBuilder messageBuffer = new StringBuilder();
        String cardUID = null;

        try {
            // Loop to read the data until a valid UID is received
            while (true) {
                if (comPort.bytesAvailable() > 0) {
                    byte[] readBuffer = new byte[comPort.bytesAvailable()];
                    comPort.readBytes(readBuffer, readBuffer.length);
                    messageBuffer.append(new String(readBuffer, "UTF-8"));

                    // Check if the message ends with '\n' indicating the end of the UID
                    String data = messageBuffer.toString();
                    if (data.contains("\n")) {
                        cardUID = data.trim(); // Get the UID and clean it up
                        break;
                    }
                }
                Thread.sleep(10); // Prevent high CPU usage
            }
        } catch (Exception e) {
            System.err.println("Error reading from the port: " + e.getMessage());
            throw new Exception("Error fetching card UID.", e);
        }

        return cardUID;
    }
}
