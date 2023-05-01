package it.polimi.ingsw.client.virtualModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents a single common goal card in the virtual model.
 * It aims to represent the state of the card in the client and update it if necessary
 */
public class ClientCommonGoalCard {

    private int id;
    private int score; // represents the last point in points, i.e. the first to be obtained
    private List<Integer> points = new ArrayList<>();
    private List<String> players;

    public ClientCommonGoalCard(int numPlayer) {
        this.id = 0;
        score = 8;
        players = new ArrayList<>();
        points.add(4);
        points.add(8);
        if(numPlayer==4)
        {
            points.add(0,2);
            points.add(2,6);
        }
        else if(numPlayer==3)
            points.add(1,6);
    }

    /**
     * Set the card's ID
     * @param id is the ID of the common goal card
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Set the list of awarded players
     * @param players list of players who have already scored points from the card
     */
    public void setPlayers(List<String> players) {
        this.players = players;
    }

    /**
     * Set the list of points available for the card
     * @param points list of points that can be obtained by completing a goal card
     */
    public void setPoints(List<Integer> points) {
        this.points = points;
    }

    /**
     * Set the maximum current score that can be obtained
     * @param points list of points that can be obtained by completing a goal card
     */
    public void setScore(List<Integer> points) {
        this.score = points.get(points.size()-1);
    }

    /**
     * Get the card's ID
     * @return ID of the common goal card
     */
    public int getId() {
        return id;
    }

    /**
     * Get the list of awarded players
     * @return a list of players who have already scored points from the card
     */
    public List<String> getPlayers() {
        return new ArrayList<>(players);
    }

    /**
     * Get the list of points available for the card
     * @return a list of points that can be obtained by completing a goal card
     */
    public ArrayList<Integer> getPoints() {
        return new ArrayList<>(points);
    }

    /**
     * Get the maximum current score that can be obtained
     * @return the score that a player can obtain by completing the card's goal
     */
    public int getScore() {
        return score;
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
     * Updates the awarded players
     * @param data contains up-to-date card details
     */
    public void updateAwardedPlayers(String data)
    {
        JSONObject jsonObject = new JSONObject(data);
        JSONArray players = jsonObject.getJSONArray("completed_by");
        List<String> awardedPlayers= new ArrayList<>();
        for (int i = 0; i < players.length(); i++) {
            awardedPlayers.add(players.getString(i));
        }
        setPlayers(awardedPlayers);
    }

    /**
     * Updates available points
     * @param data contains up-to-date card details
     */
    public void updatePoints(String data)
    {
        JSONObject jsonObject = new JSONObject(data);
        JSONArray points = jsonObject.getJSONArray("points");
        List<Integer> pointsCard = new ArrayList<>();
        for (int i = 0; i < points.length(); i++) {
            pointsCard.add(points.getInt(i));
        }
        score=pointsCard.get(pointsCard.size()-1);
        setPoints(pointsCard);
        setScore(pointsCard);
    }
}