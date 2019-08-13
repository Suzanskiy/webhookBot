# webhookBot
Первым делом мы инициализируем контекст api

```ApiContextInitializer.init();```

На этом этапе происходит два события
  
1.Мы регистрируем ссесию для лонгполлинг и вебхука

```
public static void init() 
      {
          ApiContext.register(BotSession.class, DefaultBotSession.class);//longpolling          
          ApiContext.register(Webhook.class, DefaultWebhook.class);
      }
```
    
2.При этом срабатывает этот метод класса ApiContext

``` 
public static <T, S extends T> void register(Class<T> type, Class<S> implementation) 
    {
        if (bindings.containsKey(type)) 
        {
            BotLogger.debug("ApiContext", MessageFormat.format("Class {0} already registered", type.getName()));
        }
        bindings.put(type, implementation);
    }
```
    
Который складывает интерфейс и его реализацию в мапу

 ` private static Map<Class, Class> bindings = new HashMap<>();`

Из которой в дальнейшем мы можем получать нужную реализацию для вебхука или лонгполлинга.

Далее мы создает Api telegram( по сути поднимаем гриззли,который будет обрабатывать поступающие от телеграмм POST запросы)
```
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
```

Когда мы создаем объект класса TelegramBotsApi в конструкторе происходит настройка вебхука и старт гризли сервера
```
public TelegramBotsApi(String keyStore, String keyStorePassword, String externalUrl, String internalUrl) throws TelegramApiRequestException {
        if (externalUrl == null || externalUrl.isEmpty()) {
            throw new TelegramApiRequestException("Parameter externalUrl can not be null or empty");
        }
        if (internalUrl == null || internalUrl.isEmpty()) {
            throw new TelegramApiRequestException("Parameter internalUrl can not be null or empty");
        }
        if (keyStore == null || keyStore.isEmpty()) {
            throw new TelegramApiRequestException("Parameter keyStore can not be null or empty");
        }
        if (keyStorePassword == null || keyStorePassword.isEmpty()) {
            throw new TelegramApiRequestException("Parameter keyStorePassword can not be null or empty");
        }

        this.useWebhook = true;
        this.extrenalUrl = fixExternalUrl(externalUrl);
        webhook = ApiContext.getInstance(Webhook.class);// достаем вебхук из мапы bindings
        webhook.setInternalUrl(internalUrl);
        webhook.setKeyStore(keyStore, keyStorePassword);
        webhook.startServer();
    }
```
Дальше мы создаем нашу реализацию вебхука и регестрируем ее
```
WebhookBot bot = new WebhookBot();
        try
        {
            botsApi.registerBot(bot);

        } catch (TelegramApiRequestException e)
        {
            BotLogger.error(LOGTAG,e);
        }
```

При запуске этого кода Гриззли пишет что умпешно стартовал. Но бот не работает. Ошибку я нашел только в логах сервера.

2018/07/29 15:08:43 [error] 1166#1166: *453 openat() "/var/www/www->root/data/www/mydomain.net/callback/WebhookClass failed
(2: No such file or directory), client: 149.154.167.227, server: example.net request: "POST >/callback/WebhookClass HTTP/1.1",
host: "mydomain.net"

Приложение я собираю в jar with dependencies и запускаю на сервере ubuntu 16.04 командой java -jar




