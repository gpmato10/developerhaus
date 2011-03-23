<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %> 
<html>
<head>
	<link rel="stylesheet" type="text/css" href="/css/common.css" />
	<title>board</title>
</head>
<body>
	<form action="/haus/board/insert" method="post">
		<input type="hidden" name="regUsr" value="1" />
		<table cellpadding="0" cellspacing="0" summary="게시판" class="tbl1">
			<tr>
				<td>제목 : </td>
				<td><input type="text" name="title" value="" /></td>
			</tr>
			<tr>
				<td>내용 : </td>
				<td><textarea name="contents" cols="45" rows="4"></textarea></td>
			</tr>
			<tr>
				<td colspan="2" style="text-align:right;"><input type="submit" value="저장" /></td>
			</tr>
		</table>
	</form>
</body>
</html>
