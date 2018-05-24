package app.data.loading;

import app.data.Lesson;
import app.data.Teacher;

/**
 * Created by Vladimir on 23/05/18.
 **/
public class LoadUnit {
    private GroupLoad owner;

    private Lesson lesson;
    private Teacher teacher;
    private int totalAmount; // Загальне навантаження
    private int lectures; // Лекційних занять
    private int practical; // Практичних занять
    private int laboratory; // Лабораторних занять
    private String controlForm; // Форма контролю

    public LoadUnit(GroupLoad owner) {
        this.owner = owner;
        this.lesson = null;
        this.teacher = null;
        this.totalAmount = 0;
        this.lectures = 0;
        this.practical = 0;
        this.laboratory = 0;
        this.controlForm = "";
    }

    public LoadUnit(GroupLoad owner, Lesson lesson, Teacher teacher, int totalAmount, int lectures, int practical, int laboratory, String controlForm) {
        this.owner = owner;
        this.lesson = lesson;
        this.teacher = teacher;
        this.totalAmount = totalAmount;
        this.lectures = lectures;
        this.practical = practical;
        this.laboratory = laboratory;
        this.controlForm = controlForm;
    }

    public Lesson getLesson() {
        return lesson;
    }

    public void setLesson(Lesson lesson) {
        this.lesson = lesson;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public int getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(int totalAmount) {
        this.totalAmount = totalAmount;
    }

    public int getLectures() {
        return lectures;
    }

    public void setLectures(int lectures) {
        this.lectures = lectures;
    }

    public int getPractical() {
        return practical;
    }

    public void setPractical(int practical) {
        this.practical = practical;
    }

    public int getLaboratory() {
        return laboratory;
    }

    public void setLaboratory(int laboratory) {
        this.laboratory = laboratory;
    }

    public String getControlForm() {
        return controlForm;
    }

    public void setControlForm(String controlForm) {
        this.controlForm = controlForm;
    }

    public int getClassroom() {
        return getLaboratory() + getPractical() + getLectures();
    }

    public float getWeekLoad() {
        return ((float) getClassroom()) / owner.getWeekCount();
    }
}
