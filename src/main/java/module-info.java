module it.polimi.ingsw.client.view.gui {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.json;

    opens it.polimi.ingsw.client.view.gui to javafx.fxml;
    exports it.polimi.ingsw.client.view.gui;
    exports it.polimi.ingsw.client.view.gui.controllers;
    opens it.polimi.ingsw.client.view.gui.controllers to javafx.fxml;
    exports it.polimi.ingsw.client.view.gui.controllers.boardview;
    opens it.polimi.ingsw.client.view.gui.controllers.boardview to javafx.fxml;
    exports it.polimi.ingsw.server.messages;
    exports it.polimi.ingsw.client.view.gui.controllers.boardview.graphicelements;
}