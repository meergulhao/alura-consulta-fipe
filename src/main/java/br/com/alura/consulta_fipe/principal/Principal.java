package br.com.alura.consulta_fipe.principal;

import br.com.alura.consulta_fipe.model.DadosVeiculo;
import br.com.alura.consulta_fipe.service.ConsumoApi;
import br.com.alura.consulta_fipe.service.ConverteDados;

import java.util.Scanner;

public class Principal {

    private Scanner leitura = new Scanner(System.in);
    private ConsumoApi consumo = new ConsumoApi();
    private ConverteDados conversor = new ConverteDados();

    private final String ENDERECO = "https://parallelum.com.br/fipe/api/v1/carros/marcas/25/modelos/9600/anos/2024-1";

    public void consultaFipe() {
        System.out.println("Vamos buscar um veículo na tabela FIPE!");
//        System.out.println("Digite o tipo de veículo desejado: ");

        var json = consumo.obterDados(ENDERECO);
        System.out.println(json);

        DadosVeiculo dadosVeiculo = conversor.obterDados(json, DadosVeiculo.class);
        System.out.println(dadosVeiculo);
    }
}
