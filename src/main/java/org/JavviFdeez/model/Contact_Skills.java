package org.JavviFdeez.model;

public class Contact_Skills {

    // Attributes
    private int cskill_id;
    private int contact_id;
    private int skill_id;
    private int value;

    // Constructor
    public Contact_Skills(int cskill_id, int contact_id, int skill_id, int value) {
        this.cskill_id = cskill_id;
        this.contact_id = contact_id;
        this.skill_id = skill_id;
        this.value = value;
    }

    // Getters and Setters
    public int getCskill_id() {
        return cskill_id;
    }

    public void setCskill_id(int cskill_id) {
        this.cskill_id = cskill_id;
    }

    public int getContact_id() {
        return contact_id;
    }

    public void setContact_id(int contact_id) {
        this.contact_id = contact_id;
    }

    public int getSkill_id() {
        return skill_id;
    }

    public void setSkill_id(int skill_id) {
        this.skill_id = skill_id;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
