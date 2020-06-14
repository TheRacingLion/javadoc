package model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Java model for the COLABORADOR table in the SQL database.
 * Represents a colaborator.
 */
public class COLABORADOR {
    /** Colaborator number */
    public int nocolaborador;
    /** Colaborator name */
    public String nome;
    /** Colaborator last name */
    public String apelido;
    /** Colaborator birth date */
    public java.sql.Date dtnascimento;
    /** Colaborator identification number */
    public String nident;
    /** Colaborator ID type (Either CC, BI or Passaporte) */
    public int tipoid;
    /** Colaborator fiscal number */
    public String nfiscal;
    /** Colaborator nationality */
    public String nacionalidade;
    /** Colaborator adress */
    public String morada;

    /**
     * Creates a colaborator from a {@code ResultSet}.
     * Used to facilitate the creation of a java model from a {@code ResultSet}.
     *
     * @param rs The {@code ResultSet} with the info
     */
    public COLABORADOR(ResultSet rs) {
        try {
            this.nocolaborador = rs.getInt("nocolaborador");
            this.nome = rs.getString("nome");
            this.apelido = rs.getString("apelido");
            this.dtnascimento = rs.getDate("dtnascimento");
            this.nident = rs.getString("nident");
            this.tipoid = rs.getInt("tipoid");
            this.nfiscal = rs.getString("nfiscal");
            this.nacionalidade = rs.getString("nacionalidade");
            this.morada = rs.getString("morada");
        } catch (SQLException x) {
            // quiet
        }
    }

    /**
     * Creates a colaborator
     * @param nome_ Colaborator name
     * @param apelido_ Colaborator last name
     * @param dtnascimento_ Colaborator data nascimento
     * @param nident_ Colaborator identification number
     * @param tipoid_ Colaborator ID type
     * @param nfiscal_ Colaborator fiscal number
     * @param nacionalidade_ Colaborator nationality
     * @param morada_ Colaborator adress
     */
    public COLABORADOR(String nome_,String apelido_,String dtnascimento_,String nident_,int tipoid_,String nfiscal_,String nacionalidade_,String morada_) {
        this.nome = nome_;
        this.apelido = apelido_;
        this.dtnascimento = java.sql.Date.valueOf(dtnascimento_);
        this.nident = nident_;
        this.tipoid = tipoid_;
        this.nfiscal = nfiscal_;
        this.nacionalidade = nacionalidade_;
        this.morada = morada_;
    }

    /**
     * Fills in a {@code PreparedStatement} with the information from this model
     * @param statement The {@code PreparedStatement} to fill
     * @param startIndex Start index for the parameter index in the statement
     */
    public void fillInPreparedStatement(PreparedStatement statement, int startIndex) {
        try {
            statement.setString(startIndex, this.nome);
            statement.setString(startIndex + 1, this.apelido);
            statement.setDate(startIndex + 2, this.dtnascimento);
            statement.setString(startIndex + 3, this.nident);
            statement.setInt(startIndex + 4, this.tipoid);
            statement.setString(startIndex + 5, this.nfiscal);
            statement.setString(startIndex + 6, this.nacionalidade);
            statement.setString(startIndex + 7, this.morada);
        } catch (SQLException e) {
            // Do nothing
        }
    }
}