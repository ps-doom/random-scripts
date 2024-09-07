import burp.api.montoya.BurpExtension;
import burp.api.montoya.MontoyaApi;
import burp.api.montoya.http.handler.*;
import burp.api.montoya.http.message.requests.HttpRequest;
import burp.api.montoya.logging.Logging;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.UnsupportedEncodingException;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class ExtensionTemplate implements BurpExtension, HttpHandler {
    Logging logging;
    MontoyaApi api;

    @Override
    public void initialize(MontoyaApi api) {
        this.api = api;
        api.extension().setName("Custom Extension Loaded");
        this.logging = api.logging();
        this.logging.logToOutput("Encrypting Request body sent through Repeater Tool");
        api.http().registerHttpHandler(this);
        }

    public static String encryptThis (String plainText) throws  NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, InvalidKeyException, UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException{
        String PUBLIC_KEY_PEM ="<ENTER RSA PUBLIC KEY>";
        byte[] keyBytes = Base64.getDecoder().decode(PUBLIC_KEY_PEM);
        X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey publicKey = keyFactory.generatePublic(spec);
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] encryptedBytes = cipher.doFinal(plainText.getBytes("UTF-8"));
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    @Override
    public RequestToBeSentAction handleHttpRequestToBeSent(HttpRequestToBeSent httpRequestToBeSent) {
        HttpRequest request = httpRequestToBeSent;
        String updatedRequestBody = null;
        if (httpRequestToBeSent.toolSource().toolType().toolName().toString() == "Repeater" && request.method().equals("POST") ) {
           try {
               String requestBody = request.bodyToString();
               updatedRequestBody = encryptThis(requestBody);
           }
           catch (Exception e) {
                    throw new RuntimeException(e);
           }
        }
        return RequestToBeSentAction.continueWith(request.withBody(updatedRequestBody));
    }

    @Override
    public ResponseReceivedAction handleHttpResponseReceived(HttpResponseReceived httpResponseReceived) {
        return null;
    }
}
