package com.example.scantext_gms;


public class Product {
    String name;
    int price;
    String catogory;

    //List<String> messages;

    public String getName() {
        return name;
    }

    public void setName(String catogory) {
        this.name = name;
    }

    public String getCatogory() {
        return catogory;
    }

    public void setCatogory(String catogory) {
        this.catogory = catogory;
    }


    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

  /*
    public List<String> getMessages() {
        return messages;
    }

    public void setMessages(List<String> messages) {
        this.messages = messages;
    }
*/
    @Override
    public String toString() {
        return "{" +
                "name='" + name + '\'' +
                ", price=" + price +
                ", catogory=" + catogory +
 //               ", messages=" + messages +
                '}';
    }
    public String toString2() {
        return name + " " + price + " " + catogory + '\n';
    }
}

