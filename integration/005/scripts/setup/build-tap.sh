
# -----------------------------------------------------
# Configure our identity.
#[root@tester]

        identity=${identity:-$(date '+%H:%M:%S')}
        community=${community:-$(date '+%A %-d %B %Y')}

        source "bin/01-01-init-rest.sh"

# -----------------------------------------------------
# Check the system info.
#[root@tester]

        curl \
            --header "firethorn.auth.identity:${identity:?}" \
            --header "firethorn.auth.community:${community:?}" \
            "${endpointurl:?}/system/info" \
            | bin/pp | tee system-info.json

# -----------------------------------------------------
# Load the ATLASDR1 resource.
#[root@tester]

        database=ATLASDR1
        
        source "bin/02-02-create-jdbc-space.sh" \
            'Atlas JDBC conection' \
            "jdbc:jtds:sqlserver://${datalink:?}/${database:?}" \
            "${datauser:?}" \
            "${datapass:?}" \
            "${datadriver:?}" \
            '*'
        atlasjdbc=${jdbcspace:?}

        source "bin/03-01-create-adql-space.sh" 'Atlas ADQL workspace'
        atlasadql=${adqlspace:?}

        source "bin/03-04-import-jdbc-metadoc.sh" "${atlasjdbc:?}" "${atlasadql:?}" 'ATLASDR1' 'dbo' "meta/ATLASDR1_AtlasSource.xml"

        source "bin/03-04-import-jdbc-metadoc.sh" "${atlasjdbc:?}" "${atlasadql:?}" 'ATLASDR1' 'dbo' "meta/ATLASDR1_AtlasTwomass.xml"



# -----------------------------------------------------
# Create our workspace
#[root@tester]

        source "bin/04-01-create-query-space.sh" 'Test workspace'
        source "bin/04-03-import-query-schema.sh" "${atlasadql:?}" 'ATLASDR1' 'atlas'


# -----------------------------------------------------
# Run an ATLASDR1 query
#[root@tester]

        source "bin/04-03-create-query-schema.sh"

        source "bin/05-03-execute-query.sh" \
            "AUTO" \
            "
            SELECT
                atlasSource.ra,
                atlasSource.dec
            FROM
                atlas.atlasSource
            WHERE
                atlasSource.ra  BETWEEN 354 AND 355
            AND
                atlasSource.dec BETWEEN -40 AND -39
            "



	# -----------------------------------------------------
	# Testing TAP

	resourceid=$(basename ${queryspace:?}) 

	query="SELECT+top+5+ra+from+atlasSource"
	format=VOTABLE
	lang=ADQL
	request=doQuery

        # Get VOSI
	curl ${endpointurl:?}/tap/${resourceid:?}/tables

query="SELECT+TOP+10+*+FROM+ATLASDR1.atlasSource"
	format=VOTABLE
	lang=ADQL
	request=doQuery
	

        identity=${identity:-$(date '+%H:%M:%S')}
        community=${community:-$(date '+%A %-d %B %Y')}

        source "bin/01-01-init-rest.sh"


        # Get VOSI
	curl ${endpointurl:?}/tap/${resourceid:?}/tables


	# ----------------------Query test1: synchronous-------------------------------
        # Run a synchronous query
	
	curl -v -L \
	${endpointurl:?}/tap/${resourceid:?}/sync \
	-d QUERY=${query:?} \
	-d LANG=${lang:?} \
	-d MAXREC=2 \
	-d REQUEST=${request:?}

	

	# ----------------------TAP_SCHEMA generating-------------------------------

	tap_schema_user=${metauser:?}
	tap_schema_pass=${metapass:?}
	tap_schema_url=${metadataurl:?}/${metadata?} 
	tap_schema_driver=net.sourceforge.jtds.jdbc.Driver
	tap_schema_db=${metadata?}

        # Generate TAP_SCHEMA
	curl --data "url=${tap_schema_url:?}&user=${tap_schema_user:?}&pass=${tap_schema_pass:?}&driver=${tap_schema_driver:?}&catalog=${tap_schema_db:?}" ${endpointurl:?}/tap/${resourceid:?}/generateTapSchema


	source "bin/02-02-create-jdbc-space.sh" \
            'TAP_SCHEMA conection' \
            "${tap_schema_url:?}" \
            "${tap_schema_user:?}" \
            "${tap_schema_pass:?}" \
            "${tap_schema_driver:?}" \
            '*' \
            'nilbert'


        cat >  "${HOME:?}/firethorn.spaces" << EOF
tapschemajdbc=${jdbcspace:?}
EOF


        source "bin/03-04-import-jdbc-metadoc.sh" "${jdbcspace:?}" "${adqlspace:?}" "${tap_schema_db:?}" "TAP_SCHEMA_"${resourceid:?} "meta/TAP_SCHEMA.xml"
      
	source "bin/05-03-execute-query.sh" \
            "AUTO" \
            "
            SELECT
	      * from TAP_SCHEMA.tables
            "

	source "bin/05-03-execute-query.sh" \
            "AUTO" \
            "
            SELECT
	      * from TAP_SCHEMA.schemas
            "
	




	# ----------------------Query test6: Test TAP_SCHEMA using TAP-------------------------------

	query="SELECT+*+from+TAP_SCHEMA.tables"
	data="$( curl -L \
	${endpointurl:?}/tap/${resourceid:?}/async \
	-d LANG=${lang:?} \
	-d REQUEST=${request:?} \
	-d QUERY=${query:?})"

	jobid=$(grep -oPm1 "(?<=<uws:jobId>)[^<]+" <<< "$data")

	curl -L \
	${endpointurl:?}/tap/${resourceid:?}/async/${jobid:?}/phase \
	-d PHASE=RUN 



