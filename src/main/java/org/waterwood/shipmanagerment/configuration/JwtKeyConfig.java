package org.waterwood.shipmanagerment.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Configuration
public class JwtKeyConfig {
    @Value("${jwt.private-Key}")
    private Resource privateKey;
    @Value("${jwt.public-Key}")
    private Resource publicKey;

    @Bean
    public PrivateKey getPrivateKey() throws Exception {
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(
                extraSSHKeyBase64ContentToBytes(privateKey));
        return KeyFactory.getInstance("RSA").generatePrivate(keySpec);
    }

    @Bean
    public PublicKey getPublicKey() throws Exception {
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(
                extraSSHKeyBase64ContentToBytes(publicKey)
        );
        return KeyFactory.getInstance("RSA").generatePublic(keySpec);
    }
    private byte[] extraSSHKeyBase64ContentToBytes(Resource res) throws IOException {
        String content = new String(res.getInputStream().readAllBytes());
        if(content.startsWith("-----BEGIN")){
            content =content.replaceAll("-----BEGIN.*?-----", "")
                    .replaceAll("-----END (.*)-----", "")
                    .replaceAll("\\s+", "");
        }
        return Base64.getDecoder().decode(content);
    }
}
