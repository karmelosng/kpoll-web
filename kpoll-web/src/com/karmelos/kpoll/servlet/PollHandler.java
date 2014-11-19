package com.karmelos.kpoll.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.HttpRequestHandler;

import com.karmelos.kpoll.model.ChoicePoll;
import com.karmelos.kpoll.model.FreeTextPoll;
import com.karmelos.kpoll.model.InterestArea;
import com.karmelos.kpoll.model.KeywordPoll;
import com.karmelos.kpoll.model.Participant;
import com.karmelos.kpoll.model.PollAdmin;
import com.karmelos.kpoll.model.PollOwner;
import com.karmelos.kpoll.model.PollSurvey;
import com.karmelos.kpoll.service.PersistenceService;
import com.karmelos.kpoll.util.PollContent;
import com.karmelos.kpoll.util.ServletUtil;

/**
 * Servlet implementation class PollHandler
 */
@Component("pollServlet")
public class PollHandler implements HttpRequestHandler {
	public static final Log LOG = LogFactory.getLog(PollHandler.class);
	public static final String REG_PARTICIPANT = "register";
	public static final String PUSH_STRING = "pushpoll";
	public static final String RESPONSE_POLL = "respond";
	public static final String CREATE_POLL = "createpoll";
	public static final String ADD_AFFILIATES = "addaffiliates";
	public static final String RETRIEVE_SURVEY = "retrievesurvey";
	private static final String GOOGLE_SERVER_KEY = "AIzaSyClIxHxErCXCIwzvISTUIQRokhxsBA_ZQI";
	static final String MESSAGE_KEY = "message";
	@Autowired
	private PersistenceService persistenceService;

	public void setPersistenceService(PersistenceService persistenceService) {
		this.persistenceService = persistenceService;
	}

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public PollHandler() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public void handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String cmd = request.getParameter("command");
		
		
		if (cmd.equalsIgnoreCase(REG_PARTICIPANT)) {
			// To print Out Response From Server	
			PrintWriter output = response.getWriter();
			String userType = null;
			String pType = request.getParameter("pollusertype");
			if (pType.equals("0")) {
				PollOwner tobeAdded = new PollOwner();
				userType="PollOwner";
				tobeAdded.setPasswordHash(ServletUtil.md5(request
						.getParameter("password")));
				tobeAdded.setHashSalt(ServletUtil.generateSalt());
				tobeAdded.setEmailAddress(request.getParameter("email"));
				tobeAdded.setOwnerName(request.getParameter("ownername"));
				persistenceService.registerPollUser(tobeAdded);
				
			} else if (pType.equals("1")) {

				PollAdmin tobeAdded = new PollAdmin();
				tobeAdded.setPasswordHash(ServletUtil.md5(request
						.getParameter("password")));
				userType="PollAdmin";
				tobeAdded.setHashSalt(ServletUtil.generateSalt());
				tobeAdded.setEmailAddress(request.getParameter("email"));
				persistenceService.registerPollUser(tobeAdded);

			} else {
				List<InterestArea> interestAreas = ServletUtil.splitString(request
						.getParameter("areas"));
				Participant tobeAdded = new Participant();
				tobeAdded.setPhoneNumber(request.getParameter("phone"));
				tobeAdded.setPasswordHash(ServletUtil.md5(request
						.getParameter("password")));
				userType="Participant";
				tobeAdded.setHashSalt(ServletUtil.generateSalt());
				tobeAdded.setParticipantGcmId(request.getParameter("gcmId"));
				persistenceService.registerPollUser(tobeAdded);
				// merge gcmId
				tobeAdded.setInterestAreas(interestAreas);
				persistenceService.registerPollUser(tobeAdded);
			 }
			 output.print(userType +"created");
		} 
		
		else if (cmd.equalsIgnoreCase(CREATE_POLL)) {
			// choicePoll =0, FreeText =1, KeyWord =2
			PrintWriter output = response.getWriter();
			String pollType = request.getParameter("polltype");
			String userType = null;
			String poll=null;
			if (pollType.equals("0")) {
				String pollowner_id = request.getParameter("pollowner_email");
				PollOwner owner = (PollOwner) persistenceService
						.retrieveAnyEntity(PollOwner.class, pollowner_id);
				// create new ChoicePoll instanceer
				userType ="ChoicePoll";
				ChoicePoll cPoll = new ChoicePoll();
				cPoll.setDescription(request.getParameter("description"));
				cPoll.setPollOwner(owner);
				persistenceService.createPoll(owner, cPoll,
						ServletUtil.splitString(request.getParameter("interestareas")));
			} else if (pollType.equals("1")) {
				String pollowner_id = request.getParameter("pollowner_email");
				PollOwner owner = (PollOwner) persistenceService
						.retrieveAnyEntity(PollOwner.class, pollowner_id);
				// create new ChoicePoll instance
				userType ="FreeTextPoll";
				FreeTextPoll fPoll = new FreeTextPoll();
				fPoll.setDescription(request.getParameter("description"));
				fPoll.setPollOwner(owner);
				persistenceService.createPoll(owner, fPoll,
						ServletUtil.splitString(request.getParameter("interestareas")));
			} else if (pollType.equals("2")) {
				String pollowner_id = request.getParameter("pollowner_email");
				PollOwner owner = (PollOwner) persistenceService
						.retrieveAnyEntity(PollOwner.class, pollowner_id);
				// create new ChoicePoll instance
				userType ="keywordPoll";
				KeywordPoll kPoll = new KeywordPoll();
				kPoll.setDescription(request.getParameter("description"));
				kPoll.setKeywordId(request.getParameter("keyword"));
				kPoll.setPollOwner(owner);
				persistenceService.createPoll(owner, kPoll,
						ServletUtil.splitString(request.getParameter("interestareas")));
			}
                  output.print(userType +" Created");
		} 
		
		else if (cmd.equals("listpollsbyowner")) {
			
			PollOwner owner = (PollOwner) persistenceService.retrieveAnyEntity(
					PollOwner.class, request.getParameter("email"));
			List<PollSurvey> lv = persistenceService.listPollsByOwner(owner);
			ServletOutputStream oos = response.getOutputStream();
			oos.write(ServletUtil.serialize(lv));
			

		}else if (cmd.equals("listinterestarea")) {
			
		
			List<InterestArea> lv = persistenceService.loadInterestAreas();
			ServletOutputStream oos = response.getOutputStream();
			oos.write(ServletUtil.serialize(lv));
			

		} else if (cmd.equals("testjson")) {
			// THIS USES the ObjectOutputStream!! SO an object
			PollOwner owner = (PollOwner) persistenceService.retrieveAnyEntity(
					PollOwner.class, request.getParameter("id"));
			// String jsonReply =ServletUtil.convertObjectToJSonObject(owner);
			
			ServletOutputStream oos = response.getOutputStream();
			oos.write(ServletUtil.serialize(owner));

		} else if (cmd.equals("createchoicepolloption")) {
			PrintWriter output = response.getWriter();
			ChoicePoll poll = (ChoicePoll) persistenceService
					.retrieveAnyEntity(ChoicePoll.class,
							Long.valueOf(request.getParameter("poll_id")));
			persistenceService.createChoicePollOption(poll,
					request.getParameter("description"));
             output.write("ChoicePollOption  Created");
		}
		if (cmd.equals("test")) {
			// PollOwner owner =
			// (PollOwner)persistenceService.retrieveAnyEntity(PollOwner.class,
			// "dadafeyi@chrome.com");
			InterestArea i = new InterestArea();
			i.setId((long) 1);
			List<Participant> lv = persistenceService
					.listParticipantsByInterestArea(i);
			response.setContentType("text/html;charset=UTF-8");
			try (PrintWriter out = response.getWriter()) {
				/*
				 * TODO output your page here. You may use following sample
				 * code.
				 */
				out.println("<!DOCTYPE html>");
				out.println("<html>");
				out.println("<head>");
				out.println("<title>Servlet serverWorks</title>");
				out.println("</head>");
				out.println("<body>");
				out.println("<h1>Servlet serverWorks at " + lv.size()
						+ "sdjbsdjgb" + "</h1>");
				out.println("</body>");
				out.println("</html>");
			}

		} else if (cmd.equals("loadparticipants")) {
			// PollOwner owner =
			// (PollOwner)persistenceService.retrieveAnyEntity(PollOwner.class,
			// "dadafeyi@chrome.com");
			InterestArea i = new InterestArea();
			i.setId(Long.valueOf(request.getParameter("interestid")));
			List<Participant> lv = persistenceService
					.listParticipantsByInterestArea(i);
			ServletOutputStream oos = response.getOutputStream();
			oos.write(ServletUtil.serialize(lv));

		} else if (cmd.equals("login")) {
			String responseLogin = persistenceService.login(
					request.getParameter("phn_email"),
					request.getParameter("password"),
					Integer.valueOf(request.getParameter("usertype")));
			response.setContentType("text/html;charset=UTF-8");
			try (PrintWriter out = response.getWriter()) {

				out.write(responseLogin);
			}
		} else if (cmd.equals("addaffiliates")) {
			PrintWriter out = response.getWriter();
			PollOwner owner = new PollOwner();
			owner.setEmailAddress(request.getParameter("email")); // "dadafeyi@chrome.com"
			int sizeString =persistenceService.addAffiliates(owner, null,
					request.getParameter("filestring"));
			if(sizeString >=1){
				out.write("SUCCESS::"+sizeString+" Affiliates Added/Merged");
			}
			else{
				out.write("ERROR::"+sizeString+" Affiliates Added/Merged, Reason:Users are not Registered");	
			}
			

		} else if (cmd.equalsIgnoreCase(PUSH_STRING)) {
			PrintWriter out = response.getWriter();
			PollContent poll = new PollContent();
			poll.addRegId("jjjjjj");
           //"APA91bGrVQahWoz6RIFMo18cK1QhmxDESzAnuLcuEc-aZ-ZNVfCEDP1rWprkAtcbTzR2NLCzX4gY-FSxCThPxtW1F9snRQK5d0si2FS8RVxUCrdR3rkFUQg2fTFldsEvn7h2X_z-0WWQv2yuHkHFk24i9Z_ysrGNNalTgNOm0iGYeP2A15ILR8w"
			poll.createData("WElcome", "Ijghskjdklnlnvlkn");
			ServletUtil.post(GOOGLE_SERVER_KEY, poll,out);

		} else if (cmd.equalsIgnoreCase(RETRIEVE_SURVEY)) {
			
			PollSurvey poll = persistenceService.retrievePollSurvey(Long
					.valueOf(request.getParameter("poll_id")).longValue());
			ServletOutputStream oos = response.getOutputStream();
			oos.write(ServletUtil.serialize(poll));
		}

		//

		// TODO Implement decision logic calling service methods

	}

	

}
