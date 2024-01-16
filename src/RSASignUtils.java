
import java.io.*;
import java.security.*;


public class RSASignUtils {

    private static final String ALGORITHM = "RSA";
    private static final int KEY_SIZE = 2048;
    private static final String SIGNATURE_ALGORITHM = "Sha1WithRSA";

    public static KeyPair generateKeyPair() throws Exception {
        // get the generator algorithm
        KeyPairGenerator gen = KeyPairGenerator.getInstance(ALGORITHM);
        gen.initialize(KEY_SIZE);
        return gen.generateKeyPair();
    }

    public static byte[] sign(byte[] data, PrivateKey priKey) throws Exception {

        Signature sign = Signature.getInstance(SIGNATURE_ALGORITHM);

        sign.initSign(priKey);

        sign.update(data);
        byte[] signInfo = sign.sign();

        return signInfo;
    }

    public static boolean verify(byte[] data, byte[] signInfo, PublicKey pubKey) throws Exception {

        Signature sign = Signature.getInstance(SIGNATURE_ALGORITHM);

        sign.initVerify(pubKey);

        sign.update(data);
        boolean verify = sign.verify(signInfo);

        return verify;
    }

    public static byte[] signFile(File file, PrivateKey priKey) throws Exception {

        Signature sign = Signature.getInstance(SIGNATURE_ALGORITHM);


        sign.initSign(priKey);

        InputStream in = null;

        try {
            in = new FileInputStream(file);

            byte[] buf = new byte[1024];
            int len = -1;

            while ((len = in.read(buf)) != -1) {
                sign.update(buf, 0, len);
            }

        } finally {
            close(in);
        }
        return sign.sign();
    }

    public static boolean verifyFile(File file, byte[] signInfo, PublicKey pubKey)
            throws Exception {

        Signature sign = Signature.getInstance(SIGNATURE_ALGORITHM);

        sign.initVerify(pubKey);

        InputStream in = null;

        try {
            in = new FileInputStream(file);

            byte[] buf = new byte[1024];
            int len = -1;

            while ((len = in.read(buf)) != -1) {
                sign.update(buf, 0, len);
            }

        } finally {
            close(in);
        }

        return sign.verify(signInfo);
    }

    private static void close(Closeable c) {
        if (c != null) {
            try {
                c.close();
            } catch (IOException e) {
                // nothing
            }
        }
    }

}
