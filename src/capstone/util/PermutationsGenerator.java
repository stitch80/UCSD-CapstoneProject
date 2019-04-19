package capstone.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PermutationsGenerator {

    public static ArrayList<Integer> nextPermutation(ArrayList<Integer> currPermutation) {
        if (currPermutation.size() <= 1) {
            return new ArrayList<>();
        }
        int startIdx = currPermutation.size() - 1;
        int idxK = -1;
        int idxL = -1;
        for (int i = startIdx; i > 0; i--) {
            if (currPermutation.get(i) > currPermutation.get(i - 1)) {
                idxK = i - 1;
                break;
            }
        }
        if (idxK == -1) {
            return new ArrayList<>();
        }
        else {
            for (int j = startIdx; j >= 0; j--) {
                if (currPermutation.get(j) > currPermutation.get(idxK)) {
                    idxL = j;
                    break;
                }
            }
        }
        Collections.swap(currPermutation, idxK, idxL);
        List<Integer> part1 = currPermutation.subList(0, idxK + 1);
        List<Integer> part2 = currPermutation.subList(idxK + 1, currPermutation.size());
        Collections.sort(part2);
        ArrayList<Integer> nextPermutation = new ArrayList<Integer>();
        nextPermutation.addAll(part1);
        nextPermutation.addAll(part2);
        return nextPermutation;
    }

    public static ArrayList<Integer> getBinaryPermutation(int num, int power) {
        char[] chars = Integer.toBinaryString(num).toCharArray();
        int diff = power - chars.length;
        ArrayList<Integer> nextBinaryPermutation = new ArrayList<>();
        if (diff < 0) {
            return nextBinaryPermutation;
        }
        if (diff == 0) {
            for (int i = 0; i < power; i++) {
                nextBinaryPermutation.add(i, Character.digit(chars[i], 10));
            }
        }
        else {
            for (int i = 0; i < power; i++) {
                if (i < diff) {
                    nextBinaryPermutation.add(i, 0);
                }
                else {
                    nextBinaryPermutation.add(i, Character.digit(chars[i - diff], 10));
                }
            }
        }
        return nextBinaryPermutation;
    }
}
