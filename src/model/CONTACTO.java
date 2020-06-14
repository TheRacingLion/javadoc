package model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Java model for the CONTACTO table in the SQL database.
 * Represents a colaborator contact.
 */
public class CONTACTO {
    /** Colaborator number that owns this contact */
    public int idtitular;
    /** Order number of the contact. Used as a priority number. */
    public int noordem;
    /** Colaborator Contact. Either a phone number or email. */
    public String contacto;
    /** Contact description. */
    public String descricao;

    /**
     * Creates a colaborator contact from a {@code ResultSet}.
     * Used to facilitate the creation of a java model from a {@code ResultSet}.
     *
     * @param rs The {@code ResultSet} with the info
     */
    public CONTACTO(ResultSet rs) {
        try {
            this.idtitular = rs.getInt("idtitular");
            this.noordem = rs.getInt("noordem");
            this.contacto = rs.getString("contacto");
            this.descricao = rs.getString("descricao");
        } catch (SQLException x) {
            // quiet
        }
    }

    /**
     * Creates a contact for a colaborator
     * @param noordem_ Order number of the contact. Used as a priority number.
     * @param contacto_ Colaborator Contact. Either a phone number or email.
     * @param descricao_ Contact description.
     */
    public CONTACTO(int noordem_, String contacto_, String descricao_) {
        this.noordem = noordem_;
        this.contacto = contacto_;
        this.descricao = descricao_;
    }

    /**
     * Sets the colaborator number for this contact
     * @param nocolaborador The colaborator number
     */
    public void setNoColaborador(int nocolaborador) {
        this.idtitular = nocolaborador;
    }

    /**
     * Fills in a {@code PreparedStatement} with the information from this model
     * @param statement The {@code PreparedStatement} to fill
     * @param startIndex Start index for the parameter index in the statement
     */
    public void fillInPreparedStatement(PreparedStatement statement, int startIndex) {
        try {
            statement.setInt(startIndex, this.idtitular);
            statement.setInt(startIndex + 1, this.noordem);
            statement.setString(startIndex + 2, this.contacto);
            statement.setString(startIndex + 3, this.descricao);
        } catch (SQLException e) {
            // Do nothing
        }
    }
}
