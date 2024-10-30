package br.com.alura.consulta_fipe.principal;

import br.com.alura.consulta_fipe.model.Dados;
import br.com.alura.consulta_fipe.model.DadosVeiculo;
import br.com.alura.consulta_fipe.model.Modelos;
import br.com.alura.consulta_fipe.service.ConsumoApi;
import br.com.alura.consulta_fipe.service.ConverteDados;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Principal {

    private Scanner leitura = new Scanner(System.in);
    private ConsumoApi consumo = new ConsumoApi();
    private ConverteDados conversor = new ConverteDados();

    private final String ENDERECO_BASE = "https://parallelum.com.br/fipe/api/v1/";

    // https://parallelum.com.br/fipe/api/v1/carros/marcas/25/modelos/9600/anos/2024-1

    public void consultaFipe() throws JsonProcessingException {
        System.out.println("Vamos buscar um veículo na tabela FIPE!");
        System.out.println("""
                ===| OPÇÕES |===
                Carro
                Moto
                Caminhão
                
                Digite a opção desejada:
                """);

        // Tipo de veículo
        var opcao = leitura.nextLine();
        String endereco;

        if (opcao.toLowerCase().contains("carr")) {
            endereco = ENDERECO_BASE + "carros/marcas";
        } else if (opcao.toLowerCase().contains("mot")) {
            endereco = ENDERECO_BASE + "motos/marcas";
        } else {
            endereco = ENDERECO_BASE + "caminhores/marcas";
        }

        var json = consumo.obterDados(endereco);

        // Cria lista de marcas
        var marcas = conversor.obterLista(json, Dados.class);
        System.out.println("\nMarcas disponíveis para busca:");
        marcas.stream()
                .map(m -> "Código: " + m.codigo() + "\t\tNome: " + m.nome())
                .forEach(System.out::println);

        // Marca
        System.out.println("Digite o código da marca para a busca: ");
        var codigoMarca = leitura.nextLine();
        endereco += "/" + codigoMarca + "/modelos";

        json = consumo.obterDados(endereco);

        // Cria lista de modelos
        var modelosLista = conversor.obterDados(json, Modelos.class);
        System.out.println("\nModelos dessa marca:");
        modelosLista.modelos().stream()
                .map(m -> "Código: " + m.codigo() + "\t\tNome: " + m.nome())
                .forEach(System.out::println);

        // Modelo
        System.out.println("Digite um trecho do modelo para a busca: ");
        var nomeVeiculo = leitura.nextLine();

        List<Dados> modelosFiltrados = modelosLista.modelos().stream()
                .filter(m -> m.nome().toLowerCase().contains(nomeVeiculo.toLowerCase()))
                        .collect(Collectors.toList());

        System.out.println("\nModelos filtrados:");
        modelosFiltrados.stream()
                .map(m -> "Código: " + m.codigo() + "\t\tNome: " + m.nome())
                .forEach(System.out::println);

        System.out.println("Digite o código do modelo para a busca: ");
        var codigoModelo = leitura.nextLine();
        endereco += "/" + codigoModelo + "/anos";

        json = consumo.obterDados(endereco);

        // Cria lista de anos
        List<Dados> anos = conversor.obterLista(json, Dados.class);

        // Cria lista de veículos
        List<DadosVeiculo> listaVeiculos = new ArrayList<>();

        // Consulta cada ano do modelo
        String enderecoAno;
        for (Dados ano : anos) {
            enderecoAno = endereco.concat("/" + ano.codigo());
            json = consumo.obterDados(enderecoAno);
            DadosVeiculo dadosVeiculo = conversor.obterDados(json, DadosVeiculo.class);

            // Adiciona os veículos na lista
            listaVeiculos.add(dadosVeiculo);
        }

        // Exibe a lista
        System.out.println("\nLista de veículos:");
        listaVeiculos.forEach(System.out::println);

    }
}
