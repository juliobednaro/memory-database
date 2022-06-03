import java.awt.desktop.SystemSleepEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.stream.Stream;

public class Main {
    public static void main(String[] args) {
        // Przekształcić w test case
        DB db = new DB();
        db.begin();
        db.set("a", "5");
        db.set("b", "6");
        db.commit();
        db.set("c", "5");
        long x = db.count("5");
        System.out.println(x);
    }
}
