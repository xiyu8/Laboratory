package com.jason.crypt;

import android.os.Build;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableEntryException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.security.auth.x500.X500Principal;


public class AESUtils {

    private static KeyStore keyStore;
    private static final String TRANSFORMATION_MODE = "AES/GCM/NoPadding";
    private static final String ANDROID_KEY_STORE = "AndroidKeyStore";
    //    private static final String ALIAS = "AndroidKeyStore";
    private static final String ENCODE = "UTF-8";
    private static final int ALIAS_SIZE = 32;

    public static void init() {
        try {
            keyStore = KeyStore.getInstance(ANDROID_KEY_STORE);
            keyStore.load(null);
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static String newEncrypt(String ss,@Deprecated String key) {
        if(keyStore==null) init();
        String temp = tryToGet(ss);
        if(temp!=null) return temp;

        Cipher cipher = null;
        String ALIAS = getRandomAlias(ss);
        byte[] bytes = new byte[0];
        try {
            bytes = ss.getBytes(ENCODE);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        try {
            cipher = Cipher.getInstance(TRANSFORMATION_MODE);
            cipher.init(Cipher.ENCRYPT_MODE, generateKey(ALIAS));
            byte[] iv = cipher.getIV();
            byte[] cryptText = cipher.doFinal(bytes);
            String cryptttt = Base64.encodeToString(cryptText, Base64.DEFAULT);
            saveIV(iv,ALIAS+cryptttt);
            return ALIAS+cryptttt;
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

    public static String newDecrypt(String target, @Deprecated String key) {
        if(keyStore==null) init();
        Cipher cipher = null;
        String ALIAS = target.substring(0, ALIAS_SIZE);
        String data = target.substring(ALIAS_SIZE, target.length());
        byte[] bytes= Base64.decode(data, Base64.DEFAULT);
        try {
            cipher = Cipher.getInstance(TRANSFORMATION_MODE);
            SecretKey secretKey = getSecretKey(ALIAS);
            if(secretKey==null) return null;
            byte[] iv = getIv(target);
            if(iv==null) return null;
            GCMParameterSpec spec = new GCMParameterSpec(128, iv);
            cipher.init(Cipher.DECRYPT_MODE, secretKey, spec);
            byte[] text= cipher.doFinal(bytes);
            return new String(text, ENCODE);
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




    private static String tryToGet(String target) {
        String oldData=SharePrefsManager.getCommonInstance().getString(IV_DATA_KEY);
        ArrayList<AESUtils.IvItem> ivItems = new ArrayList<>();
        if (!TextUtils.isEmpty(oldData)) {
            ivItems=new Gson().fromJson(oldData,new TypeToken<ArrayList<AESUtils.IvItem>>(){}.getType());
        }
        for (AESUtils.IvItem ivItem : ivItems) {
            String decryptData = newDecrypt(ivItem.encryptData, null);
            if (decryptData!=null && decryptData.equals(target)) {
                return ivItem.encryptData;
            }
        }
        return null;
    }

    private static String getRandomAlias(String ss) {
        UUID uuid = UUID.randomUUID();
        return uuid.toString().replace("-", "");
    }

    private static SecretKey getSecretKey(final String alias){
        try {
            if (keyStore.containsAlias(alias)) {
                return ((KeyStore.SecretKeyEntry) keyStore.getEntry(alias, null)).getSecretKey();
            }else {
                return generateKey(alias);
            }
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnrecoverableEntryException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static SecretKey generateKey(final String alias) {
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

    private static final String IV_DATA_KEY = "IV_DATA_KEY";
    private static void saveIV(byte[] iv, String data) {
        String oldData=SharePrefsManager.getCommonInstance().getString(IV_DATA_KEY);
        ArrayList<AESUtils.IvItem> ivItems = new ArrayList<>();
        if (!TextUtils.isEmpty(oldData)) {
            ivItems=new Gson().fromJson(oldData,new TypeToken<ArrayList<AESUtils.IvItem>>(){}.getType());
        }
        for (AESUtils.IvItem ivItem : ivItems) {
            if (ivItem.getEncryptData().equals(data)) {
                ivItems.remove(ivItem);
                break;
            }
        }

        ivItems.add(new AESUtils.IvItem(data,iv));
        SharePrefsManager.getCommonInstance().putString(IV_DATA_KEY,new Gson().toJson(ivItems));
    }

    private static byte[] getIv(String encryptData) {
        String oldData=SharePrefsManager.getCommonInstance().getString(IV_DATA_KEY);
        ArrayList<AESUtils.IvItem> ivItems = new ArrayList<>();
        if (!TextUtils.isEmpty(oldData)) {
            ivItems=new Gson().fromJson(oldData,new TypeToken<ArrayList<AESUtils.IvItem>>(){}.getType());
        }
        for (AESUtils.IvItem ivItem : ivItems) {
            if (ivItem.getEncryptData().equals(encryptData)) {
                return ivItem.getIv();
            }
        }

        return null;
    }

    private static String sha1(String ss) {
        MessageDigest digester = null;
        try {
            digester  = MessageDigest.getInstance("SHA-1");
            digester.update(ss.getBytes(ENCODE));
            byte[] hashD = digester.digest();
            String hashDString = Base64.encodeToString(hashD, Base64.DEFAULT);
            return hashDString;
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static class IvItem{
        private String encryptData;
        private ArrayList<Byte> iv;

        public IvItem(String encryptData, byte[] iv) {
            this.encryptData = encryptData;
            this.iv = new ArrayList<Byte>(){{
                for (byte b : iv) {
                    add(b);
                }
            }};
        }

        public String getEncryptData() {
            return encryptData;
        }

        public void setEncryptData(String encryptData) {
            this.encryptData = encryptData;
        }

        public byte[] getIv() {
            byte[] b = new byte[iv.size()];
            for (int i = 0; i < iv.size(); i++) {
                b[i] = iv.get(i);
            }
            return b;
        }

        public void setIv(byte[] iv) {
            this.iv = new ArrayList<>();
            for (byte b : iv) {
                this.iv.add(b);
            }
        }
    }

}
