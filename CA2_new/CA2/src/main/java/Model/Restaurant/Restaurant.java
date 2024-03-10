package Model.Restaurant;

import Model.Table.Table;
import Model.Address.AddressRestaurant;
import Model.Reservation.Reservation;
import lombok.Getter;
import lombok.Setter;
import java.util.List;
import java.util.ArrayList;

@Getter
@Setter
public class Restaurant {
    private int id;
    private String name;
    private String managerUsername;
    private String type;
    private String startTime;
    private String endTime;
    private String description;
    private AddressRestaurant address;
    private List<Table> tables;
    private List<Reservation> reservations;
    // We should set it later
    private double serviceAvg;
    private double foodAvg;
    private double ambianceAvg;

    //  Default constructor
    public Restaurant() {
        this.tables = new ArrayList<>();
        this.reservations = new ArrayList<>();
    }

    public Restaurant(String name, String managerUsername, String type, String startTime, String endTime, String description, AddressRestaurant address) {
        this.name = name;
        this.managerUsername = managerUsername;
        this.type = type;
        this.startTime = startTime;
        this.endTime = endTime;
        this.description = description;
        this.address = address;
        this.tables = new ArrayList<>();
        this.reservations = new ArrayList<>();

    }

    public String toHtml(int id){
        String finalStr = "<ul>" +
                "    <li id='id"+id+"'>Id: "+id+"</li>" +
                "    <li id='name"+id+"'>Name:"+this.name+"</li>" +
                "    <li id='type"+id+"'>Type: "+this.type+"</li>" +
                "    <li id='time"+id+"'>Time: "+this.startTime+" - "+this.endTime+"</li>" +
                "    <li id='description"+id+"'>Description: "+this.description+"</li>" +
                "    <li id='address"+id+"'>Address: "+this.address.getCountry()+", "+this.address.getCity()+", "+this.address.getStreet()+"</li>" +
                "    <li id='tables"+id+"'>Tables:</li>" +
                "    <ul>";
        for (int i = 0 ; i < tables.size();i++){
            finalStr += "<li>table"+i+1+"</li>";
        }
        finalStr += "    </ul>\n" +
                "</ul>";
        return finalStr;
    }
}