package LeetCode.hard;

import java.util.Map;
import java.util.TreeMap;

public class numAtoms {
    private int i;
    public String numberAtoms(String formula) {
        StringBuilder ans = new StringBuilder();
        Map<String, Integer> elementCount = parse(formula);

        i = 0;
        for (String element : elementCount.keySet()) {
            ans.append(element);
            int count = elementCount.get(element);
            if (count != 1)
                ans.append(String.valueOf(count));
        }

        return ans.toString();
    }

    private Map<String, Integer> parse(String formula) {
        // if you have nested formula you can extract the formula inside ()
        // or you use an index to indicate the starting point of the nested formula
        // then you will deal with something like ABC)2
        Map<String, Integer> eleCount = new TreeMap<>(); // keySet in TreeMap is ordered
        int start, count;
        int fLen = formula.length();
        while (i < fLen && formula.charAt(i) != ')') {
            if (formula.charAt(i) == '(') {
                i++;
                for (Map.Entry<String, Integer> entry : parse(formula).entrySet()) {
                    eleCount.put(entry.getKey(), eleCount.getOrDefault(entry.getKey(), 0) + entry.getValue());
                }
            } else {
                start = i++;
                while (Character.isLowerCase(formula.charAt(i))) i++;
                String element = formula.substring(start, i);
                start = i;
                while (Character.isDigit(formula.charAt(i))) i++;
                // at this point i points at ) or uppercase letter or out of bounds
                if (start == i) {
                    count = 1;
                } else {
                    count = Integer.parseInt(formula.substring(start, i));
                }
                eleCount.put(element, count);
            }
        }
        // next step is dealing with something like )23, uppercase is dealt with by the previous while loop
        start = ++i;
        while (i < fLen && Character.isDigit(formula.charAt(i))) i++;
        if (start < i) {
            count = Integer.parseInt(formula.substring(start, i));
            for (String key : eleCount.keySet()) {
                eleCount.put(key, eleCount.get(key) * count);
            }
        }
        return eleCount;
    }

    public static void main(String[] args) {
        String formula = "K4(ON(SO3)2)2";
        numAtoms test = new numAtoms();
        System.out.println(test.numberAtoms(formula));
    }
}
