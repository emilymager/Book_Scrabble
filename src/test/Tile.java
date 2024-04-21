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
            char l='A';
            int j=0;

            for(int i = 0; i < 26; i++)
            {
                this.BagTiles[i]= new Tile(this.lettersFreq[j],l);
                j++;
                l++;
            }
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