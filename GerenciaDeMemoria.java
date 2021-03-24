import java.util.LinkedList;

public class GerenciaDeMemoria {
    
    static Integer tabela_de_paginas[] = {2, 1, 3, 4, 0, 6, null, null, null, 5, null, 7, null, null, null, null}; //Valores da tabela de página conforme o desafio
    static LinkedList<Pagina> lista_de_paginas = new LinkedList<Pagina>(); //Uma lista com as páginas que estão na memória virtual
    static int tamanho_da_pagina = 4096; //Em bytes
    static int qntdPageFrames = 8; //Quantidade de páginas que cabem na memória
    public static void main(String[] args) {
        estadoInicial(); //Colocando as páginas informadas no desafio na lista

        int enderecoA = 46100;
        int enderecoB = 27123;

        //Imprime o cálculo dos valores solicitados no desafio
        System.out.println("Calculando endereço físico de " + enderecoA + "...");
        enderecoFisico(enderecoA);
        System.out.println("Calculando endereço físico de " + enderecoB + "...");
        enderecoFisico(enderecoB);
    }

    /**
     * Checa se o valor solicitado já está na memória física
     * 
     * Caso já tenha sido, ou seja, o valor do índice na tebela é diferente de null, vai para um método que calculará o valor de endereçamento físico
     * Porém, caso ainda não tenha sido e a memória física já esteja cheia de páginas, serão chamados dois algortimos de substituição de página
     * o FIFO e a Segunda Chance 
     * 
     * @param endereco Valor do endereço virtual
     */
    private static void enderecoFisico(int endereco) {
        int p = endereco / tamanho_da_pagina;

        if (tabela_de_paginas[p] != null) {
            System.out.println(calcularEndereco(endereco, p));
        } else {
            if(lista_de_paginas.size() == qntdPageFrames){
                System.out.println("Falta de páginas. Recalculando o valor do endereço físico com os seguintes algoritmos de substituição de páginas: ");
                System.out.println("FIFO: " + fifo(endereco, p));
                System.out.println("Segunda Chance: " + segundaChance(endereco, p));
            }
        }
    }

    /**
     * Faz a operação de cálculo de endereço físico, conforme o algoritmo explicado pelo professor Carlos na aula
     *  
     * @param endereco Valor do endereço virtual
     * @param p É o índice na tabela de páginas para 'endereco'
     * @return Uma string indicando qual o endereço físico do endereço virtual 'endereco'
     */
    private static String calcularEndereco(int endereco, int p) {
        int d = endereco % tamanho_da_pagina;
        int f = tabela_de_paginas[p];
        int enderecoFisico = (f * tamanho_da_pagina) + d;

        return ("O endereço físico é " + enderecoFisico);
    }

    /**
     * 
     * Checará os valores na lista de páginas, caso a primeira página tenha o valor do bit R igual a 1
     * será colocada no final da lista com o bit R mudado para 0. Caso o valor do bit R já seja 0, estamos em uma
     * configuração de lista em que o FIFO e a Segunda Chance teriam o mesmo resultado. 
     * 
     * @param endereco Valor do endereço virtual
     * @param p É o índice na tabela de páginas para 'endereco'
     * @return Chamada do método FIFO, pois ele removerá o primeiro elemento da lista de páginas, que ao final do método segundaChance
     * certamente será uma página com bir R igual a 0. 
     */
    private static String segundaChance(int endereco, int p) {
        Pagina aux = lista_de_paginas.getFirst();

        while(aux.r == 1){
            Pagina pagClone = lista_de_paginas.removeFirst();
            pagClone.r = 0;
            lista_de_paginas.addLast(pagClone);
            aux = lista_de_paginas.getFirst();
        }

        return fifo(endereco, p);
    }

    /**
     * 
     * O algoritmo consiste em substituir a página mais antiga da lista, e para tanto,
     * remove a página mais antiga da lista, modifica a tabela de páginas ao apagar a referência
     * do índice da página que foi substituída e acrescentar o índice da nova página que será colocada
     * na memória com o número de moldura da página substituída.  
     * 
     * Obs: Aqui foi usado um clone da lista de páginas para não interferir no cálculo do algoritmo de segunda chance
     * 
     * @param endereco Valor do endereço virtual
     * @param p É o índice na tabela de páginas para 'endereco'
     * @return Chama o método para o cálculo do endereço físico da nova página que será salva na memória
     */
    private static String fifo(int endereco, int p) {
        LinkedList<Pagina> cloneLista_de_paginas =  (LinkedList<Pagina>) lista_de_paginas.clone();
        Pagina aux = (Pagina) cloneLista_de_paginas.removeFirst();
        
        tabela_de_paginas[aux.endLogico] = null;
        tabela_de_paginas[p] = aux.endFisico;
        
        //Ao final do algorítmo deveria adicionar a nova página no final da lista de páginas, 
        //porém para não interferir com o cálculo do algortimo de segunda chance, isso não será feito.

        //Pagina newPagina = new Pagina(p, aux.endFisico, 1);
        //lista_de_paginas.add(newPagina);
        return calcularEndereco(endereco, p);
    }

    private static void estadoInicial() {
        /**
        * A classe página possui três campos
        * O primeiro indicando o p - seu índice lógico
        * O segundo indicando o f - seu índice físico
        * O terceiro indicando o bit R.
        */
        Pagina pag1 = new Pagina(0, 2, 1);
        Pagina pag2 = new Pagina(1, 1, 1);
        Pagina pag3 = new Pagina(2, 3, 0);
        Pagina pag4 = new Pagina(3, 4, 1);
        Pagina pag5 = new Pagina(4, 0, 0);
        Pagina pag6 = new Pagina(5, 6, 1);
        Pagina pag7 = new Pagina(9, 5, 0);
        Pagina pag8 = new Pagina(11, 7, 0);

        lista_de_paginas.add(pag1);
        lista_de_paginas.add(pag2);
        lista_de_paginas.add(pag3);
        lista_de_paginas.add(pag4);
        lista_de_paginas.add(pag5);
        lista_de_paginas.add(pag6);
        lista_de_paginas.add(pag7);
        lista_de_paginas.add(pag8);
    }
}

