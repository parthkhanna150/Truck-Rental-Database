import java.util.Scanner;
import java.sql.*;
import java.io.IOException;

class TruckRentalDbApp{
    public static void main(String[] args) throws SQLException, IOException{
        Scanner sc = new Scanner(System.in);
        // Register the driver.  You must register the driver before you can use it.
        try{
            DriverManager.registerDriver (new com.ibm.db2.jcc.DB2Driver()) ;
        } 
        catch (Exception cnfe){
            System.out.println("Class not found");
        }
    
        // This is the url you must use for DB2.
        String url = "jdbc:db2://comp421.cs.mcgill.ca:50000/cs421";
        String your_userid = "cs421g25";
        String your_password = "SBGPgroup25";
        Connection con = DriverManager.getConnection (url,your_userid,your_password) ;
        Statement statement = con.createStatement() ;

        boolean keep_running = true;
        int input = 0;
        String deleteSQL="";
        String insertSQL="";
        String updateSQL="";
        String querySQL="";

        while(keep_running){
            System.out.println("\n\n----------------------------------------------------");
            System.out.println("\nYou have 6 options. Type the number corresponding to the option to execute that.\n\n1 - Add a new Customer\n2 - Delete all bookings where status is cancelled and print fullnames  of the corresponding people\n3 - View registration number and size/type of the vehicle which has a booking and requested heated seats or a drop-off at airport\n4 - Increase capacity of the stores that have full capacity trucks\n5 - ADD ONE MORE QUERY\n6 - Quit\n");
        
            try{
                while(!sc.hasNextInt()) {
                    sc.next();
                }
                input = sc.nextInt();        
            }
            catch(Exception e){
                System.out.println("\nPlease type a valid input i.e. a number between 1 and 6 (inclusive)\n\n");
                continue;
            }
            switch(input){
                case 1: 
                while(true){
                    System.out.println("\nEnter the license number (8-character)");
                    String lic = sc.next();
                    // System.out.println(lic);
                    if(lic.length()!=8){
                        System.out.println("Not a valid license number");
                        continue;
                    }

                    System.out.println("\nEnter the last name");
                    String lname = sc.next();
                    // System.out.println(lname);

                    System.out.println("\nEnter the first name");
                    String fname = sc.next();
                    // System.out.println(fname);

                    System.out.println("\nEnter the email");
                    String email = sc.next();
                    // System.out.println(email);
                    sc.nextLine();
                    System.out.println("\nEnter the address");
                    String addr = sc.nextLine();
                    // System.out.println(addr);

                    System.out.println("\nEnter the phone number");
                    long phn = sc.nextLong();
                    // System.out.println(phone);
                    // if(phone.length()!=10){
                    //     System.out.println("\nNot a valid phone number");
                    //     continue;
                    // }
                    // long phn = Long.parseLong(phone);
                    insertSQL = "\ninsert into customer values('"+lic+"','"+lname+"','"+fname+"','"+email+"','"+addr+"',"+phn+")";
                    System.out.println(insertSQL);
                    try{
                        statement.executeUpdate(insertSQL) ;
                    } 
                    catch (SQLException e){
                        int sqlCode = e.getErrorCode();
                        String sqlState = e.getSQLState();
                        // something more meaningful than a print would be good
                        System.out.println("Code: " + sqlCode + "  sqlState: " + sqlState);
                    }
                    break;
                }
                System.out.println ("DONE");
                break;


                case 2: 
                querySQL = "\nselect DISTINCT firstname,lastname from customer, booking where status='Cancelled' and customer.LICENCENUMBER=booking.LICENCENUMBER";
                deleteSQL = "delete from booking where status='Cancelled'";
                System.out.println(querySQL + "\n" + deleteSQL);
                System.out.println("Before deleting:");
                try{
                    java.sql.ResultSet rs = statement.executeQuery(querySQL);
                    while(rs.next()){
                        String registration= rs.getString("Registration");
                        String size= rs.getString("SIZE");
                        int id = rs.get (1) ;
                        String name = rs.getString (2);
                        System.out.print ("Fullname:  " + registration);
                    }

                    statement.executeUpdate(deleteSQL);
                    
                    System.out.println("After deleting:");
                    java.sql.ResultSet rs = statement.executeQuery(querySQL);
                    while(rs.next()){
                        String fname= rs.getString("Registration");
                        String lname= rs.getString("SIZE");
                        System.out.print ("Fullname:  " + registration);
                    }
                }    
                catch (SQLException e){
                    int sqlCode = e.getErrorCode();
                    String sqlState = e.getSQLState();
                    // something more meaningful than a print would be good
                    System.out.println("Code: " + sqlCode + "  sqlState: " + sqlState);
                }
                break;


                case 3:
                querySQL = "select truck.registration,size from truck,booking where truck.registration=booking.registration and notes='request drop off at airport' union select truck.registration,size from truck,booking where truck.registration=booking.registration and notes='request heated seats'";
                try{
                    java.sql.ResultSet rs = statement.executeQuery(querySQL);
                    while(rs.next()){
                        String registration= rs.getString("Registration");
                        String size= rs.getString("SIZE");
                        int id = rs.get (1) ;
                        String name = rs.getString (2);
                        System.out.print ("Registration Number:  " + registration);
                        System.out.print ("\tSize/Type:  " + size);
                    }
                }
                catch (SQLException e){
                    int sqlCode = e.getErrorCode();
                    String sqlState = e.getSQLState();
                    // something more meaningful than a print would be good
                    System.out.println("Code: " + sqlCode + "  sqlState: " + sqlState);
                }
                System.out.println ("DONE");
                break;

                case 4:
                
                // case 5:
                case 6: keep_running=false; break;
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