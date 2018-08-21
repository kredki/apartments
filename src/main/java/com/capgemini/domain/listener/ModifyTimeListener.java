package com.capgemini.domain.listener;

import com.capgemini.domain.AbstractEntity;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.util.Calendar;

public class ModifyTimeListener {
    @PreUpdate
    @PrePersist
    public void setModificationTime(AbstractEntity abstractEntity) {
        abstractEntity.setLastModificationTime(Calendar.getInstance().getTime());
    }
}
