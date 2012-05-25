package asov.uws;

import java.io.File;
import java.io.IOException;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import adql.db.DBChecker;
import adql.parser.ADQLParser;
import adql.parser.ParseException;

import adql.query.ADQLQuery;

import adql.search.IReplaceHandler;

import adql.translator.ADQLTranslator;
import adql.translator.PgSphereTranslator;
import adql.translator.TranslationException;

import asov.DemoASOV;

import asov.adql.ReplacePointHandler;

import uws.UWSException;

import uws.job.AbstractJob;
import uws.job.LocalResult;

public class DemoUWS_Job extends AbstractJob {
	private static final long serialVersionUID = 1L;

	private String adql = null;

	public DemoUWS_Job(Map<String, String> lstParam) throws UWSException {
		super(lstParam);
	}

	@Override
	protected boolean loadAdditionalParams() throws UWSException {
		// Get the ADQL query (at the initialization but also, each time this parameter is changed):
		if (additionalParameters.containsKey("query")){
			adql = additionalParameters.get("query");
			return true;
		}
		return false;
	}

	@Override
	protected void jobWork() throws UWSException, InterruptedException {
		try{
			// 1. Build the ADQL parser:
			ADQLParser parser = new ADQLParser();
			parser.setQueryChecker(new DBChecker(DemoASOV.getDBTables()));
			parser.setCoordinateSystems(DemoASOV.getCoordinateSystems());

			// 2. Parse the query:
			ADQLQuery query = parser.parseQuery(adql);

			// 2bis. Manipulate the query: point('...', ra, dec) => coord:
			IReplaceHandler replacer = new ReplacePointHandler();
			replacer.searchAndReplace(query);

			// 3. Translate into SQL:
			ADQLTranslator translator = new PgSphereTranslator();
			String sql = translator.translate(query);

			// 4. Execute on the database:
			Connection conn = DemoASOV.connectDB();
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);

			// 5. Build and set the result:
			File resultFile = DemoASOV.buildResult(this, rs);
			addResult(new LocalResult(this, DemoASOV.RESULT_ID, resultFile));

		} catch(ParseException pe){
			throw new UWSException(HttpServletResponse.SC_BAD_REQUEST, pe, "Incorrect ADQL syntax between (l."+pe.getBeginLine()+",c."+pe.getBeginColumn()+") and (l."+pe.getEndLine()+",c."+pe.getEndColumn()+")");
		} catch (TranslationException e) {
			throw new UWSException(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e, "Error when translating ADQL to SQL");
		} catch (SQLException e) {
			throw new UWSException(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e, "SQL Error");
		} catch (ClassNotFoundException e) {
			throw new UWSException(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e, "JDBC Driver not found");
		} catch (IOException e) {
			throw new UWSException(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e, "Can not write the result file");
		}
	}

}