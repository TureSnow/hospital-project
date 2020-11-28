package com.example.hospital.model;

import java.io.Serializable;
import java.util.Date;

public class NaSheet implements Serializable {
    private Integer patientId;

    private Date date;

    private String naResult;

    private String illnessLevel;

    private static final long serialVersionUID = 1L;

    public Integer getPatientId() {
        return patientId;
    }

    public void setPatientId(Integer patientId) {
        this.patientId = patientId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getNaResult() {
        return naResult;
    }

    public void setNaResult(String naResult) {
        this.naResult = naResult;
    }

    public String getIllnessLevel() {
        return illnessLevel;
    }

    public void setIllnessLevel(String illnessLevel) {
        this.illnessLevel = illnessLevel;
    }

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        NaSheet other = (NaSheet) that;
        return (this.getPatientId() == null ? other.getPatientId() == null : this.getPatientId().equals(other.getPatientId()))
            && (this.getDate() == null ? other.getDate() == null : this.getDate().equals(other.getDate()))
            && (this.getNaResult() == null ? other.getNaResult() == null : this.getNaResult().equals(other.getNaResult()))
            && (this.getIllnessLevel() == null ? other.getIllnessLevel() == null : this.getIllnessLevel().equals(other.getIllnessLevel()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getPatientId() == null) ? 0 : getPatientId().hashCode());
        result = prime * result + ((getDate() == null) ? 0 : getDate().hashCode());
        result = prime * result + ((getNaResult() == null) ? 0 : getNaResult().hashCode());
        result = prime * result + ((getIllnessLevel() == null) ? 0 : getIllnessLevel().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", patientId=").append(patientId);
        sb.append(", date=").append(date);
        sb.append(", naResult=").append(naResult);
        sb.append(", illnessLevel=").append(illnessLevel);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}