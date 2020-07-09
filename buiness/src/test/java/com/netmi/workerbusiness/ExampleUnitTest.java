package com.netmi.workerbusiness;

import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.security.KeyStore;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.util.Enumeration;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        FileInputStream is = null;
        try {
            is = new FileInputStream(new File("e:\\liemi.jks"));
            KeyStore keyStore = KeyStore.getInstance("JKS");
            //这里填设置的keystore密码，两个可以不一致
            keyStore.load(is, "123456".toCharArray());

            //加载别名，这里认为只有一个别名，可以这么取；当有多个别名时，别名要用参数传进来。不然，第二次的会覆盖第一次的
            Enumeration aliasEnum = keyStore.aliases();
            String keyAlias = "" ;
            while (aliasEnum.hasMoreElements()) {
                keyAlias = (String) aliasEnum.nextElement();
                System.out.println("别名"+keyAlias);
            }

            Certificate certificate = keyStore.getCertificate(keyAlias);

            //加载公钥
            PublicKey publicKey = keyStore.getCertificate(keyAlias).getPublicKey();
            System.out.println("别名"+publicKey.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

}