package Model.Restaurant;

import Model.Table.Table;
import Model.Address.AddressRestaurant;
import Model.Reservation.Reservation;
import lombok.Getter;
import lombok.Setter;
import Model.Feedback.Feedback;

import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
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
    private List<Feedback> feedbacks;
    // We should set it later
    private double serviceAvg;
    private double foodAvg;
    private double ambianceAvg;
    private double overallAvg;


    private String image;

    //  Default constructor
    public Restaurant() {
        this.tables = new ArrayList<>();
        this.reservations = new ArrayList<>();
        this.feedbacks = new ArrayList<>();
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
        this.serviceAvg = 0;
        this.foodAvg=0;
        this.ambianceAvg = 0;
        this.overallAvg = 0;

    }
    public void addTable(Table table) {
        tables.add(table);
    }


    public void addReservation(Reservation reservation) {
        reservations.add(reservation);
    }

    public boolean doesReserveExists(String username){
        for(Reservation reservation : reservations){
            if(reservation.getUsername().equals(username)){
                return true;
            }
        }
        return false;
    }

    public boolean isTimeOk(String username){
        for(Reservation reservation : reservations){
            if(reservation.getUsername().equals(username) && reservation.getDatetime().isBefore(LocalDateTime.now())){
                return true;
            }
        }
        return false;
    }

    public void updateRatingsAvg(double food,double service, double ambiance , double overall){
        this.overallAvg = overall;
        this.foodAvg = food;
        this.serviceAvg = service;
        this.ambianceAvg = ambiance;
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