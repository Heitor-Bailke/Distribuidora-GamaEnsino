package com.fontenova.distribuidora.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Distribuidora.
 */
@Entity
@Table(name = "distribuidora")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Distribuidora implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Integer id;

    @NotNull
    @Size(min = 3)
    @Column(name = "nome", nullable = false)
    private String nome;

    @NotNull
    @Size(min = 8, max = 8)
    @Column(name = "cep", length = 8, nullable = false)
    private String cep;

    @NotNull
    @Column(name = "cidade", nullable = false)
    private String cidade;

    @NotNull
    @Column(name = "bairro", nullable = false)
    private String bairro;

    @NotNull
    @Column(name = "rua", nullable = false)
    private String rua;

    @NotNull
    @Column(name = "numero", nullable = false)
    private Integer numero;

    @Column(name = "referencia")
    private String referencia;

    @NotNull
    @Size(max = 2)
    @Column(name = "estado", length = 2, nullable = false)
    private String estado;

    @Size(min = 3)
    @Column(name = "detalhes")
    private String detalhes;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Integer getId() {
        return this.id;
    }

    public Distribuidora id(Integer id) {
        this.setId(id);
        return this;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return this.nome;
    }

    public Distribuidora nome(String nome) {
        this.setNome(nome);
        return this;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCep() {
        return this.cep;
    }

    public Distribuidora cep(String cep) {
        this.setCep(cep);
        return this;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getCidade() {
        return this.cidade;
    }

    public Distribuidora cidade(String cidade) {
        this.setCidade(cidade);
        return this;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getBairro() {
        return this.bairro;
    }

    public Distribuidora bairro(String bairro) {
        this.setBairro(bairro);
        return this;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getRua() {
        return this.rua;
    }

    public Distribuidora rua(String rua) {
        this.setRua(rua);
        return this;
    }

    public void setRua(String rua) {
        this.rua = rua;
    }

    public Integer getNumero() {
        return this.numero;
    }

    public Distribuidora numero(Integer numero) {
        this.setNumero(numero);
        return this;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    public String getReferencia() {
        return this.referencia;
    }

    public Distribuidora referencia(String referencia) {
        this.setReferencia(referencia);
        return this;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }

    public String getEstado() {
        return this.estado;
    }

    public Distribuidora estado(String estado) {
        this.setEstado(estado);
        return this;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getDetalhes() {
        return this.detalhes;
    }

    public Distribuidora detalhes(String detalhes) {
        this.setDetalhes(detalhes);
        return this;
    }

    public void setDetalhes(String detalhes) {
        this.detalhes = detalhes;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Distribuidora)) {
            return false;
        }
        return getId() != null && getId().equals(((Distribuidora) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Distribuidora{" +
            "id=" + getId() +
            ", nome='" + getNome() + "'" +
            ", cep='" + getCep() + "'" +
            ", cidade='" + getCidade() + "'" +
            ", bairro='" + getBairro() + "'" +
            ", rua='" + getRua() + "'" +
            ", numero=" + getNumero() +
            ", referencia='" + getReferencia() + "'" +
            ", estado='" + getEstado() + "'" +
            ", detalhes='" + getDetalhes() + "'" +
            "}";
    }
}
