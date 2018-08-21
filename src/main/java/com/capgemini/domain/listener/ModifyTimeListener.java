package com.capgemini.domain.listener;

import com.capgemini.domain.TimeEntity;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.util.Calendar;

public class ModifyTimeListener {
    @PreUpdate
    @PrePersist
    public void setModificationTime(TimeEntity timeEntity) {
        timeEntity.setLastModificationTime(Calendar.getInstance().getTime());
    }
}
