package asov.uws;

import java.io.IOException;
import javax.servlet.ServletException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import uws.UWSException;

import uws.job.JobList;

import uws.service.BasicUWS;
import uws.service.QueuedBasicUWS;
import uws.service.UWSUrl;
import uws.service.UserIdentifier;

public class DemoUWS_Servlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private BasicUWS<DemoUWS_Job> uws = null;

	@Override
	public void init() throws ServletException {
		super.init();
		try {
			// 1. Create a UWS with a queue of 10 jobs:
			uws = new QueuedBasicUWS<DemoUWS_Job>(DemoUWS_Job.class, 10);

			// 2. Create 1 jobs list:
			uws.addJobList(new JobList<DemoUWS_Job>("jobs"));

			// Change the home page (/asov/uwsHome.html):
			uws.setHomePage("/asov/uwsHome.html");

			// Identify users by their IP address:
			uws.setUserIdentifier(new UserIdentifier() {
				private static final long serialVersionUID = 1L;
				@Override
				public String extractUserId(UWSUrl url, HttpServletRequest request) throws UWSException {
					return request.getRemoteAddr();
				}
			});
		} catch (UWSException e) {
			throw new ServletException(e);
		}
	}

	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			// 3. Forward all requests to the UWS object:
			uws.executeRequest(request, response);
		} catch (UWSException e) {
			response.sendError(e.getHttpErrorCode(), e.getMessage());
		}
	}

}
