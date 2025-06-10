<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Update Material Detail</title>
    <link rel="stylesheet" href="css/updateMaterialDetail.css">
</head>
<body>
    <div class="container">
        <h1>UPDATE MATERIAL DETAIL</h1>
        <form action="updateMaterialServlet" method="post" enctype="multipart/form-data">
            <div class="form-row">
                <div class="form-group">
                    <label for="materialId">Enter ID:</label>
                    <input type="text" id="materialId" name="materialId">

                    <label for="image">Upload Image:</label>
                    <input type="file" id="image" name="image">

                    <label for="name">Name:</label>
                    <input type="text" id="name" name="name">

                    <label for="category">Category:</label>
                    <select id="category" name="category">
                        <option value="">Select Category</option>

                    </select>
                </div>

                <div class="form-group">
                    <label for="supplier">Supplier:</label>
                    <select id="supplier" name="supplier">
                        <option value="">Select Supplier</option>

                    </select>

                    <label for="quantity">Quantity:</label>
                    <input type="number" id="quantity" name="quantity">

                    <label for="unit">Unit:</label>
                    <select id="unit" name="unit">
                        <option value="">Select Unit</option>

                    </select>

                    <label for="status">Status:</label>
                    <select id="status" name="status">
                        <option value="">Select Status</option>

                    </select>
                </div>
            </div>

            <div class="description-group">
                <label for="description">Description:</label>
                <textarea id="description" name="description" rows="10"></textarea>
            </div>

            <div class="submit-group">
                <button type="submit">Submit</button>
            </div>
        </form>
    </div>
</body>
</html>
