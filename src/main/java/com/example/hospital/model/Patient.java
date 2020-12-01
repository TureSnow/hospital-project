package com.example.hospital.model;

import java.io.Serializable;

public class Patient implements Serializable {
    private Integer id;

    private String name;

    private String lifeState;

    private String areaLevel;

    private String illnessLevel;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLifeState() {
        return lifeState;
    }

    public void setLifeState(String lifeState) {
        this.lifeState = lifeState;
    }

    public String getAreaLevel() {
        return areaLevel;
    }

    public void setAreaLevel(String areaLevel) {
        this.areaLevel = areaLevel;
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
        Patient other = (Patient) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getName() == null ? other.getName() == null : this.getName().equals(other.getName()))
            && (this.getLifeState() == null ? other.getLifeState() == null : this.getLifeState().equals(other.getLifeState()))
            && (this.getAreaLevel() == null ? other.getAreaLevel() == null : this.getAreaLevel().equals(other.getAreaLevel()))
            && (this.getIllnessLevel() == null ? other.getIllnessLevel() == null : this.getIllnessLevel().equals(other.getIllnessLevel()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getName() == null) ? 0 : getName().hashCode());
        result = prime * result + ((getLifeState() == null) ? 0 : getLifeState().hashCode());
        result = prime * result + ((getAreaLevel() == null) ? 0 : getAreaLevel().hashCode());
        result = prime * result + ((getIllnessLevel() == null) ? 0 : getIllnessLevel().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", name=").append(name);
        sb.append(", lifeState=").append(lifeState);
        sb.append(", areaLevel=").append(areaLevel);
        sb.append(", illnessLevel=").append(illnessLevel);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}