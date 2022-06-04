import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class DB implements DataBase {
    HashMap<String, String> database;
    HashMap<String, String> changes;
    ArrayList<String> currentTransaction;
    ArrayList<ArrayList<String>> allTransactions;
    Map<Integer, Integer> valuesMap;

    public DB() {
        database = new HashMap<>();
        changes = new HashMap<>();
        allTransactions = new ArrayList<>();
    }

    private void executeCommand(String command, HashMap<String, String> db) {
        String[] args = command.split("\\s");
        String operation = args[0];
        String key = args[1];
        if (operation.equals("SET")) {
            String value = args[2];
            db.put(key, value);
        }
        else {
            db.remove(key);
        }
    }

    private void updateTransactions() {
        changes = new HashMap<>(database);
        if (allTransactions.size() > 1)
            for (ArrayList<String> transaction : allTransactions)
                for (String command : transaction)
                    this.executeCommand(command, database);
        else
            for (String command : currentTransaction)
                this.executeCommand(command, database);
    }

    private void resetCurrentTransaction() {
        if (allTransactions.size() > 1)
            currentTransaction = allTransactions.get(allTransactions.size()-1);
        else
            currentTransaction = null;
    }

    @Override
    public void set(String key, String value) {
        String command = String.format("SET %s %s", key, value);
        if (currentTransaction == null)
            executeCommand(command, database);
        else {
            executeCommand(command, changes);
            currentTransaction.add(command);
        }
    }

    @Override
    public String get(String key) {
        if (currentTransaction == null)
            return database.get(key);
        else
            return changes.get(key);
    }

    @Override
    public void del(String key) {
        String command = String.format("DEL %s", key);
        if (currentTransaction == null)
            executeCommand(command, database);
        else {
            executeCommand(command, changes);
            currentTransaction.add(command);
        }
    }

    @Override
    public long count(String value) {
        HashMap<String, String> db;
        if (currentTransaction == null)
            db = database;
        else
            db = changes;
        return db.values().stream().filter(val -> val.equals(value)).count();
    }

    @Override
    public void begin() {
        if (currentTransaction == null)
            changes = new HashMap<>(database);
        else
            allTransactions.add(currentTransaction);
        currentTransaction = new ArrayList<>();
    }

    @Override
    public void commit() {
        if (currentTransaction == null)
            return;
        if (allTransactions.size() > 1)
            for (ArrayList<String> transaction : allTransactions)
                for (String command : transaction)
                    this.executeCommand(command, database);
        else
            for (String command : currentTransaction)
                this.executeCommand(command, database);
        changes = new HashMap<>();
        allTransactions = new ArrayList<>();
        resetCurrentTransaction();
    }

    @Override
    public void rollback() {
        if (allTransactions.size() >= 1) {
            allTransactions.remove(allTransactions.size()-1);
            resetCurrentTransaction();
            updateTransactions();
        }
        else if (currentTransaction != null) {
            currentTransaction = null;

        }
        else {
            System.out.println("Nothing to rollback");
        }
    }
}
