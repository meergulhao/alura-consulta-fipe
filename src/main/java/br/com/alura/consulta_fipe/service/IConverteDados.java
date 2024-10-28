package br.com.alura.consulta_fipe.service;

public interface IConverteDados {
    <T> T obterDados(String json, Class<T> classe);
}
