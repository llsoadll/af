package com.universidad.gestion_estudiante.dto;

public class EstadisticasDTO {
    private double promedioNotas;
    private int cantidadRegulares;
    private int cantidadPromocionados;
    private int cantidadTotal;
    private double porcentajePromocionados;
    private double notaModa;
    private double promedioPrimerCuatrimestre;
    private double promedioSegundoCuatrimestre;
    private int[] distribucionNotas;
    private int totalEstudiantes;
    

    // Getters y Setters

    public int getTotalEstudiantes() {
        return totalEstudiantes;
    }

    public void setTotalEstudiantes(int totalEstudiantes) {
        this.totalEstudiantes = totalEstudiantes;
    }

    public double getPromedioNotas() {
        return promedioNotas;
    }

    public void setPromedioNotas(double promedioNotas) {
        this.promedioNotas = promedioNotas;
    }

    public int getCantidadRegulares() {
        return cantidadRegulares;
    }

    public void setCantidadRegulares(int cantidadRegulares) {
        this.cantidadRegulares = cantidadRegulares;
    }

    public int getCantidadPromocionados() {
        return cantidadPromocionados;
    }

    public void setCantidadPromocionados(int cantidadPromocionados) {
        this.cantidadPromocionados = cantidadPromocionados;
    }

    public int getCantidadTotal() {
        return cantidadTotal;
    }

    public void setCantidadTotal(int cantidadTotal) {
        this.cantidadTotal = cantidadTotal;
    }

    public double getPorcentajePromocionados() {
        return porcentajePromocionados;
    }

    public void setPorcentajePromocionados(double porcentajePromocionados) {
        this.porcentajePromocionados = porcentajePromocionados;
    }

    public double getNotaModa() {
        return notaModa;
    }

    public void setNotaModa(double notaModa) {
        this.notaModa = notaModa;
    }

    public double getPromedioPrimerCuatrimestre() {
        return promedioPrimerCuatrimestre;
    }

    public void setPromedioPrimerCuatrimestre(double promedioPrimerCuatrimestre) {
        this.promedioPrimerCuatrimestre = promedioPrimerCuatrimestre;
    }

    public double getPromedioSegundoCuatrimestre() {
        return promedioSegundoCuatrimestre;
    }

    public void setPromedioSegundoCuatrimestre(double promedioSegundoCuatrimestre) {
        this.promedioSegundoCuatrimestre = promedioSegundoCuatrimestre;
    }

    public int[] getDistribucionNotas() {
        return distribucionNotas;
    }

    public void setDistribucionNotas(int[] distribucionNotas) {
        this.distribucionNotas = distribucionNotas;
    }
}