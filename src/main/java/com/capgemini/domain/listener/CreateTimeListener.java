package com.capgemini.domain.listener;

import com.capgemini.domain.TimeEntity;

import javax.persistence.PrePersist;
import java.util.Calendar;

public class CreateTimeListener {
    @PrePersist
    public void setCreationTime(TimeEntity timeEntity) {
        timeEntity.setCreationTime(Calendar.getInstance().getTime());
    }
}
