<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.developerhaus.domain.PageInfo"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %> 
<html>
<head>
	<link rel="stylesheet" type="text/css" href="/haus/styles/common.css" />
	<title>board</title>

		
	<script type="text/javascript">
	
		function goPage(pageNum){
			var form = document.pageForm;
			form.page.value = pageNum;
			form.submit();
		}
	
	</script>
</head>
<form name="pageForm" action="/haus/post/list" method="get">
<input type="hidden" name="page" />
</form>

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
							<c:forEach items="${list}" var="post" varStatus="status">
								<tr>
									<td>${totalCnt - status.index}</td>
									<td><a href="/haus/post/view/${post.postSeq}">${post.title}</a></td>
									<td>${post.regUsr}</td>
									<td>${post.regDt}</td>
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
			<div class="btnr1"><a href="/haus/post/insert">등록</a></div>
		</div>
		<div class="paging">
				<ul>
				<% 
					PageInfo pageInfo = (PageInfo)request.getAttribute("pageInfo");
				
					if(pageInfo.getStartPage() > PageInfo.COUNT_PER_BLOCK) {
				%>
					<li><a href="javascript:goPage(1);"><img src="/haus/images/ico_gofirst.gif" alt="처음으로" /></a></li>
					<li><a href="javascript:goPage(<%=pageInfo.getPreBlockStartPage() %>);"><img src="/haus/images/ico_gopre.gif" alt="이전으로" /></a></li>
				<%	} 
					
					for(int i = pageInfo.getStartPage(); i <= pageInfo.getEndPage(); i++){
				%>
					<li <%if(i == pageInfo.getCurrentPage()){ %> class="sel" <% }%>><a href="javascript:goPage(<%=i %>);"><%=i %></a></li>	
				<%	}
					
					if(pageInfo.getEndPage() < pageInfo.getPageCount()) {
				%>
					<li><a href="javascript:goPage(<%=pageInfo.getNextBlockStartPage() %>);"><img src="/haus/images/ico_gonext.gif" alt="다음으로" /></a></li>
					<li><a href="javascript:goPage(<%=pageInfo.getPageCount() %>);"><img src="/haus/images/ico_golast.gif" alt="마지막으로" /></a></li>
				<%
					}
				%>		
				
				</ul>
			</div>
		<div class="footer"></div>
	</div>
</body>
</html>
