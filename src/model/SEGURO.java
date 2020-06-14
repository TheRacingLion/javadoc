package model;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Java model for the SEGURO table in the SQL database.
 * Represents a organization program.
 */
public class SEGURO {
    /** Insurance number */
    public int numero;
    /** Insurance colaborator number */
    public int pessoa;
    /** Insurance date */
    public java.sql.Date data;
    /** Insurance description */
    public String descricao;
    /** Insurance prize amouont */
    public BigDecimal premio;
    /** Insurance deadline */
    public String prazo;
    /** Insurance duration */
    public int duracao;

    /**
     * Creates a insurance for a colaborator
     *
     * @param data_ The insurance date
     * @param descricao_ The insurance description
     * @param premio_ The insurance prize amount
     * @param prazo_ The insurance deadline
     * @param duracao_ The insurance duration
     */
    public SEGURO(String data_,String descricao_,String premio_,String prazo_,String duracao_) {
        this.data = java.sql.Date.valueOf(data_);
        this.descricao = descricao_;
        this.premio = new BigDecimal(premio_);
        this.prazo = prazo_;
        this.duracao = Integer.valueOf(duracao_);
    }

    /**
     * Sets the colaborator number for this insurance
     *
     * @param nocolaborador The colaborator number
     */
    public void setNoColaborador(int nocolaborador) {
        this.pessoa = nocolaborador;
    }

    /**
     * Fills in a {@code PreparedStatement} with the information from this model
     *
     * @param statement The {@code PreparedStatement} to fill
     * @param startIndex Start index for the parameter index in the statement
     */
    public void fillInPreparedStatement(PreparedStatement statement, int startIndex) {
        try {
            statement.setDate(startIndex, this.data);
            statement.setInt(startIndex + 1, this.pessoa);
            statement.setString(startIndex + 2, this.descricao);
            statement.setBigDecimal(startIndex + 3, this.premio);
            statement.setString(startIndex + 4, this.prazo);
            statement.setInt(startIndex + 5, this.duracao);
        } catch (SQLException e) {
            // Do nothing
        }
    }
}
