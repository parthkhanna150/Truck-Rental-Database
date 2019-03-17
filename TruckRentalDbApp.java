import java.util.Scanner;
class TruckRentalDbApp{
    public static void main ( String [ ] args ) throws SQLException{
        Scanner sc = new Scanner(System.in);
        // Unique table names.  Either the user supplies a unique identifier as a command line argument, or the program makes one up.
        // String tableName = "";
        //     int sqlCode=0;      // Variable to hold SQLCODE
        //     String sqlState="00000";  // Variable to hold SQLSTATE

        // if ( args.length > 0 ){
        //     tableName += args [ 0 ] ;
        // }
        // else {
        //     tableName += "example3.tbl";
        // }

        // Register the driver.  You must register the driver before you can use it.
        try {
            DriverManager.registerDriver ( new com.ibm.db2.jcc.DB2Driver() ) ;
        } catch (Exception cnfe){
            System.out.println("Class not found");
            }
    
        // This is the url you must use for DB2.
        //Note: This url may not valid now !
        String url = "jdbc:db2://comp421.cs.mcgill.ca:50000/cs421";
        String your_userid = "cs421g25";
        String your_password = "SBGPgroup25";
        Connection con = DriverManager.getConnection (url,your_userid,your_password) ;
        Statement statement = con.createStatement ( ) ;
        while(true){
            System.out.println("You have 5 options. Click the number corresponding to the option to execute that.\nAdd a new Customer - 1\nDelete all bookings where status is cancelled and print fullnames  of the corresponding people- 2\nView size/type of the vehicle that have made a booking and requested heated seats or a drop-off at airport - 3\nIncrease capacity of the stores that have full capacity trucks - 4\nQuit - 5\n");
        
            try{
                int input = sc.nextInt();
            }
            catch(Exception e){
                System.out.println("please type a valid input i.e. a number between 1 and 5 (inclusive)");
                continue;
            }
            switch(input){
                case 1:
                case 2:
                case 3:
                case 4:
                case 5:
            }
            String querySQL = "SELECT id, name from Customer WHERE NAME = \'Vicki\'";
            System.out.println (querySQL) ;
            java.sql.ResultSet rs = statement.executeQuery ( querySQL ) ;
            while ( rs.next ( ) ) {
            int id = rs.getInt ( 1 ) ;
            String name = rs.getString (2);
            System.out.println ("id:  " + id);
            System.out.println ("name:  " + name);
            }
            System.out.println ("DONE");
    
        }
    }
}