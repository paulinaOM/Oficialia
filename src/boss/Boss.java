package boss;

public class Boss {
    int id;
    String name,address;
    int phone;
    byte[] img;

    public Boss(int id,String name, String address, int phone, byte img[]) {
        this.id=id;
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.img=img;
    }

    public byte[] getImg() {
        return img;
    }

    public void setImg(byte[] img) {
        this.img = img;
    }

    public Boss(){

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getPhone() {
        return phone;
    }

    public void setPhone(int phone) {
        this.phone = phone;
    }
}
