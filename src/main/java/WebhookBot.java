import org.telegram.telegrambots.api.methods.BotApiMethod;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramWebhookBot;

public class WebhookBot  extends TelegramWebhookBot {
    @Override
    public BotApiMethod onWebhookUpdateReceived(Update update)
    {
        if (update.hasMessage() && update.getMessage().hasText())
        {
            String message_text = update.getMessage().getText();
            long chat_id = update.getMessage().getChatId();

            if (message_text.equals("/start"))
            {
                SendMessage message = new SendMessage().setChatId(chat_id).setText("Hello there!");
                return message;
            }
        }
        return null;
    }

    @Override
    public String getBotUsername() {
        return "WebhookBot";
    }

    @Override
    public String getBotToken() {
        return "mytoken";
    }

    @Override
    public String getBotPath() {
        return "WebhookBot";
    }
}
