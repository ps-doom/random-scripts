import burp.api.montoya.BurpExtension;
import burp.api.montoya.MontoyaApi;
import burp.api.montoya.logging.Logging;

public class ExtensionTemplate implements BurpExtension, HttpHandler {
    Logging logging;
    MontoyaApi api;

    @Override
    public void initialize(MontoyaApi api) {
        this.api = api;
        api.extension().setName("Custom Extension");
        this.logging = api.logging();
        this.logging.logToOutput("Custom Extension Loaded Successfully");
        api.http().registerHttpHandler(this);
        }
}
