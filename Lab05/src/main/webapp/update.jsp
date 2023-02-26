<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <title>Update Product</title>
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
            <div class="col-md-8">
                <h4 class="text-success">Update Product ${product.name}</h4>
                <form class="mt-3" method="post" action="${pageContext.request.contextPath}/updateProduct">
                    <input type="hidden" name="productId" value="${productId}" />
                    <span>${product.id}</span>
                    <div class="form-group">
                        <label for="product-name">Tên sản phẩm</label>
                        <input class="form-control" type="text" placeholder="Nhập tên sản phẩm" id="product-name" name="name" value="${product.name}">
                    </div>
                    <div class="form-group">
                        <label for="price">Giá sản phẩm</label>
                        <input class="form-control" type="number" placeholder="Nhập giá bán" id="price" name="price" value="${product.price}">
                    </div>
                    <div class="form-group">
                        <button class="btn btn-success mr-2" type="submit">Update</button>
                        <a class="btn btn-danger" href="${pageContext.request.contextPath}/home">Cancel</a>
                    </div>
                </form>
            </div>
        </div>
    </div>
</body>
