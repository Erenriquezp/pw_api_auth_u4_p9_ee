package uce.edu.web.api.auth.domain;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.GenerationType;

@Entity
@Table(name = "Usuario")
public class Usuario extends PanacheEntityBase {
   
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   public Long id;

   @Column
   public String username;

   public String password;
   public String rol;

}
