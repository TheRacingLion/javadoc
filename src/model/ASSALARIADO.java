package model;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Java model for the ASSALARIADO table in the SQL database.
 * Represents colaborators with paychecks.
 */
public class ASSALARIADO {
    /** Colaborator Number. Reference to the {@code COLABORADOR} table */
    public int nocolaborador;
    /** Colaborator responsibility inside the program */
    public String cargo;
    /** Colaborator salary */
    public BigDecimal vencimento;

    /**
     * Creates a colaborator with a paycheck
     *
     * @param cargo_ The colaborators position
     * @param vencimento_ The colaborators salary
     */
    public ASSALARIADO(String cargo_, String vencimento_) {
        this.cargo = cargo_;
        this.vencimento = new BigDecimal(vencimento_);
    }

    /**
     * Sets the colaborator number for this colaborator
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
            statement.setString(startIndex + 1, this.cargo);
            statement.setBigDecimal(startIndex + 2, this.vencimento);
        } catch (SQLException e) {
            // Do nothing
        }
    }
}
