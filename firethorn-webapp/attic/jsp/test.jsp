<%@ taglib prefix="jstl-core"   uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring-tags" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="spring-form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="test"        uri="/WEB-INF/tlds/test-tags.xml"%>
<%@ page
    import="uk.ac.roe.wfau.firethorn.webapp.control.ControllerData"
    session="true"
%><%

ControllerData data = (ControllerData) request.getAttribute(
    ControllerData.MODEL_ATTRIB
    ) ;

%>
<html>
    <head>
	    <title></title>
        <link href='/css/page.css' rel='stylesheet' type='text/css'/>
    </head>
    <body>
        <div>
            Hello world :-)
        </div>

        <div>
            Data : <%= data.target() %>
        </div>

        <div>
            Frog : <test:simple  name='green' size='11'/>
        </div>
        <div>
            Toad : <test:complex name='brown' size='22'/>
        </div>

    </body>
</html>

