package com.jason.crypt;

import static com.jason.crypt.CommonSharePrefsManager.SP_NAME;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.util.Base64;
import android.view.View;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableEntryException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;

/**
 *
 * GCM算法模式 需要有初始向量iv 并且要存iv：加密完成得到iv；解密时拿alias+iv一起解密
 * sha1  sha256算法不可逆
 * alias不能重复
 *
 *
 */

public class MainActivity extends AppCompatActivity {


    private static KeyStore keyStore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CommonSharePrefsManager.getInstance().mManager = getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);

        AESUtilsV2.init();

//        try {
//            keyStore = KeyStore.getInstance("AndroidKeyStore");
//            keyStore.load(null);
//        } catch (KeyStoreException e) {
//            e.printStackTrace();
//        } catch (CertificateException e) {
//            e.printStackTrace();
//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }



    }


    String ttt = "ttt））@#~？S  \n\rttt";
    String tttt = "qqq））@#~？S  \n\rqqq";
    String ivString;
    String cptString;
    String cptStringg;
    int count = 0;
    int countD = 0;
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button:
//                cptString=cpt(ttt);
                if (count == 0) {
                     cptString=AESUtilsV2.newEncrypt(ttt,"");
                    ((TextView) (findViewById(R.id.button))).setText(cptString);
                }else {
                    cptStringg=AESUtilsV2.newEncrypt(tttt,"");
                    ((TextView) (findViewById(R.id.button))).setText(cptStringg);
                }
                count++;
                break;
            case R.id.button2:
//                String deString=decpt(cptString);
                if (countD == 0) {
                    String deString=AESUtilsV2.newDecrypt(cptString,"");
                    ((TextView) (findViewById(R.id.button2))).setText(deString);
                }else {
                    String deString=AESUtilsV2.newDecrypt(cptStringg,"");
                    ((TextView) (findViewById(R.id.button2))).setText(deString);
                }
                countD++;
                break;
        }
    }



    public String decpt(String ss) {
        Cipher cipher = null;
        byte[] bytes=Base64.decode(ss, Base64.DEFAULT);
        try {
            cipher = Cipher.getInstance("AES/GCM/NoPadding");
            SecretKey secretKey = getSecretKey("AES_KEY");
            GCMParameterSpec spec = new GCMParameterSpec(128, Base64.decode(ivString,Base64.DEFAULT));
            cipher.init(Cipher.DECRYPT_MODE, secretKey, spec);
            byte[] text= cipher.doFinal(bytes);
            return new String(text, "UTF-8");
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String cpt(String ss) {
        Cipher cipher = null;
        byte[] bytes = new byte[0];
        try {
            bytes = ss.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        try {
            cipher = Cipher.getInstance("AES/GCM/NoPadding");
            cipher.init(Cipher.ENCRYPT_MODE, generateKey("AES_KEY"));
            ivString = Base64.encodeToString(cipher.getIV(),Base64.DEFAULT);
            byte[] cryptText = cipher.doFinal(bytes);
            String cryptttt = Base64.encodeToString(cryptText, Base64.DEFAULT);
            return cryptttt;
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }
        return null;
    }

    private SecretKey getSecretKey(final String alias){
        try {
            return ((KeyStore.SecretKeyEntry) keyStore.getEntry(alias, null)).getSecretKey();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnrecoverableEntryException e) {
            e.printStackTrace();
        }
        return null;
    }

    private SecretKey generateKey(final String alias) {
        try {
            KeyGenerator keyGenerator = KeyGenerator
                    .getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore");
            keyGenerator.init(new KeyGenParameterSpec.Builder(alias,
                    KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
                    .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                    .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                    .build());
            return keyGenerator.generateKey();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        }
        return null;
    }

}