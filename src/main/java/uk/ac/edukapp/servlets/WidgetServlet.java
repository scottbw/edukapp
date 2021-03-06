/*
 *  (c) 2012 University of Bolton
 *  
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 * limitations under the License.
 */

package uk.ac.edukapp.servlets;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.shiro.SecurityUtils;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;

import uk.ac.edukapp.model.Useraccount;
import uk.ac.edukapp.model.Userrating;
import uk.ac.edukapp.model.Userreview;
import uk.ac.edukapp.model.Widgetprofile;
import uk.ac.edukapp.renderer.ExtendedWidgetProfile;
import uk.ac.edukapp.renderer.MetadataRenderer;
import uk.ac.edukapp.renderer.Renderer;
import uk.ac.edukapp.service.ActivityService;
import uk.ac.edukapp.service.UserRateService;
import uk.ac.edukapp.service.UserReviewService;
import uk.ac.edukapp.service.WidgetProfileService;
import uk.ac.edukapp.service.WidgetStatsService;
import uk.ac.edukapp.util.Message;

/**
 * API for getting widgets
 * 
 * @author scottw
 * 
 */
public class WidgetServlet extends HttpServlet {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest
	 * , javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		String part = req.getParameter("part");
		OutputStream out = resp.getOutputStream();

		//
		// Get widget resource
		//
		Widgetprofile widgetProfile = getWidgetProfile(req);
		
		if (widgetProfile == null) {
			resp.sendError(HttpServletResponse.SC_NOT_FOUND);
			return;
		}
		widgetProfile.getTags();
		widgetProfile.getActivities();
		widgetProfile.getDescription();
		widgetProfile.getFunctionalities();

		//
		// Return full metadata
		//
		if (part == null || part.isEmpty()) {
			ExtendedWidgetProfile extendedWidgetProfile = new ExtendedWidgetProfile();
			extendedWidgetProfile.setWidgetProfile(widgetProfile);
			extendedWidgetProfile.setRenderInfo(Renderer.render(widgetProfile, true));
			extendedWidgetProfile.setRenderUrl(Renderer.render(widgetProfile, false));
			extendedWidgetProfile.setDownloadUrl(Renderer.getDownloadUrl(widgetProfile));
			ActivityService activityService = new ActivityService(
					getServletContext());
			extendedWidgetProfile.setUploadedBy(activityService
					.getUploadedBy(widgetProfile));

			
			WidgetStatsService widgetStatsService = new WidgetStatsService(getServletContext());
			extendedWidgetProfile.setWidgetStats(widgetStatsService.getStats(widgetProfile));

			MetadataRenderer.render(out, extendedWidgetProfile);
		}

		// Get parts

		else if (part.equals("name")) {

		} else if (part.equals("render")) {

		} else if (part.equals("tags")) {

		} else if (part.equals("activities")) {

		} else if (part.equals("description")) {

		} else if (part.equals("comments")) {
			UserReviewService userReviewService = new UserReviewService(
					getServletContext());
			List<Userreview> reviews = userReviewService
					.getUserReviewsForWidgetProfile(widgetProfile);
			MetadataRenderer.render(out, reviews);
		} else if (part.equals("ratings")) {
			UserRateService userRateService = new UserRateService(
					getServletContext());
			List<Userrating> ratings = userRateService
					.getRatingsForWidgetProfile(widgetProfile);
			MetadataRenderer.render(out, ratings);

		} else if (part.equals("rating")) {
			UserRateService userRateService = new UserRateService(
					getServletContext());
			Number avg_rating = userRateService.getAverageRating(widgetProfile);
			MetadataRenderer.render(out, avg_rating);

		} else if (part.equals("stats")) {

		} else if (part.equals("activity")) {

		} else if (part.equals("similar")) {
			WidgetProfileService widgetProfileService = new WidgetProfileService(
					getServletContext());
			List<Widgetprofile> widgetProfiles = widgetProfileService
					.findSimilarWidgetsProfiles(widgetProfile, "en");
			MetadataRenderer.render(out, widgetProfiles);
		}
		out.flush();
		out.close();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest
	 * , javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		//
		// POST always requires a valid user login
		//
		Useraccount userAccount = (Useraccount) SecurityUtils.getSubject()
				.getPrincipal();
		if (userAccount == null) {
			resp.sendError(HttpServletResponse.SC_UNAUTHORIZED);
			return;
		}

		String body = IOUtils.toString(req.getInputStream());
		if (body == null || body.trim().length() == 0) {
			resp.sendError(400, "no tag specified");
			return;
		}

		Message message = new Message();

		String part = req.getParameter("part");

		//
		// Get widget resource
		//
		Widgetprofile widgetProfile = getWidgetProfile(req);
		if (widgetProfile == null)
			resp.sendError(HttpServletResponse.SC_NOT_FOUND);

		OutputStream out = resp.getOutputStream();

		WidgetProfileService widgetProfileService = new WidgetProfileService(
				getServletContext());

		if (part.equals("tag")) {
			message = widgetProfileService.addTag(widgetProfile, body);
			//
			// log this tagging to UserActivity table
			//
			addUserActivity("tagged", widgetProfile.getId());
		} else if (part.equals("comment")) {

			ObjectMapper mapper = new ObjectMapper();
			JsonNode json = mapper.readTree(body);
			String userId = json.findValue("userId").asText();
			String text = json.findValue("comment").asText();

			//
			// Check that the current logged in user is the same principal as
			// the comment owner
			//
			if (userAccount.getId() == Integer.parseInt(userId)) {

				// TODO Map the JSON onto an actual Comment bean or DTO

				UserReviewService userReviewService = new UserReviewService(
						getServletContext());

				if (userReviewService.publishUserReview(text, userAccount,
						widgetProfile)) {
					message.setMessage("OK");
					resp.setStatus(HttpServletResponse.SC_CREATED);
					//
					// log this review to UserActivity table
					//
					addUserActivity("commented", widgetProfile.getId());
				} else {
					message.setMessage("Problem saving review");
					resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				}
			} else {
				message.setMessage("You are not authorized to create a review");
				resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			}

		} else if (part.equals("activity")) {

			WidgetProfileService widgetService = new WidgetProfileService(
					getServletContext());

			if (widgetService.addActivity(widgetProfile, body)) {
				message.setMessage("OK");
				resp.setStatus(HttpServletResponse.SC_CREATED);
				//
				// log this activity-binding UserActivity table
				//
				addUserActivity("bindactivity", widgetProfile.getId());
			} else {
				message.setMessage("Problem saving activity");
				resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);

			}
		} else if (part.equals("rating")) {

			ObjectMapper mapper = new ObjectMapper();
			JsonNode json = mapper.readTree(body);
			String userId = json.findValue("userId").asText();
			String rating = json.findValue("rating").asText();

			System.out.println("userid = " + userId);
			System.out.println("rating = " + rating);

			if (userId == null || rating == null) {
				message.setMessage("parameters missing");
				resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			}

			UserRateService userRateService = new UserRateService(
					getServletContext());

			message = userRateService.publishUserRate(rating, userAccount,
					widgetProfile);
			if (message.getMessage().equals("OK")) {
				//
				// log this rating to UserActivity table
				//
				addUserActivity("rated", widgetProfile.getId());
				resp.setStatus(HttpServletResponse.SC_CREATED);
			} else {
				resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			}

		}

		MetadataRenderer.render(out, message);
		out.flush();
		out.close();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.servlet.http.HttpServlet#doPut(javax.servlet.http.HttpServletRequest
	 * , javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void doPut(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		String part = req.getParameter("part");

		//
		// Get widget resource
		//
		Widgetprofile widgetProfile = getWidgetProfile(req);
		if (widgetProfile == null)
			resp.sendError(HttpServletResponse.SC_NOT_FOUND);

		WidgetProfileService widgetProfileService = new WidgetProfileService(
				getServletContext());

		OutputStream out = resp.getOutputStream();

		if (part.equals("description")) {
			// Update description

			String body = IOUtils.toString(req.getInputStream());
			if (body == null || body.trim().length() == 0) {
				resp.sendError(400, "no tag specified");
				return;
			}

			Message msg = null;

			msg = widgetProfileService.updateDescription(
					widgetProfile.getWidId(), body);

			MetadataRenderer.render(out, msg);
			//
			// log this update to UserActivity table
			//
			addUserActivity("updatedescription", widgetProfile.getId());
		}
		out.flush();
		out.close();
	}

	private Widgetprofile getWidgetProfile(HttpServletRequest request) {
		String id = request.getParameter("id");
		String uri = request.getParameter("uri");

		//
		// Validate
		//
		if ((id == null || id.trim().length() == 0)
				&& (uri == null || uri.trim().length() == 0)) {
			return null;
		}

		WidgetProfileService widgetProfileService = new WidgetProfileService(
				getServletContext());

		//
		// Get widget resource
		//
		Widgetprofile widgetProfile = null;
		if (id != null && id.trim().length() > 0)
			widgetProfile = widgetProfileService.findWidgetProfileById(id);
		if (uri != null && uri.trim().length() > 0)
			widgetProfile = widgetProfileService.findWidgetProfileByUri(uri);

		return widgetProfile;
	}

	private void addActivity(int sub_id, String activity, int obj_id) {
		try {
			ActivityService avtivityServie = new ActivityService(
					getServletContext());
			avtivityServie.addUserActivity(sub_id, activity, obj_id);
		} catch (Exception e) {
			System.err.println("adding to user activity table failed");
			e.printStackTrace();
		}
	}

	private void addUserActivity(String activity, int obj_id) {
		// get userid from security principal
		Useraccount userAccount = (Useraccount) SecurityUtils.getSubject()
				.getPrincipal();
		int userId = userAccount.getId();
		addActivity(userId, activity, obj_id);
	}
}
