<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<html>
    <head>
    </head>
    <body>
        <h3>Enter the IP to be traced</h3>
        <form:form method="POST"
          action="/challenge/ip/trace" modelAttribute="ip">
             <table>
                <tr>
                    <td><form:label path="ip">Ip</form:label></td>
                    <td><form:input path="ip"/></td>
                </tr>
                <tr>
                    <td><input type="submit" value="Submit"/></td>
                </tr>
            </table>
        </form:form>
    </body>
</html>