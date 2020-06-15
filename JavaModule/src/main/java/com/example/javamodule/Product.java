package com.example.javamodule;


import java.util.List;

public class Product {
    String name;
    int price;
    String catogory;

    //List<String> messages;

    public String getname() {
        return name;
    }

    public void setName(String catogory) {
        this.name = name;
    }

    public String getcatogory() {
        return catogory;
    }

    public void setcatogory(String catogory) {
        this.catogory = catogory;
    }


    public int getprice() {
        return price;
    }

    public void setprice(int price) {
        this.price = price;
    }

  /*
    public List<String> getMessages() {
        return messages;
    }

    public void setMessages(List<String> messages) {
        this.messages = messages;
    }

    @Override
    public String toString() {
        return "Product{" +
                "name='" + name + '\'' +
                ", price=" + price +
                ", catogory=" + catogory +
 //               ", messages=" + messages +
                '}';

   */
    }
}
