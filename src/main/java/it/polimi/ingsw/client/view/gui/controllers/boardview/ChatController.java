package it.polimi.ingsw.client.view.gui.controllers.boardview;

import it.polimi.ingsw.client.view.gui.GuiController;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

public class ChatController extends GuiController {
    public Button sendButton;
    public Button closeChatButton;
    public StackPane window;
    public TextField messageField;
    public TextFlow chatTextArea;
    public ScrollPane scrollPane;

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

    public void receiveChatMessage(String message, String sender, boolean isWhisper) {
        Text t = new Text();
        if(isWhisper){
            t.setFill(Color.rgb(0, 0, 0, 0.5));
            t.setStyle("-fx-font-style: italic");
        }
        t.setText(sender+(isWhisper?" whispers":"")+") "+message+"\n");
        chatTextArea.getChildren().add(t);
    }
}
