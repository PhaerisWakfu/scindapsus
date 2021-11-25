package com.scindapsus.apt;


import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import java.io.Serializable;

/**
 * @author wyh
 * @date 2021/6/2 19:21
 */
@FastJPA(basePackage = "com.scindapsus.apt.repository")
@Entity
@Data
@IdClass(User.InnerClassId.class)
public class User {

    @Id
    private String name;

    @Id
    private Integer age;

    private String address;

    @Data
    public static class InnerClassId implements Serializable {

        private static final long serialVersionUID = 1;

        private String name;

        private Integer age;
    }
}
