package com.example.springboot.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.hateoas.RepresentationModel;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "clients")
@Data
@EqualsAndHashCode(callSuper = false)
public class Client extends RepresentationModel<Client> implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID clientId;
    private String name;
    private String login;
    private String email;
    @OneToMany(mappedBy = "client")
    @JsonIgnore
    private List<Order> orders;
}
