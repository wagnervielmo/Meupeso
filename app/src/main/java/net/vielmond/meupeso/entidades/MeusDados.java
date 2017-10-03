package net.vielmond.meupeso.entidades;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by root on 29/09/17.
 */

public class MeusDados implements Serializable {

    private Integer id;
    private Float peso;
    private String strDataHora;
    private Date datahora;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Float getPeso() {
        return peso;
    }

    public void setPeso(Float peso) {
        this.peso = peso;
    }

    public String getStrDataHora() {
        return strDataHora;
    }

    public void setStrDataHora(String strDataHora) {
        this.strDataHora = strDataHora;
    }

    public Date getDatahora() {
        return datahora;
    }

    public void setDatahora(Date datahora) {
        this.datahora = datahora;
    }
}
