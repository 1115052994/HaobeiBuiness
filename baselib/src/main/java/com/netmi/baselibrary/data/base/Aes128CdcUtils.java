package com.netmi.baselibrary.data.base;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.netmi.baselibrary.utils.AppUtils;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Base64;

import java.nio.charset.Charset;
import java.security.Security;
import java.util.Random;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * 类描述：
 * 创建人：Simple
 * 创建时间：2019/4/18
 * 修改备注：
 */
public class Aes128CdcUtils {
    /***
     * EDC aesKey
     *  test（测试）：Yjh2U2xYb0pBOXdlME9OZw==
     *  dev（开发）：TWsxWTR1VXpjT3EyT2Y5cw==
     *  admin（正式）：dlZCY1ljdVVoc2p2dnoyTw==
     */

    private static final Charset UTF_8 = Charset.forName("UTF-8");

    private String AES_KEY = "Yjh2U2xYb0pBOXdlME9OZw==";

    private static final String ALGORITHM_TYPE = "AES";

    private static final String TRANSFORMATION = "AES/CBC/PKCS7PADDING";

    private static final String TRANSFORMATION_NO_PADDING = "AES/CBC/NoPadding";

    private Random ivRandom = new Random();

    public static Aes128CdcUtils getInstance() {
        return Aes128CdcUtils.SingletonHolder.instance;
    }

    /**
     * 静态内部类,只有在装载该内部类时才会去创建单例对象
     */
    private static class SingletonHolder {
        private static final Aes128CdcUtils instance = new Aes128CdcUtils();
    }

    private Aes128CdcUtils() {
        Security.addProvider(new BouncyCastleProvider());
        AES_KEY = AppUtils.isDebug() ? "Yjh2U2xYb0pBOXdlME9OZw==" : "dlZCY1ljdVVoc2p2dnoyTw==";
    }

    /**
     * 加密：对字符串进行加密，并返回字符串
     * 规则：Base64.encode(加密后的字符串 + "::" + 向量)
     */
    public String encrypt(String encryptStr) {
        try {
            String vector = getRandomString(16);
            IvParameterSpec ivParameterSpec = new IvParameterSpec(vector.getBytes(UTF_8));
            SecretKeySpec skeySpec = new SecretKeySpec(Base64.decode(AES_KEY), ALGORITHM_TYPE);

            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, ivParameterSpec);
            byte[] encrypted = cipher.doFinal(encryptStr.getBytes());

            byte[] encode = Base64.encode(encrypted);

            return new String(Base64.encode((new String(encode) + "::" + vector).getBytes(UTF_8)), UTF_8);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * 解密：对加密后的十六进制字符串(hex)进行解密，并返回字符串
     */
    public String decrypt(String encryptContent) {

        try {
            if (JSON.parse(encryptContent) != null) {
                return encryptContent;
            }
        } catch (JSONException e) {
            //加密的内容
        }

        String content = new String(Base64.decode(encryptContent), UTF_8), result = null;

        try {
            String encryptedStr = content.split("::")[0], iv = content.split("::")[1];

            IvParameterSpec ivParameterSpec = new IvParameterSpec(iv.getBytes(UTF_8));
            SecretKeySpec skeySpec = new SecretKeySpec(Base64.decode(AES_KEY), ALGORITHM_TYPE);

            Cipher cipher = Cipher.getInstance(TRANSFORMATION_NO_PADDING);
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, ivParameterSpec);

            byte[] decode = Base64.decode(encryptedStr);

            byte[] original = cipher.doFinal(decode);

            result = new String(original);
            //去除多余的补位，只保留json字符串，
            if (!TextUtils.isEmpty(result)) {
                if (result.startsWith("{")) {
                    result = result.substring(0, result.lastIndexOf("}") + 1);
                } else if (result.startsWith("[")) {
                    result = result.substring(0, result.lastIndexOf("]") + 1);
                }
            }
            return result;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return result;
    }

    //随机字符串
    private String getRandomString(int len) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890";
        int maxPos = chars.length();
        StringBuilder pwd = new StringBuilder();
        for (int i = 0; i < len; i++) {
            pwd.append(chars.charAt(ivRandom.nextInt(maxPos)));
        }
        return pwd.toString();
    }

}
