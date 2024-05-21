package test;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.BitSet;

public class BloomFilter {
    String[] hashFuncNames;
    BitSet bitArr;

    public BloomFilter(int bitsArrSize, String ... algNames) {
        this.hashFuncNames = new String[algNames.length];
        System.arraycopy(algNames, 0, this.hashFuncNames, 0, algNames.length);
        bitArr = new BitSet(bitsArrSize);
    }

    public void add(String strToAdd) {
        for (String hashFuncName : hashFuncNames) {
            try {
                MessageDigest md = MessageDigest.getInstance(hashFuncName);
                byte[] hashBytes = md.digest(strToAdd.getBytes());
                BigInteger value = new BigInteger(hashBytes);
                int index = Math.abs(value.intValue()) % bitArr.size();
                bitArr.set(index);
            }

            catch (NoSuchAlgorithmException e) {
                System.err.println("Algorithm not available: " + hashFuncName);
            }
        }
    }

    public boolean contains(String str)
    {
        for (String hashFuncName : hashFuncNames) {
            try {
                MessageDigest md = MessageDigest.getInstance(hashFuncName);
                byte[] hashBytes = md.digest(str.getBytes());
                BigInteger value = new BigInteger(hashBytes);
                int index = Math.abs(value.intValue()) % bitArr.size();
                if(!bitArr.get(index))
                    return false;
            }

            catch (NoSuchAlgorithmException e) {
                System.err.println("Algorithm not available: " + hashFuncName);
            }
        }

        return true;
    }

    @Override
    public String toString()
    {
        StringBuilder str = new StringBuilder();
        for(int i = 0; i < bitArr.length(); i++)
        {
            if(bitArr.get(i))
                str.append('1');
            else
                str.append('0');

        }
        return str.toString();
    }
}
