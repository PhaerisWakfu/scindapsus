package com.scindapsus.apt;


import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import java.io.Serializable;

/**
 * @author wyh
 * @since 1.0
 */
@FastJPA(basePackage = "com.scindapsus.apt.repository")
@Entity
@IdClass(User.InnerClassId.class)
public class User {

    @Id
    private String name;

    @Id
    private Integer age;

    private String address;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public static class InnerClassId implements Serializable {

        private static final long serialVersionUID = 1;

        private String name;

        private Integer age;

        public static long getSerialVersionUID() {
            return serialVersionUID;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Integer getAge() {
            return age;
        }

        public void setAge(Integer age) {
            this.age = age;
        }
    }
}
