package model;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Java model for the VOLUNTARIO table in the SQL database.
 * Represents a organization program.
 */
public class VOLUNTARIO {
    /** Colaborator number of this volunteer */
    public int nocolaborador;
    /** The volunteers current ocupation */
    public String ocupacaoatual;
    /** The volunteers language */
    public String idioma;
    /** The volunteers program ID */
    public String idprograma;

    /**
     * Creates a program volunteer
     *
     * @param ocupacaoatual_ The volunteers current ocupation
     * @param idioma_ The volunteers language
     * @param idprograma_ The volunteers program ID
     */
    public VOLUNTARIO(String ocupacaoatual_, String idioma_, String idprograma_) {
        this.ocupacaoatual = ocupacaoatual_;
        this.idioma = idioma_;
        this.idprograma = idprograma_;
    }

    /**
     * Sets the colaborator number for this volunteer
     *
     * @param nocolaborador The colaborator number
     */
    public void setNoColaborador(int nocolaborador) {
        this.nocolaborador = nocolaborador;
    }

    /**
     * Fills in a {@code PreparedStatement} with the information from this model
     *
     * @param statement The {@code PreparedStatement} to fill
     * @param startIndex Start index for the parameter index in the statement
     */
    public void fillInPreparedStatement(PreparedStatement statement, int startIndex) {
        try {
            statement.setInt(startIndex, this.nocolaborador);
            statement.setString(startIndex + 1, this.ocupacaoatual);
            statement.setString(startIndex + 2, this.idioma);
            statement.setString(startIndex + 3, this.idprograma);
        } catch (SQLException e) {
            // Do nothing
        }
    }
}
