package MyPackege;

import jakarta.servlet.ServletException;
//import java.io.*;
import java.net.*;
import java.util.Date;
import java.util.Scanner;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Servlet implementation class MyServlet
 */
public class MyServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MyServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String inputData=request.getParameter("userInput");
		String city = request.getParameter("city");
		String apikey="6891ebc87d2a7090ead563e86ef2429a";
		String apiurl="https://api.openweathermap.org/data/2.5/weather?q="+city+"&appid="+apikey;
		//api intregation
		URL url= URI.create(apiurl).toURL();
		HttpURLConnection connection =(HttpURLConnection) url.openConnection();
		connection.setRequestMethod("GET");
		//Reading the Data form network
		InputStream inputStream= connection.getInputStream();
		InputStreamReader reader= new InputStreamReader(inputStream);
		
		
	StringBuilder responceContent= new StringBuilder();
		
		//reading the data form the reader by using scanner
		
		Scanner sc= new Scanner(reader);
		while (sc.hasNext()) {
			responceContent.append(sc.nextLine());
		}
		
		sc.close();
		System.out.println(responceContent);
		
	    //now we have to type caste the data = parsh the data into JSON format
		Gson gson = new Gson();
		JsonObject jsonObject= gson.fromJson(responceContent.toString(),JsonObject.class);
		
		
		  //Date & Time
        long dateTimestamp = jsonObject.get("dt").getAsLong() * 1000;
        String date = new Date(dateTimestamp).toString();
        
        //Temperature
        double temperatureKelvin = jsonObject.getAsJsonObject("main").get("temp").getAsDouble();
        int temperatureCelsius = (int) (temperatureKelvin - 273.15);
       
        //Humidity
        int humidity = jsonObject.getAsJsonObject("main").get("humidity").getAsInt();
        
        //Wind Speed
        double windSpeed = jsonObject.getAsJsonObject("wind").get("speed").getAsDouble();
        
        //Weather Condition
        String weatherCondition = jsonObject.getAsJsonArray("weather").get(0).getAsJsonObject().get("main").getAsString();
        
        request.setAttribute("date", date);
        request.setAttribute("city", city);
        request.setAttribute("temprature", temperatureCelsius);
        request.setAttribute("weatherCondition", weatherCondition);
        request.setAttribute("humidity", humidity); 
        request.setAttribute("windSpeed",windSpeed); 
        request.setAttribute("weatherdata", responceContent.toString());
        
        connection.disconnect();
      //  now i going to forward the data to jsp for rendring
        
        request.getRequestDispatcher("index.jsp").forward(request, response);
	}
}
