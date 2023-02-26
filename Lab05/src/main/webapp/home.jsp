<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <title>Danh sách sản phẩm</title>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.16.0/umd/popper.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
</head>
<body style="background-color: #f8f8f8">
    <div class="container-fluid p-5">
        <div class="row mb-5">
            <div class="col-md-6">
                <h3>Product Management</h3>
            </div>
            <div class="col-md-6 text-right">
                Xin chào <span class="text-danger">${username}</span> | <a href="login.jsp">Logout</a>
            </div>
        </div>
        <div class="row rounded border p-3">
            <div class="col-md-4">
                <h4 class="text-success">Thêm sản phẩm mới</h4>
                <form class="mt-3" method="post" action="home">
                    <div class="form-group">
                        <label for="product-name">Tên sản phẩm</label>
                        <input class="form-control" type="text" placeholder="Nhập tên sản phẩm" id="product-name" name="name">
                    </div>
                    <div class="form-group">
                        <label for="price">Giá sản phẩm</label>
                        <input class="form-control" type="number" placeholder="Nhập giá bán" id="price" name="price">
                    </div>
                    <div class="form-group">
                        <button class="btn btn-success mr-2" type="submit">Thêm sản phẩm</button>
                    </div>
				<%
                  String errorMessage = (String) request.getSession().getAttribute("errorMessage");
                  if (errorMessage != null) {
	             %>
	                  <div class="alert alert-danger" role="alert">
	                      <%= errorMessage %>
	                  </div>
	            <%
	                      request.getSession().removeAttribute("errorMessage");
	                  }
	             %>  
	                     
	            <%
					  String successMessage = (String) request.getSession().getAttribute("successMessage");
					  if (successMessage != null) {
				 %>
					  <div class="alert alert-success" role="alert">
						  <%= successMessage %>
					  </div>
				<%
					     request.getSession().removeAttribute("successMessage");
					  }
				%>
                                  
                </form>
            </div>
            <div class="col-md-8">
                <h4 class="text-success">Danh sách sản phẩm</h4>
                <p>Chọn một sản phẩm cụ thể để xem chi tiết</p>
                <table class="table table-striped">
                    <thead>
                        <tr>
                            <th>STT</th>
                            <th>Tên sản phẩm</th>
                            <th>Giá</th>
                            <th>Thao tác</th>
                        </tr>
                    </thead>
                    <tbody>
                    <c:forEach items="${productList}" var="product">
                        <tr>
                            <td>${product.id}</td>
					        <td>${product.name}</a></td>
					        <td>$${product.price}</td>
					        <td>
                             <a href="${pageContext.request.contextPath}/updateProduct?id=${product.id}">Chỉnh sửa</a>                                           
							<td>
							  <form action="${pageContext.request.contextPath}/deleteProduct" method="post" id="deleteForm${product.id}">
							    <input type="hidden" name="_method" value="DELETE" />
							    <input type="hidden" name="productId" value="${product.id}" />
							    <a href="${pageContext.request.contextPath}/home" onclick="if (confirm('Are you sure you want to delete this product?')) document.getElementById('deleteForm${product.id}').submit(); return false;">Delete</a>
							  </form>
							</td>
                        </tr>
                     </c:forEach>
                        <tr>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</body>
