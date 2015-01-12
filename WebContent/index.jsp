<%@ page import="org.semanticweb.owlapi.util.VersionInfo" %>
<%@ page import="uk.ac.manchester.cs.diff.WebDiff" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
	<head>
		<title>ecco</title>
		<meta http-equiv="Cache-Control" content="no-cache, no-store, must-revalidate" />
		<meta http-equiv="Pragma" content="no-cache" />
		<meta http-equiv="Expires" content="0" />
		<link rel="stylesheet" href="css/style.css" />
		<link rel="stylesheet" href="css/reveal.css"/>
		<link rel="icon" type="image/png" href="images/favicon.png"/>
		<script type="text/javascript" src="js/jquery-1.4.4.js"></script>
		<script type="text/javascript" src="js/jquery-ui-1.8.12.custom.min.js"></script>
		<script type="text/javascript" src="js/script.js"></script>
		<script type="text/javascript" src="js/jquery.checkboxtree.js"></script>
		<script type="text/javascript" src="js/jquery.reveal.js"></script>
		<script type="text/javascript" src="js/jscript.js"></script>
		<script type="text/javascript">
 			window.onunload=function() {
				document.getElementById('loader').style.display='none';
				document.getElementById('compute').value='compute diff';
				document.getElementById('compute').disabled=false;
			};
			function jumpToIt(frm) {
				var newPage = frm.url.options[frm.url.selectedIndex].value;
				if(newPage != "None")
					location.href = newPage;
			}
		</script>
	</head>
	<body>
		<div class="content">
			<h1>&nbsp;</h1>
			<h1><i>ecco</i>: a diff tool for OWL ontologies</h1>
			<form action="diff" method="post" enctype="multipart/form-data" id="diffargs">
				<div class="box">
					Try out pre-computed examples:
					<select name="url">
						<option value="examples/align_at/index_labels.html">Example 1 [At]</option>
						<option value="examples/align_sub/index_labels.html">Example 1 [Sub]</option>
						<option value="examples/align_gr/index_labels.html">Example 1 [Gr]</option>
						<option value="examples/axiomdiff_at/index_labels.html">Example 2 [At]</option>
						<option value="examples/axiomdiff_sub/index_labels.html">Example 2 [Sub]</option>
						<option value="examples/axiomdiff_gr/index_labels.html">Example 2 [Gr]</option>
						<option value="examples/conceptdiff_at/index_labels.html">Example 3 [At]</option>
						<option value="examples/conceptdiff_sub/index_labels.html">Example 3 [Sub]</option>
						<option value="examples/conceptdiff_gr/index_labels.html">Example 3 [Gr]</option>
						<option value="examples/ncit_06.09_at/index_labels.html">NCI Thesaurus 06.09 [At]</option>
						<option value="examples/ncit_06.09_sub/index_labels.html">NCI Thesaurus 06.09 [Sub]</option>
						<option value="examples/ncit_10.10_at/index_labels.html">NCI Thesaurus 10.10 [At] - HermiT Perf. Boost</option>
						<option value="examples/ncit_13.12_at/index_labels.html">NCI Thesaurus 13.12 [At]</option>
						<option value="examples/ncit_13.12_sub/index_labels.html">NCI Thesaurus 13.12 [Sub]</option>
						<option value="examples/ncit_14.01_at/index_labels.html">NCI Thesaurus 14.01 [At]</option>
						<option value="examples/ncit_14.01_sub/index_labels.html">NCI Thesaurus 14.01 [Sub]</option>
					</select> 
					<input type=button value="Go" onClick="jumpToIt(this.form)"><br/>
					<p>
						Submit ontology <b>files</b>, or paste a <b>URL</b> or the <b>file contents</b> of
						each ontology below:<br/><br/>
					</p>
					<h3>Ontology 1</h3>
					<label> 
						<textarea rows="3" cols="100" name="o1" style="resize:vertical"></textarea>
					</label> 
					<label> <br/> 
						<input type="file" name="o1file">
					</label>
					<br/><br/>
					<h3>Ontology 2</h3>
					<label> 
						<textarea rows="3" cols="100" name="o2" style="resize:vertical"></textarea>
					</label>
					<label><br/>
						<input type="file" name="o2file">
					</label>
					<br/><br/><br/>
					<h4 style="display:inline">Load imported ontologies: </h4><input type="radio" name="processImports" value="True" checked>yes <input type="radio" name="processImports" value="False">no
					<br/>
					<h4>Mechanism for detecting affected classes (hover for description):</h4>
					<input type="radio" name="cdiff" value="at" checked><a onmouseout="tooltip.hide();" 
						onmouseover="tooltip.show('Computes which atomic classes gained or lost atomic super/sub-classes, by comparing inferred class hierarchies. Feasible for small to large size input over the Web (computation time: fast)');">atomic</a> 
					<input type="radio" name="cdiff" value="sub"><a onmouseout="tooltip.hide();" 
						onmouseover="tooltip.show('Computes which atomic classes gained or lost super/sub-class expressions asserted in either ontology. Feasible for small to medium size input over the Web (computation time: medium)');">subconcepts</a>
					<input type="radio" name="cdiff" value="cvs"><a onmouseout="tooltip.hide();" 
						onmouseover="tooltip.show('Computes which atomic classes gained or lost super/sub-class expressions formed using some ALC constructors over *atomic classes*. Feasible for small input over the Web (computation time: slow)');">contentcvs grammar</a> 
					<input type="radio" name="cdiff" value="gr"><a onmouseout="tooltip.hide();" 
						onmouseover="tooltip.show('Computes which atomic classes gained or lost super/sub-class expressions formed using ALC constructors over *asserted class expressions*. Feasible for small input over the Web (computation time: slower than contentcvs)');">extended grammar</a>
					<br/><br/>
					<p><input type="submit" value="compute diff" id="compute" style="width:120px;"
						onclick="document.getElementById('loader').style.display='block';this.value='computing...';this.disabled=true;this.form.submit();">
					<img id="loader" src="images/loader1.gif" style="display: none;"/></p>
				</div>
			</form>
		</div>
		<div class="box">
			<small> <i>ecco</i> accepts ontologies in RDF/XML, OWL/XML, Functional, Manchester, OBO, or KRSS syntaxes. Imported<br/>
				ontologies must have an accessible IRI, e.g., if an imported ontology is in the local file system, it should have<br/>
				an IRI such as: "file:/users/me/ontologies/importedOntology.owl".<br/>
				<br/>Get the latest desktop version of <i>ecco</i> from <a href="https://github.com/rsgoncalves/ecco" target="_blank">here</a>.
				The code for this web application is hosted <a href="https://github.com/rsgoncalves/ecco-webui" target="_blank">here</a>.
			</small>
			<p>
				<small>Powered by the <a href="http://owlapi.sourceforge.net/" target="_blank">OWL API</a> 
				<%
					String version = VersionInfo.getVersionInfo().getVersion().trim();
					out.print("v" + version + ".");
				%>
				</small>
			</p>
		</div>
	</body>
</html>
