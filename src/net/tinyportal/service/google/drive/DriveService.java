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

package net.tinyportal.service.google.drive;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.tinyportal.service.google.GoogleService;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.services.drive.Drive;

public class DriveService extends GoogleService {

	/**
	 * Logger 
	 */
	protected static Logger logger = Logger.getLogger("logger");
	
	static String[] SCOPE =  {"https://www.googleapis.com/auth/drive"};
	static public String NAME = "DRIVE";

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
	public Drive getService(Credential credential) {
		return new Drive.Builder(TRANSPORT, JSON_FACTORY, credential).build();
	}

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public void service(HttpServletRequest request, HttpServletResponse response) {

	}
}
