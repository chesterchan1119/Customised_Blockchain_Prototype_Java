//Store the transaction
//Push the CAList to get the transaction and put it the the block after mined

import java.util.concurrent.CopyOnWriteArrayList; 
import org.json.*; 
import org.json.JSONTokener; 
import org.json.JSONObject; 
import java.io.*; 

public class TransactionPool {
    private String path; 
    //private CopyOnWriteArrayList<String> transactionsPool;  
    public TransactionPool(String path){
        this.path = path;
    }
    
    public void readTransaction() throws IOException{
        FileReader reader = new FileReader(path); 
        JSONArray transactionsPool = new JSONArray(new JSONTokener(reader)); 
        

        for (int i = 0 ; i< transactionsPool.length(); i++){
            JSONObject transaction = transactionsPool.getJSONObject(i);
            String txid = transaction.getString("txid");
            System.out.println( i+ ": " +  txid);
       }
    }
    
    public Transaction getTransaction() throws Exception{
        FileReader reader = new FileReader(path); 
        JSONArray transactionsPool = new JSONArray(new JSONTokener(reader)); 
        JSONObject firstTransaction = transactionsPool.getJSONObject(0);
        String previousTransactionId = firstTransaction.getJSONObject("transactionInput").getString("preTxid");
        int previousTransactionIndex = firstTransaction.getJSONObject("transactionInput").getInt("preIndex");
    
        int amount = firstTransaction.getJSONObject("transactionOutput").getInt("amount");
        String address = firstTransaction.getJSONObject("transactionOutput").getString("address");

        TransactionInput input = new TransactionInput(previousTransactionId, previousTransactionIndex);
        TransactionOutput output = new TransactionOutput(address, amount);
        Transaction requiredTransaction = new Transaction(input, output);

        return requiredTransaction;
    }
    

    public void addTransaction(Transaction newTx) throws IOException {
        FileReader reader = new FileReader(path); 
        JSONArray transactionsPool = new JSONArray(new JSONTokener(reader)); 

        JSONObject newTxJson = new JSONObject();
        JSONObject newInputTxJson = new JSONObject();
        JSONObject newOutputTxJson = new JSONObject();

       newInputTxJson.put("preIndex", newTx.getInput().getIndex() );
       newInputTxJson.put("signature",  newTx.getInput().signature);
       newInputTxJson.put("preTxid", newTx.getInput().getPreviousTransactionId() );
        
   

       newOutputTxJson.put("amount", newTx.getOutput().getAmount() );
       newOutputTxJson.put("address",  newTx.getOutput().getAddress());

        newTxJson.put("transactionInput", newInputTxJson);
        newTxJson.put("transactionOutput", newOutputTxJson);
        newTxJson.put("txid", newTx.getTransactionId());

       newTxJson.toString();


     transactionsPool.put(newTxJson);

     FileWriter writer = new FileWriter(path);
     writer.write(transactionsPool.toString());
     writer.close();

    }
    

    public static void main(String[] args) throws Exception {
        TransactionPool pool = new TransactionPool("pool.json");
        pool.readTransaction();
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
       
        pool.getTransaction();
        System.out.println(pool.getTransaction().getOutput().getAmount());
     
        System.out.println("#####################################################");
        TransactionInput in = new TransactionInput("abc0b88a1e2d1b8a00a697a32e5f7d9e3b4cf4c2a1f4c4d4a4f8d1b4c4e4a4",0);
        TransactionOutput out = new TransactionOutput("1A1zP1eP5QGefi2DMPTfTL5SLmv7DivfNa", 10.00);
        Transaction newTransaction = new Transaction(in, out);

         pool.addTransaction(newTransaction);
      
    }
    
}
