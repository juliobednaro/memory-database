public interface DataBase {
    void set(String key, String value);
    String get(String key);
    void del(String key);
    long count(String value);
    void begin();
    void commit();
    void rollback();
}
