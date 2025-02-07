<%@ taglib prefix="jstl-core"   uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring-tags" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="spring-form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="test"        uri="/WEB-INF/tlds/service-tags.xml"%>
<%@ page
    import="uk.ac.roe.wfau.firethorn.webapp.control.PathBuilder"
    import="uk.ac.roe.wfau.firethorn.webapp.control.ServletPathBuilder"

    import="uk.ac.roe.wfau.firethorn.widgeon.jdbc.JdbcResourceBean"
    import="uk.ac.roe.wfau.firethorn.widgeon.jdbc.JdbcResourceController"
    import="uk.ac.roe.wfau.firethorn.widgeon.jdbc.JdbcResourcesController"
    import="uk.ac.roe.wfau.firethorn.widgeon.jdbc.JdbcResourceCatalogsController"

    import="uk.ac.roe.wfau.firethorn.widgeon.jdbc.JdbcCatalogBean"
    import="uk.ac.roe.wfau.firethorn.widgeon.jdbc.JdbcCatalogController"

    session="true"
%><%
PathBuilder paths = new ServletPathBuilder(
    request
    );

JdbcResourceBean resource = (JdbcResourceBean) request.getAttribute(
    JdbcResourceController.RESOURCE_BEAN
    ) ;

String text = (String) request.getAttribute(
    JdbcResourceCatalogsController.SEARCH_TEXT
    );

Iterable<JdbcCatalogBean> catalogs = (Iterable<JdbcCatalogBean>) request.getAttribute(
    JdbcResourceCatalogsController.SEARCH_RESULT
    ) ;

%>
<html>
    <head>
	    <title></title>
        <link href='<%= paths.file("/css/page.css") %>' rel='stylesheet' type='text/css'/>
    </head>
    <body>
        <div>
            JDBC resources
            <span>[<a href='<%= paths.path(JdbcResourcesController.CONTROLLER_PATH, "search") %>'>search</a>]</span>
            <span>[<a href='<%= paths.path(JdbcResourcesController.CONTROLLER_PATH, "select") %>'>select</a>]</span>
            <span>[<a href='<%= paths.path(JdbcResourcesController.CONTROLLER_PATH, "create") %>'>create</a>]</span>
        </div>
        <div>
            <hr/>
        </div>
        <div>
            JDBC resource
            <div>
                <table border='1'>
                    <tr>
                        <td>Name</td>
                        <td><a href='<%= resource.getIdent() %>'><%= resource.getName() %></a></td>
                    </tr>
                    <tr>
                        <td>Created</td>
                        <td><%= resource.getCreated() %></td>
                    </tr>
                    <tr>
                        <td>Modified</td>
                        <td><%= resource.getModified() %></td>
                    </tr>
                </table>
            </div>
        </div>
        <div>
            <hr/>
        </div>
        <div>
            Resource catalogs
            <span>[<a href='<%= resource.getIdent().getPath() %>/catalogs/search'>search</a>]</span>
            <span>[<a href='<%= resource.getIdent().getPath() %>/catalogs/select'>select</a>]</span>
            <span>[<a href='<%= resource.getIdent().getPath() %>/catalogs/create'>create</a>]</span>
        </div>
        <div>
            <hr/>
        </div>
        <div>
            Search for catalogs by name
            <div>
                <form method='GET' action='<%= resource.getIdent().getPath() %>/catalogs/search'>
                    Text <input type='text' name='<%= JdbcResourceCatalogsController.SEARCH_TEXT %>' value='<%= ((text != null) ? text : "" ) %>'/>
                    <input type='submit' value='Go'/>
                </form>
            </div>
        </div>
        <div>
            <table border='1'>
                <%
                if (catalogs != null)
                    {
                    for (JdbcCatalogBean catalog : catalogs)
                        {
                        %>
                        <tr>
                            <td><a href='<%= catalog.getIdent() %>'><%= catalog.getName() %></a></td>
                            <td><%= catalog.getCreated() %></td>
                            <td><%= catalog.getModified() %></td>
                        </tr>
                        <%
                        }
                    }
                %>
            </table>
        </div>
    </body>
</html>

