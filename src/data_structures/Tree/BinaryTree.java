package data_structures.Tree;

import data_structures.LinkedList.Ordered.LinkedListOrdered;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.LinkedList;

import static data_structures.ShallowOrDeepCopy.ShallowOrDeepCopy.verifyAndCopy;

/**
 * A classe 'ArvoreBinaria’ representa uma árvore binária de busca genérica, onde o tipo de dado armazenado nos nós
 * deve implementar a interface ’Comparable’.
 *
 * @param <T> O tipo de dado armazenado na árvore, que deve implementar a interface `Comparable`.
 */
public class BinaryTree<T extends Comparable<T>> implements Cloneable {

    public Node<T> root;

    /**
     * Construtor padrão que inicializa uma árvore binária vazia.
     */
    public BinaryTree() {
        root = null;
    }

    /**
     * Construtor que cria uma nova árvore binária a partir de um nó raiz.
     *
     * @param root O nó raiz da nova árvore binária.
     * @throws IllegalArgumentException Se a raiz fornecida for nula.
     */
    public BinaryTree(Node<T> root) {
        if (root == null) throw new IllegalArgumentException("Raiz nula");
        this.root = new Node<>(root);
    }

    /**
     * Retorna o nó raiz da árvore binária.
     *
     * @return O nó raiz da árvore binária.
     */
    public Node<T> getRoot() {
        return root;
    }

    /**
     * Insere um novo valor na árvore binária.
     *
     * @param valor O valor a ser inserido.
     * @throws IllegalArgumentException Se o valor fornecido for nulo ou repetido.
     */
    @SuppressWarnings("unchecked")
    public void inserir(T valor) {
        if (valor == null) throw new IllegalArgumentException("Valor nulo");
        if (contem(valor)) throw new IllegalArgumentException("Valor repetido"); // Verifica se já existe
        Node<T> novoNode = (Node<T>) verifyAndCopy(new Node<>(valor));
        root = inserir(root, novoNode);
    }

    /**
     * metodo recursivo privado para inserir um valor em um nó da árvore.
     * Se o nó atual for nulo, um novo nó é criado com o valor fornecido.
     * Caso contrário, o valor é comparado com o valor do nó atual:
     * - Se o valor for menor, ele é inserido na subárvore esquerda.
     * - Se o valor for maior, ele é inserido na subárvore direita.
     *
     * @param nodeAtual O nó atual da recursão.
     * @param novoNode  O novo nó a ser inserido.
     * @return O nó atual, potencialmente modificado após a inserção.
     */
    private Node<T> inserir(Node<T> nodeAtual, Node<T> novoNode) {
        if (nodeAtual == null) return novoNode; // Se árvore vazia, retorna o novo nó
        if (novoNode.getValor().compareTo(nodeAtual.getValor()) < 0)
            nodeAtual.setEsquerda(inserir(nodeAtual.getEsquerda(), novoNode));
        else if (novoNode.getValor().compareTo(nodeAtual.getValor()) > 0)
            nodeAtual.setDireita(inserir(nodeAtual.getDireita(), novoNode));
        return nodeAtual; // Se valor é igual, não faz nada (evita duplicatas)
    }

    /**
     * Remove um valor da árvore binária.
     *
     * @param valor O valor a ser removido.
     * @throws Exception Se o valor não for encontrado na árvore.
     */
    public void remova(T valor) throws Exception {
        if (valor == null) throw new Exception("Informação ausente");
        root = remova(root, valor);
    }

    /**
     * metodo recursivo privado para remover um valor da árvore.
     * Este metodo implementa a lógica de remoção de um nó de uma árvore binária de busca,
     * considerando os diferentes casos:
     * <p>- Caso 1: Nó folha (sem filhos).</p>
     * <p>- Caso 2: Nó com apenas um filho.</p>
     * <p>- Caso 3: Nó com dois filhos.</p>
     *
     * @param nodeAtual O nó atual a ser verificado.
     * @param valor   O valor a ser removido.
     * @return O nó atual após a remoção do valor.
     * @throws Exception Se o valor não for encontrado na árvore.
     */
    private Node<T> remova(Node<T> nodeAtual, T valor) throws Exception {
        if (nodeAtual == null) throw new Exception("Valor não encontrado na árvore.");

        if (valor.compareTo(nodeAtual.getValor()) < 0) {
            // Caso 1: Valor menor que o nó atual - pesquisa na subárvore esquerda
            nodeAtual.setEsquerda(remova(nodeAtual.getEsquerda(), valor));
        } else if (valor.compareTo(nodeAtual.getValor()) > 0) {
            // Caso 2: Valor maior que o nó atual - pesquisa na subárvore direita
            nodeAtual.setDireita(remova(nodeAtual.getDireita(), valor));
            return nodeAtual; // Retorna o nó atual - o valor a ser removido não está na subárvore esquerda
        }
        // Caso 3: Valor encontrado no nó atual
        // Caso 3.1: Nó folha (sem filhos) - remove o nó
        if (nodeAtual.getEsquerda() == null && nodeAtual.getDireita() == null) return null;
            // Caso 3.2: Nó com apenas um filho - substitui o nó pelo seu único filho
        else if (nodeAtual.getEsquerda() == null) return nodeAtual.getDireita();
        else if (nodeAtual.getDireita() == null) return nodeAtual.getEsquerda();
        else {
            // Caso 3.3: Nó com dois filhos - encontra o sucessor (menor valor na subárvore direita)
            T sucessor = getMenor(nodeAtual.getDireita()); // Substitui o valor do nó atual pelo sucessor
            nodeAtual.setValor(sucessor);// Remove o sucessor da subárvore direita
            nodeAtual.setDireita(remova(nodeAtual.getDireita(), sucessor));
        }

        // Retorna o nó atual - o nó foi removido ou atualizado
        return nodeAtual;
    }

    /**
     * Inicia a travessia in-order na árvore binária.
     *
     * @return Uma string contendo os valores da travessia in-order.
     */
    public String inOrderTraverse() {
        StringBuilder resultado = new StringBuilder();
        inOrderTraverse(root, resultado);
        return resultado.toString().trim();
    }

    /**
     * Realiza a travessia in-order na árvore binária.
     * Nesta travessia, os 'nós' são visitados na seguinte ordem: esquerda, raiz, direita.
     *
     * @param node        O nó atual da recursão.
     * @param resultado O StringBuilder onde o resultado da travessia será armazenado.
     */
    private void inOrderTraverse(Node<T> node, StringBuilder resultado) {
        if (node != null) {
            inOrderTraverse(node.getEsquerda(), resultado);
            resultado.append(node.getValor()).append(" ");
            inOrderTraverse(node.getDireita(), resultado);
        }
    }

    /**
     * Inicia a travessia pre-order na árvore binária.
     *
     * @return Uma string contendo os valores da travessia pre-order.
     */
    public String preOrderTraverse() {
        StringBuilder resultado = new StringBuilder();
        preOrderTraverse(root, resultado);
        return resultado.toString().trim();
    }

    /**
     * Realiza a travessia pre-order na árvore binária.
     * Nesta travessia, os 'nós' são visitados na seguinte ordem: raiz, esquerda, direita.
     *
     * @param node        O nó atual da recursão.
     * @param resultado O StringBuilder onde o resultado da travessia será armazenado.
     */
    private void preOrderTraverse(Node<T> node, StringBuilder resultado) {
        if (node != null) {
            resultado.append(node.getValor()).append(" ");
            preOrderTraverse(node.getEsquerda(), resultado);
            preOrderTraverse(node.getDireita(), resultado);
        }
    }

    /**
     * Inicia a travessia post-order na árvore binária.
     *
     * @return Uma string contendo os valores da travessia post-order.
     */
    public String postOrderTraverse() {
        StringBuilder resultado = new StringBuilder();
        postOrderTraverse(root, resultado);
        return resultado.toString().trim();
    }

    /**
     * Realiza a travessia post-order na árvore binária.
     * Nesta travessia, os 'nós' são visitados na seguinte ordem: esquerda, direita, raiz.
     *
     * @param node        O nó atual da recursão.
     * @param resultado O StringBuilder onde o resultado da travessia será armazenado.
     */
    private void postOrderTraverse(Node<T> node, StringBuilder resultado) {
        if (node != null) {
            postOrderTraverse(node.getEsquerda(), resultado);
            postOrderTraverse(node.getDireita(), resultado);
            resultado.append(node.getValor()).append(" ");
        }
    }
//-------------------------------------------------------------------------

    /**
     * Inicia a travessia in-order na árvore binária e retorna o resultado como uma data_structures.LinkedList.
     *
     * @return Uma data_structures.LinkedList contendo os valores da travessia in-order.
     */
    public LinkedList<T> inOrderToList() {
        LinkedList<T> lista = new LinkedList<>();
        inOrderToList(root, lista);
        return lista;
    }

    /**
     * Realiza a travessia in-order na árvore binária e armazena o resultado em uma data_structures.LinkedList.
     *
     * @param node    O nó atual da recursão.
     * @param lista A data_structures.LinkedList onde o resultado da travessia será armazenado.
     */
    private void inOrderToList(Node<T> node, LinkedList<T> lista) {
        if (node != null) {
            inOrderToList(node.getEsquerda(), lista);
            lista.add(node.getValor());
            inOrderToList(node.getDireita(), lista);
        }
    }

    /**
     * Inicia a travessia pre-order na árvore binária e retorna o resultado como uma data_structures.LinkedList.
     *
     * @return Uma data_structures.LinkedList contendo os valores da travessia pre-order.
     */
    public LinkedList<T> preOrderToList() {
        LinkedList<T> lista = new LinkedList<>();
        preOrderToList(root, lista);
        return lista;
    }

    /**
     * Realiza a travessia pre-order na árvore binária e armazena o resultado em uma data_structures.LinkedList.
     *
     * @param node    O nó atual da recursão.
     * @param lista A data_structures.LinkedList onde o resultado da travessia será armazenado.
     */
    private void preOrderToList(Node<T> node, LinkedList<T> lista) {
        if (node != null) {
            lista.add(node.getValor());
            preOrderToList(node.getEsquerda(), lista);
            preOrderToList(node.getDireita(), lista);
        }
    }

    /**
     * Inicia a travessia post-order na árvore binária e retorna o resultado como uma data_structures.LinkedList.
     *
     * @return Uma data_structures.LinkedList contendo os valores da travessia post-order.
     */
    public LinkedList<T> postOrderToList() {
        LinkedList<T> lista = new LinkedList<>();
        postOrderToList(root, lista);
        return lista;
    }

    /**
     * Realiza a travessia post-order na árvore binária e armazena o resultado em uma data_structures.LinkedList.
     *
     * @param node    O nó atual da recursão.
     * @param lista A data_structures.LinkedList onde o resultado da travessia será armazenado.
     */
    private void postOrderToList(Node<T> node, LinkedList<T> lista) {
        if (node != null) {
            postOrderToList(node.getEsquerda(), lista);
            postOrderToList(node.getDireita(), lista);
            lista.add(node.getValor());
        }
    }

//------------------------------------------------------------------------------------

    /**
     * Inicia a travessia in-order na árvore binária e retorna o resultado como um array.
     *
     * @return Um array contendo os valores da travessia in-order.
     */
    @SuppressWarnings("unchecked")
    public T[] inOrderToArray() {
        ArrayList<T> lista = new ArrayList<>();
        inOrderToArray(root, lista);
        return lista.toArray((T[]) Array.newInstance(lista.getFirst().getClass(), lista.size()));
    }

    /**
     * Realiza a travessia in-order na árvore binária e armazena o resultado em um ArrayList.
     *
     * @param node    O nó atual da recursão.
     * @param lista O ArrayList onde o resultado da travessia será armazenado.
     */
    private void inOrderToArray(Node<T> node, ArrayList<T> lista) {
        if (node != null) {
            inOrderToArray(node.getEsquerda(), lista);
            lista.add(node.getValor());
            inOrderToArray(node.getDireita(), lista);
        }
    }

    /**
     * Inicia a travessia pre-order na árvore binária e retorna o resultado como um array.
     *
     * @return Um array contendo os valores da travessia pre-order.
     */
    @SuppressWarnings("unchecked")
    public T[] preOrderToArray() {
        ArrayList<T> lista = new ArrayList<>();
        preOrderToArray(root, lista);
        return lista.toArray((T[]) Array.newInstance(lista.getFirst().getClass(), lista.size()));
    }

    /**
     * Realiza a travessia pre-order na árvore binária e armazena o resultado em um ArrayList.
     *
     * @param node    O nó atual da recursão.
     * @param lista O ArrayList onde o resultado da travessia será armazenado.
     */
    private void preOrderToArray(Node<T> node, ArrayList<T> lista) {
        if (node != null) {
            lista.add(node.getValor());
            preOrderToArray(node.getEsquerda(), lista);
            preOrderToArray(node.getDireita(), lista);
        }
    }

    /**
     * Inicia a travessia post-order na árvore binária e retorna o resultado como um array.
     *
     * @return Um array contendo os valores da travessia post-order.
     */
    @SuppressWarnings("unchecked")
    public T[] postOrderToArray() {
        ArrayList<T> lista = new ArrayList<>();
        postOrderToArray(root, lista);
        return lista.toArray((T[]) Array.newInstance(lista.getFirst().getClass(), lista.size()));
    }

    /**
     * Realiza a travessia post-order na árvore binária e armazena o resultado em um ArrayList.
     *
     * @param node    O nó atual da recursão.
     * @param lista O ArrayList onde o resultado da travessia será armazenado.
     */
    private void postOrderToArray(Node<T> node, ArrayList<T> lista) {
        if (node != null) {
            postOrderToArray(node.getEsquerda(), lista);
            postOrderToArray(node.getDireita(), lista);
            lista.add(node.getValor());
        }
    }

    /**
     * Verifica se a árvore contém um determinado valor.
     *
     * @param valor O valor a ser procurado.
     * @return true se o valor estiver presente na árvore, false caso contrário.
     */
    public boolean contem(T valor) {
        return contem(root, valor);
    }

    /**
     * metodo recursivo privado para verificar se um valor está presente em um nó da árvore.
     *
     * @param nodeAtual O nó atual da recursão.
     * @param valor   O valor a ser procurado.
     * @return true se o valor estiver presente na árvore, false caso contrário.
     */
    private boolean contem(Node<T> nodeAtual, T valor) {
        if (nodeAtual == null) return false;
        if (valor.compareTo(nodeAtual.getValor()) == 0) return true;
        if (valor.compareTo(nodeAtual.getValor()) < 0) return contem(nodeAtual.getEsquerda(), valor);
        else return contem(nodeAtual.getDireita(), valor);
    }

    /**
     * Retorna o menor valor presente na árvore.
     *
     * @return O menor valor presente na árvore.
     * @throws Exception Se a árvore estiver vazia.
     */
    public T getMenor() throws Exception {
        if (root == null) throw new Exception("A árvore está vazia.");
        return getMenor(root);
    }

    /**
     * metodo recursivo privado para encontrar o menor valor na subárvore a partir de um determinado nó.
     *
     * @param node O nó atual da recursão.
     * @return O menor valor presente na subárvore.
     */
    @SuppressWarnings("unchecked")
    private T getMenor(Node<T> node) {
        if (node.getEsquerda() == null)
            return (T) verifyAndCopy(node.getValor());
        return getMenor(node.getEsquerda());
    }

    /**
     * Retorna o maior valor presente na árvore.
     *
     * @return O maior valor presente na árvore.
     * @throws Exception Se a árvore estiver vazia.
     */
    public T getMaior() throws Exception {
        if (root == null) throw new Exception("A árvore está vazia.");
        return getMaior(root);
    }

    /**
     * metodo recursivo privado para encontrar o maior valor na subárvore a partir de um determinado nó.
     *
     * @param node O nó atual da recursão.
     * @return O maior valor presente na subárvore.
     */
    @SuppressWarnings("unchecked")
    private T getMaior(Node<T> node) {
        if (node.getDireita() == null)
            return (T) verifyAndCopy(node.getValor());
        return getMaior(node.getDireita());
    }

    /**
     * Verifica se a árvore está balanceada, ou seja, se a diferença de altura entre as subárvores esquerda e direita
     * de cada nó é no máximo 1.
     *
     * @return `true` se a árvore estiver balanceada, `false` caso contrário.
     */
    public boolean estaBalanceada() {
        return estaBalanceada(root); // Inicia a verificação a partir da raiz da árvore.
    }

    /**
     * Metodo recursivo privado para verificar se um nó está balanceado.
     * Este metodo calcula a altura das subárvores esquerda e direita e verifica se a diferença
     * entre elas é menor ou igual a 1. A verificação é realizada recursivamente para todos os nós da árvore.
     *
     * @param node O nó a ser verificado.
     * @return `true` se o nó estiver balanceado, `false` caso contrário.
     */
    private boolean estaBalanceada(Node<T> node) {
        if (node == null) return true;

        int alturaEsquerda = altura(node.getEsquerda());
        int alturaDireita = altura(node.getDireita());

        return Math.abs(alturaEsquerda - alturaDireita) <= 1 &&
               estaBalanceada(node.getEsquerda()) &&
               estaBalanceada(node.getDireita());
    }

    /**
     * Realiza uma rotação à esquerda em um determinado nó.
     * A rotação à esquerda é uma operação que muda a estrutura da árvore
     * movendo o filho direito do nó para cima e o nó atual para baixo e para a esquerda.
     *
     * @param node O nó a ser rotacionado.
     * @return O novo nó raiz após a rotação.
     */
    private Node<T> rotacaoEsquerda(Node<T> node) {
        Node<T> novaRaiz = node.getDireita();       // 1. O filho direito do nó se torna a nova raiz da subárvore.
        node.setDireita(novaRaiz.getEsquerda());  // 2. O filho esquerdo da nova raiz se torna o filho direito do nó antigo.
        novaRaiz.setEsquerda(node);               // 3. O nó antigo se torna o filho esquerdo da nova raiz.
        return novaRaiz;
    }

    /**
     * Realiza uma rotação à direita em um determinado nó.
     * A rotação à direita é uma operação que muda a estrutura da árvore
     * movendo o filho esquerdo do nó para cima e o nó atual para baixo e para a direita.
     *
     * @param node O nó a ser rotacionado.
     * @return O novo nó raiz após a rotação.
     */
    private Node<T> rotacaoDireita(Node<T> node) {
        Node<T> novaRaiz = node.getEsquerda();      // 1. O filho esquerdo do nó se torna a nova raiz da subárvore.
        node.setEsquerda(novaRaiz.getDireita());  // 2. O filho direito da nova raiz se torna o filho esquerdo do nó antigo.
        novaRaiz.setDireita(node);                // 3. O nó antigo se torna o filho direito da nova raiz.
        return novaRaiz;
    }

    /**
     * Balanceia a árvore binária utilizando o metodo ’balancear’.
     * Este metodo percorre a árvore recursivamente e realiza rotações
     * para garantir que a árvore esteja balanceada, ou seja, que a diferença
     * de altura entre as subárvores esquerda e direita de cada nó seja no máximo 1.
     */
    public void balancear() {
        root = balancear(root);
    }

    /**
     * Balanceia a subárvore a partir de um determinado nó usando rotações.
     * Este metodo utiliza as rotações à esquerda e à direita para balancear a árvore.
     * Ele verifica a diferença de altura entre as subárvores esquerda e direita e
     * realiza as rotações necessárias para garantir o balanceamento.
     *
     * @param node O nó a partir do qual a subárvore será balanceada.
     * @return O novo nó raiz da subárvore balanceada.
     */
    private Node<T> balancear(Node<T> node) {
        if (node == null) return null;

        // Balanceia as subárvores esquerda e direita.
        node.setEsquerda(balancear(node.getEsquerda()));
        node.setDireita(balancear(node.getDireita()));

        // Calcula o fator de balanceamento do nó atual.
        int fatorBalanceamento = altura(node.getEsquerda()) - altura(node.getDireita());

        // Se a subárvore esquerda é mais alta que a direita por mais de 1 nível.
        if (fatorBalanceamento > 1) {
            // Verifica se é necessária uma rotação dupla à direita (LR).
            if (altura(node.getEsquerda().getDireita()) > altura(node.getEsquerda().getEsquerda())) {
                node.setEsquerda(rotacaoEsquerda(node.getEsquerda()));
            }
            // Realiza a rotação à direita.
            node = rotacaoDireita(node);
        }
        // Se a subárvore direita é mais alta que a esquerda por mais de 1 nível.
        else if (fatorBalanceamento < -1) {
            // Verifica se é necessária uma rotação dupla à esquerda (RL).
            if (altura(node.getDireita().getEsquerda()) > altura(node.getDireita().getDireita())) {
                node.setDireita(rotacaoDireita(node.getDireita()));
            }
            // Realiza a rotação à esquerda.
            node = rotacaoEsquerda(node);
        }

        return node; // Retorna o nó balanceado.
    }

    /**
     * Transforma a árvore binária em um data_structures.LinkedList usando um percurso em ordem.
     *
     * @return O data_structures.LinkedList contendo os valores da árvore em ordem.
     */
    public LinkedListOrdered<T> toLinkedList() {
        LinkedListOrdered<T> lista = new LinkedListOrdered<>();
        toLinkedList(root, lista);
        return lista;
    }

    /**
     * metodo recursivo privado para realizar o percurso em ordem e adicionar os valores ao data_structures.LinkedList.
     *
     * @param node    O nó atual da recursão.
     * @param lista O data_structures.LinkedList que irá armazenar os valores da árvore.
     */
    private void toLinkedList(Node<T> node, LinkedListOrdered<T> lista) {
        if (node != null) {
            toLinkedList(node.getEsquerda(), lista);
            lista.add(node.getValor());
            toLinkedList(node.getDireita(), lista);
        }
    }

    public boolean estaEspelhado(BinaryTree<T> arvore) {
        return estaEspelhado(root, arvore.root);
    }

    private boolean estaEspelhado(Node<T> node1, Node<T> node2) {
        if (node1 == null && node2 == null) return true;
        if (node1 == null || node2 == null) return false;
        return node1.getValor().equals(node2.getValor()) &&
               estaEspelhado(node1.getEsquerda(), node2.getDireita()) &&
               estaEspelhado(node1.getDireita(), node2.getEsquerda());
    }

    /**
     * Espelha a árvore binária, trocando as subárvores esquerda e direita de cada nó recursivamente.
     */
    public void espelhar() {
        espelhar(root);
    }

    /**
     * metodo recursivo privado para espelhar a subárvore a partir de um determinado nó.
     *
     * @param node O nó a partir do qual a subárvore será espelhada.
     */
    private void espelhar(Node<T> node) {
        if (node == null) return;

        Node<T> temp = node.getEsquerda();

        node.setEsquerda(node.getDireita());
        node.setDireita(temp);

        espelhar(node.getEsquerda());
        espelhar(node.getDireita());
    }

    /**
     * Encontra o nó que contém um determinado valor.
     *
     * @param valor O valor a ser procurado.
     * @return O nó que contém o valor, ou ’null’ se o valor não estiver presente na árvore.
     * @throws IllegalArgumentException Se o valor fornecido for nulo.
     */
    public Node<T> achar(T valor) {
        if (valor == null) throw new IllegalArgumentException("Valor nulo");
        return achar(root, valor);
    }

    /**
     * metodo recursivo privado para encontrar o nó que contém um valor na subárvore a partir de um determinado nó.
     *
     * @param nodeAtual O nó atual da recursão.
     * @param valor   O valor a ser procurado.
     * @return O nó que contém o valor, ou ’null` se o valor não estiver presente na subárvore.
     */
    private Node<T> achar(Node<T> nodeAtual, T valor) {
        if (nodeAtual == null) return null;
        if (valor.compareTo(nodeAtual.getValor()) == 0) return nodeAtual;
        if (valor.compareTo(nodeAtual.getValor()) < 0) return achar(nodeAtual.getEsquerda(), valor);
        else return achar(nodeAtual.getDireita(), valor);
    }

    /**
     * Conta o número de elementos em comum entre esta árvore binária e outra árvore binária
     * fornecida como parâmetro, utilizando os metodos `contem` e `compareTo`.
     *
     * @param arvore A árvore binária a ser comparada com esta árvore.
     * @return O número de elementos em comum entre as duas árvores binárias.
     */
    public int elementosEmComum(BinaryTree<T> arvore) {
        return contarElementosEmComum(arvore, this.root);
    }

    /**
     * metodo recursivo auxiliar que percorre os nós da árvore e verifica se os valores estão
     * contidos na outra árvore.
     *
     * @param node     O nó atual da árvore que está sendo percorrida.
     * @param arvore A árvore na qual os valores serão procurados.
     * @return O número de elementos em comum encontrados.
     */
    private int contarElementosEmComum(BinaryTree<T> arvore, Node<T> node) {
        if (node == null) return 0;

        int emComum = 0;

        // Verifica se o valor do nó atual está contido na outra árvore
        if (arvore.contem(node.getValor())) emComum++;

        // Recursão para os filhos esquerdo e direito
        emComum += contarElementosEmComum(arvore, node.getEsquerda());
        emComum += contarElementosEmComum(arvore, node.getDireita());

        return emComum;
    }

    /**
     * Retorna a altura da árvore, o número de nós no caminho mais longo da raiz até uma folha.
     *
     * @return A altura da árvore.
     */
    public int altura() {
        return altura(root);
    }

    /**
     * metodo recursivo privado para calcular a altura da subárvore a partir de um determinado nó.
     *
     * @param node O nó a partir do qual a altura será calculada.
     * @return A altura da subárvore.
     */
    private int altura(Node<T> node) {
        if (node == null) return -1;

        int alturaEsquerda = altura(node.getEsquerda());
        int alturaDireita = altura(node.getDireita());

        return 1 + Math.max(alturaEsquerda, alturaDireita);
    }

    /**
     * Retorna a profundidade de um determinado nó na árvore, o qual é o número de nós no caminho da raiz até o nó.
     *
     * @param node O nó cuja profundidade será calculada.
     * @return A profundidade do nó, ou -1 se o nó não estiver presente na árvore.
     */
    public int profundidade(Node<T> node) {
        return profundidade(root, node, 0);
    }

    /**
     * metodo recursivo privado para calcular a profundidade de um nó na subárvore a partir de um determinado nó.
     *
     * @param nodeAtual           O nó atual da recursão.
     * @param nodeProcurado       O nó cuja profundidade será calculada.
     * @param profundidadeAtual A profundidade atual da recursão.
     * @return A profundidade do nó, ou -1 se o nó não estiver presente na subárvore.
     */
    private int profundidade(Node<T> nodeAtual, Node<T> nodeProcurado, int profundidadeAtual) {
        if (nodeAtual == null || nodeAtual == nodeProcurado) return profundidadeAtual;

        int profundidadeEsquerda = profundidade(nodeAtual.getEsquerda(), nodeProcurado, profundidadeAtual + 1);
        if (profundidadeEsquerda != -1) return profundidadeEsquerda;

        return profundidade(nodeAtual.getDireita(), nodeProcurado, profundidadeAtual + 1);
    }

    /**
     * Verifica se a árvore está vazia.
     *
     * @return ’true` se a árvore estiver vazia, `false` caso contrário.
     */
    public boolean estaVazio() {
        return root == null;
    }

    /**
     * Remove todos os nós da árvore, tornando-a vazia.
     */
    public void limpar() {
        root = null;
    }

    /**
     * Retorna o número de nós na árvore.
     *
     * @return O número de nós na árvore.
     */
    public int tamanho() {
        if (root == null) return 0;
        return tamanho(root);
    }

    /**
     * metodo recursivo privado para calcular o número de nós na subárvore a partir de um determinado nó.
     *
     * @param node O nó a partir do qual o número de nós será calculado.
     * @return O número de nós na subárvore.
     */
    private int tamanho(Node<T> node) {
        if (node == null) return 0;
        if (node.getEsquerda() == null && node.getDireita() == null) return 1;
        if (node.getEsquerda() == null && node.getDireita() != null) return 1 + tamanho(node.getDireita());
        if (node.getEsquerda() != null && node.getDireita() == null) return 1 + tamanho(node.getEsquerda());
        return 1 + tamanho(node.getEsquerda()) + tamanho(node.getDireita());
    }

    /**
     * Construtor de cópia que cria uma nova árvore binária como uma cópia profunda de outra árvore.
     *
     * @param modelo A árvore binária a ser copiada.
     * @throws IllegalArgumentException Se a árvore fornecida for nula.
     */
    @SuppressWarnings("unchecked")
    public BinaryTree(BinaryTree<T> modelo) {
        if (modelo == null) throw new IllegalArgumentException("Modelo nulo");
        this.root = (Node<T>) verifyAndCopy(modelo.root);
    }

    /**
     * Cria e retorna uma cópia profunda da árvore binária.
     *
     * @return Uma cópia profunda da árvore binária.
     */
    @Override
    public Object clone() {
        BinaryTree<T> clone = null;
        try {
            clone = new BinaryTree<>(this);
        } catch (Exception ignored) {
        }
        return clone;
    }

    /**
     * Verifica se a árvore binária é igual a outro objeto.
     * Duas árvores binárias são consideradas iguais se possuem a mesma estrutura e os mesmos valores em cada nó.
     *
     * @param obj O objeto a ser comparado.
     * @return ’true’ se os objetos forem iguais, ‘false’ caso contrário.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (this.getClass() != obj.getClass()) return false;

        BinaryTree<?> that = (BinaryTree<?>) obj;
        return equals(this.root, that.root);
    }

    /**
     * metodo recursivo privado para verificar se dois nós somos iguais, incluindo suas subárvores.
     *
     * @param node1 O primeiro nó a ser comparado.
     * @param node2 O segundo nó a ser comparado.
     * @return ’true’ se os nós forem iguais, ’false` caso contrário.
     */
    private boolean equals(Node<?> node1, Node<?> node2) {
        if (node1 == node2) return true;
        if (node1 == null || node2 == null) return false;

        if (!node1.getValor().equals(node2.getValor())) return false;

        return equals(node1.getEsquerda(), node2.getEsquerda()) &&
               equals(node1.getDireita(), node2.getDireita());
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        return hashCode(root, prime);
    }

    private int hashCode(Node<T> raiz, int prime) {
        if (raiz == null) return 0;

        int result = raiz.getValor().hashCode();
        result *= prime + hashCode(raiz.getEsquerda(), prime);
        result *= prime + hashCode(raiz.getDireita(), prime);
        return result;
    }

    @Override
    public String toString() {
        if (root == null) return "{ }";
        return "\n" + toString(0, root, "", true, new StringBuilder());
    }

    /**
     * metodo recursivo privado para construir a representação textual da subárvore a partir de um determinado nó.
     *
     * @param level         O nível atual da recursão, representando a profundidade do nó.
     * @param node            O nó atual da recursão.
     * @param prefixo       O prefixo a ser adicionado à representação do nó.
     * @param isUltimoFilho `true` se o nó for o último filho de seu pai, `false` caso contrário.
     * @param sb            O StringBuilder usado para construir a representação textual.
     * @return A representação textual da subárvore.
     */
    private String toString(int level, Node<T> node, String prefixo, boolean isUltimoFilho, StringBuilder sb) {
        sb.append(prefixo);
        if (level == 0) // Condição para a raiz
            sb.append("[");
        else
            sb.append(isUltimoFilho ? "└─" : "├─").append("[");

        sb.append(node.getValor()).append("]").append("\n");

        String prefixoFilho = prefixo + (isUltimoFilho ? "  " : "│   ");

        if (node.getEsquerda() != null)
            toString(level + 1, node.getEsquerda(), prefixoFilho, false, sb);

        if (node.getDireita() != null)
            toString(level + 1, node.getDireita(), prefixoFilho, true, sb);

        return sb.toString();
    }
}