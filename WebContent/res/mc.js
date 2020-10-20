/**
 * javascript file to do the error-handling on the client-side
 * to avoid putting unnecessary load on the server.
 * When the submit button is pressed, the browser will invoke
 * the validate function in your JavaScript file. 
 * If the function returned true, 
 * the browser will form the query string and submit the form as usual. 
 * Otherwise, the submission is cancelled, i.e. as if the button was not clicked.
 */


function validate() {
	var ok = true;

	var p = document.getElementById("principal").value;
	if (isNaN(p) || p <= 0) {
		alert("Principle invalid!");
		document.getElementById("principal-error").style.display = "inline";
		document.getElementById("principal-error").style.color = "red";
		ok = false;
	} else {
		document.getElementById("principal-error").style.display = "none";
	}
	if (!ok)
		return false;


	p = document.getElementById("interest").value;
	if (isNaN(p) || p <= 0 || p >= 100) {
		alert("Interest Invalid. Must be in (0,100).");
		document.getElementById("interest-error").style.display = "inline";
		document.getElementById("interest-error").style.color = "red";
		ok = false;
	} else {
		document.getElementById("interest-error").style.display = "none";
	}
	if (!ok)
		return false;


	p = document.getElementById("period").value;
	if (isNaN(p) || p <= 0) {
		alert("Period Invalid. Must be in > 0.");
		document.getElementById("period-error").style.display = "inline";
		document.getElementById("period-error").style.color = "red";
		ok = false;
	} else {
		document.getElementById("period-error").style.display = "none";
	}
	if (!ok)
		return false;
	// add your code to to check the load period
	return ok;
}


function doSimpleAjax(address) {
	var request = new XMLHttpRequest();
	var data ='';
	
	/* add your code here to grab all parameters from form */
	data += "principle=" + document.getElementById("principal").value + "&";
	data += "interest=" + document.getElementById("interest").value + "&";
	data += "period=" + document.getElementById("period").value + "&";
	data += "grace=" + document.getElementById("inputGrace").checked + "&"; 
	data += "calculate=true";
	request.open("GET", (address + "?" + data), true); 
	
	request.onreadystatechange = function() {
		handler(request)
		};
		request.send(null);
		}
		

function handler(request) {
	if ((request.readyState == 4) && (request.status == 200)) {
		var target = document.getElementById("result");
		target.innerHTML = request.responseText;
	}
}

