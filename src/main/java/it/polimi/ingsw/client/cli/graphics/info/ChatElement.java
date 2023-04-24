package it.polimi.ingsw.client.cli.graphics.info;

import it.polimi.ingsw.client.cli.graphics.simple.CliBackColors;
import it.polimi.ingsw.client.cli.graphics.simple.CliForeColors;
import it.polimi.ingsw.client.cli.graphics.simple.FramedElement;
import it.polimi.ingsw.client.cli.graphics.simple.RowElement;
import it.polimi.ingsw.client.cli.graphics.util.CliDrawer;
import it.polimi.ingsw.client.cli.graphics.util.ReplaceTarget;

import java.util.ArrayDeque;
import java.util.Queue;

/**
 * Represents the in-game chat.
 */
public class ChatElement extends FramedElement {
    private static final int WIDTH = 54;
    private static final int HEIGHT = 8;
    private static final int MAX_MESSAGES = 6;

    /**
     * Represents a single message in the chat.
     * @param sender sender of the message.
     * @param message message contents.
     * @param isWhisper true if the message is directed to the current player.
     */
    private record ChatMessage(String sender, String message, boolean isWhisper) {
        private static final CliForeColors WHISPER_COLOR = CliForeColors.BRIGHT_BLACK;
        public RowElement getMessage() {
            CliForeColors color = isWhisper ? WHISPER_COLOR : CliForeColors.DEFAULT;
            return new RowElement(sender + ": " + message, color, CliBackColors.DEFAULT);
        }
    }
    private final Queue<ChatMessage> messages;

    public ChatElement() {
        super(WIDTH, HEIGHT);
        messages = new ArrayDeque<>();
    }

    /**
     * Adds a message to the chat. If the chat is full, the messages
     * will be scrolled upward.
     * @param message message to add.
     * @param from name of the player who wrote the message.
     * @param isWhisper true if the message is directed to the current player.
     */
    public void addMessage(String message, String from, boolean isWhisper) {
        if (messages.size() == MAX_MESSAGES) messages.poll();
        messages.add(new ChatMessage(from, message, isWhisper));

        CliDrawer.clearArea(this, 1, 1, WIDTH - 2, HEIGHT - 1);
        var messageIterator = messages.iterator();
        for (int i = 0; i < messages.size(); i++) {
            var currMessage = messageIterator.next();
            CliDrawer.superimposeElement(currMessage.getMessage(),this, 1, 1 + i, ReplaceTarget.EMPTY);
        }
    }

}
