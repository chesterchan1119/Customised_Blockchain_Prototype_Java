
import java.util.Date;
import java.util.ArrayList;

public class Mine {

    private int difficulty;
    private int DIFFICULTY_ADJUSTMENT_INTERVAL = 10;
    private int BLOCK_GENERATION_INTERVAL = 15;

    public Mine(int difficulty) {
        this.difficulty = difficulty;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    public int getDifficulty() {
        return this.difficulty;
    }
    private boolean hashMatchesDifficulty(String hash, int difficulty) {
        String target = new String(new char[difficulty]).replace('\0', '0');
        String hashInBinary = crypt.hexToBinary(hash);
        return hashInBinary.substring(0, difficulty).equals(target);
    }

    public Block mineBlock(Block previousBlock, String data, ArrayList<Transaction> T) {
        TransactionOutput transactionOutput = new TransactionOutput("address A" , 50);
        Transaction genesisTransaction = new Transaction(null,transactionOutput);
        ArrayList<Transaction> genesisTransactionList = new ArrayList<Transaction>();

        int index = previousBlock.getIndex() + 1;
        String previousHash = previousBlock.getHash();
        long timestamp = new Date().getTime();
        int nonce = 0;
        String hash = "";

        while (true) {
            hash = crypt.sha256(index + previousHash + timestamp + data + T + difficulty +nonce    );
            if (hashMatchesDifficulty(hash, difficulty)) {
                break;
            }
            nonce++;
        }
        System.out.println("Block mined: " + hash);
        return new Block(hash, index, data, previousHash, timestamp, T,
                difficulty, nonce);
    }

    public int getDifficulty(Blockchain blockchain) {
        Block latestBlock = blockchain.getLeastBlock();
        if (latestBlock.getIndex() % DIFFICULTY_ADJUSTMENT_INTERVAL == 0
                && latestBlock.getIndex() != 0) {
            return getAdjustedDifficulty(latestBlock, blockchain);
        } else {
            return latestBlock.getDifficulty();
        }
    }

    public int getAdjustedDifficulty(Block latestBlock, Blockchain blockchain) {
        if (blockchain.getSize() < DIFFICULTY_ADJUSTMENT_INTERVAL) {
            return latestBlock.getDifficulty();
        }
        Block prevAdjustmentBlock =
                blockchain.getBlock(blockchain.getSize() - DIFFICULTY_ADJUSTMENT_INTERVAL);
        int timeExpected = BLOCK_GENERATION_INTERVAL * DIFFICULTY_ADJUSTMENT_INTERVAL;
        int timeTaken = (int) (latestBlock.getTimeStamp() - prevAdjustmentBlock.getTimeStamp());
        if (timeTaken < timeExpected / 2) {
            return prevAdjustmentBlock.getDifficulty() + 1;
        } else if (timeTaken > timeExpected * 2) {
            if(prevAdjustmentBlock.getDifficulty() == 0){
                return 0;
            }
            return prevAdjustmentBlock.getDifficulty() - 1;
        } else {
            return prevAdjustmentBlock.getDifficulty();
        }
    }

}
