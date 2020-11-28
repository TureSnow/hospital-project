package com.example.hospital.model;

import java.io.Serializable;
import java.util.Date;

public class DailyStatus implements Serializable {
    private Integer patientId;

    private Date date;

    private Float temperature;

    private String lifeState;

    private String naResult;

    private String symptom;

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

    public Float getTemperature() {
        return temperature;
    }

    public void setTemperature(Float temperature) {
        this.temperature = temperature;
    }

    public String getLifeState() {
        return lifeState;
    }

    public void setLifeState(String lifeState) {
        this.lifeState = lifeState;
    }

    public String getNaResult() {
        return naResult;
    }

    public void setNaResult(String naResult) {
        this.naResult = naResult;
    }

    public String getSymptom() {
        return symptom;
    }

    public void setSymptom(String symptom) {
        this.symptom = symptom;
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
        DailyStatus other = (DailyStatus) that;
        return (this.getPatientId() == null ? other.getPatientId() == null : this.getPatientId().equals(other.getPatientId()))
            && (this.getDate() == null ? other.getDate() == null : this.getDate().equals(other.getDate()))
            && (this.getTemperature() == null ? other.getTemperature() == null : this.getTemperature().equals(other.getTemperature()))
            && (this.getLifeState() == null ? other.getLifeState() == null : this.getLifeState().equals(other.getLifeState()))
            && (this.getNaResult() == null ? other.getNaResult() == null : this.getNaResult().equals(other.getNaResult()))
            && (this.getSymptom() == null ? other.getSymptom() == null : this.getSymptom().equals(other.getSymptom()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getPatientId() == null) ? 0 : getPatientId().hashCode());
        result = prime * result + ((getDate() == null) ? 0 : getDate().hashCode());
        result = prime * result + ((getTemperature() == null) ? 0 : getTemperature().hashCode());
        result = prime * result + ((getLifeState() == null) ? 0 : getLifeState().hashCode());
        result = prime * result + ((getNaResult() == null) ? 0 : getNaResult().hashCode());
        result = prime * result + ((getSymptom() == null) ? 0 : getSymptom().hashCode());
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
        sb.append(", temperature=").append(temperature);
        sb.append(", lifeState=").append(lifeState);
        sb.append(", naResult=").append(naResult);
        sb.append(", symptom=").append(symptom);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}