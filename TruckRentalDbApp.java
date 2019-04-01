import java.util.Scanner;
import java.sql.*;
import java.io.IOException;

class TruckRentalDbApp{
    public static void main(String[] args) throws SQLException, IOException{
        Scanner sc = new Scanner(System.in);
        // Register the driver.  You must register the driver before you can use it.
        try{
            DriverManager.registerDriver(new com.ibm.db2.jcc.DB2Driver());
        } 
        catch (Exception cnfe){
            System.out.println("Class not found");
        }

        // This is the url you must use for DB2.
        String url = "jdbc:db2://comp421.cs.mcgill.ca:50000/cs421";
        String your_userid = "cs421g25";
        String your_password = "SBGPgroup25";
        Connection con = DriverManager.getConnection(url,your_userid,your_password);

        boolean keep_running = true;
        int input = 0;
        String deleteSQL= "";
        String insertSQL="";
        String updateSQL="";
        String querySQL="";
        String generalQuerySQL="";
        while(keep_running){
            System.out.println("\n\n----------------------------------------------------");
            System.out.println("\nYou have 6 options. Type the number corresponding to the option to execute that.\n\n1 - Add a new Customer\n2 - Delete all bookings where status is cancelled and print fullnames  of the corresponding people\n3 - View registration number and size/type of the vehicle which has a booking and requested heated seats or a drop-off at airport\n4 - View and/or increase capacity of any location if it has reached full capacity.\n5 - Remove/Close a truck location and move the parked trucks to another location. \n6 - Quit\n");
        
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
                Statement stmt1 = con.createStatement();
                while(true){
                    System.out.println("\nEnter the license number: (8-character)");
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

                    insertSQL = "\ninsert into customer values('"+lic+"','"+lname+"','"+fname+"','"+email+"','"+addr+"',"+phn+")";
                    generalQuerySQL = "select * from customer";
                    System.out.println(insertSQL);

                    try{
                        System.out.println("Before inserting, booking looks like:");
                        ResultSet rs = stmt1.executeQuery(generalQuerySQL);
                        while(rs.next()){
                            lic= rs.getString("LICENCENUMBER");
                            lname= rs.getString("LASTNAME");
                            fname= rs.getString("FIRSTNAME");
                            email= rs.getString("EMAIL");
                            addr= rs.getString("ADDRESS");
                            phn= rs.getLong("PHONENUMBER");
                            System.out.println(lic + " " + lname + " " + fname + " " + email + " " + " " + addr + " " + phn);
                        }
                        stmt1.executeUpdate(insertSQL);

                        System.out.println("\nAfter inserting, booking looks like:");
                        rs = stmt1.executeQuery(generalQuerySQL);
                        while(rs.next()){
                            lic= rs.getString("LICENCENUMBER");
                            lname= rs.getString("LASTNAME");
                            fname= rs.getString("FIRSTNAME");
                            email= rs.getString("EMAIL");
                            addr= rs.getString("ADDRESS");
                            phn= rs.getLong("PHONENUMBER");
                            System.out.println(lic + " " + lname + " " + fname + " " + email + " " + " " + addr + " " + phn);
                        }                        
                    }
                    catch (SQLException e){
                        int sqlCode = e.getErrorCode();
                        String sqlState = e.getSQLState();
                        // something more meaningful than a print would be good
                        System.out.println("Code: " + sqlCode + "  sqlState: " + sqlState);
                    }
                    break;
                }
                stmt1.close();
                System.out.println ("DONE");
                break;


                case 2:
                Statement stmt2 = con.createStatement();
                querySQL = "select DISTINCT firstname,lastname,customer.LICENCENUMBER from customer, booking where status='Cancelled' and customer.LICENCENUMBER=booking.LICENCENUMBER";
                deleteSQL = "delete from booking where status='Cancelled'";
                generalQuerySQL = "select licencenumber,status from booking";
                System.out.println(querySQL + "\n" + deleteSQL);
                System.out.println("\nBefore deleting, the relevant booking table entries are:");
                try{
                    ResultSet rs = stmt2.executeQuery(generalQuerySQL);
                    while(rs.next()){
                        String lic= rs.getString(1);
                        String status= rs.getString(2);
                        System.out.println("Licence number and status:  " + lic + "\t" + status);
                    }
                    rs = stmt2.executeQuery(querySQL);
                    while(rs.next()){
                        String fname= rs.getString(1);
                        String lname= rs.getString(2);
                        String lic= rs.getString(3);
                        System.out.println("\nFullname of person with licence no. who cancelled:  " + fname + " " + lname + ", " +lic);
                    }
                    stmt2.executeUpdate(deleteSQL);
                    System.out.println("\nAfter deleting, the relevant booking table entries are:");
                    rs = stmt2.executeQuery(generalQuerySQL);
                    while(rs.next()){
                        String lic= rs.getString(1);
                        String status= rs.getString(2);
                        System.out.println("Licence number and status:  " + lic + "\t" + status);
                    }
                    rs.close();
                }    
                catch (SQLException e){
                    int sqlCode = e.getErrorCode();
                    String sqlState = e.getSQLState();
                    // something more meaningful than a print would be good
                    System.out.println("Code: " + sqlCode + "  sqlState: " + sqlState);
                }
                stmt2.close();
                System.out.println("DONE");
                break;


                case 3:
                Statement stmt3 = con.createStatement();
                querySQL = "select truck.registration,size from truck,booking where truck.registration=booking.registration and notes='request drop off at airport' union select truck.registration,size from truck,booking where truck.registration=booking.registration and notes='request heated seats'";
                try{
                    ResultSet rs = stmt3.executeQuery(querySQL);
                    while(rs.next()){
                        String registration= rs.getString(1);
                        String size= rs.getString(2);
                        System.out.print("Registration Number:  " + registration);
                        System.out.print("\tSize/Type:  " + size + "\n");
                    }
                    rs.close();
                }
                catch (SQLException e){
                    int sqlCode = e.getErrorCode();
                    String sqlState = e.getSQLState();
                    // something more meaningful than a print would be good
                    System.out.println("Code: " + sqlCode + "  sqlState: " + sqlState);
                }
                stmt3.close();
                System.out.println ("DONE");
                break;

                case 4:
                Statement stmt4 = con.createStatement();
                String storeAddress ="";
                boolean check1= true;

                    while (check1) {
                        System.out.println("Please enter the address of the store where you want to increase capacity:");
                       storeAddress = sc.nextLine();

                       if (storeAddress.length() != 0) { check1=false;}
                   }

                    String firstQuery = ("\nselect * from location where address = '" + storeAddress + "'");
                    int storeCapacity = 0;
                    String storeName = "";
                    int numParkedTrucks =0;
                    int newCapacity = 0;
                    try{
                        ResultSet rs1 = stmt4.executeQuery(firstQuery);
                        while (rs1.next()){
                            storeAddress = rs1.getString("ADDRESS");
                            storeName = rs1.getString("NAME");
                            storeCapacity = rs1.getInt("CAPACITY");
                            System.out.println("The store '" + storeName + "'', located at " + storeAddress + " has a current capacity of "+ storeCapacity);
                        } 
                        rs1.close();
                    }
                    catch (SQLException e){
                        int sqlCode = e.getErrorCode();
                         String sqlState = e.getSQLState();
                        // something more meaningful than a print would be good
                        System.out.println("Code: " + sqlCode + "  sqlState: " + sqlState);
                    }
                    String secondQuery = ("\n select count(*) from truck where address = '"+ storeAddress + "'");
                    try {
                        ResultSet rs2 = stmt4.executeQuery(secondQuery);
                        while (rs2.next()){
                            numParkedTrucks = rs2.getInt(1);
                            if (numParkedTrucks == storeCapacity){
                            System.out.println("According to our database, " + numParkedTrucks + " trucks are parked at this location and the store is at full capacity.");
                        } else {
                            System.out.println("According to our database, " + numParkedTrucks + " trucks are parked at this location and the store is not at full capacity.");
                        }
                        }
                        rs2.close();
                    } 
                    catch (SQLException e){
                        int sqlCode = e.getErrorCode();
                         String sqlState = e.getSQLState();
                        // something more meaningful than a print would be good
                        System.out.println("Code: " + sqlCode + "  sqlState: " + sqlState);
                    }
                    System.out.println("Please enter the new desired capactiy for this location: ");
                    newCapacity = sc.nextInt();
                    String thirdQuery = ("\n UPDATE Location SET capacity = " + newCapacity + " WHERE address = '" + storeAddress + "'");
                    try {
                        stmt4.executeUpdate(thirdQuery);
                    } catch (SQLException e){
                        int sqlCode = e.getErrorCode();
                         String sqlState = e.getSQLState();
                        // something more meaningful than a print would be good
                        System.out.println("Code: " + sqlCode + "  sqlState: " + sqlState);
                    }
                    try {
                        ResultSet rs3 = stmt4.executeQuery(firstQuery);
                        while (rs3.next()){
                            storeAddress = rs3.getString("ADDRESS");
                            storeName = rs3.getString("NAME");
                            storeCapacity = rs3.getInt("CAPACITY");
                            System.out.println("After the update with increase in capacity: ");
                            System.out.println("The store '" + storeName + "', located at " + storeAddress + " has a current capacity of "+ storeCapacity);
                        } 
                        rs3.close();
                    } catch (SQLException e){
                        int sqlCode = e.getErrorCode();
                         String sqlState = e.getSQLState();
                        // something more meaningful than a print would be good
                        System.out.println("Code: " + sqlCode + "  sqlState: " + sqlState);
                    }
                    stmt4.close();
                    break;  

                case 5:
                Statement stmt5 = con.createStatement();
                String queryOne = "select address,name from location";
                String storeAdd = "";
                String nameStore = "";
                String ansTwo = "";
                int storeCap = 0;
                int storeCap2 = 0;
                boolean bool1 = true;
                boolean bool2 = true;
                String storeAdd2 = "";
                String truckReg = "";
                String truckAdd = "";
                int numOne = 0;
                try {
                    ResultSet rs1 = stmt5.executeQuery(queryOne);
                    while (rs1.next()){
                    storeAdd = rs1.getString("ADDRESS");
                    nameStore = rs1.getString("NAME");
                    System.out.println(storeAdd + " " + nameStore);
                } rs1.close(); 
                } catch (SQLException e){
                        int sqlCode = e.getErrorCode();
                         String sqlState = e.getSQLState();
                        System.out.println("Code: " + sqlCode + "  sqlState: " + sqlState);
                }
                System.out.println("From the above options, please enter the address of the location you would like to remove.");
                storeAdd = "";
                while (bool1){
                    storeAdd = sc.nextLine();
                    if (storeAdd.length() != 0) { bool1=false;}
                }
                String queryTwo = ("\n select count(*) from truck where address = '"+ storeAdd + "'");
                    try {
                        ResultSet rs2 = stmt5.executeQuery(queryTwo);
                        while (rs2.next()){
                            numOne = rs2.getInt(1);
                            System.out.println("According to our database, " + numOne + " trucks are parked at this location and will have to be moved to another location before you delete the location.");
                        } rs2.close();
                    } catch (SQLException e){
                        int sqlCode = e.getErrorCode();
                         String sqlState = e.getSQLState();
                        System.out.println("Code: " + sqlCode + "  sqlState: " + sqlState);
                }
                System.out.println("Which location would you like to move trucks to?");
                while (bool2){
                    storeAdd2 = sc.nextLine();
                    if (storeAdd2.length() != 0) { bool2=false;}
                }
                String queryThree = ("Select capacity from location where address = '" + storeAdd2 +"'");
                try {
                    ResultSet rs3 = stmt5.executeQuery(queryThree);
                    while (rs3.next()){
                        storeCap = rs3.getInt("CAPACITY");
                        System.out.println("The chosen location has a capacity of " + storeCap);
                    } rs3.close();
                } catch (SQLException e){
                        int sqlCode = e.getErrorCode();
                         String sqlState = e.getSQLState();
                        System.out.println("Code: " + sqlCode + "  sqlState: " + sqlState);
                }
                
                String queryFour = ("UPDATE Truck Set Address = '" + storeAdd2 + "' WHERE Address = '" + storeAdd + "'");
                try {
                    stmt5.executeUpdate(queryFour);
                } catch (SQLException e){
                        int sqlCode = e.getErrorCode();
                         String sqlState = e.getSQLState();
                        System.out.println("Code: " + sqlCode + "  sqlState: " + sqlState);
                }
                String queryFive = ("DELETE FROM Location WHERE Address= '" + storeAdd + "'");
                try {
                    stmt5.executeUpdate(queryFive);
                } catch (SQLException e){
                        int sqlCode = e.getErrorCode();
                         String sqlState = e.getSQLState();
                        System.out.println("Code: " + sqlCode + "  sqlState: " + sqlState);
                }
                String querySix = ("Select registration, address from truck");
                System.out.println("The new addresses for the trucks are as follows: ");
                try {
                    ResultSet rs4 = stmt5.executeQuery(querySix);
                    while (rs4.next()){
                    truckReg = rs4.getString("REGISTRATION");
                    truckAdd = rs4.getString("ADDRESS");
                    System.out.println(truckReg + " " + truckAdd);
                } rs4.close(); 
                } catch (SQLException e){
                        int sqlCode = e.getErrorCode();
                         String sqlState = e.getSQLState();
                        System.out.println("Code: " + sqlCode + "  sqlState: " + sqlState);
                }
                System.out.println("The location has been removed and the trucks have been moved.");
                stmt5.close();
                break; 




                case 6: keep_running=false; break;
                default: System.out.println("Please type a valid input i.e. a number between 1 and 5 (inclusive)"); break;
            }
        }
        con.close();
    }
}