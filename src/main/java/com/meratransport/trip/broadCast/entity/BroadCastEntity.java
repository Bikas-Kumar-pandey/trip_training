package com.meratransport.trip.broadCast.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Data
@Table(name="notification")
public class BroadCastEntity {
    @Id

    @Column(name="id")
    private Integer id;

    @Column(name="user_id")
    private String userId;

    @Column(name="tenant_id")
    private String tenantId;

    @Column(name="entity_type")
    private  String entityType;

    @Column(name="handle_source")
    private String handleSource;

    @Column(name="handle_type")
    private String  handleType;

    @Column(name="handle")
    private String handle;

    @Column(name="created_ts")
    private LocalDateTime createdTs;

    @Column(name="updated_ts")
    private LocalDateTime updatedTs;



}