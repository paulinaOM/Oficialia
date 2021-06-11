package official;

public class Official {
    int id;
    String state, municipality,address;
    int phone;
    String boss,schedule;
    int idMun,idBoss;

    public Official(int id,String state, String municipality, String address, int phone, String boss, String schedule,int idMun,int idBoss) {
        this.id=id;
        this.state = state;
        this.municipality = municipality;
        this.address = address;
        this.phone = phone;
        this.boss = boss;
        this.schedule = schedule;
        this.idMun= idMun;
        this.idBoss=idBoss;
    }

    @Override
    public String toString() {
        return "Official{" +
                "id=" + id +
                ", state='" + state + '\'' +
                ", municipality='" + municipality + '\'' +
                ", address='" + address + '\'' +
                ", phone=" + phone +
                ", boss='" + boss + '\'' +
                ", schedule='" + schedule + '\'' +
                ", idMun=" + idMun +
                ", idBoss=" + idBoss +
                '}';
    }

    public int getIdMun() {
        return idMun;
    }

    public void setIdMun(int idMun) {
        this.idMun = idMun;
    }

    public int getIdBoss() {
        return idBoss;
    }

    public void setIdBoss(int idBoss) {
        this.idBoss = idBoss;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getMunicipality() {
        return municipality;
    }

    public void setMunicipality(String municipality) {
        this.municipality = municipality;
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

    public String getBoss() {
        return boss;
    }

    public void setBoss(String boss) {
        this.boss = boss;
    }

    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }
}
