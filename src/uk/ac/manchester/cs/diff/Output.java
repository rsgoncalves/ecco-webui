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

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.transform.TransformerException;

import org.w3c.dom.Document;

import uk.ac.manchester.cs.diff.output.XMLReport;

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
		} catch (TransformerException e) {
			e.printStackTrace();
		}
	}

	
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			getOutput(request, response);
		} catch (TransformerException e) {
			e.printStackTrace();
		}
	}

	
	/**
	 * Toggle between gen syms and URI fragments
	 * @param request	Http request
	 * @param response	Http response
	 * @throws IOException
	 * @throws ServletException
	 * @throws TransformerException
	 */
	private void getOutput(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, TransformerException {
		HttpSession session = request.getSession();
		String xsltPath = (String) session.getAttribute("xsltPath");
		XMLReport report = (XMLReport) session.getAttribute("report");
		
		PrintWriter pw = response.getWriter();
		String id = request.getParameter("uuid").toString();
		String genSyms = request.getParameter("gensyms");

		boolean showGenSyms = false, showLabels = false, showNames = false;
		if(genSyms.contains("GenSyms"))
			showGenSyms = true;
		else if(genSyms.contains("Names"))
			showNames = true;
		else if(genSyms.contains("Labels"))
			showLabels = true;

		String styledXml = "";
		
		if(showGenSyms) {
			String gs_id = "";
			if(!id.endsWith("-gs")) {
				if(id.endsWith("-lbl")) 
					gs_id = id.replace("-lbl", "-gs");
				else
					gs_id = id + "-gs";
			}
			else gs_id = id;
			
			Document doc = (Document)session.getAttribute(gs_id);
			if(doc != null)
				styledXml = report.getReportAsHTML(doc, xsltPath);
			else
				throw new Error("Your session has expired. This happens after 1 hour of inactivity.");
		}
		else if(showLabels) {
			String lb_id = "";
			
			if(!id.endsWith("-lbl")) {
				if(id.endsWith("-gs"))
					lb_id = id.replace("-gs", "-lbl");
				else
					lb_id = id + "-lbl";
			}
			else lb_id = id;
			
			Document doc = (Document)session.getAttribute(lb_id);
			if(doc != null)
				styledXml = report.getReportAsHTML(doc, xsltPath);
			else
				throw new Error("Your session has expired. This happens after 1 hour of inactivity.");
		}
		else if(showNames) {
			if(id.endsWith("-gs")) 
				id = id.substring(0, id.length()-3);
			else if(id.endsWith("-lbl"))
				id = id.substring(0, id.length()-4);
			
			Document doc = (Document)session.getAttribute(id);

			if(doc != null)
				styledXml = report.getReportAsHTML(doc, xsltPath);
			else
				throw new Error("Your session has expired. This happens after 1 hour of inactivity.");
		}
		else 
			throw new Error("Document not retrieved properly.");

		response.setContentType("text/html");
		pw.println(styledXml);
		pw.flush();
		pw.close();
	}
}
