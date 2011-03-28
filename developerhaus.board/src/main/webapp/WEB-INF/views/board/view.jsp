<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %> 
<html>
<head>
	<link rel="stylesheet" type="text/css" href="/haus/styles/common.css" />
	<title>board</title>
	<script type="text/javascript">
		function deletePost(postSeq) {
			location.href = "/haus/board/delete/"+postSeq;
		}
	
	</script>
</head>
<body>
	<div class="wrap">
		<div class="head"></div>
		<div class="container">
			<form name="form" id="form" action="/haus/board/insert" method="post">
				<input type="hidden" name="regUsr" value="1" />
				<input type="text" name="postSeq" value="${board.postSeq}" />
				<table cellpadding="0" cellspacing="0" summary="게시판" class="tbl1">
					<tr>
						<th class="ct2">제목 : </th>
						<td class="le"><input type="text" name="title" value="${board.title}" /></td>
					</tr>
					<tr>
						<th class="ct2">내용 : </th>
						<td class="le"><textarea name="contents" cols="45" rows="4">${board.contents}</textarea></td>
					</tr>
				</table>
				
				<div class="btnr1"><a href="/haus/board/list">목록으로</a><input type="submit" value="저장" /><input type="button" value="삭제" onclick="deletePost(${board.postSeq});" /></div>
			</form>
			
		</div>
		<div class="footer"></div>
	</div>
</body>
</html>