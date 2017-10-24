/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package hybridvcs;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Arrays;
import javax.crypto.Cipher;
import javax.crypto.KeyAgreement;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 *
 * @author James Gagnon
 */
public class DHCRYPT {

private static final int AES_KEY_SIZE = 128;

    public static void main(String[] args) {
       
        
    }

    private static KeyPairGenerator kpg;

    static {
        try {

            kpg = KeyPairGenerator.getInstance("DH");
            kpg.initialize(1024);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    public static SecretKey agreeSecretKey(PrivateKey prk_self,
            PublicKey pbk_peer, boolean lastPhase) throws Exception {
        // instantiates and inits a KeyAgreement
        KeyAgreement ka = KeyAgreement.getInstance("DH");
        ka.init(prk_self);
        // Computes the KeyAgreement
        ka.doPhase(pbk_peer, lastPhase);
        // Generates the shared secret
        byte[] secret = ka.generateSecret();


        MessageDigest sha256 = MessageDigest.getInstance("SHA-256"); 
        byte[] bkey = Arrays.copyOf(
                sha256.digest(secret), AES_KEY_SIZE / Byte.SIZE);

        SecretKey desSpec = new SecretKeySpec(bkey, "AES");
        return desSpec;
    }

    public static KeyPair genDHKeyPair() {
        return kpg.genKeyPair();
    }
}