import java.io.Serializable;
import java.util.ArrayList;

class Block implements Serializable {
    private int index;
    private long timeStamp;
    private String hash;
    private String previousHash;
    private String data;
    private int difficulty;
    private int nonce;
    private ArrayList<Transaction> transactions;

    public Block(String hash , int index, String data, String previousHash, long timeStamp,
            ArrayList<Transaction> transactions, int difficulty, int nonce) {
        this.index = index;
        this.hash = hash;
        this.previousHash = previousHash;
        this.timeStamp = timeStamp;
        this.data = data;
        this.transactions = transactions;
        this.difficulty = difficulty;
        this.nonce = nonce;
    }

    public String printInfo() {
        String str = "Block index: " + this.index + "\n" + "Difficulty: " + this.difficulty + "\n"
                + "Block hash: " + this.hash + "\n" + "Previous hash: " + this.previousHash + "\n"
                + "Time Stamp: " + this.timeStamp + "\n" + "Data: " + this.data + "\n"
                + "Transaction: " + this.transactions + "\n";

        return str;
    }

    public int getIndex() {
        return this.index;
    }

    public String getHash() {
        return this.hash;
    }

    public String getpreviousHash() {
        return this.previousHash;
    }

    public long getTimeStamp() {
        return this.timeStamp;
    }

    public int getDifficulty() {
        return this.difficulty;
    }

    public ArrayList<Transaction> getTransactions() {
        return this.transactions;
    }

    public String calculateBlockHash() {
        String result = crypt.sha256(this.index + this.previousHash + this.timeStamp + this.data + this.transactions + this.difficulty + this.nonce);
        return result;
    }

    public Transaction getLatestTransaction() {
        return this.transactions.get(transactions.size() - 1);

    }

    public boolean addT(Transaction T){
        if (this.transactions.size() >= 2) {
            return false;
        }
        this.transactions.add(T);
        return true;
    }

    // public boolean addTransaction(String address, Double amount) throws Exception
    // {
    // if (this.transactions.size() >= 2) {
    // return false;
    // }

    // if (this.transactions.size() == 0) {
    // this.transactions.add(new Transaction(new TransactionInput(null, 0), new
    // TransactionOutput(address, amount)));
    // return true;
    // }

    // String preTxid =
    // getLatestTransaction().getInput().getPreviousTransactionId();
    // int preIndex = getLatestTransaction().getInput().getIndex();
    // this.transactions.add(new Transaction(new TransactionInput(preTxid,
    // preIndex), new TransactionOutput(address, amount)));

    // return true;
    // }

}
