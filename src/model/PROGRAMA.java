package model;

import jdbc.Database;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Java model for the PROGRAMA table in the SQL database.
 * Represents a organization program.
 */
public class PROGRAMA {
    /** Program identification number */
    public String identificador;
    /** Code for the intervention area */
    public String codigo;
    /** ID of the program organization */
    public int idassociacao;
    /** Program name */
    public String nome;
    /** Program start date */
    public java.sql.Date datainicio;
    /** Program end date */
    public java.sql.Date datafinal;
    /** Program minimal age */
    public int idademinima;
    /** Program cost */
    public BigDecimal custo;
    /** Program discrimination {@code String}. Says if the program is short ("pcd") or long ("pld") term. */
    public String atrdiscriminante;

    /**
     * Creates a program from a {@code ResultSet}.
     * Used to facilitate the creation of a java model from a {@code ResultSet}.
     *
     * @param rs The {@code ResultSet} with the info
     */
    public PROGRAMA(ResultSet rs) {
        try {
            this.identificador = rs.getString("identificador");
            this.codigo = rs.getString("codigo");
            this.idassociacao = rs.getInt("idassociacao");
            this.nome = rs.getString("nome");
            this.datainicio = rs.getDate("datainicio");
            this.datafinal = rs.getDate("datafinal");
            this.idademinima = rs.getInt("idademinima");
            this.custo = rs.getBigDecimal("custo");
            this.atrdiscriminante = rs.getString("atrdiscriminante");
        } catch (SQLException x) {
            // quiet
        }
    }

    /**
     * Returns a formatted {@code String} representation of a program.
     *
     * @return A formatted {@code String}
     */
    @Override
    public String toString() {
        String areaintervencao = Database.getAreaIntervencao(codigo);
        return nome + " (" + areaintervencao + ") (Idade min: " + idademinima + ") | De " + datainicio.toString() + " a " + datafinal.toString();
    }
}
