<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
    <title>Liste des Départements</title>
</head>

<body>
    <h1>Départements</h1>
    <a href="/departments/add">Ajouter</a>
    
    <table border="1">
        <tr>
            <th>ID</th>
            <th>Nom</th>
            <th>Description</th>
            <th>Actions</th>
        </tr>
        
        <c:forEach items="${departments}" var="dept">
            <tr>
                <td>${dept.id}</td>
                <td>${dept.name}</td>
                <td>${dept.description}</td>
                <td>
                    <a href="/departments/edit/${dept.id}">Éditer</a>
                    <a href="/departments/delete/${dept.id}" 
                       onclick="return confirm('Êtes-vous sûr?')">Supprimer</a>
                </td>
            </tr>
        </c:forEach>
    </table>
</body>
</html>