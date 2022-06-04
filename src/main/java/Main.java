import java.awt.desktop.SystemSleepEvent;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.stream.Stream;

public class Main {
    public static void main(String[] args) {
        DB db = new DB();
        db.set("a", "7");
        db.begin();
        db.set("a", "5");
        db.rollback();
    }
}
