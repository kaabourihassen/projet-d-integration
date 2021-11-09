package com.Project.Project.Entity;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Embeddable;

@Embeddable
public class ServicesRatesId implements Serializable{
	
	private Long raterId;
	private Long serviceId;
	
	public Long getRaterId() {
		return raterId;
	}
	public void setRaterId(Long raterId) {
		this.raterId = raterId;
	}
	public Long getServiceId() {
		return serviceId;
	}
	public void setServiceId(Long serviceId) {
		this.serviceId = serviceId;
	}
	
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ServicesRatesId)) return false;
        ServicesRatesId servicesRatesId = (ServicesRatesId) o;
        return Objects.equals(getRaterId(), servicesRatesId.getRaterId()) &&
                Objects.equals(getServiceId(), servicesRatesId.getServiceId());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getRaterId(), getServiceId());
    }
	

}
