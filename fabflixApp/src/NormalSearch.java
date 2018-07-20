

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashSet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.gson.*;

/**
 * Servlet implementation class NormalSearch
 */
@WebServlet("/NormalSearch")
public class NormalSearch extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public NormalSearch() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
		String loginUser = "root";
        String loginPasswd = "MySQLPassword123";
        String loginUrl = "jdbc:mysql://localhost:3306/moviedb?autoReconnect=true&useSSL=false";
        
        try {
        	Class.forName("com.mysql.jdbc.Driver").newInstance();

            Connection dbcon = DriverManager.getConnection(loginUrl, loginUser, loginPasswd);
            
            String query = request.getParameter("title");
            String[] qArray = query.trim().split("\\s+");
            
            String s = "select * from movietitles "
            		+ "where MATCH(titles) AGAINST('"; 
            
            for (int i = 0; i < qArray.length; i++)
            {
            	String add = qArray[i] + "* ";
            	s += add;
            }
            
            s += "' in boolean mode)";

            PreparedStatement statement = dbcon.prepareStatement(s);
            ResultSet rs = statement.executeQuery();
            
            rs.beforeFirst();
            
            PreparedStatement statement2 = dbcon.prepareStatement("select m.title, m.year, m.director, m.id, GROUP_CONCAT(DISTINCT(g.name)) AS genres_list, GROUP_CONCAT(DISTINCT(s.name)) AS stars_list "
        			+ "from genres as g "
        			+ "inner join genres_in_movies as gm on gm.genreId = g.id "
        			+ "inner join movies as m on m.id = gm.movieId "
        			+ "inner join stars_in_movies as sm on sm.movieId = m.id "
        			+ "inner join stars as s on s.id = sm.starId "
        			+ "where m.title = ? "
        			+ "group by m.title");
            ResultSet rs2;
            
    		HashSet<String> toAdd = new HashSet<String>();

            while (rs.next()){
            	
            	statement2.setString(1, rs.getString("titles"));
            	rs2 = statement2.executeQuery();
            	
           
            	rs2.beforeFirst();
            	
            	while (rs2.next())
            	{            		
            		toAdd.add("{\"title\": \"" +  rs2.getString("title") 
            				+ "\", \"year\": \"" +  rs2.getString("year") 
            				+ "\", \"director\": \"" +  rs2.getString("director")
            				+ "\", \"genres\": \"" +  rs2.getString("genres_list")
            				+ "\", \"stars\": \"" +  rs2.getString("stars_list")
            				+ "\"} ");
            	}
    			    	
            }
            if (toAdd.isEmpty())
            {
            	response.getWriter().write("fail");
            	return;
            }
            String json = new Gson().toJson(toAdd);
            response.getWriter().write(json);
            
        }
        catch(Exception e){
        	response.getWriter().print("error");
        }
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
