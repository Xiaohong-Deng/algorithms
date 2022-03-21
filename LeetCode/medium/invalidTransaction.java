package LeetCode.medium;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class invalidTransaction {
    public static class Transaction {
        private String name;
        private int time;
        private int amount;
        private String city;

        public Transaction(String transaction) {
            String[] TransactionArray = transaction.split(",");
            this.name = TransactionArray[0];
            this.time = Integer.parseInt(TransactionArray[1]);
            this.amount = Integer.parseInt(TransactionArray[2]);
            this.city = TransactionArray[3];
        }

        public boolean invalidAmount() {
            return this.amount > 1000;
        }

        public boolean invalidCityTime(Transaction t) {
            return !this.city.equals(t.city) && Math.abs(this.time - t.time) <= 60;
        }
    }
    public List<String> invalidTransactions(String[] transactions) {
        List<String> invalid = new ArrayList<>();
        Map<String, List<Transaction>> nameCities = new HashMap<>();

        // build the map first because transactions may out of order we are not sure if later transactions can invalidate the current one
        for (String s : transactions) {
            Transaction t = new Transaction(s);
            if (nameCities.containsKey(t.name)) {
                nameCities.get(t.name).add(t);
            } else {
                List<Transaction> tl = new ArrayList<>();
                tl.add(t);
                nameCities.put(t.name, tl);
            }
        }

        for (String s : transactions) {
            Transaction t = new Transaction(s);
            List<Transaction> tl = nameCities.get(t.name);
            if (isInvalid(t, tl)) {
                invalid.add(s);
            }
        }
        return invalid;
    }

    private boolean isInvalid(Transaction t, List<Transaction> tl) {
        if (t.invalidAmount()) {
            return true;
        }

        for (Transaction tran : tl) {
            if (t.invalidCityTime(tran)) {
                return true;
            }
        }

        return false;
    }

    public static void main(String[] args) {
        
    }
}
