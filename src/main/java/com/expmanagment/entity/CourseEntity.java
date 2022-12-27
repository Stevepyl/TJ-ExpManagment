package com.expmanagment.entity;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "course", schema = "backend", catalog = "")
public class CourseEntity {
    private int id;
    private String description;
    private String name;
    private Integer semester;
    private Integer year;
    private Integer manager;

    @Id
    @Column(name = "id")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "description")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Basic
    @Column(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Basic
    @Column(name = "semester")
    public Integer getSemester() {
        return semester;
    }

    public void setSemester(Integer semester) {
        this.semester = semester;
    }

    @Basic
    @Column(name = "year")
    public Integer getYear() {
        return year;
    }
    public void setYear(Integer year) {
        this.year = year;
    }

    @Basic
    @Column(name = "manager")
    public Integer getManager() {
        return manager;
    }
    public void setManager(Integer manager) {
        this.manager = manager;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CourseEntity that = (CourseEntity) o;

        if (id != that.id) return false;
        if (!Objects.equals(description, that.description)) return false;
        if (!Objects.equals(name, that.name)) return false;
        if (!Objects.equals(semester, that.semester)) return false;
        if (!Objects.equals(year, that.year)) return false;
        if (!Objects.equals(manager, that.manager)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (semester != null ? semester.hashCode() : 0);
        result = 31 * result + (year != null ? year.hashCode() : 0);
        result = 31 * result + (manager != null ? manager.hashCode() : 0);
        return result;
    }
}
