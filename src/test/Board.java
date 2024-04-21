package test;


import java.util.ArrayList;

public class Board {
    public Square[][] squares; // ?
    public ArrayList<Word>[] words;
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
        this.words = new ArrayList[0];
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
            if(row != 7 && col != 7)
                return false;

            return true;
        }

        for(int i = 0; i < w.tiles.length; i++)
        {
            if(this.tiles[i][col] != null)
                return false;
        }

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
            if(row != 7 && col != 7)
                return false;
            return true;
        }

        for(int i = col; i < w.tiles.length; i++)
        {
            if(this.tiles[row][i] != null)
                return false;
        }

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

}

class Square {
    public Tile t;
    public String color;

    public Square(Tile t, String color) {
        this.t = t;
        this.color = color;
    }
}
