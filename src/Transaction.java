// import java.security.Signature;
import java.util.Base64;
import java.io.Serializable;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;


public class Transaction implements Serializable{
    private String transactionId;
    private TransactionInput input;
    private TransactionOutput output;


    public Transaction(TransactionInput input, TransactionOutput output) {
        this.input = input;
        this.output = output;
        this.transactionId = calculateTransactionHash();

    }

    public String calculateTransactionHash() {

        
        String data = this.input ==null ? "" : this.input.toString() + this.output.toString();
        String result = crypt.sha256(data);
        return result;
    }

    public String getTransactionId() {
        return this.transactionId;
    }

    public TransactionInput getInput() {
        return this.input;
    }

    public TransactionOutput getOutput() {
        return this.output;
    }
}


class TransactionInput implements Serializable {
    private String previousTransactionId;
    private int previousIndex;
    String signature;
    KeyPair keyPair = RSASignUtils.generateKeyPair();
    private PublicKey pubKey = keyPair.getPublic();
    private PrivateKey priKey = keyPair.getPrivate();



    public TransactionInput(String previousTransactionId, int previousIndex) throws Exception {

        this.previousTransactionId = previousTransactionId;
        this.previousIndex = previousIndex;
        this.signature = getSignature(previousTransactionId, priKey, pubKey);
    }

    public String getPreviousTransactionId() {
        return this.previousTransactionId;
    }

    public int getIndex() {
        return this.previousIndex;
    }

    public String toString() {
        return previousTransactionId + previousIndex;
    }

    public String getSignature(String data, PrivateKey priKey, PublicKey pubKey) throws Exception {

        byte[] signInfo = RSASignUtils.sign(data.getBytes(), priKey);
        // System.out.println("--Data Signature: " + Base64.getEncoder().encodeToString(signInfo));
        signature = Base64.getEncoder().encodeToString(signInfo);

        // use public key to verigy whether the data's signature come from the key's corresponding
        // private key
        boolean verify = RSASignUtils.verify(data.getBytes(), signInfo, pubKey);
        // System.out.println("--Verify Data Signature: " + verify);

        if (verify == true) {
            return signature;
        }
        return "Signature is not verified"; // error message

    }
}


class TransactionOutput  implements Serializable{
    private String address;
    private double amount;

    public TransactionOutput(String address, double amount) {
        this.address = address;
        this.amount = amount;
    }

    public String getAddress() {
        return this.address;
    }

    public double getAmount() {
        return this.amount;
    }

    public String toString() {
        return address + amount;
    }
}

