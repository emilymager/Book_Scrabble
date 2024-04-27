package test;


import java.util.ArrayList;
import java.util.Objects;

public class Board {
    public Square[][] squares;
    public ArrayList<Word> words;
    private static Board board;
    boolean starWasUsed = false;

    public static Board getBoard()
    {
        if(board == null)
            board = new Board();

        return board;
    }

    public Board() {
        this.squares = new Square[15][15];
        String DW = "DW"; // double word score
        String TW = "TW"; // triple word score
        String TL = "TL"; // triple letter score
        String DL = "DL"; // double letter score
        String reg = "reg"; // a regular square

        // Define the layout of the special squares
        String[][] layout = {
                {TW, reg, reg, DL, reg, reg, reg, TW, reg, reg, reg, DL, reg, reg, TW},
                {reg, DW, reg, reg, reg, TL, reg, reg, reg, TL, reg, reg, reg, DW, reg},
                {reg, reg, DW, reg, reg, reg, DL, reg, DL, reg, reg, reg, DW, reg, reg},
                {DL, reg, reg, DW, reg, reg, reg, DL, reg, reg, reg, DW, reg, reg, DL},
                {reg, reg, reg, reg, DW, reg, reg, reg, reg, reg, DW, reg, reg, reg, reg},
                {reg, TL, reg, reg, reg, TL, reg, reg, reg, TL, reg, reg, reg, TL, reg},
                {reg, reg, DL, reg, reg, reg, DL, reg, DL, reg, reg, reg, DL, reg, reg},
                {TW, reg, reg, DL, reg, reg, reg, DW, reg, reg, reg, DL, reg, reg, TW},
                {reg, reg, DL, reg, reg, reg, DL, reg, DL, reg, reg, reg, DL, reg, reg},
                {reg, TL, reg, reg, reg, TL, reg, reg, reg, TL, reg, reg, reg, TL, reg},
                {reg, reg, reg, reg, DW, reg, reg, reg, reg, reg, DW, reg, reg, reg, reg},
                {DL, reg, reg, DW, reg, reg, reg, DL, reg, reg, reg, DW, reg, reg, DL},
                {reg, reg, DW, reg, reg, reg, DL, reg, DL, reg, reg, reg, DW, reg, reg},
                {reg, DW, reg, reg, reg, TL, reg, reg, reg, TL, reg, reg, reg, DW, reg},
                {TW, reg, reg, DL, reg, reg, reg, TW, reg, reg, reg, DL, reg, reg, TW}
        };

        // Initialize each square based on the layout
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 15; j++) {
                squares[i][j] = new Square(null, layout[i][j]);
            }
        }

        this.words = new ArrayList<>();
    }

    public boolean checkLegalVertical(Word w)
    {
        int row = w.getRow(), col = w.getCol();

        if(row + w.tiles.length - 1 > 14)
            return false;

        if(squares[7][7].t == null)
            return true;

        if(row - 1 >= 0 && this.squares[row - 1][col].t != null)
            return true;

        if(row + 1 <= 14 && this.squares[row + 1][col].t != null)
            return true;

        // add case where I check if I continued a word or if I contain an existing letter

        int j = 0;
        for(int i = row; i < w.tiles.length + row - 1; i++)
        {
            if(w.tiles[j] == null)
                return this.squares[i][col].t != null;

            j++;
        }

        for(int i = row; i < row + w.tiles.length; i++)
        {
            if(col - 1 > 0 && this.squares[i][col - 1].t != null)
                return true;

            if(col + 1 < 15 && this.squares[i][col + 1].t != null)
                return true;
        }
        return false;
    }


    public boolean checkLegalHorizontal(Word w)
    {
        int row = w.getRow(), col = w.getCol();

        if(col + w.tiles.length - 1 > 14)
            return false;

        if(squares[7][7].t == null)
            return true;

        if(col - 1 >= 0 && this.squares[row][col - 1].t != null)
            return true;

        if(col + 1 <= 14 && this.squares[row][col + 1].t != null)
            return true;

        int j = 0;
        for(int i = col; i < w.tiles.length + col; i++)
        {
            if(w.tiles[j] == null)
                return this.squares[row][i].t != null;

            j++;
        }

        for(int i = col; i < col + w.tiles.length; i++)
        {
            if(row - 1 > 0 && this.squares[row - 1][i].t != null)
                return true;

            if(row + 1 < 15 && this.squares[row + 1][i].t != null)
                return true;
        }

        return false;
    }

    public boolean containsMiddle(int length, int row, int col, boolean vertical){
        if(vertical) {
            if(col != 7)
                return false;
            if(row <= 7 && row + length - 1 >= 7 && row + length - 1 <= 14)
                return true;
        }
        else {
            if (row != 7)
                return false;

            if (col <= 7 && col + length - 1 >= 7 && col + length - 1 <= 14)
                return true;
        }
        return false;
    }

    public boolean boardLegal(Word w)
    {
        if(w.getRow() < 0 || w.getRow() > 14 || w.getCol() < 0 || w.getCol() > 14)
            return false;

        // first player
        if(this.squares[7][7].t == null && !containsMiddle(w.tiles.length, w.getRow(), w.getCol(), w.vertical) )
            return false;

        if(w.vertical)
            return checkLegalVertical(w);

        return checkLegalHorizontal(w);
    }

    public boolean dictionaryLegal(Word w)
    {
        return true;
    }

    public void getNewVerticalWord(int row, int col, Tile t)
    {
        int i = row; // the initial index of our new word
        int j = row; // // the final index of our new word

        while(i - 1 >= 0 && this.squares[i - 1][col].t != null)
            i--;

        int startRow = i;

        while(j + 1 <= 14 && this.squares[j + 1][col].t != null)
            j++;

        Tile[] newWordTiles = new Tile[j - i + 1];

        int k = 0;
        for (; i <= j; i++)
        {
            if(i == row)
                newWordTiles[k] = t;

            else
                newWordTiles[k] = squares[i][col].t;

            k++;
        }
        Word newWord = new Word(newWordTiles, startRow, col, true);
        if(dictionaryLegal(newWord))
            this.words.add(newWord);
    }

    public void getNewHorizontalWord(int row, int col, Tile t)
    {
        int i = col; // the initial index of our new word
        int j = col; // // the final index of our new word

        while(i - 1 >= 0 && this.squares[row][i - 1].t != null)
            i--;

        int startCol = i;

        while(j + 1 <= 14 && this.squares[row][j + 1].t != null)
            j++;

        Tile[] newWordTiles = new Tile[j - i + 1];

        int k = 0;
        for (; i <= j; i++)
        {
            if(i == col)
                newWordTiles[k] = t;

            else
                newWordTiles[k] = squares[row][i].t;

            k++;
        }
        Word newWord = new Word(newWordTiles, row, startCol, false);
        if(dictionaryLegal(newWord))
            this.words.add(newWord);
    }

    public void getWordsHorizontalWordAdded(int row, int col, int wordLen, Tile[] tiles)
    {
        boolean flag = false;
        int j = 0;
        for(int i = col; i < wordLen + col; i++)
        {
            if(tiles[j] != null) { // ??
                if (row - 1 >= 0 && this.squares[row - 1][i].t != null) {
                    getNewVerticalWord(row, i, tiles[j]);
                    flag = true;
                }

                if (row + 1 <= 14 && this.squares[row + 1][i].t != null && !flag) {
                    getNewVerticalWord(row, i, tiles[j]);
                    flag = true;
                }

                flag = false;
            }
            j++;
        }
    }

    public void getWordsVerticalWordAdded(int row, int col, int wordLen, Tile[] tiles){
        boolean flag = false;
        int j = 0;
        for(int i = row; i < wordLen + row - 1; i++)
        {
            if(tiles[j] != null) { // ??
                if (col - 1 >= 0 && this.squares[i][col - 1].t != null) {
                    getNewHorizontalWord(col, i, squares[i][col].t);
                    flag = true;
                }

                if (col + 1 <= 14 && this.squares[i][col + 1].t != null && !flag) {
                    getNewHorizontalWord(col, i, squares[i][col].t);
                    flag = true;
                }
                flag = false;
            }
            j++;
        }
    }


    public ArrayList<Word> getWords(Word w)
    {
        words.add(w);

        if(w.vertical) getWordsVerticalWordAdded(w.getRow(), w.getCol(), w.tiles.length, w.tiles);

        else getWordsHorizontalWordAdded(w.getRow(), w.getCol(), w.tiles.length, w.tiles);


        return words;
    }

    public int checkScore(Square square, int score)
    {
        if(Objects.equals(square.color, "DL"))
            return score + 2 * square.t.score;

        if(Objects.equals(square.color, "TL"))
            return score + 3 * square.t.score;

        return score + square.t.score;
    }

    public int getScore(Word w)
    {
        int score = 0;
        int tripleWordScore = 0, doubleWordScore = 0;

        if(w.vertical) {
            for(int i = w.getRow(); i < w.tiles.length + w.getRow(); i++)
            {
                score = checkScore(squares[i][w.getCol()], score);

                if(Objects.equals(squares[i][w.getCol()].color, "TW"))
                    tripleWordScore++;

                if(Objects.equals(squares[i][w.getCol()].color, "DW")) {
                    if(starWasUsed && i == 7 && w.getCol() == 7 )
                        continue;

                    if(!starWasUsed)
                        starWasUsed = true;

                    doubleWordScore++;
                }
            }
        }

        else {
            for(int i = w.getCol(); i < w.tiles.length + w.getCol(); i++)
            {
                score = checkScore(squares[w.getRow()][i], score);

                if(Objects.equals(squares[w.getRow()][i].color, "TW"))
                    tripleWordScore++;

                if(Objects.equals(squares[w.getRow()][i].color, "DW")) {
                    if(starWasUsed && i == 7 && w.getRow() == 7)
                        continue;

                    if(!starWasUsed)
                        starWasUsed = true;

                    doubleWordScore++;
                }
            }
        }

        while(tripleWordScore != 0) {
            score *= 3;
            tripleWordScore--;
        }

        while(doubleWordScore != 0) {
            score *= 2;
            doubleWordScore--;
        }

        return score;
    }

    public void placeWord(Word w)
    {
        int j = 0;
        if(w.vertical)
        {
            for(int i = w.getRow(); i < w.tiles.length + w.getRow(); i++) {
                if(squares[i][w.getCol()].t == null)
                    squares[i][w.getCol()].t = w.tiles[j];
                j++;
            }
            return;
        }
        for(int i = w.getCol(); i < w.tiles.length + w.getCol(); i++) {
            if(squares[w.getRow()][i].t == null)
                squares[w.getRow()][i].t = w.tiles[j];
            j++;
        }
    }

    public int getScoreForAllNewWords(int oldSize)
    {
        int i = words.size(), score = 0;
        while(i != oldSize && i >= 0)
        {
            i--;
            score += getScore(words.get(i));
        }
        return score;
    }

    public int tryPlaceWord(Word w)
    {
        int oldSize = words.size();
        if(boardLegal(w))
        {
            getWords(w);
            placeWord(w);
            return getScoreForAllNewWords(oldSize);
        }
        return 0;
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