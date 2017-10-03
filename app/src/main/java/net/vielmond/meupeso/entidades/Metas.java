package net.vielmond.meupeso.entidades;

import java.io.Serializable;

/**
 * Created by root on 29/09/17.
 */

public class Metas implements Serializable {

    private Integer id;
    private Float meta;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Float getMeta() {
        return meta;
    }

    public void setMeta(Float meta) {
        this.meta = meta;
    }
}
