<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
<head>
    <title>Formulaire Département</title>
</head>
<body>
    <h1>${department.id == null ? 'Ajouter' : 'Éditer'} Département</h1>
    
    <form:form action="/departments/save" method="post" modelAttribute="department">
        <form:hidden path="id"/>
        
        <div>
            <label>Nom:</label>
            <form:input path="name" required="true"/>
        </div>
        
        <div>
            <label>Description:</label>
            <form:textarea path="description"/>
        </div>
        
        <button type="submit">Sauvegarder</button>
        <a href="/departments">Annuler</a>
    </form:form>
</body>
</html>