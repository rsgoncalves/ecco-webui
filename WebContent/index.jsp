<%@ page import="org.semanticweb.owlapi.util.VersionInfo"%>
<%@ page import="uk.ac.manchester.cs.diff.WebDiff"%>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<html>
<head>
<title>ecco</title>
<link rel="stylesheet" href="css/style.css" />
<script type="text/javascript">
	function JumpToIt(frm) {
	    var newPage = frm.url.options[frm.url.selectedIndex].value;
    	if (newPage != "None") {
	        location.href=newPage;
	    }
	}
</script>
</head>
<body>
	<div class="content">
		<h1>&nbsp;</h1>
		<h1><i>ecco</i> a diff tool for OWL ontologies</h1>
		<form action="diff" method="post" enctype="multipart/form-data">
			<div class="box">
				Try out some pre-computed examples:
				<form>
					<select name="url">
						<option value="examples/ncit_05.07/index_labels.html">NCI Thesaurus 05.07</option>
						<option value="examples/ncit_06.04/index_labels.html">NCI Thesaurus 06.04</option>
						<option value="examples/ncit_07.12/index_labels.html">NCI Thesaurus 07.12</option>
						<option value="examples/ncit_12.03/index_labels.html">NCI Thesaurus 12.03</option>
						<option value="examples/ncit_12.07/index_labels.html">NCI Thesaurus 12.07</option>
						<option value="examples/ncit_13.01/index_labels.html">NCI Thesaurus 13.01</option>
						<option value="examples/ncit_13.04/index_labels.html">NCI Thesaurus 13.04</option>
						<option value="examples/artificial/index_labels.html">Artificial</option>
						<option value="examples/tdiff/index_labels.html">Totally different</option>
					</select> 
					<input type=button value="Go!" onClick="JumpToIt(this.form)">
				</form>
				<br/>
				<p>
					Browse for ontology <b>files</b>, or enter a <b>URL</b> for
					each ontology into the text areas:<br />
				</p>
				<h3>Ontology 1</h3>
				<label> 
					<textarea rows="5" cols="65" name="o1"></textarea>
				</label> 
				<label> <br/> 
					<input type="file" name="o1file">
				</label>
				<h3>Ontology 2</h3>
				<label> 
					<textarea rows="5" cols="65" name="o2"></textarea>
				</label>
				<label><br/>
					<input type="file" name="o2file">
				</label>
				<br/>
				<p><input type="submit" value="Execute Diff"></p>
			</div>
		</form>
	</div>
	<div class="box">
		<small>
			<i>ecco</i> accepts ontologies in RDF/XML, OWL/XML, Functional, Manchester, OBO, or KRSS syntaxes.<br/><br/>
			Imported ontologies must have an accessible IRI, i.e., if an imported ontology is in the local file<br/>
			system, it should have an IRI such as: "file:/users/me/ontologies/importedOntology.owl".<br/><br/>
			Get the latest desktop version of <i>ecco</i> from <a href="https://github.com/rsgoncalves/ecco" target="_blank">here</a>.
		</small>
		<p>
			<small>Powered by <a href="http://owlapi.sourceforge.net/" target="_blank">The OWL API</a> 
			<%
				String version = VersionInfo.getVersionInfo().getVersion().trim();
				out.print("v" + version);
			%>
			</small>
		</p>
	</div>
</body>
</html>
