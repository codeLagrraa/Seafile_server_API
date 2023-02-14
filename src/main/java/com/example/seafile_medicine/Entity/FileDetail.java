package com.example.seafile_medicine.Entity;

public class FileDetail {
    private String type;
    private String id;
    private String name;
    private String permission;
    private String mtime;
    private String last_modified;
    private String last_modifier_email;
    private String last_modifier_name;
    private String last_modifier_contact_email;
    private String size;
    private String starred;
    private String comment_total;
    private String can_edit;

    public FileDetail(String type, String id, String name, String permission, String mtime, String last_modified, String last_modifier_email, String last_modifier_name, String last_modifier_contact_email, String size, String starred, String comment_total, String can_edit) {
        this.type = type;
        this.id = id;
        this.name = name;
        this.permission = permission;
        this.mtime = mtime;
        this.last_modified = last_modified;
        this.last_modifier_email = last_modifier_email;
        this.last_modifier_name = last_modifier_name;
        this.last_modifier_contact_email = last_modifier_contact_email;
        this.size = size;
        this.starred = starred;
        this.comment_total = comment_total;
        this.can_edit = can_edit;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public String getMtime() {
        return mtime;
    }

    public void setMtime(String mtime) {
        this.mtime = mtime;
    }

    public String getLast_modified() {
        return last_modified;
    }

    public void setLast_modified(String last_modified) {
        this.last_modified = last_modified;
    }

    public String getLast_modifier_email() {
        return last_modifier_email;
    }

    public void setLast_modifier_email(String last_modifier_email) {
        this.last_modifier_email = last_modifier_email;
    }

    public String getLast_modifier_name() {
        return last_modifier_name;
    }

    public void setLast_modifier_name(String last_modifier_name) {
        this.last_modifier_name = last_modifier_name;
    }

    public String getLast_modifier_contact_email() {
        return last_modifier_contact_email;
    }

    public void setLast_modifier_contact_email(String last_modifier_contact_email) {
        this.last_modifier_contact_email = last_modifier_contact_email;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getStarred() {
        return starred;
    }

    public void setStarred(String starred) {
        this.starred = starred;
    }

    public String getComment_total() {
        return comment_total;
    }

    public void setComment_total(String comment_total) {
        this.comment_total = comment_total;
    }

    public String getCan_edit() {
        return can_edit;
    }

    public void setCan_edit(String can_edit) {
        this.can_edit = can_edit;
    }

    @Override
    public String toString() {
        return "FileDetail{" +
                "type='" + type + '\'' +
                ", id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", permission='" + permission + '\'' +
                ", mtime='" + mtime + '\'' +
                ", last_modified='" + last_modified + '\'' +
                ", last_modifier_email='" + last_modifier_email + '\'' +
                ", last_modifier_name='" + last_modifier_name + '\'' +
                ", last_modifier_contact_email='" + last_modifier_contact_email + '\'' +
                ", size='" + size + '\'' +
                ", starred='" + starred + '\'' +
                ", comment_total='" + comment_total + '\'' +
                ", can_edit='" + can_edit + '\'' +
                '}';
    }
}
