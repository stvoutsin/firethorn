#!/bin/bash -eu
# -e: Exit immediately if a command exits with a non-zero status.
# -u: Treat unset variables as an error when substituting.
#
#  Copyright (C) 2013 Royal Observatory, University of Edinburgh, UK
#
#  This program is free software: you can redistribute it and/or modify
#  it under the terms of the GNU General Public License as published by
#  the Free Software Foundation, either version 3 of the License, or
#  (at your option) any later version.
#
#  This program is distributed in the hope that it will be useful,
#  but WITHOUT ANY WARRANTY; without even the implied warranty of
#  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
#  GNU General Public License for more details.
#
#  You should have received a copy of the GNU General Public License
#  along with this program.  If not, see <http://www.gnu.org/licenses/>.
#
#

echo "---- ---- ---- ---- ---- ---- ---- ----"
echo "WARNING : deprecated functions"
echo "These shell script functions are deprecated"
echo "and will be removed in the next version"
echo "---- ---- ---- ---- ---- ---- ---- ----"

#
# The service endpoint URL.
endpointurl=${endpointurl:-$(
    sed -n '
        s/^firethorn\.webapp\.endpoint=\(.*\)$/\1/p
        ' "${HOME:?}/firethorn.properties"
    )}
endpointurl=${endpointurl:-'http://localhost:8080/firethorn'}


#
# Initialise our REST client.
#resty "${endpointurl:?}" -W -H 'Accept: application/json'

#
# Unique name generator 
unique()
    {
    date '+%Y%m%d-%H%M%S%N'
    }

#
# Function for setting heredoc variables.
# http://stackoverflow.com/questions/1167746/how-to-assign-a-heredoc-value-to-a-variable-in-bash
define()
    {
    IFS='\n' read -r -d '' ${1} || true;
    }

#
# Function to get the self URL from a JSON response.
self()
    {
    jq -r '.self'
    }

#
# Function to get the node path from an http URL.
node()
    {
    sed -n 's|\(https\{0,1\}\)://\([^/:]*\):\{0,1\}\([^/]*\)/\([^/]*\)/\(.*\)|/\5|p'
    }

#
# Function to get the node ident from a JSON response.
ident()
    {
    sed -n 's|.*\/\([^/]*\)|\1|p'
    }

#
# Function to get the name from a JSON response.
name()
    {
    jq -r '.name'
    }

#
# Function to get the job status from a JSON response.
status()
    {
    jq -r '.status'
    }

#
# Function to get the votable URL from a query.
votable()
    {
    jq -r '.results.formats.votable'
    }

#
# Run a query and poll the result.
runquery()
    {
    local query=${1:?}
    local status=$(
        curl \
            --header "firethorn.auth.identity:${identity:?}" \
            --header "firethorn.auth.community:${community:?}" \
            --data-urlencode "adql.query.status=RUNNING" \
            "${endpointurl:?}/${query:?}" \
            | status
            )

    #sleep 20
    
    while [ "${status:?}" == 'PENDING' -o "${status:?}" == 'RUNNING' ]
    do
        sleep 1
        status=$(
            curl \
                --header "firethorn.auth.identity:${identity:?}" \
                --header "firethorn.auth.community:${community:?}" \
                "${endpointurl:?}/${query:?}" \
                | status
                )
        echo "${status:?}"
    done

    }


