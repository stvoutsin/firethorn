package adql.query.operand;

/*
 * This file is part of ADQLLibrary.
 * 
 * ADQLLibrary is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * ADQLLibrary is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with ADQLLibrary.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Copyright 2012 - UDS/Centre de Données astronomiques de Strasbourg (CDS)
 */

/**
 * Type of possible simple numeric operations.
 * 
 * @author Gr&eacute;gory Mantelet (CDS)
 * @version 11/2010
 * 
 * @see Operation
 */
public enum OperationType {
	SUM, SUB, MULT, DIV, BIT_OR, BIT_AND, BIT_XOR, MOD;

	public static String[] getOperators() {
		return new String[] { SUM.toString(), SUB.toString(), MULT.toString(),
				DIV.toString(), MOD.toString(), BIT_OR.toString(),
				BIT_AND.toString(), BIT_XOR.toString() };
	}

	public static OperationType getOperator(String str) throws UnsupportedOperationException{
		if (str.equalsIgnoreCase("+"))
			return SUM;
		else if (str.equalsIgnoreCase("-"))
			return SUB;
		else if (str.equalsIgnoreCase("*"))
			return MULT;
		else if (str.equalsIgnoreCase("/"))
			return DIV;
		else if (str.equalsIgnoreCase("%"))
			return MOD;
		else if (str.equalsIgnoreCase("|"))
			return BIT_OR;
		else if (str.equalsIgnoreCase("&"))
			return BIT_AND;
		else if (str.equalsIgnoreCase("^"))
			return BIT_XOR;
		else
			throw new UnsupportedOperationException("Numeric operation unknown: \"" + str + "\" !");
	}

	public String toADQL(){
		return toString();
	}

	@Override
	public String toString() {
		switch (this) {
		case SUM:
			return "+";
		case SUB:
			return "-";
		case MULT:
			return "*";
		case DIV:
			return "/";
		case MOD:
			return "%";
		case BIT_OR:
			return "|";
		case BIT_AND:
			return "&";
		case BIT_XOR:
			return "^";
		default:
			return "???";
		}
	}
}
