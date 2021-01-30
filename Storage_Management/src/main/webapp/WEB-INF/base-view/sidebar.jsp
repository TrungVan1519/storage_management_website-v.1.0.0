<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>


<div id="sidebar-menu" class="main_menu_side hidden-print main_menu">
	<div class="menu_section">
		<h3>General</h3>
		<ul class="nav side-menu">
			<c:forEach items="${menuSession }" var="menu">
				<li id="${menu.idMenu }">
					<a><i class="fa fa-home"></i> ${menu.name } <span class="fa fa-chevron-down"></span></a>
					<ul class="nav child_menu">
						<c:forEach items="${menu.childMenus }" var="childMenu">
							<li id="${childMenu.idMenu }">
								<a href="<c:url value="${childMenu.url }"/>" > ${childMenu.name } </a>
							</li>
						</c:forEach>
					</ul>
				</li>
			</c:forEach>
		</ul>
	</div>
</div> 
