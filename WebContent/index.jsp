<%@ page import="org.semanticweb.owlapi.util.VersionInfo"%>
<%@ page import="uk.ac.manchester.cs.diff.WebDiff"%>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<html>
<head>
<title>ecco</title>
<meta http-equiv="Cache-Control" content="no-cache, no-store, must-revalidate" />
<meta http-equiv="Pragma" content="no-cache" />
<meta http-equiv="Expires" content="0" />
<link rel="stylesheet" href="css/style.css" />
<link rel="stylesheet" href="css/reveal.css"/>
<script type="text/javascript" src="js/jquery-1.4.4.js"></script>
<script type="text/javascript" src="js/jquery-ui-1.8.12.custom.min.js"></script>
<script type="text/javascript" src="js/script.js"></script>
<script type="text/javascript" src="js/jquery.checkboxtree.js"></script>
<script type="text/javascript" src="js/jquery.reveal.js"></script>
<script type="text/javascript" src="js/jscript.js"></script>
<script type="text/javascript">
 	window.onunload=function(){
		document.getElementById('loader').style.display='none';
		document.getElementById('compute').value='compute diff';
		document.getElementById('compute').disabled=false;
	};
	function jumpToIt(frm) {
		var newPage = frm.url.options[frm.url.selectedIndex].value;
		if (newPage != "None") {
			location.href = newPage;
		}
	}
</script>
</head>
<body>
	<div class="content">
		<h1>&nbsp;</h1>
		<h1><i>ecco</i> a diff tool for OWL ontologies</h1>
		<form action="diff" method="post" enctype="multipart/form-data" id="diffargs">
			<div class="box">
				<!-- Try out some pre-computed examples:
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
				<input type=button value="Go!" onClick="jumpToIt(this.form)"><br/> -->
				<p>
					Browse for ontology <b>files</b>, or enter a <b>URL</b> for
					each ontology into the text areas:<br />
				</p>
				<h3>Ontology 1</h3>
				<label> 
					<textarea rows="5" cols="65" name="o1" style="resize:vertical"></textarea>
				</label> 
				<label> <br/> 
					<input type="file" name="o1file">
				</label>
				<h3>Ontology 2</h3>
				<label> 
					<textarea rows="5" cols="65" name="o2" style="resize:vertical"></textarea>
				</label>
				<label><br/>
					<input type="file" name="o2file">
				</label>
				<br/><br/>
				<h4>Mechanism for detecting affected classes (hover for description):</h4>
				<input type="radio" name="cdiff" value="at" checked><a onmouseout="tooltip.hide();" 
					onmouseover="tooltip.show('Computes which atomic classes gained or lost atomic super/sub-classes, by comparing inferred class hierarchies. Feasible for small to large size input over the Web (computation time: fast)');">atomic</a> 
				<input type="radio" name="cdiff" value="sub"><a onmouseout="tooltip.hide();" 
					onmouseover="tooltip.show('Computes which atomic classes gained or lost super/sub-class expressions asserted in either ontology. Feasible for small to medium size input over the Web (computation time: medium)');">subconcepts</a>
				<input type="radio" name="cdiff" value="cvs"><a onmouseout="tooltip.hide();" 
					onmouseover="tooltip.show('Computes which atomic classes gained or lost super/sub-class expressions formed using some ALC constructors over *atomic classes*. Feasible for small input over the Web (computation time: slow)');">contentcvs grammar</a> 
				<input type="radio" name="cdiff" value="gr"><a onmouseout="tooltip.hide();" 
					onmouseover="tooltip.show('Computes which atomic classes gained or lost super/sub-class expressions formed using ALC constructors over *asserted class expressions*. Feasible for small input over the Web (computation time: slower than contentcvs)');">extended grammar</a>
				<br/>
				<br/>
				<p><input type="submit" value="compute diff" id="compute" style="width:120px;"
					onclick="document.getElementById('loader').style.display='block';this.value='computing...';this.disabled=true;this.form.submit();">
				<img id="loader" src="images/loader1.gif" style="display: none;"/></p>
			</div>
		</form>
	</div>
	<div class="box">
		<small>
			<i>ecco</i> accepts ontologies in RDF/XML, OWL/XML, Functional, Manchester, OBO, or KRSS syntaxes.<br/><br/>
			Imported ontologies must have an accessible IRI, e.g., if an imported ontology is in the local file<br/>
			system, it should have an IRI such as: "file:/users/me/ontologies/importedOntology.owl".<br/><br/>
			Get the latest desktop version of <i>ecco</i> from <a href="https://github.com/rsgoncalves/ecco" target="_blank">here</a>. 
			Code for this web app is hosted <a href="https://github.com/rsgoncalves/ecco-webui" target="_blank">here</a>.
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
