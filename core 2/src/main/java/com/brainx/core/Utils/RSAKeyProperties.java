//package com.brainx.core.Utils;
//
//import lombok.Data;
//import org.springframework.stereotype.Component;
//
//import java.security.KeyPair;
//import java.security.interfaces.RSAPrivateKey;
//import java.security.interfaces.RSAPublicKey;
//
//@Component
//@Data
//public class RSAKeyProperties {
//    private RSAPublicKey publicKey;
//    private RSAPrivateKey privateKey;
//
//    public RSAKeyProperties() {
//        KeyPair keyPair = KeyGeneratorUtil.generateRSAKey();
//        this.publicKey = (RSAPublicKey) keyPair.getPublic();
//        this.privateKey = (RSAPrivateKey) keyPair.getPrivate();
//    }
//
//    public RSAKeyProperties(RSAPublicKey publicKey, RSAPrivateKey privateKey) {
//        KeyPair pair = KeyGeneratorUtil.generateRSAKey();
//        this.publicKey = (RSAPublicKey) pair.getPublic();
//        this.privateKey = (RSAPrivateKey) pair.getPrivate();
//    }
//}
