package application;
import java.util.Scanner;
import java.util.HashSet;
import java.util.Set;
import domain.user.user;

public class Main {
    private user userD = new user();
    private Set<String> availableReq = new HashSet<String>() {{
        add("addUser");
        add("addRestaurant");
        add("addTable");
        add("reserveTable");
        add("cancelReservation");
        add("showReservationHistory");
        add("searchRestaurantsByName");
        add("searchRestaurantsByType");
        add("showAvailableTables");
        add("addReview");
    }};
    public void main(String[] args) throws InterruptedException {
        System.out.println("welcome!");
        while(true){
            Scanner myObj = new Scanner(System.in);
            System.out.println("to exit enter q else Enter the command : \n");
            String request = myObj.nextLine();
            String[] req = request.split(" ", 2);
            if(this.availableReq.contains(req[0])){
                if(req[0] == "addUser"){
                    userD.addUser(req[2]);
                }
                else if(req[0] == "addRestaurant"){

                }
                else if(req[0] == "addTable"){

                }
                else if(req[0] == "reserveTable"){

                }
                else if(req[0] == "cancelReservation"){

                }
                else if(req[0] == "showReservationHistory"){

                }
                else if(req[0] == "searchRestaurantsByName"){

                }
                else if (req[0] == "searchRestaurantsByType"){

                }
                else if(req[0] == "showAvailableTables"){

                }
                else if(req[0] == "addReview"){

                }
            }
            else if(req[0] == "q"){
                break;
            }
            else{
                System.out.println("request does not exists! try again!");
                Thread.sleep(2000);
                System.out.print("\033[H\033[2J");
                System.out.flush();
            }
        }
    }
}