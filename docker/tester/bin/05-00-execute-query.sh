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

adqlfile=${1:?}
rowidcol=rowid

#    --data   "adql.schema.query.create.rowid=${rowidcol:?}" \
#
# Create the query.
curl \
    --silent \
    --header "firethorn.auth.identity:${identity:?}" \
    --header "firethorn.auth.community:${community:?}" \
    --data   "adql.schema.query.create.rowid=${rowidcol:?}" \
    --data-urlencode "adql.query.input@${adqlfile:?}" \
    "${endpointurl:?}/${queryschema:?}/queries/create" \
     | jq '.' | tee /tmp/query-job.json

queryident=$(
    cat /tmp/query-job.json | self | node
    )

#
# Run the query.
runquery "${queryident:?}"

#
# Get the VOTable results.
#curl "$(cat /tmp/atlas-query.json | votable)"


