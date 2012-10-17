'''
Created on Sep 17, 2012

@author: stelios
'''

import web


web_services_hostname = 'localhost:8080'

base_location = '/home/stelios/Desktop/workspace/firethorn-webpy-gui'
get_jdbc_resources_url = "/firethorn/jdbc/resources/select"

local_hostname = {'index' : 'localhost:8090','services' : 'http://localhost:8090/services'}

service_select_by_name_url = 'http://' + web_services_hostname + '/firethorn/adql/services/select?'
service_select_by_name_param = 'adql.services.select.name'

service_select_with_text_url = 'http://' + web_services_hostname +' /firethorn/adql/services/search?'
service_select_with_text_param = 'adql.services.search.text'

service_create_url = 'http://'+ web_services_hostname + '/firethorn/adql/services/create'
service_create_param = 'adql.services.create.name'

service_get_url = 'http://'+ web_services_hostname + '/firethorn/adql/service/'
service_get_param = 'id'

create_menu_items = {'admin' : ['Service','JDBC connection'] , 'user' : ['Service']}

resource_uris = {'http://data.metagrid.co.uk/wfau/firethorn/types/jdbc-resource-1.0.json': '/catalogs/select',
                 'http://data.metagrid.co.uk/wfau/firethorn/types/jdbc-catalog-1.0.json': '/schemas/select',
                 'http://data.metagrid.co.uk/wfau/firethorn/types/jdbc-schema-1.0.json': '/tables/select',
                 'http://data.metagrid.co.uk/wfau/firethorn/types/jdbc-table-1.0.json': '/columns/select'}

types = {'service' : 'http://data.metagrid.co.uk/wfau/firethorn/types/adql-service-1.0.json', 'catalog' : 'http://data.metagrid.co.uk/wfau/firethorn/types/jdbc-catalog-1.0.json', 'resource' : 'http://data.metagrid.co.uk/wfau/firethorn/types/jdbc-resource-1.0.json'}

errors = {'INVALID_PARAM' : 'Parameter provided was invalid', 
          'INVALID_NETWORK_REQUEST' : 'Error processing a network request',
          'INVALID_REQUEST' : 'Invalid request to the server',
          'INVALID_TYPE' : 'Invalid type for the requested URL',
          'INVALID_ACTION' : 'Invalid action requested to server',
          'INVALID_CHECKBOX_VALUE' : 'Invalid checkbox value passed to server',
          'UNKNOWN_ERROR' : 'Unknown server error while processing a request',
          }
