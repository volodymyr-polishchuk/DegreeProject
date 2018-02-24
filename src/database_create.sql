/*створення бази даних*/
CREATE DATABASE ASfSC;

/*створення таблиці АУДИТОРІЇ*/
CREATE TABLE asfsc.auditorys
(
  name VARCHAR(256) NOT NULL,
  k INT PRIMARY KEY NOT NULL AUTO_INCREMENT
);
ALTER TABLE asfsc.auditorys COMMENT = 'Таблиця для збереження аудиторій';

/*створення таблиці ВИКЛАДАЧІВ*/
CREATE TABLE asfsc.teachers
(
  name VARCHAR(256) NOT NULL,
  preferences VARCHAR(256),
  k INT PRIMARY KEY NOT NULL AUTO_INCREMENT
);
ALTER TABLE asfsc.teachers COMMENT = 'Таблиця для збереження викладачів';

/*створення таблиця ПРЕДМЕТИ*/
CREATE TABLE asfsc.lessons
(
  name VARCHAR(256) NOT NULL,
  k INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
  auditory INT NOT NULL,
  FOREIGN KEY (auditory) REFERENCES auditorys (k)
);
ALTER TABLE asfsc.lessons COMMENT = 'Таблиця для збереження предметів';

/*створення таблиці ГРУПИ*/
CREATE TABLE asfsc.groups
(
  name VARCHAR(256) NOT NULL,
  department VARCHAR(256) NOT NULL,
  dateofcreate DATE NOT NULL,
  coments VARCHAR(256),
  k INT PRIMARY KEY NOT NULL AUTO_INCREMENT
);
ALTER TABLE asfsc.groups COMMENT = 'Таблиця для збереження даних про групи студентів';

/*створення таблиці ГРАФІКИ НАВЧАННЯ*/
CREATE TABLE asfsc.schedules
(
  period VARCHAR(256) NOT NULL,
  date_of_create DATE NOT NULL,
  coments VARCHAR(256),
  k INT PRIMARY KEY NOT NULL AUTO_INCREMENT
);
ALTER TABLE asfsc.schedules COMMENT = 'Таблиця для збереження графіків навчання';

/*створення таблиці ДАНІ ГРАФІКІВ НАВЧАННЯ*/
CREATE TABLE asfsc.schedules_data
(
  schedule INT NOT NULL,
  groups INT NOT NULL,
  data VARCHAR(52) NOT NULL,
  k INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
  FOREIGN KEY (schedule) REFERENCES schedules (k),
  FOREIGN KEY (groups) REFERENCES groups (k)
);
ALTER TABLE asfsc.schedules_data COMMENT = 'Таблиця для збереження даних графіків навчання';

/*створення таблиця РОЗКЛАДИ НАВЧАННЯ*/
CREATE TABLE asfsc.lessons_schedules
(
  k INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
  period VARCHAR(256) NOT NULL,
  date_of_create DATE NOT NULL,
  coments VARCHAR(256)
);
ALTER TABLE asfsc.lessons_schedules COMMENT = 'Таблиця для збереження розкладів навчання';

/*створення таблиці ДАНІ РОЗКЛАДІВ НАВЧАННЯ*/
CREATE TABLE asfsc.lessons_data
(
  k INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
  lessons_schedule INT NOT NULL,
  groups INT NOT NULL,
  pair_number INT NOT NULL COMMENT 'Номер пари, який відповідає часу в тижні',
  lesson INT NOT NULL,
  teacher INT NOT NULL,
  auditory INT NOT NULL,
  FOREIGN KEY (lessons_schedule) REFERENCES lessons_schedules (k),
  FOREIGN KEY (groups) REFERENCES groups (k),
  FOREIGN KEY (lesson) REFERENCES lessons (k),
  FOREIGN KEY (teacher) REFERENCES teachers (k),
  FOREIGN KEY (auditory) REFERENCES auditorys (k)
);
ALTER TABLE asfsc.lessons_data COMMENT = 'Таблиця для збереження даних розкладів навчання';

/*створення таблиці ТИЖНІ*/
CREATE TABLE asfsc.weeks
(
  mark VARCHAR(1) NOT NULL,
  k INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
  name VARCHAR(256) NOT NULL,
  color INT NOT NULL,
  abbreviation VARCHAR(16) NOT NULL
);
CREATE UNIQUE INDEX weeks_mark_uindex ON asfsc.weeks (mark);
ALTER TABLE asfsc.weeks COMMENT = 'Таблиця, що зберігає дані про типи тижнів';

/*створення таблиці ВІДДІЛЕННЯ*/
CREATE TABLE asfsc.departments
(
  k INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
  name VARCHAR(256) NOT NULL
);
ALTER TABLE asfsc.departments COMMENT = 'Таблиця для збереження відділень';
