package app.data;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * Created by Vladimir on 07/06/18.
 **/
public class DegreeDBMetaData {
    public static final ArrayList<String> CREATE_COMMANDS;
    public static final HashSet<String> DATABASE_TABLES;
    public static final ArrayList<String> FILL_CONSTANT_COMMANDS;

    static {
        CREATE_COMMANDS = new ArrayList<>();
        fillCreateCommands();

        DATABASE_TABLES = new HashSet<>();
        fillDatabaseTables();

        FILL_CONSTANT_COMMANDS = new ArrayList<>();
        fillConstantCommands();
    }

    private static void fillConstantCommands() {
        FILL_CONSTANT_COMMANDS.add("INSERT INTO departments (name) VALUE ('Будівельне відділення')");
        FILL_CONSTANT_COMMANDS.add("INSERT INTO departments (name) VALUE ('Відділення економіки-програмування')");
        FILL_CONSTANT_COMMANDS.add("INSERT INTO weeks (mark, name, color, abbreviation) VALUES ('0', 'Навчання', -1, ' ');");
        FILL_CONSTANT_COMMANDS.add("INSERT INTO weeks (mark, name, color, abbreviation) VALUES ('1', 'Канікули', -14575885, 'K');");
        FILL_CONSTANT_COMMANDS.add("INSERT INTO weeks (mark, name, color, abbreviation) VALUES ('2', 'Технологічна практика', -772554, 'Т');");
        FILL_CONSTANT_COMMANDS.add("INSERT INTO weeks (mark, name, color, abbreviation) VALUES ('3', 'Навчальна практика', -1499549, 'Н');");
        FILL_CONSTANT_COMMANDS.add("INSERT INTO weeks (mark, name, color, abbreviation) VALUES ('4', 'Переддипломна практика', -16728876, 'П');");
        FILL_CONSTANT_COMMANDS.add("INSERT INTO weeks (mark, name, color, abbreviation) VALUES ('5', 'Геодезетична практика', -7617718, 'Г');");
        FILL_CONSTANT_COMMANDS.add("INSERT INTO weeks (mark, name, color, abbreviation) VALUES ('6', 'Екзаменаційна сесія', -5317, 'С');");
        FILL_CONSTANT_COMMANDS.add("INSERT INTO weeks (mark, name, color, abbreviation) VALUES ('7', 'Дипломне проектування', -16777216, 'Ш');");
        FILL_CONSTANT_COMMANDS.add("INSERT INTO weeks (mark, name, color, abbreviation) VALUES ('8', 'Державна атестація', -43230, 'Д');");
    }

    private static void fillDatabaseTables() {
        DATABASE_TABLES.add("auditorys");
        DATABASE_TABLES.add("departments");
        DATABASE_TABLES.add("group_load");
        DATABASE_TABLES.add("groups");
        DATABASE_TABLES.add("holidays");
        DATABASE_TABLES.add("lessons");
        DATABASE_TABLES.add("lessons_data");
        DATABASE_TABLES.add("lessons_schedules");
        DATABASE_TABLES.add("load_unit");
        DATABASE_TABLES.add("schedules");
        DATABASE_TABLES.add("schedules_data");
        DATABASE_TABLES.add("semester_load");
        DATABASE_TABLES.add("teachers");
        DATABASE_TABLES.add("weeks");
    }

    private static void fillCreateCommands() {
        CREATE_COMMANDS.add("CREATE TABLE weeks (mark VARCHAR(1) NOT NULL, k INT(11) PRIMARY KEY NOT NULL AUTO_INCREMENT, name VARCHAR(256) NOT NULL, color INT(11) NOT NULL, abbreviation VARCHAR(16) NOT NULL);");
        CREATE_COMMANDS.add("CREATE UNIQUE INDEX weeks_mark_uindex ON weeks (mark);");
        CREATE_COMMANDS.add("CREATE TABLE auditorys (name VARCHAR(256) NOT NULL, k INT(11) PRIMARY KEY NOT NULL AUTO_INCREMENT);");
        CREATE_COMMANDS.add("CREATE TABLE departments (k INT(11) PRIMARY KEY NOT NULL AUTO_INCREMENT, name VARCHAR(256) NOT NULL);");
        CREATE_COMMANDS.add("CREATE TABLE holidays(date DATE NOT NULL, repeats TINYINT(1) DEFAULT '0' NOT NULL, k INT(11) PRIMARY KEY NOT NULL AUTO_INCREMENT);");
        CREATE_COMMANDS.add("CREATE TABLE groups (name VARCHAR(256) NOT NULL, department INT(11) NOT NULL, dateofcreate DATE NOT NULL, comments VARCHAR(256), k INT(11) PRIMARY KEY NOT NULL AUTO_INCREMENT, CONSTRAINT groups_departments_k_fk FOREIGN KEY (department) REFERENCES departments (k) ON DELETE CASCADE ON UPDATE CASCADE);");
        CREATE_COMMANDS.add("CREATE INDEX groups_departments_k_fk ON groups (department);");
        CREATE_COMMANDS.add("CREATE TABLE teachers (name VARCHAR(256) NOT NULL, preferences VARCHAR(256), k INT(11) PRIMARY KEY NOT NULL AUTO_INCREMENT);");
        CREATE_COMMANDS.add("CREATE TABLE lessons (name VARCHAR(256) NOT NULL, k INT(11) PRIMARY KEY NOT NULL AUTO_INCREMENT, auditory INT(11) NOT NULL, CONSTRAINT lessons_ibfk_1 FOREIGN KEY (auditory) REFERENCES auditorys (k) ON DELETE CASCADE ON UPDATE CASCADE);");
        CREATE_COMMANDS.add("CREATE INDEX lessons_ibfk_1 ON lessons (auditory);");
        CREATE_COMMANDS.add("CREATE TABLE schedules (period VARCHAR(256) NOT NULL, date_of_create DATE NOT NULL, coments VARCHAR(256), k INT(11) PRIMARY KEY NOT NULL AUTO_INCREMENT);");
        CREATE_COMMANDS.add("CREATE TABLE schedules_data (schedule INT(11) NOT NULL, groups INT(11) NOT NULL, data VARCHAR(52) NOT NULL, k INT(11) PRIMARY KEY NOT NULL AUTO_INCREMENT, CONSTRAINT schedules_data_ibfk_1 FOREIGN KEY (schedule) REFERENCES schedules (k) ON DELETE CASCADE ON UPDATE CASCADE, CONSTRAINT schedules_data_ibfk_2 FOREIGN KEY (groups) REFERENCES groups (k) ON DELETE CASCADE ON UPDATE CASCADE);");
        CREATE_COMMANDS.add("CREATE INDEX schedules_data_ibfk_1 ON schedules_data (schedule);");
        CREATE_COMMANDS.add("CREATE INDEX schedules_data_ibfk_2 ON schedules_data (groups);");
        CREATE_COMMANDS.add("CREATE TABLE semester_load (k INT(11) PRIMARY KEY NOT NULL AUTO_INCREMENT, period VARCHAR(256) NOT NULL);");
        CREATE_COMMANDS.add("CREATE TABLE lessons_schedules (k INT(11) PRIMARY KEY NOT NULL AUTO_INCREMENT, period VARCHAR(256) NOT NULL, date_of_create DATE NOT NULL, coments VARCHAR(256), semester_load INT(11), CONSTRAINT lessons_schedules_semester_load_k_fk FOREIGN KEY (semester_load) REFERENCES semester_load (k) ON DELETE SET NULL ON UPDATE CASCADE);");
        CREATE_COMMANDS.add("CREATE INDEX lessons_schedules_semester_load_k_fk ON lessons_schedules (semester_load);");
        CREATE_COMMANDS.add("CREATE TABLE lessons_data (k INT(11) PRIMARY KEY NOT NULL AUTO_INCREMENT, lessons_schedule INT(11) NOT NULL, groups INT(11) NOT NULL, pair_number VARCHAR(256) NOT NULL, lesson INT(11) NOT NULL, teacher INT(11) NOT NULL, auditory INT(11) NOT NULL, CONSTRAINT lessons_data_ibfk_1 FOREIGN KEY (lessons_schedule) REFERENCES lessons_schedules (k) ON DELETE CASCADE ON UPDATE CASCADE, CONSTRAINT lessons_data_ibfk_2 FOREIGN KEY (groups) REFERENCES groups (k) ON DELETE CASCADE ON UPDATE CASCADE, CONSTRAINT lessons_data_ibfk_3 FOREIGN KEY (lesson) REFERENCES lessons (k) ON DELETE CASCADE ON UPDATE CASCADE, CONSTRAINT lessons_data_ibfk_4 FOREIGN KEY (teacher) REFERENCES teachers (k) ON DELETE CASCADE ON UPDATE CASCADE, CONSTRAINT lessons_data_ibfk_5 FOREIGN KEY (auditory) REFERENCES auditorys (k) ON DELETE CASCADE ON UPDATE CASCADE);");
        CREATE_COMMANDS.add("CREATE INDEX lessons_data_ibfk_1 ON lessons_data (lessons_schedule);");
        CREATE_COMMANDS.add("CREATE INDEX lessons_data_ibfk_2 ON lessons_data (groups);");
        CREATE_COMMANDS.add("CREATE INDEX lessons_data_ibfk_3 ON lessons_data (lesson);");
        CREATE_COMMANDS.add("CREATE INDEX lessons_data_ibfk_4 ON lessons_data (teacher);");
        CREATE_COMMANDS.add("CREATE INDEX lessons_data_ibfk_5 ON lessons_data (auditory);");
        CREATE_COMMANDS.add("CREATE TABLE group_load (k INT(11) PRIMARY KEY NOT NULL AUTO_INCREMENT, group_key INT(11) NOT NULL, semester_load_k INT(11) NOT NULL, week_count INT(11) DEFAULT '0' NOT NULL, CONSTRAINT group_load_groups_k_fk FOREIGN KEY (group_key) REFERENCES groups (k) ON DELETE CASCADE ON UPDATE CASCADE, CONSTRAINT group_load_semester_load_k_fk FOREIGN KEY (semester_load_k) REFERENCES semester_load (k) ON DELETE CASCADE ON UPDATE CASCADE);");
        CREATE_COMMANDS.add("CREATE INDEX group_load_groups_k_fk ON group_load (group_key);");
        CREATE_COMMANDS.add("CREATE INDEX group_load_semester_load_k_fk ON group_load (semester_load_k);");
        CREATE_COMMANDS.add("CREATE TABLE load_unit (k INT(11) PRIMARY KEY NOT NULL AUTO_INCREMENT, sorted_number INT(11) NOT NULL, total_amount INT(11) DEFAULT '0' NOT NULL, lecture INT(11) DEFAULT '0' NOT NULL, practical INT(11) DEFAULT '0' NOT NULL, laboratory INT(11) DEFAULT '0' NOT NULL, control_form VARCHAR(256), lesson INT(11) NOT NULL, teacher INT(11) NOT NULL, group_load_k INT(11) NOT NULL, CONSTRAINT load_unit_lessons_k_fk FOREIGN KEY (lesson) REFERENCES lessons (k) ON DELETE CASCADE ON UPDATE CASCADE, CONSTRAINT load_unit_teachers_k_fk FOREIGN KEY (teacher) REFERENCES teachers (k) ON DELETE CASCADE ON UPDATE CASCADE, CONSTRAINT load_unit_group_load_k_fk FOREIGN KEY (group_load_k) REFERENCES group_load (k) ON DELETE CASCADE ON UPDATE CASCADE);");
        CREATE_COMMANDS.add("CREATE INDEX load_unit_group_load_k_fk ON load_unit (group_load_k);");
        CREATE_COMMANDS.add("CREATE INDEX load_unit_lessons_k_fk ON load_unit (lesson);");
        CREATE_COMMANDS.add("CREATE INDEX load_unit_teachers_k_fk ON load_unit (teacher);");
    }
}
