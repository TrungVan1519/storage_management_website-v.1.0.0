<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page isELIgnored="false"%>


<div class="right_col" role="main">
	<div class="">
		<div class="page-title">
			<div class="title_left">
				<h3>Form Action</h3>
			</div>
		</div>

		<div class="clearfix"></div>

		<div class="row">
			<div class="col-md-12 col-sm-12 ">
				<div class="x_panel">

					<div class="x_title">
						<h2>${formTitle }</h2>
						<ul class="nav navbar-right panel_toolbox">
							<li><a class="collapse-link"><i class="fa fa-chevron-up"></i></a></li>
							<li class="dropdown">
								<a href="#" class="dropdown-toggle"
									data-toggle="dropdown" role="button" aria-haspopup="true"
									aria-expanded="false">
									<i class="fa fa-wrench"></i>
								</a>
								<ul class="dropdown-menu" role="menu">
									<li><a class="dropdown-item" href="#">Settings 1</a></li>
									<li><a class="dropdown-item" href="#">Settings 2</a></li>
								</ul>
							</li>
						</ul>
						<div class="clearfix"></div>
					</div>

					<div class="x_content">
						<br />
						<form:form servletRelativeAction="/product-info/save" method="POST" modelAttribute="productInfoForm" 
								class="form-horizontal form-label-left" enctype="multipart/form-data">

							<!-- 
								> Day la 4 field khong cho phep user duoc nhap.
								> Vi the ma ta phai de form:hidden va phai tu dong nhap truoc cho user.
								> Chu y: Do trong DB ta de createdDate va updatedDate la kieu TimeStamp tuong duong voi kieu Date() trong Java,
									nhung form:hidden mac dinh luon tra ve kieu String nen trong @InitBinder ta phai convert tu kieu String
									tra ve tu cac the form:hidden nay sang kieu Date() thi moi co the update trong DB duoc
							-->
							<form:hidden path="id" />
							<form:hidden path="imageUrl"	/>
						 	<form:hidden path="activeFlag" />
							<form:hidden path="createdDate" /> 
							<form:hidden path="updatedDate"	/>
							
							<div class="form-group">
								<label for="category" class="control-label col-md-3 col-sm-3 col-xs-12">
									Category Code<span class="required">*</span>
								</label>
								<div class="col-md-6 col-sm-6 col-xs-12">
									<c:choose>
										<c:when test="${!viewMode }">
											<form:select path="category.id" class="form-control">
												<form:options items="${mapCategory }"/>
											</form:select>
										</c:when>
										<c:otherwise>
											<form:input path="category.name" cssClass="form-control col-md-7 col-xs-12" disabled="${viewMode}"/>									
										</c:otherwise>
									</c:choose>
								</div>
							</div>
							
							<div class="form-group">
								<label for="code" class="control-label col-md-3 col-sm-3 col-xs-12">
									Code <span class="required">*</span>
								</label>
								<div class="col-md-6 col-sm-6 col-xs-12">
									<form:input path="code" cssClass="form-control col-md-7 col-xs-12" disabled="${viewMode}" />
									<div class="has-error">
										<form:errors path="code" cssClass="help-block"></form:errors>
									</div>
								</div>
							</div>
							
							<div class="form-group">
								<label for="name" class="control-label col-md-3 col-sm-3 col-xs-12">
									Name <span class="required">*</span>
								</label>
								<div class="col-md-6 col-sm-6 col-xs-12">
									<form:input path="name" cssClass="form-control col-md-7 col-xs-12" disabled="${viewMode}" />
									<div class="has-error">
										<form:errors path="name" cssClass="help-block"></form:errors>
									</div>
								</div>
							</div>
							
							<div class="form-group">
								<label for="description" class="control-label col-md-3 col-sm-3 col-xs-12">
									Description <span class="required">*</span>
								</label>
								<div class="col-md-6 col-sm-6 col-xs-12">
									<form:textarea path="description" cssClass="form-control col-md-7 col-xs-12" disabled="${viewMode}" />
									<div class="has-error">
										<form:errors path="description" cssClass="help-block"></form:errors>
									</div>
								</div>
							</div>
							
							<c:if test="${!viewMode }">
								<div class="form-group">
									<label for="multipartFile" class="control-label col-md-3 col-sm-3 col-xs-12">
										Image Upload <span class="required">*</span>
									</label>
									<div class="col-md-6 col-sm-6 col-xs-12">
										<form:input path="multipartFile" type="file" cssClass="form-control col-md-7 col-xs-12" />
										<div class="has-error">
											<form:errors path="multipartFile" cssClass="help-block"></form:errors>
										</div>
									</div>
								</div>
							</c:if>
							
							<div class="ln_solid"></div>
							
							<div class="form-group">
								<div class="col-md-6 col-sm-6 col-xs-12 col-md-offset-3">
									<button class="btn btn-primary" type="button" onclick="cancel();">Cancel</button>
									
									<c:if test="${!viewMode }">
										<button class="btn btn-primary" type="reset">Reset</button>
										<button type="submit" class="btn btn-success">Submit</button>
									</c:if>
								</div>
							</div>

						</form:form>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>

<!-- Dung de xu ly cho Button cancel -->
<script type="text/javascript">
	$(document).ready(
			function() {
				$('#categorylistId').addClass('current-page').siblings().removeClass('current-page');
				var parent = $('#categorylistId').parents('li');
				parent.addClass('active').siblings().removeClass('active');
				$('#categorylistId').parents().show();
			});
	function cancel() {
		window.location.href = '<c:url value="/product-info/list"/>'
	}
</script>
