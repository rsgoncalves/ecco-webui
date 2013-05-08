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
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.transform.TransformerException;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import uk.ac.manchester.cs.diff.output.XMLReport;

/**
 * @author Rafael S. Goncalves <br/>
 * Information Management Group (IMG) <br/>
 * School of Computer Science <br/>
 * University of Manchester <br/>
 */

/**
 * Servlet implementation class WebDiff
 */
@WebServlet("/WebDiff")
public class WebDiff extends HttpServlet {
	private static final long serialVersionUID = -7457650911942029753L;
	private OWLOntology ont1, ont2;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			getDiff(request, response);
		} catch (TransformerException | FileUploadException | OWLOntologyCreationException e) {
			e.printStackTrace();
		}
	}

	
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			getDiff(request, response);
		} catch (TransformerException | OWLOntologyCreationException | FileUploadException e) {
			e.printStackTrace();
		}
	}


	/**
	 * Get the diff for the specified ontologies
	 * @param request	Http request
	 * @param response	Http response
	 * @throws IOException
	 * @throws ServletException
	 * @throws TransformerException
	 * @throws FileUploadException 
	 * @throws OWLOntologyCreationException 
	 */
	private void getDiff(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, TransformerException, OWLOntologyCreationException, FileUploadException {
		PrintWriter pw = response.getWriter();
		String xsltPath = "/usr/share/tomcat7/webapps/diff/xslt_server.xsl";	// Office Dell
//		String xsltPath = "/usr/local/apache-tomcat-7.0.37/webapps/diff/xslt_server.xsl"; // Mac Pro
//		String xsltPath = "/Users/rafa/Documents/PhD/workspace/ecco_web/WebContent/xslt_server.xsl"; // MacBook Pro
		
		EccoRunner runner = new EccoRunner(true, false, true, false, false);
		// Load ontologies
		loadOntologies(pw, request, response, runner);
		// Get diff report 
		XMLReport report = runner.computeDiff(ont1, ont2, false, false, false, xsltPath);

		request.getSession().setAttribute("xsltPath", xsltPath);
		request.getSession().setAttribute("report", report);
		
		Document doc = report.getXMLDocumentReport();
		Document genSymDoc = report.getXMLDocumentReportUsingGenSyms();
		Document labelDoc = report.getXMLDocumentReportUsingLabels();

		// Store XML document in session attribute
		Element root = doc.getElementById("root");
		String uuid = root.getAttribute("uuid");
		request.getSession().setAttribute(uuid, doc);
		request.getSession().setMaxInactiveInterval(3600);

		// Store gensym-based XML document in session attribute
		Element gs_root = genSymDoc.getElementById("root");
		String gs_uuid = gs_root.getAttribute("uuid");
		request.getSession().setAttribute(gs_uuid, genSymDoc);
		
		// Store label-based XML document in session attribute		
		Element lb_root = labelDoc.getElementById("root");
		String lb_uuid = lb_root.getAttribute("uuid");		
		request.getSession().setAttribute(lb_uuid, labelDoc);

		String styledXml = report.getReportAsHTML(labelDoc, xsltPath);

		response.setContentType("text/html");
		pw.println(styledXml);
		pw.flush();
		pw.close();
	}


	/**
	 * Load both ontologies
	 * @param pw	Print writer
	 * @param request	Http request
	 * @param response	Http response
	 * @param runner	Ecco diff runner
	 * @throws IOException
	 * @throws FileUploadException
	 * @throws OWLOntologyCreationException
	 */
	private void loadOntologies(PrintWriter pw, HttpServletRequest request, HttpServletResponse response, EccoRunner runner) 
			throws IOException, FileUploadException, OWLOntologyCreationException {
		// Create a factory for disk-based file items
		FileItemFactory factory = new DiskFileItemFactory();
		// Create a new file upload handler
		ServletFileUpload upload = new ServletFileUpload(factory);
		// Parse the request
		@SuppressWarnings("unchecked")
		List<FileItem> items = upload.parseRequest(request);
		String ont1uri = "", ont2uri = "";
		
		// Process the uploaded items
		Iterator<FileItem> iter = items.iterator();
		while (iter.hasNext()) {
			FileItem item = (FileItem) iter.next();
			String name = item.getFieldName();
			// Load from text area content
			if (item.isFormField()) {
				if(name.equals("o1")) {
					ont1uri = item.getString();
					if(!ont1uri.equals(""))
						ont1 = runner.loadOntology(1, ont1uri.trim(), false);
				}
				else if(name.equals("o2")) {
					ont2uri = item.getString();
					if(!ont2uri.equals(""))
						ont2 = runner.loadOntology(2, ont2uri.trim(), false);
				}
			} 
			// Load from uploaded file
			else {
				if(name.equals("o1file")) {
					InputStream file1Stream = item.getInputStream();
					if(file1Stream != null && file1Stream.available() != 0)
						ont1 = runner.loadOntology(1, file1Stream, false);
				}
				else if(name.equals("o2file")) {
					InputStream file2Stream = item.getInputStream();
					if(file2Stream != null  && file2Stream.available() != 0)
						ont2 = runner.loadOntology(2, file2Stream, false);
				}
			}
		}	
	}
}
