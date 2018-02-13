/*створення бази даних*/
CREATE DATABASE ASfSC;

/*створення таблиці АУДИТОРІЇ*/
CREATE TABLE asfsc.auditory
(
    name VARCHAR(256) NOT NULL
);
ALTER TABLE asfsc.auditory COMMENT = 'Таблиця для збереження аудиторій';

/*створення таблиці ВИКЛАДАЧІВ*/
CREATE TABLE asfsc.teacher
(
  name VARCHAR(256) NOT NULL,
  preferences VARCHAR(256)
);
ALTER TABLE asfsc.teacher COMMENT = 'Таблиця для збереження викладачів';

/*створення таблиця ПРЕДМЕТИ*/
CREATE TABLE asfsc.lesson
(
  name VARCHAR(256) NOT NULL,
  auditory VARCHAR(256)
);
ALTER TABLE asfsc.lesson COMMENT = 'Таблиця для збереження існуючих предметів';

/*створення таблиці ГРУПИ*/
CREATE TABLE asfsc.groups
(
  name VARCHAR(256) NOT NULL,
  department VARCHAR(256) NOT NULL,
  dateofcreate DATE NOT NULL,
  coments VARCHAR(256)
);
ALTER TABLE asfsc.groups COMMENT = 'Таблиця для збереження даних про групи студентів';
