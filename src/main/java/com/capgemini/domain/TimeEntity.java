package com.capgemini.domain;

import com.capgemini.domain.listener.CreateTimeListener;
import com.capgemini.domain.listener.ModifyTimeListener;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.util.Date;

@NoArgsConstructor
@MappedSuperclass
@EntityListeners({CreateTimeListener.class, ModifyTimeListener.class})
public abstract class TimeEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Column(name = "CREATION_TIME")
    private Date creationTime;
    @Column(name = "LAST_MODIFICATION_TIME")
    private Date lastModificationTime;

    public Date getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(final Date creationTime) {
        this.creationTime = creationTime;
    }

    public Date getLastModificationTime() {
        return lastModificationTime;
    }

    public void setLastModificationTime(final Date lastModificationTime) {
        this.lastModificationTime = lastModificationTime;
    }
}
