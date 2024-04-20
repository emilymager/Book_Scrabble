package test;


import java.util.Objects;

public class Tile {
    public final int score;
    public final char letter;


    private Tile(int score, char letter) {
        this.score = score;
        this.letter = letter;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tile tile = (Tile) o;
        return score == tile.score && letter == tile.letter;
    }

    @Override
    public int hashCode() {
        return Objects.hash(score, letter);
    }

    public static class Bag {

    }


}
