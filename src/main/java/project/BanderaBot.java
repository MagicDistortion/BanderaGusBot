package project;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendAnimation;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

public class BanderaBot extends TelegramLongPollingBot {
    private static final String botName = "MagicDistortionBot";
    private static final String botToken = "6111291896:AAH83mfIgO5_OyZfM5VnaZyUL56VRmBuBOU";
    private final Map<Long, Integer> levels = new HashMap<>();

    public static void main(String[] args) throws TelegramApiException {
        TelegramBotsApi api = new TelegramBotsApi(DefaultBotSession.class);
        api.registerBot(new BanderaBot());
    }

    @Override
    public String getBotUsername() {
        return botName;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public void onUpdateReceived(Update update) {
        Long chatId = getChatId(update);
        SendMessage message = new SendMessage();
        if (update.hasMessage() && update.getMessage().getText().equals("/start")) {
            setLevel(chatId, 1);
            sendImage("level-1", chatId);
            message = createMessage("Ґа-ґа-ґа!\n" +
                    "Вітаємо у боті біолабораторії «Батько наш Бандера».\n" +
                    "\n" +
                    "Ти отримуєш гусака №71\n" +
                    "\n" +
                    "Цей бот ми створили для того, щоб твій гусак прокачався з рівня звичайної свійської худоби до рівня біологічної зброї, здатної нищити ворога. \n" +
                    "\n" +
                    "Щоб звичайний гусак перетворився на бандерогусака, тобі необхідно:\n" +
                    "✔️виконувати завдання\n" +
                    "✔️переходити на наступні рівні\n" +
                    "✔️заробити достатню кількість монет, щоб придбати Джавеліну і зробити свєрхтра-та-та.\n" +
                    "\n" +
                    "*Гусак звичайний.* Стартовий рівень.\n" +
                    "Бонус: 5 монет.\n" +
                    "Обери завдання, щоб перейти на наступний рівень");
            attachButtons(message, getRandom(List.of(
                    "Сплести маскувальну сітку (+15 монет)"
                    , "Зібрати кошти патріотичними піснями (+15 монет)"
                    , "Вступити в Міністерство Мемів України (+15 монет)"
                    , "Запустити волонтерську акцію (+15 монет)"
                    , "Вступити до лав тероборони (+15 монет)"))
                    .stream().collect(Collectors.toMap(k -> k, v -> "level 1 task")));
            message.setChatId(chatId);
            sendApiMethodAsync(message);
        }
        if (update.hasCallbackQuery()) {
            String data = update.getCallbackQuery().getData();
            if (data.equals("level 1 task") && getLevel(chatId) == 1) {
                setLevel(chatId, 2);
                sendImage("level-2", chatId);
                message = createMessage(
                        "*Вітаємо на другому рівні! Твій гусак - біогусак.*\n" +
                                "Баланс: 20 монет. \n" +
                                "Обери завдання, щоб перейти на наступний рівень");
                attachButtons(message, getRandom(List.of(
                        "Зібрати комарів для нової біологічної зброї (+15 монет)"
                        , "Пройти курс молодого бійця (+15 монет)"
                        , "Задонатити на ЗСУ (+15 монет)"
                        , "Збити дрона банкою огірків (+15 монет)"
                        , "Зробити запаси коктейлів Молотова (+15 монет)"))
                        .stream().collect(Collectors.toMap(k -> k, v -> "level 2 task")));
            } else if (data.equals("level 2 task") && getLevel(chatId) == 2) {
                setLevel(chatId, 3);
                sendImage("level-3", chatId);
                message = createMessage(
                        "*Вітаємо на третьому рівні! Твій гусак - бандеростажер.*\n" +
                                "Баланс: 35 монет. \n" +
                                "Обери завдання, щоб перейти на наступний рівень");
                attachButtons(message, getRandom(List.of(
                        "Злітати на тестовий рейд по чотирьох позиціях (+15 монет)"
                        , "Відвезти гуманітарку на передок (+15 монет)"
                        , "Знайти зрадника та здати в СБУ (+15 монет)"
                        , "Навести арту на орків (+15 монет)"
                        , "Притягнути танк трактором (+15 монет)"))
                        .stream().collect(Collectors.toMap(k -> k, v -> "level 3 task")));
            } else if (data.equals("level 3 task") && getLevel(chatId) == 3) {
                setLevel(chatId, 4);
                sendImage("level-4", chatId);
                message = createMessage(
                        "*Вітаємо на останньому рівні! Твій гусак - готова біологічна зброя - бандерогусак.*\n" +
                                "Баланс: 50 монет. \n" +
                                "Тепер ти можеш придбати Джавелін і глушити чмонь");
                attachButtons(message, Map.of(
                        "Купити Джавелін (50 монет)", "rusni pizda"
                ));
            } else if (data.equals("rusni pizda") && getLevel(chatId) == 4) {
                setLevel(chatId, 5);
                sendImage("final", chatId);
                message = createMessage("*Джавелін твій. Повний вперед!*");
            }
            message.setChatId(chatId);
            sendApiMethodAsync(message);
        }
    }

    private SendMessage createMessage(String text) {
        SendMessage message = new SendMessage();
        message.setText(getEncodedString(text));
        message.setParseMode("markdown");
        return message;
    }

    public Long getChatId(Update update) {
        long id = 0L;
        if (update.hasMessage()) id = update.getMessage().getFrom().getId();
        else if (update.hasCallbackQuery()) id = update.getCallbackQuery().getFrom().getId();
        return id > 0 ? id : null;
    }

    public void attachButtons(SendMessage sendMessage, Map<String, String> buttons) {
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        buttons.keySet().forEach(buttonName -> {
            String buttonValue = buttons.get(buttonName);
            InlineKeyboardButton button = new InlineKeyboardButton();
            button.setText(getEncodedString(buttonName));
            button.setCallbackData(buttonValue);
            keyboard.add(List.of(button));
        });
        markup.setKeyboard(keyboard);
        sendMessage.setReplyMarkup(markup);
    }

    public void sendImage(String name, Long chatId) {
        SendAnimation animation = new SendAnimation();
        animation.setAnimation(new InputFile().setMedia(new File("images/" + name + ".gif")));
        animation.setChatId(chatId);
        executeAsync(animation);
    }

    public List<String> getRandom(List<String> variants) {
        List<String> list = new ArrayList<>(variants);
        Collections.shuffle(list);
        return list.subList(0, 3);

    }

    public int getLevel(Long chatId) {
        return this.levels.getOrDefault(chatId, 1);
    }

    public void setLevel(Long chatId, int level) {
        this.levels.put(chatId, level);
    }

    private String getEncodedString(String string) {
        return new String(string.getBytes(), StandardCharsets.UTF_8);
    }
}