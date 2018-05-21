CREATE TABLE departments (k INT(11) PRIMARY KEY NOT NULL AUTO_INCREMENT, name VARCHAR(256) NOT NULL);
CREATE TABLE holidays (date DATE NOT NULL, repeats TINYINT(1) DEFAULT '0' NOT NULL, k INT(11) PRIMARY KEY NOT NULL AUTO_INCREMENT);
CREATE TABLE weeks (mark VARCHAR(1) NOT NULL, k INT(11) PRIMARY KEY NOT NULL AUTO_INCREMENT, name VARCHAR(256) NOT NULL, color INT(11) NOT NULL, abbreviation VARCHAR(16) NOT NULL);
CREATE TABLE auditorys (name VARCHAR(256) NOT NULL, k INT(11) PRIMARY KEY NOT NULL AUTO_INCREMENT);

CREATE TABLE groups (name VARCHAR(256) NOT NULL, department INT(11) NOT NULL, dateofcreate DATE NOT NULL, comments VARCHAR(256), k INT(11) PRIMARY KEY NOT NULL AUTO_INCREMENT, CONSTRAINT groups_departments_k_fk FOREIGN KEY (department) REFERENCES departments (k));
CREATE TABLE schedules (period VARCHAR(256) NOT NULL, date_of_create DATE NOT NULL, coments VARCHAR(256), k INT(11) PRIMARY KEY NOT NULL AUTO_INCREMENT);
CREATE TABLE teachers (name VARCHAR(256) NOT NULL, preferences VARCHAR(256), k INT(11) PRIMARY KEY NOT NULL AUTO_INCREMENT);
CREATE TABLE lessons (name VARCHAR(256) NOT NULL, k INT(11) PRIMARY KEY NOT NULL AUTO_INCREMENT, auditory INT(11) NOT NULL, CONSTRAINT lessons_ibfk_1 FOREIGN KEY (auditory) REFERENCES auditorys (k));
CREATE TABLE schedules_data (schedule INT(11) NOT NULL, groups INT(11) NOT NULL, data VARCHAR(52) NOT NULL, k INT(11) PRIMARY KEY NOT NULL AUTO_INCREMENT, CONSTRAINT schedules_data_ibfk_1 FOREIGN KEY (schedule) REFERENCES schedules (k), CONSTRAINT schedules_data_ibfk_2 FOREIGN KEY (groups) REFERENCES groups (k) ON DELETE CASCADE);
CREATE TABLE lessons_schedules (k INT(11) PRIMARY KEY NOT NULL AUTO_INCREMENT, period VARCHAR(256) NOT NULL, date_of_create DATE NOT NULL, coments VARCHAR(256));
CREATE TABLE lessons_data (k INT(11) PRIMARY KEY NOT NULL AUTO_INCREMENT, lessons_schedule INT(11) NOT NULL, groups INT(11) NOT NULL, pair_number VARCHAR(256) NOT NULL, lesson INT(11) NOT NULL, teacher INT(11) NOT NULL, auditory INT(11) NOT NULL, CONSTRAINT lessons_data_ibfk_1 FOREIGN KEY (lessons_schedule) REFERENCES lessons_schedules (k), CONSTRAINT lessons_data_ibfk_2 FOREIGN KEY (groups) REFERENCES groups (k), CONSTRAINT lessons_data_ibfk_3 FOREIGN KEY (lesson) REFERENCES lessons (k), CONSTRAINT lessons_data_ibfk_4 FOREIGN KEY (teacher) REFERENCES teachers (k), CONSTRAINT lessons_data_ibfk_5 FOREIGN KEY (auditory) REFERENCES auditorys (k));

CREATE INDEX teacher ON lessons_data (teacher);
CREATE INDEX auditory ON lessons (auditory);
CREATE INDEX lesson ON lessons_data (lesson);
CREATE INDEX groups ON lessons_data (groups);

CREATE INDEX auditory ON lessons_data (auditory);
CREATE INDEX lessons_schedule ON lessons_data (lessons_schedule);
CREATE INDEX schedule ON schedules_data (schedule);
CREATE INDEX schedules_data_ibfk_2 ON schedules_data (groups);
CREATE INDEX groups_departments_k_fk ON groups (department);

CREATE UNIQUE INDEX weeks_mark_uindex ON weeks (mark);
