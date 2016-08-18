/*
 *  Copyright (C) 2012 Royal Observatory, University of Edinburgh, UK
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package uk.ac.roe.wfau.firethorn.widgeon.adql;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import lombok.extern.slf4j.Slf4j;
import uk.ac.roe.wfau.firethorn.adql.query.QueryProcessingException;
import uk.ac.roe.wfau.firethorn.adql.query.green.GreenQuery;
import uk.ac.roe.wfau.firethorn.entity.exception.IdentifierNotFoundException;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlSchema;
import uk.ac.roe.wfau.firethorn.webapp.control.AbstractEntityController;
import uk.ac.roe.wfau.firethorn.webapp.control.WebappLinkFactory;
import uk.ac.roe.wfau.firethorn.webapp.paths.Path;
import uk.ac.roe.wfau.firethorn.widgeon.green.GreenQueryBean;
import uk.ac.roe.wfau.firethorn.widgeon.name.AdqlSchemaLinkFactory;

/**
 * Spring MVC controller to handle the {@link GreenQuery} for an {@link AdqlSchema}.
 * <br/>Controller path : [{@value AdqlSchemaLinkFactory.SCHEMA_QUERY_PATH}]
 *
 */
@Slf4j
@Controller
@RequestMapping(AdqlSchemaLinkFactory.SCHEMA_GREEN_PATH)
public class AdqlSchemaGreenController
extends AbstractEntityController<GreenQuery, GreenQueryBean>
    {
    @Override
    public Path path()
        {
        return path(
            AdqlSchemaLinkFactory.SCHEMA_GREEN_PATH
            );
        }

    /**
     * Public constructor.
     *
     */
    public AdqlSchemaGreenController()
        {
        super();
        }

    /**
     * MVC property for the {@link GreenQuery} name, [{@value}].
     *
     */
    public static final String QUERY_NAME_PARAM = "adql.schema.query.create.name" ;

    /**
     * MVC property for the {@link GreenQuery} input, [{@value}].
     *
     */
    public static final String QUERY_INPUT_PARAM = "adql.schema.query.create.query" ;

    /**
     * MVC property for the {@link GreenQuery} store, [{@value}].
     *
     */
    public static final String QUERY_STORE_PARAM = "adql.schema.query.create.store" ;

    /**
     * MVC property for the {@link GreenQuery.Mode} mode, [{@value}].
     *
     */
    public static final String QUERY_MODE_PARAM = "adql.schema.query.create.mode" ;

    /**
     * MVC property for the {@link GreenQuery.Syntax.Level} level, [{@value}].
     *
     */
    public static final String QUERY_LEVEL_PARAM = "adql.schema.query.create.level" ;

    @Override
    public GreenQueryBean bean(final GreenQuery entity)
        {
        return new GreenQueryBean(
            entity
            );
        }

    @Override
    public Iterable<GreenQueryBean> bean(final Iterable<GreenQuery> iter)
        {
        return new GreenQueryBean.Iter(
            iter
            );
        }

    /**
     * Get the parent {@link AdqlSchema} based on the {@Identifier} in the request path.
     * @param ident The {@link AdqlSchema} {@Identifier} from the URL path, [{@value WebappLinkFactory.IDENT_FIELD}].
     * @return The parent {@link AdqlSchema}.
     * @throws IdentifierNotFoundException If the {@link AdqlSchema} could not be found.
     *
     */
    @ModelAttribute(AdqlSchemaController.TARGET_ENTITY)
    public AdqlSchema parent(
        @PathVariable(WebappLinkFactory.IDENT_FIELD)
        final String ident
        ) throws IdentifierNotFoundException {
        log.debug("parent() [{}]", ident);
        return factories().adql().schemas().entities().select(
            factories().adql().schemas().idents().ident(
                ident
                )
            );
        }

    /**
     * {@link RequestMethod#POST} request to create a new {@link GreenQuery}.
     * <br/>Request path : [{@value #CREATE_PATH}]
     * <br/>Content type : [{@value #JSON_MIME}]
     * @param schema The parent {@link AdqlSchema} selected using the {@Identifier} in the request path.
     * @param name The {@link GreenQuery} name, [{@value #QUERY_NAME_PARAM}].
     * @param name The {@link GreenQuery.Mode} mode, [{@value #QUERY_MODE_PARAM}].
     * @param name The {@link GreenQuery.Syntax.Level} level, [{@value #QUERY_LEVEL_PARAM}].
     * @return An {@link GreenQueryBean} wrapping the new {@link GreenQuery}.
     * @todo Rejects duplicate names.
     * 
     */
    @ResponseBody
    @RequestMapping(value=CREATE_PATH, method=RequestMethod.POST, produces=JSON_MIME)
    public ResponseEntity<GreenQueryBean> create(
        @ModelAttribute(AdqlSchemaController.TARGET_ENTITY)
        final AdqlSchema schema,
        @RequestParam(value=QUERY_INPUT_PARAM, required=true)
        final String query,
        @RequestParam(value=QUERY_NAME_PARAM, required=false)
        final String name,
        @RequestParam(value=QUERY_MODE_PARAM, required=false)
        final GreenQuery.Mode mode,
        @RequestParam(value=QUERY_LEVEL_PARAM, required=false)
        final GreenQuery.Syntax.Level level
        ) throws QueryProcessingException {
        return created(
            schema.greens().create(
                factories().adql().greens().params().create(
                    level,
                    mode
                    ),
                query,
                name
                )
            );
        }
    }
