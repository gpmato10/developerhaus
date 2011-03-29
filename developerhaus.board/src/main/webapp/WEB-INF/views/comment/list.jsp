<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %> 
<html>
<head>
	<link rel="stylesheet" type="text/css" href="/haus/styles/common.css" />
	<title>board</title>
</head>
<body>
	<div class="wrap">
		<div class="head"></div>
		<div class="container">
			
			<table cellpadding="0" cellspacing="0" summary="게시판" class="tbl1">
				<colgroup>
					<col width="10%" />
					<col width="70%" />
					<col width="10%" />
					<col width="10%" />
				</colgroup>
				<caption>댓글목록</caption>
				<thead>
					<tr>
						<th scope="col">순번</th>
						<th scope="col">제목</th>
						<th scope="col">글쓴이</th>
						<th scope="col">등록일자</th>
					</tr>
				</thead>
				<tbody>
					<c:set var="totalCnt" value="${fn:length(list)}" />
					<c:choose>
						<c:when test="${totalCnt > 0}">
							<c:forEach items="${list}" var="comment" varStatus="status">
								<tr>
									
									<td>${totalCnt - status.index}</td>
									<td><a href="/haus/post/view/${comment.commentSeq}">${comment.comment}</a></td>
									<td>${comment.regUsr}</td>
									<td>${comment.regDt}
									<form action="/haus/comment/delete" method="post">
									<input type="hidden" name="postSeq" value="${comment.postSeq}" />
									<input type="hidden" name="commentSeq" value="${comment.commentSeq}" />
									<input type="hidden" name="regUsr" value="${comment.regUsr}" />
									<input type="submit" value="삭제" />
									</form>
									</td>
								</tr>
							</c:forEach>
						</c:when>
						<c:otherwise>
							<tr>
								<td colspan="4">등록된 게시물이 없습니다.</td>
							</tr>
						</c:otherwise>
					</c:choose>
				</tbody>
			</table>
			<div class="container">
				<form action="/haus/comment/insert" method="post">
					<input type="hidden" name="postSeq" value="${postSeq}" />
					<input type="hidden" name="regUsr" value="1" />
					<table cellpadding="0" cellspacing="0" summary="게시판" class="tbl1">
						<tr>
							<th class="ct2">내용 : </th>
							<td class="le"><textarea name="comment" cols="45" rows="4"></textarea></td>
						</tr>
					</table>
					<div class="btnr1"><input type="submit" value="저장" /></div>
				</form>
			</div>
		</div>
		<div class="footer"></div>
	</div>
</body>
</html>
