package com.karmelos.kpoll.servlet;



import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.HttpRequestHandler;

import com.karmelos.kpoll.model.Participant;
import com.karmelos.kpoll.service.PersistenceService;

/**
 * Servlet implementation class PollHandler
 */
@Component("pollServlet")
public class PollHandler implements HttpRequestHandler {
	public static final Log LOG = LogFactory.getLog(PollHandler.class);

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
	public void handleRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
      String res =persistenceService.registerParticipant(new Participant());
       response.setContentType("text/html;charset=UTF-8");
       try (PrintWriter out = response.getWriter()) {
           /* TODO output your page here. You may use following sample code. */
           out.println("<!DOCTYPE html>");
           out.println("<html>");
           out.println("<head>");
           out.println("<title>Servlet serverWorks</title>");            
           out.println("</head>");
           out.println("<body>");
           out.println("<h1>Servlet serverWorks at "  +"uggyu"+ "</h1>");
           out.println("</body>");
           out.println("</html>");
       }
	//TODO Implement decision logic calling service methods
	}
}
