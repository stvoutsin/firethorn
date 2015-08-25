

source /tmp/chain.properties


pyroproperties=$(mktemp)
cat > "${pyroproperties:?}" << EOF
import os

#------------------------- General Configurations -----------------------#

### Unit test specific configuration ###
use_preset_params = False # Use the preset firethorn resource parameters at the end of this config file
use_cached_firethorn_env = False #Use cached firethorn environment, stored in (testing/conf/pyrothorn-stored.js)
firethorn_version =  "${version:?}" 
include_neighbour_import = False # Choose whether to import all neighbour tables for a catalogue or not
test_is_continuation = False # Test is continued from prev run or not (If true, duplicate queries that have been run previously will not be run)

### Directory and URL Information ###
firethorn_host = "${firelink:?}" 
firethorn_port = "8080" 
full_firethorn_host = firethorn_host if firethorn_port=='' else firethorn_host + ':' + firethorn_port
base_location = os.getcwd()

### Email ###
test_email = "test@test.roe.ac.uk" 

### Queries ###
sample_query="Select top 10 * from Filter" 
sample_query_expected_rows=10
limit_query = None
sql_rowlimit = 1000
sql_timeout = 1000
firethorn_timeout = 1000
query_mode = "AUTO" 

#------------------- Test Configurations ----------------------------------#

### SQL Database Configuration ###

test_dbserver= "${datalink:?}" 
test_dbserver_username = "${datauser:?}" 
test_dbserver_password = "${datapass:?}" 
test_dbserver_port = "${dataport:?}" 
test_database = "${testrundatabase:?}" 
neighbours_query = """ 
        SELECT DISTINCT
            ExternalSurvey.databaseName
        FROM
            RequiredNeighbours
        JOIN
            ExternalSurvey
        ON
            RequiredNeighbours.surveyID = ExternalSurvey.surveyID
        JOIN
            ExternalSurveyTable
        ON
            RequiredNeighbours.surveyID = ExternalSurveyTable.surveyID
        AND
            RequiredNeighbours.extTableID = ExternalSurveyTable.extTableID
        WHERE 
            ExternalSurvey.databaseName!='NONE'
        ORDER BY
            ExternalSurvey.databaseName
            """ 

### Reporting Database Configuration ###

reporting_dbserver= "${pyrosqllink:?}" 
reporting_dbserver_username = "root" 
reporting_dbserver_password = "" 
reporting_dbserver_port = "${pyrosqlport:?}" 
reporting_database = "pyrothorn_testing" 

### Logged Queries Configuration ###

stored_queries_dbserver= "${storedquerieslink:?}" 
stored_queries_dbserver_username = "${storedqueriesuser:?}" 
stored_queries_dbserver_password = "${storedqueriespass:?}" 
stored_queries_dbserver_port = "${storedqueriesport:?}" 
stored_queries_database = "${storedqueriesdata:?}" 
stored_queries_query = "select * from webqueries where dbname like '${testrundatabase:?}%'" 
logged_queries_txt_file = "testing/query_logs/integration_list.txt" 

### Firethorn Live test Configuration ###

adql_copy_depth = "THIN" 
resourcename = '${testrundatabase:?} JDBC conection' 
resourceuri = "jdbc:jtds:sqlserver://${datalink:?}/${testrundatabase:?}" 
adqlspacename = '${testrundatabase:?} Workspace'
catalogname = '*'
driver = '${datadriver:?}'
ogsadainame = '${testrun_ogsadai_resource:?}'
jdbccatalogname = '${testrundatabase:?}'
jdbcschemaname = 'dbo'
jdbc_resource_user = '${datauser:?}'
jdbc_resource_pass = '${datapass:?}'
metadocfile = "testing/metadocs/${testrundatabase:?}_TablesSchema.xml" 
metadocdirectory = "testing/metadocs/" 
stored_env_config = 'conf/pyrothorn-stored.js'

### Firethorn Predefined test Configuration ###

jdbcspace = "" 
adqlspace = "" 
adqlschema = "" 
query_schema = "" 
schema_name = "${testrundatabase:?}" 
schema_alias = "${testrundatabase:?}" 

EOF


chmod a+r "${pyroproperties:?}" 
chcon -t svirt_sandbox_file_t "${pyroproperties:?}" 

mkdir -p /var/logs/${pyroname:?}

docker run -i -t \
    --name ${pyroname:?} \
    --volume "${pyroproperties:?}:/home/pyrothorn/config.py" \
    --link "${firename:?}:${firelink:?}" \
    --link "${pyrosqlname:?}:${pyrosqllink:?}" \
    --link "${storedqueriesname:?}:${storedquerieslink:?}" \
    --link "${ogsaname:?}:${ogsalink:?}" \
    --link "${dataname:?}:${datalink:?}" \
    --link "${username:?}:${userlink:?}" \
       firethorn/pyrothorn /bin/bash  -c "cd /home/pyrothorn/;python testing/test_firethorn_logged_sql.py"



