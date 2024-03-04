<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html lang="en">

<head>

    <meta charset="UTF-8">

    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <title>Next Page</title>

</head>

<body>

<%

    // Check if form data is submitted through POST

    if ("POST".equalsIgnoreCase(request.getMethod())) {

        // Retrieve data from the POST request

        String CheckSumVal = request.getParameter("CheckSumVal");

        String MandateRespDoc = request.getParameter("MandateRespDoc");

        // Display the retrieved data

%>

<h2>Data submitted from the form:</h2>

<%--<p>Checksum: <%= CheckSumVal %></p>--%>

<%--<p>:MandateRespDoc <%= MandateRespDoc %></p>--%>
<form id="PostToMerchant" name="PostToMerchant" action="" method="POST">
    <label>CheckSumVal:</label>  <input type="text" ID="CheckSumVal" name="CheckSumVal" value=<%= CheckSumVal %>>
    <label>MandateRespDoc:</label><input type="text" ID="MandateRespDoc" name="MandateRespDoc" value=<%= MandateRespDoc %>>
</form>

<%

} else {

%>

<p>No data found.</p>

<%

    }

%>

</body>

</html>