<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %> 
<html>
<head>
	<link rel="stylesheet" type="text/css" href="/haus/styles/common.css" />
	<title>post</title>
</head>
<body>
	<div class="wrap">
		<div class="head"></div>
		<div class="container">
<!--		enctype="multipart/form-data"-->
			<form action="/haus/post/insert" method="post" enctype="multipart/form-data"> 
				<input type="hidden" name="regUsr" value="1" />
				<table cellpadding="0" cellspacing="0" summary="게시판" class="tbl1">
					<tr>
						<th class="ct2">제목 : </th>
						<td class="le"><input type="text" name="title" value=""  class="txt5" /></td>
					</tr>
					<tr>
						<th class="ct2">내용 : </th>
						<td class="le"><textarea name="contents" cols="70" rows="4"></textarea></td>
					</tr>
					<tr>
						<th class="ct2">첨부파일 : </th>
						<td class="le"><input type="file" name="uploadedFiles" id="file" class="txt5"/></td>
					</tr>
				</table>
				<div class="btnr1"><a href="/haus/post/list">목록으로</a><input type="submit" value="저장" /></div>
			</form>
		</div>
		<div class="footer"></div>
	</div>
</body>
</html>
