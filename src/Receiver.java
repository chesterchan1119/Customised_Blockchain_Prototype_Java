import java.io.*;
import java.net.*;
import java.util.List;
import java.util.Arrays;
import java.util.concurrent.CopyOnWriteArrayList;

public class Receiver extends Thread {
    private Blockchain blockchain; 
    private int currPort;
    public CopyOnWriteArrayList<String> activePorts;

    public Receiver(Blockchain blockchain, int currPort, CopyOnWriteArrayList<String> activePorts) {
        this.blockchain = blockchain;
        this.currPort = currPort;
        this.activePorts = activePorts;
    }

    public int getCurrPort() {
        return this.currPort;
    }

    public Blockchain getBlockchain() {
        return this.blockchain;
    }
    

    public void addPort(int port) throws IOException {
        File file = new File("ports.txt");
        FileWriter fileWriter = new FileWriter(file, true);
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
        bufferedWriter.write(port + ", ");
        bufferedWriter.close();
        fileWriter.close();
    }

    public void readPorts() throws IOException {
        BufferedReader br = new BufferedReader(new FileReader("ports.txt"));
        this.activePorts =  new CopyOnWriteArrayList<String>(Arrays.asList(br.readLine().split(", ")));
    }

    public Blockchain requestChain() throws IOException {
        for (String targetPort: this.activePorts) {
            System.out.println("this port" + currPort);
            if(Integer.parseInt(targetPort) != currPort){
                Socket socket = new Socket("localhost", Integer.parseInt(targetPort));
                OutputStream out = socket.getOutputStream();
                ObjectOutputStream  buffOut = new ObjectOutputStream(out); 
                buffOut.writeObject(currPort);
                out.flush();
                out.close();
                socket.close();
            }
        }
        return blockchain;
    }

   

    public void run() {
        try {
            //open server socket
            ServerSocket serverSocket = new ServerSocket(currPort);
            System.out.println("Receiver is running on port " + currPort);
            System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
            System.out.println("");

            
            //Request blockchain from other nodes
            blockchain.updateWholeChain(requestChain());

            // Shutdown hook
            Runtime.getRuntime().addShutdownHook(new Thread() {
                @Override
                public void run() {
                    try {
                        BufferedReader br = new BufferedReader(new FileReader("ports.txt"));
                        List<String> ports = Arrays.asList(br.readLine().split(", "));
                        File file = new File("ports.txt");
                        FileWriter fileWriter = new FileWriter(file, false);
                        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
                        bufferedWriter.write(" ");
                        fileWriter.close();

                        for (String port : activePorts) {
                            if (Integer.parseInt(port) != currPort) {
                                addPort(Integer.parseInt(port));
                            }
                        }
                        //for (int i=0; i < blockchain.getSize(); i++) {
                            //System.out.println(blockchain.getBlock(i).printInfo());
                        //}
                        System.out.println(blockchain.getLeastBlock().printInfo());
                        System.out.println("");
                        System.out.println("Shutting down...");
                    } catch (Exception e) {
                        System.out.println("Error: " + e.getMessage());
                    }
                }
            });

            while (true) {
                Socket socket = serverSocket.accept();
                InputStream in = socket.getInputStream();
            
                ObjectInputStream objIn = new ObjectInputStream(in);
                Object obj = objIn.readObject();                
                if (obj instanceof Block) {
                    Block block = (Block) obj;
                    System.out.println("Object received from sender: " + block.printInfo());
                    try{
                        if(blockchain.getSize() == 0){
                            requestChain();
                            blockchain.generateNextBlock(block);
                            System.out.println("add genesis block");
                        }
                        else if(block.getIndex() == blockchain.getLeastBlock().getIndex() + 1){
                            blockchain.generateNextBlock(block);
                        }
                    }catch(Exception e){
                        System.out.println("Error: " + e.getMessage());
                    }
                }else if(obj instanceof Blockchain){
                    Blockchain newChain = (Blockchain) obj;
                    if(newChain.getSize() > blockchain.getSize()){
                        blockchain.updateWholeChain(newChain);
                        System.out.println("Updated Blockchain received from sender");
                        System.out.println("Updated Blockchain size : " + blockchain.getSize());
                    }else{
                        System.out.println("Blockchain received from sender is not longer than current blockchain");
                    }
    
                }else if(obj instanceof Integer){
                    System.out.println("Message received from sender");
                    System.out.println("Message received from sender: " + obj);
                    readPorts();
                    System.out.println("obj: " + obj);
                        try{
                        System.out.println("Sending blockchain to sender");
                        System.out.println(obj);
                        Socket socketout = new Socket("localhost",(Integer)obj);
                        OutputStream out = socketout.getOutputStream();
                        ObjectOutputStream objOut = new ObjectOutputStream(out);
                        
                        objOut.writeObject(blockchain);
                        objOut.close();
                        out.close();
                        socketout.close();
                    }catch(Exception e){ 
                        e.printStackTrace();
                    }
                }
                objIn.close();
                in.close();
                socket.close();
            }
        }catch(BindException be){
            System.out.println("this port already in use");
            System.exit(0);
        } 
        catch (Exception e) {
            System.out.println("Error: "  );
            e.printStackTrace();
        }
    }

}
