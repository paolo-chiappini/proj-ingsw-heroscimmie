package it.polimi.ingsw.client.view.cli.graphics.info;

import it.polimi.ingsw.client.view.cli.graphics.simple.CliBackColors;
import it.polimi.ingsw.client.view.cli.graphics.simple.CliForeColors;
import it.polimi.ingsw.client.view.cli.graphics.simple.FramedElement;
import it.polimi.ingsw.client.view.cli.graphics.simple.RowElement;
import it.polimi.ingsw.client.view.cli.graphics.util.CliDrawer;
import it.polimi.ingsw.client.view.cli.graphics.util.ReplaceTarget;

import java.util.ArrayDeque;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Represents the in-game chat.
 */
public class ChatElement extends FramedElement {
    private static final int WIDTH = 54;
    private static final int HEIGHT = 8;
    private static final int MAX_MESSAGES = 6;
    private static final int MAX_MESSAGE_LEN = 120;

    /**
     * Represents a single message in the chat.
     * @param sender sender of the message.
     * @param message message contents.
     * @param isWhisper true if the message is directed to the current player.
     */
    private record ChatMessage(String sender, String message, boolean isWhisper) {
        private static final CliForeColors WHISPER_COLOR = CliForeColors.BRIGHT_BLACK;
        public List<RowElement> getMessage() {
            List<RowElement> msgRows = new LinkedList<>();
            CliForeColors color = isWhisper ? WHISPER_COLOR : CliForeColors.DEFAULT;
            String sender = this.sender + (isWhisper ? " whispers" : "");

            String remainingMessage = sender + ": " + message;

            // wrap message to fit chat width.
            while (remainingMessage.length() > WIDTH - 2) {
                String substring = remainingMessage.substring(0, WIDTH - 2);
                remainingMessage = remainingMessage.substring(WIDTH - 2, remainingMessage.length() - 1);
                msgRows.add(new RowElement(substring, color, CliBackColors.DEFAULT));
            }
            msgRows.add(new RowElement(remainingMessage, color, CliBackColors.DEFAULT));

            return msgRows;
        }
    }
    private final Queue<RowElement> messages;

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
        ChatMessage newMessage = new ChatMessage(
                from,
                message.substring(0, Integer.min(MAX_MESSAGE_LEN, message.length())),
                isWhisper
        );

        List<RowElement> messageFormat = newMessage.getMessage();
        // make space for message and add it to the chat line by line.
        for (RowElement rowElement : messageFormat) {
            if (messages.size() == MAX_MESSAGES) messages.poll();
            messages.add(rowElement);
        }

        CliDrawer.clearArea(this, 1, 1, WIDTH - 2, HEIGHT - 2);
        // compile chat element.
        var messageIterator = messages.iterator();
        for (int i = 0; i < messages.size(); i++) {
            var currMessage = messageIterator.next();
            CliDrawer.superimposeElement(currMessage,this, 1, 1 + i, ReplaceTarget.EMPTY);
        }
    }
}
