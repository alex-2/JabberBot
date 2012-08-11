package ws.raidrush.alex2.jabberbot.commands;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public class Tools {

	public static String downloadPage(URL url) {
		try {

			StringBuffer page = new StringBuffer();
			BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
			String line;
			while ((line = in.readLine()) != null) {
				page.append(line);
			}
			return page.toString();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;

	}
}
