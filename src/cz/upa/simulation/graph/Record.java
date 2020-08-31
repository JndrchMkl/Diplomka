package cz.upa.simulation.graph;

public class Record implements Comparable<Record> {
    String operator;
    Double time;
    Double talent;
    Double propensityCrime;
    Double isMurderer;
    Double isThief;

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public Double getTime() {
        return time;
    }

    public void setTime(Double time) {
        this.time = time;
    }

    public Double getTalent() {
        return talent;
    }

    public void setTalent(Double talent) {
        this.talent = talent;
    }

    public Double getPropensityCrime() {
        return propensityCrime;
    }

    public void setPropensityCrime(Double propensityCrime) {
        this.propensityCrime = propensityCrime;
    }

    public Double getIsMurderer() {
        return isMurderer;
    }

    public void setIsMurderer(Double isMurderer) {
        this.isMurderer = isMurderer;
    }

    public Double getIsThief() {
        return isThief;
    }

    public void setIsThief(Double isThief) {
        this.isThief = isThief;
    }

    public Record(String operator, Double time, Double talent, Double propensityCrime, Double isThief, Double isMurderer) {
        this.operator = operator;
        this.time = time;
        this.talent = talent;
        this.propensityCrime = propensityCrime;
        this.isThief = isThief;
        this.isMurderer = isMurderer;
    }

    @Override
    public int compareTo(Record o) {
        return this.getTime().compareTo(o.getTime());
    }
}
