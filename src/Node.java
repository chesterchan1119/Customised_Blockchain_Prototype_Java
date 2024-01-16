import java.util.concurrent.CopyOnWriteArrayList;
import java.util.Scanner;
import java.util.List;
import java.io.*;
import java.util.Arrays;
public class Node {
    private Blockchain blockchain;
    private CopyOnWriteArrayList<String> activePorts;
    private Receiver receiver;
    private Miner miner;
    private int currPort;
    private Scanner scanner;

    public Node() throws IOException{
        this.blockchain = new Blockchain();
        this.activePorts = new CopyOnWriteArrayList<String>();
        this.scanner = new Scanner(System.in);
        System.out.print("Enter port number: ");
        int currPort = scanner.nextInt();
        setCurrPort(currPort);
        addPort(currPort);
        readPorts();
        System.out.println("Active ports: " + activePorts);
        System.out.println("Current port: " + currPort);
        System.out.println("Blockchain size: " + blockchain.getSize());
        this.receiver = new Receiver(this.blockchain, this.currPort, this.activePorts);
        this.miner = new Miner(this.blockchain, this.activePorts, this.currPort);
    }

    public synchronized Blockchain getBlockChain(){
        return this.blockchain;
    }
    public int getCurrPort(){
        return this.currPort;
    }
    public void setCurrPort(int currPort){
        this.currPort = currPort;
    }
    public CopyOnWriteArrayList<String> getActivePorts(){
        return this.activePorts;
    } 

    public void readPorts() throws IOException {
        BufferedReader br = new BufferedReader(new FileReader("ports.txt"));
        List<String> temp = Arrays.asList(br.readLine().split(", "));
        this.activePorts =  new CopyOnWriteArrayList<String>(temp);
    }
    public void addPort(int port) throws IOException {
        File file = new File("ports.txt");
        FileWriter fileWriter = new FileWriter(file, true);
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
        bufferedWriter.write(port + ", ");
        bufferedWriter.close();
        fileWriter.close();
    }
    public static void main(String[] args) throws IOException {
        Node client = new Node();
        
        client.receiver.start();
        
        System.out.println("Start mining? (y/n): ");
        client.scanner.nextLine();
        String input = client.scanner.nextLine();
        if(input.equals("y")){
            System.out.println("Mining...");  
            client.miner.start();
        }else{
            System.out.println("Mining stopped.");
        }
        client.scanner.close();
        
    }
}
