/*******************************************************************************
 * This file is part of ecco.
 * 
 * ecco is distributed under the terms of the GNU Lesser General Public License (LGPL), Version 3.0.
 * 
 * Copyright 2011-2013, The University of Manchester
 * 
 * ecco is free software: you can redistribute it and/or modify it under the terms of the GNU Lesser
 * General Public License as published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * ecco is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even 
 * the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser 
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License along with ecco.
 * If not, see http://www.gnu.org/licenses/.
 ******************************************************************************/

package uk.ac.manchester.cs.diff;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.transform.TransformerException;

import org.w3c.dom.Document;

import uk.ac.manchester.cs.diff.output.xml.XMLUnifiedReport;


/**
 * @author Rafael S. Goncalves <br/>
 * Information Management Group (IMG) <br/>
 * School of Computer Science <br/>
 * University of Manchester <br/>
 */

/**
 * Servlet implementation class Output
 * Handles requests for outputting GenSyms or URI fragments
 */
@WebServlet("/GenSyms")
public class Output extends HttpServlet {
	private static final long serialVersionUID = -9167426489501269875L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			getOutput(request, response);
		} catch (TransformerException | ServletException e) {
			e.printStackTrace();
		}
	}


	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			getOutput(request, response);
		} catch (TransformerException | ServletException e) {
			e.printStackTrace();
		}
	}


	/**
	 * Toggle between gensyms, labels and entity names
	 * @param request	Http request
	 * @param response	Http response
	 * @throws IOException
	 * @throws ServletException
	 * @throws TransformerException
	 * @throws EccoException 
	 */
	private void getOutput(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, TransformerException {
		HttpSession session = request.getSession();
		PrintWriter pw = response.getWriter();
		String xsltPath = (String) session.getAttribute("xsltPath");
		XMLUnifiedReport report = (XMLUnifiedReport) session.getAttribute("report");
		
		String htmlOutput = "";
		if(report != null && request.getParameter("uuid") != null) {
			String id = request.getParameter("uuid").toString();
			String genSyms = request.getParameter("gensyms");
			if(genSyms.contains("GenSyms")) {
				Document doc = getGensymsDocument(session, id);
				if(doc != null) {
					htmlOutput = report.getReportAsHTML(doc, xsltPath);
					session.setAttribute("curuuid", doc.getElementById("root").getAttribute("uuid"));
				}
			}
			else if(genSyms.contains("Labels")) {
				Document doc = getLabelsDocument(session, id);
				if(doc != null) {
					htmlOutput = report.getReportAsHTML(doc, xsltPath);
					session.setAttribute("curuuid", doc.getElementById("root").getAttribute("uuid"));
				}
			}
			else if(genSyms.contains("Names")) {
				Document doc = getNamesDocument(session, id);
				if(doc != null) {
					htmlOutput = report.getReportAsHTML(doc, xsltPath);
					session.setAttribute("curuuid", doc.getElementById("root").getAttribute("uuid"));
				}
			}
		}
		else if(report != null) {
			String currentId = (String) request.getSession().getAttribute("curuuid");
			Document doc = (Document) request.getSession().getAttribute(currentId);
			htmlOutput = report.getReportAsHTML(doc, xsltPath);
		}
		else { 
			RequestDispatcher view = getServletContext().getRequestDispatcher("/index.jsp");
			try {
				view.forward(request, response);
			} catch (ServletException e) {
				e.printStackTrace();
			}
		}
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");
		pw.println(htmlOutput);
		pw.flush();
		pw.close();
	}
	
	
	/**
	 * Get the entity name-based XML document stored as a session attribute
	 * @param session	Http session
	 * @param id	uuid
	 * @return Name-based XML document
	 */
	private Document getNamesDocument(HttpSession session, String id) {
		if(id.endsWith("-gs")) 
			id = id.substring(0, id.length()-3);
		else if(id.endsWith("-lbl"))
			id = id.substring(0, id.length()-4);
		return (Document)session.getAttribute(id);
	}
	
	
	/**
	 * Get the Gensym-based XML document stored as a session attribute
	 * @param session	Http session
	 * @param id	uuid
	 * @return Gensym-based XML document
	 */
	private Document getGensymsDocument(HttpSession session, String id) {
		String gs_id = "";
		if(!id.endsWith("-gs")) {
			if(id.endsWith("-lbl")) 
				gs_id = id.replace("-lbl", "-gs");
			else
				gs_id = id + "-gs";
		}
		else gs_id = id;
		return (Document)session.getAttribute(gs_id);
	}
	
	
	/**
	 * Get the Label-based XML document stored as a session attribute
	 * @param session	Http session
	 * @param id	uuid
	 * @return Label-based XML document
	 */
	private Document getLabelsDocument(HttpSession session, String id) {
		String lb_id = "";
		if(!id.endsWith("-lbl")) {
			if(id.endsWith("-gs"))
				lb_id = id.replace("-gs", "-lbl");
			else
				lb_id = id + "-lbl";
		}
		else lb_id = id;
		return (Document)session.getAttribute(lb_id);
	}
}
