package com.github.sokumbhar.MyApplication.persistence.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "alert")
@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
public class Alert {
    @Id
    private String id;

    private int duration;

    private String eventType;

    private String host;

    private boolean flagAlert;
}
