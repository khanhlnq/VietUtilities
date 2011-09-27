package com.khanhlnq.vietutilities.servlets

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class StripDiacriticsServlet extends HttpServlet {

	private static final Logger log = Logger.getLogger(StripDiacriticsServlet.class.getName());

	public void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

 		String urlParam = req.getParameter("url");

 		StringBuffer respBuf = new StringBuffer();
 		String respContentType = "text/plain";

        try {
            URL url = new URL(urlParam.trim());
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(GET);

            int respCode = connection.getResponseCode();

            if (respCode == HttpURLConnection.HTTP_OK) {
            	respContentType = connection.getContentType();
                BufferedReader reader = new BufferedReader(connection.getInputStreamReader("UTF-8");
				String line;
				while ((line = reader.readLine()) != null) {
					respBuf.append(fixSmartQuotes(VietUtilities.stripDiacritics(line.trim()))).append("\n");
				}
            	reader.close();
            } else {
                log.info("HTTP response code: " + respCode);
            }
        } catch (MalformedURLException e) {
            // ...
        } catch (IOException e) {
            // ...
        }

        resp.setContentType(respContentType);
        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().println(respBuf.trim());
    }

    public void doPost(HttpServletRequest req, HttpServletResponse resp) {
    	doGet(req, resp);
    }

	 // replace Word processor smart quotes and ' with ascii if flag set
	 // http://www.coderanch.com/t/443932/java/java/bad-characters-MS-Windows-only
	  String fixSmartQuotes( String s ){
		if( !fixSmart ||
			 s == null ||
			 s.length() == 0 ){
			   return s ;
		}
		s = s.replace( (char)145, (char)'\'');
		s = s.replace( (char)8216, (char)'\''); // left single quote
		s = s.replace( (char)146, (char)'\'');
		s = s.replace( (char)8217, (char)'\''); // right single quote
		s = s.replace( (char)147, (char)'\"');
		s = s.replace( (char)148, (char)'\"');
		s = s.replace( (char)8220, (char)'\"'); // left double
		s = s.replace( (char)8221, (char)'\"'); // right double
		s = s.replace( (char)8211, (char)'-' );
		return s.replace( (char)150, (char)'-' );
	  }

}
