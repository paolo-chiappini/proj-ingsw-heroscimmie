package it.polimi.ingsw.client.view.gui.controllers.boardview;

import it.polimi.ingsw.client.view.gui.GuiController;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;

public class ChatController extends GuiController {
    public Button sendButton;
    public Button closeChatButton;
    public StackPane window;
    public TextField messageField;
    public TextArea chatTextArea;
    private BoardController boarController;

    public void setup(BoardController controller){
        this.boarController = controller;
    }

    public void send(){
        var message = messageField.getText().trim();
        messageField.clear();
        if(message.isEmpty()) return;
        if(message.startsWith("/w")){
            var splitString = message.split(" ", 3);
            var whisper = "";
            var to = "";
            if(splitString.length < 3){
                getView().notifyNewChatMessage(message);
            }else{
                to = splitString[1];
                whisper = splitString[2];
                getView().notifyNewChatWhisper(whisper, to);
            }

        }else{
            getView().notifyNewChatMessage(message);
        }
    }

    public void exit(MouseEvent e){
        closeChat();
    }

    public void closeChat(){
        window.setVisible(false);
    }

    public void openChat(){
        window.setVisible(true);
    }

    private void addMessage(String message, String sender){
        chatTextArea.appendText(sender+") "+message+"\n");
    }

    public void receiveChatMessage(String message, String sender, boolean isWhisper) {
        addMessage(message, sender + (isWhisper?" whispers":"") );
    }
}
