<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %> 
<html>
<head>
	<link rel="stylesheet" type="text/css" href="/css/common.css" />
	<title>board</title>
	<script type="text/javascript">
		function deletePost(postId) {
			var form = document.getElementById("form");
			form.method = "GET";
			form.action = "/haus/board/delete/"+postId;
			//form.submit();
			location.href = "/haus/board/delete/"+postId;
		}
	
	</script>
</head>
<body>
	<form name="form" id="form" action="/haus/board/insert" method="post">
		<input type="hidden" name="regUsr" value="1" />
		<input type="text" name="postId" value="${board.postId}" />
		<table cellpadding="0" cellspacing="0" summary="게시판" class="tbl1">
			<tr>
				<td>제목 : </td>
				<td><input type="text" name="title" value="${board.title}" /></td>
			</tr>
			<tr>
				<td>내용 : </td>
				<td><textarea name="contents" cols="45" rows="4">${board.contents}</textarea></td>
			</tr>
			<tr>
				<td colspan="2" style="text-align:right;"><a href="/haus/board/list">목록으로</a><input type="submit" value="저장" /><input type="button" value="삭제" onclick="deletePost(${board.postId});" /></td>
			</tr>
		</table>
	</form>
</body>
</html>
