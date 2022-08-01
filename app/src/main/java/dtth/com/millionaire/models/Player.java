package dtth.com.millionaire.models;

public class Player {
    public String name;
    public int score = 0;

    public Player(String name) {
        this.name = name;
    }

    public Player(String name, int score) {
        this.name = name;
        this.score = score;
    }
}
