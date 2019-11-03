// To save as "<TOMCAT_HOME>\webapps\hello\WEB-INF\classes\QueryServlet.java".
import java.io.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;

@WebServlet("/querylogin")   // Configure the request URL for this servlet (Tomcat 7/Servlet 3.0 upwards)
public class QueryServletLI extends HttpServlet {

   // The doGet() runs once per HTTP GET request to this servlet.
   @Override
   public void doPost(HttpServletRequest request, HttpServletResponse response)
               throws ServletException, IOException {
	  // String rqst_psw;
	  // String act_psw;
      // Set the MIME type for the response message
      response.setContentType("text/html");
      // Get a output writer to write the response message into the network socket
      PrintWriter out = response.getWriter();

      // Print an HTML page as the output of the query
      out.println("<html>");
      out.println("<head><title>Log In Response</title></head>");
      out.println("<body>");

      try (
         // Step 1: Allocate a database 'Connection' object
         Connection conn = DriverManager.getConnection(
               "jdbc:mysql://localhost:3306/users?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC",
               "wwwt", "Uk668itSpH");   // For MySQL
               // The format is: "jdbc:mysql://hostname:port/databaseName", "username", "password"

         // Step 2: Allocate a 'Statement' object in the Connection
         Statement stmt = conn.createStatement();
      ) {
         // Step 3: Execute a SQL SELECT query
         String sqlStr = "select * from credentials where email = "
               + "'" + request.getParameter("email") + "'";   // Single-quote SQL string
 		   // Echo for debugging
         ResultSet rset = stmt.executeQuery(sqlStr);  // Send the query to the server

         // Step 4: Process the query result set
         if(rset.next()) {
			 // rqst_psw = request.getParameter("password");
			 // act_psw = rset.getString("password");
			 // out.println(request.getParameter("psw"));
			 if(request.getParameter("psw").equals(rset.getString("password"))){
				 out.println("<h3>Thank you for logging in.</h3>");
		         out.println("<p>Click <a href=\"index.html\">Here</a> to return to the home page.");

			 }
			 else{
				 out.println("Wrong Password. Click <a href=\"login.html\">Here</a> to try again.");
			 }
		 }
		 else{
			out.println("No such user exists. Click <a href=\"signup.html\">Here</a> to sign up.");

		}
      } catch(Exception ex) {
         out.println("<p>Error: " + ex.getMessage() + "</p>");
         out.println("<p>Check Tomcat console for details.</p>");
         ex.printStackTrace();
      }  // Step 5: Close conn and stmt - Done automatically by try-with-resources (JDK 7)

      out.println("</body></html>");
      out.close();
   }
}
