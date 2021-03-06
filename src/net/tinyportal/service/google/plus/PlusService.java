/*
    This file is part of tPortal.

    tPortal is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    tPortal is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with tPortal.  If not, see <http://www.gnu.org/licenses/>.

    The original code was written by Sebastien Bettinger <sebastien.bettinger@gmail.com>

 */

package net.tinyportal.service.google.plus;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.tinyportal.Constant;
import net.tinyportal.service.google.GoogleService;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.services.plus.Plus;
import com.google.api.services.plus.model.Activity;
import com.google.api.services.plus.model.Comment;
import com.google.api.services.plus.model.PeopleFeed;
import com.google.api.services.plus.model.Person;

public class PlusService extends GoogleService {

	/**
	 * Logger 
	 */
	protected static Logger logger = Logger.getLogger("logger");
	
	static String[] SCOPE =  {"https://www.googleapis.com/auth/plus.login"};
	static public String NAME = "PLUS";

	@Override
	public List<String> getScopes() {
		return Arrays.asList(SCOPE);
	}

	/**
	 * Build and return a Google+ service object based on given request parameters.
	 * @param credential User credentials.
	 * @return Google+ service object that is ready to make requests, or null if
	 *         there was a problem.
	 */
	@Override
	public Plus getService(Credential credential) {
		return new Plus.Builder(TRANSPORT, JSON_FACTORY, credential).build();
	}

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public void service(HttpServletRequest request, HttpServletResponse response) {
		String[] path = request.getRequestURI().split("/");
		if (path.length<2) return;
		
		String context = path[1];
	
		switch (context) {
			case "comment":
				getComment(request, response);
				break;
			case "people":
				getPeople(request, response);
				break;
		}
	}
	
	private void getComment(HttpServletRequest request, HttpServletResponse response) {
		logger.log(Level.INFO, "Recuperation cercles");
		String[] path = request.getRequestURI().split("/");
		if (path.length<3) return;
		String id = path[2];
		
		Credential credential = (Credential)request.getAttribute(Constant.portlet_credentiel);
		Plus plus = getService(credential);
		try {
			List<Comment> comments = plus.comments().list(id).execute().getItems();			
			sendJson(response, comments);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void getPeople(HttpServletRequest request, HttpServletResponse response) {
		logger.log(Level.INFO, "Recuperation cercles");
		Credential credential = (Credential)request.getAttribute(Constant.portlet_credentiel);
		Plus plus = getService(credential);
		try {
			List<Activity> activities = new ArrayList<>();
			PeopleFeed peoples = plus.people().list("me", "visible").execute();
			
			for (Person person : peoples.getItems()) {
				activities.addAll(plus.activities().list(person.getId(), "public").setMaxResults(4L).execute().getItems());
			}
			sendJson(response, activities);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
