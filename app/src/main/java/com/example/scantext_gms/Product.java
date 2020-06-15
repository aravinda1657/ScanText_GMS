package com.example.scantext_gms;

public class Product {
    String name;
    int price;
    String catogory;

    //List<String> messages;

    public Product() {
        this.name = name;
        this.price = price;
        this.catogory = catogory;
    }

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
        return "{" + "\n" +
                "\"name\": \"" + name + "\",\n" +
                "\"price\": " + price + ",\n" +
                "\"catogory\": \"" + catogory + "\"\n" +
                '}';
    }

    public String toString2() {
        //String.format("%32s%10d%16s", name, price, catogory)
//      return String.format("%s%s%s", formatter(name, 32), formatter(String.valueOf(price),10), formatter(catogory,16)) + '\n';
        return String.format("%-32s%-10d%-16s", name, price, catogory) + "\n";
    }

   /* public String formatter(String s, Integer n) {
        if (s.length() > n) {
            return s;
        } else {
            for (int i = 0; i < n - s.length(); i++) {
                s = s + " ";
            }
        }
        return s;
    }

   */

 /*   public Product getProductObj() {
        Product tempProduct = new Product();
        tempProduct.name = name;
        tempProduct.price = price;
        tempProduct.catogory = catogory;

        return tempProduct;
*/
}


