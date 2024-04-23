package test;


import java.util.ArrayList;
import java.util.Objects;

public class Board {
    public Square[][] squares; // ?
    public ArrayList<Word> words;
    public Tile[][] tiles ;
    private static Board board;

    public static Board getBoard()
    {
        if(board == null)
            board = new Board();

        return board;
    }

    public Board() {
        this.squares = new Square[15][15];
        //this.words;
        this.tiles = new Tile[15][15];

        int[][] specialSquares = {
                {0, 3}, {0, 11}, {2, 6}, {2, 8},
                {3, 0}, {3, 7}, {3, 14}, {6, 2},
                {6, 6}, {6, 8}, {6, 12}, {7, 3},
                {7, 11}, {8, 2}, {8, 6}, {8, 8},
                {8, 12}, {11, 0}, {11, 7}, {11, 14},
                {12, 6}, {12, 8}, {14, 3}, {14, 11}
        };

        // Initialize the board with regular squares
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 15; j++) {
                squares[i][j] = new Square(null, "regular");
            }
        }

        // Assign special squares
        for (int[] specialSquare : specialSquares) {
            int row = specialSquare[0];
            int col = specialSquare[1];
            assignSpecialSquare(row, col);
        }

    }

    // Function to assign special squares
    private void assignSpecialSquare(int row, int col) {
        if ((row == 0 || row == 14) && (col == 0 || col == 7 || col == 14)) {
            squares[row][col] = new Square(null, "TW"); // triple word score
        } else if ((row == 1 || row == 13) && (col == 1 || col == 5 || col == 9 || col == 13)) {
            squares[row][col] = new Square(null, "DW"); // double word score
        } else if ((row == 0 || row == 7 || row == 14) && (col == 3 || col == 11)) {
            squares[row][col] = new Square(null, "DL"); // double letter score
        } else if ((row == 2 || row == 12) && (col == 6 || col == 8)) {
            squares[row][col] = new Square(null, "TL"); // triple letter score
        }
    }

    public boolean chackLegalVertical(Word w)
    {
        int row = w.getRow(), col = w.getCol();

        if(row + w.tiles.length > 15)
            return false;

        // first player
        if(this.tiles[7][7] == null) {
            return row == 7 || col == 7;
        }
        boolean flag = false;

        for(int i = row; i < w.tiles.length; i++)
        {
            if(this.tiles[i][col] != w.tiles[i] &&
                    this.tiles[i][col] != null)
            {
                return false;
            }
            if(!flag && this.tiles[i][col] == w.tiles[i])
                flag = true;
        }

        if(flag)
            return true;

        if(row - 1 > 0 && this.tiles[row - 1][col] != null)
            return true;

        if(row + 1 < 15 && this.tiles[row + 1][col] != null)
            return true;

        for(int i = 0; i < w.tiles.length; i++)
        {
            if(col - 1 > 0 && this.tiles[i][col - 1] != null)
                return true;

            if(col + 1 < 15 && this.tiles[i][col + 1] != null)
                return true;
        }
        return false;
    }

    public boolean chackLegalHorizontal(Word w)
    {
        int row = w.getRow(), col = w.getCol();

        if(col + w.tiles.length > 15)
            return false;

        // first player
        if(this.tiles[7][7] == null) {
            return row == 7 || col == 7;
        }

        boolean flag = false;

        for(int i = col; i < w.tiles.length; i++)
        {
            if(this.tiles[row][i] != w.tiles[i] && this.tiles[row][i] != null)
            {
                return false;
            }
            if(!flag && this.tiles[row][i] == w.tiles[i])
                flag = true;
        }
        if(flag)
            return true;

        if(col - 1 > 0 && this.tiles[row][col - 1] != null)
            return true;

        if(col + 1 < 15 && this.tiles[row][col + 1] != null)
            return true;

        for(int i = col; i < w.tiles.length; i++)
        {
            if(row - 1 > 0 && this.tiles[row - 1][i] != null)
                return true;

            if(row + 1 < 15 && this.tiles[row + 1][i] != null)
                return true;
        }

        return false;
    }

    public boolean boardLegal(Word w)
    {
        if(w.vertical)
            return chackLegalVertical(w);

        return chackLegalHorizontal(w);
    }

    public boolean dictionaryLegal(Word w)
    {
        return true;
    }

    public int findFirstIndexOfWordFromLeft(int row, int col, Tile[][] tilesCopy)
    {
        int i = col;

        while(tilesCopy[row][i] != null && i >= 0)
            i--;

        return col - i;
    }

    public void WhatVerticalWordAdded(Word w, Tile[][] tilesCopy)
    {
        int row = w.getRow(), col = w.getCol();
        int startCol = findFirstIndexOfWordFromLeft(row, col, tilesCopy);
        int j = startCol;

        boolean isDiff = false;

        while(tilesCopy[row][j] != null) {
            if (tilesCopy[row][j] != this.tiles[row][j])
                isDiff = true;
            j++;
        }

        if(!isDiff)
            return;

        int newWordSize = j - startCol;
        Tile[] newWordTiles = new Tile[newWordSize];
        int i = startCol;

        for(int k = 0; k < newWordSize; k++) {
            newWordTiles[k] = tilesCopy[row][i];
            i++;
        }

        Word newWord = new Word(newWordTiles, col, startCol, true);
        if(dictionaryLegal(newWord))
            this.words.add(newWord);

    }

    public int findFirstIndexOfWordAbove(int row, int col, Tile[][] tilesCopy)
    {
        int i = row;

        while(tilesCopy[i][col] != null && i >= 0)
            i--;

        return row - i;
    }


    public void checkIfWordExistedHorizontal(int row, Tile[][] tilesCopy, int col){
        int startRow = findFirstIndexOfWordAbove(row, col, tilesCopy); // will indicate where our possible new word will start
        int j = startRow;

        boolean isDiff = false;

        while(tilesCopy[j][col] != null) {
            if (tilesCopy[j][col] != this.tiles[j][col])
                isDiff = true;
            j++;
        }

        if(!isDiff) // means that there isn't a new word
            return;

        int newWordSize = j - startRow;
        Tile[] newWordTiles = new Tile[newWordSize];
        int i = startRow;

        for(int k = 0; k < newWordSize; k++) {
            newWordTiles[k] = tilesCopy[i][col];
            i++;
        }

        Word newWord = new Word(newWordTiles, col, startRow, true);
        if(dictionaryLegal(newWord))
            this.words.add(newWord);
    }

    public void WhatHorizontalWordAdded(Word w, Tile[][] tilesCopy)
    {
        for(int i = w.getCol(); i < w.tiles.length; i++)
        {
            if(tilesCopy[w.getRow() - 1][i] != null || tilesCopy[w.getRow() + 1][i] == null)
                checkIfWordExistedHorizontal(w.getRow(), tilesCopy, i);
        }
        this.words.add(w);
    }

    public void placeVertical(Word w, Tile[][] tilesCopy)
    {
        int j = 0;
        for(int i = w.getRow(); i < w.tiles.length; i++) {
            tilesCopy[i][w.getCol()] = w.tiles[j];
            j++;
        }
    }

    public void placeHorizontal(Word w, Tile[][] tilesCopy)
    {
        int j = 0;
        for(int i = w.getCol(); i < w.tiles.length; i++)
        {
            tilesCopy[w.getRow()][j]= w.tiles[j];
            j++;
        }
    }

    public void createTilesCopy(Tile[][] tiles2copy)
    {
        for(int i = 0; i < this.tiles.length; i++)
        {
            for(int j = 0; j < this.tiles.length; j++)
            {
                tiles2copy[i][j] = this.tiles[i][j];
            }
        }
    }


    public ArrayList<Word> getWords(Word w)
    {
        Tile[][] tilesCopy = new Tile[15][15]; // a copy of the matrix tiles, which we will try to make changes on
        createTilesCopy(tilesCopy);

        if(w.vertical){
            placeVertical(w, tilesCopy); // places the word tile on the tiles copy matrix
            WhatVerticalWordAdded(w, tilesCopy);
        }

        else {
            placeHorizontal(w, tilesCopy);
            WhatHorizontalWordAdded(w, tilesCopy);
        }

        return words;
    }

    public void checkScore(Square square, int score)
    {
        if(Objects.equals(square, "DL"))
            score += 2 * square.t.score;

        if(Objects.equals(square.color, "TL"))
            score += 3 * square.t.score;

        if(Objects.equals(square.color, "regular"))
            score += square.t.score;
    }

    public int getScore(Word w)
    {
        int score = 0;
        boolean tripleWordScore = false, doubleWordScore = false;

        if(w.vertical) {
            for(int i = w.getRow(); i < w.tiles.length; i++)
            {
                checkScore(squares[i][w.getCol()], score);

                if(Objects.equals(squares[i][w.getCol()].color, "TW"))
                    tripleWordScore = true;

                if(Objects.equals(squares[i][w.getCol()].color, "DW"))
                    doubleWordScore = true;
            }
        }

        else {
            for(int i = w.getCol(); i < w.tiles.length; i++)
            {
                checkScore(squares[w.getRow()][i], score);

                if(Objects.equals(squares[w.getRow()][i].color, "TW"))
                    tripleWordScore = true;

                if(Objects.equals(squares[w.getRow()][i].color, "DW"))
                    doubleWordScore = true;
            }

        }


        if(tripleWordScore)
            score *= 3;

        if(doubleWordScore)
            score *= 2;

        return score;
    }

}

class Square {
    public Tile t;
    public String color;

    public Square(Tile t, String color) {
        this.t = t;
        this.color = color;
    }
}