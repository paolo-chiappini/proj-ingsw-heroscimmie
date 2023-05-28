package it.polimi.ingsw.mock;

import it.polimi.ingsw.server.model.goals.personal.PersonalGoalCard;
import it.polimi.ingsw.server.model.bookshelf.IBookshelf;
import it.polimi.ingsw.server.model.player.IPlayer;
import it.polimi.ingsw.util.serialization.Serializer;

public class PlayerMock implements IPlayer {
    int score;
    final String name;
    IBookshelf bookshelf;
    PersonalGoalCard personalGoalCard;

    public PlayerMock(String name, int score, IBookshelf bookshelf, PersonalGoalCard personalGoalCard) {
        this.name = name;
        this.score = score;
        this.bookshelf = bookshelf;
        this.personalGoalCard = personalGoalCard;
    }

    @Override
    public String getUsername() {
        return name;
    }

    @Override
    public int getScore() {
        return score;
    }

    @Override
    public IBookshelf getBookshelf() {
        return bookshelf;
    }

    @Override
    public void setBookshelf(IBookshelf bookshelf) {
        this.bookshelf = bookshelf;
    }

    @Override
    public void setPersonalGoalCard(PersonalGoalCard personalGoalCard) {
        this.personalGoalCard = personalGoalCard;
    }

    @Override
    public PersonalGoalCard getPersonalGoalCard() {
        return personalGoalCard;
    }

    @Override
    public void addPointsToScore(int points) {
        score += points;
    }

    @Override
    public String serialize(Serializer serializer) {
        return serializer.serialize(this);
    }
}
