package model;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Java model for the CONTACTOEMERGENCIA table in the SQL database.
 * Represents a volunteer emergency contact.
 */
public class CONTACTOEMERGENCIA {
    /** Emergency contact name */
    public String nome;
    /** Emergency contact kinship degree */
    public String grauparentesco;
    /** Colaborator Contact. Either a phone number or email. */
    public String contacto;
    /** Colaborator number that owns this contact */
    public int idtitular;
    /** Order number of the contact. Used as a priority number. */
    public int noordem;

    /**
     * Creates a emergency contact for a volunteer
     *
     * @param nome_ Emergency contact name
     * @param grauparentesco_ Emergency contact kinship degree
     * @param noordem_ Order number of the contact. Used as a priority number.
     * @param contacto_ Colaborator Contact. Either a phone number or email.
     */
    public CONTACTOEMERGENCIA(String nome_,String grauparentesco_,String contacto_,int noordem_) {
        this.nome = nome_;
        this.grauparentesco = grauparentesco_;
        this.contacto = contacto_;
        this.noordem = noordem_;
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
            statement.setString(startIndex, this.nome);
            statement.setString(startIndex + 1, this.grauparentesco);
            statement.setString(startIndex + 2, this.contacto);
            statement.setInt(startIndex + 3, this.idtitular);
            statement.setInt(startIndex + 4, this.noordem);
        } catch (SQLException e) {
            // Do nothing
        }
    }

}
