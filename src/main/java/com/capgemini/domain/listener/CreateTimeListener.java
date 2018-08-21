package com.capgemini.domain.listener;

import com.capgemini.domain.AbstractEntity;

import javax.persistence.PrePersist;
import java.util.Calendar;

public class CreateTimeListener {
    @PrePersist
    public void setCreationTime(AbstractEntity abstractEntity) {
        abstractEntity.setCreationTime(Calendar.getInstance().getTime());
    }
}
