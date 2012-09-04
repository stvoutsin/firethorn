<%@ taglib prefix="jstl-core"   uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring-tags" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="spring-form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="test"        uri="/WEB-INF/tlds/service-tags.xml"%>
<%@ page
    import="uk.ac.roe.wfau.firethorn.webapp.control.PathBuilder"
    import="uk.ac.roe.wfau.firethorn.webapp.control.ServletPathBuilder"
    import="uk.ac.roe.wfau.firethorn.webapp.widgeon.DataResourceController"
    import="uk.ac.roe.wfau.firethorn.webapp.widgeon.DataResourcesController"
    import="uk.ac.roe.wfau.firethorn.widgeon.DataResource"
    session="true"
%><%
PathBuilder paths = new ServletPathBuilder(
    request
    );

DataResource resource = (DataResource) request.getAttribute(
    DataResourceController.RESOURCE_ENTITY
    ) ;
%>
<html>
    <head>
	    <title></title>
        <link href='/css/page.css' rel='stylesheet' type='text/css'/>
    </head>
    <body>
        <div>
            <span>[<a href='<%= paths.path(DataResourcesController.CONTROLLER_PATH, "search") %>'>search</a>]</span>
            <span>[<a href='<%= paths.path(DataResourcesController.CONTROLLER_PATH, "select") %>'>select</a>]</span>
            <span>[<a href='<%= paths.path(DataResourcesController.CONTROLLER_PATH, "create") %>'>create</a>]</span>
        </div>
        <div>
            Resource
            <div>
                <table border='1'>
                    <tr>
                        <td>Ident</td>
                        <td><%= resource.ident() %></td>
                    </tr>
                    <tr>
                        <td>Name</td>
                        <td><a href='<%= paths.link(resource) %>'><%= resource.name() %></a></td>
                    </tr>
                    <tr>
                        <td>Owner</td>
                        <td><%= resource.owner().name() %></td>
                    </tr>
                    <tr>
                        <td>Type</td>
                        <td><%= resource.getClass().getName() %></td>
                    </tr>
                </table>
            </div>
        </div>
    </body>
</html>

