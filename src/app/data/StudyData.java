package app.data;

/**
 * Created by Vladimir on 16/02/18.
 **/
public interface StudyData {

    String getName();

    void setName(String name);

    /**
     * @return key - значення ключа в таблиці, якщо key = -1 значить ключ не було задано і потрібно автогенерація
     */
    int getKey();

    /**
     * @param key - встановлює значення ключа в таблиці, якщо key = -1 значить ключ не задано
     */
    void setKey(int key);

    /**
     * @return повертає true якщо ключ задано або false, якщо значення ключа не задано
     */
    boolean keyExist();

}
