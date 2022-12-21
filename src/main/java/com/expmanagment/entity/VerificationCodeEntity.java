package com.expmanagment.entity;


import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "verification_code", schema = "backend", catalog = "")
public class VerificationCodeEntity {
    private int userId;
    private String code;
    private Timestamp expiresAt;

    @Id
    @Column
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Basic
    @Column(name = "code")
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Basic
    @Column(name = "expiresAt")
    public Timestamp getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(Timestamp expiresAt) {
        this.expiresAt = expiresAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        VerificationCodeEntity that = (VerificationCodeEntity) o;

        if (userId != that.userId) return false;
        if (!Objects.equals(code, that.code)) return false;
        if (!Objects.equals(expiresAt, that.expiresAt)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = userId;
        result = 31 * result + (code != null ? code.hashCode() : 0);
        result = 31 * result + (expiresAt != null ? expiresAt.hashCode() : 0);
        return result;
    }

}
