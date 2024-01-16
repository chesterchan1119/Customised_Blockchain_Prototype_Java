import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.io.*;

public class Miner extends Thread{

  private Blockchain blockchain;
  private CopyOnWriteArrayList<String> activePorts;
  private int currPort;

  public Miner(Blockchain blockchain, CopyOnWriteArrayList<String> activePorts, int currPort) {
    this.blockchain = blockchain;
    this.activePorts = activePorts;
    this.currPort = currPort;
  }

  public void readPorts() throws IOException {
      BufferedReader br = new BufferedReader(new FileReader("ports.txt"));
      this.activePorts =  new CopyOnWriteArrayList<String>(Arrays.asList(br.readLine().split(", ")));
  }

  public void run(){
        Mine mine = new Mine(15);
        TransactionPool pool = new TransactionPool("pool.json");

        System.out.println("Miner's Blockchain : " + blockchain.getSize());
        while(true){
          System.out.println("Blockchain size: " + blockchain.getSize());
          Block newBlock;
          if(blockchain.getSize() == 0){
            System.out.println("Blockchain is empty. Mining genesis block...");
            blockchain.generateGenesisBlock(mine.getDifficulty());
            newBlock = blockchain.getLeastBlock();
            try{
              readPorts();
              System.out.println("Sending block to other Node...");
              Sender.sendBlock(newBlock,activePorts, currPort);
            }catch(Exception e){
              e.printStackTrace();
            }
          }
          else{
            try {
              boolean addSuccess = blockchain.getLeastBlock().addT(pool.getTransaction());
              if (!addSuccess) {
                newBlock = mine.mineBlock(blockchain.getLeastBlock(), "Data", blockchain.getLeastBlock().getTransactions());
                int newDifficulty = mine.getAdjustedDifficulty(newBlock, blockchain);
                blockchain.generateNextBlock(newBlock);
                blockchain.getLeastBlock().addT(pool.getTransaction());
                try{
                  readPorts();
                  System.out.println("Sending block to other Node...");
                  Sender.sendBlock(newBlock,activePorts, currPort);
                }catch(Exception e){
                  e.printStackTrace();
                }
              }
            } catch (Exception e) {
              e.printStackTrace();
            }
          }
        }
    
  }
}
