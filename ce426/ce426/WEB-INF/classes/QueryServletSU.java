// To save as "<TOMCAT_HOME>\webapps\hello\WEB-INF\classes\QueryServlet.java".
import java.io.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;

@WebServlet("/querysignup")   // Configure the request URL for this servlet (Tomcat 7/Servlet 3.0 upwards)
public class QueryServletSU extends HttpServlet {

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
      out.println("<head><title>Sign Up Response</title></head>");
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

         ResultSet rset = stmt.executeQuery(sqlStr);  // Send the query to the server

         // Step 4: Process the query result set
         int count = 0;
         if(rset.next()) {
			 out.println("<p>This email is already been used. Click <a href=\"signup.html\">Here</a> to try another.</p>");
		 }
		 else{
			 if(!request.getParameter("psw").equals(request.getParameter("psw-repeat"))){

				 out.println("<p>Passwords must match. Click <a href=\"signup.html\">Here</a> to try again.</p>");
			 }
			 else{
				sqlStr = "insert into credentials values ("
		               + "'" + request.getParameter("email") + "','" + request.getParameter("psw")
					   + "')";

				stmt.executeUpdate(sqlStr);

				out.println("<p> Signed up succefully. Click <a href=\"login.html\">Here</a> to log in.</p>");
			 }

		 }
            // Print a paragraph <p>...</p> for each record
      } catch(Exception ex) {
         out.println("<p>Error: " + ex.getMessage() + "</p>");
         out.println("<p>Check Tomcat console for details.</p>");
         ex.printStackTrace();
      }  // Step 5: Close conn and stmt - Done automatically by try-with-resources (JDK 7)

      out.println("</body></html>");
      out.close();
   }
}
