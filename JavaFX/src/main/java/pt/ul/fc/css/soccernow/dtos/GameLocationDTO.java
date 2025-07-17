package pt.ul.fc.css.soccernow.dtos;

public class GameLocationDTO {
    private String address;

    private String city;

    public GameLocationDTO(String address, String city) {
        this.address = address;
        this.city = city;
    }

    public GameLocationDTO() {}

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @Override
    public String toString() {
        return "GameLocation{" + "address='" + address + '\'' + ", city='" + city + '\'' + '}';
    }
}
