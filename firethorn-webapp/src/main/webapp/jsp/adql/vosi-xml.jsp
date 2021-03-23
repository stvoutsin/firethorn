<%@ page
    import="uk.ac.roe.wfau.firethorn.meta.adql.AdqlResource"
    import="uk.ac.roe.wfau.firethorn.meta.adql.AdqlSchema"
    import="uk.ac.roe.wfau.firethorn.meta.adql.AdqlTable"
    import="uk.ac.roe.wfau.firethorn.meta.adql.AdqlColumn"
    import="uk.ac.roe.wfau.firethorn.widgeon.adql.AdqlResourceController"
    import="uk.ac.roe.wfau.firethorn.adql.util.AdqlNameModifier"


    contentType="text/xml; charset=UTF-8" 
    session="false"
%><%
AdqlResource resource = (AdqlResource) request.getAttribute(
    AdqlResourceController.TARGET_ENTITY
    ) ;
%><?xml version='1.0' encoding='UTF-8'?>
<vtm:tableset xmlns:vs="http://www.ivoa.net/xml/VODataService/v1.1" xmlns:vtm="http://www.ivoa.net/xml/VOSITables/v1.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://www.ivoa.net/xml/VODataService/v1.1 http://www.ivoa.net/xml/VOSITables/v1.0"><%
    for(AdqlSchema schema : resource.schemas().select())
        {
        %><schema>
        <name><%= new AdqlNameModifier().process(schema.name())  %></name>
        <title></title>
        <utype></utype>
        <% 

        for (AdqlTable table : schema.tables().select())
            {
            %>
        <table type='table'>
            <name><%= new AdqlNameModifier().process(schema.name()) %>.<%= new AdqlNameModifier().process(table.name()) %></name>
            <title></title>
            <utype></utype>

	    <%
            if (table.text() != null)
                {
                %><description><![CDATA[<%= table.text() %>]]></description><%
                }
            else {
                %><description/><%
                }
            %>
		
            <%
            for (AdqlColumn column : table.columns().select())
                {
                %>
            <column>
           		<name><%=  new AdqlNameModifier().process(column.name())  %></name>
		   	
                <%
                if (column.text() != null)
                    {
                    %><description><![CDATA[<%= column.text() %>]]></description><%
                    }
                else {
                    %><description/><%
                    }

                AdqlColumn.Metadata meta = column.meta();
                if ((meta != null) && (meta.adql() != null))
                    {
                    if (meta.adql().units() != null)
                        {
                        %>
                        <unit><![CDATA[<%= meta.adql().units() %>]]></unit>
                        <%
                        }
                 

                    if (meta.adql().ucd() != null)
                        {
                        %>
                        <ucd><%= meta.adql().ucd() %></ucd>
                        <%
                        }
              

                    if (meta.adql().utype() != null)
                        {
                        %>
                        <utype><%= meta.adql().utype() %></utype>
                        <%
                        }
                

                    if (meta.adql().type() != null)
                        {
                        String votableType = meta.adql().type().votype().toString();
                        String arraysize = "*";
                        
                        if (column.meta().adql().type() == AdqlColumn.AdqlType.DATE)
			                {
			                votableType = "char";
			                arraysize = "*";
			                }
			            if (column.meta().adql().type() == AdqlColumn.AdqlType.TIME)
			                {
			                votableType = "char";
			                arraysize = "*";
			                }
			            if (column.meta().adql().type() == AdqlColumn.AdqlType.DATETIME)
			                {
			                votableType = "char";
			                arraysize = "*";
			                }
						else if (column.meta().adql().type() == AdqlColumn.AdqlType.INTEGER) 
						    {	
			            	votableType = "int";
			                arraysize = "1";
			                }
			       
			                
                        if ((meta.adql().arraysize() != null) && (meta.adql().arraysize() != 0))
                            {
                            if (meta.adql().arraysize() == -1)
                                {
                                %>
                                <dataType arraysize='*' xsi:type="vs:VOTableType"><%= votableType %></dataType>
                                <%
                                }
                            else {
                                %>
                                <dataType arraysize='<%= meta.adql().arraysize() %>' xsi:type="vs:VOTableType"><%= votableType %></dataType>
                                <%
                                }
                            }
                        else {
                            %>
                            <dataType arraysize='<%= arraysize %>' xsi:type="vs:VOTableType"><%= votableType %></dataType>
                            <%
                            }
                        }
                   
                    }
                else {
                    %>
                    <!-- no metadata -->
                    <%
                    }
                %>
            </column>
            <% } %>
        </table>
        <% } %>
    </schema>
    <% } %>
</vtm:tableset>

