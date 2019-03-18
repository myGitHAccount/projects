<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1" isELIgnored="false"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>


<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Show Accounts Page</title>


<link rel="stylesheet"
	href="https://cdn.datatables.net/1.10.19/css/jquery.dataTables.min.css">

<script src="https://code.jquery.com/jquery-3.3.1.js"></script>
<script
	src="https://cdn.datatables.net/1.10.19/js/jquery.dataTables.min.js">
	
</script>

<script>
	$(document).ready(function() {
		$('#accTable').DataTable({
			//"lengthMenu": [[10, 25, 50, -1], [10, 25, 50, "All"]]
			"sPaginationType" : "full_numbers"
		});
	});
	function confirmDelete() {
		return confirm("Are you sure you want to Delete ?");
	}
	function confirmActivate() {
		return confirm("Are you sure you want to Activate ?");
	}
</script>

</head>
<%@ include file="header-inner.jsp"%><br/>
<body>
	<h2>Application Accounts Details</h2>
	<font color="green">${successMsg }</font>
	
	<font color="red">${failureMsg }</font>
	
	<table border="1" id="accTable">
		<thead>
			<tr>
				<td>SNo</td>
				<td>First Name</td>
				<td>Last Name</td>
				<td>Email</td>
				<td>Role</td>
				<td>Action</td>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${appAccounts}" var="account" varStatus="index">
				<tr>
					<td><c:out value="${index.count}" /></td>
					<td><c:out value="${account.firstName}" /></td>
					<td><c:out value="${account.lastName}" /></td>
					<td><c:out value="${account.email}" /></td>
					<td><c:out value="${account.role}" /></td>
					<td><a href="#">Edit</a> <c:if
							test="${account.activeSw =='Y'}">
							<a href="delete?accId=${account.accId}"
								onclick="return confirmDelete()">Delete</a>
						</c:if> <c:if test="${account.activeSw =='N'}">
							<a href="activate?accId=${account.accId}"
								onclick="return confirmActivate()">Activate</a>
						</c:if></td>
				</tr>
			</c:forEach>
		</tbody>
	</table>

</body>
</html>