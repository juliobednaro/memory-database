import org.junit.jupiter.api.*;

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
        void count() {

        }
    }

    @Nested
    class committingTransactionsTests {
        @Test
        void commit() {
            boolean isSame = true;
            db.begin();
            db.set("a", "5");
            db.commit();

            for (String key : db.changes.keySet()) {
                String value = db.changes.get(key);
                if (!db.database.get(key).equals(value)) {
                    isSame = false;
                    break;
                }
            }
            assertTrue(isSame);
        }
    }

    @Nested
    class rollbackTests{
        @Test
        void rollback() {

        }
    }
}