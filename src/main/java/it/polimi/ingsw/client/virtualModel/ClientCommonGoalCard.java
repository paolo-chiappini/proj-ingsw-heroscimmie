package it.polimi.ingsw.client.virtualModel;

import it.polimi.ingsw.util.observer.ModelObservable;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * This class represents a single common goal card in the virtual model.
 * It aims to represent the state of the card in the client and update it if necessary
 */
public class ClientCommonGoalCard extends ModelObservable {

    private int id;
    private int currPoints; // represents the maximum current score that can be obtained

    public ClientCommonGoalCard() {
        this.id = 0;
        currPoints = 8;
    }

    /**
     * Set the card's ID
     * @param id is the ID of the common goal card
     */
    public void setId(int id) {
        this.id = id;
        notifyObservers(obs->obs.setCommonGoal(id,currPoints));
    }

    /**
     * Set the maximum current score that can be obtained
     * @param points points that can be obtained by completing a goal card
     */
    public void setScore(int points) {
        this.currPoints = points;
        notifyObservers(obs->obs.updateCommonGoalPoints(getId(),getScore()));
    }

    /**
     * Get the card's ID
     * @return ID of the common goal card
     */
    public int getId() {
        return id;
    }

    /**
     * Get the maximum current score that can be obtained
     * @return the score that a player can obtain by completing the card's goal
     */
    public int getScore() {
        return currPoints;
    }

    /**
     * Updates the card's ID
     * @param data contains up-to-date card details
     */
    public void updateId(String data)
    {
        JSONObject jsonObject = new JSONObject(data);
        int idCard = jsonObject.getInt("card_id");
        setId(idCard);
    }

    /**
     * Updates available points
     * @param data contains up-to-date card details
     */
    public void updatePoints(String data)
    {
        JSONObject jsonObject = new JSONObject(data);
        JSONArray points = jsonObject.getJSONArray("points");
        currPoints = points.getInt(points.length()-1);
        setScore(currPoints);
    }
}