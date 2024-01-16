import java.util.Date;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.ArrayList;


public class Blockchain implements Serializable{

    private CopyOnWriteArrayList<Block> blockchain;

    public Blockchain() {
        this.blockchain = new CopyOnWriteArrayList<Block>();
    }
    public synchronized CopyOnWriteArrayList<Block> getBlockchain() {
        return blockchain;
    } 

    public synchronized void updateWholeChain(Blockchain newChain) {
        this.blockchain = newChain.getBlockchain();
    }

    public synchronized Block getBlock(int index) {
        return this.blockchain.get(index);
    }

    public synchronized int getSize() {
        return this.blockchain.size();
    }

    public synchronized Block getLeastBlock() {
        return this.blockchain.get(blockchain.size() - 1);
    }

    public synchronized void generateGenesisBlock(int difficulty) {
        TransactionOutput transactionOutput = new TransactionOutput("address A" , 50);
        Transaction genesisTransaction = new Transaction(null,transactionOutput);
        ArrayList<Transaction> genesisTransactionList = new ArrayList<Transaction>();
        genesisTransactionList.add(genesisTransaction);
        Block genesisBlock  = new Block(null,0, "GenesisBlock", null, new Date().getTime(),
                genesisTransactionList, difficulty, 0);
        blockchain.add(genesisBlock);
    }

    public synchronized void generateNextBlock(Block nextBlock) {
        if(isValidNewBlock(nextBlock, getLeastBlock())){
            blockchain.add(nextBlock);
        }else{
            System.out.println("New Block is not validate.");
        }
    }

    public boolean isValidNewBlock(Block newBlock, Block previousBlock) {

        for (int i = 1; i < blockchain.size(); i++) {

            if (!newBlock.getHash().equals(newBlock.calculateBlockHash())) {
                System.out.println("block hash is not correct");
                System.out.println("actual hash: " + newBlock.calculateBlockHash());
                System.out.println("hash: " + newBlock.getHash());
                return false;
            } else if (previousBlock.getIndex() + 1 != newBlock.getIndex()) {
                System.out.println("index is not correct");
                return false;
            } else if (!previousBlock.getHash().equals(newBlock.getpreviousHash())) {
                System.out.println("previous hash is not correct");
                return false;
            }
        }
        return true;
    }

}
