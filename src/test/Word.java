package test;


import java.util.Arrays;
import java.util.Objects;

public class Word {
        public Tile[] tiles;
        public int row, col;
        public boolean vertical;

    public Word(Tile[] tiles, int col, int row, boolean vertical) {
        this.vertical = vertical;
        this.col = col;
        this.row = row;

        this.tiles = new Tile[tiles.length]; // ???
        for(int i = 0; i < tiles.length; i++)
        {
            this.tiles[i] = tiles[i];
        }
    }

    public Tile[] getTiles() {
        return tiles;
    }

    public int getCol() {
        return col;
    }

    public int getRow() {
        return row;
    }

    public boolean isVertical() {
        return vertical;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Word word = (Word) o;
        return row == word.row && col == word.col && vertical == word.vertical && Objects.deepEquals(tiles, word.tiles);
    }
}
