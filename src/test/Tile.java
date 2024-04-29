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
        private static Bag bag;
        public int[] lettersFreq = {9, 2, 2, 4, 12, 2, 3, 2, 9, 1, 1, 4, 2, 6, 8, 2, 1, 6, 4, 6, 4, 2, 2, 1, 2, 1};
        public final int[] freqFinalArr = {9, 2, 2, 4, 12, 2, 3, 2, 9, 1, 1, 4, 2, 6, 8, 2, 1, 6, 4, 6, 4, 2, 2, 1, 2, 1};
        public Tile[] BagTiles = new Tile[26];

        private Bag() {
            this.BagTiles = new Tile[]{
                    new Tile(1, 'A'), new Tile(3, 'B'), new Tile(3, 'C'), new Tile(2, 'D'), new Tile(1, 'E'),
                    new Tile(4, 'F'), new Tile(2, 'G'), new Tile(4, 'H'), new Tile(1, 'I'), new Tile(8, 'J'),
                    new Tile(5, 'K'), new Tile(1, 'L'), new Tile(3, 'M'), new Tile(1, 'N'), new Tile(1, 'O'),
                    new Tile(3, 'P'), new Tile(10, 'Q'), new Tile(1, 'R'), new Tile(1, 'S'), new Tile(1, 'T'),
                    new Tile(1, 'U'), new Tile(4, 'V'), new Tile(4, 'W'), new Tile(8, 'X'), new Tile(4, 'Y'),
                    new Tile(10, 'Z')
            };
        }


        public Tile getRand()
        {
            if(this.size() == 0)
                return null;

            int randNum = (int)(Math.random() * (26));

            while(this.lettersFreq[randNum] == 0)
                randNum = (int)(Math.random() * (26));

            this.lettersFreq[randNum]--;
            return this.BagTiles[randNum];
        }

        public int calcLetterAsciiVal(char l)
        {
            return (int) l - (int)'A';
        }

        public Tile getTile(char l)
        {
            int index = calcLetterAsciiVal(l);
            if(index < 0 || index >= 26)
                return null;

            if(this.lettersFreq[index] > 0) {
                this.lettersFreq[index]--;
                return this.BagTiles[index];
            }

            return null;
        }

        public void put(Tile t)
        {
            int index = calcLetterAsciiVal(t.letter);
            if(this.lettersFreq[index] < this.freqFinalArr[index])
                this.lettersFreq[index]++;
        }

        public int size()
        {
            int size = 0;

            for(int i = 0; i < 26; i++)
                size += lettersFreq[i];

            return size;
        }

        public static Bag getBag()
        {
            if(bag == null)
                bag = new Bag();

            return bag;
        }

        public int[] getQuantities()
        {
            int[] cloneOfLettersFreq = new int[26];
            for(int i = 0; i < 26; i++)
            {
                cloneOfLettersFreq[i] = lettersFreq[i];
            }
            return cloneOfLettersFreq;
        }
    }
}

// :)