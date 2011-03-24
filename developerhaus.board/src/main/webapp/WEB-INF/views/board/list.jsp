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
					<col width="50%" />
					<col width="20%" />
					<col width="20%" />
				</colgroup>
				<caption>게시판</caption>
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
							<c:forEach items="${list}" var="board" varStatus="status">
								<tr>
									<td>${totalCnt - status.index}</td>
									<td><a href="/haus/board/view/${board.postSeq}">${board.title}</a></td>
									<td>${board.regUsr}</td>
									<td>${board.regDt}</td>
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
			<div class="btnr1"><a href="/haus/board/insert">등록</a></div>
		</div>
		<div class="footer"></div>
	</div>
</body>
</html>
