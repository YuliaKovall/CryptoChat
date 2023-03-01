package com.example.cryptochat.pojo;

import java.io.Serializable;
import java.util.Objects;

public class Contact implements Serializable {
   private String id;
   private String name;
   private String number;

   public Contact(String id, String name, String number) {
      this.id = id;
      this.name = name;
      this.number = number;
   }

   public String getId() {
      return id;
   }

   public void setId(String id) {
      this.id = id;
   }

   public String getName() {
      return name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public String getNumber() {
      return number;
   }

   public void setNumber(String number) {
      this.number = number;
   }

   @Override
   public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      Contact contact = (Contact) o;
      return Objects.equals(id, contact.id) && Objects.equals(name, contact.name) && Objects.equals(number, contact.number);
   }

   @Override
   public int hashCode() {
      return Objects.hash(id, name, number);
   }
}
