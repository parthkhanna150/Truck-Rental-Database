import java.sql.Connection;
import java.sql.Statement;
import java.util.Scanner;
import java.sql.SQLException;

class TruckRentalDbApp{
    public static void main ( String [ ] args ) throws SQLException{
        Scanner sc = new Scanner(System.in);
        // Register the driver.  You must register the driver before you can use it.
        try {
            DriverManager.registerDriver ( new com.ibm.db2.jcc.DB2Driver() ) ;
        } catch (Exception cnfe){
            System.out.println("Class not found");
            }
    
        // This is the url you must use for DB2.
        String url = "jdbc:db2://comp421.cs.mcgill.ca:50000/cs421";
        String your_userid = "cs421g25";
        String your_password = "SBGPgroup25";
        Connection con = DriverManager.getConnection (url,your_userid,your_password) ;
        Statement statement = con.createStatement ( ) ;
        boolean keep_running = true;
        int input = 0;
        while(keep_running){
            System.out.println("You have 5 options. Click the number corresponding to the option to execute that.\nAdd a new Customer - 1\nDelete all bookings where status is cancelled and print fullnames  of the corresponding people- 2\nView size/type of the vehicle that have made a booking and requested heated seats or a drop-off at airport - 3\nIncrease capacity of the stores that have full capacity trucks - 4\nQuit - 5\n");
        
            try{
                input = sc.nextInt();
            }
            catch(Exception e){
                System.out.println("Please type a valid input i.e. a number between 1 and 5 (inclusive)");
                continue;
            }
            
            switch(input){
                case 1: 
                while(true){
                    System.out.println("Enter the license number (8-digit)");
                    String lic = sc.nextLine();
                    if(lic.length()!=8){
                        System.out.println("Not a valid license number");
                        continue;
                    }
                    System.out.println("Enter the last name");
                    String lname = sc.nextLine();
                    System.out.println("Enter the first name");
                    String fname = sc.nextLine();
                    System.out.println("Enter the email");
                    String email = sc.nextLine();
                    System.out.println("Enter the address");
                    String addr = sc.nextLine();
                    System.out.println("Enter the phone number (10-digit)");
                    String phone = sc.nextLine();
                    if(phone.length()!=10){
                        System.out.println("Not a valid license number");
                        continue;
                    }
                    long phn = Long.parseLong(phone);
                    String insertSQL = "insert into customer values('"+lic+"','"+lname+"','"+fname+"','"+email+"','"+addr+"',"+phn+")";
                    System.out.println(insertSQL);
                    // statement.executeUpdate(querySQL) ;
                    break;
                }
                break;
                case 5: keep_running=false; break;
                default: System.out.println("Please type a valid input i.e. a number between 1 and 5 (inclusive)"); break;
            }
        }
            // String querySQL = "SELECT id, name from Customer WHERE NAME = \'Vicki\'";
            // System.out.println (querySQL) ;
            // java.sql.ResultSet rs = statement.executeQuery ( querySQL ) ;
            // while ( rs.next ( ) ) {
            // int id = rs.getInt ( 1 ) ;
            // String name = rs.getString (2);
            // System.out.println ("id:  " + id);
            // System.out.println ("name:  " + name);
            // }
            // System.out.println ("DONE");
        statement.close();
        con.close();
    }
}