<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ page isELIgnored="false" %>


<div class="right_col" role="main">
	<div class="">

		<div class="clearfix"></div>
		<div class="col-md-12 col-sm-12 col-xs-12">
			<div class="x_panel">
				<div class="x_title">
					<h2>ProductInfo List</h2>

					<div class="clearfix"></div>
				</div>

				<div class="x_content">
					<div class="container">
						<a href="<c:url value="/product-info/add"/>" class="btn btn-app">
							<i class="fa fa-plus"></i>Add new product info
						</a>
					</div>
					
					<div class="container">
						<form:form id="productInfoSearchForm" modelAttribute="productInfoSearchForm" servletRelativeAction="/product-info/list/" method="POST" class="form-horizontal form-label-left">

							<!--  Do day la Form tim kiem nen khong can cac field nay -->
							<%-- <form:hidden path="id" />
						 	<form:hidden path="activeFlag" />
							<form:hidden path="createdDate" /> 
							<form:hidden path="updatedDate"	/> --%>
							
							<div class="form-group">
								<label for="description" class="control-label col-md-3 col-sm-3 col-xs-12"> ID </label>
								<div class="col-md-6 col-sm-6 col-xs-12">
									<form:input path="id" cssClass="form-control col-md-7 col-xs-12" />
								</div>
							</div>
							
							<div class="form-group">
								<label for="code" class="control-label col-md-3 col-sm-3 col-xs-12"> Code </label>
								<div class="col-md-6 col-sm-6 col-xs-12">
									<form:input path="code" cssClass="form-control col-md-7 col-xs-12" />
								</div>
							</div>
							
							<div class="form-group">
								<label for="name" class="control-label col-md-3 col-sm-3 col-xs-12"> Name </label>
								<div class="col-md-6 col-sm-6 col-xs-12">
									<form:input path="name" cssClass="form-control col-md-7 col-xs-12" />
								</div>
							</div>
							
							<div class="form-group">
								<div class="col-md-6 col-sm-6 col-xs-12 col-md-offset-3">
									<button type="submit" class="btn btn-success">Search</button>
								</div>
							</div>

						</form:form>
					</div>

					<div class="table-responsive">
						<table class="table table-striped jambo_table bulk_action">
							<thead>
								<tr class="headings">
									<th class="column-title">#</th>
									<th class="column-title">ID</th>
									<th class="column-title">Code</th>
									<th class="column-title">Name</th>
									<th class="column-title">Image</th>
									<th class="column-title no-link last text-center" colspan="3">
										<span class="nobr">Action</span>
									</th>
								</tr>
							</thead>

							<tbody>
								<!-- 
									> varStatus="loop" dung de lam id cho vong lap
									> varStatus="loop" bat dau tu 0
								 -->
								<c:forEach items="${productInfos}" var="productInfo" varStatus="loop">

									<c:choose>
										<c:when test="${loop.index % 2 == 0 }">
											<tr class="even pointer">
										</c:when>
										<c:otherwise>
											<tr class="odd pointer">
										</c:otherwise>
									</c:choose>
										<td class=" ">${pageInfo.offset + loop.index + 1}</td>
										<td class=" ">${productInfo.id }</td>
										<td class=" ">${productInfo.code }</td>
										<td class=" ">${productInfo.name }</td>
										<td class=" ">
											<img alt="productInfoImage" src="<c:url value="${productInfo.imageUrl }"/>" width="100px" height="100px">
										</td>
										<td class="text-center">
											<a href="<c:url value="/product-info/view/${productInfo.id }"/>" class="btn btn-round btn-default"> View </a>
										</td>
										<td class="text-center">
											<a href="<c:url value="/product-info/edit/${productInfo.id }"/>" class="btn btn-round btn-primary"> Edit </a>
										</td>
										<td class="text-center">
											<!-- javascript:void(0); co nghia disable chuyen huong cua the <a -->
											<a href="javascript:void(0);" onclick="confirmDelete(${productInfo.id});" class="btn btn-round btn-danger"> Delete </a>
										</td>
									</tr>
									
								</c:forEach>
							</tbody>
						</table>
						
						<jsp:include page="/WEB-INF/base-view/paging.jsp"></jsp:include>
						
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
<script type="text/javascript">
	 function confirmDelete(productInfoId){
		 if(confirm('Do you want delete this record?')){
			 window.location.href = '<c:url value="/product-info/delete/"/>' + productInfoId;
		 }
	 }
	 

	 function gotoPage(page){
		 $('#productInfoSearchForm').attr('action','<c:url value="/product-info/list/"/>' + page);
		 $('#productInfoSearchForm').submit();
	 }
	 
	 $(document).ready(function(){
		 processMessage();
	 });
	 function processMessage(){
		 var msgSuccess = '${msgSuccess}';
		 var msgFailure = '${msgFailure}';
		 
		 if(msgSuccess){
			 new PNotify({
                 title: ' Success',
                 text: msgSuccess,
                 type: 'success',
                 styling: 'bootstrap3'
             });
		 }
		 if(msgFailure){
			 new PNotify({
                 title: ' Failure',
                 text: msgFailure,
                 type: 'error',
                 styling: 'bootstrap3'
             });
		 }
	 }
</script>
