import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.exceptions.TelegramApiRequestException;
import org.telegram.telegrambots.logging.BotLogger;

public class Main {
    private static final String LOGTAG = "MAIN";

    public static void main(String[] args)
    {
        ApiContextInitializer.init();
        TelegramBotsApi botsApi = null;
        try
        {
            botsApi = new TelegramBotsApi(
                    "src/main/resources/myDomain.net.jks",
                    "myPassword",
                    "https://myDomain.net:443",
                    "https://127.0.0.1:443");

        } catch (TelegramApiRequestException e)
        {
            BotLogger.error(LOGTAG,e);
        }
        WebhookBot bot = new WebhookBot();
        try
        {
            botsApi.registerBot(bot);

        } catch (TelegramApiRequestException e)
        {
            BotLogger.error(LOGTAG,e);
        }
    }
}
