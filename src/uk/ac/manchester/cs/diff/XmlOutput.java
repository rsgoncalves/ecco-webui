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
 * Servlet implementation class XmlOutput
 */
@WebServlet("/XmlOutput")
public class XmlOutput extends HttpServlet {
	private static final long serialVersionUID = 4167122952977568297L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			getXml(request, response);
		} catch (TransformerException e) {
			e.printStackTrace();
		}
	}

	
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			getXml(request, response);
		} catch (TransformerException e) {
			e.printStackTrace();
		}
	}

	
	/**
	 * Get XML file
	 * @param request	Http request
	 * @param response	Http response
	 * @throws IOException
	 * @throws ServletException
	 * @throws TransformerException
	 */
	private void getXml(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, TransformerException {
		PrintWriter pw = response.getWriter();
		String id = request.getParameter("uuid").toString();

		HttpSession session = request.getSession();
		Document doc = (Document)session.getAttribute(id);

		if(doc != null) {
			XMLReport report = (XMLReport) session.getAttribute("report");
			String xmlDoc = report.getReportAsString(doc);
			response.setContentType("text/xml");
			pw.println(xmlDoc);
			pw.flush();
			pw.close();
			//			Prompt to download
			//		response.setContentType("application/octet-stream");
			//		response.setHeader("Content-Disposition", "attachment; filename=\"diff.xml\"");
		}
		else
			throw new Error("Your session has expired. This happens after 1 hour of inactivity.");
	}
}
