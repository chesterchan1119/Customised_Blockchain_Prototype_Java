# COMP4137-Project

Block and Blockchain 

The Block is a fundamental component of the blockchain system that stores massage or transaction records in a decentralized and secure manner. Each Block in the blockchain is linked to its previous block to form a chain of blocks, which is a Blockchain. 

In our block class, there is a collection of variables that help to construct and manage blocks. Our block has three major features: being able to connect to the previous block, storing the transaction record (at most two in one block), and adjusting difficulty. 

Block Class: 

1. index: is an integer that specifies the block's position in the blockchain. The Genesis Block's index will be 0. 

2. timeStamp: holds the block's creation time. 

3. hash: is a unique identifier for the block that is calculated using a cryptographic hashing algorithm.  

4. previousHash: is used to link the block to its previous block in the chain. 

5. data: is used to hold any additional information associated with the block. 

6. difficulty: specifies the degree of difficulty for mining the block 

7. nonce: is a random number used in the mining process to generate a hash that satisfies the specified difficulty level. 

8. transactions: an ArrayList containing a catalog of transactions that have been validated and appended to the block, and the limit of the ArrayList size is 2. 

Here are several major methods of the Block class that are used to manage and manipulate the block and its transaction records.  

The calculateBlockHash() method is utilized to generate the block's unique hash based on the block's index, previousHash, timeStamp, and data. The addTransaction() method is used to check whether the block is full and add a new transaction record to the block's transactions list if it is not full. The getLatestTransaction() method will return the most recent transaction from the transaction records list. 

Blockchain Class: 

Our blockchain is represented by an ArrayList of Block objects, and the ArrayList would be the only attribute in the class. 

The first block in the blockchain would be created by the generateGenesisBlock() method, and the genesis block would be assigned index 0 with no previous hash and a specified degree of difficulty.  Then, the isValidNewBlock() method is used to validate the integrity of a newly inserted block by examining its hash value and ensuring that it accurately links to the preceding block in the chain. If the new block is validated, the generateNextBlock() method can be used to a new block is added to the chain by creating a new Block object and appended it to the ArrayList. The Blockchain class also includes the getLeastBlock() method returns the block inserted most recently to the chain. 

Mining and Minting 

The Mine class is also a crucial component of our blockchain system, which has two major functions: mining new blocks and modifying the blockchain's difficulty level. The Mine class contains multiple attributes and methods to support the two features. 

1. difficulty: to indicate the difficulty level 

2. DIFFICULTY_ADJUSTMENT_INTERVAL: represent the number of blocks between difficulty adjustments  

2. BLOCK_GENERATION_INTERVAL: represent anticipated duration to generate a new block 

The hashMatchesDifficulty() method accepts a hash value and a difficulty level as arguments and determines if the hash value matches the difficulty level. The hash value matches the difficulty level if the binary hash value begins with the target string. 

The mineBlock() method is used to mine a new block for the blockchain. The method accepts as parameters a preceding block and data and returns a new block. It uses the crypt. sha256() method to calculate the block's hash value and the hashMatchesDifficulty() method to determine if the hash value matches the difficulty level. 

The getDifficulty() method is used to calculate the current difficulty degree of the blockchain. It gets the latest block and then verifies that the block's index is a multiple of the DIFFICULTY ADJUSTMENT INTERVAL constant. If the index is a multiple of the constant, the getAdjustedDifficulty() method is invoked to determine the adjusted difficulty level. Otherwise, the method returns the block with the current difficulty. 

The getAdjustedDifficulty() method is used to compute the altered difficulty degree of the blockchain. If the extent of the blockchain is smaller than the DIFFICULTY ADJUSTMENT INTERVAL constant, the method returns the current difficulty level. Otherwise, it gets the latest block that has adjusted difficulty. Using the timestamps of the blocks, the method then calculates the expected time to generate DIFFICULTY ADJUSTMENT INTERVAL blocks and the actual time taken to generate the most recent block. If the actual time taken is less than half the anticipated time, the method returns the difficulty level of the previous adjustment block plus 1. If the actual time taken exceeds twice the anticipated time, the method returns the difficulty level of the previous adjustment block minus 1. Otherwise, the method returns the difficulty level of the preceding adjustment block. 

 

Minting a new coin 

Minting a new coin on a blockchain creates a new block that includes a transaction that generates new coins and adds them to the total supply. 

1. Define the coin parameters: Before minting a new coin, we  define the parameters of the coin, such as its name, symbol, total supply, and initial distribution. 

 2. Create a new transaction: To mint new coins, we create a new transaction that includes an output that generates the new coins. The amount of the output should be equal to the amount of new coins that you want to mint. 

 3. Include the transaction in a new block: Once the transaction is created,  we include it in a new block.  The block should also include a hash of the previous block, a timestamp, and a nonce. 

 4. Broadcast the new block: After the new block is created, it broadcasts to the network so that other nodes can validate it and add it to their copy of the blockchain. 

 5. Validate the new block: Once the new block is received by other nodes, they will validate it by checking its hash, the validity of the transactions, and the proof-of-work. If the block is valid, it will be added to their copy of the blockchain, and the new coins will be added to the total supply. 

 

Output Results: 

 

 

Transactions 

We have designed a transaction to record every transfer of digital assets from one address to another. It contains information about the sender, the recipient, and the amount of cryptocurrency being transferred.  

The transaction includes two types of transactions: coinbase transactions and peer-to-peer transaction.  

Transaction Class: 

Here we define class variables for the transaction. A transaction object has a 

Transaction id 

Transaction Input 

Transaction output 

For the transaction id, we applied the calculateTransactionHash() function to compute the hash of the transaction. 

The calculateTransactionHash() method calculates the hash of the transaction by concatenating the string representations of the input and output objects and then computing the SHA-256 hash of the resulting string. The method returns the hash as a string. 

The getTransactionId() returns the transactionId of the transaction. 

The getInput() returns the input object of the transaction. 

The getOutput() returns the output object of the transaction. 

 

Transaction Input Class: 

This Transaction input class represents an input to a transaction in a blockchain system. The class has five instance variables: previousTransactionId, previousIndex, signature, pubKey, and priKey. 

getPreviousTransactionId() :eturns the previousTransactionId of the input. 

getIndex() returns the previousIndex of the input. 

toString() returns a string representation of the previousTransactionId and previousIndex of the input. 

getSignature(String, PrivateKey, PublicKey) 

generates a digital signature using RSA encryption for the input data using the private key priKey. 

It first calls the RSASignUtils.sign() method to sign the input data using the private key, and then encodes the resulting signature using Base64 encoding. It then calls the RSASignUtils.verify() method to verify the signature using the public key pubKey. Here we checked whether the data came from the private key that is corresponding to the public key. If the verification is successful, the method returns the signature; otherwise, it returns an error message. 

Transaction Output Class: 

TransactionOutput class represents an output of a transaction in a blockchain system, defines the structure of our transaction output in a blockchain system. 

It has two private instance variables: address and amount. The address variable holds the recipient address of the output and while the amount carries the amount of currency being transferred. 

The constructor of the TransactionOutput class takes in an address and an amount as parameters and initialises the instance variables. 

getAddress() method returns the address of the output. 

getAmount() method returns the amount of the output. 

toString() method returns a string representation of the address and amount of the output. 

Once a transaction is created, it is broadcast to the network and validated by miners. If the transaction is valid, it is added to the blockchain and becomes a permanent record of the transfer. 

Output Results:  

 

Network 

The system hosts a peer-to-peer network that facilitates communication between nodes participating in the system. It uses the Transmission Control Protocol (TCP) to ensure that all nodes are guaranteed to receive information on any changes in the blockchain. As such, the java.net package was used to develop the network. 

 

A node in the network stores its own copy of the blockchain and participates in the network as both a server and a client. When a node first runs the program, it must input its port number. Its port number will be used by other peers to communicate with the node. After input of the port number, a text file storing all ports of nodes is appended by the input port number. 

 

In this system, the server can be referred to as the ‘receiver,’ by which it receives requests from its clients to change its copy of the blockchain if instructions to do that have been sent by its clients. As a server may have to listen to multiple clients, multithreading was used to satisfy this requirement. 

 

A client in the system can be referred to as a ‘sender,’ its job is to send instructions to alter the blockchain of its connected server’s node. Since the network is peer-to-peer, multiple clients (sockets) are created to facilitate sending requests to all servers in the network. This is achieved by allowing sockets to be created using a loop that iterates through a text file storing the ports of all nodes. This allows a node to be connected to all its peers and satisfies the requirement of many in the blockchain space to have a decentralized network. 

Output Results: 

 

Figure 1 illustrates the connections between each node in the case of 5 nodes participating in the network. 

 

Figure 1: 5 nodes connected to the system 

 