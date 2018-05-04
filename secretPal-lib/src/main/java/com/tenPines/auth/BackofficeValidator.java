package com.tenPines.auth;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;

import static java.nio.charset.StandardCharsets.UTF_8;

@Service
public class BackofficeValidator {

    private static final String CRYPTO_ALGORITHM = "HmacSHA256";
    private SecretKeySpec secret;

    public BackofficeValidator(@Value("${backoffice.secret}") String secret) {
        this.secret = new SecretKeySpec(secret.getBytes(UTF_8), CRYPTO_ALGORITHM);
    }

    public boolean isFromBackoffice(Long uid, String email, String username, String fullName, Boolean root, String hmac)
            throws SignatureException, InvalidKeyException, NoSuchAlgorithmException {
        // Because DatetypeConverter return uppercase hex
        hmac = hmac.toUpperCase();
        String uriToValidate = createUriToValidate(uid, email, username, fullName, root);

        Mac mac = Mac.getInstance(CRYPTO_ALGORITHM);
        mac.init(this.secret);

        byte[] byteResult = mac.doFinal(uriToValidate.getBytes(UTF_8));

        return hmac.equals(DatatypeConverter.printHexBinary(byteResult));
    }

    private String createUriToValidate(Long uid, String email, String username, String fullName, Boolean root) {
        /*
         * The order is important, this is completely dependant on the backoffice
         * see https://github.com/10Pines/10pines-bk/blob/d6a358dfc0433eb1a9a51ad2ec4760c600f469e2/app/models/auth_app.rb#L9
         */
        return String.join("&",
                "uid=" + uid.toString(),
                "email=" + email,
                "username=" + username,
                "full_name=" + fullName,
                "root=" + root.toString()
        );
    }
}
