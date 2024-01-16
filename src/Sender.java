import java.io.*;
import java.net.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class Sender {

    public static void sendBlock(Block newBlock ,CopyOnWriteArrayList<String> activePorts, int currPort) throws IOException{

        for (String targetPort: activePorts) {
            if(Integer.parseInt(targetPort) != currPort){
                try {
                    Socket socket = new Socket("localhost", Integer.parseInt(targetPort));
                    OutputStream out = socket.getOutputStream();
                    ObjectOutputStream objOut = new ObjectOutputStream(out);
                    
                    objOut.writeObject(newBlock);
                    objOut.close();
                    out.close();
                    socket.close();
                    System.out.println("Object sent to receiver on port " + targetPort);
                } catch (Exception e) {
                    System.out.println("Error sending object to receiver on port " +targetPort);
                }
            }
        }
    }
}
