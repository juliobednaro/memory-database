import org.junit.jupiter.api.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

class DBTest {
    DB db;


    @BeforeEach
    void setUp() {
        db = new DB();
    }

    @AfterEach
    void tearDown() {
        db = null;
    }

    @Nested
    class settingValueTests {
        @Test
        void setValueWithoutTransaction() {
            db.set("a", "5");
            assertEquals("5", db.get("a"));
            assertEquals("5", db.database.get("a"));
            assertNull(db.changes.get("a"));
        }

        @Test
        void setValueWithTransaction() {
            db.begin();
            db.set("a", "5");
            assertEquals("5", db.get("a"));
            assertNull(db.database.get("a"));
            assertEquals("5", db.changes.get("a"));
        }

        @Test
        void setValueBeforeTransaction() {
            db.set("a", "5");
            db.begin();
            assertEquals("5", db.get("a"));
            assertEquals("5", db.database.get("a"));
            assertEquals("5", db.changes.get("a"));
        }

        @Test
        void setValueAfterTransaction() {
            db.begin();
            db.set("a", "5");
            db.commit();
            db.set("a", "6");
            assertEquals("6", db.get("a"));
            assertEquals("6", db.database.get("a"));
            assertNull(db.changes.get("a"));
        }
    }

    @Nested
    class gettingValueTests {
        @Test
        void getValueWithoutTransaction() {
            db.set("getWithout", "a");
            assertEquals("a", db.database.get("getWithout"));
            assertNull(db.database.get("noExist"));
        }

        @Test
        void getValueWithTransaction() {
            db.begin();
            db.set("getWith", "b");
            assertEquals("b", db.changes.get("getWith"));
            assertNull(db.database.get("noExist"));
        }
    }

    @Nested
    class deletingValueTests{
        @Test
        void del() {

        }

    }

    @Nested
    class countingValuesTests{
        @Test
        void countValuesAfterCommit() {
            db.begin();
            db.set("a", "5");
            db.set("b", "6");
            db.set("c", "5");
            db.commit();
            long x = db.count("5");
            assertEquals(2, x);
        }
        @Test
        void countSetValuesAfterCommit() {
            db.begin();
            db.set("a", "5");
            db.set("b", "6");
            db.set("c", "5");
            db.commit();
            db.set("d", "5");
            long x = db.count("5");
            assertEquals(3, x);
        }

        @Test
        void countValuesWithoutTransaction() {
            db.set("a", "5");
            db.set("b", "6");
            db.set("c", "5");
            db.set("d", "5");
            long x = db.count("5");
            assertEquals(3, x);
        }
    }

    @Nested
    class committingTransactionsTests {
        boolean isDatabaseEqualChanges() {
            boolean isSame = true;
            for (String key : db.changes.keySet()) {
                String value = db.changes.get(key);
                if (!db.database.get(key).equals(value)) {
                    isSame = false;
                    break;
                }
            }
            return isSame;
        }
        @Test
        void commitWithTransaction() {
            db.begin();
            db.set("a", "5");
            db.commit();
            boolean isSame = isDatabaseEqualChanges();
            assertTrue(isSame);
        }

        @Test
        void commitWithoutTransaction() {
            db.set("a", "5");
            db.commit();
            boolean isSame = isDatabaseEqualChanges();
            assertTrue(isSame);
        }
    }

    @Nested
    class rollbackTests{
        private static final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        private static final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
        private static final PrintStream originalErr = System.err;
        private static final PrintStream originalOut = System.out;
        @BeforeAll
        static void setUpStreams() {
            System.setOut(new PrintStream(outContent));
            System.setErr(new PrintStream(errContent));
        }

        @AfterAll
        static void restoreStreams() {
            System.setOut(originalOut);
            System.setErr(originalErr);
        }

        @Test
        void rollbackWithoutTransaction() {
            db.set("a", "5");
            db.rollback();
            assertEquals("Nothing to rollback", outContent.toString().trim());
        }

        @Test
        void rollbackWithOneTransaction() {
            db.set("a", "7");
            db.begin();
            db.set("a", "5");
            db.rollback();
            assertNull(outContent.toString().trim());
        }

        @Test
        void rollbackWithTwoTransactions() {
            db.set("a", "7");
            db.begin();
            db.set("a", "5");
            db.begin();
            db.set("a", "5");
            db.rollback();

        }

        @Test
        void onlyRollback() {
            db.rollback();
        }

    }
}