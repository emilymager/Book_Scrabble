package test;


import java.util.ArrayList;
import java.util.Objects;

public class Board {
    public Square[][] squares;
    public ArrayList<Word> words;
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

        if(row + w.tiles.length - 1 > 14)
            return false;

        if(squares[7][7].t == null)
            return true;

        boolean flag = false;

        for(int i = row; i < w.tiles.length + row - 1; i++)
        {
            if(this.squares[i][col].t != w.tiles[i] && this.squares[i][col].t != null)
            {
                return false;
            }
            if(!flag && this.squares[i][col].t == w.tiles[i])
                flag = true;
        }

        if(flag)
            return true;

        for(int i = 0; i < w.tiles.length; i++)
        {
            if(col - 1 > 0 && this.squares[i][col - 1].t != null)
                return true;


            if(col + 1 < 15 && this.squares[i][col + 1].t != null)
                return true;
        }
        return false;
    }


    public boolean chackLegalHorizontal(Word w)
    {
        int row = w.getRow(), col = w.getCol();

        if(col + w.tiles.length - 1 > 14)
            return false;

        if(squares[7][7].t == null)
            return true;

        boolean flag = false;

        for(int i = col; i < w.tiles.length + col - 1; i++)
        {
            if(this.squares[row][i].t != w.tiles[i] && this.squares[row][i].t != null)
            {
                return false;
            }
            if(!flag && this.squares[row][i].t == w.tiles[i])
                flag = true;
        }
        if(flag)
            return true;

        for(int i = col; i < w.tiles.length; i++)
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
            return chackLegalVertical(w);

        return chackLegalHorizontal(w);
    }

    public boolean dictionaryLegal(Word w)
    {
        return true;
    }

   public ArrayList<Word> getWords(Word w)
    {
        if(w.vertical){

        }

        else {
        }

        return words;
    }

    public int checkScore(Square square, int score)
    {
        if(Objects.equals(square, "DL"))
            score += 2 * square.t.score;

        if(Objects.equals(square.color, "TL"))
            score += 3 * square.t.score;

        if(Objects.equals(square.color, "regular"))
            score += square.t.score;

        return score;
    }

    public int getScore(Word w)
    {
        int score = 0;
        int tripleWordScore = 0, doubleWordScore = 0;

        if(w.vertical) {
            for(int i = w.getRow(); i < w.tiles.length + w.getRow() - 1; i++)
            {
                score = checkScore(squares[i][w.getCol()], score);

                if(Objects.equals(squares[i][w.getCol()].color, "TW"))
                    tripleWordScore++;

                if(Objects.equals(squares[i][w.getCol()].color, "DW"))
                    doubleWordScore++;
            }
        }

        else {
            for(int i = w.getCol(); i < w.tiles.length + w.getCol() - 1; i++)
            {
                score = checkScore(squares[w.getRow()][i], score);

                if(Objects.equals(squares[w.getRow()][i].color, "TW"))
                    tripleWordScore++;

                if(Objects.equals(squares[w.getRow()][i].color, "DW"))
                    doubleWordScore++;
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
}

class Square {
    public Tile t;
    public String color;

    public Square(Tile t, String color) {
        this.t = t;
        this.color = color;
    }
}